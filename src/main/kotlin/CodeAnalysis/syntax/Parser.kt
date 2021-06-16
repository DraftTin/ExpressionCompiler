package CodeAnalysis.syntax

import CodeAnalysis.syntax.ExpressionSyntax.*
import java.io.Reader
import java.util.*
import kotlin.collections.ArrayList

/**
 * 语法分析
 */
class Parser {
    private var tokens = ArrayList<SyntaxToken>()
    private var position = 0

    private var diagnostics = LinkedList<String>()

    constructor(data: Reader) {
        var lexer = Lexer(data)
        while(true) {
            val tok = lexer.scan()
            tokens.add(tok)
            if(tok.kind == SyntaxToken.EndOfFileToken.kind) break
        }
        // 将词法分析器所有的错误信息添加到diagnostics中
        this.diagnostics.addAll(lexer.diagnostics)
    }

    /**
     * 返回token
     */
    private fun peek(offset: Int): SyntaxToken {
        var index = position + offset
        if(index >= tokens.size) return tokens[tokens.size - 1]
        return tokens[index]
    }

    private var current: SyntaxToken
        get() {
            return peek(0)
        }
        set(value) {}

    private fun nextToken(): SyntaxToken {
        val tok = current
        ++position
        return tok
    }

    private fun matchToken(kind: TokenKind): SyntaxToken {
        if(current.kind == kind) {
            return nextToken()
        }
        diagnostics.add("ERROR: Unexpected token <${current.kind}>, expected <$kind>.")
        return current
//        return SyntaxToken(kind, kind.toString())
    }

    fun parse(): SyntaxTree {
        var expression = parseExpression()
        var endOfFileToken = matchToken(TokenKind.EOF)
        return SyntaxTree(this.diagnostics, expression, endOfFileToken)
    }

    private fun parseExpression(parentPrecedence: Int = 0): ExpressionSyntax {
        var left: ExpressionSyntax?
        var unaryOperatorPrecedence = SyntaxFacts.getUnaryOperatorPrecedence(current.kind)
        // 单目运算符接表达式，当前运算符优先级较高先运算
        if(unaryOperatorPrecedence != 0 && unaryOperatorPrecedence >= parentPrecedence) {
            var operatorToken = nextToken()
            var operand = parseExpression(unaryOperatorPrecedence)
            left = UnaryExpressionSyntax(operatorToken, operand)
        }
        // 双目运算符接表达式，当前运算符优先级较高先计算
        else {
            left = parsePrimiaryExpression()
        }
        while(true) {
            var precedence = SyntaxFacts.getBinaryOperatorPrecedence(current.kind)
            if (precedence == 0 || precedence <= parentPrecedence) {
                break
            }
            var operatorToken = nextToken()
            var right = parseExpression(precedence)
            left = BinaryExpressionSyntax(left!!, operatorToken, right)
        }
        return left!!
    }

    /**
     * 解析表达式
     */
    private fun parsePrimiaryExpression(): ExpressionSyntax {
        return when (current.kind) {
            TokenKind.OpenParenToken -> parseParenthesizedExpression()
            TokenKind.FalseToken, TokenKind.TrueToken-> parseBooleanLiteral()
            TokenKind.NumberToken -> parseNumberLiteral()
            else -> BadExpressionSyntax(current)
        }
    }

    private fun parseNumberLiteral(): ExpressionSyntax {
        var numberToken = matchToken(TokenKind.NumberToken)
        return LiteralExpressionSyntax(numberToken)
    }

    private fun parseBooleanLiteral(): ExpressionSyntax {
        var booleanToken = matchToken(current.kind)
        return LiteralExpressionSyntax(booleanToken)
    }

    private fun parseParenthesizedExpression(): ExpressionSyntax {
        var left = matchToken(TokenKind.OpenParenToken)
        var mid = parseExpression()
        var right = matchToken(TokenKind.ClosedParenToken)
        return ParenthesizedExpressionSyntax(left, mid, right)
    }
}

fun main() {
    for (i in 0..15) {
        for (j in 0..15) {
            val number = i * 15 + j
            val str = String.format("%3d", number)
            print("\u001b[48;5;${number}m $str")
            print("\u001b[0m")
        }
        println()
    }

}
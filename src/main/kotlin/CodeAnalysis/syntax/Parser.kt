package CodeAnalysis.syntax

import CodeAnalysis.syntax.ExpressionSyntax.*
import java.io.Reader
import java.lang.Exception
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
        var expression = parseExpr()
        var endOfFileToken = matchToken(TokenKind.EOF)
        return SyntaxTree(this.diagnostics, expression, endOfFileToken)
    }

    private fun parseExpr(parentPrecedence: Int = 0): ExpressionSyntax {
        var left: ExpressionSyntax?
        var unaryOperatorPrecedence = SyntaxFacts.getUnaryOperatorPrecedence(current.kind)
        // 单目运算符接表达式，当前运算符优先级较高先运算
        if(unaryOperatorPrecedence != 0 && unaryOperatorPrecedence >= parentPrecedence) {
            var operatorToken = nextToken()
            var operand = parseExpr(unaryOperatorPrecedence)
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
            var right = parseExpr(precedence)
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
        var mid = parseExpr()
        var right = matchToken(TokenKind.ClosedParenToken)
        return ParenthesizedExpressionSyntax(left, mid, right)
    }

    /**
     * 解析条件表达式或表达式@
     * cexpr ::= expr otherCExpr
     * otherCExpr ::= CmpOp cexpr | ε
     */
    private fun parseConditionalExpression(): ExpressionSyntax {
        var left: ExpressionSyntax = parseExpression()
        when(current.kind) {
            TokenKind.EqualToken, TokenKind.NotEqualsToken,
            TokenKind.GE, TokenKind.LE, TokenKind.GT, TokenKind.LT -> {
                var operatorToken = nextToken()
                var right = parseConditionalExpression()
                left = BinaryExpressionSyntax(left, operatorToken, right)
            }
        }
        return left
    }

    /**
     * 解析算数Expression，返回解析后的根
     * Expression ::= Term OtherExpression
     * OtherExpression ::= PLUS Expression | ε
     */
    private fun parseExpression(): ExpressionSyntax {
        var left: ExpressionSyntax = parseTerm()

        when(current.kind) {
            TokenKind.PlusToken, TokenKind.MinusToken, TokenKind.PipePipeToken -> {
                var operatorToken = nextToken()
                var right = parseExpression()
                left = BinaryExpressionSyntax(left, operatorToken, right)
            }
        }
        return left
    }

    /**
     * 解析一个Term返回解析后的根
     * Term ::= factor OtherTerm
     * OtherTerm ::= STAR Term | ε
     */
    private fun parseTerm(): ExpressionSyntax {
        var left: ExpressionSyntax = parseFactor()

        if(current.kind == TokenKind.StarToken ||
                current.kind == TokenKind.SlashToken) {

        }
        when(current.kind) {
            TokenKind.StarToken, TokenKind.SlashToken, TokenKind.AmpersandAmpersandToken -> {
                var operatorToken = nextToken()
                var right = parseTerm()
                left = BinaryExpressionSyntax(left, operatorToken, right)
            }
        }
        return left
    }

    /**
     * 解析一个factor并返回解析后的结果
     * Factor ::= NUMBER | (ConditionalExpression) | MINUS Factor | PLUS Factor | Bang Factor
     */
    private fun parseFactor(): ExpressionSyntax {
        if(current.kind == TokenKind.OpenParenToken) {
            var left = matchToken(TokenKind.OpenParenToken)
//            var mid = parseExpression()
            var mid = parseConditionalExpression()
            var right = matchToken(TokenKind.ClosedParenToken)
            return ParenthesizedExpressionSyntax(left, mid, right)
        }
        if(current.kind == TokenKind.PlusToken || current.kind == TokenKind.MinusToken) {
            var operator = matchToken(current.kind)
            var expr = parseFactor()
            return UnaryExpressionSyntax(operator, expr)
        }
        if(current.kind == TokenKind.NumberToken) {
            var numberToken = matchToken(TokenKind.NumberToken)
            return LiteralExpressionSyntax(numberToken)
        }
        if(current.kind == TokenKind.FalseToken || current.kind == TokenKind.TrueToken) {
            var booleanToken = matchToken(current.kind)
            return LiteralExpressionSyntax(booleanToken)
        }
        if(current.kind == TokenKind.BangToken) {
            var operator = matchToken(TokenKind.BangToken)
            var expr = parseFactor()
            return UnaryExpressionSyntax(operator, expr)
        }
        return BadExpressionSyntax(current)
    }

    /**
     * 解析Program
     * Program ::= ProgramHead DeclarePart ProgramBody
     */
    private fun praseProgram() {

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
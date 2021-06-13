package parser.syntax

import lexer.*
import parser.syntax.ExpressionSyntax.*
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

    fun parse(): SyntaxTree {
        var expression = parseExpression()
        var endOfFileToken = matchToken(TokenKind.EOF)
        return SyntaxTree(this.diagnostics, expression, endOfFileToken)
    }

    /**
     * 解析算数Epxression，返回解析后的根
     * Expression ::= Term OtherExpression
     * OtherExpression ::= PLUS Expression | ε
     */
    private fun parseExpression(): ExpressionSyntax {
        var left: ExpressionSyntax = parseTerm()

        while(current.kind == TokenKind.PLUS ||
                current.kind == TokenKind.MINUS) {
            var operatorToken = nextToken()
            var right = parseExpression()
            left = BinaryExpressionSyntax(left, operatorToken, right)
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

        while(current.kind == TokenKind.STAR ||
                current.kind == TokenKind.SLASH) {
            var operatorToken = nextToken()
            var right = parseTerm()
            left = BinaryExpressionSyntax(left, operatorToken, right)
        }
        return left
    }

    /**
     * 解析一个factor并返回解析后的结果
     * Factor ::= NUMBER | (Expression) | MINUS Factor | PLUS Factor
     */
    private fun parseFactor(): ExpressionSyntax {
        if(current.kind == TokenKind.OpenParenToken) {
            var left = matchToken(TokenKind.OpenParenToken)
            var mid = parseExpression()
            var right = matchToken(TokenKind.ClosedParenToken)
            return ParenthesizedExpressionSyntax(left, mid, right)
        }
        if(current.kind == TokenKind.PLUS || current.kind == TokenKind.MINUS) {
            var operator = matchToken(current.kind)
            var expr = parseFactor()
            return UnaryExpressionSyntax(operator, expr)
        }
        var numberToken = matchToken(TokenKind.NUMBER)
        if(numberToken is NumberToken) {
            return NumberExpressionSyntax(numberToken)
        }
        return BadExpressionSyntax(numberToken)
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
        return SyntaxToken(kind)
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
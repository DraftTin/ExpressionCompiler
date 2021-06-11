package parser

import lexer.Kind
import lexer.Lexer
import lexer.NumberToken
import lexer.Token
import parser.ExpressionSyntax.*
import java.io.Reader
import java.util.*
import kotlin.collections.ArrayList

/**
 * 语法分析
 */
class Parser {
    var tokens = ArrayList<Token>()
    var position = 0

    var diagnostics = LinkedList<String>()

    constructor(data: Reader) {
        var lexer = Lexer(data)
        while(true) {
            val tok = lexer.scan()
            tokens.add(tok)
            if(tok.kind == Token.EndOfFileToken.kind) break
        }
        // 将词法分析器所有的错误信息添加到diagnostics中
        this.diagnostics.addAll(lexer.diagnosics)
    }

    fun parse(): ExpressionSyntax {
        var left: ExpressionSyntax = parsePrimaryExpression()

        while(current.kind == Kind.PLUS ||
                current.kind == Kind.MINUS) {
            var operatorToken = nextToken()
            var right = parsePrimaryExpression()
            left = BinaryExpressionSyntax(left, operatorToken, right)
        }
        return left
    }


    private fun parsePrimaryExpression(): ExpressionSyntax {
        var numberToken = match(Kind.NUMBER)
        return NumberExpressionSyntax(numberToken as NumberToken)
    }

    /**
     * 返回token
     */
    private fun peek(offset: Int): Token {
        var index = position + offset
        if(index >= tokens.size) return tokens[tokens.size - 1]
        return tokens[index]
    }

    private var current: Token
        get() {
            return peek(0)
        }
        set(value) {}

    private fun nextToken(): Token {
        val tok = current
        ++position
        return tok
    }

    private fun match(kind: Kind): Token {
        if(current.kind == kind) {
            return nextToken()
        }
        diagnostics.add("ERROR: Unexpected token <${current.kind}>, expected <$kind>.")
        return Token(kind)
    }
}
package lexer

/**
 * 整数Token
 */
class NumberToken(var value: Int) : SyntaxToken(TokenKind.NUMBER) {
    override fun toString(): String {
        return "<Number: ${this.value}>"
    }
}

package lexer

/**
 * 浮点数Token
 */
class RealToken(var value: Float) : SyntaxToken(TokenKind.FLOAT) {
    override fun toString(): String {
        return "<Real: ${this.value}>"
    }
}

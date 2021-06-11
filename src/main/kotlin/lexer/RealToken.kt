package lexer

/**
 * 浮点数Token
 */
class RealToken(var value: Float) : Token(Kind.FLOAT) {
    override fun toString(): String {
        return "<Real: ${this.value}>"
    }
}

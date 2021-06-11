package lexer

/**
 * 整数Token
 */
class NumberToken(var value: Int) : Token(Kind.NUMBER) {
    override fun toString(): String {
        return "<Number: ${this.value}>"
    }
}

package lexer

class CharToken(val ch: Char): Word(ch.toString(), Kind.CHAR) {
    override fun toString(): String {
        return "<Char: '$ch'>"
    }
}
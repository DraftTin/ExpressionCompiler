package lexer

class CharToken(val ch: Char): Word(ch.toString(), TokenKind.CHAR) {
    override fun toString(): String {
        return "<Char: '$ch'>"
    }
}
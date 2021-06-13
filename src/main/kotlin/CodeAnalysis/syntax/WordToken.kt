package lexer


/**
 * Word Token
 */
open class Word(lexeme: String, kind: TokenKind) : SyntaxToken(kind) {
    val lexeme = lexeme
    override fun toString(): String {
        return "<Word: \"${this.lexeme}\">"
    }

    companion object {
        val eq = Word("=", TokenKind.EQ)
        val gt = Word(">", TokenKind.GT)
        val lt = Word("<", TokenKind.LT)
        val ge = Word(">=", TokenKind.GE)
        val le = Word("<=", TokenKind.LE)
        val lsparen = Word("(", TokenKind.OpenParenToken)
        val rsparen = Word(")", TokenKind.ClosedParenToken)
        val plus = Word("+", TokenKind.PLUS)
        val minus = Word("-", TokenKind.MINUS)
        val times = Word("*", TokenKind.STAR)
        val div = Word("/", TokenKind.SLASH)
        val assign = Word(":=", TokenKind.ASSIGN)
        val lbparen = Word("{", TokenKind.LBPAREN)
        val rbparen = Word("}", TokenKind.RBPAREN)
        val semi = Word(";", TokenKind.SEMI)
        val mod = Word("%", TokenKind.MOD)
        val lbracket = Word("[", TokenKind.LBRACKET)
        val rbracket = Word("]", TokenKind.RBRACKET)
        val comma = Word(",", TokenKind.COMMA)
        val dot = Word(".", TokenKind.DOT)

        val True = Word("true", TokenKind.TRUE)
        val False = Word("false", TokenKind.FALSE)
        val temp = Word("t", TokenKind.TEMP)
    }
}

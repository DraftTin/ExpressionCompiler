package lexer


/**
 * Word Token
 */
open class Word(lexeme: String, kind: Kind) : Token(kind) {
    val lexeme = lexeme
    override fun toString(): String {
        return "<Word: \"${this.lexeme}\">"
    }

    companion object {
        val eq = Word("=", Kind.EQ)
        val gt = Word(">", Kind.GT)
        val lt = Word("<", Kind.LT)
        val ge = Word(">=", Kind.GE)
        val le = Word("<=", Kind.LE)
        val lsparen = Word("(", Kind.LSPAREN)
        val rsparen = Word(")", Kind.RSPAREN)
        val plus = Word("+", Kind.PLUS)
        val minus = Word("-", Kind.MINUS)
        val times = Word("*", Kind.STAR)
        val div = Word("/", Kind.DIV)
        val assign = Word(":=", Kind.ASSIGN)
        val lbparen = Word("{", Kind.LBPAREN)
        val rbparen = Word("}", Kind.RBPAREN)
        val semi = Word(";", Kind.SEMI)
        val mod = Word("%", Kind.MOD)
        val lbracket = Word("[", Kind.LBRACKET)
        val rbracket = Word("]", Kind.RBRACKET)
        val comma = Word(",", Kind.COMMA)
        val dot = Word(".", Kind.DOT)

        val True = Word("true", Kind.TRUE)
        val False = Word("false", Kind.FALSE)
        val temp = Word("t", Kind.TEMP)
    }
}

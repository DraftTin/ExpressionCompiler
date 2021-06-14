package CodeAnalysis.syntax

/**
 * Token原始类型，其他Token继承该token，继承SyntaxNode，方便表示子节点
 */
open class SyntaxToken(kind: TokenKind, var text: String, var value: Any = ""): SyntaxNode(kind) {
    // 该token的类型
    override var kind = kind

    companion object {
        val EndOfFileToken = SyntaxToken(TokenKind.EOF, "end of file")     // EOF

        val eq = SyntaxToken(TokenKind.EqualToken,"=")
        val gt = SyntaxToken(TokenKind.GT,">")
        val lt = SyntaxToken(TokenKind.LT, "<")
        val ge = SyntaxToken(TokenKind.GE, ">=")
        val le = SyntaxToken(TokenKind.LE, "<=")
        val lsparen = SyntaxToken(TokenKind.OpenParenToken, "(")
        val rsparen = SyntaxToken(TokenKind.ClosedParenToken,")")
        val plus = SyntaxToken(TokenKind.PlusToken,"+")
        val minus = SyntaxToken(TokenKind.MinusToken,"-")
        val times = SyntaxToken(TokenKind.StarToken,"*")
        val div = SyntaxToken(TokenKind.SlashToken,"/")
        val assign = SyntaxToken(TokenKind.ASSIGN,":=")
        val lbparen = SyntaxToken(TokenKind.OpenComment,"{")
        val rbparen = SyntaxToken(TokenKind.ClosedComment,"}")
        val semi = SyntaxToken(TokenKind.SEMI,";")
        val mod = SyntaxToken(TokenKind.MOD,"%")
        val lbracket = SyntaxToken(TokenKind.LBRACKET,"[")
        val rbracket = SyntaxToken(TokenKind.RBRACKET,"]")
        val comma = SyntaxToken(TokenKind.COMMA,",")
        val dot = SyntaxToken(TokenKind.DOT,".")
        val ampersandampersand = SyntaxToken(TokenKind.AmpersandAmpersandToken, "&&")
        val pipepipe = SyntaxToken(TokenKind.PipePipeToken, "||")
        val bang = SyntaxToken(TokenKind.BangToken, "!")
    }

    /**
     * 输出token的kind
     */
    override fun toString(): String {
        return "<${this.kind}>"
    }
}
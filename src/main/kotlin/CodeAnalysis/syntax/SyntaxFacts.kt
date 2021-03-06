package CodeAnalysis.syntax

object SyntaxFacts {
    // 返回单目运算符的优先级
    fun getUnaryOperatorPrecedence(kind: TokenKind): Int = when(kind) {
        // 6表示优先级比双目运算符都高
        TokenKind.PlusToken, TokenKind.MinusToken, TokenKind.BangToken -> 6
        else -> 0
    }

    // 返回双目运算符的优先级
    fun getBinaryOperatorPrecedence(kind: TokenKind): Int = when(kind) {
        TokenKind.StarToken, TokenKind.SlashToken -> 5
        TokenKind.PlusToken, TokenKind.MinusToken -> 4
        TokenKind.EqualToken, TokenKind.NotEqualsToken, TokenKind.LT, TokenKind.GE, TokenKind.GT, TokenKind.LE -> 3
        TokenKind.AmpersandAmpersandToken -> 2
        TokenKind.PipePipeToken -> 1
        else -> 0
    }
}
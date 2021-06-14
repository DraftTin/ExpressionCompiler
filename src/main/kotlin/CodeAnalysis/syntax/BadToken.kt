package CodeAnalysis.syntax

/**
 * 未识别的token
 * @param ch 对应未识别的字符
 */
class BadToken(val ch: Char, val msg: String = "bad token"): SyntaxToken(TokenKind.BadToken, ch.toString()) {
    override fun toString(): String {
        return "<BadToken: '$ch'>"
    }
}
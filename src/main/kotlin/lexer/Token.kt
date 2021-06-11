package lexer

import parser.SyntaxNode

/**
 * Token原始类型，其他Token继承该token，继承SyntaxNode，方便表示子节点
 */
open class Token(kind: Kind): SyntaxNode(kind) {
    // 该token的类型
    override var kind = kind

    companion object {
        val EndOfFileToken = Token(Kind.EOF)     // EOF
    }

    /**
     * 输出token的kind
     */
    override fun toString(): String {
        return "<${this.kind}>"
    }
}
package parser.syntax

import lexer.TokenKind

/**
 * 语法树节点类
 */
abstract class SyntaxNode(open var kind: TokenKind) {
    open fun getChildren(): ArrayList<SyntaxNode> {
        return ArrayList()
    }
}


package parser

import lexer.Kind
import lexer.NumberToken
import lexer.Token

/**
 * 语法树节点类
 */
abstract class SyntaxNode(open var kind: Kind) {
    open fun getChildren(): ArrayList<SyntaxNode> {
        return ArrayList()
    }
}

sealed class ExpressionSyntax(kind: Kind): SyntaxNode(kind) {
    data class NumberExpressionSyntax(var numberToken: NumberToken): ExpressionSyntax(Kind.NumberExpression) {
        override fun getChildren(): ArrayList<SyntaxNode> {
           return arrayListOf(numberToken)
        }
    }
    data class BinaryExpressionSyntax(var left: ExpressionSyntax, var operatorToken: Token,
                                      var right: ExpressionSyntax): ExpressionSyntax(Kind.BinaryExpression) {
        override fun getChildren(): ArrayList<SyntaxNode> {
            return arrayListOf(left, operatorToken, right)
        }
    }

}

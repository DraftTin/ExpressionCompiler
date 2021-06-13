package parser.syntax

import lexer.TokenKind
import lexer.NumberToken
import lexer.SyntaxToken

/**
 * 算数表达式节点类
 */
sealed class ExpressionSyntax(kind: TokenKind): SyntaxNode(kind) {
    data class NumberExpressionSyntax(var numberToken: NumberToken): ExpressionSyntax(TokenKind.NumberExpression) {
        override fun getChildren(): ArrayList<SyntaxNode> {
           return arrayListOf(numberToken)
        }
    }
    data class BinaryExpressionSyntax(var left: ExpressionSyntax, var operatorToken: SyntaxToken,
                                      var right: ExpressionSyntax): ExpressionSyntax(TokenKind.BinaryExpression) {
        override fun getChildren(): ArrayList<SyntaxNode> {
            return arrayListOf(left, operatorToken, right)
        }
    }

    data class ParenthesizedExpressionSyntax(var openParenthesisToken: SyntaxToken, var mid: ExpressionSyntax,
                                             var closedParenthesisToken: SyntaxToken): ExpressionSyntax(TokenKind.ParenthesizedExpression) {
        override fun getChildren(): ArrayList<SyntaxNode> {
            return arrayListOf(openParenthesisToken, mid, closedParenthesisToken)
        }
    }

    data class UnaryExpressionSyntax(var operator: SyntaxToken, var expression: ExpressionSyntax):
            ExpressionSyntax(TokenKind.UnaryExpressionSyntax) {
        override fun getChildren(): ArrayList<SyntaxNode> {
            return arrayListOf(operator, expression)
        }
    }

    /**
     * 语法分析发现异常token返回未识别表达式节点
     */
    data class BadExpressionSyntax(var tok: SyntaxToken) : ExpressionSyntax(TokenKind.BadExpressionSyntax) {
        override fun getChildren(): ArrayList<SyntaxNode> {
            return arrayListOf(this.tok)
        }
    }

}
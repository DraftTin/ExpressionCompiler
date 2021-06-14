package CodeAnalysis.syntax

/**
 * 表达式节点类
 */
sealed class ExpressionSyntax(kind: TokenKind): SyntaxNode(kind) {
    data class LiteralExpressionSyntax(var literalToken: SyntaxToken): ExpressionSyntax(TokenKind.LiteralExpression) {
        override fun getChildren(): ArrayList<SyntaxNode> {
           return arrayListOf(literalToken)
        }
    }
    data class BinaryExpressionSyntax(var left: ExpressionSyntax, var operator: SyntaxToken,
                                      var right: ExpressionSyntax): ExpressionSyntax(TokenKind.BinaryExpression) {
        override fun getChildren(): ArrayList<SyntaxNode> {
            return arrayListOf(left, operator, right)
        }
    }

    data class ParenthesizedExpressionSyntax(var openParenthesisToken: SyntaxToken, var mid: ExpressionSyntax,
                                             var closedParenthesisToken: SyntaxToken): ExpressionSyntax(TokenKind.ParenthesizedExpression) {
        override fun getChildren(): ArrayList<SyntaxNode> {
            return arrayListOf(openParenthesisToken, mid, closedParenthesisToken)
        }
    }

    data class UnaryExpressionSyntax(var operator: SyntaxToken, var operand: ExpressionSyntax):
            ExpressionSyntax(TokenKind.UnaryExpressionSyntax) {
        override fun getChildren(): ArrayList<SyntaxNode> {
            return arrayListOf(operator, operand)
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
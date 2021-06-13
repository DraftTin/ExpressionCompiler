package parser

import lexer.TokenKind
import parser.syntax.ExpressionSyntax
import parser.syntax.ExpressionSyntax.*

class Evaluator(var root: ExpressionSyntax) {
    fun evaluate(): Int {
        return evaluateExpression(root)
    }

    /**
     * 返回计算的结果
     */
    private fun evaluateExpression(expression: ExpressionSyntax): Int = when(expression) {
        is NumberExpressionSyntax -> {
            expression.numberToken.value
        }
        is BinaryExpressionSyntax -> {
            var bexpr = expression
            var left = evaluateExpression(bexpr.left)
            var right = evaluateExpression(bexpr.right)
            when(bexpr.operatorToken.kind) {
                TokenKind.PLUS -> left + right
                TokenKind.MINUS -> left - right
                TokenKind.STAR -> left * right
                TokenKind.SLASH -> left  / right
                else -> throw Exception("Unexpected binary operator ${bexpr.operatorToken.kind}")
            }
        }
        is ParenthesizedExpressionSyntax -> {
            var mid = expression.mid
            evaluateExpression(mid)
        }
        is UnaryExpressionSyntax -> {
            var expr = expression.expression
            var operator = expression.operator
            when(operator.kind) {
                TokenKind.PLUS -> evaluateExpression(expr)
                TokenKind.MINUS -> 0 - evaluateExpression(expr)
                else -> throw Exception("Unexpected unary operator ${operator.kind}")
            }
        }

        is BadExpressionSyntax -> {
            throw Exception("Can't parse BadExpressionSyntax")
        }
    }
}

fun main() {
    var a: Int = 0
    println("123".toInt())
    a += readLine()!!.toInt()
    println(a)
}
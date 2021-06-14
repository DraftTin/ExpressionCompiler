package CodeAnalysis

import CodeAnalysis.Binding.BoundBinaryOperatorKind
import CodeAnalysis.Binding.BoundExpression
import CodeAnalysis.Binding.BoundExpression.*
import CodeAnalysis.Binding.BoundUnaryOperatorKind
import CodeAnalysis.syntax.TokenKind
import CodeAnalysis.syntax.ExpressionSyntax
import CodeAnalysis.syntax.ExpressionSyntax.*

class Evaluator(var root: BoundExpression) {
    fun evaluate(): Any {
        return evaluateExpression(root)
    }

    /**
     * 返回计算的结果
     */
    private fun evaluateExpression(expression: BoundExpression): Any = when(expression) {
        is BoundNumberExpression -> {
            expression.value as Int
        }
        is BoundBinaryExpression -> {
            var bexpr = expression
            var left = evaluateExpression(bexpr.left) as Int
            var right = evaluateExpression(bexpr.right) as Int
            when(bexpr.operatorKind) {
                BoundBinaryOperatorKind.Addition -> left + right
                BoundBinaryOperatorKind.Subtraction -> left - right
                BoundBinaryOperatorKind.Multiplication -> left * right
                BoundBinaryOperatorKind.Division -> left  / right
            }
        }
//        is ParenthesizedExpressionSyntax -> {
//            var mid = expression.mid
//            evaluateExpression(mid)
//        }
        is BoundUnaryExpression -> {
            var expr = expression.operand
            var operatorKind = expression.operatorKind
            when(operatorKind) {
                BoundUnaryOperatorKind.Identity -> evaluateExpression(expr) as Int
                BoundUnaryOperatorKind.Negation -> 0 - evaluateExpression(expr) as Int
                else -> throw Exception("Unexpected unary operator ${operatorKind}")
            }
        }
        else -> {
            throw Exception("Can't parse BadExpressionSyntax")
        }
//        is BadExpressionSyntax -> {
//            throw Exception("Can't parse BadExpressionSyntax")
//        }
    }
}

fun main() {
    var a: Int = 0
    println("123".toInt())
    a += readLine()!!.toInt()
    println(a)
}
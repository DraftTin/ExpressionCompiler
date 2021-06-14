package CodeAnalysis

import CodeAnalysis.Binding.BoundBinaryOperatorKind
import CodeAnalysis.Binding.BoundExpression
import CodeAnalysis.Binding.BoundExpression.*
import CodeAnalysis.Binding.BoundUnaryOperatorKind

class Evaluator(var root: BoundExpression) {
    fun evaluate(): Any {
        return evaluateExpression(root)
    }

    /**
     * 返回计算的结果
     */
    private fun evaluateExpression(expression: BoundExpression): Any = when(expression) {
        is BoundLiteralExpression -> {
            expression.value
        }
        is BoundBinaryExpression -> {
            var bexpr = expression
            var left = evaluateExpression(bexpr.left)
            var right = evaluateExpression(bexpr.right)
            when(bexpr.operatorKind) {
                BoundBinaryOperatorKind.Addition -> left as Int + right as Int
                BoundBinaryOperatorKind.Subtraction -> left as Int - right as Int
                BoundBinaryOperatorKind.Multiplication -> left as Int * right as Int
                BoundBinaryOperatorKind.Division -> left as Int  / right as Int
                BoundBinaryOperatorKind.LogicalAnd -> left as Boolean && right as Boolean
                BoundBinaryOperatorKind.LogicalOr -> left as Boolean || right as Boolean
                BoundBinaryOperatorKind.Equation -> left == right
            }
        }
//        is ParenthesizedExpressionSyntax -> {
//            var mid = expression.mid
//            evaluateExpression(mid)
//        }
        is BoundUnaryExpression -> {
            var expr = expression.operand
            var operatorKind = expression.operatorKind
            var operand = evaluateExpression(expr)
            when(operatorKind) {
                BoundUnaryOperatorKind.Identity -> operand as Int
                BoundUnaryOperatorKind.Negation -> 0 - operand as Int
                BoundUnaryOperatorKind.LogicalNegation -> !(operand as Boolean)
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
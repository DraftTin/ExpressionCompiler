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
    private fun evaluateExpression(expression: BoundExpression): Any = when (expression) {
        is BoundLiteralExpression -> {
            expression.value
        }
        is BoundBinaryExpression -> {
            var bexpr = expression
            var left = evaluateExpression(bexpr.left)
            var right = evaluateExpression(bexpr.right)
            when (bexpr.op.boundKind) {
                // 算数运算符
                BoundBinaryOperatorKind.Addition -> left as Int + right as Int
                BoundBinaryOperatorKind.Subtraction -> left as Int - right as Int
                BoundBinaryOperatorKind.Multiplication -> left as Int * right as Int
                BoundBinaryOperatorKind.Division -> left as Int / right as Int
                // 逻辑运算符
                BoundBinaryOperatorKind.LogicalAnd -> left as Boolean && right as Boolean
                BoundBinaryOperatorKind.LogicalOr -> left as Boolean || right as Boolean
                // 比较运算符
                BoundBinaryOperatorKind.Equals -> left == right
                BoundBinaryOperatorKind.NotEquals -> left != right
                BoundBinaryOperatorKind.GreaterThan -> when{
                    left is Int && right is Int -> left > right
                    left is Char && right is Char -> left > right
                    else -> throw Exception("Unknown Error")
                }
                BoundBinaryOperatorKind.LessThan -> when {
                    left is Int && right is Int -> left < right
                    left is Char && right is Char -> left < right
                    else -> throw Exception("Unknown Error")
                }
                BoundBinaryOperatorKind.GreaterEquals -> when{
                    left is Int && right is Int -> left >= right
                    left is Char && right is Char -> left >= right
                    else -> throw Exception("Unknown Error")
                }
                BoundBinaryOperatorKind.LessEquals -> when{
                    left is Int && right is Int -> left <= right
                    left is Char && right is Char -> left <= right
                    else -> throw Exception("Unknown Error")
                }
            }
        }
//        is ParenthesizedExpressionSyntax -> {
//            var mid = expression.mid
//            evaluateExpression(mid)
//        }
        is BoundUnaryExpression -> {
            var expr = expression.operand
            var operatorKind = expression.op.boundKind
            var operand = evaluateExpression(expr)
            when (operatorKind) {
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
    println("asd".compareTo("asd"))
    var a: Int = 0
    println("123".toInt())
    a += readLine()!!.toInt()
    println(a)
}
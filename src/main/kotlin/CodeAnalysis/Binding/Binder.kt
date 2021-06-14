package CodeAnalysis.Binding

import CodeAnalysis.Binding.BoundExpression.*
import CodeAnalysis.syntax.ExpressionSyntax
import CodeAnalysis.syntax.ExpressionSyntax.*
import CodeAnalysis.syntax.TokenKind
import java.lang.Exception
import java.util.*
import kotlin.reflect.KClass

class Binder() {
    var diagnostics = LinkedList<String>()

    fun bindExpression(syntax: ExpressionSyntax): BoundExpression = when(syntax) {
        is LiteralExpressionSyntax -> bindLiteralExpression(syntax)
        is UnaryExpressionSyntax -> bindUnaryExpression(syntax)
        is BinaryExpressionSyntax -> bindBinaryExpression(syntax)
        is ParenthesizedExpressionSyntax -> bindExpression(syntax.mid)
        is BadExpressionSyntax -> bindBadExpression(syntax)
    }

    private fun bindBadExpression(syntax: BadExpressionSyntax): BoundExpression {
        return BoundBadExpression(syntax.tok)
    }

    private fun bindLiteralExpression(syntax: LiteralExpressionSyntax): BoundExpression {
        return BoundLiteralExpression(syntax.literalToken.value)
    }

    private fun bindBinaryExpression(syntax: BinaryExpressionSyntax): BoundExpression {
        var left = bindExpression(syntax.left)
        var right = bindExpression(syntax.right)
        var leftClassType = left.getClassType()
        var rightClassType = right.getClassType()
        var operator: BoundBinaryOperator? = BoundBinaryOperator.bind(syntax.operator.kind, leftClassType, rightClassType)
        if(operator == null) {
            diagnostics.add("Binary operator ${syntax.operator} is not defined for ${prettyClassName(leftClassType)} and ${prettyClassName(rightClassType)}")
            return left
        }
        return BoundBinaryExpression(left, operator, right)
    }

    /**
     * 获取将UnaryExpression绑定后的结果
     */
    private fun bindUnaryExpression(syntax: UnaryExpressionSyntax): BoundExpression{
        var operand = bindExpression(syntax.operand)
        var operandClassType = operand.getClassType()
        var operator: BoundUnaryOperator? = BoundUnaryOperator.bind(syntax.operator.kind, operandClassType)
        if(operator == null) {
            diagnostics.add("Unary operator ${syntax.operator} is not defined for ${prettyClassName(operandClassType)}")
            return operand
        }
        return BoundUnaryExpression(operator, operand)
    }

    private fun prettyClassName(classType: KClass<out Any>) = when(classType){
        Int::class -> "int"
        Float::class -> "float"
        Boolean::class -> "bool"
        else -> "garbage"
    }
}
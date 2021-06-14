package CodeAnalysis.Binding

import CodeAnalysis.Binding.BoundExpression.*
import CodeAnalysis.syntax.ExpressionSyntax
import CodeAnalysis.syntax.ExpressionSyntax.*
import CodeAnalysis.syntax.TokenKind
import java.util.*
import kotlin.reflect.KClass

class Binder() {
    var diagnostics = LinkedList<String>()

    fun bindExpression(syntax: ExpressionSyntax): BoundExpression = when(syntax) {
        is NumberExpressionSyntax -> bindNumberExpression(syntax)
        is UnaryExpressionSyntax -> bindUnaryExpression(syntax)
        is BinaryExpressionSyntax -> bindBinaryEression(syntax)
        is BadExpressionSyntax -> bindBadExpression(syntax)
        else -> throw Exception("Unexpected syntax ${syntax.kind}")
    }

    private fun bindBadExpression(syntax: BadExpressionSyntax): BoundExpression {
        return BoundBadExpression(syntax.tok)
    }

    private fun bindNumberExpression(syntax: NumberExpressionSyntax): BoundExpression {
        return BoundNumberExpression(syntax.numberToken.value)
    }

    private fun bindBinaryEression(syntax: BinaryExpressionSyntax): BoundExpression {
        var left = bindExpression(syntax.left)
        var right = bindExpression(syntax.right)
        var leftClassType = left.getClassType()
        var rightClassType = right.getClassType()
        var operator: BoundBinaryOperatorKind? = getBoundBinaryOperatorKind(syntax.operator.kind, leftClassType, rightClassType)
        if(operator == null) {
            diagnostics.add("Binary operator ${syntax.operator} is not defined for ${prettyClassName(leftClassType)} and ${prettyClassName(rightClassType)}")
            return left
        }
        return BoundBinaryExpression(left, operator, right)
    }

    /**
     * 返回BoundBinaryOperatorKind，如果出现类型不匹配的情况，返回null
     */
    private fun getBoundBinaryOperatorKind(opeartorKind: TokenKind, leftClassType: KClass<out Any>,
                                           rightClassType: KClass<out Any>): BoundBinaryOperatorKind? {
        if(leftClassType != Int::class || rightClassType != Int::class) {
            return null
        }
        when(opeartorKind) {
            TokenKind.PLUS -> return BoundBinaryOperatorKind.Addition
            TokenKind.MINUS -> return BoundBinaryOperatorKind.Subtraction
            TokenKind.STAR -> return BoundBinaryOperatorKind.Multiplication
            TokenKind.SLASH -> return BoundBinaryOperatorKind.Division
            else -> throw Exception("Unexpected unary operator $opeartorKind")
        }
    }

    private fun bindUnaryExpression(syntax: UnaryExpressionSyntax): BoundExpression{
        var operand = bindExpression(syntax.operand)
        var operandClassType = operand.getClassType()
        var operator: BoundUnaryOperatorKind? = getBoundUnaryOperatorKind(syntax.operator.kind, operandClassType)
        if(operator == null) {
            diagnostics.add("Unary operator ${syntax.operator} is not defined for ${prettyClassName(operandClassType)}")
            return operand
        }
        return BoundUnaryExpression(operator, operand)
    }

    private fun getBoundUnaryOperatorKind(operatorKind: TokenKind, classType: KClass<out Any>): BoundUnaryOperatorKind? {
        if(classType != Int::class) {
            return null
        }
        when(operatorKind) {
            TokenKind.PLUS -> return BoundUnaryOperatorKind.Identity
            TokenKind.MINUS -> return BoundUnaryOperatorKind.Negation
            else -> throw Exception("Unexpected binary operator $operatorKind")
        }
    }

    private fun prettyClassName(classType: KClass<out Any>) = when(classType){
        Int::class -> "int"
        Float::class -> "float"
        Boolean::class -> "bool"
        else -> "void"
    }
}
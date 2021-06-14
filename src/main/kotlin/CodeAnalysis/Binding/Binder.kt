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
    private fun getBoundBinaryOperatorKind(operatorKind: TokenKind, leftClassType: KClass<out Any>,
                                           rightClassType: KClass<out Any>): BoundBinaryOperatorKind? {
        if(leftClassType == Int::class && rightClassType == Int::class) {
            when(operatorKind) {
                TokenKind.PlusToken -> return BoundBinaryOperatorKind.Addition
                TokenKind.MinusToken -> return BoundBinaryOperatorKind.Subtraction
                TokenKind.StarToken -> return BoundBinaryOperatorKind.Multiplication
                TokenKind.SlashToken -> return BoundBinaryOperatorKind.Division
                TokenKind.EqualToken -> return BoundBinaryOperatorKind.Equation
            }
        }
        if(leftClassType == Boolean::class && rightClassType == Boolean::class) {
            when(operatorKind) {
                TokenKind.AmpersandAmpersandToken -> return BoundBinaryOperatorKind.LogicalAnd
                TokenKind.PipePipeToken -> return BoundBinaryOperatorKind.LogicalOr
                TokenKind.EqualToken -> return BoundBinaryOperatorKind.Equation
            }
        }
        return null

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
        if(classType == Int::class) {
            when(operatorKind) {
                TokenKind.PlusToken -> return BoundUnaryOperatorKind.Identity
                TokenKind.MinusToken -> return BoundUnaryOperatorKind.Negation
            }
        }
        if(classType == Boolean::class) {
            when(operatorKind) {
                TokenKind.BangToken -> return BoundUnaryOperatorKind.LogicalNegation
            }
        }
        return null
    }

    private fun prettyClassName(classType: KClass<out Any>) = when(classType){
        Int::class -> "int"
        Float::class -> "float"
        Boolean::class -> "bool"
        else -> "void"
    }
}
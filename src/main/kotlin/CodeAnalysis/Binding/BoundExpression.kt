package CodeAnalysis.Binding

import CodeAnalysis.syntax.SyntaxToken
import kotlin.reflect.KClass

sealed class BoundExpression(kind: BoundNodeKind): BoundNode(kind) {
    data class BoundNumberExpression(var value: Any): BoundExpression(BoundNodeKind.NumberExpression) {
        override fun getClassType(): KClass<out Any> {
            return value::class
        }
    }

    data class BoundUnaryExpression(var operatorKind: BoundUnaryOperatorKind, var operand: BoundExpression): BoundExpression(BoundNodeKind.UnaryExpression) {
        override fun getClassType(): KClass<out Any> {
            return operand.getClassType()
        }
    }

    data class BoundBinaryExpression(var left: BoundExpression, var operatorKind: BoundBinaryOperatorKind,
                                     var right: BoundExpression): BoundExpression(BoundNodeKind.BinaryExpression) {
        override fun getClassType(): KClass<out Any> {
            return left.getClassType()
        }
    }

    class BoundBadExpression(var tok: SyntaxToken) : BoundExpression(BoundNodeKind.BadExpression) {
        override fun getClassType(): KClass<out Any> {
            return tok.value::class
        }
    }

}
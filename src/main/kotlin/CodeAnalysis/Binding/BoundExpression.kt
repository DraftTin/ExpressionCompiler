package CodeAnalysis.Binding

import CodeAnalysis.syntax.SyntaxToken
import CodeAnalysis.syntax.TokenKind
import kotlin.reflect.KClass

sealed class BoundExpression(kind: BoundNodeKind): BoundNode(kind) {
    data class BoundLiteralExpression(var value: Any): BoundExpression(BoundNodeKind.NumberExpression) {
        override fun getClassType(): KClass<out Any> {
            return value::class
        }
    }

    data class BoundUnaryExpression(var op: BoundUnaryOperator, var operand: BoundExpression): BoundExpression(BoundNodeKind.UnaryExpression) {
        // 由操作符返回类型
        override fun getClassType(): KClass<out Any> {
            return op.resultType
        }
    }

    data class BoundBinaryExpression(var left: BoundExpression, var op: BoundBinaryOperator,
                                     var right: BoundExpression): BoundExpression(BoundNodeKind.BinaryExpression) {
        // 由操作符返回类型
        override fun getClassType(): KClass<out Any> {
            return op.resultType
        }
    }

    class BoundBadExpression(var tok: SyntaxToken) : BoundExpression(BoundNodeKind.BadExpression) {
        override fun getClassType(): KClass<out Any> {
            return tok.value::class
        }
    }
}
package CodeAnalysis.Binding

import CodeAnalysis.syntax.TokenKind
import kotlin.reflect.KClass

/**
 * 根据操作数和操作符确定元素的类型
 */
class BoundBinaryOperator {
    var resultType: KClass<out Any>     // 运算结果类型，用于返回
    var rightType: KClass<out Any>      // 右边表达式的类型
    var leftType: KClass<out Any>       // 左边表达式的类型
    var boundKind: BoundBinaryOperatorKind   // 该运算符的Bound类型
    var syntaxKind: TokenKind           // 该运算符的Token类型，方便对照识别

    constructor(syntaxKind: TokenKind, kind: BoundBinaryOperatorKind, type: KClass<out Any>)
            : this(syntaxKind, kind, type, type, type)

    constructor(syntaxKind: TokenKind, kind: BoundBinaryOperatorKind, leftType: KClass<out Any>,
                rightType: KClass<out Any>, resultType: KClass<out Any>) {
        this.syntaxKind = syntaxKind
        this.boundKind = kind
        this.leftType = leftType
        this.rightType = rightType
        this.resultType = resultType
    }

    // 已存在的几种operators
    companion object {
        var operators: ArrayList<BoundBinaryOperator> = arrayListOf(
                // 算数运算符
                BoundBinaryOperator(TokenKind.PlusToken, BoundBinaryOperatorKind.Addition, Int::class),
                BoundBinaryOperator(TokenKind.MinusToken, BoundBinaryOperatorKind.Subtraction, Int::class),
                BoundBinaryOperator(TokenKind.StarToken, BoundBinaryOperatorKind.Multiplication, Int::class),
                BoundBinaryOperator(TokenKind.SlashToken, BoundBinaryOperatorKind.Division, Int::class),
                // 逻辑运算符
                BoundBinaryOperator(TokenKind.AmpersandAmpersandToken, BoundBinaryOperatorKind.LogicalAnd, Boolean::class),
                BoundBinaryOperator(TokenKind.PipePipeToken, BoundBinaryOperatorKind.LogicalOr, Boolean::class),
                BoundBinaryOperator(TokenKind.EqualToken, BoundBinaryOperatorKind.Equals, Boolean::class),
                BoundBinaryOperator(TokenKind.EqualToken, BoundBinaryOperatorKind.Equals, Int::class, Int::class, Boolean::class),
                BoundBinaryOperator(TokenKind.NotEqualsToken, BoundBinaryOperatorKind.NotEquals, Boolean::class),
                BoundBinaryOperator(TokenKind.NotEqualsToken, BoundBinaryOperatorKind.NotEquals, Int::class, Int::class, Boolean::class),
                BoundBinaryOperator(TokenKind.GE, BoundBinaryOperatorKind.GreaterEquals, Int::class, Int::class, Boolean::class),
                BoundBinaryOperator(TokenKind.GT, BoundBinaryOperatorKind.GreaterThan, Int::class, Int::class, Boolean::class),
                BoundBinaryOperator(TokenKind.LE, BoundBinaryOperatorKind.LessEquals, Int::class, Int::class, Boolean::class),
                BoundBinaryOperator(TokenKind.LT, BoundBinaryOperatorKind.LessThan, Int::class, Int::class, Boolean::class),
        )

        /**
         * 返回表达式对应的operator，如果类型不匹配则返回null
         */
        fun bind(syntaxKind: TokenKind, leftType: KClass<out Any>, rightType: KClass<out Any>): BoundBinaryOperator? {
            for(op in operators) {
                if(op.syntaxKind == syntaxKind && op.leftType == leftType && op.rightType == rightType) {
                    return op
                }
            }
            return null
        }
    }
}
package CodeAnalysis.Binding

import CodeAnalysis.syntax.TokenKind
import kotlin.reflect.KClass

/**
 * 单目运算符，用于返回运算后的数据类型
 */
class BoundUnaryOperator {
    constructor(syntaxKind: TokenKind, kind: BoundUnaryOperatorKind, type: KClass<out Any>): this(syntaxKind, kind, type, type)

    constructor(syntaxKind: TokenKind, kind: BoundUnaryOperatorKind, operandType: KClass<out Any>, resultType: KClass<out Any>) {
        this.syntaxKind = syntaxKind        // 运算符Token类型
        this.boundKind = kind               // 运算符Bound类型
        this.operandType = operandType      // 操作数类型
        this.resultType = resultType        // 运算结果类型，用于返回
    }

    var resultType: KClass<out Any>
    var operandType: KClass<out Any>
    var boundKind: BoundUnaryOperatorKind
    var syntaxKind: TokenKind

    companion object {
        var operators: ArrayList<BoundUnaryOperator> = arrayListOf(
                BoundUnaryOperator(TokenKind.PlusToken, BoundUnaryOperatorKind.Identity, Int::class),
                BoundUnaryOperator(TokenKind.MinusToken, BoundUnaryOperatorKind.Negation, Int::class),
                BoundUnaryOperator(TokenKind.BangToken, BoundUnaryOperatorKind.LogicalNegation, Boolean::class)
        )

        /**
         * 返回表达式对应的operator，如果类型不匹配则返回null
         */
        fun bind(syntaxKind: TokenKind, operandType: KClass<out Any>): BoundUnaryOperator? {
            for(op in operators) {
                if(op.syntaxKind == syntaxKind && op.operandType == operandType) {
                    return op
                }
            }
            return null
        }
    }
}
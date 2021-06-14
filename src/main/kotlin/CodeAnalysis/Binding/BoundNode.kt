package CodeAnalysis.Binding

import kotlin.reflect.KClass

/**
 * 携带类型信息的结点
 */
abstract class BoundNode(open var kind: BoundNodeKind) {
    /**
     * 返回类型信息，如表达式返回该表达式计算的整数或布尔类型
     */
    abstract fun getClassType(): KClass<out Any>
}
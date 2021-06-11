package inter

import lexer.Token
import symbols.Type

/**
 * 三地址指令的右部
 * @param tok 操作符的Token或临时变量
 * @param p 该操作符的类型
 */
open class Expr(tok: Token, p: Type): Node() {
    val op: Token
    var type: Type

    init {
        op = tok
        type = p
    }

    // 返回三指令的右部
    open fun gen(): Expr { return this }
    // 将表达式计算为一个单一的地址并返回
    open fun reduce(): Expr { return this }

    /**
     * @param t 布尔表达式为true时的出口
     * @param f 布尔表达式为false时的出口
     * 特殊标号0 表示穿越该Expr到下一条指令
     */
    fun jumping(t: Int, f: Int) {
        emitJumps(toString(), t, f)
    }

    fun emitJumps(test: String, t: Int, f: Int) {
        if(t != 0 && f != 0) {
            emit("if $test goto L$t")
            emit("goto L$f")
        }
        else if(t != 0) emit("if $test goto L $t")
        else if(f != 0) emit("iffalse $test goto L $f")
        else {}  // nothing hanppened
    }

    override fun toString(): String {
        return op.toString()
    }

}
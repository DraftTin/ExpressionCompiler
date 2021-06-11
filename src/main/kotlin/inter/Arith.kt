package inter

import lexer.Token
import symbols.Type

/**
 * 双目运算符
 * @param tok 表示该类型的此法单元
 * @param x1 操作数1
 * @param x2 操作数2
 * null 占位符, 构造的时候不能确定两个操作数最终的类型，需要调用Type.max来判断
 */
class Arith(tok: Token, x1: Expr, x2: Expr): Op(tok, Type.Void) {
    private var expr1: Expr
    private var expr2: Expr

    init {
        this.expr1 = x1
        this.expr2 = x2
        this.type = Type.max(expr1.type, expr1.type)
        if(this.type == Type.Void) error("type error")
    }

    // 分别计算操作符两遍的表达式并构造新的三地址指令
    override fun gen(): Expr {
        return Arith(op, expr1.reduce(), expr2.reduce())
    }

    override fun toString(): String {
        return "$expr1 $op $expr2"
    }
}
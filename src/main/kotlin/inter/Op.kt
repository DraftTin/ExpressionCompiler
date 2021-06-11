package inter

import lexer.Token
import symbols.Type

/**
 * Op:
 */
open class Op(tok: Token, p: Type): Expr(tok, p){
    override fun reduce(): Expr {
        // 获取三地址码
        val x = gen()
        // 创建临时变量
        val t = Temp(type)
        emit("$t = $x")
        return t
    }
}
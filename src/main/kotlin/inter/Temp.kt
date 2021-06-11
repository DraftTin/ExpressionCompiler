package inter

import lexer.Word
import symbols.Type

/**
 * 临时变量，用于计算表达式进行规约时替代表达式的结果
 * @param p 表示该临时变量的数据类型
 */
class Temp(p: Type) : Expr(Word.temp, p) {
    var number = 0

    companion object {
        var count = 0
    }

    init {
        number = ++count
    }

    override fun toString(): String {
        return "t$number"
    }
}
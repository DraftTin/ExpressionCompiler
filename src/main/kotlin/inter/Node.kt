package inter

import lexer.Lexer

/**
 * 抽象语法树的节点
 */
open class Node {
    // 词法分析的行号
    var lexline = 0
    init {
        this.lexline = Lexer.line
    }

    companion object {
        var labels: Int = 0
    }

    fun error(msg: String) { throw Error("near line $lexline: $msg") }

    fun newLabel(): Int = ++labels

    fun emitLabel(i: Int) { print("L$i:") }

    fun emit(s: String) { println("\t$s") }

}
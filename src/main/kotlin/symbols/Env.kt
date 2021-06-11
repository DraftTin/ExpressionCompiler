package symbols

import inter.Id
import lexer.Token
import java.util.*

/**
 * 符号表，env表示一个作用域
 */
class Env {
    private var symbolTable: Hashtable<Token, Id> = Hashtable()
    private var prevEnv: Env? = null

    /**
     * 当前表添加一个标识符
     */
    fun put(token: Token, id: Id) {
        symbolTable.put(token, id)
    }

    /**
     * 根据token获取标识符
     */
    fun get(token: Token): Id? {
        var curEnv: Env? = this
        while(curEnv != null) {
            var id = curEnv.symbolTable.get(token)
            if(id != null) return id
            curEnv = curEnv.prevEnv
        }
        // 没有找到token对应的symbol
        return null
    }
}
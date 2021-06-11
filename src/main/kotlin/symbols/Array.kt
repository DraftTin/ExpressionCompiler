package symbols

import lexer.Kind

class Array(sz: Int, p: Type): Type("[]", Kind.INDEX, sz * p.width) {
    var size = 0            // 元素数量
    val itemType: Type      // 元素种类

    init {
        this.size = sz
        this.itemType = p
    }

    override fun toString(): String {
        return "[$size] $itemType"
    }

}

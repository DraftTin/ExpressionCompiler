package inter

class Stmt: Node() {
    companion object {
        val NULL  = Stmt()
        val Enclosing = Stmt.NULL
    }

    /**
     * @return 空
     */
    fun gen(a: Int, b: Int) {}

    var after = 0
}
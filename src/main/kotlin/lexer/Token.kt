package lexer

class Token(row: Int, col: Int, type: TokenType, value: String) {
    var row:Int = row           // 行号
    var col:Int = col           // 列号
    var type: TokenType = type  // 类型
    var value: String = value   // 值
}

fun main() {
    Token(1, 2, TokenType.ERROR, "asdasd").row
}
package lexer

import kotlin.system.exitProcess

class Lexer {
    private var data: String = ""
    private var current: Int = 0
    private var row: Int = 1
    private var col: Int = 1
    private lateinit var tokens: ArrayList<Token>

    fun test() {
        println("fuck you")
    }

    /**
     * 1. 读取数据中的token
     * 2. 如果为ERROR则报错
     * 3. 如果为null则表示读取完成
     * 4. 其他情况将返回的token加入到token序列中
     * 5. 返回token序列
     */
    fun tokenlizer(data: String): ArrayList<Token> {
        this.current= 0                       // 当前data扫描的位置，扫描头
        this.row= 1                           // 当前扫描的行号
        this.col= 1                           // 当前扫描的列号
        this.tokens = arrayListOf()    // 返回的tokens
        this.data = data

        val singleDelimiter = arrayListOf('+', '-', '*', '/', '%', '(', ')', ';', '[', ']', '=', '<', '>')
        val doubleDelimiter = arrayListOf(':', '/')
        while(current < data.length) {
            val char = data[current]
            var curToken: Token
            // 双分界符
            when {
                char in doubleDelimiter -> {
                    curToken = handleDoubleDelimiter()
                }
                // 单分界符
                char in singleDelimiter -> {
                    curToken = handleSingleDelimiter()
                }
                // 空白符
                isWhiteSpace(char) -> {
                    curToken = handleWhiteSpace()
                }
                // 数字
                isDigit(char, '1') -> {
                    curToken = handleNumber()
                }
                // 变量
                isAlpha(char) -> {
                    curToken = handleIdentifier()
                }
                // 字符串
                char == '"' -> {
                    curToken = handleString()
                }
                // 不能识别的字符
                else -> {
                    curToken = Token(row, col, TokenType.ERROR, "error")
                }
            }

            // 判断Token的类型
            when(curToken.type) {
                TokenType.ERROR -> {
                    // TODO 错误处理
                    exitProcess(-1)
                }
                TokenType.WHITESPACE -> {/* skip */}
                else -> tokens.add(curToken)
            }
        }
        // 返回tokens
        return tokens
    }

    /**
     * 处理单分界符
     */
    private fun handleSingleDelimiter(): Token {
        val char = data[current]
        var type: TokenType = TokenType.ERROR
        when(char) {
            '+' -> type = TokenType.PLUS
            '-' -> type = TokenType.MINUS
            '*' -> type = TokenType.TIMES
            '/' -> type = TokenType.DIVIDE
            '%' -> type = TokenType.MOD
            '(' -> type = TokenType.LPAREN
            ')' -> type = TokenType.RPAREN
            ';' -> type = TokenType.SEMI
            '[' -> type = TokenType.LBRACKET
            ']' -> type = TokenType.RBRACKET
            '=' -> type = TokenType.EQUALS
            '<' -> type = TokenType.LT
            '>' -> type = TokenType.GT
        }
        val curToken = Token(row, col, type, char.toString())
        current++
        col++
        return curToken
    }

    /**
     * 处理空白符
     */
    private fun handleWhiteSpace(): Token {
        while(current < data.length && isWhiteSpace(data[current])) {
            if(data[current] == '\n' || data[current] == '\r') {
                current++
                row++
                col = 1
            }
            else {
                current++
                col++
            }
        }
        return Token(row, col, TokenType.WHITESPACE, "whitespace")
    }

    /**
     * 处理双分界符
     */
    private fun handleDoubleDelimiter(): Token {
        if(current + 1 >= data.length) {
            return Token(row, col, TokenType.ERROR, "error")
        }
        var curToken = Token(row, col, TokenType.ERROR, "UNKNOWN")
        val savedRow = row
        val savedCol = col
        when(data[current]) {
            ':' -> {
                current++
                if(data[current] == '=') {
                    curToken = Token(row, col, TokenType.ASSIGN, ":=")
                    ++current
                    ++col
                }
            }
            '/' -> {
                current++
                col++
                when(data[current]) {
                    // /*comment*/的情况
                    '*' -> {
                        while(current < data.length - 1 && (data[current] != '*' || data[current + 1] != '/')) {
                            ++current
                            ++col
                        }
                        if(current >= data.length - 1) {
                            curToken = Token(row, col, TokenType.ERROR, "error")
                        }
                        else {
                            current += 2    // */
                            col += 2
                            curToken = Token(savedRow, savedCol, TokenType.COMMENT, "comment")
                        }
                    }
                    // //comment的情况
                    '/' -> {
                        // 如果是//注释，则读取data直到换行
                        while(current < data.length && (data[current] != '\n' && data[current] != '\r')) {
                            ++current
                            ++col
                        }
                        curToken = Token(savedRow, savedCol, TokenType.COMMENT, "comment")
                    }
                    // 回退一步，用单分界符处理
                    else -> {
                        current--
                        curToken = handleSingleDelimiter()
                    }
                }
            }
        }
        return curToken
    }

    /**
     * 处理数字
     */
    private fun handleNumber(): Token {
        var value = ""
        val savedRow = row
        val savedCol = col
        while(current < data.length && isDigit(data[current])) {
            value += data[current]
            current++
            col++
        }
        return Token(savedRow, savedCol, TokenType.NUMBER, value)
    }

    /**
     * 处理变量
     */
    private fun handleIdentifier(): Token {
        var value = ""
        val savedRow = row
        val savedCol = col
        while(current < data.length && (isAlpha(data[current]) || isDigit(data[current]) || data[current] == '_')) {
            value += data[current]
            current++
            col++
        }
        return Token(savedRow, savedCol, TokenType.IDENTIFIER, value)
    }

    /**
     * 处理字符串
     */
    private fun handleString(): Token {
        var value = ""
        val savedRow = row
        val savedCol = col
        current++
        col++
        while(current < data.length &&  data[current] != '"') {
            if(current == 17) {
                true
            }
            // 不允许字符串中间换行
            if(data[current] == '\n' || data[current] == '\r') {
                return Token(row, col, TokenType.ERROR, "error")
            }
            if(data[current] == '\\') {
                current++
                col++
                // 超界
                if(current >= data.length) {
                    return Token(row, col, TokenType.ERROR, "error")
                }
                when(data[current]) {
                    't' -> value += '\t'
                    'n' -> value += '\n'
                    'r' -> value += '\r'
                    // 不能识别
                    else -> return Token(row, col, TokenType.ERROR, "error")
                }
            }
            value += data[current]
            current++
            col++
        }
        // 没有扫描到封闭"
        if(current >= data.length) {
            return Token(row, col, TokenType.ERROR, "error")
        }
        // 忽略封闭'"'
        ++current
        return Token(savedRow, savedCol, TokenType.STRING, value)
    }

    /**
     * 判断是否是空白符
     */
    private fun isWhiteSpace(char: Char): Boolean {
        if(char == ' ' || char == '\n' || char == '\r' || char == '\t') return true
        return false
    }

    /**
     * 判断是否是数字
     */
    private fun isDigit(char: Char, start: Char = '0', end: Char = '9'): Boolean{
        return char in start..end
    }

    /**
     * 判断是否是字符
     */
    private fun isAlpha(char: Char): Boolean {
        return char in 'a'..'z' || char in 'A'..'Z'
    }

}

fun main() {
}

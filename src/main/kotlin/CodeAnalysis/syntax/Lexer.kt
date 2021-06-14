package CodeAnalysis.syntax

import java.io.Reader
import java.util.*

class Lexer(data: Reader) {
    // 数据流
    private var data: Reader
    // 当前扫描的字符
    private var readPoint: Char = ' '
    // 当前的扫描行
    private var row: Int = 1
    // 当前的扫描列
    private var col: Int = 0
    private var words: Hashtable<String, SyntaxToken> = Hashtable()

    var diagnostics = LinkedList<String>()

    companion object{
        var line: Int = 1   // 扫描到的行数
    }

    /**
     * 添加保留字
     */
    init {
        reserve(SyntaxToken(TokenKind.PROGRAM,"program"))
        reserve(SyntaxToken(TokenKind.PROCEDURE,"procedure"))
        reserve(SyntaxToken(TokenKind.WHILE,"while"))
        reserve(SyntaxToken(TokenKind.DO,"do"))
        reserve(SyntaxToken(TokenKind.ELIHW,"elihw"))
        reserve(SyntaxToken(TokenKind.BEGIN,"begin"))
        reserve(SyntaxToken(TokenKind.END,"end"))
        reserve(SyntaxToken(TokenKind.RETURN,"return"))
        reserve(SyntaxToken(TokenKind.NUMBER,"integer"))
        reserve(SyntaxToken(TokenKind.REAL,"float"))
        reserve(SyntaxToken(TokenKind.CHAR,"char"))
        reserve(SyntaxToken(TokenKind.BOOL,"bool"))
        reserve(SyntaxToken(TokenKind.WHILE,"for"))
        reserve(SyntaxToken(TokenKind.VAR,"var"))
        reserve(SyntaxToken(TokenKind.IF,"if"))
        reserve(SyntaxToken(TokenKind.THEN,"then"))
        reserve(SyntaxToken(TokenKind.ELSE,"else"))
        reserve(SyntaxToken(TokenKind.FI,"fi"))

        this.data = data
    }

    /**
     * 添加保留字
     */
    private fun reserve(word: SyntaxToken) {
        words.put(word.text, word)
    }

    /**
     * 读取下一个字符
     */
    private fun readch() {
        readPoint = data.read().toChar()
        this.col++
        if(readPoint == '\r') {
            this.row++
            this.col = 1
        }
    }


    /**
     * 读取一个字符验证是否是ch
     * @return 验证的结果
     */
    fun readch(ch: Char): Boolean {
        readch()
        if(readPoint != ch) return false
        readPoint = ' '      // 置空
        return true
    }

    /**
     * 扫描并返回一个token
     */
    fun scan(): SyntaxToken {
        while(true) {
            if(readPoint == ' ' || readPoint == '\t') {
                readch()
            }
            // windows换行"\r\n"
            else if(readPoint == '\r') {
                if(readch('\n')) {
                    readch()
                }
            }
            else break
        }
        // 保留符号
        when(readPoint) {
            '=' -> {
                readch()
                return SyntaxToken.eq
            }
            '>' -> return if(readch('=')) SyntaxToken.ge else SyntaxToken.gt
            '<' -> return if(readch('=')) SyntaxToken.le else SyntaxToken.lt
            '(' -> {
                readch()
                return SyntaxToken.lsparen
            }
            ')' -> {
                readch()
                return SyntaxToken.rsparen
            }
            '+' -> {
                readch()
                return SyntaxToken.plus
            }
            '-' -> {
                readch()
                return SyntaxToken.minus
            }
            '*' -> {
                readch()
                return SyntaxToken.times
            }
            '/' -> {
                readch()
                return SyntaxToken.div
            }
            ':' -> return if(readch('=')) SyntaxToken.assign else BadToken(':')
            '{' -> {
                readch()
                return SyntaxToken.lbparen
            }
            '}' -> {
                readch()
                return SyntaxToken.rbparen
            }
            ';' -> {
                readch()
                return SyntaxToken.semi
            }
            '%' -> {
                readch()
                return SyntaxToken.mod
            }
            '[' -> {
                readch()
                return SyntaxToken.lbracket
            }
            ']' -> {
                readch()
                return SyntaxToken.rbracket
            }
            ',' -> {
                readch()
                return SyntaxToken.comma
            }
            '.' -> {
                readch()
                return SyntaxToken.dot
            }
            '&' -> if(readch('&')) {
                return SyntaxToken.ampersandampersand
            } else {
                return BadToken('&')
            }
            '|' -> if(readch('|')) {
                return SyntaxToken.pipepipe
            } else {
                return BadToken('|')
            }
        }
        // 整数或浮点数
        if(readPoint.isDigit()) {
            var _text = ""  // 记录数值
//            while(peek.isDigit()) {
//                intValue = intValue * 10 + peek.toInt() - '0'.toInt()
//                readch()
//            }
            while(readPoint.isDigit()) {
                _text += readPoint
                readch()
            }
            if(readPoint != '.') {
                var intValue = 0
                try {
                    intValue = _text.toInt()
                } catch (e: NumberFormatException) {
                    diagnostics.add("The number $_text can't be represented by an int32")
                }
                return SyntaxToken(kind = TokenKind.NUMBER, text = _text, value = intValue)
            }
            _text += '.'
            readch()
            while(readPoint.isDigit()) {
                _text += readPoint
                readch()
            }
            var floatValue = 0F
            try {
                floatValue = _text.toFloat()
            } catch (e: java.lang.NumberFormatException) {
                diagnostics.add("The number $_text can't be represented by an float number")
            }
//            var floatValue = _text.toFloat()
//            var digit = 10
//            while(peek.isDigit()) {
//                floatValue += peek.toFloat() / digit
//                digit *= 10
//                readch()
//            }
            return SyntaxToken(kind = TokenKind.REAL, text = _text, value = floatValue)
        }
        // 变量或保留字
        if(readPoint.isLetter()) {
            var name = StringBuffer("")
            while(readPoint.isLetterOrDigit()) {
                name.append(readPoint)
                readch()
            }
            val s = name.toString()
            when(getWordType(s)) {
                TokenKind.TRUE -> return SyntaxToken(kind = TokenKind.TRUE, text = "true", value = true)
                TokenKind.FALSE -> return SyntaxToken(kind = TokenKind.FALSE, text = "false", value = false)
            }
            val tok = words[s]
            if(tok != null) return tok;
            val w = SyntaxToken(TokenKind.ID, s)
            words.put(w.text, w)
            return w
        }
        // 字符型
        if(readPoint == '\'') {
            readch()
            // ''的情况不是字符
            if(readPoint == '\'')  {
                return BadToken(ch = '\'')
            }
            // 检查是否有封闭的''
            else {
                // 记录当前的peek, 也就是字符值
                val tmp = readPoint
                readch()
                // 不是字符, 回溯peek, 并返回BadToken
                if(readPoint != '\'') {
                    when(tmp) {
                        // 如果忽略的是空白符直接返回
                        ' ', '\t' -> {}
                        // 如果忽略的是换行符则增加一行
                        '\r' -> {
                            if(readch('\n')) {
                                readch()
                            }
                        }
                        // 其他情况回溯
                        else -> readPoint = tmp
                    }
                    return BadToken(ch = '\'')
                }
                // 是字符, 返回字符token
                else {
                    readch()
                    return SyntaxToken(TokenKind.CHAR, tmp.toString(), tmp)
                }
            }
        }
        if(readPoint == (-1).toChar()) return SyntaxToken(TokenKind.EOF, "end of file")
        // 其他情况，返回bad token
        diagnostics.add("ERROR: bad  character input: '$readPoint' at ${row}row ${col}col")
        val tok = BadToken(ch = readPoint)
        readPoint = ' '
        return tok
    }

    /**
     * 根据读取的字符串判断是否是特殊保留字，如true, false
     */
    fun getWordType(s: String): TokenKind = when(s) {
        "true" -> TokenKind.TRUE
        "false" -> TokenKind.FALSE
        else -> TokenKind.ID
    }
}
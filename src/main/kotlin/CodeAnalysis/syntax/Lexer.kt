package lexer

import java.io.Reader
import java.util.*

class Lexer(data: Reader) {
    // 数据流
    private var data: Reader
    // 当前扫描的字符
    private var peek: Char = ' '
    // 当前的扫描行
    private var row: Int = 0
    // 当前的扫描列
    private var col: Int = 0
    private var words: Hashtable<String, Word> = Hashtable()

    var diagnostics = LinkedList<String>()

    companion object{
        var line: Int = 1   // 扫描到的行数
    }

    /**
     * 添加保留字
     */
    init {
        this.data = data
        reserve(Word("program", TokenKind.PROGRAM))
        reserve(Word("procedure", TokenKind.PROCEDURE))
        reserve(Word("while", TokenKind.WHILE))
        reserve(Word("do", TokenKind.DO))
        reserve(Word("elihw", TokenKind.ELIHW))
        reserve(Word("begin", TokenKind.BEGIN))
        reserve(Word("end", TokenKind.END))
        reserve(Word("return", TokenKind.RETURN))
        reserve(Word("integer", TokenKind.NUMBER))
        reserve(Word("float", TokenKind.FLOAT))
        reserve(Word("char", TokenKind.CHAR))
        reserve(Word("bool", TokenKind.BOOL))
        reserve(Word("for", TokenKind.WHILE))
        reserve(Word("var", TokenKind.VAR))
        reserve(Word("if", TokenKind.IF))
        reserve(Word("then", TokenKind.THEN))
        reserve(Word("else", TokenKind.ELSE))
        reserve(Word("fi", TokenKind.FI))
        reserve(Word.True)
        reserve(Word.False)
    }


    /**
     * 添加保留字
     */
    private fun reserve(word: Word) {
        words.put(word.lexeme, word)
    }

    /**
     * 读取下一个字符
     */
    private fun readch() {
        peek = data.read().toChar()
        this.col++
        if(peek == '\r') {
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
        if(peek != ch) return false
        peek = ' '      // 置空
        return true
    }

    /**
     * 扫描并返回一个token
     */
    fun scan(): SyntaxToken {
        while(true) {
            if(peek == ' ' || peek == '\t') {
                readch()
            }
            // windows换行"\r\n"
            else if(peek == '\r') {
                if(readch('\n')) {
                    readch()
                }
            }
            else break
        }
        // 保留符号
        when(peek) {
            '=' -> {
                readch()
                return Word.eq
            }
            '>' -> return if(readch('=')) Word.ge else Word.gt
            '<' -> return if(readch('=')) Word.le else Word.lt
            '(' -> {
                readch()
                return Word.lsparen
            }
            ')' -> {
                readch()
                return Word.rsparen
            }
            '+' -> {
                readch()
                return Word.plus
            }
            '-' -> {
                readch()
                return Word.minus
            }
            '*' -> {
                readch()
                return Word.times
            }
            '/' -> {
                readch()
                return Word.div
            }
            ':' -> return if(readch('=')) Word.assign else BadToken(':')
            '{' -> {
                readch()
                return Word.lbparen
            }
            '}' -> {
                readch()
                return Word.rbparen
            }
            ';' -> {
                readch()
                return Word.semi
            }
            '%' -> {
                readch()
                return Word.mod
            }
            '[' -> {
                readch()
                return Word.lbracket
            }
            ']' -> {
                readch()
                return Word.rbracket
            }
            ',' -> {
                readch()
                return Word.comma
            }
            '.' -> {
                readch()
                return Word.dot
            }
        }
        // 整数或浮点数
        if(peek.isDigit()) {
            var _text = ""  // 记录数值
//            while(peek.isDigit()) {
//                intValue = intValue * 10 + peek.toInt() - '0'.toInt()
//                readch()
//            }
            while(peek.isDigit()) {
                _text += peek
                readch()
            }
            if(peek != '.') {
                var intValue = 0
                try {
                    intValue = _text.toInt()
                } catch (e: NumberFormatException) {
                    diagnostics.add("The number $_text can't be represented by an int32")
                }
                return NumberToken(intValue)
            }
            _text += '.'
            readch()
            while(peek.isDigit()) {
                _text += peek
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
            return RealToken(floatValue)
        }
        // 变量或保留字
        if(peek.isLetter()) {
            var name = StringBuffer("")
            while(peek.isLetterOrDigit()) {
                name.append(peek)
                readch()
            }
            val s = name.toString()
            val tok = words[s]
            if(tok != null) return tok;
            val w = Word(s, TokenKind.ID)
            words.put(w.lexeme, w)
            return w
        }
        // 字符型
        if(peek == '\'') {
            readch()
            // ''的情况不是字符
            if(peek == '\'')  {
                return BadToken(ch = '\'')
            }
            // 检查是否有封闭的''
            else {
                // 记录当前的peek, 也就是字符值
                val tmp = peek
                readch()
                // 不是字符, 回溯peek, 并返回BadToken
                if(peek != '\'') {
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
                        else -> peek = tmp
                    }
                    return BadToken(ch = '\'')
                }
                // 是字符, 返回字符token
                else {
                    readch()
                    return CharToken(tmp)
                }
            }
        }
        if(peek == (-1).toChar()) return SyntaxToken(TokenKind.EOF)
        // 其他情况，返回bad token
        diagnostics.add("ERROR: bad  character input: '$peek' at ${row}row ${col}col")
        val tok = BadToken(ch = peek)
        peek = ' '
        return tok
    }
}
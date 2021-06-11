package lexer

import java.io.Reader
import java.util.*
import kotlin.collections.ArrayList

class Lexer(data: Reader) {
    // 数据流
    private var data: Reader
    // 当前扫描的字符
    private var peek: Char = ' '
    // 当前的扫描行
    private var row: Int = 1
    // 当前的扫描列
    private var col: Int = 1
    private var words: Hashtable<String, Word>

    var diagnosics = LinkedList<String>()

    companion object{
        var line: Int = 1   // 扫描到的行数
    }

    /**
     * 添加保留字
     */
    init {
        this.data = data
        this.row = 1
        this.col = 1
        this.words = Hashtable()
        reserve(Word("program", Kind.PROGRAM))
        reserve(Word("procedure", Kind.PROCEDURE))
        reserve(Word("while", Kind.WHILE))
        reserve(Word("do", Kind.DO))
        reserve(Word("elihw", Kind.ELIHW))
        reserve(Word("begin", Kind.BEGIN))
        reserve(Word("end", Kind.END))
        reserve(Word("return", Kind.RETURN))
        reserve(Word("integer", Kind.NUMBER))
        reserve(Word("float", Kind.FLOAT))
        reserve(Word("char", Kind.CHAR))
        reserve(Word("bool", Kind.BOOL))
        reserve(Word("for", Kind.WHILE))
        reserve(Word("var", Kind.VAR))
        reserve(Word("if", Kind.IF))
        reserve(Word("then", Kind.THEN))
        reserve(Word("else", Kind.ELSE))
        reserve(Word("fi", Kind.FI))
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
    fun scan(): Token {
        if(peek == (-1).toChar()) return Token(Kind.EOF)
        while(true) {
            if(peek == ' ' || peek == '\t') {
                readch()
            }
            // windows换行"\r\n"
            else if(peek == '\r') {
                row += 1
                col = 1
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
            var intValue = 0
            while(peek.isDigit()) {
                intValue = intValue * 10 + peek.toInt() - '0'.toInt()
                readch()
            }
            if(peek != '.') return NumberToken(intValue)
            readch()
            var floatValue = intValue.toFloat()
            var digit = 10
            while(peek.isDigit()) {
                floatValue += peek.toFloat() / digit
                digit *= 10
            }
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
            val w = Word(s, Kind.ID)
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
                            row += 1
                            col = 1
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
        // 其他情况，返回bad token
        diagnosics.add("ERROR: bad  character input: '$peek'")
        val tok = BadToken(ch = peek)
        peek = ' '
        return tok
    }
}
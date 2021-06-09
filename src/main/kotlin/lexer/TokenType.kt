package lexer

enum class TokenType(val v: String) {
    WHITESPACE("whitespace"),   // 空白符
    COMMENT("comment"),         // 注释
    ERROR("error"),             // 错误状态，方便程序识别
    EOF("eof"),                 // end of file

    IDENTIFIER("id"),           // id变量
    /* 数据类型 */
    INTEGER("integer"),         // 数字
    CHAR("char"),               // 字符
    STRING("string"),           // 字符串
    ARRAY("array"),             // 数组类型

    /* 其他保留字 */
    PROGRAM("program"),         // program
    TYPE("type"),               // ?
    VAR("var"),                 // var
    IF("if"),                   // if
    THEN("then"),               // then
    ELSE("else"),               // else
    FI("fi"),                   // fi
    WHILE("while"),             // while
    DO("do"),                   // do
    ELIHW("elihw"),             // elihw
    BEGIN("begin"),             // begin
    END("end"),                 // end
    READ("read"),               // read
    WRITE("write"),             // write
    OF("of"),                   // of
    RETURN("return"),           // return
    PROCEDURE("procedure"),     // procedure


    /* 界限符号 */
    // 单分界符
    LPAREN("("),     // (
    RPAREN(")"),     // )
    EQUALS("="),     // =
    PLUS("+"),       // +
    MINUS("-"),      // -
    TIMES("*"),      // *
    DIVIDE("/"),     // /
    MOD("%"),        // %
    SEMI(";"),       // ;
    LBRACKET("["),   // [
    RBRACKET("]"),   // ]
    LT("<"),         // <
    GT(">"),         // >
    COMMA(","),      // ,

    // 双分界符
    ASSIGN(":="),     // :=
    DSLASH("//"),     // //
    LCOMMENT("/*"),   // /*
    RCOMMENT("*/");    // */

    fun value(): String {
        return v
    }

    companion object {
        /**
         * 返回保留字
         */
        fun getReservedWords(): ArrayList<TokenType> {
            return arrayListOf(
                    PROGRAM, TYPE,
                    VAR, IF,
                    THEN, ELSE,
                    FI, WHILE,
                    DO, ELIHW,
                    BEGIN, END,
                    WRITE, READ,
                    OF, RETURN,
                    INTEGER, CHAR,
                    PROCEDURE
            )
        }
    }
}
package lexer

enum class TokenType() {
    WHITESPACE,     // 空白符
    COMMENT,        // 注释

    ERROR,          // 错误状态，方便程序识别
    IDENTIFIER,     // id变量
    NUMBER,         // 数字
    CHAR,           // 字符
    STRING,         // 字符串

    // 保留字
    // 单分界符
    LPAREN,     // (
    RPAREN,     // )
    EQUALS,     // =
    PLUS,           // +
    MINUS,      // -
    TIMES,      // *
    DIVIDE,     // /
    MOD,        // %
    SEMI,       // ;
    LBRACKET,   // [
    RBRACKET,   // ]
    LT,         // <
    GT,         // >

    // 双分界符
    ASSIGN,     // :=
    DSLASH,     // //
    LCOMMENT,   // /*
    RCOMMENT    // */
}


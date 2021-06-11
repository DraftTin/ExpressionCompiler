package lexer

import java.util.*

/**
 * 定义各个保留字的值
 */
//object Kind {
//    var table: Hashtable<Int, String> = Hashtable()
//    init {
//        table.put(501, "NUMBER")
//        table.put(1001, "BinaryExpression")
//        table.put(316, "PLUS")
//        table.put(1002, "NumberExpression")
//    }
//    val VAR = 256
//    val IF = 257
//    val THEN = 258
//    val ELSE = 259
//    val FI = 300
//    val WHILE = 301
//    val DO = 302
//    val ELIHW = 303
//    val BEGIN = 304
//    val END = 305
//    val RETURN = 306
//    val PROGRAM = 307
//    val PROCEDURE = 308
//    val TRUE = 309
//    val FALSE = 310
//
//    val EQ = 309            // =
//    val GT = 310            // >
//    val LT = 311            // <
//    val GE = 312            // >=
//    val LE = 313            // <=
//    val LSPAREN = 314       // (
//    val RSPAREN = 315       // )
//    val PLUS = 316          // +
//    val MINUS = 317           // -
//    val STAR = 318         // *
//    val DIV = 319           // /
//    val ASSIGN = 320        // :=
//    val LBPAREN = 321       // {
//    val RBPAREN = 322       // }
//    val SEMI = 323          // ;
//    val MOD = 324           // %
//    val LBRACKET = 325      // [
//    val RBRACKET = 326      // ]
//    val COMMA = 327         // ,
//    val DOT = 328           // .
//
//    val NUMBER = 501
//    const val FLOAT = 502
//    const val CHAR = 503
//    const val BOOL = 504
//    const val VOID = 505          // 空类型，用于占位
//
//    const val ID = 601
//
//    const val BASIC = 701         // 基本类型
//    const val INDEX = 702         // 可索引类型
//
//    const val TEMP = 801
//
//    const val EOF = 901           // end of file
//
//    const val BinaryExpression = 1001
//    const val NumberExpression = 1002
//}

enum class Kind {
    VAR,
    IF,
    THEN,
    ELSE,
    FI,
    WHILE,
    DO,
    ELIHW,
    BEGIN,
    END,
    RETURN,
    PROGRAM,
    PROCEDURE,
    TRUE,
    FALSE,

    EQ,             // =
    GT,             // >
    LT,             // <
    GE,             // >=
    LE,             // <=
    LSPAREN,        // (
    RSPAREN,        // )
    PLUS,           // +
    MINUS,           // -
    STAR,           // *
    DIV,            // /
    ASSIGN,         // :=
    LBPAREN,        // {
    RBPAREN,        // }
    SEMI,           // ;
    MOD,            // %
    LBRACKET,       // [
    RBRACKET,       // ]
    COMMA,          // ,
    DOT,            // .

    NUMBER,
    FLOAT,
    CHAR,
    BOOL,
    VOID,          // 空类型，用于占位

    ID,

    BASIC,         // 基本类型
    INDEX,         // 可索引类型

    TEMP,

    EOF,            // end of file
    BadToken,       // 未识别的token

    BinaryExpression,
    NumberExpression
}
package lexer

import java.util.*

enum class TokenKind {
    // Tokens
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

    EQ,                     // =
    GT,                     // >
    LT,                     // <
    GE,                     // >=
    LE,                     // <=
    OpenParenToken,         // (
    ClosedParenToken,       // )
    PLUS,                   // +
    MINUS,                  // -
    STAR,                   // *
    SLASH,                  // /
    ASSIGN,                 // :=
    LBPAREN,                // {
    RBPAREN,                // }
    SEMI,                   // ;
    MOD,                    // %
    LBRACKET,               // [
    RBRACKET,               // ]
    COMMA,                  // ,
    DOT,                    // .

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

    // Expressions
    BinaryExpression,
    NumberExpression,
    ParenthesizedExpression,
    BadExpressionSyntax,
    UnaryExpressionSyntax,
}
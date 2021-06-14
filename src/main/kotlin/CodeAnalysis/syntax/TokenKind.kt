package CodeAnalysis.syntax

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
    TrueToken,
    FalseToken,

    EqualToken,             // =
    NotEqualsToken,         // !=
    GT,                     // >
    LT,                     // <
    GE,                     // >=
    LE,                     // <=
    OpenParenToken,         // (
    ClosedParenToken,       // )
    PlusToken,                   // +
    MinusToken,                  // -
    StarToken,                   // *
    SlashToken,                  // /
    ASSIGN,                 // :=
    OpenComment,                // {
    ClosedComment,                // }
    SEMI,                   // ;
    MOD,                    // %
    LBRACKET,               // [
    RBRACKET,               // ]
    COMMA,                  // ,
    DOT,                    // .
    AmpersandAmpersandToken,    // &&
    PipePipeToken,              // ||
    BangToken,                  // !

    NumberToken,
    REAL,
    CHAR,
    BOOL,

    ID,

    EOF,            // end of file
    BadToken,       // 未识别的token

    // Expressions
    BinaryExpression,
    LiteralExpression,
    ParenthesizedExpression,
    BadExpressionSyntax,
    UnaryExpressionSyntax,
}
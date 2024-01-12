enum TokenType {
    // Single-character tokens.
    LEFT_PAREN, RIGHT_PAREN,
    COMMA, DOT, MINUS, PLUS, SEMICOLON, SLASH, STAR, QUESTION, COLON,
    // One or two character tokens.
    BANG,
    EQUAL,
    GREATER, GREATER_EQUAL,
    LESS, LESS_EQUAL,
    // Literals.
    IDENTIFIER, NUMBER,
    // Keywords.
    MAX, MIN, CEIL, FLOOR, ROUND, ABS, QURT, LOG,

    EOF
}
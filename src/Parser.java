import java.util.ArrayList;
import java.util.List;

class Parser {
    private static class ParseError extends RuntimeException {}
    private final List<Token> tokens;
    private int current = 0;
    Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    List<Expr> parse() {
        List<Expr> statements = new ArrayList<>();
        while (!isAtEnd()) {
            statements.add(expression());
        }
        return statements;
    }


    private Expr expression() {
        return term();
    }




    private Expr term() {
        Expr expr = factor();
        while (match(TokenType.MINUS, TokenType.PLUS)) {
            Token operator = previous();
            Expr right = factor();
            expr = new Expr.Binary(expr, operator, right);
        }
        return expr;
    }

    private Expr factor() {
        Expr expr = unary();
        while (match(TokenType.SLASH, TokenType.STAR)) {
            Token operator = previous();
            Expr right = unary();
            expr = new Expr.Binary(expr, operator, right);
        }
        return expr;
    }


    private Expr unary() {
        if (match(TokenType.MINUS)) {
            Token operator = previous();
            Expr right = unary();
            return new Expr.Unary(operator, right);
        }
        Expr right = primary();
        while(match(TokenType.BANG)){
            Token operator = previous();
            right = new Expr.Unary(operator, right);
        }
        return right;
    }


    private Expr primary() {

        if (match(TokenType.NUMBER))
            return new Expr.Literal((Double) previous().literal);

        if (match(TokenType.LEFT_PAREN)) {
            Expr expr = expression();
            consume(TokenType.RIGHT_PAREN, "Expect ')' after expression.");
            return new Expr.Grouping(expr);
        }


        throw error(peek(), "Expect expression.");
    }


    private boolean match(TokenType... types) {
        for (TokenType type : types) {
            if (check(type)) {
                advance();
                return true;
            }
        }
        return false;
    }

    private Token consume(TokenType type, String message) {
        if (check(type)) return advance();
        throw error(peek(), message);
    }

    private boolean check(TokenType type) {
        if (isAtEnd()) return false;
        return peek().type == type;
    }

    private Token advance() {
        if (!isAtEnd()) current++;
        return previous();
    }

    private boolean isAtEnd() {
        return peek().type == TokenType.EOF;
    }
    private Token peek() {
        return tokens.get(current);
    }

    private Token previous() {
        return tokens.get(current - 1);
    }

    private ParseError error(Token token, String message) {
        MathProblemSolver.error(token, message);
        return new ParseError();
    }


}
import java.util.ArrayList;
import java.util.LinkedList;
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
        // While there is addition or subtraction add them to the expression
        while (match(TokenType.MINUS, TokenType.PLUS)) {
            Token operator = previous();
            Expr right = factor();
            expr = new Expr.Binary(expr, operator, right);
        }
        return expr;
    }

    private Expr factor() {
        Expr expr = exponent();
        // While there is division or multiplication add them to the expression
        while (match(TokenType.SLASH, TokenType.STAR)) {
            Token operator = previous();
            Expr right = exponent();
            expr = new Expr.Binary(expr, operator, right);
        }
        return expr;
    }
    private Expr exponent() {
        Expr expr = unary();
        // While there is an exponent add it to the expression
        while (match(TokenType.CARET)) {
            Token operator = previous();
            Expr right = unary();
            expr = new Expr.Binary(expr, operator, right);
        }
        return expr;
    }

    private Expr unary() {
        // if there is a minus, then it's a negation minus
        if (match(TokenType.MINUS)) {
            Token operator = previous();
            Expr right = unary();
            return new Expr.Unary(operator, right);
        }
        Expr right = function();
        // as long as there are factorial signs keep adding them to the expression
        while(match(TokenType.BANG)){
            Token operator = previous();
            right = new Expr.Unary(operator, right);
        }
        return right;
    }
    private Expr function(){
        // check for a function
        if(match(TokenType.FUNC)){
            Token func = previous();
            // Make sure there is a left parentheses after the function name
            consume(TokenType.LEFT_PAREN, "Expect '(' after a function");
            List<Expr> params = new LinkedList<>();
            // find all the parameters
            do{
                params.add(expression());

            }while(match(TokenType.COMMA));
            // Make sure there is a right parentheses at the end of the function
            consume(TokenType.RIGHT_PAREN, "Expect '(' in the end of a function");
            return new Expr.Function(func, params);
        }
        return primary();
    }

    private Expr primary() {
        // check for a literal
        if (match(TokenType.NUMBER))
            return new Expr.Literal((Double) previous().literal);
        // check for parentheses
        if (match(TokenType.LEFT_PAREN)) {
            List<Expr> values = new LinkedList<>();
            values.add(expression());
            // check for a vector
            while(match(TokenType.COMMA))
                values.add(expression());
            // Make sure there is a right parentheses at the end
            consume(TokenType.RIGHT_PAREN, "Expect ')' after expression.");
            // if there is only one value than it's a simple expression, if there is more than it's a vector
            if(values.size() == 1)
                return new Expr.Grouping(values.get(0));
            else
                return new Expr.Vector(values);
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
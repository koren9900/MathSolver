import java.util.ArrayList;
import java.util.List;


class Interpreter implements Expr.Visitor<Expr> {


    List<Expr> interpret(List<Expr> expressions) {
        try {
            List<Expr> evaluatedExpressions = new ArrayList<>();
            for (Expr expression : expressions) {
                evaluatedExpressions.add(execute(expression));
            }
            return evaluatedExpressions;
        } catch (RuntimeError error) {
            MathProblemSolver.runtimeError(error);
        }
        return null;
    }

    private String stringify(Object object) {
        if (object == null) return "nil";
        if (object instanceof Double) {
            String text = object.toString();
            if (text.endsWith(".0")) {
                text = text.substring(0, text.length() - 2);
            }
            return text;
        }
        return object.toString();
    }
    @Override
    public Expr visitLiteralExpr(Expr.Literal expr) {

        return new Expr.Literal((double) expr.value);
    }

    @Override
    public Expr visitUnaryExpr(Expr.Unary expr) {
        Object right = evaluate(expr.right);
        switch (expr.operator.type) {
            case BANG:
                return new Expr.Literal(factorial(expr.operator, (double)((Expr.Literal) right).value));
            case MINUS:
                checkNumberOperand(expr.operator, right);
                return new Expr.Literal(-(double)((Expr.Literal) right).value);
        }
        // Unreachable.
        return null;
    }

    private Double factorial(Token token, Object right) {
        if(right instanceof Double){
            if(Math.round((double)right) != (double) right || (double) right < 1)
                throw new RuntimeError(token,"Factorial works only on natural numbers");

            double fact = 1;
            for (int i = 2; i <= (double) right; i++)
                fact = fact * i;
            return fact;
        }
        else
            throw new RuntimeError(token,"Factorial works only on numbers");

    }

    @Override
    public Expr visitBinaryExpr(Expr.Binary expr) {
        Expr left = evaluate(expr.left);
        Expr right = evaluate(expr.right);
        checkNumberOperands(expr.operator, left, right);
        switch (expr.operator.type) {
            case MINUS -> {
                return new Expr.Literal((double)((Expr.Literal) left).value - (double)((Expr.Literal) right).value);
            }
            case PLUS -> {
                return new Expr.Literal((double)((Expr.Literal) left).value + (double)((Expr.Literal) right).value);
            }
            case SLASH -> {
                return new Expr.Literal((double)((Expr.Literal) left).value / (double)((Expr.Literal) right).value);
            }
            case STAR -> {

                return new Expr.Literal((double)((Expr.Literal) left).value * (double)((Expr.Literal) right).value);
            }
        }
        // Unreachable.
        return null;
    }

    @Override
    public Expr visitGroupingExpr(Expr.Grouping expr) {
        return evaluate(expr.expression);
    }

    private Expr evaluate(Expr expr) {
        return expr.accept(this);
    }

    private Expr execute(Expr expr) {
        return expr.accept(this);
    }

    private void checkNumberOperand(Token operator, Object operand) {
        if (operand instanceof Expr.Literal) return;
        throw new RuntimeError(operator, "Operand must be a number.");
    }

    private void checkNumberOperands(Token operator, Object left, Object right) {
        if (left instanceof Expr.Literal && right instanceof Expr.Literal) return;
        throw new RuntimeError(operator, "Operands must be numbers.");
    }








}
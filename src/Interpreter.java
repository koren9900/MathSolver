import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
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
    @Override
    public Expr visitLiteralExpr(Expr.Literal expr) {

        return new Expr.Literal((double) expr.value);
    }

    @Override
    public Expr visitUnaryExpr(Expr.Unary expr) {
        Object right = evaluate(expr.right);
        switch (expr.operator.type) {
            case BANG:
                return new Expr.Literal(factorial(expr.operator, ((Expr.Literal) right).value));
            case MINUS:
                checkNumberOperand(expr.operator, right);
                return new Expr.Literal(-(double)((Expr.Literal) right).value);
        }
        // Unreachable.
        return null;
    }

    @Override
    public Expr visitFunctionExpr(Expr.Function expr) {
        List<Expr> evaluatedParameters = new LinkedList<>();
        for(Expr param : expr.parameters)
            evaluatedParameters.add(param.accept(this));
        List<Double> evaluatedDoubleParams = exprToDouble(expr.function, evaluatedParameters);
        switch (expr.function.lexeme){
            case "max" ->{
                if(evaluatedDoubleParams.size() < 2)
                    throw new RuntimeError(expr.function, "Max functions take at least two parameters");
                return new Expr.Literal(Collections.max(evaluatedDoubleParams));
            }
            case "min" ->{
                if(evaluatedDoubleParams.size() < 2)
                    throw new RuntimeError(expr.function, "Min functions take at least two parameters");
                return new Expr.Literal(Collections.min(evaluatedDoubleParams));
            }
            case "root" ->{
                if(evaluatedDoubleParams.size() != 2)
                    throw new RuntimeError(expr.function, "Root functions take exactly two parameters");
                return new Expr.Literal(Math.pow(evaluatedDoubleParams.get(0), 1/evaluatedDoubleParams.get(1)));
            }
            case "sqrt" ->{
                if(evaluatedDoubleParams.size() != 1)
                    throw new RuntimeError(expr.function, "Sqrt functions take exactly one parameter");
                return new Expr.Literal(Math.sqrt(evaluatedDoubleParams.get(0)));
            }
            case "abs" ->{
                if(evaluatedDoubleParams.size() != 1)
                    throw new RuntimeError(expr.function, "Abs functions take exactly one parameter");
                return new Expr.Literal(Math.abs(evaluatedDoubleParams.get(0)));
            }
            case "floor" ->{
                if(evaluatedDoubleParams.size() != 1)
                    throw new RuntimeError(expr.function, "Floor functions take exactly one parameter");
                return new Expr.Literal(Math.floor(evaluatedDoubleParams.get(0)));
            }
            case "ceil" ->{
                if(evaluatedDoubleParams.size() != 1)
                    throw new RuntimeError(expr.function, "Ceil functions take exactly one parameter");
                return new Expr.Literal(Math.ceil(evaluatedDoubleParams.get(0)));
            }
            case "round" ->{
                if(evaluatedDoubleParams.size() != 1)
                    throw new RuntimeError(expr.function, "Round functions take exactly one parameter");
                return new Expr.Literal((double) Math.round(evaluatedDoubleParams.get(0)));
            }
            case "log" ->{
                if(evaluatedDoubleParams.size() != 2)
                    throw new RuntimeError(expr.function, "Round functions take exactly one parameter");
                return new Expr.Literal(Math.log(evaluatedDoubleParams.get(1))/Math.log(evaluatedDoubleParams.get(0)));
            }

        }
        throw new RuntimeError(expr.function, "Unknown Function");
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
            case CARET -> {
                return new Expr.Literal(Math.pow((double)((Expr.Literal) left).value ,(double)((Expr.Literal) right).value));
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

    private List<Double> exprToDouble(Token func, List<Expr> expressions){
        List<Double> doubles = new LinkedList<>();
        for(Expr expr: expressions){
            if (expr instanceof Expr.Literal)
                doubles.add((double)((Expr.Literal) expr).value);
            else
                throw new RuntimeError(func, "Params must be numbers");

        }
        return doubles;
    }







}
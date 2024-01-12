import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Stringer  implements Expr.Visitor<String> {

    public StringBuilder stringify(List<Expr> expressions){
        try {
            StringBuilder str = new StringBuilder();
            for (Expr expression : expressions) {
                str.append(expression.accept(this));
            }
            return str;
        } catch (RuntimeError error) {
            MathProblemSolver.runtimeError(error);
        }
        return null;
    }
    @Override
    public String visitBinaryExpr(Expr.Binary expr) {
        return expr.left.accept(this) + expr.operator.toString() + expr.right.accept(this);
    }

    @Override
    public String visitGroupingExpr(Expr.Grouping expr) {
        return "("+expr.expression.accept(this)+")";
    }

    @Override
    public String visitLiteralExpr(Expr.Literal expr) {
        if(Math.round((double)expr.value) == (double)expr.value)
            return expr.value.toString().substring(0,expr.value.toString().indexOf("."));
        return expr.value.toString();
    }

    @Override
    public String visitUnaryExpr(Expr.Unary expr) {
        if(expr.operator.type == TokenType.BANG)
            return expr.right.accept(this) + expr.operator.lexeme;

        else
            return expr.operator.lexeme + expr.right.accept(this);
    }

    @Override
    public String visitFunctionExpr(Expr.Function expr) {
        StringBuilder funcString = new StringBuilder();
        for(Expr param : expr.parameters)
            funcString.append(param.accept(this)).append(',');
        funcString.delete(funcString.length()-1, funcString.length()).append(')');
        switch (expr.function.lexeme){
            case "max" ->{
                return funcString.insert(0, "max(").toString();
            }
            case "min" ->{
                return funcString.insert(0, "min(").toString();
            }
            case "root" ->{
                return funcString.insert(0, "root(").toString();
            }
            case "sqrt" ->{
                return funcString.insert(0, "sqrt(").toString();
            }
            case "abs" ->{
                return funcString.insert(0, "abs(").toString();
            }
            case "floor" ->{
                return funcString.insert(0, "floor(").toString();
            }
            case "ceil" ->{
                return funcString.insert(0, "ceil(").toString();
            }
            case "round" ->{
                return funcString.insert(0, "round(").toString();
            }
            case "log" ->{
                return funcString.insert(0, "log(").toString();
            }
        }
        return null;
    }
}

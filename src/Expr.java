import java.util.List;

abstract class Expr {
    interface Visitor<R> {
        R visitBinaryExpr(Binary expr);
        R visitGroupingExpr(Grouping expr);
        R visitLiteralExpr(Literal expr);
        R visitVectorExpr(Vector expr);
        R visitUnaryExpr(Unary expr);
        R visitFunctionExpr(Function expr);
    }

    static class Binary extends Expr {
        Binary(Expr left, Token operator, Expr right) {
            this.left = left;
            this.operator = operator;
            this.right = right;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitBinaryExpr(this);
        }

        final Expr left;
        final Token operator;
        final Expr right;
    }

    static class Grouping extends Expr {
        Grouping(Expr expression) {
            this.expression = expression;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitGroupingExpr(this);
        }

        final Expr expression;
    }

    static class Literal extends Expr {
        Literal(Double value) {
            this.value = value;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitLiteralExpr(this);
        }

        final Object value;
    }
    static class Vector extends Expr {
        Vector(List<Expr> values) {
            this.values = values;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitVectorExpr(this);
        }

        final List<Expr> values;
    }
    static class Unary extends Expr {
        Unary(Token operator, Expr right) {
            this.operator = operator;
            this.right = right;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitUnaryExpr(this);
        }

        final Token operator;
        final Expr right;
    }
    static class Function extends Expr {
        Function(Token function, List<Expr> parameters) {
            this.function = function;
            this.parameters = parameters;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitFunctionExpr(this);
        }

        final Token function;
        final List<Expr> parameters;
    }


    abstract <R> R accept(Visitor<R> visitor);
}

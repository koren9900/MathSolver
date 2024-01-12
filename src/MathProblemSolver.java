import java.util.List;

public class MathProblemSolver {
    private static boolean hadError = false;
    private static boolean hadRuntimeError = false;

    private static  List<Expr> expressions;
    MathProblemSolver(String exp){
        Scanner scanner = new Scanner(exp);
        List<Token> scanned = scanner.scanTokens();
        Parser parser = new Parser(scanned);
        expressions = parser.parse();

    }

    public String solve(){
        Interpreter interpreter = new Interpreter();
        List<Expr> evaluatedExpressions = interpreter.interpret(expressions);
        Stringer stringer = new Stringer();
        String s = stringer.stringify(expressions).toString();
        s += "=";
        s += stringer.stringify(evaluatedExpressions);
        return stringer.stringify(expressions).toString() + "=" + stringer.stringify(evaluatedExpressions);
    }
    static void error(int line, String message) {
        report(line, "", message);
    }
    private static void report(int line, String where, String message) {
        System.err.println("[line " + line + "] Error" + where + ": " + message);
        hadError = true;
    }

    static void error(Token token, String message) {
        if (token.type == TokenType.EOF) {
            report(token.line, " at end", message);
        } else {
            report(token.line, " at '" + token.lexeme + "'", message);
        }
    }
    static void runtimeError(RuntimeError error) {
        System.err.println(error.getMessage() +
                "\n[line " + error.token.line + "]");
        hadRuntimeError = true;
    }
}

package xu.oop.expression;

public enum Operator {
    PLUS('+'),
    MINUS('-'),
    MULTIPLY('x'),
    DIVIDE(':'),
    MOD('%'),
    RIGHT_PATHESIS('('),
    LEFT_PATHESIS(')');

    private char operator;

    private Operator(char operator) {
        this.operator = operator;
    }

    public char getOperator() {
        return operator;
    }

    public static boolean isOperator(char c) {
        for (Operator operator : Operator.values()) {
            if (operator.getOperator() == c) {
                return true;
            }
        }
        return false;
    }
}

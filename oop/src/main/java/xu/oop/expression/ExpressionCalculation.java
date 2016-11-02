package xu.oop.expression;

public class ExpressionCalculation {
    public class Node {
        Node left;
        Node right;
        int value;
        Operator operator;

        public Node getLeft() {
            return left;
        }

        public void setLeft(Node left) {
            this.left = left;
        }

        public Node getRight() {
            return right;
        }

        public void setRight(Node right) {
            this.right = right;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }

        public Operator getOperator() {
            return operator;
        }

        public void setOperator(Operator operator) {
            this.operator = operator;
        }
    }

    private Node root;

    public void build(String expression) {
        expression = expression.replaceAll(" ", "");

        for (int i = 0; i < expression.length(); i++) {
            char c = expression.charAt(i);
            if (Operator.isOperator(c)) {

            } else {
                // put node.
            }
        }
    }

    public int calculate() {
        return calculate(root);
    }

    public int calculate(Node node) {
        if (node == null) {
            // plus -> 0
            // multiply -> 1
            return 0;
        }

        if (node.right == null) {
            return node.value + calculate(node.left);
        } 
        if (node.left == null) {
            return node.value * calculate(node.right);
        }
        return 0;
    }

    public Node getRoot() {
        return root;
    }

    public void setRoot(Node root) {
        this.root = root;
    }
}

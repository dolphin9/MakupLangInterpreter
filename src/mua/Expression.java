package mua;

import mua.exceptions.MuaExceptions;
import mua.interfaces.Context;
import mua.interfaces.Fragment;
import mua.values.*;

import java.util.Stack;

public class Expression {
    private static int countBrackets(String str) {
        int count = 0;
        for (int i = 0; i < str.length(); ++i) {
            if (str.charAt(i) == '(')
                ++count;
            else if (str.charAt(i) == ')')
                --count;
        }
        return count;
    }

    private static StringBuilder addBracketsAndSpaces(StringBuilder stringBuilder) {
        for (int i = 0; i < stringBuilder.length(); ++i) {
            char ch = stringBuilder.charAt(i);
            if (ch == '+' || ch == '-' || ch == '*' || ch == '/' || ch == '%' || ch == '(' || ch == ')') {
                if (i + 1 < stringBuilder.length() && stringBuilder.charAt(i + 1) != ' ')
                    stringBuilder.insert(i + 1, ' ');
                if (i > 0 && stringBuilder.charAt(i - 1) != ' ')
                    stringBuilder.insert(i, ' ');
            }
        }
        if (stringBuilder.charAt(0) != '(' || stringBuilder.charAt(stringBuilder.length() - 1) != ')') {
            stringBuilder.insert(0, "( ");
            stringBuilder.append(" )");
        }
        return stringBuilder;
    }

    public static boolean isExpression(String str) {
        return str.contains("(") || str.contains(")") ||
                str.contains("+") || str.contains("-") ||
                str.contains("*") || str.contains("/") || str.contains("%");
    }

    public static WordValue build(String startItem, Fragment fragment) throws MuaExceptions {
        StringBuilder expr = new StringBuilder(startItem);
        int bracketCount = countBrackets(startItem);

        while (bracketCount != 0) {
            String nextInstruction = fragment.nextRawInstruction().toString();
            bracketCount += countBrackets(nextInstruction);
            expr.append(' ');
            expr.append(nextInstruction);
        }
        String result = addBracketsAndSpaces(expr).toString();
        return new WordValue(result);
    }

    public static Value evaluate(Context context, WordValue wordValue)
            throws MuaExceptions, Function.FunctionStop {
        String[] expression = wordValue.toString().split(" ");
        Stack<Weighted> opStack = new Stack<>();
        Stack<Value> argStack = new Stack<>();
        boolean afterBracket = false;

        for (String item : expression) {
            if (item.startsWith(":")) {
                afterBracket = false;
                argStack.push(context.getSymbol(item.substring(1)));
            } else if (NumberValue.isNumber(item)) {
                afterBracket = false;
                argStack.push(NumberValue.parse(item));
            } else {
                Weighted weightedOp;
                switch (item) {
                    case "(":
                        weightedOp = new OpenBracket();
                        break;
                    case "+":
                        weightedOp = new Add();
                        break;
                    case "-":
                        weightedOp = new Sub();
                        break;
                    case "*":
                        weightedOp = new Mul();
                        break;
                    case "/":
                        weightedOp = new Div();
                        break;
                    case "%":
                        weightedOp = new Mod();
                        break;
                    case ")":
                        while (!opStack.isEmpty() && !(opStack.peek() instanceof OpenBracket)) {
                            Operator op = (Operator) opStack.pop();
                            Value rhs = argStack.pop(), lhs = argStack.pop();
                            op.addArgument(lhs);
                            op.addArgument(rhs);
                            argStack.push(op.execute(context));
                        }
                        opStack.pop();
                        afterBracket = false;
                        continue;
                    default:
                        throw new MuaExceptions.UnknownOperatorException(item);
                }
                if (weightedOp.isArithmetic()) {
                    while (!opStack.isEmpty() && opStack.peek().isArithmetic() &&
                            opStack.peek().getWeight() >= weightedOp.getWeight()) {
                        Operator op = (Operator) opStack.pop();
                        Value rhs = argStack.pop(), lhs = argStack.pop();
                        op.addArgument(lhs);
                        op.addArgument(rhs);
                        argStack.push(op.execute(context));
                    }
                }
                opStack.push(weightedOp);
                if (afterBracket &&
                        (weightedOp instanceof Add || weightedOp instanceof Sub))
                    argStack.push(new NumberValue(0));
                afterBracket = weightedOp instanceof OpenBracket;
            }
        }
        return argStack.pop();
    }

    interface Weighted {
        int getWeight();

        default boolean isArithmetic() {
            return true;
        }
    }

    private static class OpenBracket implements Weighted {
        @Override
        public int getWeight() {
            return 0;
        }

        @Override
        public String toString() {
            return "(";
        }

        @Override
        public boolean isArithmetic() {
            return false;
        }
    }

    private static class Add extends Operator.Add implements Weighted {
        @Override
        public int getWeight() {
            return 1;
        }
    }

    private static class Sub extends Operator.Sub implements Weighted {
        @Override
        public int getWeight() {
            return 1;
        }
    }

    private static class Mul extends Operator.Mul implements Weighted {
        @Override
        public int getWeight() {
            return 2;
        }
    }

    private static class Div extends Operator.Div implements Weighted {
        @Override
        public int getWeight() {
            return 2;
        }
    }

    private static class Mod extends Operator.Mod implements Weighted {
        @Override
        public int getWeight() {
            return 2;
        }
    }
}

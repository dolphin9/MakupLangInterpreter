package mua.interfaces;

import mua.Expression;
import mua.SymbolTable;
import mua.exceptions.MuaExceptions;
import mua.values.Function;
import mua.values.NumberValue;
import mua.values.Value;
import mua.values.WordValue;

import java.util.Stack;

public interface Context extends Fragment {
    void addSymbol(String symbol, Value value);

    Value getSymbol(String symbol);

    boolean isSymbol(String string);

    void removeSymbol(String symbol);

    SymbolTable getSymbolTable();

    boolean isExecutable(String item);

    Executable getExecutable(String item) throws MuaExceptions;

    Value read() throws MuaExceptions;

    Value readList() throws MuaExceptions;

    void print(Value value);

    default Value nextInstruction() throws MuaExceptions, Function.FunctionStop {
        return nextInstruction(this);
    }

    default void run() throws MuaExceptions, Function.FunctionStop {
        run(this);
    }

    default Value nextInstruction(Fragment fragment)
            throws MuaExceptions, Function.FunctionStop {
        Value instruction = fragment.nextRawInstruction();
        if (instruction instanceof NumberValue)
            return instruction;
        else if (instruction instanceof WordValue) {
            String instructionStr = instruction.toString();
            if (instructionStr.startsWith(":")) {
                return getSymbol(instructionStr.substring(1));
            } else if (Expression.isExpression(instructionStr)) {
                return Expression.evaluate(this, (WordValue) instruction);
            } else if (instructionStr.contains("\"")) {
                return new WordValue(instructionStr.substring(1));
            } else if (isExecutable(instructionStr)) {
                return (Value) getExecutable(instructionStr);
            } else {
                throw new MuaExceptions.UnknownOperatorException(instructionStr);
            }
        } else {
            return instruction;
        }
    }

    default void run(Fragment fragment) throws MuaExceptions, Function.FunctionStop {
        Stack<Executable> opStack = new Stack<>();
        while (fragment.hasNextInstruction()) {
            Executable op = (Executable) nextInstruction(fragment);
            opStack.push(op);
            while (!opStack.isEmpty()) {
                // If the operator needs no more arguments, it is finished
                // and will be popped from the stack, and add to the argument
                // list of the last layer, or set it the root.
                while (!opStack.isEmpty() && !opStack.peek().needsMoreArguments()) {
                    Executable op2 = opStack.pop();
                    Value result = op2.execute(this);
                    if (!opStack.isEmpty())
                        opStack.peek().addArgument(result);
                }
                if (opStack.isEmpty())
                    break;
                // Executable op2 = opStack.peek();
                Value nextInstruction = nextInstruction(fragment);
                if (nextInstruction instanceof Executable)
                    opStack.push((Executable) nextInstruction);
                else
                    opStack.peek().addArgument(nextInstruction);
            }
        }
    }
}

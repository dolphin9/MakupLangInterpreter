package mua.interfaces;

import mua.Expression;
import mua.SymbolTable;
import mua.exceptions.MuaExceptions;
import mua.values.*;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Stack;

public interface Context extends Fragment {
    default void addSymbol(String symbol, Value value) {
        getSymbolTable().put(symbol, value);
    }

    default Value getSymbol(String symbol) {
        return getSymbolTable().get(symbol);
    }

    default boolean isSymbol(String string) {
        return getSymbolTable().hasSymbol(string);
    }

    default void removeSymbol(String symbol) {
        getSymbolTable().remove(symbol);
    }

    SymbolTable getSymbolTable();

    default boolean isExecutable(String item) {
        return isSymbol(item) && getSymbol(item) instanceof Executable;
    }

    default Executable getExecutable(String item) throws MuaExceptions {
        return ((Executable) getSymbol(item)).clone();
    }

    Value read() throws MuaExceptions;

    Value readList() throws MuaExceptions;

    void clear();

    default void listAll() {
        listAll(getSymbolTable());
    }

    void listAll(SymbolTable table);

    default void save(WordValue fileName) {
        try {
            FileOutputStream out = new FileOutputStream(fileName.toString());
            getSymbolTable().listAll(out);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    default void load(WordValue fileName) throws MuaExceptions {
        try {
            List<String> lines = Files.readAllLines(Paths.get(fileName.toString()));
            for (String line : lines) {
                String[] items = line.split(" ", 2);
                String word = items[0];
                String rawValue = items[1];

                if (rawValue.contains("[")) {
                    ListValue list = ListValue.Builder.fromString(rawValue);
                    if (Function.isFunction(list))
                        addSymbol(word, new Function(word, list));
                    else
                        addSymbol(word, list);
                } else if (NumberValue.isNumber(rawValue)) {
                    addSymbol(word, NumberValue.parse(rawValue));
                } else if (BoolValue.isBool(rawValue)) {
                    addSymbol(word, BoolValue.parse(rawValue));
                } else {
                    addSymbol(word, new WordValue(rawValue));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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
            } else if (instructionStr.contains("\"")) {
                return new WordValue(instructionStr.substring(1));
            } else if (Expression.isExpression(instructionStr)) {
                return Expression.evaluate(this, (WordValue) instruction);
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
                Executable op2 = opStack.peek();
                Value nextInstruction = nextInstruction(fragment);
                if (nextInstruction instanceof Executable)
                    opStack.push((Executable) nextInstruction);
                else
                    op2.addArgument(nextInstruction);
            }
        }
    }
}

package mua;

import mua.parser.SyntaxErrorException;

import java.io.IOException;
import java.util.Stack;

public interface Context extends Fragment {
    void addSymbol(String symbol, Value value);
    Value getSymbol(String symbol);
    boolean isSymbol(String string);
    void removeSymbol(String symbol);

    SymbolTable getSymbolTable();

    boolean isExecutable(String item);
    Executable getExecutable(String item) throws IllegalAccessException, InstantiationException;

    Value read() throws LexicalErrorException, SyntaxErrorException, IOException;
    Value readList() throws LexicalErrorException, SyntaxErrorException, IOException;

    void print(Value value);

    // Value parse(Object value) throws LexicalErrorException, SyntaxErrorException;

    // String lexerWait();

    // Queue<Token> parserWait() throws LexicalErrorException;

    default void run()
            throws SyntaxErrorException, LexicalErrorException, IOException, InstantiationException, IllegalAccessException {
        Stack<Executable> opStack = new Stack<>();
        while (hasNextInstruction()) {
            Value instruction = nextInstruction();
            if (instruction instanceof Executable)
                // opStack.push(Operator.extract(item));
                opStack.push((Executable) instruction);
            while (!opStack.isEmpty()) {
                Executable op = opStack.peek();
                if (op.needsMoreArguments()) {
                    final Value nextInstruction = nextInstruction();
                    if (nextInstruction instanceof Executable)
                        opStack.push((Executable) nextInstruction);
                    else
                        op.addArgument(nextInstruction);
                } else {
                    // If the operator needs no more arguments, it is finished
                    // and will be popped from the stack, and add to the argument
                    // list of the last layer, or set it the root.
                    while (!opStack.isEmpty()) {
                        Executable op2 = opStack.pop();
                        Value result = op2.execute(this);
                        if (!opStack.isEmpty())
                            opStack.peek().addArgument(result);
                    }
                }
            }
        }
    }

    // boolean isNested();

    // void run(Operator op) throws LexicalErrorException, SyntaxErrorException;
}

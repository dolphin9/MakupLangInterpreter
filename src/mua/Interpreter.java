package mua;

import mua.lexer.*;
import mua.parser.*;

import java.io.PrintStream;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Queue;
import java.util.Scanner;

public class Interpreter {
    private SymbolTable mTable = new SymbolTable();
    private PrintStream mOut;
    private Scanner mScanner;

    public Interpreter(Scanner scanner, PrintStream printStream) {
        mScanner = scanner;
        mOut = printStream;
    }

    public void printPrompt() {
        mOut.print(" (mua) > ");
    }

    public void execute(String line) throws LexicalErrorException, SyntaxErrorException {
        Lexer lexer = new Lexer();
        Parser parser = new Parser();
        Queue<Token> tokenQueue = lexer.lex(line);

        ParseTree parseTree = parser.parse(tokenQueue);

        OperatorNode op = parseTree.getRoot();

        while (op != null) {
            ValueNode value = op.execute(new RunningContext() {
                @Override
                public void addSymbol(String symbol, Object value) {
                    mTable.put(symbol, value);
                }

                @Override
                public Object getSymbol(String symbol) {
                    return mTable.get(symbol);
                }

                @Override
                public boolean isSymbol(String string) {
                    return mTable.hasSymbol(string);
                }

                @Override
                public void removeSymbol(String symbol) {
                    mTable.remove(symbol);
                }

                @Override
                public void print(Object value) {
                    System.out.println(value);
                }

                @Override
                public ValueNode read() throws LexicalErrorException, SyntaxErrorException {
                    String item = mScanner.nextLine();
                    if (item.contains(" "))
                        throw new LexicalErrorException(); // TODO: 17-10-8 Too many items.
                    return parser.parse(lexer.lex(item).poll());
                }

                @Override
                public ValueNode readList() throws LexicalErrorException, SyntaxErrorException {
                    String line = mScanner.nextLine();
                    return parser.parse(lexer.lex("[" + line + "]").poll());
                }

                @Override
                public ValueNode parse(String string) throws LexicalErrorException, SyntaxErrorException {
                    return parser.parse(lexer.lex(string).poll());
                }

                @Override
                public OperatorNode parse(ArrayList<Token> tokens) throws LexicalErrorException, SyntaxErrorException {
                    return parser.parse(new ArrayDeque<Token>(tokens)).getRoot();
                }
            });
            if (value == null)
                break;
            if (value.isOperator())
                op = (OperatorNode) value;
            else if (value.isString())
                op = parser.parse(lexer.lex((String) value.getValue())).getRoot();
            else if (value.isList()) {
                Queue<Token> tokens = new ArrayDeque<Token>((ArrayList<Token>) value.getValue());
                op = parser.parse(tokens).getRoot();
            }
        }

        // tokenQueue.forEach(mOut::println);
    }
}

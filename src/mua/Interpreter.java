package mua;

import mua.lexer.Lexer;
import mua.lexer.LexicalErrorException;
import mua.lexer.Token;
import mua.parser.*;

import java.io.PrintStream;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.Scanner;

public class Interpreter {
    private static final String PROMPT = "(mua) > ";
    private static final String CMD_WAIT_PROMPT = " ...  > ";
    private static final String READ_PROMPT = " ...  : ";
    private static final String READLINST_PROMPT = "..... : ";
    private SymbolTable mTable = new SymbolTable();
    private PrintStream mOut;
    private Scanner mScanner;
    private Context mContext = new Context();
    private Lexer mLexer = new Lexer(mContext);
    private Parser mParser = new Parser(mContext);

    public Interpreter(Scanner scanner, PrintStream printStream) {
        mScanner = scanner;
        mOut = printStream;
    }

    public void printPrompt() {
        mOut.print(PROMPT);
    }

    public void execute(String line) throws LexicalErrorException, SyntaxErrorException {
        Lexer lexer = mLexer;
        Parser parser = mParser;
        Queue<Token> tokenQueue = lexer.lex(line);
        ParseTree parseTree = parser.parse(tokenQueue);
        OperatorNode op = parseTree.getRoot();

        // mContext.run(op);
        // tokenQueue.forEach(mOut::println);
    }

    private class Context implements mua.Context {
        private boolean mNested = false;

        @Override
        public boolean isNested() {
            return mNested;
        }

        private void setNested(boolean nested) {
            mNested = nested;
        }

        @Override
        public void run(OperatorNode op) throws LexicalErrorException, SyntaxErrorException {
            setNested(false);
            ValueNode value = op.execute(this);
            setNested(true);

            while (value != null) {
                if (value.isOperator())
                    op = (OperatorNode) value;
                else if (value.isString())
                    op = mParser.parse(mLexer.lex((String) value.getValue())).getRoot();
                else if (value.isList()) {
                    Queue<Token> tokens = new ArrayDeque<>(((ListNode) value).getTokens());
                    op = mParser.parse(tokens).getRoot();
                } else
                    throw new UnknownOperatorException();
                if (op == null)
                    break;
            }
        }

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
            mOut.print(READ_PROMPT);
            String item = mScanner.nextLine();
            if (item.contains(" "))
                throw new LexicalErrorException(); // TODO: 17-10-8 Too many items.
            return mParser.parse(mLexer.lex(item).poll());
        }

        @Override
        public ValueNode readList() throws LexicalErrorException, SyntaxErrorException {
            mOut.print(READLINST_PROMPT);
            String line = mScanner.nextLine();
            return mParser.parse(mLexer.lex("[" + line + "]").poll());
        }

        @Override
        public ValueNode parse(Object value) throws LexicalErrorException, SyntaxErrorException {
            Token token = mLexer.lex(value);
            if (token != null)
                return mParser.parse(token);
            else
                return null;
        }

        @Override
        public String lexerWait() {
            mOut.print(CMD_WAIT_PROMPT);
            return mScanner.nextLine();
        }

        @Override
        public Queue<Token> parserWait() throws LexicalErrorException {
            mOut.print(CMD_WAIT_PROMPT);
            return mLexer.lex(mScanner.nextLine());
        }
    }
}

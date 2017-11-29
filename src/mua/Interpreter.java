package mua;

import mua.parser.SyntaxErrorException;
import mua.parser.UnknownOperatorException;

import java.io.IOException;
import java.io.PrintStream;
import java.util.Scanner;
import java.util.Stack;
import java.util.logging.Logger;

public class Interpreter implements Context {
    public static final Logger LOGGER = Logger.getGlobal();
    // private static final String PROMPT = "(mua) > ";
    // private static final String CMD_WAIT_PROMPT = " ...  > ";
    // private static final String READ_PROMPT = " ...  : ";
    // private static final String READLINST_PROMPT = "..... : ";
    private SymbolTable mSymbolTable = new SymbolTable();
    private PrintStream mOut;
    // private PrintStream mPrompt;
    private Scanner mCodeScanner;
    private Scanner mInputScanner;
    // private Context mContext = new Context();
    // private Lexer mLexer = new Lexer(mContext);
    // private Parser mParser = new Parser(mContext);

    private boolean mNested = false;

    {
        mSymbolTable.put("make", new Operator.Make());
        mSymbolTable.put("thing", new Operator.Thing());
        mSymbolTable.put("erase", new Operator.Erase());
        mSymbolTable.put("isname", new Operator.IsName());
        mSymbolTable.put("print", new Operator.Print());
        mSymbolTable.put("read", new Operator.Read());
        mSymbolTable.put("readlinst", new Operator.ReadList());
        mSymbolTable.put("add", new Operator.Add());
        mSymbolTable.put("sub", new Operator.Sub());
        mSymbolTable.put("mul", new Operator.Mul());
        mSymbolTable.put("div", new Operator.Div());
        mSymbolTable.put("mod", new Operator.Mod());
        mSymbolTable.put("eq", new Operator.Eq());
        mSymbolTable.put("gt", new Operator.Gt());
        mSymbolTable.put("lt", new Operator.Lt());
        mSymbolTable.put("and", new Operator.And());
        mSymbolTable.put("or", new Operator.Or());
        mSymbolTable.put("not", new Operator.Not());
    }

    public Interpreter(Scanner scanner, PrintStream printStream) {
        mCodeScanner = scanner;
        mOut = printStream;
        mInputScanner = scanner;
        // mPrompt = printStream;
    }

    /*
    public Interpreter(Scanner scanner, PrintStream printStream, PrintStream promptStream) {
        mCodeScanner = scanner;
        mOut = printStream;
        mPrompt = promptStream;
    }

    public void printPrompt() {
        mPrompt.print(PROMPT);
    }
    */
    static public boolean isNumber(String item) {
        return item.startsWith("-") || !item.isEmpty() && Character.isDigit(item.charAt(0));
    }

    private String nextInstruction() throws IOException {
        if (!mInputScanner.hasNext())
            throw new IOException();
        return mInputScanner.next();
    }

    /*
    public Value parseItem(String item) throws LexicalErrorException {
        if (isNumber(item)) {
            return NumberValue.parse(item);
        } else if (item.startsWith(":")) {
            return getSymbol(item.substring(1));
        } else if (item.startsWith("\""))
            return new WordValue(item.substring(1));
        else
            throw new LexicalErrorException();
    }
    */

    public void startInterpret()
            throws SyntaxErrorException, LexicalErrorException,
            IOException, InstantiationException, IllegalAccessException {
        Stack<Executable> opStack = new Stack<>();
        String item = nextInstruction();
        if (isExecutable(item))
            // opStack.push(Operator.extract(item));
            opStack.push(getExecutable(item));
        while (!opStack.isEmpty()) {
            Executable op = opStack.peek();
            if (op.needsMoreArguments()) {
                final String nextItem = nextInstruction();
                if (nextItem.startsWith("[")) {
                    op.addArgument(ListValue.Builder.fromCode(mCodeScanner, nextItem));
                } else if (nextItem.startsWith("\"")) {
                    op.addArgument(new WordValue(nextItem.substring(1)));
                } else if (nextItem.startsWith(":")) {
                    op.addArgument(getSymbol(nextItem.substring(1)));
                } else if (NumberValue.isNumber(nextItem)) {
                    op.addArgument(NumberValue.parse(nextItem));
                } else {
                    if (isExecutable(nextItem))
                        opStack.push(getExecutable(nextItem));
                    else
                        throw new UnknownOperatorException();
                }
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

    /*
    public void execute(String line) throws LexicalErrorException, SyntaxErrorException {
        Lexer lexer = mLexer;
        Parser parser = mParser;
        Queue<Token> tokenQueue = lexer.lex(line);
        ParseTree parseTree = parser.parse(tokenQueue);
        Operator op = parseTree.getRoot();

        mContext.run(op);
        // tokenQueue.forEach(mOut::println);
    }
    */

    @Override
    public boolean isNested() {
        return mNested;
    }

    private void setNested(boolean nested) {
        mNested = nested;
    }

        /*
        @Override
        public void run(Operator op) throws LexicalErrorException, SyntaxErrorException {
            setNested(false);
            Value value = op.execute(this);
            setNested(true);

            while (value != null) {
                if (value.isOperator())
                    op = (Operator) value;
                else if (value.isString())
                    op = mParser.parse(mLexer.lex((String) value.getValue())).getRoot();
                else if (value.isList()) {
                    Queue<Token> tokens = new ArrayDeque<>(((ListValue) value).getTokens());
                    op = mParser.parse(tokens).getRoot();
                } else
                    throw new UnknownOperatorException();
                if (op == null)
                    break;
            }
        }
        */

    @Override
    public void addSymbol(String symbol, Value value) {
        mSymbolTable.put(symbol, value);
    }

    @Override
    public Value getSymbol(String symbol) {
        return mSymbolTable.get(symbol);
    }

    @Override
    public boolean isSymbol(String string) {
        return mSymbolTable.hasSymbol(string);
    }

    @Override
    public void removeSymbol(String symbol) {
        mSymbolTable.remove(symbol);
    }

    @Override
    public void print(Value value) {
        mOut.println(value);
    }

    @Override
    public boolean isExecutable(String item) {
        return mSymbolTable.hasSymbol(item) && mSymbolTable.get(item) instanceof Executable;
    }

    @Override
    public Executable getExecutable(String item) throws IllegalAccessException, InstantiationException {
        return (Executable) mSymbolTable.get(item).getClass().newInstance();
    }

    @Override
    public Value read() throws LexicalErrorException, SyntaxErrorException, IOException {
        // mPrompt.print(READ_PROMPT);
        String item = nextInstruction();
        if (NumberValue.isNumber(item)) {
            return NumberValue.parse(item);
        } else
            return new WordValue(item);
    }

    @Override
    public Value readList() throws LexicalErrorException, SyntaxErrorException, IOException {
        // String line = mCodeScanner.nextLine();
        return ListValue.Builder.fromInput(mInputScanner);
        // return mParser.parse(mLexer.lex("[" + line + "]").poll());
    }

    /*
    @Override
    public Value parse(Object value) throws LexicalErrorException, SyntaxErrorException {
        Token token = mLexer.lex(value);
        if (token != null)
            return mParser.parse(token);
        else
            return null;
    }
    */

    /*
    @Override
    public String lexerWait() {
        // mPrompt.print(CMD_WAIT_PROMPT);
        return mCodeScanner.nextLine();
    }

    @Override
    public Queue<Token> parserWait() throws LexicalErrorException {
        // mPrompt.print(CMD_WAIT_PROMPT);
        return mLexer.lex(mCodeScanner.nextLine());
    }
    */
}

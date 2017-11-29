package mua;

import mua.parser.SyntaxErrorException;

import java.io.IOException;

public interface Context {
    void addSymbol(String symbol, Value value);

    Value getSymbol(String symbol);
    boolean isSymbol(String string);
    void removeSymbol(String symbol);

    void print(Value value);

    boolean isExecutable(String item);

    Executable getExecutable(String item) throws IllegalAccessException, InstantiationException;

    Value read() throws LexicalErrorException, SyntaxErrorException, IOException;

    Value readList() throws LexicalErrorException, SyntaxErrorException, IOException;
    // Value parse(Object value) throws LexicalErrorException, SyntaxErrorException;

    // String lexerWait();

    // Queue<Token> parserWait() throws LexicalErrorException;

    boolean isNested();

    // void run(Operator op) throws LexicalErrorException, SyntaxErrorException;
}

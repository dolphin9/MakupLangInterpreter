package mua;

import mua.lexer.LexicalErrorException;
import mua.lexer.Token;
import mua.parser.OperatorNode;
import mua.parser.SyntaxErrorException;
import mua.parser.ValueNode;

import java.util.Queue;

public interface Context {
    void addSymbol(String symbol, Object value);
    Object getSymbol(String symbol);
    boolean isSymbol(String string);
    void removeSymbol(String symbol);

    void print(Object value);

    ValueNode read() throws LexicalErrorException, SyntaxErrorException;
    ValueNode readList() throws LexicalErrorException, SyntaxErrorException;
    ValueNode parse(Object value) throws LexicalErrorException, SyntaxErrorException;

    String lexerWait();

    Queue<Token> parserWait() throws LexicalErrorException;

    boolean isNested();

    void run(OperatorNode op) throws LexicalErrorException, SyntaxErrorException;
}

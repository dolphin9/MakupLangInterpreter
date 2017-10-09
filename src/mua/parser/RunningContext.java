package mua.parser;

import mua.lexer.LexicalErrorException;
import mua.lexer.Token;

import java.util.ArrayList;

public interface RunningContext {
    void addSymbol(String symbol, Object value);
    Object getSymbol(String symbol);
    boolean isSymbol(String string);
    void removeSymbol(String symbol);

    void print(Object value);

    ValueNode read() throws LexicalErrorException, SyntaxErrorException;
    ValueNode readList() throws LexicalErrorException, SyntaxErrorException;
    ValueNode parse(Object value) throws LexicalErrorException, SyntaxErrorException;
    // OperatorNode parse(ArrayList<Token> tokens) throws LexicalErrorException, SyntaxErrorException;
}

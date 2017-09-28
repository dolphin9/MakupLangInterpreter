package mua;

import mua.lexer.*;

import java.util.ArrayList;

public class Interpreter {
    private SymbolTable mTable = new SymbolTable();

    public String getPrompt() {
        return " (mua) > ";
    }

    public void execute(String line) throws LexicalErrorException {
        Lexer lexer = new Lexer();
        ArrayList<Token> tokenList = lexer.lex(line);
        tokenList.forEach(System.out::println);
    }
}

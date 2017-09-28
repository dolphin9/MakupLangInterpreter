package mua;

import mua.lexer.Token;

import java.util.HashMap;

public class SymbolTable {
    /** Symbol table of the program */
    private HashMap<String, Token> mTable = new HashMap<>();

    public Token get(String word) {
        return mTable.get(word);
    }

    public void put(String word, Token value) {
        mTable.put(word, value);
    }
}

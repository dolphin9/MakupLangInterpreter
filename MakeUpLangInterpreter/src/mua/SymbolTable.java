package mua;

import mua.lexer.Token;

import java.util.HashMap;

public class SymbolTable {
    /** Symbol table of the program */
    private HashMap<String, Object> mTable = new HashMap<>();

    public Object get(String word) {
        return mTable.get(word);
    }

    public void put(String word, Object value) {
        mTable.put(word, value);
    }

    public void remove(String word) {
        mTable.remove(word);
    }

    public boolean hasSymbol(String word) {
        return mTable.containsKey(word);
    }
}

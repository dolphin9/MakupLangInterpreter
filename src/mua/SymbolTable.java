package mua;

import java.util.HashMap;

public class SymbolTable {
    /** Symbol table of the program */
    private HashMap<String, Value> mTable = new HashMap<>();

    public Value get(String word) {
        return mTable.get(word);
    }

    public void put(String word, Value value) {
        mTable.put(word, value);
    }

    public void remove(String word) {
        mTable.remove(word);
    }

    public boolean hasSymbol(String word) {
        return mTable.containsKey(word);
    }
}

package mua;

import java.util.HashMap;

public class SymbolTable {
    /** Symbol table of the program */
    private HashMap<String, Literal> mTable = new HashMap<>();

    public Literal get(String word) {
        return mTable.get(word);
    }

    public Literal get(WordLiteral word) {
        return mTable.get(word.getValue());
    }

    public void put(WordLiteral word, Literal value) {
        mTable.put(word.getValue(), value);
    }
}

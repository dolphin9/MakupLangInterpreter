package mua;

import mua.values.Function;
import mua.values.Operator;
import mua.values.Value;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;

class ReversedSymbolTable {
    /**
     * Reversed symbol table of the program
     */
    private static final HashMap<String, Value> TABLE = new HashMap<>();

    static {
        TABLE.putAll(Operator.DEFINED_OPS);
        TABLE.putAll(Function.FUNCTION_OPS);
    }

    protected Value get(String word) {
        return TABLE.get(word);
    }

    protected boolean hasSymbol(String word) {
        return TABLE.containsKey(word);
    }
}

public class SymbolTable extends ReversedSymbolTable {
    /** Symbol table of the program */
    private HashMap<String, Value> mTable = new HashMap<>();

    @Override
    public Value get(String word) {
        if (super.hasSymbol(word))
            return super.get(word);
        else
            return mTable.get(word);
    }

    public void put(String word, Value value) {
        if (!super.hasSymbol(word))
            mTable.put(word, value);
    }

    public void remove(String word) {
        mTable.remove(word);
    }

    public boolean hasSymbol(String word) {
        return super.hasSymbol(word) || mTable.containsKey(word);
    }

    public void merge(SymbolTable other) {
        mTable.putAll(other.mTable);
    }

    public void clear() {
        mTable.clear();
    }

    public void listAll(final OutputStream out) {
        final StringBuilder content = new StringBuilder();
        mTable.forEach((word, value) -> {
            content.append(word.toString());
            content.append(" ");
            // if (value instanceof Function)
            // content.append(((ListValue) value).toString());
            // else
            content.append(value.toString());
            content.append("\n");
        });
        try {
            out.write(content.toString().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

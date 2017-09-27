package mua;

import java.util.ArrayList;

public abstract class Literal {
    protected enum Type {
        kNumber,
        kWord,
        kList,
    }
    /** The type of literal */
    protected Type mType;

    protected Literal(Type type) {
        mType = type;
    }

    public boolean isNumber() {
        return mType == Type.kNumber;
    }

    public boolean isWord() {
        return mType == Type.kWord;
    }

    public boolean isList() {
        return mType == Type.kList;
    }

    /** Get the value of the literal, which depends on the type. */
    public abstract Object getValue();

    /** Parse a literal from a string */
    public static Literal parse(String[] strings) {
        // TODO: 17-9-27  To be implemented.
    }
}

class NumberLiteral extends Literal {

    NumberLiteral() {
        super(Type.kNumber);
        // TODO: 17-9-27 To be implemented.
    }

    @Override
    public Number getValue() {
        // TODO: 17-9-27  To be implemented.
    }
}

class WordLiteral extends Literal {
    private String mWord;

    WordLiteral(String word) {
        super(Type.kWord);
        mWord = word;
    }

    @Override
    public String getValue() {
        return mWord;
    }
}

class ListLiteral extends Literal {
    private ArrayList<Literal> mList;

    ListLiteral(ArrayList<Literal> list) {
        super(Type.kList);
        mList = list;
    }

    @Override
    public ArrayList<Literal> getValue() {
        return mList;
    }
}

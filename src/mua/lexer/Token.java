package mua.lexer;

import java.util.ArrayList;

public abstract class Token {
    protected enum Type {
        kNumber,
        kWord,
        kList,
        kOperator,
    }
    /** The type of token */
    protected Type mType;

    protected Token(Type type) {
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

    public boolean isOperator() {
        return mType == Type.kOperator;
    }

    /** Get the value of the token, which depends on the type. */
    public abstract Object getValue();
}

class NumberToken extends Token {
    private boolean mIsFloat;
    private int mIntValue;
    private double mFloatValue;

    NumberToken(String numStr) {
        super(Type.kNumber);
        mIsFloat = numStr.contains(".");
        if (mIsFloat)
            mFloatValue = Double.valueOf(numStr);
        else
            mIntValue = Integer.valueOf(numStr);
    }

    @Override
    public String toString() {
        return "<Number>" + getValue().toString();
    }

    @Override
    public Number getValue() {
        if (mIsFloat)
            return mFloatValue;
        else
            return mIntValue;
    }
}

class WordToken extends Token {
    private String mWord;

    WordToken(String word) {
        super(Type.kWord);
        mWord = word;
    }

    @Override
    public String getValue() {
        return mWord;
    }

    @Override
    public String toString() {
        return "<Word>" + mWord;
    }
}

class ListToken extends Token {
    private ArrayList<Token> mList;

    ListToken(ArrayList<Token> list) {
        super(Type.kList);
        mList = list;
    }

    @Override
    public ArrayList<Token> getValue() {
        return mList;
    }

    public Token get(int index) {
        return mList.get(index);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("<List>[");
        for (Token token : mList) {
            builder.append(token);
            builder.append(", ");
        }
        builder.append("]");
        return builder.toString();
    }
}

package mua.lexer;

public abstract class Token {
    /** The type of token */
    protected TokenType mTokenType;
    protected Token(TokenType type) {
        mTokenType = type;
    }

    public boolean isNumber() {
        return mTokenType == TokenType.kNumber;
    }

    public boolean isString() {
        return mTokenType == TokenType.kString;
    }

    public boolean isList() {
        return mTokenType == TokenType.kList;
    }

    protected boolean isSymbol() {
        return mTokenType == TokenType.kSymbol;
    }

    /** Get the value of the token, which depends on the type. */
    public abstract Object getValue();

    protected enum TokenType {
        kNumber,
        kString,
        kList,
        kSymbol,
    }
}

class NumberToken extends Token {
    private boolean mIsFloat;
    private int mIntValue;
    private double mFloatValue;

    NumberToken(Number number) {
        super(TokenType.kNumber);
        mIsFloat = !(number instanceof Integer);
        if (mIsFloat)
            mFloatValue = number.doubleValue();
        else
            mIntValue = number.intValue();
    }

    NumberToken(String numStr) {
        super(TokenType.kNumber);
        mIsFloat = numStr.contains(".");
        if (mIsFloat)
            mFloatValue = Double.valueOf(numStr);
        else
            mIntValue = Integer.valueOf(numStr);
    }

    @Override
    public String toString() {
        return getValue().toString();
    }

    @Override
    public Number getValue() {
        if (mIsFloat)
            return mFloatValue;
        else
            return mIntValue;
    }
}

class StringToken extends Token {
    private String mWord;

    StringToken(String word) {
        super(TokenType.kString);
        mWord = word;
    }

    @Override
    public String getValue() {
        return mWord;
    }

    @Override
    public String toString() {
        return mWord;
    }
}


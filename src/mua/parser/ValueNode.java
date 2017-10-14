package mua.parser;

public abstract class ValueNode extends ParseNode {
    abstract public Object getValue();
}

class NumberNode extends ValueNode {
    private Number mValue;

    NumberNode(Number number) {
        mValue = number;
    }

    @Override
    public boolean isNumber() {
        return true;
    }

    @Override
    public Number getValue() {
        return mValue;
    }
}

class BoolNode extends ValueNode {
    private boolean mValue;

    BoolNode(boolean bool) {
        mValue = bool;
    }

    @Override
    public boolean isBool() {
        return true;
    }

    @Override
    public Boolean getValue() {
        return mValue;
    }
}

class StringNode extends ValueNode {
    protected String mValue;

    StringNode(String value) {
        mValue = value;
    }

    @Override
    public String getValue() {
        return mValue;
    }

    @Override
    public boolean isString() {
        return true;
    }

}

class WordNode extends StringNode {

    WordNode(String word) {
        super(word);
    }

    @Override
    public String getValue() {
        return mValue;
    }

    @Override
    public boolean isWord() {
        return true;
    }

}


package mua.parser;

import mua.lexer.Token;

import java.util.ArrayList;

public abstract class ValueNode extends ParseNode {
    @Override
    public boolean isValue() {
        return true;
    }

    @Override
    public boolean match(NodeType other) {
        return other.isValue();
    }

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
    public boolean match(NodeType other) {
        return other.isNumber() || other.isValue();
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
    public boolean match(NodeType other) {
        return other.isBool() || other.isValue();
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

    @Override
    public boolean match(NodeType other) {
        return other.isString() || other.isValue();
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

    @Override
    public boolean match(NodeType other) {
        return other.isWord();
    }
}

class ListNode extends ValueNode {
    private ArrayList<Token> mValue;

    ListNode(ArrayList<Token> value) {
        mValue = value;
    }

    @Override
    public ArrayList<Token> getValue() {
        return mValue;
    }

    @Override
    public boolean isList() {
        return true;
    }

    @Override
    public boolean match(NodeType other) {
        return other.isList() || other.isValue();
    }
}

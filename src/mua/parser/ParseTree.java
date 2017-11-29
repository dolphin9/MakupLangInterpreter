package mua.parser;

import mua.Operator;

public class ParseTree {
    private Operator mRoot;

    public ParseTree(Operator root) {
        mRoot = root;
    }

    public Operator getRoot() {
        return mRoot;
    }
}

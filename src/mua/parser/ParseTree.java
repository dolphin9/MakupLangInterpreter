package mua.parser;

public class ParseTree {
    private OperatorNode mRoot;

    public ParseTree(OperatorNode root) {
        mRoot = root;
    }

    public OperatorNode getRoot() {
        return mRoot;
    }
}

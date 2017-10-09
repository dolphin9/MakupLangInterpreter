package mua.parser;

import java.util.Iterator;

public class ParseTree implements Iterable<ParseNode> {
    private OperatorNode mRoot;

    public ParseTree(OperatorNode root) {
        mRoot = root;
        buildTree(mRoot);
    }

    public OperatorNode getRoot() {
        return mRoot;
    }

    private void buildTree(OperatorNode op) {
        for (ParseNode child : op.getArguments()) {
            if (child.isOperator())
                buildTree((OperatorNode) child);
            child.setParent(op);
        }
    }

    @Override
    public Iterator<ParseNode> iterator() {
        return new Iterator<ParseNode>() {
            @Override
            public boolean hasNext() {
                return false;
            }

            @Override
            public ParseNode next() {
                return null;
            }
        };
    }
}

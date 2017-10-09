package mua.parser;

interface NodeType {
    boolean isWord();
    boolean isValue();
    boolean isNumber();
    boolean isBool();
    boolean isString();
    boolean isList();
    boolean match(NodeType other);
}

public class ParseNode implements NodeType {
    protected static final NodeType kWord = new WordNode(null);
    protected static final NodeType kValue = new ValueNode() {
        @Override
        public Object getValue() {
            return null;
        }
    };
    protected static final NodeType kNumber = new NumberNode(null);
    protected static final NodeType kBool = new BoolNode(false);
    protected static final NodeType kString = new StringNode(null);
    protected static final NodeType kList = new ListNode(null);

    private OperatorNode mParent = null;

    @Override
    public boolean isValue() {
        return false;
    }

    @Override
    public boolean isWord() {
        return false;
    }

    @Override
    public boolean isNumber() {
        return false;
    }

    @Override
    public boolean isBool() {
        return false;
    }

    @Override
    public boolean isString() {
        return false;
    }

    @Override
    public boolean isList() {
        return false;
    }

    @Override
    public boolean match(NodeType other) {
        return false;
    }

    public boolean isOperator() {
        return false;
    }

    public void setParent(OperatorNode mParent) {
        this.mParent = mParent;
    }

    public ParseNode getParent() {
        return mParent;
    }

    static NodeType or(NodeType lhs, NodeType rhs) {
        return new NodeType() {
            @Override
            public boolean isValue() {
                return lhs.isValue() || rhs.isValue();
            }

            @Override
            public boolean isWord() {
                return lhs.isWord() || rhs.isWord();
            }

            @Override
            public boolean isNumber() {
                return lhs.isNumber() || rhs.isNumber();
            }

            @Override
            public boolean isBool() {
                return lhs.isBool() || rhs.isBool();
            }

            @Override
            public boolean isString() {
                return lhs.isString() || rhs.isString();
            }

            @Override
            public boolean isList() {
                return lhs.isList() || rhs.isList();
            }

            @Override
            public boolean match(NodeType other) {
                return lhs.match(other) || rhs.match(other);
            }
        };
    }
}


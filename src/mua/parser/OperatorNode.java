package mua.parser;

import mua.lexer.OperatorToken;
import mua.lexer.Token;

import java.util.ArrayList;

class ParseNode {
    protected Token mToken;

    public ParseNode(Token token) {
        mToken = token;
    }

    public Object getValue() {
        return mToken.getValue();
    }
}

public class OperatorNode extends ParseNode {
    private Runnable mFunction;

    private ArrayList<ParseNode> mArguments;

    public OperatorNode(OperatorToken token) {
        super(token);
    }

}

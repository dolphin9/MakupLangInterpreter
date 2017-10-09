package mua.parser;

import mua.lexer.ListToken;
import mua.lexer.Token;

import java.util.ArrayList;

public class ListNode extends ValueNode {
    private ListToken mToken;

    ListNode(ListToken token) {
        mToken = token;
    }

    @Override
    public ListToken getValue() {
        return mToken;
    }

    public ArrayList<Token> getTokens() {
        return mToken.getValue();
    }

    @Override
    public boolean isList() {
        return true;
    }
}

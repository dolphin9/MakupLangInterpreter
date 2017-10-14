package mua.lexer;

import java.util.ArrayList;

public class ListToken extends Token {
    private ArrayList<Token> mList;

    ListToken(ArrayList<Token> list) {
        super(TokenType.kList);
        mList = list;
    }

    @Override
    public ArrayList<Token> getValue() {
        return mList;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("[");
        for (int i = 0; i < mList.size(); ++i) {
            builder.append(mList.get(i));
            if (i < mList.size() - 1)
                builder.append(" ");
        }
        builder.append("]");
        return builder.toString();
    }
}

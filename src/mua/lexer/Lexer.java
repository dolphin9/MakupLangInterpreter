package mua.lexer;

import mua.Context;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Queue;
import java.util.Stack;

public class Lexer {
    private Context mContext;

    public Lexer(Context context) {
        mContext = context;
    }

    static private String removeComments(String line) {
        return line.replaceFirst("//.*$", "");
    }

    static private boolean isStringToken(String item) {
        return !isNumberToken(item)
                && !hasListBeginToken(item)
                && !hasListEndToken(item);
    }

    static private boolean isNumberToken(String item) {
        return item.startsWith("-") || !item.isEmpty() && Character.isDigit(item.charAt(0));
    }

    static private boolean hasListBeginToken(String item) {
        return item.startsWith("[");
    }

    static private boolean hasListEndToken(String item) {
        return item.endsWith("]");
    }

    public Queue<Token> lex(String line) throws LexicalErrorException {
        line = removeComments(line);

        String[] items = line.split(" ");
        int nItems = items.length;
        Queue<Token> tokenList = new ArrayDeque<>();
        Stack<Token> tokenStack = new Stack<>();
        for (int i = 0; i < nItems; ++i) {
            String item = items[i];
            if (isNumberToken(item)) {
                tokenList.add(new NumberToken(item));
            } else if (isStringToken(item)) {
                tokenList.add(new StringToken(item));
            } else if (hasListBeginToken(item)) {
                int bracketCount = 0;
                for (int j = i; j < nItems; ++j) {
                    int startLoc = 0;
                    int endLoc = items[j].length();
                    while (startLoc < endLoc && items[j].charAt(startLoc) == '[') {
                        tokenStack.push(new SymbolToken(SymbolToken.SymbolType.kListBegin));
                        ++bracketCount;
                        ++startLoc;
                    }
                    while (startLoc < endLoc && items[j].charAt(endLoc - 1) == ']') {
                        --endLoc;
                        --bracketCount;
                    }
                    String subStr = items[j].substring(startLoc, endLoc);
                    if (isNumberToken(subStr)) {
                        tokenStack.push(new NumberToken(subStr));
                    } else if (!subStr.isEmpty()) {
                        tokenStack.push(new StringToken(subStr));
                    }
                    while (endLoc < items[j].length()) {
                        if (tokenStack.empty())
                            throw new BracketNotPairedException();
                        ArrayDeque<Token> subList = new ArrayDeque<>();
                        while (!tokenStack.empty()) {
                            Token token = tokenStack.pop();
                            if (token.isSymbol()
                                    && ((SymbolToken) token).getSymbolType() == SymbolToken.SymbolType.kListBegin) {
                                tokenStack.add(new ListToken(new ArrayList<>(subList)));
                                break;
                            } else {
                                subList.addFirst(token);
                            }
                        }
                        ++endLoc;
                    }
                    if (bracketCount == 0) {
                        tokenList.add(tokenStack.pop());
                        i = j;
                        break;
                    }
                    while (j == nItems - 1 && !tokenStack.empty()) {
                        String nextPart = mContext.lexerWait();
                        nextPart = removeComments(nextPart);
                        line += " " + nextPart;
                        items = line.split(" ");
                        nItems = items.length;
                    }
                }
            }
        }
        return tokenList;
    }

    public Token lex(Object value) throws LexicalErrorException {
        if (value instanceof Number)
            return new NumberToken((Number) value);
        else if (value instanceof String)
            return new StringToken((String) value);
        else if (value instanceof ListToken)
            return (ListToken) value;
        else
            return null;
    }
}

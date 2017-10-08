package mua.lexer;

import java.util.*;

public class Lexer {
    static private String removeComments(String line) {
        return line.replaceFirst("//.*$", "");
    }

    static private boolean isWordToken(String item) {
        return (item.startsWith("\"") || item.startsWith(":"))
                || !hasListBeginToken(item) && !hasListEndToken(item);
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
            } else if (isWordToken(item)) {
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
                        if (!tokenStack.empty())
                            throw new BracketNotPairedException();
                        i = j;
                        break;
                    }
                }
            }
        }
        return tokenList;
    }

}

package mua.lexer;

import java.util.*;

public class Lexer {
    static private String removeComments(String line) {
        return line.replaceFirst("//.*$", "");
    }

    static private boolean isWordToken(String item) {
        return item.startsWith("\"");
    }

    static private boolean isThingOfWordToken(String item) {
        return item.startsWith(":");
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

    public ArrayList<Token> lex(String line) throws LexicalErrorException {
        // TODO: 17-9-27 To be implemented.
        line = removeComments(line);

        String[] items = line.split(" ");
        int nItems = items.length;
        ArrayList<Token> tokenList = new ArrayList<>();
        Stack<Token> tokenStack = new Stack<>();
        for (int i = 0; i < nItems; ++i) {
            String item = items[i];
            // Process the word literal
            if (isWordToken(item)) {
                tokenList.add(new WordToken(item.substring(1)));
            } else if (isThingOfWordToken(item)) {
                tokenList.add(new OperatorToken(OperatorToken.OpType.kThing));
                tokenList.add(new WordToken(item.substring(1)));
            } else if (isNumberToken(item)) {
                tokenList.add(new NumberToken(item));
            } else if (hasListBeginToken(item)) {
                int bracketCount = 0;
                for (int j = i; j < nItems; ++j) {
                    int startLoc = 0;
                    int endLoc = items[j].length();
                    while (startLoc < endLoc && items[j].charAt(startLoc) == '[') {
                        tokenStack.push(new OperatorToken(OperatorToken.OpType.kListBegin));
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
                        tokenStack.push(new WordToken(subStr));
                    }
                    while (endLoc < items[j].length()) {
                        if (tokenStack.empty())
                            throw new BracketNotPairedException();
                        ArrayDeque<Token> subList = new ArrayDeque<>();
                        while (!tokenStack.empty()) {
                            Token token = tokenStack.pop();
                            if (token.isOperator()
                                    && ((OperatorToken) token).getOpType() == OperatorToken.OpType.kListBegin) {
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

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

    /**
     * It generates a token list based on the input string.
     *
     * @param line the input string
     * @return the token list
     * @throws LexicalErrorException
     */
    public Queue<Token> lex(String line) throws LexicalErrorException {
        line = removeComments(line);

        String[] items = line.split(" ");
        int nItems = items.length;
        Queue<Token> tokenList = new ArrayDeque<>();
        // Use a token stack to process the list tokens.
        Stack<Token> tokenStack = new Stack<>();
        for (int i = 0; i < nItems; ++i) {
            String item = items[i];
            if (isNumberToken(item)) {
                // Process the number token
                tokenList.add(new NumberToken(item));
            } else if (isStringToken(item)) {
                // Process the string token
                tokenList.add(new StringToken(item));
            } else if (hasListBeginToken(item)) {
                // Process the list token.

                // Counter for the bracket, which is increased or decreased
                // when there is a "[" or "]".
                int bracketCount = 0;
                for (int j = i; j < nItems; ++j) {
                    // The start location and the end location of the current token,
                    // removing the brackets ("[" or "]") in it.
                    int startLoc = 0;
                    int endLoc = items[j].length();

                    // If there are "["s, just push it into the stack and modify the
                    // bracket counter and the start location.
                    while (startLoc < endLoc && items[j].charAt(startLoc) == '[') {
                        // Push the "[" into the token stack.
                        tokenStack.push(new SymbolToken(SymbolToken.SymbolType.kListBegin));
                        ++bracketCount;
                        ++startLoc;
                    }
                    // If there are "]"s, just modify the bracket counter and the end location.
                    while (startLoc < endLoc && items[j].charAt(endLoc - 1) == ']') {
                        --endLoc;
                        --bracketCount;
                    }
                    // Get the substring without brackets, which is list item to be added.
                    String subStr = items[j].substring(startLoc, endLoc);
                    // The substring is either a number token or a string token, if not empty.
                    if (isNumberToken(subStr)) {
                        tokenStack.push(new NumberToken(subStr));
                    } else if (!subStr.isEmpty()) {
                        tokenStack.push(new StringToken(subStr));
                    }
                    // If there are "]"s, meaning that the sublist is finished, which needs
                    // to be popped from the token stack, and pushed back to the stack, or to
                    // the token list, if the whole list token is finished.
                    while (endLoc < items[j].length()) {
                        // If there is no "[", the bracket was not paired properly.
                        if (tokenStack.empty())
                            throw new BracketNotPairedException();
                        ArrayDeque<Token> subList = new ArrayDeque<>();
                        // The tokens in the stack are popped and put into a sublist in reversed
                        // order one by one, until a "[" is on the top of the list.
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
                    // If all of the "["s in the stack are paired, the whole list token is finished,
                    // and will be put to the token list.
                    if (bracketCount == 0) {
                        tokenList.add(tokenStack.pop());
                        i = j;
                        break;
                    }
                    // If it reaches the end of the command line, and there are still "["s not paired,
                    // this command is incomplete, and a next line should be received from the input.
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

    /**
     * It generates a token based on an input value.
     *
     * @param value the input value
     * @return the token
     * @throws LexicalErrorException
     */
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

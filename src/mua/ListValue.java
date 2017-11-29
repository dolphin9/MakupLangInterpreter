package mua;

import java.io.IOException;
import java.util.*;

public class ListValue extends Value {
    protected List<Value> mList;

    public ListValue(List<Value> list) {
        mList = list;
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

    protected Value get(int index) {
        return mList.get(index);
    }

    protected int size() {
        return mList.size();
    }

    public static abstract class Builder {
        static ListValue fromInput(final Scanner scanner) throws IOException, LexicalErrorException {
            return new Builder() {
                private String[] mItems;
                private int mOffset = 0;

                {
                    if (!scanner.hasNextLine())
                        throw new IOException();
                    mItems = scanner.nextLine().split(" ");
                }

                @Override
                String nextItem() {
                    return mItems[mOffset++];
                }
            }.buildList();
        }

        static ListValue fromCode(final Scanner codeScanner, final String startItem)
                throws IOException, LexicalErrorException {
            return new Builder() {
                private boolean mStarted = false;

                @Override
                String nextItem() throws IOException {
                    if (mStarted) {
                        if (!codeScanner.hasNext())
                            throw new IOException();
                        return codeScanner.next();
                    } else {
                        mStarted = true;
                        return startItem;
                    }
                }
            }.buildList();
        }

        abstract String nextItem() throws IOException;

        public ListValue buildList() throws IOException, LexicalErrorException {
            // Use a stack to process the list values.
            Stack<Value> valueStack = new Stack<>();
            // Counter for the bracket, which is increased or decreased
            // when there is a "[" or "]".
            int bracketCount = 0;
            do {
                String item = nextItem();

                int startLoc = 0;
                int endLoc = item.length();

                // If there are "["s, just push it into the stack and modify the
                // bracket counter and the start location.
                while (startLoc < endLoc && item.charAt(startLoc) == '[') {
                    // Push the "[" into the token stack.
                    valueStack.push(new ListStartSymbol());
                    ++bracketCount;
                    ++startLoc;
                }
                // If there are "]"s, just modify the bracket counter and the end location.
                while (startLoc < endLoc && item.charAt(endLoc - 1) == ']') {
                    --endLoc;
                    --bracketCount;
                }
                // Get the substring without brackets, which is list item to be added.
                String subStr = item.substring(startLoc, endLoc);
                // The substring is either a number token or a string token, if not empty.
                if (NumberValue.isNumber(subStr)) {
                    valueStack.push(NumberValue.parse(subStr));
                } else if (!subStr.isEmpty()) {
                    valueStack.push(new WordValue(subStr));
                }
                // If there are "]"s, meaning that the sublist is finished, which needs
                // to be popped from the token stack, and pushed back to the stack, or to
                // the token list, if the whole list token is finished.
                while (endLoc < item.length()) {
                    // If there is no "[", the bracket was not paired properly.
                    if (valueStack.empty())
                        throw new BracketNotPairedException();
                    ArrayDeque<Value> subList = new ArrayDeque<>();
                    // The tokens in the stack are popped and put into a sublist in reversed
                    // order one by one, until a "[" is on the top of the list.
                    while (!valueStack.empty()) {
                        Value popped = valueStack.pop();
                        // Token token = stringStack.pop();
                        if (popped instanceof ListStartSymbol) {
                            valueStack.add(new ListValue(new ArrayList<>(subList)));
                            break;
                        } else {
                            subList.addFirst(popped);
                        }
                    }
                    ++endLoc;
                }
                // If all of the "["s in the stack are paired, the whole list token is finished,
                // and will be put to the token list.
            } while (bracketCount != 0);
            return (ListValue) valueStack.pop();
        /*
        if (bracketCount == 0) {
            tokenList.add(valueStack.pop());
            i = j;
            break;
        }
        // If it reaches the end of the command line, and there are still "["s not paired,
        // this command is incomplete, and a next line should be received from the input.
        while (j == nItems - 1 && !valueStack.empty()) {
            String nextPart = mContext.lexerWait();
            nextPart = removeComments(nextPart);
            line += " " + nextPart;
            items = line.split(" ");
            nItems = items.length;
        }
        */
        }

        private class ListStartSymbol extends WordValue {
            ListStartSymbol() {
                super("[");
            }
        }
    }
}

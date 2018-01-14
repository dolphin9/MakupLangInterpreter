package mua.values;

import mua.Expression;
import mua.exceptions.MuaExceptions;
import mua.interfaces.Fragment;

import java.util.*;

public class ListValue extends Value {
    protected List<Value> mList;

    public ListValue(List<Value> list) {
        mList = list;
    }

    static public ListValue concat(Value v1, Value v2) {
        List<Value> list = new ArrayList<>();
        if (v1 instanceof ListValue)
            list.addAll(((ListValue) v1).mList);
        else
            list.add(v1);

        if (v2 instanceof ListValue)
            list.addAll(((ListValue) v2).mList);
        else
            list.add(v2);
        return new ListValue(list);
    }

    static public ListValue join(Value v1, Value v2) {
        List<Value> list = new ArrayList<>();
        list.add(v1);
        list.add(v2);
        return new ListValue(list);
    }

    public ListValue append(Value value) {
        List<Value> list = new ArrayList<>(mList);
        list.add(value);
        return new ListValue(list);
    }

    public Value first() {
        return mList.get(0);
    }

    public Value last() {
        return mList.get(mList.size() - 1);
    }

    public ListValue removeFirst() {
        return new ListValue(mList.subList(1, mList.size()));
    }

    public ListValue removeLast() {
        return new ListValue(mList.subList(0, mList.size() - 1));
    }

    public boolean isEmpty() {
        return mList.isEmpty();
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

    public static abstract class Builder implements Fragment {
        public static ListValue fromInput(final Scanner scanner) throws MuaExceptions {
            return new Builder() {
                private String[] mItems;
                private int mOffset = 0;

                {
                    scanner.nextLine();
                    if (!scanner.hasNextLine())
                        throw new MuaExceptions.InvalidInputException();
                    mItems = ("[" + scanner.nextLine() + "]").split(" ");
                }

                @Override
                public boolean hasNextInstruction() {
                    return scanner.hasNext();
                }

                @Override
                String nextInstruction() {
                    return mItems[mOffset++];
                }
            }.buildList();
        }

        public static ListValue fromCode(final String startItem, final Fragment fragment) throws MuaExceptions {
            return new Builder() {
                private boolean mStarted = false;

                @Override
                public boolean hasNextInstruction() {
                    return fragment.hasNextInstruction();
                }

                @Override
                String nextInstruction() throws MuaExceptions {
                    if (mStarted) {
                        return fragment.nextRawInstruction().toString();
                    } else {
                        mStarted = true;
                        return startItem;
                    }
                }
            }.buildList();
        }

        public static ListValue fromString(final String string) throws MuaExceptions {
            return new Builder() {
                private String[] mItems = string.split(" ");
                private int mIndex = 0;

                @Override
                public boolean hasNextInstruction() {
                    return mIndex < mItems.length;
                }

                @Override
                String nextInstruction() throws MuaExceptions {
                    return mItems[mIndex++];
                }
            }.buildList();
        }

        abstract String nextInstruction() throws MuaExceptions;

        @Override
        public WordValue nextRawInstruction() throws MuaExceptions {
            return new WordValue(nextInstruction());
        }

        public ListValue buildList() throws MuaExceptions {
            // Use a stack to process the list values.
            Stack<Value> valueStack = new Stack<>();
            // Counter for the bracket, which is increased or decreased
            // when there is a "[" or "]".
            int bracketCount = 0;
            do {
                if (!hasNextInstruction())
                    throw new MuaExceptions.BracketNotPairedException();
                String item = nextInstruction();

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
                } else if (Expression.isExpression(subStr)) {
                    valueStack.push(Expression.build(subStr, this));
                } else if (!subStr.isEmpty()) {
                    valueStack.push(new WordValue(subStr));
                }
                // If there are "]"s, meaning that the sublist is finished, which needs
                // to be popped from the token stack, and pushed back to the stack, or to
                // the token list, if the whole list token is finished.
                while (endLoc < item.length()) {
                    // If there is no "[", the bracket was not paired properly.
                    if (valueStack.empty())
                        throw new MuaExceptions.BracketNotPairedException();
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
        }

        private class ListStartSymbol extends WordValue {
            ListStartSymbol() {
                super("[");
            }
        }
    }
}

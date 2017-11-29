package mua;

import mua.parser.SyntaxErrorException;

import java.util.ArrayList;
import java.util.List;

public class Function extends ListValue implements Executable {
    private List<WordValue> mFormalArgList;
    private List<Value> mInstructionList;
    private List<Value> mActualArgList;

    public Function(List<Value> list) {
        super(list);
        mFormalArgList = new ArrayList<>();
        for (Value value : ((ListValue) list.get(0)).mList)
            mFormalArgList.add((WordValue) value);
        mInstructionList = ((ListValue) list.get(1)).mList;
    }

    public static boolean isFunction(ListValue list) {
        if (list.size() != 2)
            return false;
        if (!(list.get(0) instanceof ListValue || list.get(1) instanceof ListValue))
            return false;
        for (Value value : ((ListValue) list.get(0)).mList)
            if (!(value instanceof WordValue))
                return false;
        return true;
    }

    @Override
    public Value execute(Context context) throws SyntaxErrorException, LexicalErrorException {
        return null;
    }

    @Override
    public boolean needsMoreArguments() {
        return false;
    }

    @Override
    public void addArgument(Value argument) {

    }
}

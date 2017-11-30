package mua.values;

import mua.SymbolTable;
import mua.exceptions.MuaExceptions;
import mua.interfaces.Context;
import mua.interfaces.Executable;
import mua.interfaces.FunctionContext;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Function extends ListValue implements Executable {
    static private final SymbolTable FUNCTION_OPS = new SymbolTable();

    static {
        FUNCTION_OPS.put("output", new Operator.Output());
        FUNCTION_OPS.put("stop", new Operator.Stop());
    }

    private final List<WordValue> mFormalArgList =
            ((ListValue) mList.get(0)).mList.stream()
                    .map(value -> (WordValue) value)
                    .collect(Collectors.toList());
    private final List<Value> mInstructionList =
            ((ListValue) mList.get(1)).mList;
    private final String mName;
    private List<Value> mActualArgList = new ArrayList<>();
    private LocalContext mLocalContext;

    public Function(String name, List<Value> list) {
        super(list);
        mName = name;
    }

    private Function(Function function) {
        super(function.mList);
        mName = function.getName();
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

    public String getName() {
        return mName;
    }

    @Override
    public String toString() {
        return mName;
    }

    @Override
    public Value execute(Context context) throws MuaExceptions {
        Context globalContext = context;
        if (context instanceof FunctionContext)
            globalContext = ((FunctionContext) context).getGlobalContext();
        mLocalContext = new LocalContext(globalContext);

        for (int i = 0; i < mFormalArgList.size(); ++i)
            mLocalContext.addSymbol(mFormalArgList.get(i).toString(), mActualArgList.get(i));
        mLocalContext.merge(FUNCTION_OPS);


        mLocalContext.merge(globalContext.getSymbolTable());
        try {
            mLocalContext.run();
        } catch (FunctionStop functionStop) {
            // Ignore
        }
        return mLocalContext.getOutputValue();
    }

    @Override
    public boolean needsMoreArguments() {
        return mActualArgList.size() < mFormalArgList.size();
    }

    @Override
    public void addArgument(Value argument) {
        int index = mActualArgList.size();
        mActualArgList.add(argument);
    }

    @Override
    public Function clone() {
        return new Function(this);
    }

    static public class FunctionStop extends Throwable {
    }

    private class LocalContext implements FunctionContext {
        private Context mGlobalContext;
        private int mInstructionPointer = 0;
        private SymbolTable mLocalTable = new SymbolTable();
        private Value mOutputValue = null;

        LocalContext(Context globalContext) {
            mGlobalContext = globalContext;
        }

        @Override
        public void addSymbol(String symbol, Value value) {
            mLocalTable.put(symbol, value);
        }

        @Override
        public Value getSymbol(String symbol) {
            return mLocalTable.get(symbol);
        }

        @Override
        public boolean isSymbol(String string) {
            return mLocalTable.hasSymbol(string);
        }

        @Override
        public void removeSymbol(String symbol) {
            mLocalTable.remove(symbol);
        }

        public void merge(SymbolTable other) {
            mLocalTable.merge(other);
        }

        @Override
        public void print(Value value) {
            mGlobalContext.print(value);
        }

        @Override
        public SymbolTable getSymbolTable() {
            return mLocalTable;
        }

        @Override
        public boolean isExecutable(String instruction) {
            return isSymbol(instruction) && getSymbol(instruction) instanceof Executable;
        }

        @Override
        public Executable getExecutable(String instruction) {
            return ((Executable) mLocalTable.get(instruction)).clone();
        }

        @Override
        public Value read() throws MuaExceptions {
            return mGlobalContext.read();
        }

        @Override
        public Value readList() throws MuaExceptions {
            return mGlobalContext.readList();
        }

        @Override
        public boolean hasNextInstruction() {
            return mInstructionPointer < mInstructionList.size();
        }

        @Override
        public Value nextRawInstruction() {
            return mInstructionList.get(mInstructionPointer++);
        }

        @Override
        public void stop() throws FunctionStop {
            throw new FunctionStop();
        }

        @Override
        public Value getOutputValue() {
            return mOutputValue;
        }

        @Override
        public void setOutputValue(Value outputValue) {
            mOutputValue = outputValue;
        }

        @Override
        public Context getGlobalContext() {
            return mGlobalContext;
        }

        @Override
        public void run() throws MuaExceptions, FunctionStop {
            FunctionContext.super.run();
        }
    }
}

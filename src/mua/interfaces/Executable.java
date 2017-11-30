package mua.interfaces;

import mua.exceptions.MuaExceptions;
import mua.values.Function;
import mua.values.Value;

public interface Executable {
    Value execute(Context context) throws MuaExceptions, Function.FunctionStop;

    boolean needsMoreArguments();

    void addArgument(Value argument) throws MuaExceptions.InvalidArgumentTypeException;

    Executable clone();
}

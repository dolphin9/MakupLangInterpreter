package mua.interfaces;

import mua.exceptions.MuaExceptions;
import mua.values.Function;
import mua.values.Value;

public interface FunctionContext extends Context {
    void stop() throws Function.FunctionStop;

    Value getOutputValue();

    void setOutputValue(Value outputValue);
    Context getGlobalContext();


    @Override
    default void run() throws MuaExceptions, Function.FunctionStop {
        Context.super.run();
    }
}

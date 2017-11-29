package mua;

public interface FunctionContext extends Context {
    void stop();

    Value getOutputValue();

    void setOutputValue(Value outputValue);
}

package mua;

public abstract class Value {
    // abstract public Object getValue();
    @Override
    abstract public String toString();
}

class NumberValue extends Value {
    private Number mValue;

    NumberValue(Number number) {
        mValue = number;
    }

    public static boolean isNumber(String item) {
        return item.matches("[+-]?\\d+(\\.\\d+)?");
    }

    public static NumberValue parse(String numItem) {
        if (numItem.contains("."))
            return new NumberValue(Double.valueOf(numItem));
        else
            return new NumberValue(Integer.valueOf(numItem));
    }

    public static NumberValue add(NumberValue lhs, NumberValue rhs) {
        if (lhs.mValue instanceof Integer && rhs.mValue instanceof Integer)
            return new NumberValue(lhs.intValue() + rhs.intValue());
        else
            return new NumberValue(lhs.doubleValue() + rhs.doubleValue());
    }

    public static NumberValue sub(NumberValue lhs, NumberValue rhs) {
        if (lhs.mValue instanceof Integer && rhs.mValue instanceof Integer)
            return new NumberValue(lhs.intValue() - rhs.intValue());
        else
            return new NumberValue(lhs.doubleValue() - rhs.doubleValue());
    }

    public static NumberValue mul(NumberValue lhs, NumberValue rhs) {
        if (lhs.mValue instanceof Integer && rhs.mValue instanceof Integer)
            return new NumberValue(lhs.intValue() * rhs.intValue());
        else
            return new NumberValue(lhs.doubleValue() * rhs.doubleValue());
    }

    public static NumberValue div(NumberValue lhs, NumberValue rhs) {
        if (lhs.mValue instanceof Integer && rhs.mValue instanceof Integer)
            return new NumberValue(lhs.intValue() / rhs.intValue());
        else
            return new NumberValue(lhs.doubleValue() / rhs.doubleValue());
    }

    public static NumberValue mod(NumberValue lhs, NumberValue rhs) {
        if (lhs.mValue instanceof Integer && rhs.mValue instanceof Integer)
            return new NumberValue(lhs.intValue() % rhs.intValue());
        else
            return new NumberValue(lhs.doubleValue() % rhs.doubleValue());
    }

    public static BoolValue eq(NumberValue lhs, NumberValue rhs) {
        if (lhs.mValue instanceof Integer && rhs.mValue instanceof Integer)
            return new BoolValue(lhs.intValue() == rhs.intValue());
        else
            return new BoolValue(lhs.doubleValue() == rhs.doubleValue());
    }

    public static BoolValue gt(NumberValue lhs, NumberValue rhs) {
        if (lhs.mValue instanceof Integer && rhs.mValue instanceof Integer)
            return new BoolValue(lhs.intValue() > rhs.intValue());
        else
            return new BoolValue(lhs.doubleValue() > rhs.doubleValue());
    }

    public static BoolValue lt(NumberValue lhs, NumberValue rhs) {
        if (lhs.mValue instanceof Integer && rhs.mValue instanceof Integer)
            return new BoolValue(lhs.intValue() < rhs.intValue());
        else
            return new BoolValue(lhs.doubleValue() < rhs.doubleValue());
    }

    private int intValue() {
        return mValue.intValue();
    }

    private double doubleValue() {
        return mValue.doubleValue();
    }

    @Override
    public String toString() {
        return mValue.toString();
    }
}

class BoolValue extends Value {
    private boolean mValue;

    BoolValue(boolean bool) {
        mValue = bool;
    }

    public static BoolValue and(BoolValue lhs, BoolValue rhs) {
        return new BoolValue(lhs.mValue && rhs.mValue);
    }

    public static BoolValue or(BoolValue lhs, BoolValue rhs) {
        return new BoolValue(lhs.mValue || rhs.mValue);
    }

    public static BoolValue not(BoolValue lhs) {
        return new BoolValue(!lhs.mValue);
    }

    @Override
    public String toString() {
        return Boolean.toString(mValue);
    }
}

class WordValue extends Value {
    String mValue;

    WordValue(String value) {
        mValue = value;
    }

    @Override
    public String toString() {
        return mValue;
    }
}


package mua.values;

public class NumberValue extends Value {
    private Number mValue;

    public NumberValue(Number number) {
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

    public NumberValue sqrt() {
        return new NumberValue(Math.sqrt(mValue.doubleValue()));
    }

    public int intValue() {
        return mValue.intValue();
    }

    public double doubleValue() {
        return mValue.doubleValue();
    }

    @Override
    public String toString() {
        return mValue.toString();
    }
}

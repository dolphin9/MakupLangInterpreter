package mua.values;

public class BoolValue extends Value {
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

    public static boolean isBool(String item) {
        return item.equals("false") || item.equals("true");
    }

    public static BoolValue parse(String item) {
        return new BoolValue(Boolean.valueOf(item));
    }

    boolean boolValue() {
        return mValue;
    }

    @Override
    public String toString() {
        return Boolean.toString(mValue);
    }
}

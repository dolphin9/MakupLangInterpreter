package mua.values;

public class WordValue extends Value {
    private String mValue;

    public WordValue(String value) {
        mValue = value;
    }

    @Override
    public String toString() {
        return mValue;
    }
}

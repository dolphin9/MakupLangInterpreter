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

    public boolean isEmpty() {
        return mValue.isEmpty();
    }

    public WordValue concat(Value value) {
        return new WordValue(mValue.concat(value.toString()));
    }

    public WordValue first() {
        return new WordValue(mValue.substring(0, 1));
    }

    public WordValue last() {
        return new WordValue(mValue.substring(mValue.length() - 1));
    }

    public WordValue removeFirst() {
        return new WordValue(mValue.substring(1));
    }

    public WordValue removeLast() {
        return new WordValue(mValue.substring(0, mValue.length() - 1));
    }
}

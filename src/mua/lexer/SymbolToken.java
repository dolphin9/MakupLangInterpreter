package mua.lexer;

public class SymbolToken extends Token {
    public enum SymbolType {
        kListBegin,
        kListEnd,
    }

    private SymbolType mType;

    public SymbolType getSymbolType() {
        return mType;
    }

    protected SymbolToken(SymbolType type) {
        super(TokenType.kSymbol);
        mType = type;
    }

    @Override
    public String toString() {
        return "<" + mType.toString() + ">";
    }

    @Override
    public Object getValue() {
        return null;
    }
}

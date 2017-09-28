package mua.lexer;

public class OperatorToken extends Token {
    public enum OpType {
        kMake,
        kThing,
        kErase,
        kIsName,
        kPrint,
        kRead,
        kReadLinst,
        kAdd,
        kSub,
        kMul,
        kDiv,
        kMod,
        kEq,
        kGt,
        kLt,
        kAnd,
        kOr,
        kNot,
        // Special operators
        kListBegin,
        kListEnd,
        // ...
    }
    private OpType mOpType;

    protected OperatorToken(OpType opType) {
        super(Type.kOperator);
        mOpType = opType;
    }

    @Override
    public String toString() {
        return "<" + mOpType.toString() + ">";
    }

    public OpType getOpType() {
        return mOpType;
    }

    @Override
    public Object getValue() {
        return null;
    }
}

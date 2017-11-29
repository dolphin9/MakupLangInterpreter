package mua;

import mua.parser.InvalidArgumentTypeException;
import mua.parser.SyntaxErrorException;
import mua.parser.UnknownOperatorException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class Operator extends Value implements Executable {
    protected Class[] mArgTypes;
    protected List<Value> mArguments = new ArrayList<>();
    // protected int mNumArgs;

    static public Operator extract(String opStr) throws SyntaxErrorException {
        Operator op = Operator.tryExtract(opStr);
        if (op == null)
            throw new UnknownOperatorException();
        else
            return op;
    }

    static public Operator tryExtract(String opStr) {
        switch (opStr) {
            case "make":
                return new Make();
            case "thing":
                return new Thing();
            case "erase":
                return new Erase();
            case "isname":
                return new IsName();
            case "print":
                return new Print();
            case "read":
                return new Read();
            case "readlinst":
                return new ReadList();
            case "add":
                return new Add();
            case "sub":
                return new Sub();
            case "mul":
                return new Mul();
            case "div":
                return new Div();
            case "mod":
                return new Mod();
            case "eq":
                return new Eq();
            case "gt":
                return new Gt();
            case "lt":
                return new Lt();
            case "and":
                return new And();
            case "or":
                return new Or();
            case "not":
                return new Not();
            default:
                return null;
        }
    }

    @Override
    public Operator clone() {
        Operator op = null;
        try {
            op = getClass().newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return op;
    }

    /*
    public Value tmp(Context context) throws SyntaxErrorException, LexicalErrorException {
        // Execute the operator nodes in the parse tree in post-order recursively.
        for (int i = 0; i < mArguments.size(); ++i) {
            if (mArguments.get(i).isOperator()) {
                Operator op = (Operator) mArguments.get(i);
                Value value = op.execute(context);
                if (value == null)
                    throw new InvalidArgumentTypeException();
                mArguments.set(i, value);
            }
        }
        Number num1 = null, num2 = null, res = null;
        BiFunction<Integer, Integer, Object> intOp = null;
        BiFunction<Double, Double, Object> doubleOp = null;
        BiFunction<Boolean, Boolean, Boolean> boolOp = null;
        boolean bool1 = false, bool2 = false, bool = false;
        // Execute itself depending on its type of operator.
        switch (mOpType) {
            case kMake:
                context.addSymbol(getWordArgAt(0), getValueArgAt(1));
                return null;
            case kThing:
                return context.parse(context.getSymbol(getWordArgAt(0)));
            case kErase:
                context.removeSymbol(getWordArgAt(0));
                return null;
            case kIsName:
                return new BoolValue(context.isSymbol(getWordArgAt(0)));
            case kPrint:
                context.print(getValueArgAt(0));
                return null;
            case kRead:
                return context.read();
            case kReadLinst:
                return context.readList();
            case kAdd:
                if (intOp == null) intOp = (a, b) -> a + b;
                if (doubleOp == null) doubleOp = (a, b) -> a + b;
                // fallthrough
            case kSub:
                if (intOp == null) intOp = (a, b) -> a - b;
                if (doubleOp == null) doubleOp = (a, b) -> a - b;
                // fallthrough
            case kMul:
                if (intOp == null) intOp = (a, b) -> a * b;
                if (doubleOp == null) doubleOp = (a, b) -> a * b;
                // fallthrough
            case kDiv:
                if (intOp == null) intOp = (a, b) -> a / b;
                if (doubleOp == null) doubleOp = (a, b) -> a / b;
                // fallthrough
            case kMod:
                if (intOp == null) intOp = (a, b) -> a % b;
                if (doubleOp == null) doubleOp = (a, b) -> a % b;
                // fallthrough

                num1 = getNumArgAt(0);
                num2 = getNumArgAt(1);
                if (num1 instanceof Integer && num2 instanceof Integer)
                    res = (Number) intOp.apply(num1.intValue(), num2.intValue());
                else
                    res = (Number) doubleOp.apply(num1.doubleValue(), num2.doubleValue());
                return new NumberValue(res);

            case kEq:
                if (intOp == null) intOp = Integer::equals;
                if (doubleOp == null) doubleOp = Double::equals;
                // fallthrough
            case kGt:
                if (intOp == null) intOp = (a, b) -> a > b;
                if (doubleOp == null) doubleOp = (a, b) -> a > b;
                // fallthrough
            case kLt:
                if (intOp == null) intOp = (a, b) -> a < b;
                if (doubleOp == null) doubleOp = (a, b) -> a < b;
                // fallthrough

                num1 = getNumOrNumOfWordArgAt(0, context);
                num2 = getNumOrNumOfWordArgAt(1, context);
                if (num1 instanceof Integer && num2 instanceof Integer)
                    bool = (Boolean) intOp.apply(num1.intValue(), num2.intValue());
                else
                    bool = (Boolean) doubleOp.apply(num1.doubleValue(), num2.doubleValue());
                return new BoolValue(bool);

            case kAnd:
                if (boolOp == null) boolOp = (a, b) -> a && b;
                // fallthrough
            case kOr:
                if (boolOp == null) boolOp = (a, b) -> a || b;
                // fallthrough
                bool2 = getBoolArgAt(1);

            case kNot:
                if (boolOp == null) boolOp = (a, b) -> !a;
                // fallthrough
                bool1 = getBoolArgAt(0);
                return new BoolValue(boolOp.apply(bool1, bool2));

            default:
                return null;
        }
    }
    */

    /*
    @Override
    public Object getValue() {
        return null;
    }

    @Override
    public boolean isOperator() {
        return true;
    }
    */

    NumberValue getNumArgAt(int index) throws SyntaxErrorException {
        if (mArguments.get(index) instanceof NumberValue)
            return ((NumberValue) mArguments.get(index));
        else
            throw new InvalidArgumentTypeException();
    }

    NumberValue getNumOrNumOfWordArgAt(int index, Context context)
            throws SyntaxErrorException {
        Value value = mArguments.get(index);
        if (value instanceof WordValue)
            return ((NumberValue) context.getSymbol(getWordArgAt(index).toString()));
        else if (value instanceof NumberValue)
            return getNumArgAt(index);
        else
            throw new InvalidArgumentTypeException();
    }

    WordValue getWordArgAt(int index) throws SyntaxErrorException {
        Value value = mArguments.get(index);
        if (value instanceof WordValue)
            return ((WordValue) value);
        else
            throw new InvalidArgumentTypeException();
    }

    Value getValueArgAt(int index) {
        return mArguments.get(index);
    }

    BoolValue getBoolArgAt(int index) throws SyntaxErrorException {
        if (mArguments.get(index) instanceof BoolValue)
            return ((BoolValue) mArguments.get(index));
        else
            throw new InvalidArgumentTypeException();
    }

    @Override
    public boolean needsMoreArguments() {
        return mArguments.size() < mArgTypes.length;
    }

    @Override
    public void addArgument(Value argument) throws InvalidArgumentTypeException {
        int index = mArguments.size();
        // if (argument.getClass().isInstance(mArgTypes[index]))
        if (mArgTypes[index].isInstance(argument))
            mArguments.add(argument);
        else
            throw new InvalidArgumentTypeException();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName().toLowerCase();
    }

    /*
    private enum OpType {
        kMake(2),
        kThing(1),
        kErase(1),
        kIsName(1),
        kPrint(1),
        kRead(0),
        kReadLinst(0),
        kAdd(2),
        kSub(2),
        kMul(2),
        kDiv(2),
        kMod(2),
        kEq(2),
        kGt(2),
        kLt(2),
        kAnd(2),
        kOr(2),
        kNot(1);
        // ...

        private int mNumArgs;

        OpType(int numArgs) {
            mNumArgs = numArgs;
        }

        public int getNumArgs() {
            return mNumArgs;
        }
    }
    */

    static final class Make extends Operator {
        {
            mArgTypes = new Class[]{WordValue.class, Value.class};
        }

        @Override
        public Value execute(Context context) throws SyntaxErrorException {
            String word = getWordArgAt(0).toString();
            Value value = getValueArgAt(1);
            if (value instanceof ListValue) {
                if (Function.isFunction((ListValue) value)) {
                    value = new Function(word, ((ListValue) value).mList);
                }
            }
            context.addSymbol(word, value);
            return null;
        }
    }

    static final class Thing extends Operator {
        {
            mArgTypes = new Class[]{WordValue.class};
        }

        @Override
        public Value execute(Context context) throws SyntaxErrorException, LexicalErrorException {
            return context.getSymbol(getWordArgAt(0).toString());
        }
    }

    static final class Erase extends Operator {
        {
            mArgTypes = new Class[]{WordValue.class};
        }

        @Override
        public Value execute(Context context) throws SyntaxErrorException {
            context.removeSymbol(getWordArgAt(0).toString());
            return null;
        }
    }

    static final class IsName extends Operator {
        {
            mArgTypes = new Class[]{WordValue.class};
        }

        @Override
        public Value execute(Context context) throws SyntaxErrorException {
            return new BoolValue(context.isSymbol(getWordArgAt(0).toString()));
        }
    }

    static final class Print extends Operator {
        {
            mArgTypes = new Class[]{Value.class};
        }

        @Override
        public Value execute(Context context) {
            context.print(getValueArgAt(0));
            return null;
        }
    }

    static final class Read extends Operator {
        {
            mArgTypes = new Class[0];
        }

        @Override
        public Value execute(Context context) throws SyntaxErrorException, LexicalErrorException, IOException {
            return context.read();
        }
    }

    static final class ReadList extends Operator {
        {
            mArgTypes = new Class[0];
        }

        @Override
        public Value execute(Context context) throws SyntaxErrorException, LexicalErrorException, IOException {
            return context.readList();
        }
    }

    static final class Add extends Operator {
        {
            mArgTypes = new Class[]{NumberValue.class, NumberValue.class};
        }

        @Override
        public Value execute(Context context) throws SyntaxErrorException {
            return NumberValue.add(getNumArgAt(0), getNumArgAt(1));
        }
    }

    static final class Sub extends Operator {
        {
            mArgTypes = new Class[]{NumberValue.class, NumberValue.class};
        }

        @Override
        public Value execute(Context context) throws SyntaxErrorException {
            return NumberValue.sub(getNumArgAt(0), getNumArgAt(1));
        }
    }

    static final class Mul extends Operator {
        {
            mArgTypes = new Class[]{NumberValue.class, NumberValue.class};
        }

        @Override
        public Value execute(Context context) throws SyntaxErrorException {
            return NumberValue.mul(getNumArgAt(0), getNumArgAt(1));
        }
    }

    static final class Div extends Operator {
        {
            mArgTypes = new Class[]{NumberValue.class, NumberValue.class};
        }

        @Override
        public Value execute(Context context) throws SyntaxErrorException {
            return NumberValue.div(getNumArgAt(0), getNumArgAt(1));
        }
    }

    static final class Mod extends Operator {
        {
            mArgTypes = new Class[]{NumberValue.class, NumberValue.class};
        }

        @Override
        public Value execute(Context context) throws SyntaxErrorException {
            return NumberValue.mod(getNumArgAt(0), getNumArgAt(1));
        }
    }

    static final class Eq extends Operator {
        {
            mArgTypes = new Class[]{NumberValue.class, NumberValue.class};
        }

        @Override
        public Value execute(Context context) throws SyntaxErrorException {
            return NumberValue.eq(
                    getNumOrNumOfWordArgAt(0, context),
                    getNumOrNumOfWordArgAt(1, context));
        }
    }

    static final class Gt extends Operator {
        {
            mArgTypes = new Class[]{NumberValue.class, NumberValue.class};
        }

        @Override
        public Value execute(Context context) throws SyntaxErrorException {
            return NumberValue.gt(
                    getNumOrNumOfWordArgAt(0, context),
                    getNumOrNumOfWordArgAt(1, context));
        }
    }

    static final class Lt extends Operator {
        {
            mArgTypes = new Class[]{NumberValue.class, NumberValue.class};
        }

        @Override
        public Value execute(Context context) throws SyntaxErrorException {
            return NumberValue.lt(
                    getNumOrNumOfWordArgAt(0, context),
                    getNumOrNumOfWordArgAt(1, context));
        }
    }

    static final class And extends Operator {
        {
            mArgTypes = new Class[]{BoolValue.class, BoolValue.class};
        }

        @Override
        public Value execute(Context context) throws SyntaxErrorException {
            return BoolValue.and(getBoolArgAt(0), getBoolArgAt(1));
        }
    }

    static final class Or extends Operator {
        {
            mArgTypes = new Class[]{BoolValue.class, BoolValue.class};
        }

        @Override
        public Value execute(Context context) throws SyntaxErrorException {
            return BoolValue.or(getBoolArgAt(0), getBoolArgAt(1));
        }
    }

    static final class Not extends Operator {
        {
            mArgTypes = new Class[]{BoolValue.class};
        }

        @Override
        public Value execute(Context context) throws SyntaxErrorException {
            return BoolValue.not(getBoolArgAt(0));
        }
    }
}


package mua.parser;

import mua.Context;
import mua.lexer.LexicalErrorException;

import java.util.ArrayList;
import java.util.function.BiFunction;

public class OperatorNode extends ValueNode {
    private ArrayList<ParseNode> mArguments = new ArrayList<>();
    private OpType mOpType;

    private OperatorNode(OpType opType) {
        mOpType = opType;
    }

    static OperatorNode extract(String opStr) throws SyntaxErrorException {
        if (opStr.startsWith(":")) {
            OperatorNode op = new OperatorNode(OpType.kThing);
            op.addArgument(new WordNode(opStr.substring(1)));
            return op;
        } else {
            OperatorNode op = OperatorNode.tryExtract(opStr);
            if (op == null)
                throw new UnknownOperatorException();
            else
                return op;
        }
    }

    static private OperatorNode tryExtract(String opStr) {
        switch (opStr) {
            case "make":
                return new OperatorNode(OpType.kMake);
            case "thing":
                return new OperatorNode(OpType.kThing);
            case "erase":
                return new OperatorNode(OpType.kErase);
            case "isname":
                return new OperatorNode(OpType.kIsName);
            case "print":
                return new OperatorNode(OpType.kPrint);
            case "read":
                return new OperatorNode(OpType.kRead);
            case "readlinst":
                return new OperatorNode(OpType.kReadLinst);
            case "add":
                return new OperatorNode(OpType.kAdd);
            case "sub":
                return new OperatorNode(OpType.kSub);
            case "mul":
                return new OperatorNode(OpType.kMul);
            case "div":
                return new OperatorNode(OpType.kDiv);
            case "mod":
                return new OperatorNode(OpType.kMod);
            case "eq":
                return new OperatorNode(OpType.kEq);
            case "gt":
                return new OperatorNode(OpType.kGt);
            case "lt":
                return new OperatorNode(OpType.kLt);
            case "and":
                return new OperatorNode(OpType.kAnd);
            case "or":
                return new OperatorNode(OpType.kOr);
            case "not":
                return new OperatorNode(OpType.kNot);
            default:
                return null;
        }
    }

    public ValueNode execute(Context context) throws SyntaxErrorException, LexicalErrorException {
        for (int i = 0; i < mArguments.size(); ++i) {
            if (mArguments.get(i).isOperator()) {
                OperatorNode op = (OperatorNode) mArguments.get(i);
                ValueNode value = op.execute(context);
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
                return new BoolNode(context.isSymbol(getWordArgAt(0)));
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
                return new NumberNode(res);

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
                return new BoolNode(bool);

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
                return new BoolNode(boolOp.apply(bool1, bool2));

            default:
                return null;
        }
    }

    @Override
    public Object getValue() {
        return null;
    }

    @Override
    public boolean isOperator() {
        return true;
    }

    private Number getNumArgAt(int index) throws SyntaxErrorException {
        if (mArguments.get(index).isNumber())
            return ((NumberNode) mArguments.get(index)).getValue();
        else
            throw new InvalidArgumentTypeException();
    }

    private Number getNumOrNumOfWordArgAt(int index, Context context)
            throws SyntaxErrorException {
        ParseNode node = mArguments.get(index);
        if (node.isWord())
            return (Number) context.getSymbol(getWordArgAt(index));
        else if (node.isNumber())
            return getNumArgAt(index);
        else
            throw new InvalidArgumentTypeException();
    }

    private String getWordArgAt(int index) throws SyntaxErrorException {
        ParseNode node = mArguments.get(index);
        if (node.isWord())
            return ((WordNode) node).getValue();
        else if (node.isString()) {
            String str = ((StringNode) node).getValue();
            if (str.startsWith("\"")) {
                WordNode word = new WordNode(str.substring(1));
                mArguments.set(index, word);
                return word.getValue();
            }
            else
                throw new InvalidArgumentTypeException();
        }
        else
            throw new InvalidArgumentTypeException();
    }

    private Object getValueArgAt(int index) {
        return ((ValueNode) mArguments.get(index)).getValue();
    }

    private boolean getBoolArgAt(int index) throws SyntaxErrorException {
        if (mArguments.get(index).isBool())
            return ((BoolNode) mArguments.get(index)).getValue();
        else
            throw new InvalidArgumentTypeException();
    }

    boolean needsMoreArguments() {
        return mArguments.size() < mOpType.getNumArgs();
    }

    void addArgument(ParseNode argument) {
        mArguments.add(argument);
    }

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
}


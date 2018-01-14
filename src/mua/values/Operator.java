package mua.values;

import mua.exceptions.MuaExceptions;
import mua.interfaces.Context;
import mua.interfaces.Executable;
import mua.interfaces.FunctionContext;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class Operator extends Value implements Executable {
    public static final HashMap<String, Operator> DEFINED_OPS = new HashMap<>();

    static {
        DEFINED_OPS.put("make", new Operator.Make());
        DEFINED_OPS.put("thing", new Operator.Thing());
        DEFINED_OPS.put("erase", new Operator.Erase());
        DEFINED_OPS.put("isname", new Operator.IsName());
        DEFINED_OPS.put("print", new Operator.Print());
        DEFINED_OPS.put("read", new Operator.Read());
        DEFINED_OPS.put("readlinst", new Operator.ReadList());
        DEFINED_OPS.put("add", new Operator.Add());
        DEFINED_OPS.put("sub", new Operator.Sub());
        DEFINED_OPS.put("mul", new Operator.Mul());
        DEFINED_OPS.put("div", new Operator.Div());
        DEFINED_OPS.put("mod", new Operator.Mod());
        DEFINED_OPS.put("eq", new Operator.Eq());
        DEFINED_OPS.put("gt", new Operator.Gt());
        DEFINED_OPS.put("lt", new Operator.Lt());
        DEFINED_OPS.put("and", new Operator.And());
        DEFINED_OPS.put("or", new Operator.Or());
        DEFINED_OPS.put("not", new Operator.Not());
        DEFINED_OPS.put("repeat", new Operator.Repeat());
        DEFINED_OPS.put("isnumber", new Operator.IsNumber());
        DEFINED_OPS.put("isword", new Operator.IsWord());
        DEFINED_OPS.put("islist", new Operator.IsList());
        DEFINED_OPS.put("isbool", new Operator.IsBool());
        DEFINED_OPS.put("isempty", new Operator.IsEmpty());
        DEFINED_OPS.put("random", new Operator.Random());
        DEFINED_OPS.put("sqrt", new Operator.Sqrt());
        DEFINED_OPS.put("int", new Operator.Int());
        DEFINED_OPS.put("word", new Operator.Word());
        DEFINED_OPS.put("if", new Operator.If());
        DEFINED_OPS.put("sentence", new Operator.Sentence());
        DEFINED_OPS.put("list", new Operator.List());
        DEFINED_OPS.put("join", new Operator.Join());
        DEFINED_OPS.put("first", new Operator.First());
        DEFINED_OPS.put("last", new Operator.Last());
        DEFINED_OPS.put("butfirst", new Operator.ButFirst());
        DEFINED_OPS.put("butlast", new Operator.ButLast());
        DEFINED_OPS.put("wait", new Operator.Wait());
        DEFINED_OPS.put("save", new Operator.Save());
        DEFINED_OPS.put("load", new Operator.Load());
        DEFINED_OPS.put("erall", new Operator.EraseAll());
        DEFINED_OPS.put("poall", new Operator.PostAll());
    }
    protected Class[] mArgTypes;
    protected java.util.List<Value> mArguments = new ArrayList<>();
    // protected int mNumArgs;

    @Override
    public Operator clone() {
        Operator op = null;
        try {
            op = getClass().newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            try {
                throw new MuaExceptions.InitFunctionFailingException(op);
            } catch (MuaExceptions.InitFunctionFailingException e1) {
                e1.printStackTrace();
            }
        }
        return op;
    }

    NumberValue getNumArgAt(int index) throws MuaExceptions.InvalidArgumentTypeException {
        if (mArguments.get(index) instanceof NumberValue)
            return ((NumberValue) mArguments.get(index));
        else
            throw new MuaExceptions.InvalidArgumentTypeException(
                    mArguments.get(index).getClass(), NumberValue.class);
    }

    NumberValue getNumOrNumOfWordArgAt(int index, Context context)
            throws MuaExceptions.InvalidArgumentTypeException {
        Value value = mArguments.get(index);
        if (value instanceof WordValue)
            return ((NumberValue) context.getSymbol(getWordArgAt(index).toString()));
        else if (value instanceof NumberValue)
            return getNumArgAt(index);
        else
            throw new MuaExceptions.InvalidArgumentTypeException(
                    mArguments.get(index).getClass(), NumberValue.class);
    }

    WordValue getWordArgAt(int index) throws MuaExceptions.InvalidArgumentTypeException {
        Value value = mArguments.get(index);
        if (value instanceof WordValue)
            return ((WordValue) value);
        else
            throw new MuaExceptions.InvalidArgumentTypeException(
                    mArguments.get(index).getClass(), WordValue.class);
    }

    Value getValueArgAt(int index) {
        return mArguments.get(index);
    }

    BoolValue getBoolArgAt(int index)
            throws MuaExceptions.InvalidArgumentTypeException {
        if (mArguments.get(index) instanceof BoolValue)
            return ((BoolValue) mArguments.get(index));
        else
            throw new MuaExceptions.InvalidArgumentTypeException(
                    mArguments.get(index).getClass(), BoolValue.class);
    }

    ListValue getListValueAt(int index) throws MuaExceptions.InvalidArgumentTypeException {
        if (mArguments.get(index) instanceof ListValue)
            return (ListValue) mArguments.get(index);
        else
            throw new MuaExceptions.InvalidArgumentTypeException(
                    mArguments.get(index).getClass(), BoolValue.class);
    }

    @Override
    public boolean needsMoreArguments() {
        return mArguments.size() < mArgTypes.length;
    }

    @Override
    public void addArgument(Value argument) throws MuaExceptions.InvalidArgumentTypeException {
        int index = mArguments.size();
        // if (argument.getClass().isInstance(mArgTypes[index]))
        if (mArgTypes[index].isInstance(argument))
            mArguments.add(argument);
        else
            throw new MuaExceptions.InvalidArgumentTypeException(
                    argument.getClass(), mArgTypes[index]);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName().toLowerCase();
    }

    static final class Make extends Operator {
        {
            mArgTypes = new Class[]{WordValue.class, Value.class};
        }

        @Override
        public Value execute(Context context) throws MuaExceptions {
            String word = getWordArgAt(0).toString();
            Value value = getValueArgAt(1);
            if (value instanceof ListValue) {
                if (Function.isFunction((ListValue) value)) {
                    value = new Function(word, (ListValue) value);
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
        public Value execute(Context context) throws MuaExceptions {
            return context.getSymbol(getWordArgAt(0).toString());
        }
    }

    static final class Erase extends Operator {
        {
            mArgTypes = new Class[]{WordValue.class};
        }

        @Override
        public Value execute(Context context) throws MuaExceptions {
            context.removeSymbol(getWordArgAt(0).toString());
            return null;
        }
    }

    static final class IsName extends Operator {
        {
            mArgTypes = new Class[]{WordValue.class};
        }

        @Override
        public Value execute(Context context) throws MuaExceptions {
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
        public Value execute(Context context) throws MuaExceptions {
            return context.read();
        }
    }

    static final class ReadList extends Operator {
        {
            mArgTypes = new Class[0];
        }

        @Override
        public Value execute(Context context) throws MuaExceptions {
            return context.readList();
        }
    }

    public static class Add extends Operator {
        {
            mArgTypes = new Class[]{NumberValue.class, NumberValue.class};
        }

        @Override
        public Value execute(Context context) throws MuaExceptions {
            return NumberValue.add(getNumArgAt(0), getNumArgAt(1));
        }
    }

    public static class Sub extends Operator {
        {
            mArgTypes = new Class[]{NumberValue.class, NumberValue.class};
        }

        @Override
        public Value execute(Context context) throws MuaExceptions {
            return NumberValue.sub(getNumArgAt(0), getNumArgAt(1));
        }
    }

    public static class Mul extends Operator {
        {
            mArgTypes = new Class[]{NumberValue.class, NumberValue.class};
        }

        @Override
        public Value execute(Context context) throws MuaExceptions {
            return NumberValue.mul(getNumArgAt(0), getNumArgAt(1));
        }
    }

    public static class Div extends Operator {
        {
            mArgTypes = new Class[]{NumberValue.class, NumberValue.class};
        }

        @Override
        public Value execute(Context context) throws MuaExceptions {
            return NumberValue.div(getNumArgAt(0), getNumArgAt(1));
        }
    }

    public static class Mod extends Operator {
        {
            mArgTypes = new Class[]{NumberValue.class, NumberValue.class};
        }

        @Override
        public Value execute(Context context) throws MuaExceptions {
            return NumberValue.mod(getNumArgAt(0), getNumArgAt(1));
        }
    }

    static final class Eq extends Operator {
        {
            mArgTypes = new Class[]{NumberValue.class, NumberValue.class};
        }

        @Override
        public Value execute(Context context) throws MuaExceptions {
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
        public Value execute(Context context) throws MuaExceptions {
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
        public Value execute(Context context) throws MuaExceptions {
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
        public Value execute(Context context) throws MuaExceptions {
            return BoolValue.and(getBoolArgAt(0), getBoolArgAt(1));
        }
    }

    static final class Or extends Operator {
        {
            mArgTypes = new Class[]{BoolValue.class, BoolValue.class};
        }

        @Override
        public Value execute(Context context) throws MuaExceptions {
            return BoolValue.or(getBoolArgAt(0), getBoolArgAt(1));
        }
    }

    static final class Not extends Operator {
        {
            mArgTypes = new Class[]{BoolValue.class};
        }

        @Override
        public Value execute(Context context) throws MuaExceptions {
            return BoolValue.not(getBoolArgAt(0));
        }
    }

    static final class Repeat extends Operator {
        {
            mArgTypes = new Class[]{NumberValue.class, ListValue.class};
        }

        @Override
        public Value execute(Context context) throws MuaExceptions, Function.FunctionStop {
            CodeFragment codeFragment = new CodeFragment(getListValueAt(1));
            for (int i = 0; i < getNumArgAt(0).intValue(); ++i) {
                codeFragment.resetPointer();
                context.run(codeFragment);
            }
            return null;
        }
    }

    static final class If extends Operator {
        {
            mArgTypes = new Class[]{BoolValue.class, ListValue.class, ListValue.class};
        }

        @Override
        public Value execute(Context context) throws MuaExceptions, Function.FunctionStop {
            CodeFragment codeFragment;
            if (getBoolArgAt(0).boolValue())
                codeFragment = new CodeFragment(getListValueAt(1));
            else
                codeFragment = new CodeFragment(getListValueAt(2));
            context.run(codeFragment);
            return null;
        }
    }

    static final class Output extends Operator {
        {
            mArgTypes = new Class[]{Value.class};
        }

        @Override
        public Value execute(Context context) throws MuaExceptions {
            ((FunctionContext) context).setOutputValue(getValueArgAt(0));
            return null;
        }
    }

    static final class Stop extends Operator {
        {
            mArgTypes = new Class[0];
        }

        @Override
        public Value execute(Context context) throws MuaExceptions, Function.FunctionStop {
            ((FunctionContext) context).stop();
            return null;
        }
    }

    static final class Export extends Operator {
        {
            mArgTypes = new Class[0];
        }

        @Override
        public Value execute(Context context) throws MuaExceptions, Function.FunctionStop {
            ((FunctionContext) context).export();
            return null;
        }

    }

    static final class IsNumber extends Operator {
        {
            mArgTypes = new Class[]{Value.class};
        }

        @Override
        public Value execute(Context context) throws MuaExceptions, Function.FunctionStop {
            return new BoolValue(getValueArgAt(0) instanceof NumberValue);
        }
    }

    static final class IsWord extends Operator {
        {
            mArgTypes = new Class[]{Value.class};
        }

        @Override
        public Value execute(Context context) throws MuaExceptions, Function.FunctionStop {
            return new BoolValue(getValueArgAt(0) instanceof WordValue);
        }
    }

    static final class IsList extends Operator {
        {
            mArgTypes = new Class[]{Value.class};
        }

        @Override
        public Value execute(Context context) throws MuaExceptions, Function.FunctionStop {
            return new BoolValue(getValueArgAt(0) instanceof ListValue);
        }
    }

    static final class IsBool extends Operator {
        {
            mArgTypes = new Class[]{Value.class};
        }

        @Override
        public Value execute(Context context) throws MuaExceptions, Function.FunctionStop {
            return new BoolValue(getValueArgAt(0) instanceof BoolValue);
        }
    }

    static final class IsEmpty extends Operator {
        {
            mArgTypes = new Class[]{Value.class};
        }

        @Override
        public Value execute(Context context) throws MuaExceptions, Function.FunctionStop {
            Value value = getValueArgAt(0);
            if (value instanceof WordValue)
                return new BoolValue(((WordValue) value).isEmpty());
            else if (value instanceof ListValue)
                return new BoolValue(((ListValue) value).isEmpty());
            else
                throw new MuaExceptions.InvalidArgumentTypeException(value.getClass(), Value.class);
        }
    }

    static final class Sqrt extends Operator {
        {
            mArgTypes = new Class[]{NumberValue.class};
        }

        @Override
        public Value execute(Context context) throws MuaExceptions, Function.FunctionStop {
            return getNumArgAt(0).sqrt();
        }
    }

    static final class Random extends Operator {
        private static java.util.Random mRandom = new java.util.Random();

        {
            mArgTypes = new Class[]{NumberValue.class};
        }

        @Override
        public Value execute(Context context) throws MuaExceptions, Function.FunctionStop {
            return new NumberValue(mRandom.nextInt(getNumArgAt(0).intValue()));
        }
    }

    static final class Int extends Operator {
        {
            mArgTypes = new Class[]{NumberValue.class};
        }

        @Override
        public Value execute(Context context) throws MuaExceptions, Function.FunctionStop {
            return new NumberValue(getNumArgAt(0).intValue());
        }
    }

    static final class Word extends Operator {
        {
            mArgTypes = new Class[]{WordValue.class, Value.class};
        }

        @Override
        public Value execute(Context context) throws MuaExceptions, Function.FunctionStop {
            Value value = getValueArgAt(1);
            if (!(value instanceof WordValue) && !(value instanceof NumberValue) && !(value instanceof BoolValue))
                throw new MuaExceptions.InvalidArgumentTypeException(value.getClass(), Value.class);
            return getWordArgAt(0).concat(value);
        }
    }

    static final class Sentence extends Operator {
        {
            mArgTypes = new Class[]{Value.class, Value.class};
        }

        @Override
        public Value execute(Context context) throws MuaExceptions, Function.FunctionStop {
            return ListValue.concat(getValueArgAt(0), getValueArgAt(1));
        }
    }

    static final class List extends Operator {
        {
            mArgTypes = new Class[]{Value.class, Value.class};
        }

        @Override
        public Value execute(Context context) throws MuaExceptions, Function.FunctionStop {
            return ListValue.join(getValueArgAt(0), getValueArgAt(1));
        }
    }

    static final class Join extends Operator {
        {
            mArgTypes = new Class[]{ListValue.class, Value.class};
        }

        @Override
        public Value execute(Context context) throws MuaExceptions, Function.FunctionStop {
            return getListValueAt(0).append(getValueArgAt(1));
        }
    }

    static final class First extends Operator {
        {
            mArgTypes = new Class[]{Value.class};
        }

        @Override
        public Value execute(Context context) throws MuaExceptions, Function.FunctionStop {
            Value value = getValueArgAt(0);
            if (value instanceof ListValue)
                return ((ListValue) value).first();
            else if (value instanceof WordValue)
                return ((WordValue) value).first();
            else
                throw new MuaExceptions.InvalidArgumentTypeException(
                        value.getClass(), Value.class);
        }
    }

    static final class Last extends Operator {
        {
            mArgTypes = new Class[]{Value.class};
        }

        @Override
        public Value execute(Context context) throws MuaExceptions, Function.FunctionStop {
            Value value = getValueArgAt(0);
            if (value instanceof ListValue)
                return ((ListValue) value).last();
            else if (value instanceof WordValue)
                return ((WordValue) value).last();
            else
                throw new MuaExceptions.InvalidArgumentTypeException(
                        value.getClass(), Value.class);
        }
    }

    static final class ButFirst extends Operator {
        {
            mArgTypes = new Class[]{Value.class};
        }

        @Override
        public Value execute(Context context) throws MuaExceptions, Function.FunctionStop {
            Value value = getValueArgAt(0);
            if (value instanceof ListValue)
                return ((ListValue) value).removeFirst();
            else if (value instanceof WordValue)
                return ((WordValue) value).removeFirst();
            else
                throw new MuaExceptions.InvalidArgumentTypeException(
                        value.getClass(), Value.class);
        }
    }

    static final class ButLast extends Operator {
        {
            mArgTypes = new Class[]{Value.class};
        }

        @Override
        public Value execute(Context context) throws MuaExceptions, Function.FunctionStop {
            Value value = getValueArgAt(0);
            if (value instanceof ListValue)
                return ((ListValue) value).removeLast();
            else if (value instanceof WordValue)
                return ((WordValue) value).removeLast();
            else
                throw new MuaExceptions.InvalidArgumentTypeException(
                        value.getClass(), Value.class);
        }
    }

    static final class Wait extends Operator {
        {
            mArgTypes = new Class[]{NumberValue.class};
        }

        @Override
        public Value execute(Context context) throws MuaExceptions, Function.FunctionStop {
            try {
                wait(getNumArgAt(0).intValue());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    static final class Save extends Operator {
        {
            mArgTypes = new Class[]{WordValue.class};
        }

        @Override
        public Value execute(Context context) throws MuaExceptions, Function.FunctionStop {
            context.save(getWordArgAt(0));
            return null;
        }
    }

    static final class Load extends Operator {
        {
            mArgTypes = new Class[]{WordValue.class};
        }

        @Override
        public Value execute(Context context) throws MuaExceptions, Function.FunctionStop {
            context.load(getWordArgAt(0));
            return null;
        }
    }

    static final class EraseAll extends Operator {
        {
            mArgTypes = new Class[0];
        }

        @Override
        public Value execute(Context context) throws MuaExceptions, Function.FunctionStop {
            context.clear();
            return null;
        }
    }

    static final class PostAll extends Operator {
        {
            mArgTypes = new Class[0];
        }

        @Override
        public Value execute(Context context) throws MuaExceptions, Function.FunctionStop {
            context.listAll();
            return null;
        }
    }

    public static final class Run extends Operator {
        {
            mArgTypes = new Class[]{NumberValue.class, ListValue.class};
        }

        @Override
        public Value execute(Context context) throws MuaExceptions, Function.FunctionStop {
            CodeFragment codeFragment = new CodeFragment(getListValueAt(0));
            context.run(codeFragment);
            return null;
        }

        @Override
        public String toString() {
            return "[[code] [repeat 1 :code]]";
        }
    }
}


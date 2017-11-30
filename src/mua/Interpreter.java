package mua;

import mua.exceptions.MuaExceptions;
import mua.interfaces.Context;
import mua.interfaces.Executable;
import mua.interfaces.Fragment;
import mua.values.*;

import java.io.PrintStream;
import java.util.Scanner;

public class Interpreter implements Context {
    private SymbolTable mSymbolTable = new SymbolTable();
    private PrintStream mOut;
    private Scanner mCodeScanner;
    private Scanner mInputScanner;

    {
        mSymbolTable.merge(Operator.DEFINED_OPS);
    }

    public Interpreter(Scanner scanner, PrintStream printStream) {
        mCodeScanner = scanner;
        mOut = printStream;
        mInputScanner = scanner;
        // mPrompt = printStream;
    }

    /*
    public Interpreter(Scanner scanner, PrintStream printStream, PrintStream promptStream) {
        mCodeScanner = scanner;
        mOut = printStream;
        mPrompt = promptStream;
    }

    public void printPrompt() {
        mPrompt.print(PROMPT);
    }
    */

    @Override
    public Value nextRawInstruction() throws MuaExceptions.MissingArgumentException {
        if (!mCodeScanner.hasNext())
            throw new MuaExceptions.MissingArgumentException();
        String nextItem = mCodeScanner.next();
        if (nextItem.startsWith("//")) {
            mCodeScanner.nextLine();
            if (!mCodeScanner.hasNext())
                throw new MuaExceptions.MissingArgumentException();
            nextItem = mCodeScanner.next();
        }
        return new WordValue(nextItem);
    }

    @Override
    public Value nextInstruction(Fragment fragment) throws MuaExceptions, Function.FunctionStop {
        if (fragment == this)
            return nextInstruction();
        else
            return Context.super.nextInstruction(fragment);
    }

    @Override
    public Value nextInstruction() throws MuaExceptions, Function.FunctionStop {
        String nextItem = nextRawInstruction().toString();
        if (nextItem.startsWith("[")) {
            return ListValue.Builder.fromCode(nextItem, this);
        } else if (NumberValue.isNumber(nextItem)) {
            return NumberValue.parse(nextItem);
        } else if (Expression.isExpression(nextItem)) {
            return Expression.evaluate(this, Expression.build(nextItem, this));
        } else if (nextItem.startsWith("\"")) {
            return new WordValue(nextItem.substring(1));
        } else if (nextItem.startsWith(":")) {
            return getSymbol(nextItem.substring(1));
        } else {
            if (isExecutable(nextItem)) {
                return (Value) getExecutable(nextItem);
            } else {
                throw new MuaExceptions.UnknownOperatorException(nextItem);
            }
        }
    }


    @Override
    public SymbolTable getSymbolTable() {
        return mSymbolTable;
    }

    @Override
    public void addSymbol(String symbol, Value value) {
        mSymbolTable.put(symbol, value);
    }

    @Override
    public Value getSymbol(String symbol) {
        return mSymbolTable.get(symbol);
    }

    @Override
    public boolean isSymbol(String string) {
        return mSymbolTable.hasSymbol(string);
    }

    @Override
    public void removeSymbol(String symbol) {
        mSymbolTable.remove(symbol);
    }

    @Override
    public void print(Value value) {
        mOut.println(value);
    }

    @Override
    public boolean isExecutable(String instruction) {
        return isSymbol(instruction) && getSymbol(instruction) instanceof Executable;
    }

    @Override
    public Executable getExecutable(String instruction) {
        return ((Executable) mSymbolTable.get(instruction)).clone();
    }

    @Override
    public Value read() throws MuaExceptions {
        // mPrompt.print(READ_PROMPT);
        if (!mInputScanner.hasNext())
            throw new MuaExceptions.MissingArgumentException();
        String item = mInputScanner.next();
        if (NumberValue.isNumber(item)) {
            return NumberValue.parse(item);
        } else
            return new WordValue(item);
    }

    @Override
    public Value readList() throws MuaExceptions {
        // String line = mCodeScanner.nextLine();
        return ListValue.Builder.fromInput(mInputScanner);
        // return mParser.parse(mLexer.lex("[" + line + "]").poll());
    }

    @Override
    public boolean hasNextInstruction() {
        return mCodeScanner.hasNext();
    }
}

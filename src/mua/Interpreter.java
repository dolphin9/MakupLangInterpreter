package mua;

import mua.exceptions.MuaExceptions;
import mua.interfaces.Context;
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
        mSymbolTable.put("pi", new NumberValue(3.14159));
        mSymbolTable.put("run", new Operator.Run());
    }

    public Interpreter(Scanner scanner, PrintStream printStream) {
        mCodeScanner = scanner;
        mOut = printStream;
        mInputScanner = scanner;
    }

    public Interpreter(Scanner codeScanner, PrintStream printStream, Scanner inputScanner) {
        mCodeScanner = codeScanner;
        mOut = printStream;
        mInputScanner = inputScanner;
    }

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
        } else if (nextItem.startsWith("\"")) {
            return new WordValue(nextItem.substring(1));
        } else if (NumberValue.isNumber(nextItem)) {
            return NumberValue.parse(nextItem);
        } else if (Expression.isExpression(nextItem)) {
            return Expression.evaluate(this, Expression.build(nextItem, this));
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
    public void print(Value value) {
        mOut.println(value);
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
    public void clear() {
        mSymbolTable.clear();
    }

    @Override
    public void listAll(SymbolTable symbolTable) {
        symbolTable.listAll(mOut);
    }

    @Override
    public boolean hasNextInstruction() {
        return mCodeScanner.hasNext();
    }
}

package mua.exceptions;

import mua.interfaces.Executable;
import mua.values.Value;

public class MuaExceptions extends Exception {
    private MuaExceptions(String message) {
        super(message);
    }

    public static class InvalidArgumentTypeException extends MuaExceptions {
        public InvalidArgumentTypeException(Class<? extends Value> type,
                                            Class<? extends Value> expectedType) {
            super("Invalid argument type: " + type.getSimpleName() +
                    ", expected: " + expectedType.getSimpleName());
        }
    }

    public static class BracketNotPairedException extends MuaExceptions {
        public BracketNotPairedException() {
            super("Bracket not paired.");
        }
    }

    public static class MissingArgumentException extends MuaExceptions {
        public MissingArgumentException() {
            super("Missing argument.");
        }
    }

    public static class UnknownOperatorException extends MuaExceptions {
        public UnknownOperatorException(String opStr) {
            super("Unknown operator: " + opStr);
        }
    }

    public static class InitFunctionFailingException extends MuaExceptions {
        public InitFunctionFailingException(Executable func) {
            super("Init function failed: " + func);
        }
    }

    public static class InvalidInputException extends MuaExceptions {
        public InvalidInputException() {
            super("Invalid input:");
        }
    }
}

package mua;

import mua.parser.InvalidArgumentTypeException;
import mua.parser.SyntaxErrorException;

import java.io.IOException;

/**
 * Created by whjpji on 17-11-27.
 */
public interface Executable {
    Value execute(Context context) throws SyntaxErrorException, LexicalErrorException, IOException;

    boolean needsMoreArguments();

    void addArgument(Value argument) throws InvalidArgumentTypeException;
}

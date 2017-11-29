package mua;

import mua.parser.UnknownOperatorException;

import java.io.IOException;

public interface Fragment {
    boolean hasNextInstruction();

    Value nextInstruction() throws IOException, LexicalErrorException, InstantiationException, IllegalAccessException, UnknownOperatorException;

}

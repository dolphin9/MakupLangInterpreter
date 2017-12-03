package mua.parser;

import mua.lexer.LexicalErrorException;

public class SyntaxErrorException extends Exception {
    // TODO: 17-9-28 To be implemented.

}

class UnknownOperatorException extends SyntaxErrorException {
    // TODO: 17-9-28 To be implemented.
}

class InvalidArgumentTypeException extends SyntaxErrorException {
    // TODO: 17-9-28 To be implemented.
}

class MissingArgumentException extends SyntaxErrorException {

}

class RedundentTokenException extends SyntaxErrorException {

}

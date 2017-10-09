package mua.parser;

public class SyntaxErrorException extends Exception {
    // TODO: 17-9-28 To be implemented.

}

class InvalidArgumentTypeException extends SyntaxErrorException {
    // TODO: 17-9-28 To be implemented.
}

class MissingArgumentException extends SyntaxErrorException {

}

class RedundentTokenException extends SyntaxErrorException {

}

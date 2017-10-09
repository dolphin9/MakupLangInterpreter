package mua.parser;

import mua.lexer.Token;

import java.util.ArrayList;
import java.util.Queue;
import java.util.Stack;

public class Parser {
    public ParseTree parse(Queue<Token> tokens) throws SyntaxErrorException {
        Stack<OperatorNode> opNodeStack = new Stack<>();
        Token token = tokens.poll();
        if (!token.isString())
            throw new UnknownOperatorException();
        String tokenStr = (String) token.getValue();
        OperatorNode root = OperatorNode.extract(tokenStr);
        opNodeStack.push(root);
        while (!opNodeStack.isEmpty()) {
            OperatorNode op = opNodeStack.peek();
            if (op.needsMoreArguments()) {
                if (tokens.isEmpty())
                    throw new MissingArgumentException();
                ParseNode node = parse(tokens.poll(), op.nextArgType());
                if (node.isOperator() && ((OperatorNode) node).needsMoreArguments())
                    opNodeStack.push((OperatorNode) node);
                else
                    op.addArgument(node);
            } else {
                opNodeStack.pop();
                if (!opNodeStack.empty()) {
                    opNodeStack.peek().addArgument(op);
                } else
                    root = op;
            }
        }
        if (!tokens.isEmpty())
            throw new RedundentTokenException();

        return new ParseTree(root);
    }

    public ValueNode parse(Token token) throws SyntaxErrorException {
        return (ValueNode) parse(token, ParseNode.kValue);
    }

    public ParseNode parse(Token token, NodeType type) throws SyntaxErrorException {
        if (type.isWord()) {
            if (!token.isString())
                throw new InvalidArgumentTypeException();
            String tokenStr = (String) token.getValue();
            if (tokenStr.startsWith("\""))
                return new WordNode(tokenStr.substring(1));
            else if (tokenStr.startsWith(":"))
                return OperatorNode.extract(tokenStr);
            else
                throw new InvalidArgumentTypeException();
        } else if (type.isValue()) {
            if (token.isNumber()) {
                return new NumberNode((Number) token.getValue());
            } else if (token.isList()) {
                return new ListNode((ArrayList<Token>) token.getValue());
            } else if (token.isString()) {
                String tokenStr = (String) token.getValue();
                try {
                    return OperatorNode.extract(tokenStr);
                } catch (UnknownOperatorException e) {
                    return new StringNode(tokenStr);
                }
            } else
                throw new InvalidArgumentTypeException();
        } else
            throw new InvalidArgumentTypeException();
    }
}


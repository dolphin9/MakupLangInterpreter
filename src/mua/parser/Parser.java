package mua.parser;

import mua.Context;
import mua.lexer.LexicalErrorException;
import mua.lexer.ListToken;
import mua.lexer.Token;

import java.util.Queue;
import java.util.Stack;

public class Parser {
    private Context mContext;

    public Parser(Context context) {
        mContext = context;
    }

    public ParseTree parse(Queue<Token> tokens) throws SyntaxErrorException, LexicalErrorException {
        Stack<OperatorNode> opNodeStack = new Stack<>();
        OperatorNode root = null;
        while (!tokens.isEmpty()) {
            Token token = tokens.poll();
            if (!token.isString())
                throw new UnknownOperatorException();
            String tokenStr = (String) token.getValue();
            root = OperatorNode.extract(tokenStr);
            opNodeStack.push(root);
            while (!opNodeStack.isEmpty()) {
                OperatorNode op = opNodeStack.peek();
                if (op.needsMoreArguments()) {
                    while (tokens.isEmpty()) {
                        if (mContext.isNested())
                            throw new MissingArgumentException();
                        tokens = mContext.parserWait();
                    }
                    ParseNode node = parse(tokens.poll());
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
            if (!tokens.isEmpty()) {
                mContext.run(root);
                root = null;
            }
        }

        return new ParseTree(root);
    }

    public ValueNode parse(Token token) throws SyntaxErrorException {
        if (token.isString()) {
            String tokenStr = (String) token.getValue();
            try {
                return OperatorNode.extract(tokenStr);
            } catch (UnknownOperatorException e) {
                return new StringNode(tokenStr);
            }
        } else if (token.isNumber()) {
            return new NumberNode((Number) token.getValue());
        } else if (token.isList()) {
            return new ListNode((ListToken) token);
        } else
            throw new InvalidArgumentTypeException();
    }
}


package org.vsu.amm.calculator;

import java.util.List;

public class Calculator {
    private final List<Token> tokens;
    private final VariableContext context;
    private int pos = 0;

    public Calculator(List<Token> tokens, VariableContext context) {
        this.tokens = tokens;
        this.context = context;
    }

    public double calculate() {
        return expression();
    }

    private double expression() {
        //TODO: implement method
        return 0.0;
    }

    private double term() {
        //TODO: implement method
        return 0.0;
    }

    private double factor() {
        //TODO: implement method
        return 0.0;
    }

    private boolean match() {
        //TODO: implement method
        return false;
    }

    private boolean check() {
        //TODO: implement method
        return false;
    }

    private Token advance() {
        //TODO: implement method
        return null;
    }

    private boolean isAtEnd() {
        return peek().type == TokenType.EOF;
    }

    private Token peek() {
        return tokens.get(pos);
    }

    private Token previous() {
        return tokens.get(pos - 1);
    }
}

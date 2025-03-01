package org.vsu.amm.calculator;

public class Token {
    public final TokenType type;
    public final String value;

    public Token(TokenType type, String value){
        this.type = type;
        this.value = value;
    }

    @Override
    public String toString(){
        return String.format("Token(%s, %s)", type, value);
    }
}

package org.vsu.amm.calculator;
import java.util.ArrayList;
import java.util.List;

public class ExpressionParser {
    private final String input;
    private final VariableContext context;
    private int pos = 0;

    public ExpressionParser(String input, VariableContext context){
        this.input = input;
        this.context=context;
    }
    public List<Token> parse(){
        List<Token> tokens = new ArrayList<>();
        while (pos < input.length()) {
            char ch = input.charAt(pos);

            if (Character.isWhitespace(ch)) {
                pos++;
            }
            else if (Character.isDigit(ch) || ch == '.'){
                tokens.add(readNumber());
            }
            else if (Character.isLetter(ch)){
                tokens.add(readIdentifier());
            }
            else if (ch == '+' || ch == '-' || ch == '*' || ch == '/'){
                tokens.add(new Token(TokenType.OPERATOR, String.valueOf(ch)));
                pos++;
            }
            else if (ch == '('){
                tokens.add(new Token(TokenType.LEFT_PARENTHESIS, "("));
            }
            else if (ch == ')'){
                tokens.add(new Token(TokenType.RIGHT_PARENTHESIS, ")"));
            }
            else{
                throw new RuntimeException("Unexpected character: " + ch);
            }
            tokens.add(new Token(TokenType.EOF, ""));

            return tokens;
        }
    }

    private Token readNumber(){
        //to be implemented
        return new Token();
    }

    private Token readIdentifier(){
        //to be implemented
        return new Token();
    }
}
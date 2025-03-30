package org.vsu.amm.calculator;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс для разбора математических выражений.
 * <p>
 * Осуществляет лексический анализ входной строки, преобразуя её в список токенов.
 * Поддерживает числа, операторы, скобки, переменные и тригонометрические функции (sin, cos, tan).
 * </p>
 *
 * @author Jordosi
 * @version 1.0
 * @see Token
 * @see TokenType
 */
public class ExpressionParser {
    private final String input;
    private int pos = 0;

    /**
     * Создает новый экземпляр парсера для указанного выражения.
     *
     * @param input строка с математическим выражением для разбора
     * @throws IllegalArgumentException если входная строка равна null
     */
    public ExpressionParser(String input){
        if (input == null){
            throw new IllegalArgumentException("Input string cannot be null");
        }
        this.input = input;
    }

    /**
     * Выполняет разбор математического выражения.
     * <p>
     * Метод проходит по каждому символу входной строки и формирует список токенов,
     * распознавая числа, операторы, скобки, переменные и функции.
     * </p>
     *
     * @return список токенов, представляющих разобранное выражение
     * @throws RuntimeException если встречается неподдерживаемый символ
     */
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
                pos++;
            }
            else if (ch == ')'){
                tokens.add(new Token(TokenType.RIGHT_PARENTHESIS, ")"));
                pos++;
            }
            else {
                throw new RuntimeException("Unexpected character: " + ch);
            }
        }
        tokens.add(new Token(TokenType.EOF, ""));
        return tokens;
    }

    /**
     * Читает число из входной строки и преобразует его в токен.
     * <p>
     * Поддерживает как целые, так и дробные числа (с точкой в качестве разделителя).
     * Позиция парсера продвигается до первого символа после числа.
     * </p>
     *
     * @return токен типа NUMBER, содержащий числовое значение
     */
    private Token readNumber(){
        StringBuilder sb = new StringBuilder();
        while (pos < input.length()
                && (Character.isDigit(input.charAt(pos))
                || input.charAt(pos) == '.')) {
            sb.append(input.charAt(pos++));
        }
        return new Token(TokenType.NUMBER, sb.toString());
    }

    /**
     * Читает идентификатор из входной строки и преобразует его в токен.
     * <p>
     * Идентификатор может быть либо именем функции (sin, cos, tan),
     * либо именем переменной. Позиция парсера продвигается до первого символа
     * после идентификатора.
     * </p>
     *
     * @return токен типа FUNCTION или VARIABLE в зависимости от значения идентификатора
     */
    private Token readIdentifier(){
        StringBuilder sb = new StringBuilder();
        while (pos < input.length() && Character.isLetter(input.charAt(pos))) {
            sb.append(input.charAt(pos++));
        }
        String identifier = sb.toString();
        if (identifier.equals("sin") || identifier.equals("cos") || identifier.equals("tan")) {
            return new Token(TokenType.FUNCTION, identifier);
        } else {
            return new Token(TokenType.VARIABLE, identifier);
        }
    }
}
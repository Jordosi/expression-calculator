package org.vsu.amm.calculator;

import java.util.List;

/**
 * Класс для вычисления значения математического выражения, представленного списком токенов.
 * <p>
 * Реализует рекурсивный нисходящий парсер (recursive descent parser) для вычисления выражений
 * с учетом приоритетов операций и скобок. Поддерживает:
 * <ul>
 *   <li>Базовые арифметические операции: +, -, *, /</li>
 *   <li>Скобки для изменения порядка вычислений</li>
 *   <li>Переменные (значения запрашиваются через VariableContext)</li>
 *   <li>Тригонометрические функции: sin, cos, tan (аргумент в градусах)</li>
 * </ul>
 *
 * @author Jordosi
 * @version 1.0
 * @see ExpressionParser
 * @see Token
 * @see VariableContext
 */
public class Calculator {
    private final List<Token> tokens;
    private final VariableContext context;
    private int pos = 0;

    /**
     * Создает экземпляр калькулятора для вычисления выражения.
     *
     * @param tokens список токенов, полученных от парсера
     * @param context контекст переменных для получения их значений
     * @throws IllegalArgumentException если tokens или context равны null
     */
    public Calculator(List<Token> tokens, VariableContext context) {
        if (context == null || tokens == null){
            throw new IllegalArgumentException("Tokens and context cannot be null");
        }
        this.tokens = tokens;
        this.context = context;
    }

    /**
     * Вычисляет значение выражения.
     *
     * @return результат вычисления выражения
     * @throws RuntimeException при синтаксических ошибках в выражении
     */
    public double calculate() {
        double result = expression();
        if (!isAtEnd()) {
            throw new RuntimeException("Unexpected token: " + peek().value);
        }
        return result;
    }

    /**
     * Обрабатывает операции сложения и вычитания.
     * Реализует правило: expression → term (('+' | '-') term)*
     */
    private double expression() {
        double result = term();
        while (true) {
            if (match(TokenType.OPERATOR, "+")) {
                result += term();
            } else if (match(TokenType.OPERATOR, "-")) {
                result -= term();
            } else {
                break;
            }
        }
        return result;
    }

    /**
     * Обрабатывает операции умножения и деления.
     * Реализует правило: term → factor (('*' | '/') factor)*
     */
    private double term() {
        double result = factor();
        while (true) {
            if (match(TokenType.OPERATOR, "*")) {
                result *= factor();
            } else if (match(TokenType.OPERATOR, "/")) {
                double divisor = factor();
                if (divisor == 0) throw new ArithmeticException("Division by zero");
                result /= divisor;
            } else {
                break;
            }
        }
        return result;
    }

    /**
     * Обрабатывает базовые элементы выражения.
     * Реализует правило: factor → NUMBER | VARIABLE | FUNCTION '(' expression ')' | '(' expression ')'
     */
    private double factor() {
        if (match(TokenType.LEFT_PARENTHESIS)) {
            double result = expression();
            consume(TokenType.RIGHT_PARENTHESIS, "Expect ')' after expression");
            return result;
        }
        else if (match(TokenType.NUMBER)) {
            return Double.parseDouble(previous().value);
        }
        else if (match(TokenType.VARIABLE)) {
            return context.getVariableValue(previous().value);
        }
        else if (match(TokenType.FUNCTION)) {
            String funcName = previous().value;
            consume(TokenType.LEFT_PARENTHESIS, "Expect '(' after function name");
            double arg = expression();
            consume(TokenType.RIGHT_PARENTHESIS, "Expect ')' after function argument");
            return applyFunction(funcName, arg);
        }
        throw new RuntimeException("Unexpected token: " + peek().value);
    }

    /**
     * Применяет тригонометрическую функцию к аргументу.
     *
     * @param funcName имя функции (sin, cos, tan)
     * @param arg аргумент в градусах
     * @return результат применения функции
     * @throws IllegalArgumentException если имя функции неизвестно
     */
    private double applyFunction(String funcName, double arg) {
        switch (funcName) {
            case "sin": return Math.sin(Math.toRadians(arg));
            case "cos": return Math.cos(Math.toRadians(arg));
            case "tan": return Math.tan(Math.toRadians(arg));
            default: throw new IllegalArgumentException("Unknown function: " + funcName);
        }
    }

    /**
     * Проверяет, соответствует ли текущий токен заданному типу и значению.
     * Если соответствует - потребляет токен (переходит к следующему).
     *
     * @param type ожидаемый тип токена
     * @param value ожидаемое значение токена
     * @return true если токен соответствует, иначе false
     */
    private boolean match(TokenType type, String value) {
        if (check(type) && peek().value.equals(value)) {
            pos++;
            return true;
        }
        return false;
    }

    /**
     * Проверяет, соответствует ли текущий токен заданному типу.
     * Если соответствует - потребляет токен (переходит к следующему).
     *
     * @param type ожидаемый тип токена
     * @return true если токен соответствует, иначе false
     */
    private boolean match(TokenType type) {
        if (check(type)) {
            pos++;
            return true;
        }
        return false;
    }

    /**
     * Потребляет текущий токен, если он соответствует ожидаемому типу.
     * В противном случае выбрасывает исключение с сообщением об ошибке.
     *
     * @param type ожидаемый тип токена
     * @param message сообщение об ошибке, если токен не соответствует
     * @return потребленный токен
     * @throws RuntimeException если текущий токен не соответствует ожидаемому типу
     */
    private Token consume(TokenType type, String message) {
        if (check(type)) return advance();
        throw new RuntimeException(message);
    }

    /**
     * Проверяет, соответствует ли текущий токен заданному типу
     * без его потребления (без продвижения позиции).
     *
     * @param type тип токена для проверки
     * @return true если текущий токен соответствует заданному типу
     */
    private boolean check(TokenType type) {
        if (isAtEnd()) return false;
        return peek().type == type;
    }

    /**
     * Переходит к следующему токену и возвращает предыдущий.
     *
     * @return предыдущий токен
     */
    private Token advance() {
        if (!isAtEnd()) pos++;
        return previous();
    }

    /**
     * Проверяет, достигнут ли конец списка токенов.
     *
     * @return true если все токены обработаны
     */
    private boolean isAtEnd() {
        return peek().type == TokenType.EOF;
    }

    /**
     * Возвращает текущий токен без продвижения позиции.
     *
     * @return текущий токен
     */
    private Token peek() {
        return tokens.get(pos);
    }

    /**
     * Возвращает предыдущий токен.
     *
     * @return предыдущий токен
     * @throws IndexOutOfBoundsException если позиция меньше 1
     */
    private Token previous() {
        return tokens.get(pos - 1);
    }
}

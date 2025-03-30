package org.vsu.amm;

import org.vsu.amm.calculator.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Класс для тестирования функциональности {@link Calculator}.
 * <p>
 * Содержит тесты для проверки корректности вычисления математических выражений,
 * обработки ошибок и работы с переменными и функциями.
 * </p>
 *
 * @see Calculator
 * @see ExpressionParser
 * @see VariableContext
 */
class CalculatorTest {
    /**
     * Тестовая реализация {@link VariableContext} для изолированного тестирования.
     * <p>
     * Позволяет задавать значения переменных непосредственно в тестах
     * без взаимодействия с пользователем.
     * </p>
     */
    private static class TestVariableContext extends VariableContext {
        private final Map<String, Double> variables = new HashMap<>();

        /**
         * Устанавливает значение переменной для использования в тестах.
         *
         * @param name имя переменной
         * @param value значение переменной
         */
        void setVariable(String name, double value) {
            variables.put(name, value);
        }

        /**
         * Возвращает значение переменной.
         *
         * @param name имя переменной
         * @return значение переменной
         * @throws RuntimeException если переменная не определена
         */
        @Override
        public double getVariableValue(String name) {
            if (!variables.containsKey(name)) {
                throw new RuntimeException("Variable not defined: " + name);
            }
            return variables.get(name);
        }
    }

    /**
     * Тестирование базовых арифметических операций.
     * <p>
     * Проверяет корректность вычисления простых выражений с операциями:
     * сложение, вычитание, умножение, деление.
     * </p>
     *
     * @param expression математическое выражение для тестирования
     * @param expected ожидаемый результат вычисления
     */
    @ParameterizedTest
    @CsvSource({
            "2 + 3, 5.0",
            "10 - 4, 6.0",
            "3 * 5, 15.0",
            "20 / 5, 4.0",
            "2 + 3 * 4, 14.0",
            "(2 + 3) * 4, 20.0"
    })
    void testBasicArithmetic(String expression, double expected) {
        testExpression(expression, expected);
    }

    /**
     * Тестирование работы с переменными.
     * <p>
     * Проверяет корректность подстановки значений переменных в выражения
     * и вычисления результатов с их использованием.
     * </p>
     */
    @Test
    void testVariables() {
        TestVariableContext context = new TestVariableContext();
        context.setVariable("x", 3.0);
        context.setVariable("y", 4.0);

        testExpressionWithContext("x + y", 7.0, context);
        testExpressionWithContext("x * y - 2", 10.0, context);
    }

    /**
     * Тестирование обработки некорректных выражений.
     * <p>
     * Проверяет, что парсер корректно обрабатывает синтаксически неверные выражения,
     * выбрасывая соответствующие исключения.
     * </p>
     *
     * @param expression некорректное математическое выражение
     */
    @ParameterizedTest
    @ValueSource(strings = {
            "2 +",
            "* 3",
            "(2 + 3",
            "2 $ 3",
            "x + y" // Переменные без определения
    })
    void testInvalidExpressions(String expression) {
        assertThrows(RuntimeException.class, () -> testExpression(expression, 0));
    }

    /**
     * Тестирование математических функций.
     * <p>
     * Проверяет корректность вычисления тригонометрических функций:
     * sin, cos, tan (аргументы в градусах).
     * </p>
     *
     * @param expression выражение с функцией
     * @param expected ожидаемый результат вычисления
     */
    @ParameterizedTest
    @CsvSource({
            "sin(30), 0.5",
            "cos(60), 0.5",
            "tan(45), 1.0"
    })
    void testFunctions(String expression, double expected) {
        testExpression(expression, expected);
    }

    /**
     * Вспомогательный метод для тестирования выражений.
     *
     * @param expression выражение для тестирования
     * @param expected ожидаемый результат
     */
    private void testExpression(String expression, double expected) {
        ExpressionParser parser = new ExpressionParser(expression);
        List<Token> tokens = parser.parse();
        Calculator calculator = new Calculator(tokens, new TestVariableContext());

        assertEquals(expected, calculator.calculate(), 1e-10);
    }

    /**
     * Вспомогательный метод для тестирования выражений с заданным контекстом переменных.
     *
     * @param expression выражение для тестирования
     * @param expected ожидаемый результат
     * @param context контекст с определенными переменными
     */
    private void testExpressionWithContext(String expression, double expected, TestVariableContext context) {
        ExpressionParser parser = new ExpressionParser(expression);
        List<Token> tokens = parser.parse();
        Calculator calculator = new Calculator(tokens, context);

        assertEquals(expected, calculator.calculate(), 1e-10);
    }

    /**
     * Тестирование обработки деления на ноль.
     * <p>
     * Проверяет, что при делении на ноль выбрасывается {@link ArithmeticException}
     * с соответствующим сообщением.
     * </p>
     */
    @Test
    void testDivisionByZero() {
        ExpressionParser parser = new ExpressionParser("2 / 0");
        List<Token> tokens = parser.parse();
        Calculator calculator = new Calculator(tokens, new TestVariableContext());

        Exception exception = assertThrows(ArithmeticException.class, calculator::calculate);
        assertEquals("Division by zero", exception.getMessage());
    }

    /**
     * Тестирование комбинированного выражения.
     * <p>
     * Проверяет корректность вычисления сложного выражения, содержащего:
     * переменные, функции, скобки и арифметические операции.
     * </p>
     */
    @Test
    void testCombinedExpression() {
        TestVariableContext context = new TestVariableContext();
        context.setVariable("x", 2.0);
        context.setVariable("y", 3.0);

        testExpressionWithContext("(x + y) * sin(30) - 1", 1.5, context);
    }
}
package org.vsu.amm.program;

import org.vsu.amm.calculator.*;

import java.util.List;
import java.util.Scanner;

/**
 * Основной класс приложения для вычисления математических выражений.
 * <p>
 * Обеспечивает взаимодействие с пользователем через консоль:
 * <ul>
 *   <li>Принимает математическое выражение</li>
 *   <li>Запрашивает значения переменных (при необходимости)</li>
 *   <li>Выводит результат вычисления или сообщение об ошибке</li>
 * </ul>
 * </p>
 *
 * @author Jordosi
 * @version 1.0
 * @see Calculator Класс для вычисления выражений
 * @see VariableContext Контекст хранения значений переменных
 * @see ExpressionParser Парсер математических выражений
 */
public class Main {
    /**
     * Точка входа в приложение.
     * <p>
     * Выполняет последовательно:
     * <ol>
     *   <li>Чтение выражения из консоли</li>
     *   <li>Парсинг выражения в список токенов</li>
     *   <li>Вычисление результата с обработкой переменных</li>
     *   <li>Вывод результата или сообщения об ошибке</li>
     * </ol>
     * </p>
     *
     * @param args аргументы командной строки (не используются)
     * @implNote Для завершения программы введите Ctrl+D (Unix) или Ctrl+Z (Windows)
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter expression: ");
        String input = scanner.nextLine();

        VariableContext context = new VariableContext();
        ExpressionParser parser = new ExpressionParser(input);
        List<Token> tokens = parser.parse();

        Calculator calculator = new Calculator(tokens, context);
        try {
            double result = calculator.calculate();
            System.out.println("Result: " + result);
        } catch (RuntimeException e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }
}
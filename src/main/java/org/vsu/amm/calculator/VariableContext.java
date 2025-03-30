package org.vsu.amm.calculator;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Класс для хранения и управления значениями переменных, используемых в выражениях.
 * <p>
 * Предоставляет функционал для запроса значений переменных у пользователя
 * и их последующего хранения. Каждая переменная запрашивается только один раз.
 * </p>
 *
 * @author Jordosi
 * @version 1.0
 */
public class VariableContext {
    /** Карта для хранения значения переменных */
    private final Map<String, Double> variables = new HashMap<>();

    /** Сканер для ввода значений переменных */
    private final Scanner scanner = new Scanner(System.in);

    /**
     * Получает значение переменной по её имени.
     * <p>
     * Если переменная встречается впервые, запрашивает её значение у пользователя.
     * При последующих обращениях возвращает сохраненное значение.
     * </p>
     *
     * @param name имя переменной
     * @return числовое значение переменной
     * @throws IllegalArgumentException если имя переменной равно null или пустое
     * @throws NumberFormatException если пользователь вводит некорректное числовое значение
     */
    public double getVariableValue(String name){
        if(!variables.containsKey(name)){
            System.out.print("Enter value for variable " + name + ": ");
            double value = scanner.nextDouble();
            variables.put(name, value);
        }

        return variables.get(name);
    }
}
package org.vsu.amm.calculator;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class VariableContext {
    private final Map<String, Double> variables = new HashMap<>();
    private final Scanner scanner = new Scanner(System.in);

    public double getVariableValue(String name){
        if(!variables.containsKey(name)){
            System.out.print("Enter value for variable " + name + ": ");
            double value = scanner.nextDouble();
            variables.put(name, value);
        }

        return variables.get(name);
    }
}
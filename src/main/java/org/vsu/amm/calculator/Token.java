package org.vsu.amm.calculator;

/**
 * Класс, представляющий токен - минимальную значимую единицу математического выражения.
 * <p>
 * Каждый токен имеет тип (из перечисления TokenType) и строковое значение.
 * </p>
 *
 * @author Jordosi
 * @version 1.0
 * @see TokenType
 */
public class Token {
    /** Тип токена */
    public final TokenType type;

    /** Лексическое значение токена */
    public final String value;

    /**
     * Создает новый токен указанного типа с заданным значением.
     *
     * @param type тип создаваемого токена
     * @param value лексическое значение токена
     * @throws IllegalArgumentException если значение равно null
     */
    public Token(TokenType type, String value){
        if (value == null){
            throw new IllegalArgumentException("Value cannot be null");
        }
        this.type = type;
        this.value = value;
    }

    /**
     * Возвращает строковое представление токена в формате "Token(тип, значение)".
     *
     * @return строковое представление токена
     */
    @Override
    public String toString(){
        return String.format("Token(%s, %s)", type, value);
    }
}

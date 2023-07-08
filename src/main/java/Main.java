import com.sun.source.tree.IfTree;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {
    // Римские числа
    private final static String[] ROMAN_NUMERALS = {"M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I"};
    // Арабские числа
    private final static int[] DECIMAL_VALUES = {1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1};

    public static void main(String[] args) throws WrongNumbers, DifferentNumberSystems, CorrespondingString, NegationNumbers {
        // System.out.println(calc("2-3"));
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String input = scanner.nextLine();
            System.out.println(calc(input));
        }

    }

    public static String calc(String input) throws WrongNumbers, DifferentNumberSystems, CorrespondingString, NegationNumbers {
        // Проверяет, содержится ли определенный оператор в инпуте. И если да, то добавляет тот или иной оператор в переменную .
        String operatorType = null;
        if (input.contains("+")) {
            operatorType = "\\+";
        } else if (input.contains("-")) {
            operatorType = "-";
        } else if (input.contains("*")) {
            operatorType = "\\*";
        } else if (input.contains("/")) {
            operatorType = "/";
        } else {
            System.out.println("Не подхоядший оператор");
        }

        //Разбивает на подстроку через определенный оператор в переменной operatorType
        assert operatorType != null;
        String[] split = input.replace(" ", "").split(operatorType);
        String firstValue = split[0];
        String lastValue = split[1];

        // Нужен, что-бы проверить, не превышает ли сравнения двух чисел.
        boolean isLengthTrue = split.length == 2;
        String result = null;

        // Проверяет через регулярное выражение какое значение передается. Арабик или роман
        //Roman числа
        if (firstValue.matches("[IVXLCDM]+") && lastValue.matches("[IVXLCDM]+")) {
            result = romanCalculate(firstValue, lastValue, operatorType, isLengthTrue);
        }
        //Arab числа
        else if (firstValue.matches("-?\\d+(\\.\\d+)?") && lastValue.matches("-?\\d+(\\.\\d+)?")) {
            result = arabCalculate(firstValue, lastValue, operatorType, isLengthTrue);
        }
        // Если обо сразу,то исключение
        else if (firstValue.matches("[IVXLCDM]+") && lastValue.matches("-?\\d+(\\.\\d+)?") || lastValue.matches("[IVXLCDM]+") && firstValue.matches("-?\\d+(\\.\\d+)?")) {
            throw new DifferentNumberSystems("Используются одновременно разные системы счисления");
        } else {
            throw new CorrespondingString("Cтрока не является математической операцией или превышает сравнение двух чисел");
        }
        return result;
    }

    // Арабский вычислитель
    static String arabCalculate(String firstValue, String lastValue, String operatorType, boolean isLengthTrue) throws WrongNumbers {
        int firstNumber = Integer.parseInt(firstValue);
        int lastNumber = Integer.parseInt(lastValue);
        // Проверяет, целые числа и подходят ли числа к лимуту от 1-10
        boolean isNumberLimitTrue = firstNumber >= 1 && firstNumber <= 10 && lastNumber >= 1 && lastNumber <= 10;
        String result = null;

        //Производит математическую операцию в зависимости от оператора в переменной operatorType.
        if (isNumberLimitTrue && operatorType.contains("+") && isLengthTrue) {
            result = String.valueOf(firstNumber + lastNumber);
        } else if (isNumberLimitTrue && operatorType.contains("-") && isLengthTrue) {
            result = String.valueOf(firstNumber - lastNumber);
        } else if (isNumberLimitTrue && operatorType.contains("*") && isLengthTrue) {
            result = String.valueOf(firstNumber * lastNumber);
        } else if (isNumberLimitTrue && operatorType.contains("/") && isLengthTrue) {
            result = String.valueOf(firstNumber / lastNumber);
        } else {
            throw new WrongNumbers("Неподходящие числа");
        }
        return result;
    }

    //Римский вычислитель
    static String romanCalculate(String firstValue, String lastValue, String operatorType, boolean isLengthTrue) throws WrongNumbers, NegationNumbers {
        int firstNumber = fromRomanNumeral(firstValue);
        int lastNumber = fromRomanNumeral(lastValue);

        //Временная переменная, что бы потом после вычисления преобразовать число в римское
        int calculate = 0;
        String result = null;

        // Проверяет, целые числа и подходят ли числа к лимуту от 1-10
        boolean isNumberLimitTrue = firstNumber >= 1 && firstNumber <= 10 && lastNumber >= 1 && lastNumber <= 10;

        if (isNumberLimitTrue && operatorType.contains("+") && isLengthTrue) {
            calculate = firstNumber + lastNumber;
        } else if (isNumberLimitTrue && operatorType.contains("-") && isLengthTrue) {
            calculate = firstNumber - lastNumber;
        } else if (isNumberLimitTrue && operatorType.contains("*") && isLengthTrue) {
            calculate = firstNumber * lastNumber;
        } else if (isNumberLimitTrue && operatorType.contains("/") && isLengthTrue) {
            calculate = firstNumber / lastNumber;
        } else {
            throw new WrongNumbers("Неподходящие числа");
        }
        if (calculate < 1) {
            throw new NegationNumbers("B римской системе нет отрицательных чисел");
        }
        result = toRomanNumeral(calculate);

        return result;
    }

    //Преобразователь в римский
    public static String toRomanNumeral(int number) {
        var result = new StringBuilder();
        int remaining = number;
        for (int i = 0; i < ROMAN_NUMERALS.length; i++) {
            int decimalValue = DECIMAL_VALUES[i];
            String romanNumeral = ROMAN_NUMERALS[i];
            while (remaining >= decimalValue) {
                result.append(romanNumeral);
                remaining -= decimalValue;
            }
        }
        return result.toString();
    }

    //Преобразователь в арабский
    public static int fromRomanNumeral(String romanNumeral) {
        int result = 0;
        var input = romanNumeral.toUpperCase();
        for (int i = 0; i < ROMAN_NUMERALS.length; i++) {
            var romanNumeralToCheck = ROMAN_NUMERALS[i];
            int decimalValue = DECIMAL_VALUES[i];
            while (input.startsWith(romanNumeralToCheck)) {
                result += decimalValue;
                input = input.substring(romanNumeralToCheck.length());
            }
        }
        return result;
    }

}

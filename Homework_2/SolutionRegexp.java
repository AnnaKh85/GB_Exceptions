import java.util.Scanner;
import java.util.regex.Pattern;

public class SolutionRegexp {

    //1. Реализуйте метод, который запрашивает у пользователя ввод дробного числа (типа float)
    private static float getFloatNum() {
        String PATTERN = "^\\d{0,10}[\\.]?\\d{1,10}$";
        //Pattern pattern = Pattern.compile("^\\d{0,10}[\\.]?\\d{1,10}$", Pattern.CASE_INSENSITIVE);
        Scanner scanner = new Scanner(System.in);

        while(true) {
            System.out.println("Введите дробное число (float: не более 10 знаков перед точкой и не более 10 знаков после точки)");
            System.out.print("-->");
            String value = scanner.nextLine().trim();
            //Matcher matcher = pattern.matcher(value);
            if (Pattern.matches(PATTERN, value)) {
                float f = Float.parseFloat(value);
                System.out.println("Успешный ввод. Вы ввели число:" + f);
                return f;
            } else {
                System.out.println("Ошибка: число " + value +" не является числом с плавающей точкой");
            }
        }
    }
    public static void main(String[] params) {
        //getFloatNum();
        task4();
    }


    //4. Разработайте программу, которая выбросит Exception, когда пользователь вводит пустую строку
    private static void task4() {
        Scanner scanner = new Scanner(System.in);

        while(true) {
            System.out.println("Введите непустую строку");
            System.out.print("-->");
            String value = scanner.nextLine();
            if (value.trim().length() > 0) {
                System.out.println("Успешный ввод. Вы ввели строку:" + value);
            } else {
                throw new RuntimeException("Ошибка: строка пуста");
            }
        }
    }


}

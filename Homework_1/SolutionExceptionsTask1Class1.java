public class SolutionExceptionsTask1Class1 {

    //1 Реализуйте 3 метода, чтобы в каждом из них получить разные исключения
    //NumberFormatException
    private static void method1() {
        String strValue = "абв";
        System.out.println("strValue" + Integer.parseInt(strValue));
    }
    //ArithmeticException
    private static void method2()  throws NullPointerException{
        boolean trueValue = System.currentTimeMillis() > 0;
        int result = Integer.MAX_VALUE / (trueValue ? 0 : 1);
        System.out.println("result=" + result);
    }
    //ClassCastException
    private static Long method3() {
        Object objValue = "345";
        return (Long) objValue;
    }

    public static void main(String[] params) {
//        method1();
//        method2();
//        method3();
//        methodRaznost(new int[]{1,2,3,4,5}, new int[] {9,8,7,6,5});
//        methodChastnoe(new int[]{1,2,3,4,5}, new int[] {9,8,0,6,5});
    }

    //////////////////////////////////////////////////////////////////////
    //2 Посмотрите на код, и подумайте сколько разных типов исключений вы тут сможете получить?
    //NullPointerException (для arr = null)
    //IntexOutOfBounds (для второй размерности < 5)
    //NumberFormatException
    public static int sum2d(String[][] arr) {
        int sum = 0;
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < 5; j++) {
                int val = Integer.parseInt(arr[i][j]);
                sum += val;
            }
        }
        return sum;
    }

    ///////////////////////////////////////////////////////////////////////
    //3 ...возвращающий новый массив, каждый элемент которого равен разности элементов двух входящих массивов
    private static int[] methodRaznost(int[] arr1, int[] arr2) {
        if (arr1 == null) throw new RuntimeException("Ошибка: первый массив NULL");
        if (arr2 == null) throw new RuntimeException("Ошибка: второй массив NULL");
        if (arr1.length != arr2.length) throw new RuntimeException("Ошибка: массивы разной длины");

        int[] result = new int[arr1.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = arr1[i] - arr2[i];
        }

        return result;
    }

    ///////////////////////////////////////////////////////////////////////
    //4 ... возвращающий новый массив, каждый элемент которого равен частному элементов двух входящих массивов
    private static double[] methodChastnoe(int[] arr1, int[] arr2) {
        if (arr1 == null) throw new RuntimeException("Ошибка: первый массив NULL");
        if (arr2 == null) throw new RuntimeException("Ошибка: второй массив NULL");
        if (arr1.length != arr2.length) throw new RuntimeException("Ошибка: массивы разной длины");

        double[] result = new double[arr1.length];
        for (int i = 0; i < result.length; i++) {
            if (arr2[i] == 0) throw new RuntimeException("Делитель, взятый из позиции [" + i + "] второго массива, равен нулю");
            result[i] = (double) arr1[i] / arr2[i];
        }

        return result;
    }

}

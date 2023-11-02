import java.lang.Math;

public class Main {
    public static void main(String[] args) {
        short[] c = initFirstArray();
        double[] x = initSecondArray();
        double[][] result = initThirdArray(c, x);

        print2DDoubleArray(result);
    }

    public static short[] initFirstArray() {
        var array = new short[10];
        for (int i = 0; i < 10; i++) {
            array[i] = i == 0 ? 24 : (short)(array[i - 1] - 2);
        }
        return array;
    }

    public static double[] initSecondArray() {
        var array = new double[20];
        for (int i = 0; i < 20; i++) {
            // Случайное число в диапазоне [min..max]
            // (Math.random() * (max - min)) + min
            double val = Math.random() * (9 + 5 + 1) - 5;
            array[i] = val > 9 ? 9 : val;
        }
        return array;
    }

    public static double[][] initThirdArray(short[] firstArray, double[] secondArray) {
        var array = new double[10][20];
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 20; j++) {
                switch (firstArray[i]) {
                case 10 ->
                    array[i][j] = Math.sin(Math.pow(
                                (Math.pow(secondArray[j], 3. / 4 * (secondArray[j] - 1))) /
                                (Math.pow(secondArray[j], secondArray[j] / (secondArray[j] - 1)) + (1. / 2)),
                                3));
                case 6, 8, 12, 16, 22 ->
                    array[i][j] = Math.pow(Math.log(Math.acos((secondArray[j] + 2) / 14.)),
                            ((Math.atan(Math.exp(-Math.abs(secondArray[j])))) / 1.) / 3.);
                default ->
                    array[i][j] = (3 +
                            Math.asin(Math.sin(Math.asin((secondArray[j] + 2) / 14.)))) / 0.5;
                }
            }
        }
        return array;
    }

    public static void print2DDoubleArray(double[][] array) {
        for (double[] itArray : array) {
            for (double it : itArray) {
                System.out.printf("%.2f\t", it);
            }
            System.out.print('\n');
        }
    }
}

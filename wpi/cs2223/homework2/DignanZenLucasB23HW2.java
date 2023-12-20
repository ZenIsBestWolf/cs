import java.util.InputMismatchException;
import java.util.Scanner;

public class DignanZenLucasB23HW2 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Please enter the number of LucasNumbers (and ZenNumbers) to calculate: ");
        int n;
        try {
            n = scanner.nextInt();
            if (n < 0) {
                System.out.println("You cannot use negatives. Defaulting to 45.");
                n = 45;
            }
        } catch (InputMismatchException e) {
            System.out.println("Your input was invalid. Defaulting to 45.");
            n = 45;
        }
        long prevStartTime = System.nanoTime();
        long prevLucasNum = LucasNumber(0);
        long prevEndTime = System.nanoTime();
        int prevTotalTime = (int) (prevEndTime - prevStartTime);

        for (int i = 1; i < n; i++) {
            System.out.printf("The Lucas Number of %d is %d\n", i-1, prevLucasNum);
            System.out.printf("It took %d nano-seconds to calculate.\n", prevTotalTime);
            long startTime = System.nanoTime();
            int lucasNum = LucasNumber(i);
            long endTime = System.nanoTime();
            int totalTime = (int) (endTime - startTime);
            double calcRatio = (double) lucasNum / prevLucasNum;
            double timeRatio = (double) totalTime / prevTotalTime;
            prevLucasNum = lucasNum;
            prevTotalTime = (int) (endTime - startTime);
            System.out.printf("Calc Ratio: %f, Time Ratio: %f\n", calcRatio, timeRatio);
        }

        prevStartTime = System.nanoTime();
        long prevZenNum = ZenSequence(0);
        prevEndTime = System.nanoTime();
        prevTotalTime = (int) (prevEndTime - prevStartTime);
        for (int i = 1; i < n; i++) {
            System.out.printf("The Zen Number of %d is %d\n", i-1, prevZenNum);
            System.out.printf("It took %d nano-seconds to calculate.\n", prevTotalTime);
            long startTime = System.nanoTime();
            int zenNum = ZenSequence(i);
            long endTime = System.nanoTime();
            int totalTime = (int) (endTime - startTime);
            double calcRatio = (double) zenNum / prevLucasNum;
            double timeRatio = (double) totalTime / prevTotalTime;
            prevZenNum = zenNum;
            prevTotalTime = (int) (endTime - startTime);
            System.out.printf("Calc Ratio: %f, Time Ratio: %f\n", calcRatio, timeRatio);
        }
    }

    public static int LucasNumber(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("n cannot be less than 0");
        }
        if (n == 0) {
            return 1;
        }
        if (n == 1) {
            return 2;
        }
        return LucasNumber(n - 1) + LucasNumber(n - 2);
    }

    private static int ZenSequence(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("n cannot be less than 0");
        }
        if (n == 0) {
            return 0;
        }
        if (n == 1) {
            return 1;
        }
        return ZenSequence(n/15) + LucasNumber(n/2);
    }
}

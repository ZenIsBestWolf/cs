import stdlib.StdIn;
import stdlib.StdOut;

public class ClosestPair {
    // Prints the closest pair of integers in a, separated by a space.
    private static void closestPair(int[] a) {
        int index1 = -1;
        int index2 = -1;
        int dif = Integer.MAX_VALUE;
        for (int i = 0; i < a.length; i++) {
            for (int j = i + 1; j < a.length; j++) {
                if (Math.abs(a[i] - a[j]) < dif) {
                    index1 = a[i];
                    index2 = a[j];
                    dif = Math.abs(a[i] - a[j]);
                }
            }
        }
        StdOut.println(index1 + " " + index2);
    }

    // Entry point [DO NOT EDIT].
    public static void main(String[] args) {
        int[] a = StdIn.readAllInts();
        closestPair(a);
    }
}
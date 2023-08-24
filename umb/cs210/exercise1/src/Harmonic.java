import stdlib.StdOut;

public class Harmonic {
    // Entry point.
    public static void main(String[] args) {
        // Accept n (int) as command-line argument.
        int n = Integer.parseInt(args[0]);

        // Set total to the rational number 0.
        Rational total = new Rational(0, 1);

        // For each 1 <= i <= n, add the rational term 1 / i to total.
        for (int i = 1; i <= n; i++) {
            total = total.add(new Rational(1, i));
        }

        // Write total to standard output.
        StdOut.println(total);
    }
}
import stdlib.StdIn;
import stdlib.StdOut;

public class Count {
    // Entry point [DO NOT EDIT].
    public static void main(String[] args) {
        String s = args[0];
        int l = Integer.parseInt(args[1]);
        String[] a = StdIn.readAllStrings();
        StdOut.println("# of strings of length >= " + l + " = " + stringsOfLength(a, l));
        StdOut.println("frequency(" + s + ") = " + frequencyOf(a, s));
    }

    // Returns the number of strings of length >= l in a.
    private static int stringsOfLength(String[] a, int l) {
        int counter = 0;
        // increase counter as larger strings are found
        for (String s : a) {
            if (s.length() >= l) {
                counter++;
            }
        }
        return counter;
    }

    // Returns the frequency of the string s in a.
    private static int frequencyOf(String[] a, String s) {
        int counter = 0;
        // increase counter as matching values are found
        for (String value : a) {
            if (s.equals(value)) {
                counter++;
            }
        }
        return counter;
    }
}

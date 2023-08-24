import stdlib.In;
import stdlib.StdOut;

// Accepts filenames A and B as command-line arguments and writes to standard output the number of
// unique words in A, the number of unique words in B, and the number of words that occur both in
// A and B.
public class WordStats {
    // Entry point.
    public static void main(String[] args) {
        SetX<String> a = new SetX<>(new In(args[0]).readAllStrings());
        SetX<String> b = new SetX<>(new In(args[1]).readAllStrings());

        StdOut.println(a.size());
        StdOut.println(b.size());
        StdOut.println(a.intersection(b).size());
    }
}

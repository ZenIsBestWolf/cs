import java.util.Arrays;
import java.util.Comparator;

import stdlib.In;
import stdlib.StdIn;
import stdlib.StdOut;

public class Autocomplete {
    Term[] terms; // list of terms

    // Constructs an autocomplete data structure from an array of terms.
    public Autocomplete(Term[] terms) {
        if (terms == null) {
            throw new NullPointerException("terms is null");
        }
        this.terms = new Term[terms.length];
        for (int i = 0; i < terms.length; i++) {
            this.terms[i] = terms[i];
        }
        Arrays.sort(this.terms);
    }

    // Returns all terms that start with prefix, in descending order of their weights.
    public Term[] allMatches(String prefix) {
        if (prefix == null) {
            throw new NullPointerException("prefix is null");
        }
        Term term = new Term(prefix);
        Comparator<Term> prefixOrder = Term.byPrefixOrder(prefix.length());
        int first = BinarySearchDeluxe.firstIndexOf(terms, term, prefixOrder);
        int last = BinarySearchDeluxe.lastIndexOf(terms, term, prefixOrder);
        int count = first == -1 && last == -1 ? 0 : last - first + 1;
        Term[] n = new Term[count];
        if (count == 0) {
            return n;
        }
        for (int i = first; i <= last; i++) {
            n[i - first] = terms[i];
        }
        Comparator<Term> byReverseWeightOrder = Term.byReverseWeightOrder();
        Arrays.sort(n, byReverseWeightOrder);
        return n;
    }

    // Returns the number of terms that start with prefix.
    public int numberOfMatches(String prefix) {
        if (prefix == null) {
            throw new NullPointerException("prefix is null");
        }
        Term term = new Term(prefix);
        Comparator<Term> prefixOrder = Term.byPrefixOrder(prefix.length());
        int first = BinarySearchDeluxe.firstIndexOf(terms, term, prefixOrder);
        int last = BinarySearchDeluxe.lastIndexOf(terms, term, prefixOrder);
        return first == -1 && last == -1 ? 0 : last - first + 1;
    }

    // Unit tests the data type. [DO NOT EDIT]
    public static void main(String[] args) {
        String filename = args[0];
        int k = Integer.parseInt(args[1]);
        In in = new In(filename);
        int N = in.readInt();
        Term[] terms = new Term[N];
        for (int i = 0; i < N; i++) {
            long weight = in.readLong();
            in.readChar();
            String query = in.readLine();
            terms[i] = new Term(query.trim(), weight);
        }
        Autocomplete autocomplete = new Autocomplete(terms);
        StdOut.print("Enter a prefix (or ctrl-d to quit): ");
        while (StdIn.hasNextLine()) {
            String prefix = StdIn.readLine();
            Term[] results = autocomplete.allMatches(prefix);
            String msg = " matches for \"" + prefix + "\", in descending order by weight:";
            if (results.length == 0) {
                msg = "No matches";
            } else if (results.length > k) {
                msg = "First " + k + msg;
            } else {
                msg = "All" + msg;
            }
            StdOut.printf("%s\n", msg);
            for (int i = 0; i < Math.min(k, results.length); i++) {
                StdOut.println("  " + results[i]);
            }
            StdOut.print("Enter a prefix (or ctrl-d to quit): ");
        }
    }
}

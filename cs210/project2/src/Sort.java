import dsa.LinkedStack;

import stdlib.StdIn;
import stdlib.StdOut;

public class Sort {
    // Entry point.
    public static void main(String[] args) {
        LinkedDeque<String> d = new LinkedDeque<String>();
        // Iterate over every word provided to standard input and then sort them
        // by their position in the alphabet
        for (String w : StdIn.readAllStrings()) {
            if (d.size() == 0) { // Stick it at the front if the deque is empty
                d.addFirst(w);
            } else if (less(w, d.peekFirst())) {
                // If the word comes before the first word, stick it at the front
                d.addFirst(w);
            } else if (less(d.peekLast(), w)) {
                // If the word comes after the last word, stick it at the back
                d.addLast(w);
            } else {
                // Otherwise take out every word that comes after this word until
                // there is one that is less, then stick it in front of that word
                LinkedStack<String> s = new LinkedStack<String>();
                s.push(d.removeFirst());
                while (!d.isEmpty() && less(d.peekFirst(), w)) {
                    s.push(d.removeFirst());
                }
                d.addFirst(w);
                for (String i : s) {
                    d.addFirst(s.pop());
                }
            }
        }
        // your deque is now sorted! go ahead and print
        for (String word : d) {
            StdOut.println(word);
        }
    }

    // Returns true if v is less than w according to their lexicographic order, and false otherwise.
    private static boolean less(String v, String w) {
        return v.compareTo(w) < 0;
    }
}

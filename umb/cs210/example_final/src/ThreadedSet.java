import java.util.NoSuchElementException;

import dsa.SeparateChainingHashST;
import stdlib.StdOut;

// A data type to store a threaded set of strings, which maintains a set of strings (no
// duplicates) and the order in which the strings were inserted.
public class ThreadedSet {
    SeparateChainingHashST<String, String> set; // storage for strings
//    int n; // number of strings
    private String last;

    // Constructs an empty threaded set.
    public ThreadedSet() {
        set = new SeparateChainingHashST<>();
        last = "";
//        n = 0;
    }

    // Adds s to this set if it is not already in the set.
    public void add(String s) {
        if (set.contains(s)) {
            return;
        }
        set.put(s, last);
        last = s;
    }

    // Returns true if this set contains s, and false otherwise.
    public boolean contains(String s) {
        return set.contains(s);
    }

    // Returns the string that was added to this set immediately before s; returns null if s is the
    // first string added; and throws java.util.NoSuchElementException if s is not in this set.
    public String previousKey(String s) {
        if (!s.contains(s)) {
            throw new NoSuchElementException("s is not in this set");
        }
        String key = set.get(s);
        return key.equals("") ? null : key;
    }

    // Unit tests the data type [DO NOT EDIT].
    public static void main(String[] s) {
        ThreadedSet set = new ThreadedSet();
        StdOut.println("Adding aardvark, bear, cat, and bear to a threaded set...");
        set.add("aardvark");
        set.add("bear");
        set.add("cat");
        set.add("bear");
        StdOut.println("set.contains(bear)        = " + set.contains("bear"));
        StdOut.println("set.contains(tiger)       = " + set.contains("tiger"));
        StdOut.println("set.previousKey(cat)      = " + set.previousKey("cat"));
        StdOut.println("set.previousKey(bear)     = " + set.previousKey("bear"));
        StdOut.println("set.previousKey(aardvark) = " + set.previousKey("aardvark"));
    }
}
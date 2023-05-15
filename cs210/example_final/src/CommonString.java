import dsa.SeparateChainingHashST;

import java.util.Arrays;

import stdlib.StdOut;

public class CommonString {
    // Returns a string that appears both in a and b, or null if the arrays don't have any
    // strings in common.
    private static String commonString(String[] a, String[] b) {
        ThreadedSet set = new ThreadedSet();
        for (String s : a) {
            set.add(s);
        }
        for (String s : b) {
            if (set.contains(s)) {
                return s;
            }
        }
        return null;
    }

    // Entry point [DO NOT EDIT].
    public static void main(String[] args) {
        String a = "GCA TCA ACG ACT GTC AGC GTA ATG";
        String b = "GAT GCA CAG GCT TCG GTC CTA ATG";
        String c = "it was the best of times it was the worst of times";
        String[] aList = a.split("\\s+");
        String[] bList = b.split("\\s+");
        String[] cList = c.split("\\s+");
        StdOut.println("a                  = " + Arrays.toString(aList));
        StdOut.println("b                  = " + Arrays.toString(bList));
        StdOut.println("c                  = " + Arrays.toString(cList));
        StdOut.println("commonString(a, b) = " + commonString(aList, bList));
        StdOut.println("commonString(a, c) = " + commonString(aList, cList));
    }
}
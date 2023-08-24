import dsa.MinPQ;
import stdlib.StdOut;

public class Ramanujan2 {
    // Entry point.
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        if (n < 2) {
            return;
        }
        MinPQ pq = new MinPQ();
        for (int i = 1; i * i * i < n; i++) {
            pq.insert(new Pair(i, i+1));
        }
        Pair prev;
        Pair curr = ((Pair) pq.delMin());
        pq.insert(new Pair(curr.i, curr.j + 1));
        while (!pq.isEmpty()) {
            prev = curr;
            curr = ((Pair) pq.delMin());
            int k = prev.i;
            int l = prev.j;
            int i = curr.i;
            int j = curr.j;
            int sum = k*k*k + l*l*l;
            if (sum == i*i*i + j*j*j && sum <= n) {
                StdOut.println(sum +" = "+k+"^3 + "+l+"^3 = "+i+"^3 + "+j+"^3");
            }
            if (j*j*j < n) {
                pq.insert(new Pair(i, j+1));
            }
        }
    }

    // A data type that encapsulates a pair of numbers (i, j) and the sum of their cubes.
    private static class Pair implements Comparable<Pair> {
        private int i;          // first number in the pair
        private int j;          // second number in the pair
        private int sumOfCubes; // i^3 + j^3

        // Constructs a pair (i, j).
        public Pair(int i, int j) {
            this.i = i;
            this.j = j;
            sumOfCubes = i * i * i + j * j * j;
        }

        // Returns a comparison of pairs this and other based on their sum-of-cubes values.
        public int compareTo(Pair other) {
            return sumOfCubes - other.sumOfCubes;
        }
    }
}

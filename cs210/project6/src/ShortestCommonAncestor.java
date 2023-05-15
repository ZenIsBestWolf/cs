import dsa.BFSDiPaths;
import dsa.DiGraph;
import dsa.LinkedQueue;
import dsa.SeparateChainingHashST;
import stdlib.In;
import stdlib.StdIn;
import stdlib.StdOut;

public class ShortestCommonAncestor {
    DiGraph G; // rooted digraph

    // Constructs a ShortestCommonAncestor object given a rooted DAG.
    public ShortestCommonAncestor(DiGraph G) {
        if (G == null) {
            throw new NullPointerException("G is null");
        }
        this.G = G;
    }

    // Returns length of the shortest ancestral path between vertices v and w.
    public int length(int v, int w) {
        if (v < 0 || v >= G.V()) {
            throw new IndexOutOfBoundsException("v is invalid");
        }
        if (w < 0 || w >= G.V()) {
            throw new IndexOutOfBoundsException("w is invalid");
        }
        int ancestor = ancestor(v, w);
        return distFrom(w).get(ancestor) + distFrom(v).get(ancestor);
    }

    // Returns a shortest common ancestor of vertices v and w.
    public int ancestor(int v, int w) {
        if (v < 0 || v >= G.V()) {
            throw new IndexOutOfBoundsException("v is invalid");
        }
        if (w < 0 || w >= G.V()) {
            throw new IndexOutOfBoundsException("w is invalid");
        }
        SeparateChainingHashST<Integer, Integer> vdist = distFrom(v);
        SeparateChainingHashST<Integer, Integer> wdist = distFrom(w);

        // to compute this, enumerate the vertices in distFrom(v) to
        // find a vertex x that is also in distFrom(w)
        // and has the minimum value for distFrom(v)[x] + distFrom(w)[x]

        int min = -1;

        for (int key : vdist.keys()) {
            if (!wdist.contains(key)) {
                continue;
            }
            if (min == -1) {
                min = key;
                continue;
            }
            int minDist = vdist.get(min) + wdist.get(min);
            int dist = vdist.get(key) + wdist.get(key);
            min = dist < minDist ? key : min;
        }

        return min;
    }

    // Returns length of the shortest ancestral path of vertex subsets A and B.
    public int length(Iterable<Integer> A, Iterable<Integer> B) {
        if (A == null) {
            throw new NullPointerException("A is null");
        }
        if (B == null) {
            throw new NullPointerException("B is null");
        }
        if (!A.iterator().hasNext()) {
            throw new IllegalArgumentException("A is empty");
        }
        if (!B.iterator().hasNext()) {
            throw new IllegalArgumentException("B is empty");
        }
        int[] triad = triad(A, B);
        return length(triad[1], triad[2]);
    }

    // Returns a shortest common ancestor of vertex subsets A and B.
    public int ancestor(Iterable<Integer> A, Iterable<Integer> B) {
        if (A == null) {
            throw new NullPointerException("A is null");
        }
        if (B == null) {
            throw new NullPointerException("B is null");
        }
        if (!A.iterator().hasNext()) {
            throw new IllegalArgumentException("A is empty");
        }
        if (!B.iterator().hasNext()) {
            throw new IllegalArgumentException("B is empty");
        }
        return triad(A, B)[0];
    }

    // Returns a map of vertices reachable from v and their respective shortest distances from v.
    private SeparateChainingHashST<Integer, Integer> distFrom(int v) {
        BFSDiPaths paths = new BFSDiPaths(G, v);
        SeparateChainingHashST<Integer, Integer> result = new SeparateChainingHashST<>();
        for (int i = 0; i < G.V(); i++) {
            if (!paths.hasPathTo(i)) {
                continue;
            }
            result.put(i, ((int) paths.distTo(i)));
        }
        return result;
    }

    // Returns an array consisting of a shortest common ancestor a of vertex subsets A and B,
    // vertex v from A, and vertex w from B such that the path v-a-w is the shortest ancestral
    // path of A and B.
    private int[] triad(Iterable<Integer> A, Iterable<Integer> B) {
        LinkedQueue<int[]> triads = new LinkedQueue<>();
        for (int v : A) {
            for (int w : B) {
                int[] triad = new int[3];
                triad[0] = ancestor(v, w);
                triad[1] = v;
                triad[2] = w;
                triads.enqueue(triad);
            }
        }
        int[] min = triads.peek();
        for (int[] triad : triads) {
            int minDist = length(min[1], min[2]);
            int curDist = length(triad[1], triad[2]);
            min = minDist > curDist ? triad : min;
        }

        return min;
    }

    // Unit tests the data type. [DO NOT EDIT]
    public static void main(String[] args) {
        In in = new In(args[0]);
        DiGraph G = new DiGraph(in);
        in.close();
        ShortestCommonAncestor sca = new ShortestCommonAncestor(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length = sca.length(v, w);
            int ancestor = sca.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}

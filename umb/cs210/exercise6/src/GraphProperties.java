import dsa.BFSPaths;
import dsa.Graph;
import dsa.RedBlackBinarySearchTreeST;
import stdlib.In;
import stdlib.StdOut;

public class GraphProperties {
    private RedBlackBinarySearchTreeST<Integer, Integer> st; // degree -> frequency
    private double avgDegree;                                // average degree of the graph
    private double avgPathLength;                            // average path length of the graph
    private double clusteringCoefficient;                    // clustering coefficient of the graph

    // Computes graph properties for the undirected graph G.
    public GraphProperties(Graph G) {
        // degree distribution with st:
        // a function mapping each degree value in G to the number of vertices with that value.
        st = new RedBlackBinarySearchTreeST<>();
        int ticker = 0; // "global" counter for paths

        for (int i = 0; i < G.V(); i++) {
            // variables
            int degree = G.degree(i);
            BFSPaths paths = new BFSPaths(G, i);

            // average degree calculations
            if (st.get(degree) == null) {
                st.put(degree, 1);
            } else {
                st.put(degree, st.get(degree) + 1);
            }

            // avg path length calculations
            for (int j = 0; j < G.V(); j++) {
                if (j == i) {
                    continue;
                }
                avgPathLength += paths.distTo(j);
                ticker++;
            }

            // clustering coeffecient calculations
            int edges = 0;
            int counter = 0;
            for (int neighbor : G.adj(i)) {
                for (int kittycorner : G.adj(i)) {
                    edges += hasEdge(G, neighbor, kittycorner) ? 1 : 0;
                }
                counter++;
            }
            if (counter <= 1) {
                continue;
            }
            clusteringCoefficient += ((double) (edges / 2)) / ((counter * (counter - 1.0)) * 0.5);
        }

        clusteringCoefficient *= (1.0/G.V());
        avgPathLength /= ticker;
        avgDegree = (((double) G.E()) * 2.0) /((double) G.V());
    }

    // Returns the degree distribution of the graph (a symbol table mapping each degree value to
    // the number of vertices with that value).
    public RedBlackBinarySearchTreeST<Integer, Integer> degreeDistribution() {
        return st;
    }

    // Returns the average degree of the graph.
    public double averageDegree() {
        return avgDegree;
    }

    // Returns the average path length of the graph.
    public double averagePathLength() {
        return avgPathLength;
    }

    // Returns the global clustering coefficient of the graph.
    public double clusteringCoefficient() {
        return clusteringCoefficient;
    }

    // Returns true if G has an edge between vertices v and w, and false otherwise.
    private static boolean hasEdge(Graph G, int v, int w) {
        for (int u : G.adj(v)) {
            if (u == w) {
                return true;
            }
        }
        return false;
    }

    // Unit tests the data type. [DO NOT EDIT]
    public static void main(String[] args) {
        In in = new In(args[0]);
        Graph G = new Graph(in);
        GraphProperties gp = new GraphProperties(G);
        RedBlackBinarySearchTreeST<Integer, Integer> st = gp.degreeDistribution();
        StdOut.println("Degree distribution:");
        for (int degree : st.keys()) {
            StdOut.println("  " + degree + ": " + st.get(degree));
        }
        StdOut.printf("Average degree         = %7.3f\n", gp.averageDegree());
        StdOut.printf("Average path length    = %7.3f\n", gp.averagePathLength());
        StdOut.printf("Clustering coefficient = %7.3f\n", gp.clusteringCoefficient());
    }
}

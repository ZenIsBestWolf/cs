import dsa.WeightedQuickUnionUF;
import stdlib.In;
import stdlib.StdOut;

// An implementation of the Percolation API using the UF data structure.
public class UFPercolation implements Percolation {
    private final int n; // Size of percolation system, final makes this constant
    private boolean[][] open; // true = open, false = blocked, default: all blocked
    private int openSites; // Number of states
    private WeightedQuickUnionUF uf; // The Union Find implementation of the model

    // Constructs an n x n percolation system, with all sites blocked.
    public UFPercolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("Illegal n");
        }
        this.n = n;
        this.open = new boolean[n][n];
        this.openSites = 0;
        this.uf = new WeightedQuickUnionUF(n*n + 2);
    }

    // Opens site (i, j) if it is not already open.
    public void open(int i, int j) {
        if (i < 0 || i > n-1 || j < 0 || j > n-1) {
            throw new IndexOutOfBoundsException("Illegal i or j");
        }

        // If it's open, it's already run through all of these before
        // and it would simply waste compute to repeat it, so do nothing.
        // This also prevents double-counting open sites.
        if (open[i][j]) {
            return;
        }

        // Open the site and count it.
        open[i][j] = true;
        openSites++;

        // The following two statements check if the site is next to the sink
        // and/or drain, and connects it if applicable.
        if (i == 0) {
            uf.union(encode(i, j), 0);
        }

        // Backwash problem solution: Do not connect to sink if it already percolates.
        // Also, this is not an "else if" because when n = 1 both need to run.
        if (i == n-1 && !percolates()) {
            uf.union(encode(i, j), n*n+1);
        }

        // The following four check for any open sites that are next
        // to the current site, and connects to them if so
        if (!(i == 0) && open[i - 1][j]) {
            uf.union(encode(i, j), encode(i - 1, j));
        }
        if (!(i == n-1) && open[i + 1][j]) {
            uf.union(encode(i, j), encode(i + 1, j));
        }
        if (!(j == 0) && open[i][j - 1]) {
            uf.union(encode(i, j), encode(i, j - 1));
        }
        if (!(j == n-1) && open[i][j + 1]) {
            uf.union(encode(i, j), encode(i, j + 1));
        }
    }

    // Returns true if site (i, j) is open, and false otherwise.
    public boolean isOpen(int i, int j) {
        if (i < 0 || i > n-1 || j < 0 || j > n-1) {
            throw new IndexOutOfBoundsException("Illegal i or j");
        }

        return open[i][j];
    }

    // Returns true if site (i, j) is full, and false otherwise.
    public boolean isFull(int i, int j) {
        if (i < 0 || i > n-1 || j < 0 || j > n-1) {
            throw new IndexOutOfBoundsException("Illegal i or j");
        }
        // Checks if the site is open & connected to the source
        return (isOpen(i, j) && uf.connected(encode(i, j), 0));
    }

    // Returns the number of open sites.
    public int numberOfOpenSites() {
        return openSites;
    }

    // Returns true if this system percolates, and false otherwise.
    public boolean percolates() {
        // Checks if the source is connected to the sink
        return (uf.connected(0, n*n+1));
    }

    // Returns an integer ID (1...n) for site (i, j).
    private int encode(int i, int j) {
        // Converts a 2D array ID to 1D array ID
        // Allowing comparison from open[][] to uf
        return (n*i + j) + 1;
    }

    // Unit tests the data type. [DO NOT EDIT]
    public static void main(String[] args) {
        String filename = args[0];
        In in = new In(filename);
        int n = in.readInt();
        UFPercolation perc = new UFPercolation(n);
        while (!in.isEmpty()) {
            int i = in.readInt();
            int j = in.readInt();
            perc.open(i, j);
        }
        StdOut.printf("%d x %d system:\n", n, n);
        StdOut.printf("  Open sites = %d\n", perc.numberOfOpenSites());
        StdOut.printf("  Percolates = %b\n", perc.percolates());
        if (args.length == 3) {
            int i = Integer.parseInt(args[1]);
            int j = Integer.parseInt(args[2]);
            StdOut.printf("  isFull(%d, %d) = %b\n", i, j, perc.isFull(i, j));
        }
    }
}
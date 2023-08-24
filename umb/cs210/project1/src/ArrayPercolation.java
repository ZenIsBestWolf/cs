import stdlib.In;
import stdlib.StdOut;

// An implementation of the Percolation API using a 2D array.
public class ArrayPercolation implements Percolation {
    private final int n; // Size of percolation system, final makes this constant
    private boolean[][] open; // true = open, false = blocked, default: all blocked
    private int numberOfOpenSites; // Number of sites

    // Constructs an n x n percolation system, with all sites blocked.
    public ArrayPercolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("Illegal n");
        }

        // Initiate instance variables being the dimension, sitemap, and number of open sites.
        this.n = n;
        this.open = new boolean[n][n];
        this.numberOfOpenSites = 0;
    }

    // Opens site (i, j) if it is not already open.
    public void open(int i, int j) {
        if (i < 0 || i > n-1 || j < 0 || j > n-1) {
            throw new IndexOutOfBoundsException("Illegal i or j");
        }

        // ONLY open and increment if not already open
        // This prevents "phantom sites" being added to numberOfOpenSites
        // which would count 1 site twice or more times.
        if (!open[i][j]) {
            open[i][j] = true;
            numberOfOpenSites++;
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

        boolean[][] full = new boolean[n][n];

        // Fill every entry on the top row
        // and let that "water" flow down
        for (int k = 0; k < n; k++) {
            floodFill(full, 0, k);
        }

        // We're left with any available open sites
        // that linked to the top row full.
        // So now return if the provided site is full.
        return full[i][j];
    }

    // Returns the number of open sites.
    public int numberOfOpenSites() {
        return numberOfOpenSites;
    }

    // Returns true if this system percolates, and false otherwise.
    public boolean percolates() {
        for (int i = 0; i < n; i++) {
            // Check each entry on the last row if it is full
            // This works because isFull() starts filling from the top
            // So if it's full on the bottom, then "water" got from the
            // top to the bottom, percolating.
            if (isFull(n-1, i)) {
                return true;
            }
        }
        return false;
    }

    // Recursively flood fills full[][] using depth-first exploration, starting at (i, j).
    private void floodFill(boolean[][] full, int i, int j) {
        // If the args are invalid, or the site is full or blocked, do nothing
        if (i < 0 || j < 0 || i > n-1 || j > n-1 || full[i][j] || !open[i][j]) {
            return;
        }

        full[i][j] = true; // Fill here
        floodFill(full, i-1, j); // Fill west
        floodFill(full, i+1, j); // Fill east
        floodFill(full, i, j+1); // Fill south
        floodFill(full, i, j-1); // Fill north
    }

    // Unit tests the data type. [DO NOT EDIT]
    public static void main(String[] args) {
        String filename = args[0];
        In in = new In(filename);
        int n = in.readInt();
        ArrayPercolation perc = new ArrayPercolation(n);
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
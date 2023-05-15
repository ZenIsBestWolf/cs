import dsa.Inversions;
import dsa.LinkedQueue;
import stdlib.In;
import stdlib.StdOut;

// A data type to represent a board in the 8-puzzle game or its generalizations.
public class Board {
    int[][] tiles; // tiles in board
    int n; // board size
    int hamming; // distance to hamming goal
    int manhattan; // distance to manhattan goal
    int blankPos; // position of blank position

    // Constructs a board from an n x n array; tiles[i][j] is the tile at row i and column j, with 0
    // denoting the blank tile.
    public Board(int[][] tiles) {
        this.tiles = tiles;
        n = tiles.length;
        // set up the hamming and the manhattan goals
        hamming = 0;
        manhattan = 0;
        int val = 1;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (tiles[i][j] == 0) {
                    blankPos = n * i + j + 1;
                    val++;
                    continue;
                }
                hamming += tiles[i][j] == val ? 0 : 1;
                int iGoal = (tiles[i][j] - 1) / n;
                int jGoal = (tiles[i][j] - 1) % n;
                manhattan += Math.abs(i - iGoal) + Math.abs(j - jGoal);
                val++;
            }
        }
    }

    // Returns the size of this board.
    public int size() {
        return n;
    }

    // Returns the tile at row i and column j of this board.
    public int tileAt(int i, int j) {
        return tiles[i][j];
    }

    // Returns Hamming distance between this board and the goal board.
    public int hamming() {
        return hamming;
    }

    // Returns the Manhattan distance between this board and the goal board.
    public int manhattan() {
        return manhattan;
    }

    // Returns true if this board is the goal board, and false otherwise.
    public boolean isGoal() {
        return hamming == 0;
    }

    // Returns true if this board is solvable, and false otherwise.
    public boolean isSolvable() {
        int[] solvableArray = new int[n*n - 1];
        int pos = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (tiles[i][j] == 0) {
                    continue;
                }
                solvableArray[pos] = tiles[i][j];
                pos++;
            }
        }
        long invers = Inversions.count(solvableArray);
        return n % 2 == 0 ? (invers + ((blankPos - 1)/n)) % 2 == 1 : invers % 2 == 0;
    }

    // Returns an iterable object containing the neighboring boards of this board.
    public Iterable<Board> neighbors() {
        LinkedQueue<Board> q = new LinkedQueue<Board>();
        int iBlank = (blankPos - 1) / n;
        int jBlank = (blankPos - 1) % n;

        // valid south checking
        if (iBlank != n-1) {
            int[][] clone = cloneTiles();
            clone[iBlank][jBlank] = clone[iBlank + 1][jBlank];
            clone[iBlank + 1][jBlank] = 0;
            Board cloneBoard = new Board(clone);
            q.enqueue(cloneBoard);
        }

        // valid north checking
        if (iBlank != 0) {
            int[][] clone = cloneTiles();
            clone[iBlank][jBlank] = clone[iBlank - 1][jBlank];
            clone[iBlank - 1][jBlank] = 0;
            Board cloneBoard = new Board(clone);
            q.enqueue(cloneBoard);
        }

        // valid west checking
        if (jBlank != n-1) {
            int[][] clone = cloneTiles();
            clone[iBlank][jBlank] = clone[iBlank][jBlank + 1];
            clone[iBlank][jBlank + 1] = 0;
            Board cloneBoard = new Board(clone);
            q.enqueue(cloneBoard);
        }

        // valid east checking
        if (jBlank != 0) {
            int[][] clone = cloneTiles();
            clone[iBlank][jBlank] = clone[iBlank][jBlank - 1];
            clone[iBlank][jBlank - 1] = 0;
            Board cloneBoard = new Board(clone);
            q.enqueue(cloneBoard);
        }
        return q;
    }

    // Returns true if this board is the same as other, and false otherwise.
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        if (other == this) {
            return true;
        }
        if (other.getClass() != this.getClass()) {
            return false;
        }
        if (((Board) other).n != n) {
            return false;
        }
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (tiles[i][j] != ((Board) other).tiles[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    // Returns a string representation of this board.
    public String toString() {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                s.append(String.format("%2s", tiles[i][j] == 0 ? " " : tiles[i][j]));
                if (j < n - 1) {
                    s.append(" ");
                }
            }
            if (i < n - 1) {
                s.append("\n");
            }
        }
        return s.toString();
    }

    // Returns a defensive copy of tiles[][].
    private int[][] cloneTiles() {
        int[][] clone = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                clone[i][j] = tiles[i][j];
            }
        }
        return clone;
    }

    // Unit tests the data type. [DO NOT EDIT]
    public static void main(String[] args) {
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                tiles[i][j] = in.readInt();
            }
        }
        Board board = new Board(tiles);
        StdOut.printf("The board (%d-puzzle):\n%s\n", n, board);
        String f = "Hamming = %d, Manhattan = %d, Goal? %s, Solvable? %s\n";
        StdOut.printf(f, board.hamming(), board.manhattan(), board.isGoal(), board.isSolvable());
        StdOut.println("Neighboring boards:");
        for (Board neighbor : board.neighbors()) {
            StdOut.println(neighbor);
            StdOut.println("----------");
        }
    }
}

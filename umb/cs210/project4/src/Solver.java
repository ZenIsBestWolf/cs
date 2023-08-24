import dsa.LinkedStack;
import dsa.MinPQ;
import stdlib.In;
import stdlib.StdOut;

// A data type that implements the A* algorithm for solving the 8-puzzle and its generalizations.
public class Solver {
    int moves; // minimum number of moves needed to solve
    LinkedStack<Board> solution; // stack of boards of the (shortest) solution

    // Finds a solution to the initial board using the A* algorithm.
    public Solver(Board board) {
        if (board == null) {
            throw new NullPointerException("board is null");
        }
        if (!board.isSolvable()) {
            throw new IllegalArgumentException("board is unsolvable");
        }
        solution = new LinkedStack<Board>(); // for some reason not in the instructions to init this
        MinPQ<SearchNode> pq = new MinPQ<SearchNode>();
        pq.insert(new SearchNode(board, 0, null));
        while (!pq.isEmpty()) {
            SearchNode node = pq.delMin();
            if (node.board.isGoal()) {
                moves = node.moves;
                SearchNode currNode = node;
                while (!currNode.board.equals(board)) {
                    solution.push(currNode.board);
                    currNode = currNode.previous;
                }
                break;
            } else {
                for (Board b : node.board.neighbors()) {
                    if (b.equals(board)) {
                        continue;
                    }
                    // CRITICAL optimization mentioned in the PDF
                    if (node.previous != null && b.equals(node.previous.board)) {
                        continue;
                    }
                    pq.insert(new SearchNode(b, node.moves+1, node));
                }
            }
        }
    }

    // Returns the minimum number of moves needed to solve the initial board.
    public int moves() {
        return moves;
    }

    // Returns a sequence of boards in a shortest solution of the initial board.
    public Iterable<Board> solution() {
        return solution;
    }

    private class SearchNode implements Comparable<SearchNode> {
        Board board; // the current board that is being searched
        int moves; // number of moves made thusfar
        SearchNode previous; // the previous searchnode; will be null (like a reversed linked list)

        // Constructs a new search node.
        public SearchNode(Board board, int moves, SearchNode previous) {
            this.board = board;
            this.moves = moves;
            this.previous = previous;
        }

        // Returns a comparison of this node and other based on the following sum:
        //   Manhattan distance of the board in the node + the # of moves to the node
        public int compareTo(SearchNode other) {
            int thisSum = board.manhattan + moves;
            int otherSum = other.board.manhattan + other.moves;
            return thisSum - otherSum;
        }
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
        Board initial = new Board(tiles);
        if (initial.isSolvable()) {
            Solver solver = new Solver(initial);
            StdOut.printf("Solution (%d moves):\n", solver.moves());
            StdOut.println(initial);
            StdOut.println("----------");
            for (Board board : solver.solution()) {
                StdOut.println(board);
                StdOut.println("----------");
            }
        } else {
            StdOut.println("Unsolvable puzzle");
        }
    }
}

import java.util.Arrays;

public class Queens {
    public static boolean isLegalMove(int[] board, int n) {
        boolean[][] queenBoard = new boolean[n][n];
        boolean[][] threatMap = new boolean[n][n];

        // build board of queen locations
        for (int i = 0; i < board.length; i++) {
            if (board[i] == 0) continue;
            queenBoard[i][board[i] - 1] = true;
        }

        // build board of threat mappings
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (!queenBoard[i][j]) continue;
                for (int k = 0; k < n; k++) {
                    if (k != j) threatMap[i][k] = true;
                    if (k != i) threatMap[k][j] = true;
                }
                int diagonal = 1;
                while (i - diagonal >= 0 && j - diagonal >= 0) {
                    threatMap[i - diagonal][j - diagonal] = true;
                    diagonal++;
                }
                diagonal = 1;
                while (i + diagonal < n && j + diagonal < n) {
                    threatMap[i + diagonal][j + diagonal] = true;
                    diagonal++;
                }
                diagonal = 1;
                while (i + diagonal < n && j - diagonal >= 0) {
                    threatMap[i + diagonal][j - diagonal] = true;
                    diagonal++;
                }
                diagonal = 1;
                while (i - diagonal >= 0 && j + diagonal < n) {
                    threatMap[i - diagonal][j + diagonal] = true;
                    diagonal++;
                }
            }
        }

//        System.out.println(stringifyThreatMap(threatMap));

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (queenBoard[i][j] && threatMap[i][j]) return false;
            }
        }
        return true;
    }

    private static String stringifyThreatMap(boolean[][] threatMap) {
        StringBuilder output = new StringBuilder();
        for (boolean[] row : threatMap) {
            output.append("[");
            for (boolean tile : row) {
                output.append(String.format("%s, ", tile ? "X" : "O"));
            }
            output.delete(output.length() - 2, output.length());
            output.append("]\n");
        }
        return output.toString();
    }

    public static int[] nextLegalMove(int[] board, int n) {
        int[] next = board.clone();

        for (int i = n - 1; i >= 0; i--) {
            if (next[i] == 0 && i != 0 && next[i - 1] == 0) continue;
            while (next[i] <= n - 1) {
                next[i] += 1;
                if (isLegalMove(next, n)) return next;
            }
            next[i] = 0;
        }
        throw new IllegalArgumentException("No legal move can be calculated");
    }

    public static void main(String[] args) {
        int n = 8;

        System.out.println("Legal move testing:");
        System.out.printf("Test 1 %s.\n", isLegalMove(new int[] {1, 6, 8, 3, 7, 4, 2, 5}, n) ? "passed" : "failed");
        System.out.printf("Test 2 %s.\n", isLegalMove(new int[] {1, 6, 8, 3, 7, 0, 0, 0}, n) ? "passed" : "failed");
        System.out.printf("Test 3 %s.\n", isLegalMove(new int[] {1, 6, 8, 3, 5, 0, 0, 0}, n) ? "failed" : "passed");
        // Test 4 arose from a faulty diagonal check that I didn't notice until testing nextLegalMove.
        System.out.printf("Test 4 %s.\n", isLegalMove(new int[] {1, 6, 8, 3, 7, 2, 0, 0}, n) ? "failed" : "passed");

        int[] nextMove1 = {1, 6, 8, 5, 0, 0, 0, 0};
        int[] nextMove2 = {1, 6, 8, 3, 7, 4, 0, 0};
        int[] nextMove3 = {1, 6, 8, 3, 7, 0, 0, 0};

        System.out.println("Next move testing:");
        System.out.printf("Test 1 %s.\n", Arrays.equals(nextLegalMove(new int[] {1, 6, 8, 3, 7, 4, 2, 5}, n), nextMove1) ? "passed" : "failed");
        System.out.printf("Test 2 %s.\n", Arrays.equals(nextLegalMove(new int[] {1, 6, 8, 3, 7, 0, 0, 0}, n), nextMove2) ? "passed" : "failed");
        System.out.printf("Test 3 %s.\n", Arrays.equals(nextLegalMove(new int[] {1, 6, 8, 3, 5, 0, 0, 0}, n), nextMove3) ? "passed" : "failed");
    }
}

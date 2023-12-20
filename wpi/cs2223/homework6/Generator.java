import java.util.Arrays;

public class Generator {
    public static void main(String[] args) {
        int maximum = 36;
        /* n=21 is the maximum I can calculate with a reasonable amount of time
        * on my M1 Pro MacBook. Your mileage may vary, so adjust as you need.
        */

        for (int n = 4; n <= maximum; n++) {
            int starter = 1;
            int solution = 1;
            int[] previousBoard = new int[n];
            while (starter <= n) {
                int[] board = new int[n];
                board[0] = starter;
                try {
                    while (board[n - 1] == 0) {
                        board = Queens.nextLegalMove(board, n);
                    }
                    if (Arrays.equals(board, previousBoard)) throw new IllegalArgumentException("Same board");
                    System.out.printf("n=%d: %s\n", n, Arrays.toString(board));
                    break;
                } catch (IllegalArgumentException ignored) {}
                starter++;
                previousBoard = board;
            }
        }
    }
}

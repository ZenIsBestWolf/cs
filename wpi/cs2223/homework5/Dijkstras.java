import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.io.File;

public class Dijkstras {
    public static void main(String[] args) throws FileNotFoundException {
        Scanner fileScanner = new Scanner(new File("./DijkstrasAlgorithmDataB23.txt"));
        int n = fileScanner.nextInt();
        int[][] matrix = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                matrix[i][j] = fileScanner.nextInt();
            }
        }
        fileScanner.close();

        Scanner inputScanner = new Scanner(System.in);
        int start = -1;
        while (start < 0 || start > n) {
            System.out.printf("Please choose a starting node [0-%d]: ", n - 1);
            try {
                start = inputScanner.nextInt();
                if (start < 0 || start > n) throw new InputMismatchException();
            } catch (InputMismatchException e) {
                System.out.println("Invalid input given. Try again.");
                inputScanner.next();
            }
        }

        int end = -1;
        while (end < 0 || end > n) {
            System.out.printf("Please choose a ending node [0-%d]: ", n - 1);
            try {
                end = inputScanner.nextInt();
                if (end < 0 || end > n) throw new InputMismatchException();
            } catch (InputMismatchException e) {
                System.out.println("Invalid input given. Try again.");
                inputScanner.next();
            }
        }

        Integer[] dijkstas = calculate(matrix, start, end);
        System.out.print("The path is as follows: ");
        for (int i = dijkstas.length - 1; i > 0; i--) {
            System.out.printf("%c -> ", convert(dijkstas[i]));
        }
        System.out.printf("%c. That is ", convert(dijkstas[0]));

        int length = 0;
        for (int i = 0; i < dijkstas.length - 1; i++) {
            length += matrix[dijkstas[i]][dijkstas[i+1]];
        }

        System.out.printf("%d long.\n", length);
    }

    public static Integer[] calculate(int[][] matrix, int start, int end) {
        int n = matrix.length;
        int[] path = new int[n];
        int[] dist = new int[n] ;
        boolean[] visited = new boolean[n];

        Arrays.fill(dist, Integer.MAX_VALUE);
        dist[start] = 0;
        path[start] = -1;

        for (int i = 0; i < n; i++) {
            int min = dist[0];
            int tile = 0;
            for (int j = 1; j < n; j++) {
                if (visited[j] || dist[j] > min) continue;
                min = dist[j];
                tile = j;
            }
            visited[tile] = true;

            for (int j = 0; j < n; j++) {
                if (visited[j]) continue;
                if (matrix[tile][j] == 0) continue;
                if (dist[tile] == Integer.MAX_VALUE) continue;
                if (dist[tile] + matrix[tile][j] >= dist[j]) continue;

                dist[j] += matrix[tile][j];
                path[j] = tile;
            }
        }


        ArrayList<Integer> cleanPath = new ArrayList<>();
        int current = end;
        while (current != -1){
            cleanPath.add(current);
            current = path[current];
        }

        return cleanPath.toArray(new Integer[0]);
    }
    private static char convert(int i) {
        if (i < 0 || i > 9) throw new IllegalArgumentException("i must be between 0 and 9");
        return switch (i) {
            case 0 -> 'A';
            case 1 -> 'J';
            case 2 -> 'M';
            case 3 -> 'R';
            case 4 -> 'K';
            case 5 -> 'S';
            case 6 -> 'I';
            case 7 -> 'N';
            case 8 -> 'T';
            case 9 -> 'D';
            default -> ' ';
        };
    }
}

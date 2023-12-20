import java.util.stream.IntStream;

public class Hobbit {
    public static void main(String[] args) {
        int[][] lonelyMountain = new int[8][8];
        lonelyMountain[7] = new int[] {96, 33, 44, 98, 75, 68, 99, 84};
        lonelyMountain[6] = new int[] {10, 41, 1, 86, 46, 24, 53, 93};
        lonelyMountain[5] = new int[] {83, 97, 94, 27, 65, 51, 30, 7};
        lonelyMountain[4] = new int[] {56, 70, 47, 64, 22, 88, 67, 12};
        lonelyMountain[3] = new int[] {91, 11, 77, 48, 13, 71, 92, 15};
        lonelyMountain[2] = new int[] {32, 59, 17, 25, 31, 4, 16, 63};
        lonelyMountain[1] = new int[] {79, 5, 14, 23, 78, 37, 40, 74};
        lonelyMountain[0] = new int[] {35, 89, 52, 66, 82, 20, 95, 21};

        int[] values = pathFinder(lonelyMountain);
        System.out.printf("Total: %d\n", values[values.length - 1]);
        System.out.printf("Final Vault: %d\n", values[0] + 1);
        StringBuilder path = new StringBuilder();
        for (int i = values.length - 2; i >= 0; i--) {
            path.append(String.format("%d -> ", values[i] + 1));
        }

        System.out.printf("Path: %s\n", path.substring(0, path.length() - 4));
    }

    /* D&D is better */
    private static int[] pathFinder(int[][] matrix) {
        int n = matrix.length;
        int[] path = new int[n + 1];

        for (int i = n - 2; i >= 0; i--) {
            for (int j = 0; j < matrix[0].length; j++) {
                int[] maxPositions = generateMaxPositions(matrix, i, j);
                int max = maxPositions[getMaxIndex(maxPositions)];
                matrix[i][j] += max;
            }
        }

        int vaultIndex = getMaxIndex(matrix[0]);
        path[0] = vaultIndex; // vault number
        path[path.length - 1] = matrix[0][vaultIndex]; // total gemstones

        /* fill path */
        for (int i = 0; i < matrix.length - 1; i++) {
            int[] posMaxes = generateMaxPositions(matrix, i, vaultIndex);
            vaultIndex += getMaxIndex(posMaxes) - 1;
            path[i + 1] = vaultIndex;
        }
        return path;
    }

    private static int[] generateMaxPositions(int[][] matrix, int row, int col) {
        int[] positions = new int[3];
        for (int i = 0; i < 3; i++) {
            int index = col + (i - 1);
            if (index >= 0 && index < matrix[0].length) {
                positions[i] = matrix[row + 1][index];
            }
        }
        return positions;
    }

    /* why is this not just a native java thing */
    private static int getMaxIndex(int[] arr) {
        int max = 0;
        for (int i = 1; i < arr.length; i++) {
            max = arr[i] > arr[max] ? i : max;
        }
        return max;
    }
}

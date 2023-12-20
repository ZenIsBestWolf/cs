import java.util.Arrays;

public class Matrix {
    public static void solveViaRREF(float[][] matrix) {
        int n = matrix.length;
        for (int i = 0; i < n; i++) {
            int pivotRow = i;
            for (int j = i + 1; j < n; j++) {
                if (Math.abs(matrix[j][i]) > Math.abs(matrix[pivotRow][i])) {
                    pivotRow = j;
                }
            }

            // conditional swap
            if (pivotRow != i) {
                float[] tmp = matrix[i];
                matrix[i] = matrix[pivotRow];
                matrix[pivotRow] = tmp;
            }

            float marker = matrix[i][i];
            if (marker == 0.0) throw new IllegalStateException("Matrix does not have unique solution.");
            if (marker != 1.0) {
                for (int j = i; j <= n; j++) {
                    matrix[i][j] /= marker;
                }
            }

            for (int j = 0; j < n; j++) {
                if (i == j || matrix[j][i] == 0.0) continue;
                float scale = matrix[j][i];
                for (int k = i; k <= n; k++) {
                    matrix[j][k] -= matrix[i][k] * scale;
                }
            }
        }
//        float[] solutionVector = new float[n];
//        for (int i = 0; i < n; i++) {
//            solutionVector[i] = matrix[n - 1][i];
//        }
//        return solutionVector;
        System.out.println("=====================");
        for (int i = 0; i < n; i++) {
            System.out.printf("%d ", Math.round(matrix[i][n]));
        }
        System.out.println();
    }
    public static void solveViaRREF(float[][] matrix, float[] vector) {
        int n = matrix.length;
        float[][] augmentedMatrix = new float[n][n + 1];
        for (int i = 0; i < n; i++) {
            System.arraycopy(matrix[i], 0, augmentedMatrix[i], 0, n);
        }
        for (int i = 0; i < n; i++) {
            augmentedMatrix[i][n] = vector[i];
        }
        solveViaRREF(augmentedMatrix);
    }

    public static void main(String[] args) {
//        float[][] simpleUniqueMatrix = new float[3][3];
//        simpleUniqueMatrix[0] = new float[] {1, 1, 1};
//        simpleUniqueMatrix[1] = new float[] {1, 1, 2};
//        simpleUniqueMatrix[2] = new float[] {1, 2, 3};
//
//        float[] simpleUniqueVector = {6, 9, 14};
//
//        solveViaRREF(simpleUniqueMatrix, simpleUniqueVector);
//
//        float[][] simpleInfiniteMatrix = new float[3][3];
//        simpleInfiniteMatrix[0] = new float[] {1, 1, 1};
//        simpleInfiniteMatrix[1] = new float[] {1, 1, 2};
//        simpleInfiniteMatrix[2] = new float[] {2, 2, 3};
//
//        float[] simpleInfiniteVector = {6, 9, 15};
//
//        solveViaRREF(simpleInfiniteMatrix, simpleInfiniteVector);
//
        float[][] complexUniqueMatrix = new float[9][9];
        complexUniqueMatrix[0] = new float[] {1, 1, 1, 1, 1, 1, 1, 1, 1};
        complexUniqueMatrix[1] = new float[] {1, 1, 1, 1, 1, -1, -1, -1, -1};
        complexUniqueMatrix[2] = new float[] {1, -1, 1, -1, 1, -1, 1, -1, 1};
        complexUniqueMatrix[3] = new float[] {1, 1, 0, 0, 0, 0, 0, 0, 0};
        complexUniqueMatrix[4] = new float[] {0, 0, 1, 1, 0, 0, 0, 0, 0};
        complexUniqueMatrix[5] = new float[] {0, 0, 0, 0, 1, 1, 0, 0, 0};
        complexUniqueMatrix[6] = new float[] {0, 0, 0, 0, 0, 0, 0, 1, 1};
        complexUniqueMatrix[7] = new float[] {9, -8, 7, -6, 5, -4, 3, -2, 1};
        complexUniqueMatrix[8] = new float[] {1, 1, -1, 1, 1, -1, 1, 1, -1};

        float[] complexUniqueVector = {122, -88, 32, 3, 7, 18, 76, 41, 0};

        solveViaRREF(complexUniqueMatrix, complexUniqueVector);
    }
}

import java.util.*;

public class Inversions {
    private final int[] arr;

    public Inversions(int[] arr) {
        this.arr = arr;
    }

    public int easyInversionCount() {
        /**
         * Write a program with a naive O(n2) [sorting] algorithm
         * that counts the number of inversions in such an array A.
         */
        int ticker = 0;
        for (int i = 0; i < arr.length; i++) {
            for (int j = i; j < arr.length; j++) {
                if (arr[i] <= arr[j]) continue;
                ticker++;
            }
        }
        return ticker;
    }

    public int fastInversionCount() {
        /**
         * Write a program with a naive O(nlogn) [sorting] algorithm
         * that counts the number of inversions in such an array A.
         */
        return sort(arr, 0, arr.length - 1);
    }

    private static int sort(int[] arr, int left, int right) {
        int count = 0;
        if (left < right) {
            int mid = (left + right) / 2;

            count += sort(arr, left, mid);
            count += sort(arr, mid + 1, right);
            count += merge(arr, left, mid, right);
        }

        return count;
    }

    private static int merge(int[] arr, int left, int mid, int right) {
        int[] leftArr = Arrays.copyOfRange(arr, left, mid + 1);
        int[] rightArr = Arrays.copyOfRange(arr, mid + 1, right + 1);

        int i = 0;
        int j = 0;
        int k = left;
        int swaps = 0;

        while (i < leftArr.length && j < rightArr.length) {
            if (leftArr[i] <= rightArr[j]) {
                arr[k++] = leftArr[i++];
            } else {
                arr[k++] = rightArr[j++];
                swaps += (mid + 1) - (left + i);
            }
        }

        while (i < leftArr.length) {
            arr[k++] = leftArr[i++];
        }

        while (j < rightArr.length) {
            arr[k++] = rightArr[j++];
        }

        return swaps;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the amount of numbers for the list (8 is default): ");
        String input = scanner.nextLine();
        int size = 8;
        try {
            size = Integer.parseInt(input);
            if (size == 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            System.out.println("Default value of 8 is being used.");
        }
        int[] theArray = new int[size];
        for (int i = 0; i < size; i++) {
            System.out.printf("Enter the number for index %d (default is %d): ", i, size - i);
            input = scanner.nextLine();
            try {
                theArray[i] = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                theArray[i] = size - i;
                System.out.println("Default was used.");
            }
        }
        System.out.printf("You entered: %s\n", Arrays.toString(theArray));
        Inversions test = new Inversions(theArray);
        int easyConversions = test.easyInversionCount();
        System.out.println(easyConversions);
        int fastConversions = test.fastInversionCount();
        System.out.println(fastConversions);
    }
}

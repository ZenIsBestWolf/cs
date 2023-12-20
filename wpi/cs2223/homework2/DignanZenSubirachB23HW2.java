import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DignanZenSubirachB23HW2 {
    public static final int[] MAGICSQUARE = {15, 14, 14, 13, 11, 10, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1};
    public static final int SUM = 33;

    public static void main(String[] args) {
        HashMap<Integer, Integer> validNSums = new HashMap<>();
        HashMap<Integer, Integer> allNSums = new HashMap<>();
        List<List<Integer>> subsets = getSubsets();

        for (List<Integer> subset : subsets) {
            int localSum = subset.stream().mapToInt(x -> x).sum();
            int size = subset.size();
            if (localSum == SUM) validNSums.put(size, validNSums.getOrDefault(size, 0) + 1);
            allNSums.put(localSum, allNSums.getOrDefault(localSum, 0) + 1);
        }

        int totalSum = validNSums.values().stream().mapToInt(x -> x).sum();

        int commonSum = 0;
        int commonSumVal = Integer.MIN_VALUE;
        for (int sum : allNSums.keySet()) {
            int localSumValue = allNSums.get(sum);
            if (localSumValue > commonSumVal) {
                commonSumVal = localSumValue;
                commonSum = sum;
            }
        }

        System.out.printf("The number of four sums is %d.\n", validNSums.get(4));
        System.out.printf("The number of n sums is %d.\n", totalSum);
        System.out.printf("The number of possible combos is %d.\n", subsets.size());
        System.out.printf("The most common sum was %d.\n", commonSum);
    }

    private static List<List<Integer>> getSubsets() {
        List<List<Integer>> subsets = new ArrayList<>();
        subsets.add(new ArrayList<>());
        for (int elem : MAGICSQUARE) {
            int len = subsets.size();
            for (int i = 0; i < len; i++) {
                List<Integer> subset = new ArrayList<>(subsets.get(i));
                subset.add(elem);
                subsets.add(subset);
            }
        }
        return subsets;
    }
}

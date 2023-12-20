import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Hashing {
    public static void main(String[] args) throws FileNotFoundException {
        String path = "Moby-Dick-Chapter-1-groomed.txt";
        File file = new File(path);
        ArrayList<String> words = new ArrayList<>();
        Scanner scanner = new Scanner(file);

        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            String[] lineWords = line.split("\\s");
            for (String word : lineWords) {
                word = word.replaceAll("[^a-zA-Z'-]", "");
                if (words.contains(word) || word.isEmpty()) continue;
                words.add(word);
            }
        }

        MyHashTable table = new MyHashTable(words, 997);

        int matches = 0;
        int[] distanceMatch = new int[997];

        System.out.println("Address, Word, Hash\n");
        for (int i = 0; i < 997; i++) {
            String word = table.lookup(i);
            int hash = word == null ? -1 : MyHashTable.hashWord(word, 997);
            System.out.printf("%d, %s, %d\n", i, word, hash);
            if (hash == -1) continue;
            int distance = Math.abs(i - (hash % 997));
            distanceMatch[i] = distance;
            if (distance == 0) matches++;

        }
        int occuppied = table.getOccuppied();
        System.out.printf("\nThere are %d occurrences in the hash table, with %d empty.\n", occuppied, 997 - occuppied);
        System.out.printf("The resulting loadfactor is %f.\n", (float) occuppied / 997.0);
        System.out.printf("There are %d matches, where the hash value did not drift.\n", matches);
        int largestDistanceIndex = 0;
        int largestDistance = distanceMatch[0];
        for (int i = 1; i < distanceMatch.length; i++) {
            if (largestDistance >= distanceMatch[i]) continue;
            largestDistance = distanceMatch[i];
            largestDistanceIndex = i;
        }
        System.out.printf("The furthest distance is %d which is %d away from its address.", largestDistanceIndex, largestDistance);
    }
}

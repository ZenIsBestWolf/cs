import java.util.ArrayList;
import java.util.HashMap;

public class MyHashTable {
    private final static int C = 123;
    private int occuppied;
    private HashMap<String, Integer> offsets;
    private String[] words;

    public MyHashTable(ArrayList<String> words, int size) {
        this.words = new String[size];
        this.offsets = new HashMap<>();
        this.occuppied = 0;
        for (String word : words) {
            int offset = 0;
            int index = hashWord(word, size);
            while (this.words[(index + offset) % size] != null) {
                offset++;
            }
            this.offsets.put(word, offset);
            this.words[(index + offset) % size] = word;
            occuppied++;
        }
    }

    public int getOffset(String word) {
        return offsets.getOrDefault(word, -1);
    }

    public String lookup(int index) {
        return words[index];
    }

    public int getOccuppied() {
        return occuppied;
    }

    public static int hashWord(String word, int size) {
        int hash = 0;
        for (char character : word.toCharArray()) {
            if (character < 39 || character > 122) throw new IllegalArgumentException("Invalid char attempted to be hashed: " + character);
            hash = (hash * C + character) % size;
        }
        return hash;
    }
}

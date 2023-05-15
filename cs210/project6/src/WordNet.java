import dsa.DiGraph;
import dsa.SeparateChainingHashST;
import dsa.Set;
import stdlib.In;
import stdlib.StdOut;

public class WordNet {
    SeparateChainingHashST<String, Set<Integer>> st; // ask about replacing this with SCHST
    SeparateChainingHashST<Integer, String> rst; // synset ID to synset string
    ShortestCommonAncestor sca; // shortest common ancestor instance

    // Constructs a WordNet object given the names of the input (synset and hypernym) files.
    public WordNet(String synsets, String hypernyms) {
        if (synsets == null) {
            throw new NullPointerException("synsets is null");
        }
        if (hypernyms == null) {
            throw new NullPointerException("hypernyms is null");
        }
        st = new SeparateChainingHashST<>();
        rst = new SeparateChainingHashST<>();
        In synsetsIn = new In(synsets);
        String[] synsetLines = synsetsIn.readAllLines();

        for (String line : synsetLines) {
            String[] parsedLine = line.split(",", 3);
            String[] parsedNouns = parsedLine[1].split(" ");
            rst.put(Integer.parseInt(parsedLine[0]), parsedLine[1]);
            for (String noun : parsedNouns) {
                if (st.contains(noun)) {
                    st.get(noun).add(Integer.parseInt(parsedLine[0]));
                } else {
                    Set<Integer> set = new Set<>();
                    set.add(Integer.parseInt(parsedLine[0]));
                    st.put(noun, set);
                }
            }
        }

        int lineCount = synsetLines.length;
        In hypernymsIn = new In(hypernyms);
        DiGraph G = new DiGraph(lineCount);
        for (String line : hypernymsIn.readAllLines()) {
            String[] parsedLine = line.split(",");
            for (int i = 1; i < parsedLine.length; i++) {
                G.addEdge(Integer.parseInt(parsedLine[0]), Integer.parseInt(parsedLine[i]));
            }
        }
        sca = new ShortestCommonAncestor(G);
    }

    // Returns all WordNet nouns.
    public Iterable<String> nouns() {
        return st.keys();
    }

    // Returns true if the given word is a WordNet noun, and false otherwise.
    public boolean isNoun(String word) {
        if (word == null) {
            throw new NullPointerException("word is null");
        }
        return st.contains(word);
    }

    // Returns a synset that is a shortest common ancestor of noun1 and noun2.
    public String sca(String noun1, String noun2) {
        if (noun1 == null) {
            throw new NullPointerException("noun1 is null");
        }
        if (noun2 == null) {
            throw new NullPointerException("noun2 is null");
        }
        if (!isNoun(noun1)) {
            throw new IllegalArgumentException("noun1 is not a noun");
        }
        if (!isNoun(noun2)) {
            throw new IllegalArgumentException("noun2 is not a noun");
        }
        return rst.get(sca.ancestor(st.get(noun1), st.get(noun2)));
    }

    // Returns the length of the shortest ancestral path between noun1 and noun2.
    public int distance(String noun1, String noun2) {
        if (noun1 == null) {
            throw new NullPointerException("noun1 is null");
        }
        if (noun2 == null) {
            throw new NullPointerException("noun2 is null");
        }
        if (!isNoun(noun1)) {
            throw new IllegalArgumentException("noun1 is not a noun");
        }
        if (!isNoun(noun2)) {
            throw new IllegalArgumentException("noun2 is not a noun");
        }
        return sca.length(st.get(noun1), st.get(noun2));
    }

    // Unit tests the data type. [DO NOT EDIT]
    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        String word1 = args[2];
        String word2 = args[3];
        int nouns = 0;
        for (String noun : wordnet.nouns()) {
            nouns++;
        }
        StdOut.printf("# of nouns = %d\n", nouns);
        StdOut.printf("isNoun(%s)? %s\n", word1, wordnet.isNoun(word1));
        StdOut.printf("isNoun(%s)? %s\n", word2, wordnet.isNoun(word2));
        StdOut.printf("isNoun(%s %s)? %s\n", word1, word2, wordnet.isNoun(word1 + " " + word2));
        StdOut.printf("sca(%s, %s) = %s\n", word1, word2, wordnet.sca(word1, word2));
        StdOut.printf("distance(%s, %s) = %s\n", word1, word2, wordnet.distance(word1, word2));
    }
}

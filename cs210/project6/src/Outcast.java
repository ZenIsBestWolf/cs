import stdlib.StdIn;
import stdlib.StdOut;

public class Outcast {
    WordNet wordnet; // wordnet

    // Constructs an Outcast object given the WordNet semantic lexicon.
    public Outcast(WordNet wordnet) {
        this.wordnet = wordnet;
    }

    // Returns the outcast noun from nouns.
    public String outcast(String[] nouns) {
        String maxNoun = null;
        int maxDistance = -1;
        for (String noun : nouns) {
            int runningTotal = 0;
            for (String subnoun : nouns) {
                runningTotal += wordnet.distance(noun, subnoun);
            }
            if (runningTotal > maxDistance) {
                maxDistance = runningTotal;
                maxNoun = noun;
            }
        }
        return maxNoun;
    }

    // Unit tests the data type. [DO NOT EDIT]
    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        String[] nouns = StdIn.readAllStrings();
        String outcastNoun = outcast.outcast(nouns);
        for (String noun : nouns) {
            StdOut.print(noun.equals(outcastNoun) ? "*" + noun + "* " : noun + " ");
        }
        StdOut.println();
    }
}

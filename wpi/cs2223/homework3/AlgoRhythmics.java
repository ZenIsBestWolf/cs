import java.util.ArrayList;
import java.util.List;

public class AlgoRhythmics {
    public final ArrayList<String> band;
    private final ArrayList<String> grayCodes;
    public AlgoRhythmics(String[] band) {
        this.band = new ArrayList<>(List.of(band));
        this.grayCodes = getNGray(band.length);
    }

    private static ArrayList<String> getNGray(int n) {
        if (n <= 1) {
            ArrayList<String> result = new ArrayList<>();
            result.add("0");
            if (n == 1) result.add("1");
            return result;
        }

        ArrayList<String> root = new ArrayList<>();
        ArrayList<String> recurse = getNGray(n - 1);

        for (String s : recurse) {
            root.add("0" + s);
        }
        for (int i = recurse.size() - 1; i >= 0; i--) {
            String s = recurse.get(i);
            root.add("1" + s);
        }

        return root;
    }

    private String nextPerformance(String code) {
        String result = String.format("%s | ", code);
        if (Integer.parseInt(code) == 0) {
            return result.concat(String.format("%-45s", "SILENT STAGE")).concat("| SILENCE");
        }
        String players = "";
        char[] codeArr = code.toCharArray();
        for (int i = 0; i < band.size(); i++) {
            if (codeArr[i] == '0') continue;
            players = players.concat(String.format("%s ", band.get((band.size() - 1) - i)));
        }
        result = result.concat(String.format("%-45s", players));

        char[] prevCodeArr = grayCodes.get(grayCodes.indexOf(code) - 1).toCharArray();
        int i = 0;
        while ((Integer.parseInt(String.valueOf(codeArr[i])) ^ Integer.parseInt(String.valueOf(prevCodeArr[i]))) != 1) {
            i++;
        }
        String action = codeArr[i] == '1' ? "Joins" : "Fades";
        result = result.concat(String.format("| %s %s", band.get((band.size() - 1) - i), action));

        return result;
    }

    public void perform() {
        for (String code : grayCodes) {
            System.out.println(nextPerformance(code));
        }
    }

    public static void main(String[] args) {
        String[] members = {"Alfie", "Berty", "Crizz", "Dietz", "Elmer", "Fleek", "Gomer"};
        AlgoRhythmics band = new AlgoRhythmics(members);
        band.perform();
    }
}

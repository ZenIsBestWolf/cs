package dev.dignan.DoubleTrouble;

import java.util.HashMap;

/**
 * The class that contains the core game logic and game board
 * @author Zen Dignan
 */

public class DoubleTrouble {
    /**
     * The game board as a HashMap with the colors and corresponding remaining markers
     */
    private final HashMap<String, Integer> markers;

    /**
     * The current turn number
     */
    private int turn;
    /**
     * Whether it is the player's turn or not
     */
    private boolean isPlayerTurn;
    /**
     * Whether debugMode is on or not
     */
    private boolean debugMode;

    /**
     * The most specific constructor for DoubleTrouble
     * @param greens initial number of green markers
     * @param yellows initial number of yellow markers
     * @param oranges initial number of orange markers
     * @param isPlayerFirst whether the player is first or not
     */
    public DoubleTrouble(int greens, int yellows, int oranges, boolean isPlayerFirst) {
        this.markers = new HashMap<>();
        this.markers.put("green", greens);
        this.markers.put("yellow", yellows);
        this.markers.put("orange", oranges);
        this.turn = 0;
        this.isPlayerTurn = !isPlayerFirst;
        this.debugMode = false;
    }

    /**
     * Less specific constructor for DoubleTrouble, assumes player will go first
     * @param greens initial number of green markers
     * @param yellows initial number of yellow markers
     * @param oranges initial number of orange markers
     */
    public DoubleTrouble(int greens, int yellows, int oranges) {
        this(greens, yellows, oranges, true);
    }

    /**
     * Less specific constructor for DoubleTrouble, assumes base setup of 3, 7, and 5.
     * @param isPlayerFirst whether the player is first or not
     */
    public DoubleTrouble(boolean isPlayerFirst) {
        this(3, 7, 5, isPlayerFirst);
    }

    /**
     * The minimum constructor for DoubleTrouble
     */
    public DoubleTrouble() {
        this(true);
    }

    /**
     * Checks if the game is over
     * @return whether the game is over or not
     */
    public boolean isGameOver() {
        for (String key : markers.keySet()) {
            if (markers.get(key) != 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * Enables debug mode
     */
    public void enableDebug() {
        this.debugMode = true;
    }

    /**
     * Checks if debug mode is on
     * @return whether debug mode is on or not
     */
    public boolean isDebugModeEnabled() {
        return debugMode;
    }

    /**
     * Get the number of green markers
     * @return the number of green markers
     */
    public int getGreens() {
        return markers.get("green");
    }

    /**
     * Get the number of yellow markers
     * @return the number of yellow markers
     */
    public int getYellows() {
        return markers.get("yellow");
    }

    /**
     * Get the number of orange markers
     * @return the number of orange markers
     */
    public int getOranges() {
        return markers.get("orange");
    }

    /**
     * Get the number of specified color markers
     * @param color the color marker to look up
     * @return the number of specified markers
     */
    public int getMarkers(String color) {
        if (!markers.containsKey(color)) {
            throw new IllegalArgumentException("Invalid color provided");
        }

        return markers.get(color);
    }

    /**
     * Get the markers of which a move can be made on
     * @return a hashmap of markers with 1 or more remaining
     */
    public HashMap<String, Integer> getAvailableMarkers() {
        HashMap<String, Integer> available = new HashMap<>();
        for (String color : markers.keySet()) {
            if (markers.get(color) != 0) {
                available.put(color, markers.get(color));
            }
        }
        return available;
    }

    /**
     * Checks if it is the player's turn
     * @return whether it is the player's turn or not
     */
    public boolean isPlayerTurn() {
        return isPlayerTurn;
    }

    /**
     * Changes the turn from the Player to COM and vice versa
     */
    public void flipTurn() {
        isPlayerTurn = !isPlayerTurn;
    }

    /**
     * Removes the specified amount of markers of the specified color
     * @param color the color markers to remove
     * @param count the number of markers to remove
     * @return a string summarizing the move made
     */
    public String removeMarkers(String color, int count) {
        if (!markers.containsKey(color)) {
            throw new IllegalArgumentException("Invalid color provided");
        }
        if (count <= 0) {
            throw new IllegalArgumentException("Must remove at least 1 marker");
        }

        int newMarkerCount = markers.get(color) - count;
        markers.put(color, newMarkerCount);

        String player = isPlayerTurn ? "You" : "COM";
        String markerPlurality = count == 1 ? "marker" : "markers";
        return String.format("[Turn %d] %s removed %d %s %s.", turn, player, count, color, markerPlurality);
    }

    /**
     * Get the current state of the game board
     * @return a string summarizing the state of the game board
     */
    public String showBoard() {
        int greenCount = getGreens();
        int yellowCount = getYellows();
        int orangeCount = getOranges();
        String greenPlurality = greenCount == 1 ? "1 green" : String.format("%d greens", greenCount);
        String yellowPlurality = yellowCount == 1 ? "1 yellow" : String.format("%d yellows", yellowCount);
        String orangePlurality = orangeCount == 1 ? "1 orange" : String.format("%d oranges", orangeCount);
        return String.format("[Turn %d] The board stands at: %s, %s, and %s.", turn, greenPlurality, yellowPlurality, orangePlurality);
    }

    /**
     * Handle operations to start the next turn
     * @return a string stating who's turn it is
     */
    public String nextTurn() {
        turn++;
        flipTurn();
        String person = isPlayerTurn() ? "your" : "COM's";
        return String.format("[Turn %d] It's %s turn!", turn, person);
    }
}

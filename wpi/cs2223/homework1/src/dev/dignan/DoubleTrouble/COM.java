package dev.dignan.DoubleTrouble;

import java.util.ArrayList;
import java.util.Random;

/**
 * This class controls the Computer player in DoubleTrouble.
 * @author Zen Dignan
 */
public class COM {
    /**
     * A random number generator
     */
    private final Random random;
    /**
     * The game object for the current game
     */
    private final DoubleTrouble game;

    /**
     * Minimum constructor for a new COM object
     * @param game DoubleTrouble game object for current game
     */
    public COM(DoubleTrouble game) {
        this.random = new Random();
        this.game = game;
    }

    /**
     * Constructor for new COM object with specific seed
     * @param game DoubleTrouble game object for current game
     * @param seed Integer for seed to be used in random number generator
     */
    public COM(DoubleTrouble game, int seed) {
        this.random = new Random(seed);
        this.game = game;
        System.out.printf("[D] COM seed is set to: %d\n", seed);
    }

    /**
     * Calculate and make a move on the game board
     * @return the string containing the move made
     */
    public String move() {
        if (game.isGameOver()) {
            throw new IllegalStateException("The board is cleared, no move can be made.");
        }

        /*
         * Formulas found at https://en.wikipedia.org/wiki/Nim#Mathematical_theory
         */
        int nimSum = game.getGreens() ^ game.getYellows() ^ game.getOranges();
        if (nimSum != 0) {
            if (game.isDebugModeEnabled()) System.out.println("[D] COM is making a calculated move.");
            ArrayList<String> colors = new ArrayList<>(game.getAvailableMarkers().keySet());
            for (String color : colors) {
                int heapSize = game.getMarkers(color);
                int localNimSum = heapSize ^ nimSum;
                if (localNimSum < heapSize) {
                    int take = heapSize - localNimSum;
                    return game.removeMarkers(color, take);
                }
            }
            if (game.isDebugModeEnabled()) System.out.println("[D] Something went wrong. The nim-sum was not 0 but no valid move was found.");
        }

        return randomMove();
    }

    /**
     * Make a completely random move on the game board
     * @return the string containing the move made
     */
    private String randomMove() {
        if (game.isDebugModeEnabled()) System.out.println("[D] COM is making a random move.");
        ArrayList<String> colors = new ArrayList<>(game.getAvailableMarkers().keySet());
        String color = colors.get(random.nextInt(colors.size()));
        int take = random.nextInt(game.getMarkers(color)) + 1;

        return game.removeMarkers(color, take);
    }
}
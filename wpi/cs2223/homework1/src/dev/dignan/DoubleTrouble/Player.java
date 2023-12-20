package dev.dignan.DoubleTrouble;

import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * This class controls the Player object and handles user interaction
 * @author Zen Dignan
 */
public class Player {
    /**
     * The scanner object used to handle user input
     */
    private final Scanner scan;
    /**
     * The game object for the current game
     */
    private final DoubleTrouble game;

    /**
     * The minimum constructor for a new Player object
     * @param game DoubleTrouble game object for current game
     */
    public Player(DoubleTrouble game) {
        this.game = game;
        scan = new Scanner(System.in);
    }

    /**
     * Have the player make a move on the game board
     * @return the string containing the move made
     */
    public String move() {
        if (game.isGameOver()) {
            throw new IllegalStateException("The board is cleared, no move can be made.");
        }

        String color;
        int available;
        int take;

        System.out.println("[?] Please select a heap to pull from (green, yellow, orange):");
        try {
            color = scan.next().toLowerCase().strip();
            available = game.getMarkers(color);
        } catch (IllegalArgumentException e) {
            color = "INVALID";
            available = -1;
        }
        while (available <= 0) {
            System.out.println("[X] Either that heap is empty or you entered something invalid. Please try again.");
            try {
                color = scan.next().toLowerCase().strip();
                available = game.getMarkers(color);
            } catch (IllegalArgumentException e) {
                color = "INVALID";
                available = -1;
            }
        }

        System.out.println("[?] How many would you like to take? You can remove no less than 1.");
        try {
            take = scan.nextInt();
        } catch (InputMismatchException e) {
            take = -1;
        }
        while (take < 0 || take > available) {
            System.out.println("[X] Either that heap doesn't have enough, is empty, or you entered something invalid. Please try again.");
            try {
                take = scan.nextInt();
            } catch (InputMismatchException e) {
                take = -1;
            }
        }

        return game.removeMarkers(color, take);
    };
}

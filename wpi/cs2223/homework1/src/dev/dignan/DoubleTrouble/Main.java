package dev.dignan.DoubleTrouble;

import java.util.Scanner;

/**
 * Main class to run the game
 * @author Zen Dignan
 */
public class Main {
    /**
     * Main method to run the game
     * @param args command line arguments
     */
    public static void main(String[] args) {
        boolean debug = args.length > 1 && args[0].equals("--debug");
        if (debug) System.out.println("[D] DEBUG MODE IS ACTIVATED!");

        DoubleTrouble game = new DoubleTrouble();
        Player player = new Player(game);
        COM com = debug ? new COM(game, Integer.parseInt(args[1])) : new COM(game);
        Scanner scan = new Scanner(System.in);

        if (debug) game.enableDebug();

        System.out.println("[*] Welcome to DOUBLE TROUBLE!");
        System.out.println("[*] For more information about this game, visit https://en.wikipedia.org/wiki/Nim");
        System.out.println("[?] Do you want to go first? (yes/no)");
        String choice = scan.next().toLowerCase().strip();

        if (choice.equals("n") || choice.equals("no")) {
            game.flipTurn();
        } else if (!choice.equals("y") && !choice.equals("yes")) {
            System.out.println("[!] You did not pick a valid option so you're going first.");
            System.out.println("[!] Did you think you were going to get a prize? A chance to choose something else? That's not how this works.");
        }

        /*
         * The game will continue until a game-over state has been reached, where all heaps are 0.
         * This loop prints out basic information and then runs the relative move. All logic is in the classes.
         */
        while (!game.isGameOver()) {
            System.out.println(game.nextTurn());
            System.out.println(game.showBoard());
            if (debug) {
                int nimSum = game.getGreens() ^ game.getYellows() ^ game.getOranges();
                System.out.printf("[D] The nim-sum of the board stands at %d\n", nimSum);
            }
            System.out.println(game.isPlayerTurn() ? player.move() : com.move());
        }

        String winner = game.isPlayerTurn() ? "you" : "COM";
        System.out.printf("[!] The game is over! The winner is %s!\n", winner);
    }
}

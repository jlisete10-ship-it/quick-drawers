package com.codeforall.online.quickdrawers.arenashooter;

/**
 * Starts the arena shooter application.
 *
 * Pseudocode:
 * 1. Create the game.
 * 2. Initialize the game.
 */
public class Main {

    public static void main(String[] args) {
        Game game = new Game();

        game.init();
        game.start();
    }
}

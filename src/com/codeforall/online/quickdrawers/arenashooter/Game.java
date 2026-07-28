package com.codeforall.online.quickdrawers.arenashooter;

/**
 * Coordinates the initialization and execution of the game.
 *
 * Pseudocode:
 * 1.Create the arena
 * 2.Store the arena for later use
 * 3.Draw the initial game area
 */
public class Game {

    private Arena arena;

    public void init() {
        arena = new Arena();
        arena.draw();
    }


}

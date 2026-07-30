package com.codeforall.online.quickdrawers.arenashooter;

/**
 * Coordinates the initialization and execution of the game.
 *
 * The game loop repeatedly updates the game state while the game is running
 * Player, enemy, bullet and collision updates will be added during integration
 * Pseudocode:
 * INITIALIZE GAME
 * 1.Create the arena
 * 2.Draw the arena
 * START GAME
 * 1.Mark the game as running
 * 2.Reapeat while the game is running:
 *      a)Update the game state
 *      b)Pause before next update
 * STOP GAME
 * 1.Mark the game as no longer running
 * 2.Allow the loop to finish
 */
public class Game {

    private static final int FRAME_DELAY = 30;

    //private Arena arena;
    private boolean running;

    public void init() {
       // arena = new Arena();
      //  arena.draw();
    }

    public void start() {
        running = true;

        while (running) {
            update();
            pauseGameLoop();
        }
    }
    /**
     * Future integration will:
     * 1. Update player and enemy movement.
     * 2. Update active bullets.
     * 3. Remove bullets that reach an arena boundary.
     * 4. Check collisions.
     * 5. Stop the game when a player is defeated.
     */
    private void update() {
            //Player, enemy, bullets, and collisions wil be updated here during integration
    }

    private void pauseGameLoop () {
        try {
            Thread.sleep(FRAME_DELAY);
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            running = false;
        }
    }

    public void stop() {
        running = false;
    }

}

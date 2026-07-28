package com.codeforall.online.quickdrawers.arenashooter;

import com.codeforall.simplegraphics.graphics.Rectangle;

/**
 * Defines and draws the area where the game takes place.
 *
 * Pseudocode:
 * 1. Define the arena dimensions and padding.
 * 2. Create the rectangle representing the arena.
 * 3. Draw the arena during game initialization.
 */
public class Arena {

    private static final int PADDING = 10;
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;

    private final Rectangle boundary;

    public Arena() {
        boundary = new Rectangle(PADDING, PADDING, WIDTH, HEIGHT);
    }

    public void draw() {
        boundary.draw();
    }
}
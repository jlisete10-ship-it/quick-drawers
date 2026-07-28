package com.codeforall.online.quickdrawers.arenashooter;

import com.codeforall.simplegraphics.graphics.Rectangle;

/**
 * Defines and draws the area where the game takes place.
 *
 * Pseudocode:
 * 1.Define the arena dimensions and padding.
 * 2.Create the rectangle representing the arena.
 * 3.Draw the arena during game initialization.
 * 4.Provide the boundaries required to keep moving objects inside
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

    /**
     * Returns the x-coordinate of the arena's left edge.
     * Moving objects can use this value to avoid leaving the arena on the left.
     */
    public int getLeftBoundary() {
        return boundary.getX();
    }

    /**
     * Returns the x-coordinate of the arena's right edge.
     * The right edge is calculated by adding the width to the starting x-coordinate.
     */
    public int getRightBoundary() {
        return boundary.getX() + boundary.getWidth();
    }

    /**
     * Returns the y-coordinate of the arena's top edge.
     * Moving objects can use this value to avoid leaving the arena at the top.
     */
    public int getTopBoundary() {
        return boundary.getY();
    }

    /**
     * Returns the y-coordinate of the arena's bottom edge.
     * The bottom edge is calculated by adding the height to the starting y-coordinate.
     */
    public int getBottomBoundary() {
        return boundary.getY() + boundary.getHeight();
    }

}
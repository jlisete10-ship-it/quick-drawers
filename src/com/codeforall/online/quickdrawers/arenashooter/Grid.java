package com.codeforall.online.quickdrawers.arenashooter;

import com.codeforall.simplegraphics.graphics.Rectangle;

public class Grid {

    public static final int PADDING = 10;
    public static final int WINDOW_WIDTH = 1500;
    public static final int WINDOW_HEIGHT = 800;
    private int cellSize = 82;
    private int cols;
    private int rows;
    private Rectangle field;

    public Grid() {
        this.cols = (WINDOW_WIDTH - 2 * PADDING) / cellSize;
        this.rows = (WINDOW_HEIGHT - 2 * PADDING) / cellSize;
    }

    public void init() {// it will initialize the field:
        field = new Rectangle(PADDING, PADDING, cols * cellSize, rows * cellSize);
        field.draw();

    }
    public Rectangle getField() {
        return field;
    }

    public int getCellSize() {
        return cellSize;
    }

    public int getCols() {
        return this.cols;
    }

    public int getRows() {
        return this.rows;
    }


    public int rowToY(int row) {// converts into pixels

        return PADDING + cellSize * row;
    }


    public int columnToX(int column) {

        return PADDING + cellSize * column;
    }

}

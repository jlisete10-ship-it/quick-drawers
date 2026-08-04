package com.codeforall.online.quickdrawers.arenashooter;

import com.codeforall.simplegraphics.graphics.Rectangle;
import com.codeforall.simplegraphics.pictures.Picture;

public class Position {

    private int col,row;
    private Grid grid;
    private Picture picture;


    public Position(Grid grid, int col, int row, String imagePath){
        this.grid = grid;
        this.col = col;
        this.row =row;

      //  Convert the grid coordinates into pixel coordinates.

        int x = grid.columnToX(col);
        int y = grid.rowToY(row);
        picture = new Picture(x,y, imagePath); // Create and draw the picture at the calculated position.
        picture.draw();


    }

    public boolean moveUp(){
        if(row==0){
            return false;
        }

        row--;
        picture.translate(0,-grid.getCellSize());
        return true;
    }

    public boolean moveDown(){
        if(row == grid.getRows()-1){
            return false;
        }
        row ++;
        picture.translate(0,grid.getCellSize());
        return true;
    }
    public boolean moveRight(){

        if (col==grid.getCols() -1){
            return false; //random direction
        }

        col++;
        picture.translate(grid.getCellSize(),0);
        return true; //continua
    }

    public boolean moveLeft(){
        if(col ==0){
            return false;
        }

        col--;
        picture.translate(-grid.getCellSize(),0);
        return true;
    }

    public int getCol(){
        return col;
    }

    public int getRow() {
        return row;
    }

    public void removeBullet(){// it will remove the bullet graphically
        picture.translate(-5000, -5000);
        picture.delete();

    }

    public Grid getGrid() {
        return grid;
    }
    //These getters return the collision hitbox of the picture.

    public int getCollisionX() {
        return picture.getX() + 15;
    }

    public int getCollisionY() {
        return picture.getY() + 15;
    }

    // The hitbox is smaller than the image to make collision detection more accurate.

    public int getCollisionWidth() {
        return picture.getWidth() - 30;
    }

    public int getCollisionHeight() {
        return picture.getHeight() - 30;
    }

}
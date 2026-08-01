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

        // graphical conversion of col and row into pixels:

        int x = grid.columnToX(col);
        int y = grid.rowToY(row);
        picture = new Picture(x,y, imagePath); // pseudo creation of player
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
    public boolean moveRight(){  //seria fazer um boolean em que fazemos um if -- se passar os limites retorna falso, se nao estiver nos limites returna treu e anda para a direita

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

    public Grid getGrid(){
        return grid;
    }

    public void removeBullet(){// irá remover a bullet graficamente
        this.picture.delete();
    }
}
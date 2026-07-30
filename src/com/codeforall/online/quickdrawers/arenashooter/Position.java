package com.codeforall.online.quickdrawers.arenashooter;

import com.codeforall.simplegraphics.graphics.Rectangle;

public class Position {

    private int col,row;
    private Grid grid;
    private Rectangle rectangle;


    public Position(Grid grid, int col, int row){
        this.grid = grid;
        this.col = col;
        this.row =row;

        // graphical conversion of col and row into pixels:

        int x = grid.columnToX(col);
        int y = grid.rowToY(row);
        rectangle = new Rectangle(x,y, grid.getCellSize(), grid.getCellSize()); // pseudo creation of player
        rectangle.fill();


    }

    public boolean moveUp(){
        if(row==0){
            return false;
        }

        row--;
        rectangle.translate(0,-grid.getCellSize());
        return true;
    }

    public boolean moveDown(){
        if(row == grid.getRows()-1){
            return false;
        }
        row ++;
        rectangle.translate(0,grid.getCellSize());
        return true;
    }
    public boolean moveRight(){  //seria fazer um boolean em que fazemos um if -- se passar os limites retorna falso, se nao estiver nos limites returna treu e anda para a direita

        if (col==grid.getCols() -1 ){
            return false; //random direction
        }

        col++;
        rectangle.translate(grid.getCellSize(),0);
        return true; //continua
    }

    public boolean moveLeft(){
        if(col ==0){
            return false;
        }

        col--;
        rectangle.translate(-grid.getCellSize(),0);
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
        this.rectangle.delete();
    }
}
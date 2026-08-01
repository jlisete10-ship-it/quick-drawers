package com.codeforall.online.quickdrawers.arenashooter;

public class Bullet implements Movable{

    private Position position;
    private Grid grid;
    private Directions direction;
    private boolean bulletOn = true;
    private boolean movingRight;

    //public Bullet (Grid grid, Position position){// este position recebido como parametro é o position do player
        // A Bullet recebe a posição atual do Player, consulta onde ele está e define o
        // início uma coluna à direita, mantendo a mesma linha.
    /**
     * Lorenzo:
     * Creating a new Bullet Constructor with Grid, Shooter Position (Player or Enemy),
     * the image path and a boolean "moving right" > if true player bullet for the right
     * if false, enemy bullet for the left
     */
     public Bullet(Grid grid, Position shooterPosition, String imagePath, boolean movingRight){
        //int startCol = position.getCol()+1;
        //int startRow = position.getRow();
        this.grid = grid;
        this.movingRight = movingRight;

        //If the bullet is going right, create the bullet in the col on the right from the one who shot
        //If the bullet is going left, create the bullet in the col on the left from the one who shot
        int startCol;
        if (movingRight) {
            startCol = shooterPosition.getCol() + 1;
        } else {
            startCol = shooterPosition.getCol() -1;
        }
        //Creat the bullet in the same line of the one who shot
        int startRow = shooterPosition.getRow();

        // Aqui estou a criar uma position propria para a bullet:
        this.position = new Position(grid, startCol, startRow, imagePath);
        


    }

    @Override
    public void move() {
        //if (!this.position.moveRight()){ // se a bullet chegar ao limite da wall direita
                //bulletOn = false;
                //return;
        /**
         * Which way the bullet is going, move the bullet in this direction and
         * if the bullet is off the limits turn it off
         */
                boolean stillInsideGrid;

                if (movingRight) {
                    stillInsideGrid = position.moveRight();
                }else {
                    stillInsideGrid = position.moveLeft();
                }
                if (!stillInsideGrid) {
                    bulletOn = false;
                }
    }

    public boolean isBulletActive(){
        return bulletOn;
    }

    public void removeBullet(){
        this.position.removeBullet();
    }
}

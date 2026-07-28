package com.codeforall.online.quickdrawers.arenashooter;

public class Bullet implements Movable{

    private Position position;
    private Grid grid;
    private Directions direction;

    public Bullet (Grid grid, Position position){// este position recebido como parametro é o position do player
        // A Bullet recebe a posição atual do Player, consulta onde ele está e define o
        // início uma coluna à direita, mantendo a mesma linha.
        int startCol = position.getCol()+1;
        int startRow = position.getRow();
        this.grid = grid;

        // Aqui estou a criar uma position propria para a bullet:
        this.position = new Position(grid,startCol,startRow);
       


    }

    @Override
    public void move() {

        this.position.moveRight();





    }

}

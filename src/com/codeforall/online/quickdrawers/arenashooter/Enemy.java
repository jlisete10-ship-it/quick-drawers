package com.codeforall.online.quickdrawers.arenashooter;

import java.util.ArrayList;
import java.util.List;

public class Enemy implements Shootable, Movable{

    private Grid grid;
    //enemy position on the grid > object that makes enemy move
    private Position position;
   //enemy direction at the moment
    private Directions currentDirection = Directions.UP;
    //steps remaining until a new random direction is choosen
    private int stepsRemaining = 0;
    //control enemy speed
    private int frameCounter = 0;
    //static shared with all enemy instances
    private static final int MOVE_DELAY = 5;
    private boolean alive = true;
    //private Bullet bullet;

    //creating array list for bullets
    private List<Bullet> bullets = new ArrayList<>();

    // Stores the time (milliseconds) of the last time the enemy shot
    private long lastShootTime = 0;

    public Enemy(Grid grid, Position position){
        this.grid = grid;
        this.position = position;
    }

    @Override
    public void move() {

        // Enemy movement logic 1 in 5 method calls
       frameCounter++;

       if (frameCounter < MOVE_DELAY) { // para desacelerar o movimento do enemy
           return;
        }

        frameCounter = 0;

        // If steps remaining = zero we choose another direction and how many steps in this direction
        if (stepsRemaining == 0) {
            if (Math.random() < 0.5) {
                currentDirection = Directions.UP;
            } else {
                currentDirection = Directions.DOWN;
            }
            stepsRemaining = (int) (Math.random() * 20) + 8;
        }

        boolean moved;

        if (currentDirection == Directions.UP) {
            moved = position.moveUp();
        } else {
            moved = position.moveDown();
        }

        if (!moved) {
            if (currentDirection == Directions.UP) {
                currentDirection = Directions.DOWN;
                position.moveDown();
            } else {
                currentDirection = Directions.UP;
                position.moveUp();
            }

            stepsRemaining = (int) (Math.random() * 20) + 8;

        } else {
            stepsRemaining--;
        }

        //Shooting in the end of the movement 1 in 5 frames
        long currentTime = System.currentTimeMillis();

        if (currentTime - lastShootTime >= 1000) {
            shoot();
            lastShootTime = currentTime;
        }
    }

    @Override
    public void shoot() {
        Bullet bullet = new Bullet(grid, position, "/enemyBullet.png", false);
        bullets.add(bullet);
    }

    //de trás para frente para evitar o risco de saltar elementos
    //remover enquanto decorre a iteração
    public void moveEnemyBullets() {
        // Loop backwards so removing an item doesn't shift the
        // indices of the elements we still need to check
        for (int i = bullets.size() - 1; i >= 0; i--) {
            Bullet bullet = bullets.get(i);
            if (bullet.isBulletActive()) {
                bullet.move();
            } else {
                bullet.removeBullet();
                bullets.remove(i);
            }
        }
    }

    public List<Bullet> getBullets(){
        return bullets;
    }

    public Position getPosition() {
        return position;
    }

    public boolean isAlive() {
        return alive;
    }

    public void hit(){
        alive= false;
    }

}


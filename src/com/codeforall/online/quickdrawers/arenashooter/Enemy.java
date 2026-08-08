package com.codeforall.online.quickdrawers.arenashooter;

import java.util.ArrayList;
import java.util.List;

public class Enemy implements Shootable, Movable{

    private Grid grid;
    private Position position;
    private Directions currentDirection = Directions.UP;
    private int stepsRemaining = 0;
    private int frameCounter = 0;
    private boolean alive =true;
    private DifficultyStrategy difficulty;

    //creating array list for bullets
    private List<Bullet> bullets = new ArrayList<>();

    // Stores the time (milliseconds) of the last time the enemy shot
    private long lastShootTime = 0;

    public Enemy(Grid grid, Position position, DifficultyStrategy difficulty){
        this.grid = grid;
        this.position = position;
        this.difficulty = difficulty;
    }

    @Override
    public void move() {

       frameCounter++;
        System.out.println(difficulty.getEnemyMoveDelay());

       if (frameCounter < difficulty.getEnemyMoveDelay()) { // to desacelerate the enemy movement
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

        long currentTime = System.currentTimeMillis(); // devolve o momento atual em milissegundos

        if (currentTime - lastShootTime >= 1000) {// se passaram pelo menos 1000 milissegundos desde o ultimo disparo, volto a disparar
            shoot();
            lastShootTime = currentTime;
        }
    }

    @Override
    public void shoot() {
        System.out.println("Enemy shot");
        Bullet bullet = new Bullet(grid, position, "resources/enemyBullet.png", false, difficulty);
        bullets.add(bullet);
    }

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

    public void removeEnemy(){
        this.position.remove();

    }

    public void removeEnemyBullets(){
        for(int i=bullets.size()-1;i >=0; --i ){
            bullets.get(i).removeBullet();// removes graphically
            bullets.remove(i);// removes from arraylist
        }
    }

   public void setDifficulty(DifficultyStrategy difficulty){
       this.difficulty = difficulty;
   }

}


package com.codeforall.online.quickdrawers.arenashooter;

import java.util.ArrayList;
import java.util.List;

public class Enemy implements Shootable, Movable {

    private Grid grid;
    private Position position;

    private Directions currentDirection = Directions.UP;
    private int stepsRemaining = 0;
    private int frameCounter = 0;

    private boolean alive = true;

    private DifficultyStrategy difficulty;

    private List<Bullet> bullets = new ArrayList<>();

    // Stores the time of the last enemy shot
    private long lastShootTime = 0;


    public Enemy(
            Grid grid,
            Position position,
            DifficultyStrategy difficulty) {

        this.grid = grid;
        this.position = position;
        this.difficulty = difficulty;
    }


    @Override
    public void move() {

        frameCounter++;

        // Controls enemy movement speed according to the difficulty
        if (frameCounter < difficulty.getEnemyMoveDelay()) {
            return;
        }

        frameCounter = 0;


        // Choose a new direction when there are no steps remaining
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


        // If the enemy reaches the edge, change direction
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


        // Enemy shoots every second
        long currentTime = System.currentTimeMillis();

        if (currentTime - lastShootTime >= 1000) {

            shoot();

            lastShootTime = currentTime;
        }
    }


    @Override
    public void shoot() {

        System.out.println("Enemy shot");

        Bullet bullet = new Bullet(
                grid,
                position,
                "resources/enemyBullet.png",
                false,
                difficulty
        );

        bullets.add(bullet);
    }


    public void moveEnemyBullets() {

        // Iterate backwards so bullets can be safely removed
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


    public List<Bullet> getBullets() {
        return bullets;
    }


    public Position getPosition() {
        return position;
    }


    public boolean isAlive() {
        return alive;
    }


    public void hit() {
        alive = false;
    }


    public void removeEnemy() {
        position.remove();
    }


    public void removeEnemyBullets() {

        for (int i = bullets.size() - 1; i >= 0; i--) {

            bullets.get(i).removeBullet();
            bullets.remove(i);
        }
    }


    public void setDifficulty(DifficultyStrategy difficulty) {
        this.difficulty = difficulty;
    }
}


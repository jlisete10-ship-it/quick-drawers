package com.codeforall.online.quickdrawers.arenashooter;

import com.codeforall.simplegraphics.keyboard.Keyboard;
import com.codeforall.simplegraphics.keyboard.KeyboardEvent;
import com.codeforall.simplegraphics.keyboard.KeyboardEventType;
import com.codeforall.simplegraphics.keyboard.KeyboardHandler;
import com.codeforall.simplegraphics.pictures.Picture;

public class Game {

    private static final int FRAME_DELAY = 30;

    private Grid grid;
    private boolean running;
    private Player player;
    private Enemy enemy;
    private CollisionDetector collisionDetector;
    private Picture background;
    private Picture gameOver;
    private Picture youWin;

    public void init() {

        background = new Picture(0,0, "resources/space4.jpg");
        background.draw();

        grid = new Grid();
        grid.init();

        Position playerPosition = new Position(grid, 0, 0, "resources/playerShip.png");
        player = new Player(playerPosition);

        Position enemyPosition = new Position(grid, grid.getCols() - 2, 0, "resources/Enemy.png");
        enemy = new Enemy(grid, enemyPosition);

        collisionDetector = new CollisionDetector();

    }

    public void start() {

        running = true;

        while (running) {
            update();
            pauseGameLoop();
        }
    }

    private void update() {

        if (!running) {
            return;
        }

        // Move player and enemy
        player.move();
        enemy.move();

        // Move bullets
        player.movePlayerBullets();
        enemy.moveEnemyBullets();

        // Check collisions
        resolveCollisions();
    }

    private void resolveCollisions() {

        boolean enemyWasHit = false;
        boolean playerWasHit = false;

        /**
         * Player bullets -> Enemy
         */
        for (Bullet bullet : player.getBullets()) {

            if (!bullet.isBulletActive()) {
                continue;
            }

            if (collisionDetector.collides(
                    bullet.getPosition(),
                    enemy.getPosition())) {

                bullet.deactivate();
                enemy.hit();

                enemyWasHit = true;
            }
        }

        /**
         * Enemy bullets -> Player
         */
        for (Bullet bullet : enemy.getBullets()) {

            if (!bullet.isBulletActive()) {
                continue;
            }

            if (collisionDetector.collides(
                    bullet.getPosition(),
                    player.getPosition())) {

                bullet.deactivate();
                player.hit();

                playerWasHit = true;
            }
        }

        if (!enemyWasHit && !playerWasHit) {
            return;
        }

        if (enemyWasHit && playerWasHit) {

            System.out.println("Draw!");

        } else if (enemyWasHit) {

            System.out.println("Player wins!");
            youWin = new Picture(0,0, "resources/youWin.png");

            int centerX = (background.getWidth() - youWin.getWidth()) / 2;
            int centerY = (background.getHeight() - youWin.getHeight()) / 2;
            youWin.translate(centerX, centerY);
            youWin.draw();


        } else {

            System.out.println("Enemy wins!");
            gameOver = new Picture(0,0, "resources/game_over.png");

            int centerX = (background.getWidth() - gameOver.getWidth()) / 2;
            int centerY = (background.getHeight() - gameOver.getHeight()) / 2;
            gameOver.translate(centerX, centerY);
            gameOver.draw();

        }

        stop();
    }

    private void pauseGameLoop() {

        try {
            Thread.sleep(FRAME_DELAY);

        } catch (InterruptedException e) {
            stop();
        }
    }

    public void stop() {
        running = false;
    }

}
package com.codeforall.online.quickdrawers.arenashooter;

public class Game {

    private static final int FRAME_DELAY = 30;

    private Grid grid;
    private boolean running;
    private Player player;
    private Enemy enemy;
    private CollisionDetector collisionDetector;

    public void init() {

        grid = new Grid();
        grid.init();

        Position playerPosition = new Position(grid, 0, 0, "/playerShip.png");
        player = new Player(playerPosition);

        Position enemyPosition = new Position(grid, grid.getCols() - 2, 0, "/Enemy.png");
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

        } else {

            System.out.println("Enemy wins!");
        }

        stop();
    }

    private void pauseGameLoop() {

        try {
            Thread.sleep(FRAME_DELAY);

        } catch (InterruptedException exception) {

            Thread.currentThread().interrupt();
            stop();
        }
    }

    public void stop() {
        running = false;
    }
}
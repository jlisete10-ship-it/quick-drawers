package com.codeforall.online.quickdrawers.arenashooter;

import com.codeforall.simplegraphics.graphics.Color;
import com.codeforall.simplegraphics.graphics.Text;
import com.codeforall.simplegraphics.keyboard.Keyboard;
import com.codeforall.simplegraphics.keyboard.KeyboardEvent;
import com.codeforall.simplegraphics.keyboard.KeyboardEventType;
import com.codeforall.simplegraphics.keyboard.KeyboardHandler;
import com.codeforall.simplegraphics.pictures.Picture;
import java.util.ArrayList;
import java.util.List;

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
    private Text Score;
    private List<Picture> lifeIcons = new ArrayList<>();
    private int tryScore;
    private int highScore;

    public void init() {
        System.out.println(System.getProperty("user.dir"));
        background = new Picture(0,0, "/space4.jpg");
        background.draw();

        grid = new Grid();
        grid.init();

        Position playerPosition = new Position(grid, 0, 0, "/playerShip.png");
        player = new Player(playerPosition);

        Position enemyPosition = new Position(grid, grid.getCols() - 2, 0, "/Enemy.png");
        enemy = new Enemy(grid, enemyPosition);

        collisionDetector = new CollisionDetector();

        drawPlayerLives();

        //where the score display is structured, edited and shown
        Score = new Text(350,30, "SCORE: " + tryScore);
        Score.grow(40,20);
        Score.setColor(Color.YELLOW);
        Score.draw();

    }

    public void start() {

        tryScore = 0;
        running = true;
        System.out.println("Game has started!");

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
                tryScore = tryScore + 50;
                Score.setText("SCORE: " + tryScore);

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
                updatePlayerLivesDisplay();

                playerWasHit = true;
            }
        }

        if (!enemyWasHit && !playerWasHit) {
            return;
        }

/**
 * Both were hit and neither survived
 */
        if (enemyWasHit && playerWasHit && !player.isAlive()) {
            if (tryScore>highScore){
                highScore = tryScore;
            }
            System.out.println("Draw!");
            System.out.println("Your score: " + tryScore);
            System.out.println("Highscore: " + highScore);
            stop();
            return;
        }

/**
 * The enemy was hit
 */
        if (enemyWasHit) {
            if (tryScore>highScore){
                highScore = tryScore;
            }
            System.out.println("Player wins!");
            System.out.println("Your score: " + tryScore);
            System.out.println("Highscore: " + highScore);

            youWin = new Picture(0, 0, "/youWin.png");

            int centerX = (background.getWidth() - youWin.getWidth()) / 2;
            int centerY = (background.getHeight() - youWin.getHeight()) / 2;

            youWin.translate(centerX, centerY);
            youWin.draw();

            stop();
            return;
        }

/**
 * The player only loses when no lives remain
 */
        if (!player.isAlive()) {

            System.out.println("Enemy wins!");

            gameOver = new Picture(0, 0, "/game_over.png");

            int centerX = (background.getWidth() - gameOver.getWidth()) / 2;
            int centerY = (background.getHeight() - gameOver.getHeight()) / 2;

            gameOver.translate(centerX, centerY);
            gameOver.draw();
            if (tryScore>highScore){
                highScore = tryScore;
            }
            System.out.println("Your score: " + tryScore);
            System.out.println("Highscore: " + highScore);

            stop();
        }
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

    private void drawPlayerLives() {

        int spacing = 10;

        // Keep the life icons away from the player's movement column
        int startX = grid.columnToX(2);

        int startY = 20;

        for (int i = 0; i < player.getLives(); i++) {

            Picture lifeIcon = new Picture(
                    startX,
                    startY,
                    "/playerLife.png"
            );

            lifeIcon.draw();
            lifeIcons.add(lifeIcon);

            // Place the next icon after the current image
            startX += lifeIcon.getWidth() + spacing;
        }
    }

    private void updatePlayerLivesDisplay() {

        // Remove icons until the display matches the player's current lives
        while (lifeIcons.size() > player.getLives()) {
            tryScore=tryScore-10;
            Score.setText("SCORE: " + tryScore);
            int lastIconIndex = lifeIcons.size() - 1;
            Picture lostLifeIcon = lifeIcons.remove(lastIconIndex);

            lostLifeIcon.delete();
        }


    }

    private void scoreTimer(){ //when each second goes by, the player loses a point

    }

}
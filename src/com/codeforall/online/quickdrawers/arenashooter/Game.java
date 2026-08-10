package com.codeforall.online.quickdrawers.arenashooter;

import com.codeforall.simplegraphics.keyboard.Keyboard;
import com.codeforall.simplegraphics.keyboard.KeyboardEvent;
import com.codeforall.simplegraphics.keyboard.KeyboardEventType;
import com.codeforall.simplegraphics.keyboard.KeyboardHandler;
import com.codeforall.simplegraphics.mouse.Mouse;
import com.codeforall.simplegraphics.mouse.MouseEvent;
import com.codeforall.simplegraphics.mouse.MouseEventType;
import com.codeforall.simplegraphics.mouse.MouseHandler;
import com.codeforall.simplegraphics.pictures.Picture;


public class Game implements MouseHandler {

    private static final int FRAME_DELAY = 30;

    private enum State {
        MENU,
        PLAYING,
        GAME_OVER
    }


    private Grid grid;
    private boolean running;
    private Player player;
    private Enemy enemy;
    private CollisionDetector collisionDetector;
    private Picture background;
    private Picture resultPicture; //YouWin or GameOver

    private Mouse mouse;
    private State state;
    private Button startButton;
    private Button restartButton;
    private Picture titlePicture;

    public void init() {

        showMenu();
        mouse = new Mouse(this);
        mouse.addEventListener(MouseEventType.MOUSE_CLICKED);

    }

    private void showMenu() {

        background = new Picture(0, 0, "/space2.jpeg");
        background.draw();

        int centerX = background.getWidth() / 2;
        int centerY = background.getHeight() / 2 + 150;

        //Título
        titlePicture = new Picture(0,0, "/gameName.png");
        titlePicture.draw();

        int titleX = centerX - titlePicture.getWidth() / 2;
        int titleY = centerY - 400;
        titlePicture.translate(titleX, titleY);

        startButton = new Button(centerX, centerY, "/start.png");

        state = State.MENU;
    }

    private void startGame(){

        grid = new Grid();
        grid.init();

        Position playerPosition = new Position(grid, 0, 0, "/playerShip.png");
        player = new Player(playerPosition);

        Position enemyPosition = new Position(grid, grid.getCols() - 2, 0, "/Enemy.png");
        enemy = new Enemy(grid, enemyPosition);

        collisionDetector = new CollisionDetector();

        state = State.PLAYING;
        running = true;

    }

    public void start() {

        while (true) {

            if (state == State.PLAYING) {
                update();
            }

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
            showResult("/youWin.png");
        } else if (enemyWasHit) {
            System.out.println("Player wins!");
            showResult("/youWin.png");
        } else {
            System.out.println("Enemy wins!");
            showResult("/game_over.png");
        }
        stop();
    }

    public void showResult(String imagePath){

        resultPicture = new Picture(0, 0, imagePath);

        int centerX = background.getWidth() / 2;
        int centerY = background.getHeight() / 2;

        resultPicture.translate(centerX - resultPicture.getWidth() / 2, centerY - resultPicture.getHeight() / 2);
        resultPicture.draw();

        int buttonY = centerY + resultPicture.getHeight() / 2 + 100;
        restartButton = new Button(centerX, buttonY, "/restart.png");

        state = State.GAME_OVER;
    }

    /**
     * Implementing the MouseHandler methods
     */

    @Override

    //object "e" type MouseEvent
    public void mouseClicked(MouseEvent e){

        System.out.println("Clique detetado em: x=" + e.getX() + " y=" + e.getY());
        if (state == State.MENU && startButton.isClicked(e.getX(), e.getY())){
            startButton.remove();
            titlePicture.delete();
            startGame();
            return;
        }
        if (state == State.GAME_OVER && restartButton.isClicked(e.getX(), e.getY())){
            cleanupPreviousGame();
            restartButton.remove();
            resultPicture.delete();
            startGame();
        }
    }

    public void mouseMoved(MouseEvent e){

    }

    private void cleanupPreviousGame(){
        for (Bullet bullet : player.getBullets()){
            bullet.removeBullet();
        }
        for (Bullet bullet : enemy.getBullets()){
            bullet.removeBullet();
        }

        player.getPosition().delete();
        enemy.getPosition().delete();

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
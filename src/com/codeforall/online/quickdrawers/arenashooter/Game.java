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

public class Game{

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
    private List<Enemy> enemies = new ArrayList<>();
    private int enemiesCreated;
    private int enemiesKilled;
    private long lastSpawn;
    long currentTime;
    private DifficultyStrategy difficulty;

    public void init() {

        System.out.println(System.getProperty("user.dir"));
        background = new Picture(0,0, "resources/space4.jpg");
        background.draw();

        grid = new Grid();
        grid.init();

       difficulty = new NormalMode();


        createPlayer();
        createEnemy();
         lastSpawn = System.currentTimeMillis(); // last time enemy was created


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

    public void update() {

        if (!running) {
            return;
        }
        currentTime = System.currentTimeMillis();
        // Move player and its bullets
        player.move();
        player.movePlayerBullets();


        if (currentTime - lastSpawn >= 1000 && enemiesCreated <10 && enemies.size()<=3) {// se passaram pelo menos 5000 milissegundos desde a criação do ultimo inimigo ,
            // numero de inimigos criados nao aitngiu os 10 e o numero de inimigos ativos é menor que 3, cria-se novo inimigo
            createEnemy();
            lastSpawn = currentTime;
        }
        // move enemies and their bullets
        for(Enemy enemy: enemies){ // aqui estou a aceder à lista de inimigos
            enemy.move();
            enemy.moveEnemyBullets();
        }


        // Check collisions
        resolveCollisions();
    }

    public void createEnemy(){

        Position enemyPosition = new Position(grid, grid.getCols() - 2, 0, "resources/Enemy.png");
        Enemy enemy = new Enemy(grid, enemyPosition,difficulty);
        enemies.add(enemy);
        enemiesCreated++;

    }

    public void createPlayer(){

        Position playerPosition = new Position(grid, 0, 0, "resources/playerShip.png");
        player = new Player(playerPosition,this);

    }

    public void resolveCollisions() {

        boolean enemyWasHit = false;
        boolean playerWasHit = false;

        /**
         * Player bullets -> Enemy
         */
        for (Bullet bullet : player.getBullets()) {

            if (!bullet.isBulletActive()) {
                continue;
            }
            for (int i = enemies.size() - 1; i >= 0; i--) {
                if (collisionDetector.collides(bullet.getPosition(), enemies.get(i).getPosition())) {

                    bullet.deactivate();
                    enemies.get(i).hit();
                    enemies.get(i).removeEnemyBullets();//remove bullets graphically and from array list
                    enemies.get(i).removeEnemy();// remove enemy graphically
                    enemies.remove(i);//remove enemy from list


                    tryScore = tryScore + 50;
                    Score.setText("SCORE: " + tryScore);

                    enemyWasHit = true;

                    break;
                }
            }
        }

        /**
         * Enemy bullets -> Player
         */
        for (Enemy enemy : enemies) {
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
        if (enemyWasHit && enemiesCreated == 10 && enemies.isEmpty()){
            if (tryScore>highScore){
                highScore = tryScore;
            }
            System.out.println("Player wins!");
            System.out.println("Your score: " + tryScore);
            System.out.println("Highscore: " + highScore);

            youWin = new Picture(0, 0, "resources/youWin.png");

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

            gameOver = new Picture(0, 0, "resources/game_over.png");

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

    public void pauseGameLoop() {

        try {
            Thread.sleep(FRAME_DELAY);

        } catch (InterruptedException e) {
            stop();
        }
    }

    public void stop() {
        running = false;
    }



    public void drawPlayerLives() {

        int spacing = 10;

        // Keep the life icons away from the player's movement column
        int startX = grid.columnToX(2);

        int startY = 20;

        for (int i = 0; i < player.getLives(); i++) {

            Picture lifeIcon = new Picture(startX, startY, "resources/playerLife.png");

            lifeIcon.draw();
            lifeIcons.add(lifeIcon);

            // Place the next icon after the current image
            startX += lifeIcon.getWidth() + spacing;
        }
    }


    public void updatePlayerLivesDisplay() {

        // Remove icons until the display matches the player's current lives
        while (lifeIcons.size() > player.getLives()) {
            tryScore=tryScore-10;
            Score.setText("SCORE: " + tryScore);
            int lastIconIndex = lifeIcons.size() - 1;
            Picture lostLifeIcon = lifeIcons.remove(lastIconIndex);

            lostLifeIcon.delete();
        }


    }

    public void activateEasyMode(){
        // Aqui estou a desacelerar os movimentos dos inimigos e das suas balas

        difficulty = new EasyMode();

        for(Enemy enemy: enemies){
            enemy.setDifficulty(difficulty);

        }
    }


    public void scoreTimer(){ //when each second goes by, the player loses a point

    }


}
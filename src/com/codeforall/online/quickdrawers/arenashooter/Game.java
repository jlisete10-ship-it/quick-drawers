package com.codeforall.online.quickdrawers.arenashooter;

import com.codeforall.simplegraphics.graphics.Color;
import com.codeforall.simplegraphics.graphics.Text;
import com.codeforall.simplegraphics.mouse.Mouse;
import com.codeforall.simplegraphics.mouse.MouseEvent;
import com.codeforall.simplegraphics.mouse.MouseEventType;
import com.codeforall.simplegraphics.mouse.MouseHandler;
import com.codeforall.simplegraphics.pictures.Picture;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;

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
    private CollisionDetector collisionDetector;

    private Picture background;
    private Picture resultPicture;

    private Mouse mouse;
    private State state;

    private Button startButton;
    private Button restartButton;
    private Picture titlePicture;

    private Text Score;
    private Text HighScore;

    private List<Picture> lifeIcons = new ArrayList<>();

    private int tryScore;
    private int highScore;

    private final String HIGH_SCORE_FILE = "highscore.txt";

    private List<Enemy> enemies = new ArrayList<>();

    private int enemiesCreated;
    private int enemiesKilled;

    private long lastSpawn;
    private long currentTime;

    private DifficultyStrategy difficulty;


    public void init() {

        showMenu();

        mouse = new Mouse(this);
        mouse.addEventListener(MouseEventType.MOUSE_CLICKED);
    }


    private void showMenu() {

        background = new Picture(0, 0, "resources/space2.jpeg");

        background.draw();

        int centerX = background.getWidth() / 2;
        int centerY = background.getHeight() / 2 + 150;


        // Title
        titlePicture = new Picture(0, 0, "resources/gameName.png");

        int titleX = centerX - titlePicture.getWidth() / 2;
        int titleY = centerY - 400;

        titlePicture.translate(titleX, titleY);
        titlePicture.draw();


        // Start button
        startButton = new Button(centerX, centerY, "resources/start.png");

        state = State.MENU;
    }


    private void startGame() {

        tryScore = 0;

        enemiesCreated = 0;
        enemiesKilled = 0;

        lastSpawn = System.currentTimeMillis();

        // Every new game starts in Normal Mode
        difficulty = new NormalMode();

        grid = new Grid();
        grid.init();


         //First game:
        //create Player + Keyboard.

         //Restart:
         // reuse the same Player + Keyboard.

        createPlayer();

        createEnemy();

        collisionDetector = new CollisionDetector();

        drawPlayerLives();


         // Load High Score only once, when the game starts.

        if (HighScore == null) {
            loadHighScore();
        }


        // Score
        Score = new Text(350, 30, "SCORE: " + tryScore);

        Score.grow(40, 20);
        Score.setColor(Color.YELLOW);
        Score.draw();


        // High Score
        HighScore = new Text(500, 30, "HIGHSCORE: " + highScore);

        HighScore.grow(50, 20);
        HighScore.setColor(Color.ORANGE);
        HighScore.draw();


        state = State.PLAYING;
        running = true;

        System.out.println("Game has started!");
    }


    public void start() {

        while (true) {

            if (state == State.PLAYING) {
                update();
            }

            pauseGameLoop();
        }
    }


    public void update() {

        if (!running) {
            return;
        }

        currentTime = System.currentTimeMillis();


        // Move player and player bullets
        player.move();
        player.movePlayerBullets();



         // Spawn enemies according to the difficulty.

         //NormalMode = 1000 ms
         //EasyMode = 2500 ms

        if (currentTime - lastSpawn >= difficulty.getEnemySpawnDelay() && enemiesCreated < 10 && enemies.size() <= 3) {

            createEnemy();

            lastSpawn = currentTime;
        }


        // Move enemies and enemy bullets
        for (Enemy enemy : enemies) {

            enemy.move();
            enemy.moveEnemyBullets();
        }


        // Check collisions
        resolveCollisions();
    }


    public void createPlayer() {

        Position playerPosition = new Position(grid, 0, 0, "resources/playerShip.png"
        );



         //First game -> create Player.

         //Restart -> reuse the existing Player.

        if (player == null) {

            player = new Player(playerPosition, this);

        } else {

            player.reset(playerPosition);
        }
    }


    public void createEnemy() {

        Position enemyPosition = new Position(grid, grid.getCols() - 2, 0, "resources/Enemy.png");

        Enemy enemy = new Enemy(grid, enemyPosition, difficulty);

        enemies.add(enemy);

        enemiesCreated++;
    }


    public void resolveCollisions() {

        boolean enemyWasHit = false;
        boolean playerWasHit = false;



         // Player bullets -> Enemy

        for (Bullet bullet : player.getBullets()) {

            if (!bullet.isBulletActive()) {
                continue;
            }

            for (int i = enemies.size() - 1; i >= 0; i--) {

                Enemy enemy = enemies.get(i);

                if (collisionDetector.collides(bullet.getPosition(), enemy.getPosition())) {

                    bullet.deactivate();

                    enemy.hit();

                    enemy.removeEnemyBullets();
                    enemy.removeEnemy();

                    enemies.remove(i);

                    enemiesKilled++;

                    tryScore += 50;

                    Score.setText("SCORE: " + tryScore);

                    enemyWasHit = true;

                    break;
                }
            }
        }



         // Enemy bullets -> Player

        for (Enemy enemy : enemies) {

            for (Bullet bullet : enemy.getBullets()) {

                if (!bullet.isBulletActive()) {
                    continue;
                }

                if (collisionDetector.collides(bullet.getPosition(), player.getPosition())) {

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



         // Player loses

        if (!player.isAlive()) {

            updateHighScore();

            System.out.println("Enemy wins!");
            System.out.println("Your score: " + tryScore);
            System.out.println("Highscore: " + highScore);

            showResult("resources/game_over.png");

            stop();

            return;
        }



         // Player wins

        if (enemyWasHit && enemiesCreated == 10 && enemies.isEmpty()) {

            updateHighScore();

            System.out.println("Player wins!");
            System.out.println("Your score: " + tryScore);
            System.out.println("Highscore: " + highScore);

            showResult("resources/youWin.png");

            stop();
        }
    }



     // Checks if the current score is a new High Score.

    private void updateHighScore() {

        if (tryScore > highScore) {

            highScore = tryScore;

            if (HighScore != null) {
                HighScore.setText("HIGHSCORE: " + highScore);
            }

            saveHighScore();
        }
    }


    public void showResult(String imagePath) {

        resultPicture = new Picture(0, 0, imagePath);

        int centerX = background.getWidth() / 2;
        int centerY = background.getHeight() / 2;

        resultPicture.translate(centerX - resultPicture.getWidth() / 2, centerY - resultPicture.getHeight() / 2);

        resultPicture.draw();


        int buttonY = centerY + resultPicture.getHeight() / 2 + 100;

        restartButton = new Button(centerX, buttonY, "resources/restart.png");

        state = State.GAME_OVER;
    }


    public void drawPlayerLives() {

        int spacing = 10;

        int startX = grid.columnToX(2);
        int startY = 20;

        for (int i = 0; i < player.getLives(); i++) {

            Picture lifeIcon = new Picture(startX, startY, "resources/playerLife.png");

            lifeIcon.draw();

            lifeIcons.add(lifeIcon);

            startX += lifeIcon.getWidth() + spacing;
        }
    }


    public void updatePlayerLivesDisplay() {

        while (lifeIcons.size() > player.getLives()) {

            tryScore -= 10;

            Score.setText("SCORE: " + tryScore);

            int lastIconIndex = lifeIcons.size() - 1;

            Picture lostLifeIcon = lifeIcons.remove(lastIconIndex);

            lostLifeIcon.delete();
        }
    }



     //Hidden Easy Mode

    public void activateEasyMode() {

        difficulty = new EasyMode();

        /*
         * Existing enemies also receive Easy Mode.
         */
        for (Enemy enemy : enemies) {
            enemy.setDifficulty(difficulty);
        }

        System.out.println("Easy Mode activated!");
    }


    @Override
    public void mouseClicked(MouseEvent e) {


         //Start

        if (state == State.MENU && startButton.isClicked(e.getX(), e.getY())) {

            startButton.remove();
            titlePicture.delete();

            startGame();

            return;
        }



         //Restart

        if (state == State.GAME_OVER
                && restartButton.isClicked(
                e.getX(),
                e.getY())) {

            cleanupPreviousGame();

            restartButton.remove();
            resultPicture.delete();

            startGame();
        }
    }


    @Override
    public void mouseMoved(MouseEvent e) {

    }


    private void cleanupPreviousGame() {


         //Remove player bullets.

        if (player != null) {

            for (Bullet bullet : player.getBullets()) {
                bullet.removeBullet();
            }

            player.getBullets().clear();



             //Remove player image.

             // The Player object itself is kept because
             // we reuse its Keyboard on restart.

            player.getPosition().remove();
        }



         //Remove all enemies and their bullets.

        for (Enemy enemy : enemies) {

            for (Bullet bullet : enemy.getBullets()) {
                bullet.removeBullet();
            }

            enemy.getBullets().clear();

            enemy.getPosition().remove();
        }

        enemies.clear();



         // Remove life icons.

        for (Picture lifeIcon : lifeIcons) {
            lifeIcon.delete();
        }

        lifeIcons.clear();



         //Remove score display.

         //HighScore is intentionally NOT deleted because
         // the value persists between games.

        if (Score != null) {

            Score.delete();
            Score = null;
        }


        enemiesCreated = 0;
        enemiesKilled = 0;
        lastSpawn = 0;

        running = false;
    }


    private void saveHighScore() {

        try {

            FileWriter writer = new FileWriter(HIGH_SCORE_FILE);

            writer.write(String.valueOf(highScore));

            writer.close();

            System.out.println("Saved high score: " + highScore);

        } catch (IOException e) {

            System.out.println("Could not save high score.");

            e.printStackTrace();
        }
    }


    private void loadHighScore() {

        try {

            File file =
                    new File(HIGH_SCORE_FILE);

            System.out.println("High score file:");
            System.out.println(file.getAbsolutePath());
            System.out.println("Does it exist? " + file.exists());


            BufferedReader reader = new BufferedReader(new FileReader(file));

            String line = reader.readLine();

            System.out.println("Read from file: " + line);


            if (line != null) {
                highScore = Integer.parseInt(line);
            }

            reader.close();

            System.out.println("Loaded high score: " + highScore);

        } catch (IOException e) {

            System.out.println("Could not load high score.");

            highScore = 0;
        }
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
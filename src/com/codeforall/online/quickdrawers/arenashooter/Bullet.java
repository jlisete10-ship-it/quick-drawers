package com.codeforall.online.quickdrawers.arenashooter;

public class Bullet implements Movable {

    private Position position;
    private Grid grid;
    private boolean bulletOn = true;
    private boolean movingRight;
    private int frameCounter = 0;
    private DifficultyStrategy difficulty;
    private boolean enemyBullet;
    private int moveDelay = 5;



     // Constructor for player bullets.

     // movingRight = true -> player bullet
        //movingRight = false -> enemy bullet

    public Bullet(Grid grid, Position shooterPosition, String imagePath, boolean movingRight) {

        this.grid = grid;
        this.movingRight = movingRight;
        this.difficulty = difficulty;
        this.enemyBullet = false;

        // Player bullet starts one column to the right of the player
        int startCol;

        if (movingRight) {
            startCol = shooterPosition.getCol() + 1;
        } else {
            startCol = shooterPosition.getCol() - 1;
        }

        // Same row as the shooter
        int startRow = shooterPosition.getRow();

        this.position = new Position(grid, startCol, startRow, imagePath);
    }


    /**
     * Constructor for enemy bullets.
     */
    public Bullet(
            Grid grid,
            Position shooterPosition,
            String imagePath,
            boolean movingRight,
            DifficultyStrategy difficulty) {

        this.grid = grid;
        this.movingRight = movingRight;
        this.difficulty = difficulty;
        this.enemyBullet = true;

        // Enemy bullet starts one column to the left of the enemy
        int startCol;

        if (movingRight) {
            startCol = shooterPosition.getCol() + 1;
        } else {
            startCol = shooterPosition.getCol() - 1;
        }

        // Same row as the shooter
        int startRow = shooterPosition.getRow();

        this.position = new Position(
                grid,
                startCol,
                startRow,
                imagePath
        );
    }


    @Override
    public void move() {

        frameCounter++;

        if (enemyBullet) {
            if (frameCounter < difficulty.getEnemyBulletMoveDelay()) {
                return;
            }
        }

        if (!enemyBullet) {
            if (frameCounter < moveDelay) {
                return;
            }
        }

        frameCounter = 0;

        boolean stillInsideGrid;

        if (movingRight) {
            stillInsideGrid = position.moveRight();
        } else {
            stillInsideGrid = position.moveLeft();
        }

        System.out.println(
                "Bullet move - col: "
                        + position.getCol()
                        + " active: "
                        + bulletOn
        );

        if (!stillInsideGrid) {
            deactivate();
        }
    }

    public boolean isBulletActive() {
        return bulletOn;
    }


    public void removeBullet() {
        position.removeBullet();
    }


    public Position getPosition() {
        return position;
    }


    public void deactivate() {

        if (!bulletOn) {
            return;
        }

        bulletOn = false;

        // Remove the bullet graphically from the screen
        position.removeBullet();
    }
}

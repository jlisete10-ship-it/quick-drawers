package com.codeforall.online.quickdrawers.arenashooter;

/**
 * Coordinates the initialization and execution of the game.
 *
 * The game loop repeatedly updates the game state while the game is running
 * Player, enemy, bullet and collision updates will be added during integration
 * Pseudocode:
 * INITIALIZE GAME
 * 1.Create the arena
 * 2.Draw the arena
 * START GAME
 * 1.Mark the game as running
 * 2.Reapeat while the game is running:
 *      a)Update the game state
 *      b)Pause before next update
 * STOP GAME
 * 1.Mark the game as no longer running
 * 2.Allow the loop to finish
 */
public class Game {

    private static final int FRAME_DELAY = 30;

    private Grid grid;
    private boolean running;
    private Player player;
    private Enemy enemy;

    public void init() {
        // prepara o jogo: cria a Grid, a posição inicial e o Player.
        grid= new Grid(100, 100);
        grid.init();
        /**
         * Adding enemy and player
         */
        Position playerPosition = new Position(grid, 0,0, "/playerShip.png");
        player = new Player(playerPosition);

        Position enemyPosition = new Position(grid, grid.getCols() - 8, 0, "/Enemy.png");
        enemy = new Enemy(grid, enemyPosition);


    }

    public void start() {
        //inicia o jogo e mantém o loop.
        running = true;

        while (running) {
            update();
            pauseGameLoop();
        }
    }
    /**
     * Future integration will:
     * 1. Update player and enemy movement.
     * 2. Update active bullets.
     * 3. Remove bullets that reach an arena boundary.
     * 4. Check collisions.
     * 5. Stop the game when a player is defeated.
     */
    private void update() {
    // faz aquilo que deve ser atualizado a cada frame: movimento do Player, depois Enemy, Bullets, colisões,
            //Player, enemy, bullets, and collisions wil be updated here during integration

        player.move();
        enemy.move();

        /**
         * Player bullets
         */
        for(int i=player.getBullets().size()-1; i>=0; --i){ // se começasse com i=0, depois na proxima iteração nao iria aceder ao novo elemento do indice 0 mas sim ao indice 1
            // por isso é que estou a começar pelo ultimo indice do array, para ir removendo as balas com segurança
            if(player.getBullets().get(i).isBulletActive()) {//
                player.getBullets().get(i).move();
            }
            else {
                player.getBullets().get(i).removeBullet();// remove graficamente

                player.getBullets().remove(i);// remove da lista

            }
        }
        /**
         * Enemy bullets
         */
        enemy.moveBullets();


    }

    private void pauseGameLoop () {
        try {
            Thread.sleep(FRAME_DELAY);
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            running = false;
        }
    }

    public void stop() {
        running = false;
    }

}

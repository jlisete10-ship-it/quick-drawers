package com.codeforall.online.quickdrawers.arenashooter;

import com.codeforall.simplegraphics.keyboard.Keyboard;
import com.codeforall.simplegraphics.keyboard.KeyboardEvent;
import com.codeforall.simplegraphics.keyboard.KeyboardEventType;
import com.codeforall.simplegraphics.keyboard.KeyboardHandler;

import java.util.ArrayList;
import java.util.List;

public class Player implements Shootable, Movable, KeyboardHandler {

    private Keyboard keyboard;
    private Directions direction;
    private Position position;
    private static final int INITIAL_LIVES = 3;
    private int lives = INITIAL_LIVES;
    private List<Bullet> bullets = new ArrayList<>();
    private Game game;
    private static final int PLAYER_MOVE_DELAY = 3;
    private int frameCounter = 0;


    public Player(Position position, Game game) {
        this.keyboard = new Keyboard(this);
        this.position = position;
        this.game = game;
        initKeys();
    }

    public void initKeys() {

        KeyboardEvent up = new KeyboardEvent();
        up.setKey(KeyboardEvent.KEY_UP);
        up.setKeyboardEventType(KeyboardEventType.KEY_PRESSED);

        keyboard.addEventListener(up);

        KeyboardEvent down = new KeyboardEvent();
        down.setKey(KeyboardEvent.KEY_DOWN);
        down.setKeyboardEventType(KeyboardEventType.KEY_PRESSED);

        keyboard.addEventListener(down);

        KeyboardEvent upReleased = new KeyboardEvent();
        upReleased.setKey(KeyboardEvent.KEY_UP);
        upReleased.setKeyboardEventType(KeyboardEventType.KEY_RELEASED);

        keyboard.addEventListener(upReleased);

        KeyboardEvent downReleased = new KeyboardEvent();
        downReleased.setKey(KeyboardEvent.KEY_DOWN);
        downReleased.setKeyboardEventType(KeyboardEventType.KEY_RELEASED);

        keyboard.addEventListener(downReleased);

        // for bullet
        KeyboardEvent space = new KeyboardEvent();
        space.setKey(KeyboardEvent.KEY_SPACE);
        space.setKeyboardEventType(KeyboardEventType.KEY_PRESSED);

        keyboard.addEventListener(space);

        // for hidden easy mode
        KeyboardEvent x = new KeyboardEvent();
        x.setKey(KeyboardEvent.KEY_X);
        x.setKeyboardEventType(KeyboardEventType.KEY_PRESSED);

        keyboard.addEventListener(x);


    }

    public void setDirection(Directions direction) {
        this.direction = direction;
    }


    @Override
    public void move() {

        frameCounter++;

        if (frameCounter < PLAYER_MOVE_DELAY) {
            return;
        }

        frameCounter = 0;
        if (direction == null) {
            return;
        }
        switch (direction) {

            case UP:
                this.position.moveUp();
                break;

            case DOWN:
                position.moveDown();
                break;

        }
    }
    @Override
    public void keyPressed (KeyboardEvent keyboardEvent) {
        if (keyboardEvent.getKey() == KeyboardEvent.KEY_UP) {// here I am associating the UP arrow key to the direction up
            setDirection(Directions.UP);
            return;


        }
        if (keyboardEvent.getKey() == KeyboardEvent.KEY_DOWN) {
            setDirection(Directions.DOWN);
            return;
        }
        if (keyboardEvent.getKey() == KeyboardEvent.KEY_SPACE) {// here I am associating the space arrow key to the keyboard space

            shoot();
            return;


        }
        if (keyboardEvent.getKey() == KeyboardEvent.KEY_X) {// here I am associating the UP arrow key to the direction up
           game.activateEasyMode();
            return;


        }
    }

    @Override
    public void keyReleased (KeyboardEvent keyboardEvent){
        setDirection(null);// if I release the keyboard player stops

    }

    @Override
    public void shoot () {

        /**
         * Lorenzo: alteração com picture
         */
        Bullet bullet = new Bullet(position.getGrid(), this.position, "resources/playerBullet.png", true);
        bullets.add(bullet);
    }

    public List<Bullet>  getBullets() {
        return bullets;
    }

    public Position getPosition() {
        return position;
    }

    public void movePlayerBullets(){
        for(int i=this.getBullets().size()-1; i>=0; --i){
            //Iterate backwards so bullets can be safely removed without affecting the remaining indices.
            if(this.getBullets().get(i).isBulletActive()) {//
                this.getBullets().get(i).move();
            }
            else {
                this.getBullets().get(i).removeBullet();// remove graphically

                this.getBullets().remove(i);// remove from list

            }
        }
    }
    public int getLives() {
        return lives;
    }

    public boolean isAlive() {
        return lives >0;
    }

    public boolean hit() {

        // Prevent the number of lives from becoming negative
        if (lives > 0) {
            lives--;
        }

        System.out.println("Player lives: " + lives);

        return isAlive();
    }


}
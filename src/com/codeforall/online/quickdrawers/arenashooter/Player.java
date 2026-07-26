package com.codeforall.online.quickdrawers.arenashooter;

import com.codeforall.simplegraphics.keyboard.Keyboard;
import com.codeforall.simplegraphics.keyboard.KeyboardEvent;
import com.codeforall.simplegraphics.keyboard.KeyboardEventType;
import com.codeforall.simplegraphics.keyboard.KeyboardHandler;

public class Player implements Shootable, Movable, KeyboardHandler {

    private Keyboard keyboard;
    private Directions direction;



    public Player(){

        this.keyboard = new Keyboard(this);
        initKeys();

    }

    public void initKeys(){
        KeyboardEvent up = new KeyboardEvent();
        up.setKey(KeyboardEvent.KEY_UP);
        up.setKeyboardEventType(KeyboardEventType.KEY_PRESSED);

        keyboard.addEventListener(up);

        KeyboardEvent down = new KeyboardEvent();
        down.setKey(KeyboardEvent.KEY_DOWN);
        down.setKeyboardEventType(KeyboardEventType.KEY_PRESSED);

        keyboard.addEventListener(down);

    }

    public Directions setDirection(Directions direction){

        return this.direction;

    }
    @Override
    public void keyPressed(KeyboardEvent keyboardEvent) {
        if(keyboardEvent.getKey() == KeyboardEvent.KEY_UP){
            setDirection(Directions.UP);
        }

        if(keyboardEvent.getKey() == KeyboardEvent.KEY_DOWN){
            setDirection(Directions.DOWN);
        }

    }

    @Override
    public void keyReleased(KeyboardEvent keyboardEvent) {

    }

    @Override
    public void move() {


    }

    @Override
    public void shoot() {

    }
}

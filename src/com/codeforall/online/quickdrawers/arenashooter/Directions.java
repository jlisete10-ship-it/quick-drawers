package com.codeforall.online.quickdrawers.arenashooter;

public enum Directions {

    UP("moving up"),
    DOWN("moving down");


    private  final String description;

    Directions(String description){
        this.description = description;
    }

    public Directions getDirection(Directions direction){
        return this;
    }




}

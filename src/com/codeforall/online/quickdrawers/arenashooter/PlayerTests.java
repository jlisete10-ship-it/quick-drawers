package com.codeforall.online.quickdrawers.arenashooter;


public class PlayerTests {

    public static void main(String[] args) throws InterruptedException {

        Grid field = new Grid(100, 100);
        field.init();
        Position position = new Position(field, 0,0, "/playerShip.png");
        Player player = new Player(position);

        while (true) {

            // Pause for a while
            Thread.sleep(150);
            player.move();
            player.shoot();
         }


    }
}

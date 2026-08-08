package com.codeforall.online.quickdrawers.arenashooter;

public class NormalMode implements  DifficultyStrategy{


    @Override
    public int getEnemyMoveDelay() {
        return 4;
    }

    @Override
    public int getEnemyBulletMoveDelay() {
        return 4;
    }
}

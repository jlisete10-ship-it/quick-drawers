package com.codeforall.online.quickdrawers.arenashooter;

public class EasyMode implements DifficultyStrategy{



    @Override
    public int getEnemyMoveDelay() {
        return 12;
    }

    @Override
    public int getEnemyBulletMoveDelay() {
        return 5;
    }

    @Override
    public int getEnemySpawnDelay() {
        return 2500;
    }

}


package com.codeforall.online.quickdrawers.arenashooter;

public interface DifficultyStrategy {

    int getEnemyMoveDelay();
    int getEnemyBulletMoveDelay();
    int getEnemySpawnDelay();
}

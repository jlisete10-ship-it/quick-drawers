package com.codeforall.online.quickdrawers.arenashooter;

public class CollisionDetector {

    public boolean collides(Position first, Position second) {
        return first != null && second != null && overlaps(first, second);
    }

    private boolean overlaps(Position first, Position second) {
        //com este método vou passando por parametros as posições dos objetos(bala do player e posição do enemy e vice versa)

        return first.getCollisionX() < second.getCollisionX() + second.getCollisionWidth()
                && first.getCollisionX() + first.getCollisionWidth() > second.getCollisionX()
                && first.getCollisionY() < second.getCollisionY() + second.getCollisionHeight()
                && first.getCollisionY() + first.getCollisionHeight() > second.getCollisionY();
        }




}

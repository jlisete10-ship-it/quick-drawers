package com.codeforall.online.quickdrawers.arenashooter;

import com.codeforall.simplegraphics.pictures.Picture;

public class Button {

    private Picture picture;
    private double x;
    private double y;
    private int width;
    private int height;

    public Button(int centerX, int centerY, String imagePath){
        this.picture = new Picture(0, 0, imagePath);
        picture.draw();

        this.width = picture.getWidth();
        this.height = picture.getHeight();

        this.x = centerX - width / 2.0;
        this.y = centerY - height / 2.0;

        picture.translate((int) x, (int) y);
    }

    public boolean isClicked(double mouseX, double mouseY){
        return mouseX >= x && mouseX <= x + width
                && mouseY >= y && mouseY <= y + height;
    }

    public void remove(){
        picture.translate(-5000, -5000);
        picture.delete();
    }
}

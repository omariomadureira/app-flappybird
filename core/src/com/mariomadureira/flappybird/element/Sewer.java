package com.mariomadureira.flappybird.element;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.mariomadureira.flappybird.config.Device;
import sun.rmi.runtime.Log;

public class Sewer {
    private Image image;

    private float positionX;
    private float positionY;

    private boolean rotated;

    public Sewer() {
        Texture texture = new Texture("sewer.png");
        image = new Image(texture);
    }

    //region Getters and Setters
    public Image getImage() {
        image.setPosition(positionX, positionY);
        return image;
    }

    public float getPositionX() {
        return positionX;
    }

    public void setPositionX(float positionX) {
        this.positionX = positionX;
    }

    public float getPositionY() {
        return positionY;
    }

    public void setPositionY(float positionY) {
        this.positionY = positionY;
    }

    public boolean isRotated() {
        return rotated;
    }

    public void setRotated(boolean rotated) {
        rotated = rotated;
    }
    //endregion

    public void move() {
        float deltaTime = Gdx.graphics.getDeltaTime();
        positionX = positionX - deltaTime * 200;
    }

    public void rotate() {
        image.rotateBy(180);
        rotated = true;
    }

    public boolean isGone() {
        return positionX < -image.getWidth();
    }

    public boolean isTouched(Bird bird, Device device) {
        boolean touched = false;

        if (rotated) {
            float sewerHeightOut = positionY - device.getHeight();
            float sewerHeightIn = image.getHeight() - sewerHeightOut;
            float sewerPositionY = device.getHeight() - sewerHeightIn - bird.getTexture(0).getHeight();

            if (bird.getPositionY() >= sewerPositionY) {
                float sewerPositionX = positionX - image.getWidth() - bird.getTexture(0).getWidth();
                float sewerWidthPositionX = sewerPositionX + image.getWidth();

                if (bird.getPositionX() >= sewerPositionX && bird.getPositionX() <= sewerWidthPositionX) {
                    touched = true;
                }
            }
        } else {
            float sewerHeightOut = 0 - positionY;
            float sewerPositionY = image.getHeight() - sewerHeightOut;

            if (bird.getPositionY() <= sewerPositionY) {
                float sewerPositionX = positionX - bird.getTexture(0).getWidth();
                float sewerWidthPositionX = sewerPositionX + image.getWidth();

                if (bird.getPositionX() >= sewerPositionX && bird.getPositionX() <= sewerWidthPositionX) {
                    touched = true;
                }
            }
        }

        return touched;
    }

    public boolean isTouched(Coin coin, Device device) {
        boolean touched = false;

        if (rotated) {
            float sewerHeightOut = positionY - device.getHeight();
            float sewerHeightIn = image.getHeight() - sewerHeightOut;
            float sewerPositionY = device.getHeight() - sewerHeightIn - coin.getTexture(0).getHeight();

            if (coin.getPositionY() >= sewerPositionY) {
                float sewerPositionX = positionX - image.getWidth() - coin.getTexture(0).getWidth();
                float sewerWidthPositionX = sewerPositionX + image.getWidth();

                if (coin.getPositionX() >= sewerPositionX && coin.getPositionX() <= sewerWidthPositionX) {
                    touched = true;
                }
            }
        } else {
            float sewerHeightOut = 0 - positionY;
            float sewerHeightIn = image.getHeight() - sewerHeightOut;

            if (coin.getPositionY() <= sewerHeightIn) {
                float sewerPositionX = positionX - coin.getTexture(0).getWidth();
                float sewerWidthPositionX = sewerPositionX + image.getWidth();

                if (coin.getPositionX() >= sewerPositionX && coin.getPositionX() <= sewerWidthPositionX) {
                    touched = true;
                }
            }
        }

        return touched;
    }
}

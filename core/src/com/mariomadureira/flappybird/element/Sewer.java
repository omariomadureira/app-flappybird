package com.mariomadureira.flappybird.element;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.mariomadureira.flappybird.config.Device;
import sun.rmi.runtime.Log;

public class Sewer {
    private Rectangle body;
    private Image image;

    private float positionX;
    private float positionY;

    public Sewer() {
        Texture texture = new Texture("sewer.png");
        image = new Image(texture);
        body = new Rectangle();
    }

    //region Getters and Setters
    public Rectangle getBody() {
        return body;
    }

    private void setBody() {
        body = new Rectangle(
                positionX, positionY,
                image.getWidth(), image.getHeight()
        );
    }

    public Image getImage() {
        image.setPosition(positionX, positionY);
        return image;
    }

    public float getPositionX() {
        return positionX;
    }

    public void setPositionX(float positionX) {
        this.positionX = positionX;
        body.setX(positionX);
    }

    public float getPositionY() {
        return positionY;
    }

    public void setPositionY(float positionY) {
        this.positionY = positionY;
        body.setY(positionY);
    }
    //endregion

    public void move() {
        float deltaTime = Gdx.graphics.getDeltaTime();
        positionX = positionX - deltaTime * 200;
        setBody();
    }

    public void rotate() {
        image.rotateBy(180);
    }

    public boolean isGone() {
        return positionX < -image.getWidth();
    }
}

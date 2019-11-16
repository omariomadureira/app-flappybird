package com.mariomadureira.flappybird.element;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Circle;

public class Bird {
    private Texture[] texture;
    private float textureVariation;
    private Circle body;

    private int speed;

    private float positionY;
    private float positionX;

    public Bird() {
        texture = new Texture[3];
        texture[0] = new Texture("bird1.png");
        texture[1] = new Texture("bird2.png");
        texture[2] = new Texture("bird3.png");
        textureVariation = 0;
        body = new Circle();
        speed = 0;
    }

    //region Getters and Setters
    public Texture getTexture(int index) {
        if (index > texture.length - 1)
            index = 0;

        return texture[index];
    }

    public Circle getBody() {
        return body;
    }

    private void setBody() {
        float halfTextureX = (float) getTexture(0).getWidth() / 2;
        float halfTextureY = (float) getTexture(0).getHeight() / 2;

        body.set(positionX + halfTextureX,
                positionY + halfTextureY,
                halfTextureX);
    }

    public void setPositionY(float positionY) {
        this.positionY = positionY;
    }

    public float getPositionY() {
        return positionY;
    }

    public void setPositionX(float positionX) {
        this.positionX = positionX;
    }

    public float getPositionX() {
        return positionX;
    }
    //endregion

    public Texture animate() {
        float deltaTime = Gdx.graphics.getDeltaTime();
        textureVariation = textureVariation + deltaTime * 10;

        if (textureVariation > texture.length - 1)
            textureVariation = 0;

        return getTexture((int) textureVariation);
    }

    public void fly() {
        float deltaTime = Gdx.graphics.getDeltaTime();
        positionX = positionX + deltaTime * 300;
        setBody();
    }

    public void fall() {
        speed = speed + 1;

        if (positionY > 0 || speed < 0)
            positionY = positionY - speed;

        setBody();
    }

    public void rise() {
        speed = -15;
    }

    public void die() {
        speed = 0;
    }
}
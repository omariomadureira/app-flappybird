package com.mariomadureira.flappybird.element;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Circle;
import com.mariomadureira.flappybird.config.Device;

public class Coin {
    private Texture[] texture;
    private float textureVariation;
    private Circle body;

    private float positionY;
    private float positionX;

    public Coin() {
        textureVariation = 0;
        texture = new Texture[7];
        texture[0] = new Texture("coin1.png");
        texture[1] = new Texture("coin2.png");
        texture[2] = new Texture("coin3.png");
        texture[3] = new Texture("coin4.png");
        texture[4] = new Texture("coin5.png");
        texture[5] = new Texture("coin6.png");
        texture[6] = new Texture("coin7.png");
        body = new Circle();
        positionX = new Device().getWidth();
    }

    //region Getters and Setters
    public Texture getTexture(int index) {
        if (index > texture.length - 1)
            index = 0;

        return texture[index];
    }

    private void setBody() {
        float halfTextureX = (float) getTexture(0).getWidth() / 2;
        float halfTextureY = (float) getTexture(0).getHeight() / 2;

        body.set(positionX + halfTextureX,
                positionY + halfTextureY,
                halfTextureX);
    }

    public Circle getBody() {
        return body;
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
        textureVariation = textureVariation + deltaTime * 5;

        if (textureVariation > texture.length - 1)
            textureVariation = 0;

        return getTexture((int) textureVariation);
    }

    public void move() {
        float deltaTime = Gdx.graphics.getDeltaTime();
        positionX = positionX - deltaTime * 200;
        setBody();
    }

    public boolean isGone() {
        return positionX < -texture[0].getWidth();
    }
}
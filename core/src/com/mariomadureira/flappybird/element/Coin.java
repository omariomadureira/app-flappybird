package com.mariomadureira.flappybird.element;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Circle;

public class Coin
{
    private Texture[] texture;
    private float textureVariation;

    private Circle circle;

    private float positionY;
    private float positionX;

    public Coin()
    {
        texture = new Texture[7];
        texture[0] = new Texture("coin1.png");
        texture[1] = new Texture("coin2.png");
        texture[2] = new Texture("coin3.png");
        texture[3] = new Texture("coin4.png");
        texture[4] = new Texture("coin5.png");
        texture[5] = new Texture("coin6.png");
        texture[6] = new Texture("coin7.png");
        textureVariation = 0;
        circle = new Circle();
    }

    public Texture texture(int index)
    {
        if (index > texture.length - 1)
            index = 0;

        return texture[index];
    }

    public Texture turn()
    {
        float deltaTime = Gdx.graphics.getDeltaTime();
        textureVariation = textureVariation + deltaTime * 5;

        if (textureVariation > texture.length - 1)
            textureVariation = 0;

        return texture((int) textureVariation);
    }

    public void move()
    {
        float deltaTime = Gdx.graphics.getDeltaTime();
        positionX = positionX - deltaTime * 200;
        setBody();
    }

    public void setBody()
    {
        float halfTextureX = texture(0).getWidth() / 2;
        float halfTextureY = texture(0).getHeight() / 2;

        circle.set(positionX + halfTextureX,
                positionY + halfTextureY,
                halfTextureX);
    }

    public Circle body()
    {
        return circle;
    }

    public void positionY(float positionY) {
        this.positionY = positionY;
    }

    public float positionY() {
        return positionY;
    }

    public void positionX(float positionX) {
        this.positionX = positionX;
    }

    public float positionX() {
        return positionX;
    }
}
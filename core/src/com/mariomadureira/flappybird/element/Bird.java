package com.mariomadureira.flappybird.element;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Circle;

public class Bird
{
    private Texture[] texture;
    private float textureVariation;

    private Circle circle;

    private int speed;
    private float positionY;
    private float positionX;

    public Bird()
    {
        texture = new Texture[3];
        texture[0] = new Texture("passaro1.png");
        texture[1] = new Texture("passaro2.png");
        texture[2] = new Texture("passaro3.png");
        textureVariation = 0;
        circle = new Circle();
        speed = 0;
    }

    public Texture texture(int index)
    {
        if (index > texture.length - 1)
            index = 0;

        return texture[index];
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

    public Texture fly()
    {
        float deltaTime = Gdx.graphics.getDeltaTime();
        textureVariation = textureVariation + deltaTime * 10;

        if (textureVariation > texture.length - 1)
            textureVariation = 0;

        return texture((int) textureVariation);
    }

    public void move()
    {
        float deltaTime = Gdx.graphics.getDeltaTime();
        positionX = positionX + deltaTime * 300;
        setBody();
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

    public void fall()
    {
        speed = speed + 1;

        if (positionY > 0 || speed < 0)
            positionY = positionY - speed;

        setBody();
    }

    public void rise()
    {
        speed = - 15;
    }

    public void die()
    {
        speed = 0;
    }
}
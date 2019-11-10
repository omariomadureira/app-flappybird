package com.mariomadureira.flappybird.element;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

public class Sewer
{
    private Texture texture;
    private Rectangle rectangle;

    private float positionX;
    private float positionY;

    public Sewer(String type)
    {
        if (type.equals("down"))
            texture = new Texture("cano_baixo.png");
        else if (type.equals("up"))
            texture = new Texture("cano_topo.png");

        rectangle = new Rectangle();
    }

    public Texture texture()
    {
        return texture;
    }

    public void setRectangle()
    {
        rectangle = new Rectangle(
            positionX, positionY,
            texture.getWidth(), texture.getHeight()
        );
    }

    public Rectangle body()
    {
        return rectangle;
    }

    public float positionX() {
        return positionX;
    }

    public void positionX(float positionX) {
        this.positionX = positionX;
    }

    public float positionY() {
        return positionY;
    }

    public void positionY(float positionY) {
        this.positionY = positionY;
    }

    public void move()
    {
        float deltaTime = Gdx.graphics.getDeltaTime();
        positionX = positionX - deltaTime * 200;
        setRectangle();
    }

    public boolean inScreen()
    {
        return positionX >= -texture.getWidth();
    }
}

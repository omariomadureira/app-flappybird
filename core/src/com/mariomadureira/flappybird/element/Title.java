package com.mariomadureira.flappybird.element;

import com.badlogic.gdx.graphics.Texture;

public class Title
{
    private Texture texture;

    public Title(String name)
    {
        if (name.equals("gameOver"))
            texture = new Texture("game_over.png");
    }

    public Texture texture()
    {
        return texture;
    }
}

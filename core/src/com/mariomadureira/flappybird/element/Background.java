package com.mariomadureira.flappybird.element;

import com.badlogic.gdx.graphics.Texture;

public class Background
{
    private Texture texture;

    public Background()
    {
        texture = new Texture("fundo.png");
    }

    public Texture texture()
    {
        return texture;
    }
}

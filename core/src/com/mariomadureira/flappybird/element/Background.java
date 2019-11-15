package com.mariomadureira.flappybird.element;

import com.badlogic.gdx.graphics.Texture;

public class Background
{
    private Texture texture;

    public Background(int phase)
    {
        switch (phase)
        {
            case 1:
                texture = new Texture("fundo-1.png");
                break;
            case 2:
                texture = new Texture("fundo-2.png");
                break;
        }
    }

    public Texture texture()
    {
        return texture;
    }
}

package com.mariomadureira.flappybird.element;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class Text
{
    private BitmapFont font;

    public Text(int size)
    {
        font = new BitmapFont();
        font.setColor(Color.WHITE);
        font.getData().setScale(size);
    }

    public BitmapFont get()
    {
        return font;
    }
}

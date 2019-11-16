package com.mariomadureira.flappybird.element;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class Time {
    private BitmapFont font;
    private int value;

    private int count;
    private float countVariation;

    public Time(int value) {
        font = new BitmapFont();
        font.setColor(Color.WHITE);
        font.getData().setScale(3);
        this.value = value;
        count = value;
        countVariation = value;
    }

    //region Getters
    public int get() {
        return count;
    }

    public BitmapFont getTitle() {
        return font;
    }
    //endregion

    public void loss() {
        float deltaTime = Gdx.graphics.getDeltaTime();
        countVariation = countVariation - deltaTime;

        if (countVariation < 1)
            countVariation = 0;

        count = (int) countVariation;
    }

    public void clean() {
        count = value;
        countVariation = value;
    }
}

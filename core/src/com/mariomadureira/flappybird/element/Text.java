package com.mariomadureira.flappybird.element;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

public class Text {
    private BitmapFont font;
    private float width;
    private String text;

    public Text(String text) {
        this.text = text;
    }

    //region Getters and Setters
    public BitmapFont get() {
        return font;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
    //endregion

    public void setTitle(int size) {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("font.ttf"));

        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = size;

        font = generator.generateFont(parameter);
        font.setColor(Color.WHITE);

        generator.dispose();
    }

    public void setSubtitle(int size) {
        font = new BitmapFont();
        font.setColor(Color.WHITE);
        font.getData().setScale(size);
    }

    public void calculateWidth() {
        this.width = new GlyphLayout(font, text).width;
    }
}

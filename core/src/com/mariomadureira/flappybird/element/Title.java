package com.mariomadureira.flappybird.element;

import com.badlogic.gdx.graphics.Texture;

public class Title {
    private Texture texture;

    public Title(String type) {
        if (type.equals("gameOver"))
            texture = new Texture("gameover.png");
    }

    public Texture getTexture() {
        return texture;
    }
}

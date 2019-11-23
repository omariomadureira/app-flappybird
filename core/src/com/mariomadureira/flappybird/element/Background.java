package com.mariomadureira.flappybird.element;

import com.badlogic.gdx.graphics.Texture;

public class Background {
    private Texture texture;

    public Background(int phase) {
        switch (phase) {
            case 1:
                texture = new Texture("background1.png");
                break;
            case 2:
                texture = new Texture("background2.png");
                break;
            case 3:
                texture = new Texture("background3.png");
                break;
            case 4:
                texture = new Texture("background4.png");
                break;
            default:
                texture = new Texture("background0.png");
        }
    }

    public Texture getTexture() {
        return texture;
    }
}

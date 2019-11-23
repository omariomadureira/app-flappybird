package com.mariomadureira.flappybird.phase;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.mariomadureira.flappybird.config.Device;
import com.mariomadureira.flappybird.element.*;

import java.util.Random;

public class Main {
    private Device device;
    private SpriteBatch batch;
    private Stage stage;

    private Background background;
    private TextButton button;

    private int status;

    public Main() {
        device = new Device();
        batch = new SpriteBatch();

        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        background = new Background(0);

        Text playText = new Text("jogar");
        playText.setTitle(120);
        playText.calculateWidth();

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = playText.get();

        button = new TextButton(playText.getText(), textButtonStyle);
        button.setPosition(device.getWidth() / 3 - playText.getWidth() / 2, 150);
        stage.addActor(button);

        status = 0;
    }

    public SpriteBatch getBatch() {
        return batch;
    }

    public boolean isPlayPressed() {
        return status == 1;
    }

    private void draw() {
        batch.begin();
        batch.draw(background.getTexture(), 0, 0, device.getWidth(), device.getHeight());
        batch.end();
        stage.draw();
    }

    public void render() {
        button.addListener(new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {
                status = 1;
            }
        });

        draw();
    }
}
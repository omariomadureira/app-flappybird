package com.mariomadureira.flappybird.phase;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mariomadureira.flappybird.config.Device;
import com.mariomadureira.flappybird.element.Background;
import com.mariomadureira.flappybird.element.Bird;
import com.mariomadureira.flappybird.element.Text;

public class EndGame {
    private Device device;
    private SpriteBatch batch;

    private Background background;
    private Bird bird;
    private Text scoreText;
    private Text congratulations;

    public EndGame(int score) {
        device = new Device();
        batch = new SpriteBatch();

        background = new Background(5);

        bird = new Bird();
        bird.setPositionX(-120);
        bird.setPositionY(device.getHeight() / 2);

        scoreText = new Text("Pontos: " + score);
        scoreText.setTitle(120);
        scoreText.calculateWidth();

        congratulations = new Text("Fim de jogo");
        congratulations.setTitle(100);
        congratulations.calculateWidth();

        score = 0;
    }

    public SpriteBatch getBatch() {
        return batch;
    }

    private void draw() {
        batch.begin();

        batch.draw(background.getTexture(), 0, 0, device.getWidth(), device.getHeight());
        batch.draw(bird.animate(), bird.getPositionX(), bird.getPositionY());

        double textHeight = device.getHeight() * 0.20;
        scoreText.get().draw(batch, scoreText.getText(),
                device.getWidth() / 2 - scoreText.getWidth() / 2, (float) textHeight);

        textHeight = device.getHeight() * 0.90;
        congratulations.get().draw(batch, congratulations.getText(),
                device.getWidth() / 2 - congratulations.getWidth() / 2, (float) textHeight);

        batch.end();
    }

    public void render() {
        float deltaTime = Gdx.graphics.getDeltaTime();
        float positionX = bird.getPositionX() + deltaTime * 50;
        bird.setPositionX(positionX);

        draw();
    }
}
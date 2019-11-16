package com.mariomadureira.flappybird.phase;

import com.mariomadureira.flappybird.element.*;
import com.mariomadureira.flappybird.config.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;

import java.util.Random;

public class PhaseOne {
    private Device device;
    private SpriteBatch batch;

    private Background background;
    private Title gameOver;
    private Text text;
    private Time time;
    private int score;

    private Bird bird;
    private Sewer sewer;
    private Coin[] coins;
    private Tree tree;

    private int state;

    public PhaseOne() {
        device = new Device();
        batch = new SpriteBatch();

        background = new Background(1);
        gameOver = new Title("gameOver");
        text = new Text(3);
        time = new Time(45);

        bird = new Bird();
        bird.setPositionX(120);
        sewer = new Sewer();
        coins = new Coin[]
                {
                        new Coin(),
                        new Coin(),
                        new Coin(),
                        new Coin()
                };
        tree = new Tree();
        tree.setPositionX(device.getWidth());
        tree.setPositionY(-20);

        state = 0;

        begin();
    }

    //region Getters and Setters
    public SpriteBatch getBatch() {
        return batch;
    }

    public int getScore() {
        return score;
    }
    //endregion

    private void begin() {
        score = 0;
        time.clean();

        bird.die();
        bird.setPositionY(device.getHeight() / 2);

        float sewerHeight = device.getHeight() / 2 - sewer.getImage().getHeight();
        sewer.setPositionY(sewerHeight);
        sewer.setPositionX(device.getWidth());

        Coin previous = coins[0];

        for (int i = 0; i < coins.length; i++) {
            if (i > 0) {
                coins[i].setPositionX(previous.getPositionX() + device.getWidth() / 2);
            }

            coins[i].setPositionY(sewerHeight + sewer.getImage().getHeight() + 150);

            previous = coins[i];
        }
    }

    private void finish() {
        bird.fly();

        int treePartialWidth = (tree.getTexture().getWidth() * 70) / 100;
        int treeFinalWidth = (int) device.getWidth() - treePartialWidth;

        if (tree.getPositionX() > treeFinalWidth) {
            tree.move();
        }

        if (bird.getPositionX() >= device.getWidth()) {
            state = 4;
        }
    }

    public boolean isFinished() {
        if (state == 5) {
            return true;
        }

        return false;
    }

    private void draw() {
        batch.begin();

        batch.draw(background.getTexture(), 0, 0, device.getWidth(), device.getHeight());
        sewer.getImage().draw(batch, device.getWidth());
        batch.draw(bird.animate(), bird.getPositionX(), bird.getPositionY());

        for (Coin coin : coins) {
            batch.draw(coin.animate(), coin.getPositionX(), coin.getPositionY());
        }

        String timeText = "Tempo: " + time.get();
        time.getTitle().draw(batch, timeText, device.getWidth() - 250, device.getHeight() - 50);

        String scoreText = "Pontos: " + score;
        time.getTitle().draw(batch, scoreText, device.getWidth() - 500, device.getHeight() - 50);

        if (state == 0) {
            text.get().draw(batch, "Fase 1: Toque para começar!",
                    device.getWidth() / 2 - 270, device.getHeight() / 2 - gameOver.getTexture().getHeight());
        } else if (state == 2) {
            text.get().draw(batch, "Fase 1: Toque para reiniciar!",
                    device.getWidth() / 2 - 270, device.getHeight() / 2 - gameOver.getTexture().getHeight());
            batch.draw(gameOver.getTexture(), device.getWidth() / 2 - (float) gameOver.getTexture().getWidth() / 2,
                    device.getHeight() / 2);
        } else if (state == 3) {
            batch.draw(tree.getTexture(), tree.getPositionX(), tree.getPositionY());
        } else if (state > 3) {
            batch.draw(tree.getTexture(), tree.getPositionX(), tree.getPositionY());
            text.get().draw(batch, "Você passou de fase!",
                    device.getWidth() / 2 - 215, device.getHeight() / 2);
            text.get().draw(batch, "Toque para continuar...",
                    device.getWidth() / 2 - 215, device.getHeight() / 2 - 40);
        }

        batch.end();
    }

    public void render() {
        if (state == 0) {
            if (Gdx.input.justTouched()) {
                state = 1;
            }
        } else if (state == 1) {
            bird.fall();
            sewer.move();
            time.loss();

            if (Gdx.input.justTouched()) {
                bird.rise();
            }

            if (sewer.isGone() && time.get() > 0) {
                int sewerImageHeight = (int) sewer.getImage().getHeight();
                int sewerMaxHeight = (int) device.getHeight() - sewerImageHeight - bird.getTexture(0).getHeight() - 200;
                int sewerMinHeight = -600;
                int sewerRandomHeight = new Random().nextInt(sewerMaxHeight - sewerMinHeight) + sewerMinHeight;

                sewer.setPositionY(sewerRandomHeight);
                sewer.setPositionX(device.getWidth());

                for (Coin coin : coins) {
                    if (Intersector.overlaps(coin.getBody(), sewer.getBody())) {
                        sewer.setPositionY(sewer.getPositionY() - 200);
                    }
                }
            }

            Coin previous = coins[0];

            for (Coin coin : coins) {
                if (previous.getPositionX() >= device.getWidth() * 0.6 || !coin.isGone()) {
                    coin.move();
                }

                if (coin.isGone() && time.get() > 0) {
                    coin.setPositionX(device.getWidth());

                    int sewerImageHeight = (int) sewer.getImage().getHeight();
                    int sewerMaxHeight = (int) device.getHeight() - sewerImageHeight - bird.getTexture(0).getHeight() - 200;
                    int coinMaxHeight = sewerMaxHeight + sewerImageHeight + 150;
                    int coinMinHeight = (int) sewer.getPositionY() + sewerImageHeight + 100;
                    int coinRandomHeight = new Random().nextInt(coinMaxHeight - coinMinHeight) + coinMinHeight;

                    coin.setPositionY(coinRandomHeight);
                }

                if (Intersector.overlaps(bird.getBody(), coin.getBody())) {
                    score++;
                    coin.setPositionX(-coin.getTexture(0).getWidth());
                }

                previous = coin;
            }

            if (sewer.isGone() && time.get() <= 0) {
                boolean isEmptyScreen = true;

                for (Coin coin : coins) {
                    if (!coin.isGone()) {
                        isEmptyScreen = false;
                    }
                }

                if (isEmptyScreen) {
                    state = 3;
                }
            }

            if (Intersector.overlaps(bird.getBody(), sewer.getBody())
                    || bird.getPositionY() <= 0
                    || bird.getPositionY() >= device.getHeight()) {
                state = 2;
            }
        } else if (state == 2) {
            if (Gdx.input.justTouched()) {
                state = 1;
                begin();
            }
        } else if (state == 3) {
            finish();
        } else {
            if (Gdx.input.justTouched()) {
                state = 5;
            }
        }

        draw();
    }
}
package com.mariomadureira.flappybird.phase;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Intersector;
import com.mariomadureira.flappybird.element.Background;
import com.mariomadureira.flappybird.element.Coin;
import com.mariomadureira.flappybird.element.Sewer;

import java.util.List;
import java.util.Random;

public class PhaseThree extends PhasePrincipal {
    private Background background;
    private Sewer sewer;
    private List<Coin> coins;

    public PhaseThree() {
        super.create("fase 3");

        background = new Background(1);
        sewer = new Sewer(3);
        coins = getCoins(5);

        begin();
    }

    @Override
    void begin() {
        super.begin();

        if (sewer.isRotated()) {
            sewer.rotate();
            sewer.setRotated(false);
        }

        float sewerHeight = getDevice().getHeight() / 2 - sewer.getImage().getHeight();
        sewer.setPositionY(sewerHeight);
        sewer.setPositionX(getDevice().getWidth());

        Coin previous = coins.get(0);

        for (int i = 0; i < coins.size(); i++) {
            if (i > 0) {
                coins.get(i).setPositionX(previous.getPositionX() + getDevice().getWidth() / 2);
            }

            coins.get(i).setPositionY(sewerHeight + sewer.getImage().getHeight() + 150);
            previous = coins.get(i);
        }
    }

    @Override
    void draw() {
        getBatch().begin();

        getBatch().draw(background.getTexture(), 0, 0, getDevice().getWidth(), getDevice().getHeight());
        sewer.getImage().draw(getBatch(), getDevice().getWidth());

        for (Coin coin : coins) {
            getBatch().draw(coin.animate(), coin.getPositionX(), coin.getPositionY());
        }

        super.draw();
        getBatch().end();
    }

    @Override
    void play() {
        bird.fall();
        sewer.move();
        time.loss();

        if (Gdx.input.justTouched()) {
            bird.rise();
        }

        if (sewer.isGone() && time.get() > 0) {
            sewer.rotate();

            if (sewer.isRotated()) {
                sewer.setRotated(false);

                int sewerImageHeight = (int) sewer.getImage().getHeight();
                int sewerMaxHeight = (int) getDevice().getHeight() - sewerImageHeight - bird.getTexture(0).getHeight() - 200;
                int sewerMinHeight = -600;
                int sewerRandomHeight = new Random().nextInt(sewerMaxHeight - sewerMinHeight) + sewerMinHeight;

                sewer.setPositionY(sewerRandomHeight);
                sewer.setPositionX(getDevice().getWidth());
            } else {
                sewer.setRotated(true);

                int sewerMaxHeight = (int) getDevice().getHeight() + bird.getTexture(0).getHeight();
                int sewerMinHeight = (int) getDevice().getHeight() + 600;
                int sewerRandomHeight = new Random().nextInt(sewerMinHeight - sewerMaxHeight) + sewerMaxHeight;

                sewer.setPositionY(sewerRandomHeight);
                sewer.setPositionX(getDevice().getWidth() + sewer.getImage().getWidth());
            }
        }

        Coin previous = coins.get(0);

        for (Coin coin : coins) {
            if (previous.getPositionX() >= getDevice().getWidth() * 0.6 || !coin.isGone()) {
                coin.move();
            }

            if (coin.isGone() && time.get() > 0) {
                int sewerImageHeight = (int) sewer.getImage().getHeight();
                int coinMaxHeight = 0, coinMinHeight = 0;

                if (sewer.isRotated()) {
                    coinMaxHeight = (int) sewer.getPositionY() - sewerImageHeight - 100;
                    coinMinHeight = 150;
                } else {
                    coinMaxHeight = sewerImageHeight + 100;
                    coinMinHeight = (int) sewer.getPositionY() + sewerImageHeight + 100;
                }

                int coinRandomHeight = new Random().nextInt(coinMaxHeight - coinMinHeight) + coinMinHeight;
                coin.setPositionY(coinRandomHeight);
                coin.setPositionX(getDevice().getWidth());
            }

            if (Intersector.overlaps(bird.getBody(), coin.getBody())) {
                addScore();
                coin.setPositionX(-coin.getTexture(0).getWidth());
            }

            if (sewer.isTouched(coin, getDevice())) {
                coin.move();
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
                setStatus(3);
            }
        }

        if (sewer.isTouched(bird, getDevice())
                || bird.getPositionY() <= 0
                || bird.getPositionY() >= getDevice().getHeight()) {
            setStatus(2);
        }
    }

    public void render() {
        super.renderStatus();
        draw();
    }
}
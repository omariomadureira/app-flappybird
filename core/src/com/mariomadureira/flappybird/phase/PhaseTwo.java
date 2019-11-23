package com.mariomadureira.flappybird.phase;

import com.mariomadureira.flappybird.element.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Intersector;

import java.util.List;
import java.util.Random;

public class PhaseTwo extends PhasePrincipal {
    private Background background;
    private Sewer sewer;
    private List<Coin> coins;

    public PhaseTwo() {
        super.create("fase 2");

        background = new Background(2);
        sewer = new Sewer(2);
        sewer.rotate();
        coins = getCoins(4);

        begin();
    }

    @Override
    void begin() {
        super.begin();

        float sewerHeight = (sewer.getImage().getHeight() - getDevice().getHeight() / 2) + getDevice().getHeight();
        sewer.setPositionY(sewerHeight);
        sewer.setPositionX(getDevice().getWidth() + sewer.getImage().getWidth());

        Coin previous = coins.get(0);

        for (int i = 0; i < coins.size(); i++) {
            if (i > 0) {
                coins.get(i).setPositionX(previous.getPositionX() + getDevice().getWidth() / 2);
            }

            coins.get(i).setPositionY(sewerHeight - sewer.getImage().getHeight() - 150);
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
            int sewerMaxHeight = (int) getDevice().getHeight() + bird.getTexture(0).getHeight();
            int sewerMinHeight = (int) getDevice().getHeight() + 600;
            int sewerRandomHeight = new Random().nextInt(sewerMinHeight - sewerMaxHeight) + sewerMaxHeight;

            sewer.setPositionY(sewerRandomHeight);
            sewer.setPositionX(getDevice().getWidth() + sewer.getImage().getWidth());
        }

        Coin previous = coins.get(0);

        for (Coin coin : coins) {
            if (previous.getPositionX() >= getDevice().getWidth() * 0.6 || !coin.isGone()) {
                coin.move();
            }

            if (coin.isGone() && time.get() > 0) {
                int sewerImageHeight = (int) sewer.getImage().getHeight();
                int coinMaxHeight = (int) sewer.getPositionY() - sewerImageHeight - 100;
                int coinMinHeight = 150;
                int coinRandomHeight = new Random().nextInt(coinMaxHeight - coinMinHeight) + coinMinHeight;

                coin.setPositionY(coinRandomHeight);
                coin.setPositionX(getDevice().getWidth());
            }

            if (Intersector.overlaps(bird.getBody(), coin.getBody())) {
                addScore();
                coin.setPositionX(-coin.getTexture(0).getWidth());
            }

            //if (sewer.isTouched(coin, device)) {
            //sewer.setPositionY(coin.getPositionY() + 100);
            //}

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
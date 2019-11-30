package com.mariomadureira.flappybird.phase;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Intersector;
import com.mariomadureira.flappybird.element.Background;
import com.mariomadureira.flappybird.element.Coin;
import com.mariomadureira.flappybird.element.Sewer;

import java.util.List;
import java.util.Random;

public class PhaseSix extends PhasePrincipal {
    private Background background;
    private Sewer sewer;
    private float sewerRandomHeight;
    private List<Coin> coins;

    public PhaseSix() {
        super.create("fase 6");

        background = new Background(2);
        sewer = new Sewer(3);
        sewer.rotate();
        sewer.setRotated(true);
        coins = getCoins(5);

        begin();
    }

    @Override
    void begin() {
        super.begin();

        if (!sewer.isRotated()) {
            sewer.rotate();
            sewer.setRotated(true);
        }

        float sewerHeight = (int) getDevice().getHeight() + 600;
        sewer.setPositionY(sewerHeight);
        sewer.setPositionX(getDevice().getWidth() + sewer.getImage().getWidth());

        int sewerMaxHeight = (int) getDevice().getHeight() + bird.getTexture(0).getHeight();
        int sewerMinHeight = (int) getDevice().getHeight() + 600;
        sewerRandomHeight = new Random().nextInt(sewerMinHeight - sewerMaxHeight) + sewerMaxHeight;

        Coin previous = coins.get(0);

        for (int i = 0; i < coins.size(); i++) {
            if (i > 0) {
                coins.get(i).setPositionX(previous.getPositionX() + getDevice().getWidth() / 2);
            }

            coins.get(i).setPositionY(sewerRandomHeight - sewer.getImage().getHeight() - 150);
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

        if (sewer.isRotated() && sewer.getPositionY() >= sewerRandomHeight) {
            boolean canGrow = true;

            for (Coin coin : coins) {
                float coinHeight = coin.getPositionY() + 300;

                if (sewer.getPositionY() <= coinHeight) {
                    canGrow = false;
                    break;
                }
            }

            if (canGrow) {
                sewer.grow();
            }
        } else if (!sewer.isRotated() && sewer.getPositionY() <= sewerRandomHeight) {
            boolean canGrow = true;

            for (Coin coin : coins) {
                float coinHeight = coin.getPositionY() - 300;

                if (sewer.getPositionY() > coinHeight) {
                    canGrow = false;
                    break;
                }
            }

            if (canGrow) {
                sewer.grow();
            }
        }

        if (Gdx.input.justTouched()) {
            bird.rise();
        }

        if (sewer.isGone() && time.get() > 0) {
            sewer.rotate();

            if (sewer.isRotated()) {
                sewer.setRotated(false);

                float sewerHeight = (float) (getDevice().getHeight() * 0.2) - sewer.getImage().getHeight();
                sewer.setPositionY(sewerHeight);
                sewer.setPositionX(getDevice().getWidth());

                int sewerImageHeight = (int) sewer.getImage().getHeight();
                int sewerMaxHeight = (int) getDevice().getHeight() - sewerImageHeight - bird.getTexture(0).getHeight() - 200;
                int sewerMinHeight = -600;
                sewerRandomHeight = new Random().nextInt(sewerMaxHeight - sewerMinHeight) + sewerMinHeight;
            } else {
                sewer.setRotated(true);

                float sewerHeight = (int) getDevice().getHeight() + 600;
                sewer.setPositionY(sewerHeight);
                sewer.setPositionX(getDevice().getWidth() + sewer.getImage().getWidth());

                int sewerMaxHeight = (int) getDevice().getHeight() + bird.getTexture(0).getHeight();
                int sewerMinHeight = (int) getDevice().getHeight() + 600;
                sewerRandomHeight = new Random().nextInt(sewerMinHeight - sewerMaxHeight) + sewerMaxHeight;
            }
        }

        Coin previous = coins.get(0);

        for (Coin coin : coins) {
            if (previous.getPositionX() >= getDevice().getWidth() * 0.6 || !coin.isGone()) {
                coin.move();
            }

            if (coin.isGone() && time.get() > 0) {
                if (sewer.isRotated()) {
                    int sewerImageHeight = (int) sewer.getImage().getHeight();
                    int coinMaxHeight = (int) sewerRandomHeight - sewerImageHeight - 100;
                    int coinMinHeight = 150;
                    int coinRandomHeight = new Random().nextInt(coinMaxHeight - coinMinHeight) + coinMinHeight;

                    coin.setPositionY(coinRandomHeight);
                    coin.setPositionX(getDevice().getWidth());
                } else {
                    coin.setPositionX(getDevice().getWidth());

                    int sewerMaxHeight = (int) sewer.getImage().getHeight();
                    int coinMaxHeight = sewerMaxHeight + 100;
                    int coinMinHeight = (int) sewerRandomHeight + sewerMaxHeight + 100;
                    int coinRandomHeight = new Random().nextInt(coinMaxHeight - coinMinHeight) + coinMinHeight;

                    coin.setPositionY(coinRandomHeight);
                }
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
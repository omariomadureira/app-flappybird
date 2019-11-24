package com.mariomadureira.flappybird.phase;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Intersector;
import com.mariomadureira.flappybird.element.Background;
import com.mariomadureira.flappybird.element.Coin;
import com.mariomadureira.flappybird.element.Sewer;

import java.util.List;
import java.util.Random;

public class PhaseFive extends PhasePrincipal {
    private Background background;
    private Sewer sewerTop;
    private float sewerTopHeight;
    private Sewer sewerBottom;
    private float sewerBottomHeight;
    private float spaceBetweenSewers;
    private List<Coin> coins;

    public PhaseFive() {
        super.create("fase 5");

        background = new Background(4);
        sewerTop = new Sewer(1);
        sewerTop.rotate();
        sewerBottom = new Sewer(2);
        coins = getCoins(3);
        spaceBetweenSewers = 300;

        begin();
    }

    @Override
    void begin() {
        super.begin();

        float sewerRandomHeight = new Random().nextInt(400) - 200;

        sewerTopHeight = (sewerTop.getImage().getHeight() - getDevice().getHeight() / 2) + getDevice().getHeight();
        sewerTopHeight = sewerTopHeight + spaceBetweenSewers / 2 + sewerRandomHeight;
        sewerTop.setPositionY(sewerTopHeight);
        sewerTop.setPositionX(getDevice().getWidth() + sewerTop.getImage().getWidth());

        sewerBottomHeight = getDevice().getHeight() / 2 - sewerBottom.getImage().getHeight();
        sewerBottomHeight = sewerBottomHeight - spaceBetweenSewers / 2 + sewerRandomHeight;
        sewerBottom.setPositionY(sewerBottomHeight);
        sewerBottom.setPositionX(getDevice().getWidth());

        Coin previous = coins.get(0);

        for (int i = 0; i < coins.size(); i++) {
            if (i > 0) {
                coins.get(i).setPositionX(previous.getPositionX() + getDevice().getWidth() / 2);
            }

            coins.get(i).setPositionY(sewerBottom.getPositionY() + sewerBottom.getImage().getHeight() + 150);
            previous = coins.get(i);
        }
    }

    @Override
    void draw() {
        getBatch().begin();

        getBatch().draw(background.getTexture(), 0, 0, getDevice().getWidth(), getDevice().getHeight());
        sewerTop.getImage().draw(getBatch(), getDevice().getWidth());
        sewerBottom.getImage().draw(getBatch(), getDevice().getWidth());

        for (Coin coin : coins) {
            getBatch().draw(coin.animate(), coin.getPositionX(), coin.getPositionY());
        }

        super.draw();
        getBatch().end();
    }

    @Override
    void play() {
        bird.fall();
        sewerTop.move();
        sewerBottom.move();
        time.loss();

        if (Gdx.input.justTouched()) {
            bird.rise();
        }

        if (sewerBottom.isGone() && time.get() > 0) {
            float sewerRandomHeight = new Random().nextInt(400) - 200;

            sewerTopHeight = (sewerTop.getImage().getHeight() - getDevice().getHeight() / 2) + getDevice().getHeight();
            sewerTopHeight = sewerTopHeight + spaceBetweenSewers / 2 + sewerRandomHeight;
            sewerTop.setPositionY(sewerTopHeight);
            sewerTop.setPositionX(getDevice().getWidth() + sewerTop.getImage().getWidth());

            sewerBottomHeight = getDevice().getHeight() / 2 - sewerBottom.getImage().getHeight();
            sewerBottomHeight = sewerBottomHeight - spaceBetweenSewers / 2 + sewerRandomHeight;
            sewerBottom.setPositionY(sewerBottomHeight);
            sewerBottom.setPositionX(getDevice().getWidth());
        }

        Coin previous = coins.get(0);

        for (Coin coin : coins) {
            if (previous.getPositionX() >= getDevice().getWidth() * 0.6 || !coin.isGone()) {
                coin.move();
            }

            if (coin.isGone() && time.get() > 0) {
                int sewerImageHeight = (int) sewerBottom.getImage().getHeight();
                int coinMaxHeight = (int) sewerTopHeight - sewerImageHeight - 100;
                int coinMinHeight = (int) sewerBottomHeight + sewerImageHeight + 100;
                int coinRandomHeight = new Random().nextInt(coinMaxHeight - coinMinHeight) + coinMinHeight;

                coin.setPositionY(coinRandomHeight);
                coin.setPositionX(getDevice().getWidth());
            }

            if (Intersector.overlaps(bird.getBody(), coin.getBody())) {
                addScore();
                coin.setPositionX(-coin.getTexture(0).getWidth());
            }

            if (sewerBottom.isTouched(coin, getDevice())) {
                coin.move();
            }

            if (sewerTop.isTouched(coin, getDevice())) {
                coin.move();
            }

            previous = coin;
        }

        if (sewerBottom.isGone() && time.get() <= 0) {
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

        if (sewerTop.isTouched(bird, getDevice())
                || bird.getPositionY() <= 0
                || bird.getPositionY() >= getDevice().getHeight()) {
            setStatus(2);
        }

        if (sewerBottom.isTouched(bird, getDevice())
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
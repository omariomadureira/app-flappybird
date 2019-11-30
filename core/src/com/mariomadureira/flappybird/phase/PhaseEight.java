package com.mariomadureira.flappybird.phase;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Intersector;
import com.mariomadureira.flappybird.element.Background;
import com.mariomadureira.flappybird.element.Coin;
import com.mariomadureira.flappybird.element.Sewer;

import java.util.List;
import java.util.Random;

public class PhaseEight extends PhasePrincipal {
    private Background background;
    private Sewer sewerTop;
    private float sewerTopHeight;
    private boolean displaySewerTop;
    private Sewer sewerBottom;
    private float sewerBottomHeight;
    private boolean displaySewerBottom;
    private float spaceBetweenSewers;
    private List<Coin> coins;

    public PhaseEight() {
        super.create("fase 8");

        background = new Background(3);
        sewerTop = new Sewer(2);
        sewerTop.rotate();
        sewerTop.setRotated(true);
        sewerBottom = new Sewer(3);
        coins = getCoins(5);
        spaceBetweenSewers = 300;

        begin();
    }

    @Override
    void begin() {
        super.begin();

        displaySewerBottom = displaySewerTop = true;
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

        if (displaySewerTop) {
            sewerTop.getImage().draw(getBatch(), getDevice().getWidth());
        }

        if (displaySewerBottom) {
            sewerBottom.getImage().draw(getBatch(), getDevice().getWidth());
        }

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

            displaySewerTop = !displaySewerBottom || Math.random() < 0.5;
            sewerTopHeight = (sewerTop.getImage().getHeight() - getDevice().getHeight() / 2) + getDevice().getHeight();
            sewerTopHeight = sewerTopHeight + spaceBetweenSewers / 2 + sewerRandomHeight;
            sewerTop.setPositionY(sewerTopHeight);
            sewerTop.setPositionX(getDevice().getWidth() + sewerTop.getImage().getWidth());

            displaySewerBottom = !displaySewerTop || Math.random() < 0.5;
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

            if (sewerBottom.isTouched(coin, getDevice()) && displaySewerBottom) {
                coin.move();
            }

            if (sewerTop.isTouched(coin, getDevice()) && displaySewerTop) {
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

        if (sewerTop.isTouched(bird, getDevice()) && displaySewerTop) {
            setStatus(2);
        }

        if (sewerBottom.isTouched(bird, getDevice()) && displaySewerBottom) {
            setStatus(2);
        }

         if (bird.getPositionY() <= 0 || bird.getPositionY() >= getDevice().getHeight()) {
            setStatus(2);
        }
    }

    public void render() {
        super.renderStatus();
        draw();
    }
}
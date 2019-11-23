package com.mariomadureira.flappybird.phase;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.mariomadureira.flappybird.element.*;
import com.mariomadureira.flappybird.config.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;

import java.util.Random;

public class PhaseOne {
    private Device device;
    private SpriteBatch batch;
    private Stage stage;

    private Background background;
    private TextButton restart;

    private Text title;
    private Text subtitle;
    private Text congratulations;

    private Time time;
    private int score;

    private Bird bird;
    private Sewer sewer;
    private Coin[] coins;
    private Tree tree;

    private int status;

    public PhaseOne() {
        device = new Device();
        batch = new SpriteBatch();
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        background = new Background(1);

        Text gameOver = new Text("tentar de novo");
        gameOver.setTitle(70);
        gameOver.calculateWidth();

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = gameOver.get();

        double subtitleHeight = device.getHeight() * 0.35;
        restart = new TextButton(gameOver.getText(), textButtonStyle);
        restart.setPosition(device.getWidth() / 3 - gameOver.getWidth() / 2, (float) subtitleHeight);
        stage.addActor(restart);

        title = new Text("fase 1");
        title.setTitle(120);
        title.calculateWidth();

        congratulations = new Text("fase concluida");
        congratulations.setTitle(120);
        congratulations.calculateWidth();

        subtitle = new Text("");
        subtitle.setSubtitle(2);

        time = new Time(45);

        bird = new Bird();
        bird.setPositionX(120);
        sewer = new Sewer();
        coins = new Coin[]
                {
                        new Coin(),
                        new Coin(),
                        new Coin(),
                        new Coin(),
                        new Coin()
                };
        tree = new Tree();
        tree.setPositionX(device.getWidth());
        tree.setPositionY(-20);

        status = 0;

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
            status = 4;
        }
    }

    public boolean isFinished() {
        return status == 5;
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

        if (status == 0) {
            double titleHeight = device.getHeight() * 0.55;
            title.get().draw(batch, title.getText(),
                    device.getWidth() / 2 - title.getWidth() / 2, (float) titleHeight);

            double subtitleHeight = device.getHeight() * 0.44;
            subtitle.setText("Toque para começar!");
            subtitle.calculateWidth();
            subtitle.get().draw(batch, subtitle.getText(),
                    device.getWidth() / 2 - subtitle.getWidth() / 2, (float) subtitleHeight);
        } else if (status == 2) {
            stage.draw();
        } else if (status == 3) {
            batch.draw(tree.getTexture(), tree.getPositionX(), tree.getPositionY());
        } else if (status > 3) {
            batch.draw(tree.getTexture(), tree.getPositionX(), tree.getPositionY());
            double titleHeight = device.getHeight() * 0.55;
            congratulations.get().draw(batch, congratulations.getText(),
                    device.getWidth() / 2 - congratulations.getWidth() / 2, (float) titleHeight);

            double subtitleHeight = device.getHeight() * 0.44;
            subtitle.setText("Toque para continuar!");
            subtitle.calculateWidth();
            subtitle.get().draw(batch, subtitle.getText(),
                    device.getWidth() / 2 - subtitle.getWidth() / 2, (float) subtitleHeight);
        }

        batch.end();
    }

    public void render() {
        if (status == 0) {
            if (Gdx.input.justTouched()) {
                status = 1;
            }
        } else if (status == 1) {
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
            }

            Coin previous = coins[0];

            for (Coin coin : coins) {
                if (previous.getPositionX() >= device.getWidth() * 0.6 || !coin.isGone()) {
                    coin.move();
                }

                if (coin.isGone() && time.get() > 0) {
                    coin.setPositionX(device.getWidth());

                    int sewerMaxHeight = (int) sewer.getImage().getHeight();
                    int coinMaxHeight = sewerMaxHeight + 100;
                    int coinMinHeight = (int) sewer.getPositionY() + sewerMaxHeight + 100;
                    int coinRandomHeight = new Random().nextInt(coinMaxHeight - coinMinHeight) + coinMinHeight;

                    coin.setPositionY(coinRandomHeight);
                }

                if (Intersector.overlaps(bird.getBody(), coin.getBody())) {
                    score++;
                    coin.setPositionX(-coin.getTexture(0).getWidth());
                }

                //if (sewer.isTouched(coin, device)) {
                    //sewer.setPositionY(coin.getPositionY() - 100);
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
                    status = 3;
                }
            }

            if (sewer.isTouched(bird, device)
                    || bird.getPositionY() <= 0
                    || bird.getPositionY() >= device.getHeight()) {
                status = 2;
            }
        } else if (status == 2) {
            restart.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    status = 1;
                    begin();
                }
            });
        } else if (status == 3) {
            finish();
        } else {
            if (Gdx.input.justTouched()) {
                status = 5;
            }
        }

        draw();
    }
}
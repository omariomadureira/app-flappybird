package com.mariomadureira.flappybird.phase;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.mariomadureira.flappybird.config.Device;
import com.mariomadureira.flappybird.element.*;

import java.util.ArrayList;
import java.util.List;

public class PhasePrincipal {
    private Device device;
    private SpriteBatch batch;
    private Stage stage;

    private TextButton restart;
    private Text title;
    private Text subtitle;
    private Text congratulations;

    Bird bird;
    private Tree tree;

    Time time;
    private int score;
    private int status;

    //region Getters and Setters
    public SpriteBatch getBatch() {
        return batch;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    Device getDevice() {
        return device;
    }

    void addScore() {
        score++;
    }

    void setStatus(int status) {
        this.status = status;
    }

    List<Coin> getCoins(int quantity) {
        List<Coin> coins = new ArrayList<Coin>();

        for (int i = 0; i < quantity; i++) {
            coins.add(new Coin());
        }

        return coins;
    }
    //endregion

    void create(String phaseName) {
        device = new Device();
        batch = new SpriteBatch();
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        Text gameOver = new Text("tentar de novo");
        gameOver.setTitle(70);
        gameOver.calculateWidth();

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = gameOver.get();

        double subtitleHeight = device.getHeight() * 0.35;
        restart = new TextButton(gameOver.getText(), textButtonStyle);
        restart.setPosition(device.getWidth() / 3 - gameOver.getWidth() / 2, (float) subtitleHeight);
        stage.addActor(restart);

        title = new Text(phaseName);
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

        tree = new Tree();
        tree.setPositionX(device.getWidth());
        tree.setPositionY(-20);

        status = 0;
    }

    void begin() {
        score = 0;
        time.clean();
        bird.die();
        bird.setPositionY(device.getHeight() / 2);
    }

    void draw() {
        batch.draw(bird.animate(), bird.getPositionX(), bird.getPositionY());

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
    }

    void renderStatus() {
        switch (status) {
            case 1:
                play();
                break;
            case 2:
                restart();
                break;
            case 3:
                finish();
                break;
            case 4:
                dispose();
                break;
            default:
                start();
                break;
        }
    }

    private void start() {
        if (Gdx.input.justTouched()) {
            status = 1;
        }
    }

    void play() {
    }

    private void restart() {
        restart.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                status = 1;
                begin();
            }
        });
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

    private void dispose() {
        if (Gdx.input.justTouched()) {
            status = 5;
        }
    }
}
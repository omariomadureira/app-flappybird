package com.mariomadureira.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mariomadureira.flappybird.config.Device;
import com.mariomadureira.flappybird.phase.Main;
import com.mariomadureira.flappybird.phase.PhaseOne;
import com.mariomadureira.flappybird.phase.PhaseTwo;

public class FlappyBird extends ApplicationAdapter {
    private OrthographicCamera camera;
    private Viewport viewport;

    private Main main;

    private int phase;
    private PhaseOne phaseOne;
    private PhaseTwo phaseTwo;

    private int score;

    //region Getters and Setters
    private int getScore() {
        return score;
    }

    private void setScore(int score) {
        this.score = score;
    }

    private int getPhase() {
        return phase;
    }

    private void setPhase(int phase) {
        this.phase = phase;
    }
    //#endregion

    private void createPhase() {
        switch (phase) {
            case 1:
                phaseOne = new PhaseOne();
                break;
            case 2:
                phaseTwo = new PhaseTwo();
                phaseTwo.setScore(getScore());
                break;
            default:
                main = new Main();
        }
    }

    @Override
    public void create() {
        Device device = new Device();
        camera = new OrthographicCamera();
        camera.position.set(device.getWidth() / 2, device.getHeight() / 2, 0);
        viewport = new StretchViewport(device.getWidth(), device.getHeight(), camera);
        score = 0;
        phase = 0;
        createPhase();
    }

    @Override
    public void render() {
        camera.update();
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        if (getPhase() == 1) {
            phaseOne.getBatch().setProjectionMatrix(camera.combined);
            phaseOne.render();

            if (phaseOne.isFinished()) {
                setScore(phaseOne.getScore());
                setPhase(2);
                createPhase();
            }
        } else if (getPhase() == 2) {
            phaseTwo.getBatch().setProjectionMatrix(camera.combined);
            phaseTwo.render();
        } else {
            main.getBatch().setProjectionMatrix(camera.combined);
            main.render();

            if (main.isPlayPressed()) {
                setPhase(1);
                createPhase();
            }
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }
}
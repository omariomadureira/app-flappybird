package com.mariomadureira.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mariomadureira.flappybird.config.Device;
import com.mariomadureira.flappybird.phase.*;

public class FlappyBird extends ApplicationAdapter {
    private OrthographicCamera camera;
    private Viewport viewport;

    private Main main;
    private int phase;
    private int score;

    private PhaseOne phaseOne;
    private PhaseTwo phaseTwo;
    private PhaseThree phaseThree;
    private PhaseFour phaseFour;
    private PhaseFive phaseFive;
    private PhaseSix phaseSix;
    private PhaseSeven phaseSeven;
    private PhaseEight phaseEight;
    private PhaseNine phaseNine;
    private PhaseTen phaseTen;

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
            case 3:
                phaseThree = new PhaseThree();
                phaseThree.setScore(getScore());
                break;
            case 4:
                phaseFour = new PhaseFour();
                phaseFour.setScore(getScore());
                break;
            case 5:
                phaseFive = new PhaseFive();
                phaseFive.setScore(getScore());
                break;
            case 6:
                phaseSix = new PhaseSix();
                phaseSix.setScore(getScore());
                break;
            case 7:
                phaseSeven = new PhaseSeven();
                phaseSeven.setScore(getScore());
                break;
            case 8:
                phaseEight = new PhaseEight();
                phaseEight.setScore(getScore());
                break;
            case 9:
                phaseNine = new PhaseNine();
                phaseNine.setScore(getScore());
                break;
            case 10:
                phaseTen = new PhaseTen();
                phaseTen.setScore(getScore());
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
        phase = 10;
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
                phaseOne = null;
            }
        } else if (getPhase() == 2) {
            phaseTwo.getBatch().setProjectionMatrix(camera.combined);
            phaseTwo.render();

            if (phaseTwo.isFinished()) {
                setScore(phaseTwo.getScore());
                setPhase(3);
                createPhase();
                phaseTwo = null;
            }
        } else if (getPhase() == 3) {
            phaseThree.getBatch().setProjectionMatrix(camera.combined);
            phaseThree.render();

            if (phaseThree.isFinished()) {
                setScore(phaseThree.getScore());
                setPhase(4);
                createPhase();
                phaseThree = null;
            }
        } else if (getPhase() == 4) {
            phaseFour.getBatch().setProjectionMatrix(camera.combined);
            phaseFour.render();

            if (phaseFour.isFinished()) {
                setScore(phaseFour.getScore());
                setPhase(5);
                createPhase();
                phaseFour = null;
            }
        } else if (getPhase() == 5) {
            phaseFive.getBatch().setProjectionMatrix(camera.combined);
            phaseFive.render();

            if (phaseFive.isFinished()) {
                setScore(phaseFive.getScore());
                setPhase(6);
                createPhase();
                phaseFive = null;
            }
        } else if (getPhase() == 6) {
            phaseSix.getBatch().setProjectionMatrix(camera.combined);
            phaseSix.render();

            if (phaseSix.isFinished()) {
                setScore(phaseSix.getScore());
                setPhase(7);
                createPhase();
                phaseSix = null;
            }
        } else if (getPhase() == 7) {
            phaseSeven.getBatch().setProjectionMatrix(camera.combined);
            phaseSeven.render();

            if (phaseSeven.isFinished()) {
                setScore(phaseSeven.getScore());
                setPhase(8);
                createPhase();
                phaseSeven = null;
            }
        } else if (getPhase() == 8) {
            phaseEight.getBatch().setProjectionMatrix(camera.combined);
            phaseEight.render();

            if (phaseEight.isFinished()) {
                setScore(phaseEight.getScore());
                setPhase(9);
                createPhase();
                phaseEight = null;
            }
        } else if (getPhase() == 9) {
            phaseNine.getBatch().setProjectionMatrix(camera.combined);
            phaseNine.render();

            if (phaseNine.isFinished()) {
                setScore(phaseNine.getScore());
                setPhase(10);
                createPhase();
                phaseNine = null;
            }
        } else if (getPhase() == 10) {
            phaseTen.getBatch().setProjectionMatrix(camera.combined);
            phaseTen.render();
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
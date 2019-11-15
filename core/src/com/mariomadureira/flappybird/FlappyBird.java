package com.mariomadureira.flappybird;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mariomadureira.flappybird.config.Device;
import com.mariomadureira.flappybird.phase.PhaseOne;
import com.mariomadureira.flappybird.phase.PhaseTwo;

public class FlappyBird extends ApplicationAdapter
{
    private Device device;
    private OrthographicCamera camera;
    private Viewport viewport;

    private PhaseOne phaseOne;
    private PhaseTwo phaseTwo;

    private int score;
    private int phase;

    public void setScore(int score) {
        this.score = score;
    }

    public void setPhase(int phase) {
        this.phase = phase;
    }

    public int getScore() {
        return score;
    }

    public int getPhase() {
        return phase;
    }

    @Override
    public void create ()
    {
        device = new Device();
        camera = new OrthographicCamera();
        camera.position.set(device.width() /2, device.height() /2, 0);
        viewport = new StretchViewport(device.width(), device.height(), camera);

        score = 0;

        phase = 1;
        createPhaseOne();
    }

    public void createPhaseOne()
    {
        phaseOne = new PhaseOne();
        phaseOne.create();
    }

    public void createPhaseTwo()
    {
        phaseTwo = new PhaseTwo();
        phaseTwo.create();
        phaseTwo.setScore(getScore());
    }

    @Override
    public void render ()
    {
        camera.update();
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        if (getPhase() == 1)
        {
            phaseOne.getBatch().setProjectionMatrix(camera.combined);
            phaseOne.render();

            if (phaseOne.isFinished())
            {
                setScore(phaseOne.getScore());
                setPhase(2);
                createPhaseTwo();
            }
        }
        else if (getPhase() == 2)
        {
            phaseTwo.getBatch().setProjectionMatrix(camera.combined);
            phaseTwo.render();
        }
    }

    @Override
    public void resize(int width, int height)
    {
        viewport.update(width, height);
    }
}
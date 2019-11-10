package com.mariomadureira.flappybird;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mariomadureira.flappybird.config.Device;
import com.mariomadureira.flappybird.phase.PhaseOne;

public class FlappyBird extends ApplicationAdapter
{
    private Device device;
    private OrthographicCamera camera;
    private Viewport viewport;

    private PhaseOne phase;

    @Override
    public void create ()
    {
        device = new Device();
        camera = new OrthographicCamera();
        camera.position.set(device.width() /2, device.height() /2, 0);
        viewport = new StretchViewport(device.width(), device.height(), camera);

        phase = new PhaseOne();
        phase.create();
    }

    @Override
    public void render ()
    {
        //Limpa a memória do dispositivo
        camera.update();
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        //Configurar dados de projeção da câmera
        phase.batch().setProjectionMatrix(camera.combined);
        phase.render();
    }

    @Override
    public void resize(int width, int height)
    {
        viewport.update(width, height);
    }
}
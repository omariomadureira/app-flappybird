package com.mariomadureira.flappybird.phase;

import com.mariomadureira.flappybird.element.*;
import com.mariomadureira.flappybird.config.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import java.util.Random;

public class PhaseOne
{
    private Device device;
    private SpriteBatch batch;

    private Background background;
    private Title gameOver;
    private Text text;

    private Bird bird;
    private Sewer sewer;

    private Time time;
    private int state;

    public SpriteBatch batch () {
        return batch;
    }

    public void create ()
    {
        device = new Device();
        batch = new SpriteBatch();

        background = new Background();
        gameOver = new Title("gameOver");
        text = new Text(3);

        bird = new Bird();
        bird.positionY(device.height() / 2);
        bird.positionX(120);

        sewer = new Sewer("down");
        sewer.positionX(device.width());
        float sewerHeight = device.height() / 2 - sewer.texture().getHeight();
        sewer.positionY(sewerHeight);

        time = new Time(30);
        state = 0;
    }

    private void draw ()
    {
        batch.begin();

        batch.draw(background.texture(), 0, 0, device.width(), device.height());
        batch.draw(sewer.texture(), sewer.positionX(), sewer.positionY());
        batch.draw(bird.fly(), bird.positionX(), bird.positionY());

        String timeText = "Tempo: " + time.get();
        time.title().draw(batch, timeText, device.width() - 250, device.height() - 50);

        if (state == 0)
        {
            text.get().draw(batch, "Fase 1: toque para começar!",
                    device.width() / 2 - 230, device.height() / 2 - gameOver.texture().getHeight());
        }
        else if (state == 2)
        {
            text.get().draw(batch, "Fase 1: Toque para reiniciar!",
                    device.width() / 2 - 230, device.height() / 2 - gameOver.texture().getHeight());

            batch.draw(gameOver.texture(), device.width() / 2 - gameOver.texture().getWidth() / 2, device.height() / 2);
        }
        else if (state == 3)
        {
            text.get().draw(batch, "Você passou de fase!",
                    device.width() / 2 - 230, device.height() / 2 - gameOver.texture().getHeight());
        }

        batch.end();
    }

    public void render ()
    {
        //Estado 0: aguardando start
        if (state == 0)
        {
            //Quando o usuário tocar a tela, a fase começa
            if (Gdx.input.justTouched())
                state = 1;
        }

        //Estado 1: start
        else if (state == 1)
        {
            bird.fall();
            sewer.move();
            time.loss();

            //Verifica se o tempo acabou
            if (time.get() <= 0)
                state = 3;

            //Verifica se o usuário tocou na tela
            if (Gdx.input.justTouched())
                bird.rise();

            //Verifica se o cano saiu inteiramente da tela
            if (!sewer.inScreen())
            {
                int sewerMaxHeight = (int) device.height() - sewer.texture().getHeight() - bird.texture(0).getHeight() - 200;
                int sewerMinHeight = -600;
                int sewerRandomHeight = new Random().nextInt(sewerMaxHeight - sewerMinHeight) + sewerMinHeight;
                sewer.positionY(sewerRandomHeight);
                sewer.positionX(device.width());
            }

            //Verifica se houve colisão
            if (Intersector.overlaps(bird.body(), sewer.body())
                    || bird.positionY() <= 0
                    || bird.positionY() >= device.height())
            {
                state = 2;
            }
        }

        //Estado 2: game over
        else if (state == 2)
        {
            //Zerar o valores padrões
            if (Gdx.input.justTouched())
            {
                state = 0;
                time.clean();
                bird.die();
                bird.positionY(device.height() / 2);
                sewer.positionX(device.width());
            }
        }

        draw();
    }
}

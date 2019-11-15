package com.mariomadureira.flappybird.phase;

import com.mariomadureira.flappybird.element.*;
import com.mariomadureira.flappybird.config.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import jdk.nashorn.internal.objects.Global;

import java.util.Random;

public class PhaseTwo
{
    private Device device;
    private SpriteBatch batch;

    private Background background;
    private Title gameOver;
    private Text text;

    private Bird bird;
    private Sewer sewer;
    private Coin[] coins;
    private Tree tree;

    private Time time;
    private int state;
    private int score;

    public SpriteBatch getBatch ()
    {
        return batch;
    }

    public void setScore (int score)
    {
        this.score = score;
    }

    public void create ()
    {
        device = new Device();
        batch = new SpriteBatch();

        background = new Background(2);
        gameOver = new Title("gameOver");
        text = new Text(3);

        bird = new Bird();
        bird.positionX(120);
        sewer = new Sewer("down");

        coins = new Coin[]
                {
                        new Coin(device.width()),
                        new Coin(device.width()),
                        new Coin(device.width()),
                        new Coin(device.width()),
                };

        tree = new Tree();
        tree.positionX(device.width());
        tree.positionY(-20);

        time = new Time(45);
        state = 0;

        begin();
    }

    private void begin ()
    {
        score = 0;
        time.clean();

        bird.die();
        bird.positionY(device.height() / 2);

        sewer.positionX(device.width());
        float sewerHeight = device.height() / 2 - sewer.texture().getHeight();
        sewer.positionY(sewerHeight);

        Coin previous = coins[0];

        for (int i = 0; i < coins.length; i++)
        {
            if (i > 0)
            {
                coins[i].positionX(previous.positionX() + device.width() / 2);
            }

            coins[i].setScored(false);
            coins[i].positionY(sewerHeight + sewer.texture().getHeight() + 150);

            previous = coins[i];
        }
    }

    private void finish ()
    {
        bird.move();

        int treePartialWidth = (tree.texture().getWidth() * 70) / 100;
        int treeFinalWidth = (int) device.width() - treePartialWidth;

        if (tree.positionX() > treeFinalWidth)
        {
            tree.move();
        }

        if (bird.positionX() >= device.width())
        {
            state = 4;
        }
    }

    public boolean isFinished ()
    {
        if (state == 5)
        {
            return true;
        }

        return false;
    }

    private void draw ()
    {
        batch.begin();

        batch.draw(background.texture(), 0, 0, device.width(), device.height());
        batch.draw(sewer.texture(), sewer.positionX(), sewer.positionY());
        batch.draw(bird.fly(), bird.positionX(), bird.positionY());

        for (int i = 0; i < coins.length; i++)
        {
            batch.draw(coins[i].turn(), coins[i].positionX(), coins[i].positionY());
        }

        String timeText = "Tempo: " + time.get();
        time.title().draw(batch, timeText, device.width() - 250, device.height() - 50);

        String scoreText = "Pontos: " + score;
        time.title().draw(batch, scoreText, device.width() - 500, device.height() - 50);

        if (state == 0)
        {
            text.get().draw(batch, "Fase 2: Toque para começar!",
                    device.width() / 2 - 270, device.height() / 2 - gameOver.texture().getHeight());
        }
        else if (state == 2)
        {
            text.get().draw(batch, "Fase 2: Toque para reiniciar!",
                    device.width() / 2 - 270, device.height() / 2 - gameOver.texture().getHeight());

            batch.draw(gameOver.texture(), device.width() / 2 - gameOver.texture().getWidth() / 2, device.height() / 2);
        }
        else if (state == 3)
        {
            batch.draw(tree.texture(), tree.positionX(), tree.positionY());
        }
        else if (state > 3)
        {
            batch.draw(tree.texture(), tree.positionX(), tree.positionY());
            text.get().draw(batch, "Você passou de fase!",
                    device.width() / 2 - 215, device.height() / 2 - gameOver.texture().getHeight());
        }

        batch.end();
    }

    public void render ()
    {
        if (state == 0)
        {
            if (Gdx.input.justTouched())
            {
                state = 1;
            }
        }
        else if (state == 1)
        {
            bird.fall();
            sewer.move();
            time.loss();

            if (Gdx.input.justTouched())
            {
                bird.rise();
            }

            if (!sewer.inScreen() && time.get() > 0)
            {
                int sewerMaxHeight = (int) device.height() - sewer.texture().getHeight()
                        - bird.texture(0).getHeight() - 200;
                int sewerMinHeight = -600;
                int sewerRandomHeight = new Random().nextInt(sewerMaxHeight - sewerMinHeight) + sewerMinHeight;

                sewer.positionY(sewerRandomHeight);
                sewer.positionX(device.width());
            }

            Coin previous = coins[0];

            for (int i = 0; i < coins.length; i++)
            {
                if (previous.positionX() >= device.width() * 0.6 || coins[i].inScreen())
                {
                    coins[i].move();
                }

                if (!coins[i].inScreen() && time.get() > 0)
                {
                    coins[i].positionX(device.width());

                    int sewerMaxHeight = (int) device.height() - sewer.texture().getHeight()
                            - bird.texture(0).getHeight() - 200;
                    int coinMaxHeight = sewerMaxHeight + sewer.texture().getHeight() + 150;
                    int coinMinHeight = (int) sewer.positionY() + sewer.texture().getHeight() + 100;
                    int coinRandomHeight = new Random().nextInt(coinMaxHeight - coinMinHeight) + coinMinHeight;

                    coins[i].positionY(coinRandomHeight);
                }

                if (Intersector.overlaps(bird.body(), coins[i].body()))
                {
                    score++;
                    coins[i].positionX(-coins[i].texture(0).getWidth());
                }

                previous = coins[i];
            }

            if (!sewer.inScreen() && time.get() <= 0)
            {
                boolean isEmptyScreen = true;

                for (int i = 0; i < coins.length; i++)
                {
                    if (coins[i].inScreen())
                    {
                        isEmptyScreen = false;
                    }
                }

                if (isEmptyScreen)
                {
                    state = 3;
                }
            }

            if (Intersector.overlaps(bird.body(), sewer.body())
                    || bird.positionY() <= 0
                    || bird.positionY() >= device.height())
            {
                state = 2;
            }
        }
        else if (state == 2)
        {
            if (Gdx.input.justTouched())
            {
                state = 1;
                begin();
            }
        }
        else if (state == 3)
        {
            finish();
        }
        else
        {
            if (Gdx.input.justTouched())
            {
                state = 5;
            }
        }

        draw();
    }
}
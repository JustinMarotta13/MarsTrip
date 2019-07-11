package com.justinmarotta.game.scenes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.justinmarotta.game.MarsTrip;

public class Hud implements Disposable {
    public Stage stage;
    private Viewport viewport;

    private Integer worldTime;
    private float timeCount;
    private static Integer score;

    static Label scoreLabel;
    Label countLabel;
    Label timeLabel;
    Label playerLabel;

    public Hud(SpriteBatch sb){
        worldTime = 0;
        timeCount = 0;
        score = 0;

        viewport = new FitViewport(MarsTrip.WIDTH, MarsTrip.HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, sb);

        Table table = new Table();
        table.top();
        table.setFillParent(true);

        countLabel = new Label(String.format("%04d", worldTime), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        scoreLabel = new Label(String.format("%07d", score), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        timeLabel = new Label("TIME", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        playerLabel = new Label("PLAYER", new Label.LabelStyle(new BitmapFont(), Color.WHITE));

        table.add(playerLabel).padLeft(-380).padTop(10);
        table.add(timeLabel).padRight(-380).padTop(10);
        table.row();
        table.add(scoreLabel).padLeft(-380);
        table.add(countLabel).padRight(-380);

        stage.addActor(table);
    }

    public void update(float dt){
        timeCount += dt;
        if (timeCount >= 1){
            worldTime++;
            addScore(100);
            countLabel.setText(String.format("%04d", worldTime));
            timeCount = 0;
        }
    }

    public static void addScore(int value){
        score += value;
        scoreLabel.setText(String.format("%07d", score));
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}

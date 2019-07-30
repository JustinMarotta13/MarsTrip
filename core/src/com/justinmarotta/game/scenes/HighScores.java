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

public class HighScores implements Disposable {
    public Stage stage;

    private Integer worldTime;
    private float timeCount;
    private static Integer score;

    private static Label scoreLabel;
    private Label countLabel;

    public HighScores(SpriteBatch sb){
        worldTime = 0;
        timeCount = 0;
        score = 0;
        Label timeLabel;
        Label playerLabel;

        Viewport viewport;
        viewport = new FitViewport(MarsTrip.WIDTH, MarsTrip.HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, sb);

        Table table = new Table();
        table.top();
        table.setFillParent(true);

        countLabel = new Label(worldTime.toString(), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        scoreLabel = new Label(score.toString(), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        timeLabel = new Label("TIME", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        playerLabel = new Label("PLAYER", new Label.LabelStyle(new BitmapFont(), Color.WHITE));

        table.add(playerLabel).padLeft(-(MarsTrip.WIDTH) + 100).padTop(10);
        table.add(timeLabel).padRight(-(MarsTrip.WIDTH) + 100).padTop(10);
        table.row();
        table.add(scoreLabel).padLeft(-(MarsTrip.WIDTH) + 100);
        table.add(countLabel).padRight(-(MarsTrip.WIDTH) + 100);

        stage.addActor(table);
    }

    public void update(float dt){
        timeCount += dt;
        if (timeCount >= 1){
            worldTime++;
            addScore(100);
            countLabel.setText(worldTime.toString());
            timeCount = 0;
        }
    }

    public static void addScore(int value){
        score += value;
        scoreLabel.setText(score.toString());
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
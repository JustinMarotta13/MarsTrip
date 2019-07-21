package com.justinmarotta.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.justinmarotta.game.MarsTrip;

import static com.badlogic.gdx.Input.Keys.SPACE;

public class GameOverScreen implements Screen {
    private MarsTrip game;
    private OrthographicCamera gamecam;
    private Viewport gamePort;

    private Texture bg;
    private Texture playBtn;
    private Texture gameover;

    public GameOverScreen(MarsTrip game){
        this.game = game;
        gamecam = new OrthographicCamera();
        gamePort = new FitViewport((MarsTrip.WIDTH / 2), (MarsTrip.HEIGHT / 2), gamecam);
        gamecam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);
        bg = new Texture("bg.png");
        playBtn = new Texture("playbtn.png");
        gameover = new Texture("gameover.png");

    }

    public void handleInput(float dt) {
        if (Gdx.input.isKeyJustPressed(SPACE)){
            game.setScreen(new PlayScreen(game));
        }
    }

    @Override
    public void render(float delta) {
        update(delta);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.batch.setProjectionMatrix(gamecam.combined);
        game.batch.begin();
        game.batch.draw(bg, 0, 0);
        game.batch.draw(gameover, gamecam.position.x - gameover.getWidth() / 2, gamecam.position.y + 50);
        game.batch.draw(playBtn, gamecam.position.x - playBtn.getWidth() / 2, gamecam.position.y - 50);
        game.batch.end();
    }

    public void update(float dt){
        handleInput(dt);
    }

    @Override
    public void resize(int width, int height) {
        gamePort.update(width, height);
    }

    @Override
    public void show() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        gameover.dispose();
        bg.dispose();
        playBtn.dispose();
    }
}

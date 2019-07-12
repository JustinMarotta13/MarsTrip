package com.justinmarotta.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.justinmarotta.game.MarsTrip;
import com.justinmarotta.game.scenes.Hud;
import com.justinmarotta.game.sprites.Asteroid;
import com.justinmarotta.game.sprites.Bullet;
import com.justinmarotta.game.sprites.Ship;
import com.justinmarotta.game.sprites.Tower;

import java.util.Random;

import static com.badlogic.gdx.Input.Keys.A;
import static com.badlogic.gdx.Input.Keys.D;
import static com.badlogic.gdx.Input.Keys.S;
import static com.badlogic.gdx.Input.Keys.SPACE;
import static com.badlogic.gdx.Input.Keys.W;

public class PlayScreen implements Screen {
    private MarsTrip game;
    private OrthographicCamera gamecam;
    private Viewport gamePort;
    private Hud hud;
    private int camPos = 0;

    private Texture bg;

    private Ship ship;

    //towers
    private static final int TOWER_SPACING = 300;
    private static final int TOWER_COUNT = 4;
    private Array<Tower> towers;

    //asteroids
    private static final int ASTEROID_COUNT = 5;
    private Array<Asteroid> asteroids;
    private Random rand;

    //bullets
    private Array<Bullet> bullets;

    //Constructor
    public PlayScreen(MarsTrip game) {
        this.game = game;
        gamecam = new OrthographicCamera();
        gamePort = new FitViewport((MarsTrip.WIDTH / 2), (MarsTrip.HEIGHT / 2), gamecam);
        gamecam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);
        hud = new Hud(game.batch);

        rand = new Random();

        bg = new Texture("bg.png");

        ship = new Ship(55, 200);

        //init tower array
        towers = new Array<Tower>();
        for (int i = 1; i <= TOWER_COUNT; i++){
            towers.add(new Tower(i * (TOWER_SPACING + Tower.TOWER_WIDTH)));
        }

        //init asteroid array
        asteroids = new Array<Asteroid>();
        for (int i = 1; i <= ASTEROID_COUNT; i++){
            asteroids.add(new Asteroid((rand.nextInt(MarsTrip.WIDTH * 3) + MarsTrip.WIDTH), rand.nextInt(Asteroid.HEIGHT_MAX)));
        }

        //init bullet array
        bullets = new Array<Bullet>();
    }

    public void handleInput(float dt) {
        if (Gdx.input.isKeyPressed(W) && ship.position.y < 380)
            ship.moveUp();
        if (Gdx.input.isKeyPressed(S))
            ship.moveDown();
        if (Gdx.input.isKeyPressed(A) && ship.position.x > camPos)
            ship.moveLeft();
        if (Gdx.input.isKeyPressed(D) && ship.position.x < 200 + camPos)
            ship.moveRight();
        if (Gdx.input.isKeyJustPressed(SPACE)) {
            bullets.add(new Bullet(ship.position.x + 20, ship.position.y + 12));
        }
    }

    @Override
    public void render(float delta) {
        update(delta);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.batch.setProjectionMatrix(gamecam.combined);

        game.batch.begin();
        game.batch.draw(bg, gamecam.position.x - 120, gamecam.position.y - 200);

        //draw bullets
        for (Bullet bullet : bullets){
            game.batch.draw(bullet.getTexture(), bullet.getPosition().x, bullet.getPosition().y);
        }

        //draw ship
        game.batch.draw(ship.getTexture(), ship.getPosition().x, ship.getPosition().y);

        //draw towers
        for (Tower tower : towers){
            game.batch.draw(tower.getTopTower(), tower.getPosTopTower().x, tower.getPosTopTower().y);
            game.batch.draw(tower.getBottomTower(), tower.getPosBotTower().x, tower.getPosBotTower().y);
        }

        //draw asteroids
        for (Asteroid asteroid : asteroids) {
            game.batch.draw(asteroid.getTexture(), asteroid.getPosition().x, asteroid.getPosition().y);
        }

        game.batch.end();

        //draw what hud sees
        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();
    }

    public void update(float dt) {
        handleInput(dt);
        gamecam.position.add(4, 0, 0);

        //int for camera location
        camPos = camPos + (Ship.MOVEMENT / 60);

        hud.update(dt);

        ship.update(dt);

        //update towers
        for (int i = 0; i < towers.size; i++){

            Tower tower = towers.get(i);

            if (gamecam.position.x - (gamecam.viewportWidth / 2) > tower.getPosTopTower().x + tower.getTopTower().getWidth()
                    && gamecam.position.x - (gamecam.viewportWidth / 2) > tower.getPosBotTower().x + tower.getBottomTower().getWidth()){

                tower.reposition(tower.getPosTopTower().x + ((Tower.TOWER_WIDTH + TOWER_SPACING) * TOWER_COUNT));
                Hud.addScore(150);
            }

            if (tower.collides(ship.getBounds()))
                game.setScreen(new GameOverScreen(game));
        }

        //update asteroids
        for (int i = 0; i < asteroids.size; i++) {
            Asteroid asteroid = asteroids.get(i);
            asteroid.update(dt);

            if (gamecam.position.x - (gamecam.viewportWidth / 2) > asteroid.getPosition().x + asteroid.getTexture().getWidth()){
                asteroid.reposition(camPos + (rand.nextInt(MarsTrip.WIDTH * 4) + MarsTrip.WIDTH), rand.nextInt(Asteroid.HEIGHT_MAX));
                Hud.addScore(50);
            }

            if (asteroid.collides(ship.getBounds()))
                game.setScreen(new GameOverScreen(game));
        }

        //update bullets
        for (int i = 0; i < bullets.size; i++){
            Bullet bullet = bullets.get(i);
            bullet.update(dt);

            if (gamecam.position.x + (gamecam.viewportWidth / 2) < bullet.getPosition().x)
                bullets.removeIndex(i);
        }

        //bullet asteroid collision
//        for (int i = 0; i < bullets.size; i++){
//            Bullet bullet = bullets.get(i);
//
//            for (int j = 0; j < asteroids.size; j++){
//                Asteroid asteroid = asteroids.get(j);
//
//                if (bullet.collides(asteroid.getBounds()))
//                    Hud.addScore(100);
//                    asteroid.reposition(camPos + (rand.nextInt(MarsTrip.WIDTH * 4) + MarsTrip.WIDTH), rand.nextInt(Asteroid.HEIGHT_MAX));
//            }
//        }

        gamecam.update();
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
        bg.dispose();
        ship.dispose();
        for (Tower tower : towers){
            tower.dispose();
        }
        for (Asteroid asteroid : asteroids){
            asteroid.dispose();
        }
        for (Bullet bullet : bullets){
            bullet.dispose();
        }
    }
}

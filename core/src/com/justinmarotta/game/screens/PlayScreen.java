package com.justinmarotta.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.justinmarotta.game.MarsTrip;
import com.justinmarotta.game.scenes.Hud;
import com.justinmarotta.game.sprites.Asteroid;
import com.justinmarotta.game.sprites.Bullet;
import com.justinmarotta.game.sprites.Ship;
import com.justinmarotta.game.sprites.Tower;

import java.util.ArrayList;
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
    private int leftShipBound = 0;
    private int leftShipBoundInc = (Ship.MOVEMENT / 60);

    private Random rand;

    private Texture bg;

    private Texture ground;
    private Vector2 groundPos1, groundPos2;

    private Ship ship;

    //towers
    private double towerTimer = 0;
    private ArrayList<Tower> towers = new ArrayList<Tower>();
    private ArrayList<Tower> rmTowers = new ArrayList<Tower>();

    //asteroids
    private float asteroidSpawnTimer;
    private static final float MIN_ASTEROID_SPAWN_TIME = .5f;
    private static final float MAX_ASTEROID_SPAWN_TIME = 2f;
    private ArrayList<Asteroid> asteroids = new ArrayList<Asteroid>();
    private ArrayList<Asteroid> rmAsteroids = new ArrayList<Asteroid>();

    //bullets
    private ArrayList<Bullet> bullets = new ArrayList<Bullet>();
    private ArrayList<Bullet> rmBullets = new ArrayList<Bullet>();

    //score to be added array

    //Constructor
    public PlayScreen(MarsTrip game) {
        this.game = game;
        gamecam = new OrthographicCamera();
        gamePort = new FitViewport((MarsTrip.WIDTH / 2), (MarsTrip.HEIGHT / 2), gamecam);
        gamecam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);
        hud = new Hud(game.batch);

        rand = new Random();

        bg = new Texture("bg.png");

        ground = new Texture("ground.png");
        groundPos1 = new Vector2(gamePort.getScreenWidth(), 0);
        groundPos2 = new Vector2(ground.getWidth(), 0);

        ship = new Ship(55, 200);

        //init asteroid timer
        asteroidSpawnTimer = rand.nextFloat() * (MAX_ASTEROID_SPAWN_TIME - MIN_ASTEROID_SPAWN_TIME) + MIN_ASTEROID_SPAWN_TIME;

    }

    public void handleInput(float dt) {
        if (Gdx.input.isKeyPressed(W) && ship.position.y < 350)
            ship.moveUp();
        if (Gdx.input.isKeyPressed(S) && ship.position.y > 20)
            ship.moveDown();
        if (Gdx.input.isKeyPressed(A) && ship.position.x > leftShipBound)
            ship.moveLeft();
        if (Gdx.input.isKeyPressed(D) && ship.position.x < 200 + leftShipBound)
            ship.moveRight();
        if (Gdx.input.isKeyJustPressed(SPACE)) {
            bullets.add(new Bullet(ship.position.x + 20, ship.position.y + 12));
        }
    }

    @Override
    public void render(float delta) {
        update(delta);

        //clears screen and begins sprite batch
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.batch.setProjectionMatrix(gamecam.combined);
        game.batch.begin();

        game.batch.draw(bg, leftShipBound, 0);

        //draw bullets
        for (Bullet bullet : bullets){
            game.batch.draw(bullet.getTexture(), bullet.getPosition().x, bullet.getPosition().y);
        }

        //draw ship
        game.batch.draw(ship.getTexture(), ship.getPosition().x, ship.getPosition().y);

        //draw towers
        towerTimer = towerTimer + .625;
        if (towerTimer % 60 == 0) {
            towers.add(new Tower(leftShipBound + (MarsTrip.WIDTH / 2) + Tower.TOWER_WIDTH));
        }
        for (Tower tower : towers){
            game.batch.draw(tower.getTower(), tower.getTowerPos().x, tower.getTowerPos().y);
        }

        //draw asteroids
        asteroidSpawnTimer -= delta;
        if (asteroidSpawnTimer <= 0){
            asteroidSpawnTimer = rand.nextFloat() * (MAX_ASTEROID_SPAWN_TIME - MIN_ASTEROID_SPAWN_TIME) + MIN_ASTEROID_SPAWN_TIME;
            asteroids.add(new Asteroid(leftShipBound + (rand.nextInt(MarsTrip.WIDTH * 4) + MarsTrip.WIDTH), rand.nextInt(Asteroid.MAX_HEIGHT) + Asteroid.MIN_HEIGHT));
        }
        for (Asteroid asteroid : asteroids) {
            game.batch.draw(asteroid.getTexture(), asteroid.getPosition().x, asteroid.getPosition().y);
        }

        //draw ground
        game.batch.draw(ground, groundPos1.x, groundPos1.y);
        game.batch.draw(ground, groundPos2.x, groundPos2.y);

        //end game batch
        game.batch.end();

        //draw what hud sees
        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();
    }

    public void update(float dt) {
        handleInput(dt);

        //cam positioning
        gamecam.position.add(leftShipBoundInc, 0, 0);
        leftShipBound = leftShipBound + leftShipBoundInc;

        hud.update(dt);

        ship.update(dt);

        updateGround();


        //update towers
        for (Tower tower : towers){

            if (gamecam.position.x - (gamecam.viewportWidth / 2) > tower.getTowerPos().x + tower.getTower().getWidth()){
                rmTowers.add(tower);
                Hud.addScore(100);
            }

            if (tower.collides(ship.getBounds()))
                game.setScreen(new GameOverScreen(game));
        }
        towers.removeAll(rmTowers);

        //update asteroids
        for (Asteroid asteroid : asteroids) {
            asteroid.update(dt);

            if (gamecam.position.x - (gamecam.viewportWidth / 2) > asteroid.getPosition().x + asteroid.getTexture().getWidth()){
                rmAsteroids.add(asteroid);
                Hud.addScore(50);
            }

            if (asteroid.collides(ship.getBounds()))
                game.setScreen(new GameOverScreen(game));
        }

        //update bullets
        for (Bullet bullet : bullets) {
            bullet.update(dt);

            if (gamecam.position.x + (gamecam.viewportWidth / 2) < bullet.getPosition().x){
                rmBullets.add(bullet);
            }
        }

        //asteroid bullet collision
        for (Bullet bullet : bullets){
            for (Asteroid asteroid : asteroids){
                if (bullet.collides(asteroid.getBounds())){
                    rmAsteroids.add(asteroid);
                    rmBullets.add(bullet);
                    Hud.addScore(250);
                }
            }
        }

        asteroids.removeAll(rmAsteroids);
        bullets.removeAll(rmBullets);

        //update the game camera
        gamecam.update();
    }

    private void updateGround(){
        if (gamecam.position.x - (gamecam.viewportWidth / 2) > groundPos1.x + ground.getWidth())
            groundPos1.add(ground.getWidth() * 2, 0);
        if (gamecam.position.x - (gamecam.viewportWidth / 2)  > groundPos2.x + ground.getWidth())
            groundPos2.add(ground.getWidth() * 2, 0);
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
        ground.dispose();
        for (Tower tower : towers){
            tower.dispose();
        }
        for (Tower tower : rmTowers){
            tower.dispose();
        }
        for (Asteroid asteroid : asteroids){
            asteroid.dispose();
        }
        for (Asteroid asteroid : rmAsteroids){
            asteroid.dispose();
        }
        for (Bullet bullet : bullets){
            bullet.dispose();
        }
        for (Bullet bullet : rmBullets){
            bullet.dispose();
        }
    }
}

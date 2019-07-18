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
import com.justinmarotta.game.sprites.Moon;
import com.justinmarotta.game.sprites.Rover;
import com.justinmarotta.game.sprites.Ship;
import com.justinmarotta.game.sprites.Tower;
import com.justinmarotta.game.sprites.UFO;

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

    //bullets
    private ArrayList<Bullet> bullets = new ArrayList<Bullet>();
    private ArrayList<Bullet> rmBullets = new ArrayList<Bullet>();

    //asteroids
    private float asteroidSpawnTimer;
    private static final float MIN_ASTEROID_SPAWN_TIME = .5f;
    private static final float MAX_ASTEROID_SPAWN_TIME = 2f;
    private ArrayList<Asteroid> asteroids = new ArrayList<Asteroid>();
    private ArrayList<Asteroid> rmAsteroids = new ArrayList<Asteroid>();

    //towers
    private double towerSpawnTimer = 0;
    private static final float MIN_TOWER_SPAWN_TIME = 2f;
    private static final float MAX_TOWER_SPAWN_TIME = 8f;
    private ArrayList<Tower> towers = new ArrayList<Tower>();
    private ArrayList<Tower> rmTowers = new ArrayList<Tower>();

    //moons
    private float moonSpawnTimer = 0;
    private static final float MIN_MOON_SPAWN_TIME = 10f;
    private static final float MAX_MOON_SPAWN_TIME = 30f;
    private ArrayList<Moon> moons = new ArrayList<Moon>();
    private ArrayList<Moon> rmMoons = new ArrayList<Moon>();

    //ufos
    private float ufoSpawnTimer = 0;
    private static final float MIN_UFO_SPAWN_TIME = 5f;
    private static final float MAX_UFO_SPAWN_TIME = 15f;
    private ArrayList<UFO> ufos = new ArrayList<UFO>();
    private ArrayList<UFO> rmUfos = new ArrayList<UFO>();

    //rovers
    private float roverSpawnTimer = 0;
    private static final float MIN_ROVER_SPAWN_TIME = 2f;
    private static final float MAX_ROVER_SPAWN_TIME = 6f;
    private ArrayList<Rover> rovers = new ArrayList<Rover>();
    private ArrayList<Rover> rmRovers = new ArrayList<Rover>();


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

        //init tower timer
        towerSpawnTimer = rand.nextFloat() * (MAX_TOWER_SPAWN_TIME - MIN_TOWER_SPAWN_TIME) + MIN_TOWER_SPAWN_TIME;

        //init moon timer
        moonSpawnTimer = rand.nextFloat() * (MAX_MOON_SPAWN_TIME - MIN_MOON_SPAWN_TIME) + MIN_MOON_SPAWN_TIME;

        //init ufo timer
        ufoSpawnTimer = rand.nextFloat() * (MAX_UFO_SPAWN_TIME - MIN_UFO_SPAWN_TIME) + MIN_UFO_SPAWN_TIME;

        //init rover timer
        roverSpawnTimer = rand.nextFloat() * (MAX_ROVER_SPAWN_TIME - MIN_ROVER_SPAWN_TIME) + MIN_ROVER_SPAWN_TIME;

    }

    public void handleInput(float dt) {
        if (Gdx.input.isKeyPressed(W) && ship.position.y < 360)
            ship.moveUp();
        if (Gdx.input.isKeyPressed(S) && ship.position.y > 20)
            ship.moveDown();
        if (Gdx.input.isKeyPressed(A) && ship.position.x > leftShipBound)
            ship.moveLeft();
        if (Gdx.input.isKeyPressed(D) && ship.position.x < 200 + leftShipBound)
            ship.moveRight();
        if (Gdx.input.isKeyJustPressed(SPACE)) {
            bullets.add(new Bullet(ship.position.x + 20, ship.position.y + 8));
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

        //draw moons
        moonSpawnTimer -= delta;
        if (moonSpawnTimer <= 0) {
            moonSpawnTimer = rand.nextFloat() * (MAX_MOON_SPAWN_TIME - MIN_MOON_SPAWN_TIME) + MIN_MOON_SPAWN_TIME;
            moons.add(new Moon(gamecam.position.x + (gamecam.viewportWidth / 2), rand.nextInt(Moon.MAX_HEIGHT) + Moon.MIN_HEIGHT));
        }
        for (Moon moon : moons){
            game.batch.draw(moon.getTexture(), moon.getPosition().x, moon.getPosition().y);
        }

        //draw towers
        towerSpawnTimer -= delta;
        if (towerSpawnTimer <= 0) {
            towerSpawnTimer = rand.nextFloat() * (MAX_TOWER_SPAWN_TIME - MIN_TOWER_SPAWN_TIME) + MIN_TOWER_SPAWN_TIME;
            towers.add(new Tower(gamecam.position.x + (gamecam.viewportWidth / 2)));
        }
        for (Tower tower : towers){
            game.batch.draw(tower.getTexture(), tower.getTowerPos().x, tower.getTowerPos().y);
        }

        //draw asteroids
        asteroidSpawnTimer -= delta;
        if (asteroidSpawnTimer <= 0){
            asteroidSpawnTimer = rand.nextFloat() * (MAX_ASTEROID_SPAWN_TIME - MIN_ASTEROID_SPAWN_TIME) + MIN_ASTEROID_SPAWN_TIME;
            asteroids.add(new Asteroid(gamecam.position.x + (gamecam.viewportWidth / 2), rand.nextInt(Asteroid.MAX_HEIGHT) + Asteroid.MIN_HEIGHT));
        }
        for (Asteroid asteroid : asteroids) {
            game.batch.draw(asteroid.getTexture(), asteroid.getPosition().x, asteroid.getPosition().y);
        }

        //draw ufos
        ufoSpawnTimer -= delta;
        if (ufoSpawnTimer <= 0){
            ufoSpawnTimer = rand.nextFloat() * (MAX_UFO_SPAWN_TIME - MIN_UFO_SPAWN_TIME) + MIN_UFO_SPAWN_TIME;
            ufos.add(new UFO(gamecam.position.x + (gamecam.viewportWidth / 2)));
        }
        for (UFO ufo : ufos) {
            game.batch.draw(ufo.getTexture(), ufo.getPosition().x, ufo.getPosition().y);
        }

        //draw rovers
        roverSpawnTimer -= delta;
        if (roverSpawnTimer <= 0){
            roverSpawnTimer = rand.nextFloat() * (MAX_ROVER_SPAWN_TIME - MIN_ROVER_SPAWN_TIME) + MIN_ROVER_SPAWN_TIME;
            rovers.add(new Rover(gamecam.position.x + (gamecam.viewportWidth / 2)));
        }
        for (Rover rover : rovers) {
            game.batch.draw(rover.getTexture(), rover.getPosition().x, rover.getPosition().y);
        }

        //draw bullets
        for (Bullet bullet : bullets){
            game.batch.draw(bullet.getTexture(), bullet.getPosition().x, bullet.getPosition().y);
        }

        //draw ship
        game.batch.draw(ship.getTexture(), ship.getPosition().x, ship.getPosition().y);

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

        //update bullets
        for (Bullet bullet : bullets) {
            bullet.update(dt);

            if (gamecam.position.x + (gamecam.viewportWidth / 2) < bullet.getPosition().x){
                rmBullets.add(bullet);
            }
        }

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

        //update ufos
        for (UFO ufo : ufos){
            ufo.update(dt);

            if (gamecam.position.x - (gamecam.viewportWidth / 2) > ufo.getPosition().x + ufo.getTexture().getRegionWidth()){
                rmUfos.add(ufo);
                Hud.addScore(150);
            }

            if (ufo.collides(ship.getBounds()))
                game.setScreen(new GameOverScreen(game));
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

        //ufo bullet collision
        for (UFO ufo : ufos){
            for (Bullet bullet : bullets){
                if (bullet.collides(ufo.getBounds()))
                    rmBullets.add(bullet);
            }
        }

        asteroids.removeAll(rmAsteroids);
        bullets.removeAll(rmBullets);

        //update towers
        for (Tower tower : towers){

            if (gamecam.position.x - (gamecam.viewportWidth / 2) > tower.getTowerPos().x + tower.getTexture().getWidth()){
                rmTowers.add(tower);
                Hud.addScore(100);
            }

            if (tower.collides(ship.getBounds()))
                game.setScreen(new GameOverScreen(game));
        }
        towers.removeAll(rmTowers);

        //update moons
        for (Moon moon : moons) {
            moon.update(dt);

            if (gamecam.position.x - (gamecam.viewportWidth / 2) > moon.getPosition().x + moon.getTexture().getWidth()){
                rmMoons.add(moon);
                Hud.addScore(200);
            }

            if (moon.collides(ship.getBounds()))
                game.setScreen(new GameOverScreen(game));
        }
        moons.removeAll(rmMoons);

        for (Rover rover : rovers){
            rover.update(dt);

            if (gamecam.position.x - (gamecam.viewportWidth / 2) > rover.getPosition().x + rover.getTexture().getRegionWidth()){
                rmRovers.add(rover);
            }
        }

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
        for (Moon moon : moons){
            moon.dispose();
        }
        for (Moon moon : rmMoons){
            moon.dispose();
        }
        for (UFO ufo : ufos){
            ufo.dispose();
        }
        for (UFO ufo : rmUfos){
            ufo.dispose();
        }
        for (Rover rover : rovers){
            rover.dispose();
        }
        for (Rover rover : rmRovers){
            rover.dispose();
        }
    }
}

package com.justinmarotta.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.justinmarotta.game.MarsTrip;
import com.justinmarotta.game.scenes.PlayHud;
import com.justinmarotta.game.scenes.TouchPad;
import com.justinmarotta.game.sprites.Asteroid;
import com.justinmarotta.game.sprites.Bullet;
import com.justinmarotta.game.sprites.Moon;
import com.justinmarotta.game.sprites.Rover;
import com.justinmarotta.game.sprites.Ship;
import com.justinmarotta.game.sprites.SmallExplosion;
import com.justinmarotta.game.sprites.Tower;
import com.justinmarotta.game.sprites.UFO;

import java.util.ArrayList;
import java.util.Random;

import static com.badlogic.gdx.Input.Keys.A;
import static com.badlogic.gdx.Input.Keys.D;
import static com.badlogic.gdx.Input.Keys.DOWN;
import static com.badlogic.gdx.Input.Keys.LEFT;
import static com.badlogic.gdx.Input.Keys.RIGHT;
import static com.badlogic.gdx.Input.Keys.S;
import static com.badlogic.gdx.Input.Keys.SPACE;
import static com.badlogic.gdx.Input.Keys.UP;
import static com.badlogic.gdx.Input.Keys.W;

public class PlayScreen implements Screen {

    private MarsTrip game;
    public OrthographicCamera gamecam;
    public Viewport gamePort;
    private PlayHud playHud;
    private TouchPad touchPad;
    private Rectangle camBound;
    private float camInc;

    private Random rand;

    private Texture stars;
    private Texture hills;

    private Texture ground;

    private Vector2 starsPos1, starsPos2, hillsPos1, hillsPos2, groundPos1, groundPos2;

    private static Ship ship;

    //bullets
    private static ArrayList<Bullet> bullets = new ArrayList<Bullet>();
    private ArrayList<Bullet> rmBullets = new ArrayList<Bullet>();

    //asteroids
    private float asteroidSpawnTimer;
    private static final float MIN_ASTEROID_SPAWN_TIME = .25f;
    private static final float MAX_ASTEROID_SPAWN_TIME = 1.75f;
    private ArrayList<Asteroid> asteroids = new ArrayList<Asteroid>();
    private ArrayList<Asteroid> rmAsteroids = new ArrayList<Asteroid>();

    //towers
    private double towerSpawnTimer = 0;
    private static final float MIN_TOWER_SPAWN_TIME = 1f;
    private static final float MAX_TOWER_SPAWN_TIME = 5f;
    private ArrayList<Tower> towers = new ArrayList<Tower>();
    private ArrayList<Tower> rmTowers = new ArrayList<Tower>();

    //moons
    private float moonSpawnTimer = 0;
    private static final float MIN_MOON_SPAWN_TIME = 5f;
    private static final float MAX_MOON_SPAWN_TIME = 12f;
    private ArrayList<Moon> moons = new ArrayList<Moon>();
    private ArrayList<Moon> rmMoons = new ArrayList<Moon>();

    //ufos
    private float ufoSpawnTimer = 0;
    private static final float MIN_UFO_SPAWN_TIME = 3f;
    private static final float MAX_UFO_SPAWN_TIME = 8f;
    private ArrayList<UFO> ufos = new ArrayList<UFO>();
    private ArrayList<UFO> rmUfos = new ArrayList<UFO>();

    //rovers
    private float roverSpawnTimer = 0;
    private static final float MIN_ROVER_SPAWN_TIME = 10f;
    private static final float MAX_ROVER_SPAWN_TIME = 25f;
    private ArrayList<Rover> rovers = new ArrayList<Rover>();
    private ArrayList<Rover> rmRovers = new ArrayList<Rover>();

    //small explosions
    private ArrayList<SmallExplosion> smallExplosions = new ArrayList<SmallExplosion>();
    private ArrayList<SmallExplosion> rmSmallExplosions = new ArrayList<SmallExplosion>();


    //Constructor
    public PlayScreen(MarsTrip game) {
        this.game = game;
        gamecam = new OrthographicCamera();
        gamePort = new FitViewport((MarsTrip.WIDTH / 2), (MarsTrip.HEIGHT / 2), gamecam);
        gamecam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);
        camBound = new Rectangle(0, 0, gamePort.getWorldWidth(), gamePort.getWorldHeight());
        playHud = new PlayHud(game.batch);
        touchPad = new TouchPad(game.batch);

        rand = new Random();

        stars = new Texture("stars.png");
        starsPos1 = new Vector2(gamePort.getScreenWidth(), 0);
        starsPos2 = new Vector2(stars.getWidth(), 0);

        hills = new Texture("hills.png");
        hillsPos1 = new Vector2(gamePort.getScreenWidth(), 0);
        hillsPos2 = new Vector2(hills.getWidth(), 0);

        ground = new Texture("ground.png");
        groundPos1 = new Vector2(gamePort.getScreenWidth(), 0);
        groundPos2 = new Vector2(ground.getWidth(), 0);

        ship = new Ship((gamePort.getWorldWidth() / 4), gamePort.getWorldHeight() / 2);
        MarsTrip.manager.get("audio/ship.ogg", Sound.class).loop(.05f);


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
        if (Gdx.input.isKeyPressed(W) && ship.position.y < (gamePort.getWorldHeight() - 40))
            ship.moveUp(5);
        if (Gdx.input.isKeyPressed(S) && ship.position.y > ground.getHeight())
            ship.moveDown(-5);
        if (Gdx.input.isKeyPressed(A) && ship.position.x > camBound.getX())
            ship.moveLeft(-4);
        if (Gdx.input.isKeyPressed(D) && ship.position.x < (camBound.getX() + camBound.getWidth() - ship.getTexture().getRegionWidth()))
            ship.moveRight(3);

        if (Gdx.input.isKeyPressed(UP) && ship.position.y < (gamePort.getWorldHeight() - 40) && !Gdx.input.isKeyPressed(W))
            ship.moveUp(5);
        if (Gdx.input.isKeyPressed(DOWN) && ship.position.y > ground.getHeight() && !Gdx.input.isKeyPressed(S))
            ship.moveDown(-5);
        if (Gdx.input.isKeyPressed(LEFT) && ship.position.y > camBound.getX() && !Gdx.input.isKeyPressed(A))
            ship.moveLeft(-4);
        if (Gdx.input.isKeyPressed(RIGHT) && ship.position.x < (camBound.getX() + camBound.getWidth() - ship.getTexture().getRegionWidth()) && !Gdx.input.isKeyPressed(D))
            ship.moveRight(3);

        if (TouchPad.touchpad.getKnobPercentY() > 0 && ship.position.y < (gamePort.getWorldHeight() - 40))
            ship.moveUp(TouchPad.touchpad.getKnobPercentY() * 5);
        if (TouchPad.touchpad.getKnobPercentY() < 0 && ship.position.y > ground.getHeight())
            ship.moveDown(TouchPad.touchpad.getKnobPercentY() * 5);
        if (TouchPad.touchpad.getKnobPercentX() < 0 && ship.position.x > camBound.getX())
            ship.moveLeft(TouchPad.touchpad.getKnobPercentX() * 4);
        if (TouchPad.touchpad.getKnobPercentX() > 0 && ship.position.x < (camBound.getX() + camBound.getWidth() - ship.getTexture().getRegionWidth()))
            ship.moveRight(TouchPad.touchpad.getKnobPercentX() * 3);

        if (Gdx.input.isKeyJustPressed(SPACE)){
            shoot();
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

        game.batch.draw(stars, starsPos1.x, starsPos1.y);
        game.batch.draw(stars, starsPos2.x, starsPos2.y);
        game.batch.draw(hills, hillsPos1.x, hillsPos1.y);
        game.batch.draw(hills, hillsPos2.x, hillsPos2.y);

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
            MarsTrip.manager.get("audio/asteroid.ogg", Sound.class).play();
        }
        for (Asteroid asteroid : asteroids) {
            game.batch.draw(asteroid.getTexture(), asteroid.getPosition().x, asteroid.getPosition().y);
        }

        //draw ufos
        ufoSpawnTimer -= delta;
        if (ufoSpawnTimer <= 0){
            ufoSpawnTimer = rand.nextFloat() * (MAX_UFO_SPAWN_TIME - MIN_UFO_SPAWN_TIME) + MIN_UFO_SPAWN_TIME;
            ufos.add(new UFO(gamecam.position.x + (gamecam.viewportWidth / 2)));
            MarsTrip.manager.get("audio/ufo.ogg", Sound.class).play();
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

        //draw small explosions
        for (SmallExplosion smallExplosion : smallExplosions){
            game.batch.draw(smallExplosion.getTexture(), smallExplosion.getPosition().x, smallExplosion.getPosition().y);
        }

        //draw ground
        game.batch.draw(ground, groundPos1.x, groundPos1.y);
        game.batch.draw(ground, groundPos2.x, groundPos2.y);

        //end game batch
        game.batch.end();

        //draw what playHud sees
        game.batch.setProjectionMatrix(playHud.stage.getCamera().combined);
        playHud.stage.draw();

        if (MenuScreen.touchUser) {
            touchPad.stage.draw();
        }
    }

    public void update(float dt) {
        handleInput(dt);

        //cam positioning
        camInc = (Ship.MOVEMENT / (1 / dt));
        gamecam.position.add(camInc, 0, 0);
        camBound.setX(gamecam.position.x - (gamePort.getWorldWidth() / 2));

        playHud.update(dt);

        ship.update(dt);

        updateStars(220 * dt);
        updateHills(100 * dt);
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
                PlayHud.addScore(50);
            }

            if (asteroid.collides(ship.getBounds())) {
                killPlayer();
            }
        }

        //update ufos
        for (UFO ufo : ufos){
            ufo.update(dt);

            if (gamecam.position.x - (gamecam.viewportWidth / 2) > ufo.getPosition().x + ufo.getTexture().getRegionWidth()){
                rmUfos.add(ufo);
                PlayHud.addScore(150);
            }

            if (ufo.collides(ship.getBounds())) {
                killPlayer();
            }
        }
        ufos.removeAll(rmUfos);

        //asteroid bullet collision
        for (Bullet bullet : bullets){
            for (Asteroid asteroid : asteroids){
                if (bullet.collides(asteroid.getBounds())){
                    rmAsteroids.add(asteroid);
                    rmBullets.add(bullet);
                    smallExplosions.add(new SmallExplosion(asteroid.getPosition().x, asteroid.getPosition().y, Asteroid.MOVEMENT));
                    MarsTrip.manager.get("audio/smallexplosion.ogg", Sound.class).play();
                    PlayHud.addScore(250);
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
        for (Tower tower : towers) {

            if (gamecam.position.x - (gamecam.viewportWidth / 2) > tower.getTowerPos().x + tower.getTexture().getWidth()) {
                rmTowers.add(tower);
                PlayHud.addScore(100);
            }

            if (tower.collides(ship.getBounds())){
                killPlayer();
            }
        }
        towers.removeAll(rmTowers);

        //update moons
        for (Moon moon : moons) {
            moon.update(dt);

            if (gamecam.position.x - (gamecam.viewportWidth / 2) > moon.getPosition().x + moon.getTexture().getWidth()){
                rmMoons.add(moon);
                PlayHud.addScore(200);
            }

            if (moon.collides(ship.getBounds())) {
                killPlayer();
            }
        }
        moons.removeAll(rmMoons);

        //update rovers
        for (Rover rover : rovers){
            rover.update(dt);

            if (gamecam.position.x - (gamecam.viewportWidth / 2) > rover.getPosition().x + rover.getTexture().getRegionWidth()){
                rmRovers.add(rover);
            }
        }
        rovers.removeAll(rmRovers);


        //update small explosions
        for (SmallExplosion smallExplosion : smallExplosions){
            smallExplosion.update(dt);

            if (gamecam.position.x - (gamecam.viewportWidth / 2) > smallExplosion.getPosition().x + smallExplosion.getTexture().getRegionWidth()){
                rmSmallExplosions.add(smallExplosion);
            }
        }
        smallExplosions.removeAll(rmSmallExplosions);

        //update the game camera
        gamecam.update();
    }

    private void updateStars(float x){
        starsPos1.add(x, 0);
        starsPos2.add(x, 0);
        if (gamecam.position.x - (gamecam.viewportWidth / 2) > starsPos1.x + stars.getWidth())
            starsPos1.add(stars.getWidth() * 2, 0);
        if (gamecam.position.x - (gamecam.viewportWidth / 2)  > starsPos2.x + stars.getWidth())
            starsPos2.add(stars.getWidth() * 2, 0);
    }

    private void updateHills(float x){
        hillsPos1.add(x, 0);
        hillsPos2.add(x, 0);
        if (gamecam.position.x - (gamecam.viewportWidth / 2) > hillsPos1.x + hills.getWidth())
            hillsPos1.add(hills.getWidth() * 2, 0);
        if (gamecam.position.x - (gamecam.viewportWidth / 2)  > hillsPos2.x + hills.getWidth())
            hillsPos2.add(hills.getWidth() * 2, 0);
    }

    private void updateGround(){
        if (gamecam.position.x - (gamecam.viewportWidth / 2) > groundPos1.x + ground.getWidth())
            groundPos1.add(ground.getWidth() * 2, 0);
        if (gamecam.position.x - (gamecam.viewportWidth / 2)  > groundPos2.x + ground.getWidth())
            groundPos2.add(ground.getWidth() * 2, 0);
    }

    private void killPlayer(){
        game.setScreen(new GameOverScreen(game));
        MarsTrip.manager.get("audio/ship.ogg", Sound.class).stop();

    }

    public static void shoot(){
        bullets.add(new Bullet(ship.position.x + 20, ship.position.y + 8));
        MarsTrip.manager.get("audio/bullet.ogg", Sound.class).play();
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
        stars.dispose();
        hills.dispose();
        ship.dispose();
        ground.dispose();
        touchPad.dispose();
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

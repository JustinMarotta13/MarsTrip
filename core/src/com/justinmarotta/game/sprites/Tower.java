package com.justinmarotta.game.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.justinmarotta.game.MarsTrip;

import java.util.Random;

public class Tower {
    public static final int TOWER_WIDTH = 52;
    private static final int LOWEST_SPAWN = -260;
    private static final int GAP_MIN = 60;
    private static final int MAX_GAP_ADDITION = 80;
    private static int botHeight;
    private static int towerGap;

    private Texture topTower, bottomTower;
    private Vector2 posTopTower, posBotTower;
    private Rectangle boundsTop, boundsBot;
    private Random rand;

    public Tower(float x){
        topTower = new Texture("toptower.png");
        bottomTower = new Texture("bottomtower.png");
        rand = new Random();

        posBotTower = new Vector2(x, LOWEST_SPAWN +  genBotHeight());
        posTopTower = new Vector2(x + 160 + 26, posBotTower.y + topTower.getHeight() + genTowerGap());

        boundsTop = new Rectangle(posTopTower.x, posTopTower.y, topTower.getWidth(), topTower.getHeight());
        boundsBot = new Rectangle(posBotTower.x, posBotTower.y, bottomTower.getWidth(), bottomTower.getHeight());
    }

    private int genBotHeight(){
        botHeight = rand.nextInt(MarsTrip.HEIGHT / 4);
        return botHeight;
    }

    private int genTowerGap(){
        towerGap = rand.nextInt(MAX_GAP_ADDITION) + GAP_MIN;
        return towerGap;
    }

    public boolean collides(Rectangle rect){
        return rect.overlaps(boundsTop) || rect.overlaps(boundsBot);
    }

    public Texture getTopTower() {
        return topTower;
    }

    public Texture getBottomTower() {
        return bottomTower;
    }

    public Vector2 getPosTopTower() {
        return posTopTower;
    }

    public Vector2 getPosBotTower() {
        return posBotTower;
    }

    public void dispose(){
        topTower.dispose();
        bottomTower.dispose();
    }
}

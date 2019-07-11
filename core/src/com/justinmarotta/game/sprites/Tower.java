package com.justinmarotta.game.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.Random;

public class Tower {
    public static final int TOWER_WIDTH = 52;
    private static final int FLUCTUATION = 200;
    private static final int LOWEST_OPENING = 70;
    private static final int GAP_TOLERANCE = 60;
    private static int towerGap;

    private Texture topTower, bottomTower;
    private Vector2 posTopTower, posBotTower;
    private Rectangle boundsTop, boundsBot;
    private Random rand;

    public Tower(float x){
        topTower = new Texture("toptower.png");
        bottomTower = new Texture("bottomtower.png");
        rand = new Random();
        towerGap = rand.nextInt(100);

        posTopTower = new Vector2(x + 150,  LOWEST_OPENING + towerGap + GAP_TOLERANCE + rand.nextInt(FLUCTUATION));
        posBotTower = new Vector2(x + 300, posTopTower.y - towerGap - GAP_TOLERANCE - bottomTower.getHeight());

        boundsTop = new Rectangle(posTopTower.x, posTopTower.y, topTower.getWidth(), topTower.getHeight());
        boundsBot = new Rectangle(posBotTower.x, posBotTower.y, bottomTower.getWidth(), bottomTower.getHeight());
    }

    public void reposition(float x){
        posTopTower.set(x + 150, LOWEST_OPENING + towerGap + GAP_TOLERANCE + rand.nextInt(FLUCTUATION));
        posBotTower.set(x + 250, posTopTower.y - towerGap - GAP_TOLERANCE - bottomTower.getHeight());
        boundsTop.setPosition(posTopTower.x, posTopTower.y);
        boundsBot.setPosition(posBotTower.x, posBotTower.y);
    }

    public boolean collides(Rectangle player){
        return player.overlaps(boundsTop) || player.overlaps(boundsBot);
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

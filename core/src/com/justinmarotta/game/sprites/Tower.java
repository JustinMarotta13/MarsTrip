package com.justinmarotta.game.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.justinmarotta.game.MarsTrip;

import java.util.Random;

public class Tower {

    public static final int TOWER_WIDTH = 52;
    private static final int LOWEST_SPAWN = -220;
    private static int towerHeight;
    private static int towerGap;

    private Texture tower;
    private Vector2 towerPos;
    private Rectangle bounds;
    private Random rand;

    public Tower(float x){
        tower = new Texture("tower.png");
        rand = new Random();

        towerPos = new Vector2(x, LOWEST_SPAWN +  genTowerHeight());

        bounds = new Rectangle(towerPos.x, towerPos.y, tower.getWidth(), tower.getHeight());
    }

    private int genTowerHeight(){
        towerHeight = rand.nextInt(MarsTrip.HEIGHT / 4);
        return towerHeight;
    }

    public boolean collides(Rectangle rect){
        return rect.overlaps(bounds) || rect.overlaps(bounds);
    }

    public Texture getTexture() {
        return tower;
    }


    public Vector2 getTowerPos() {
        return towerPos;
    }

    public void dispose(){
        tower.dispose();
    }
}

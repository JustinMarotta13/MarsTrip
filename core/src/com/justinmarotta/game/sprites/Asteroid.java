package com.justinmarotta.game.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

public class Asteroid {
    private static final int MOVEMENT = -200;
    public static final int HEIGHT_MAX = 420;
    private Vector3 position;
    private Vector3 velocity;
    private Texture texture;
    private static Rectangle bounds;

    public Asteroid(int x, int y){
        texture = new Texture("asteroid.png");
        position = new Vector3(x, y, 0);
        velocity = new Vector3(0, 0, 0);
        bounds = new Rectangle(x, y, texture.getWidth(), texture.getHeight());
    }

    public void update(float dt){
        velocity.scl(dt);
        position.add(MOVEMENT * dt, velocity.y, 0);
        position.add(0, velocity.y, 0);
        velocity.scl(1/dt);
        bounds.setPosition(position.x, position.y);
    }

    public void reposition(int x, int y){
        position.set(x, y, 0);
    }

    public boolean collides(Rectangle player){
        return player.overlaps(bounds);
    }

    public Rectangle getBounds(){
        return bounds;
    }

    public Vector3 getPosition() {
        return position;
    }

    public Texture getTexture(){
        return texture;
    }

    public void dispose(){
        texture.dispose();
    }
}

package com.justinmarotta.game.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

public class Bullet {
    public static final int MOVEMENT = 700;
    public Vector3 position;
    private Vector3 velocity;
    private Rectangle bounds;
    private Texture texture;

    public Bullet(float x, float y){
        position = new Vector3(x, y, 0);
        velocity = new Vector3(0, 0, 0);
        texture = new Texture("bullet.png");
        bounds = new Rectangle(x, y, texture.getWidth(), texture.getHeight());
    }

    public void update(float dt){
        velocity.scl(dt);
        position.add(MOVEMENT * dt, velocity.y, 0);
        position.add(0, velocity.y, 0);
        velocity.scl(1/dt);
        bounds.setPosition(position.x, position.y);
    }

    public boolean collides(Rectangle asteroid){
        return asteroid.overlaps(bounds);
    }

    public Vector3 getPosition() {
        return position;
    }

    public Texture getTexture(){
        return texture;
    }

    public void dispose() {
        texture.dispose();
    }
}

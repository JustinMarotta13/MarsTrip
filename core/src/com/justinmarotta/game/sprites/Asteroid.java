package com.justinmarotta.game.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.justinmarotta.game.MarsTrip;

public class Asteroid {
    private static final int MOVEMENT = -100;
    public static final int MIN_HEIGHT = 50;
    public static final int MAX_HEIGHT = (MarsTrip.HEIGHT / 2) - MIN_HEIGHT - 50;
    private Vector3 position;
    private Vector3 velocity;
    private Texture texture;
    private Rectangle bounds;

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

    public boolean collides(Rectangle rect){
        return rect.overlaps(bounds);
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

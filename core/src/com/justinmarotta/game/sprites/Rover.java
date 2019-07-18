package com.justinmarotta.game.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

public class Rover {
    public static final int HEIGHT = 20;
    private static final int MOVEMENT = 175;
    public Vector3 position;
    private Vector3 velocity;
    private Rectangle bounds;
    private Animation animation;
    private Texture texture;

    public Rover(float x) {
        position = new Vector3(x, HEIGHT, 0);
        velocity = new Vector3(0, 0, 0);
        texture = new Texture("roveranimation.png");
        animation = new Animation(new TextureRegion(texture), 3, 0.5f);
        bounds = new Rectangle(x, HEIGHT, texture.getWidth() / 3, texture.getHeight());
    }

    public void update(float dt){
        animation.update(dt);
        velocity.scl(dt);
        position.add(MOVEMENT * dt, velocity.y, 0);
        position.add(0, velocity.y, 0);
        velocity.scl(1/dt);
        bounds.setPosition(position.x, position.y);
    }

    public boolean collides(Rectangle rect){
        return rect.overlaps(bounds);
    }

    public TextureRegion getTexture() {
        return animation.getFrame();
    }

    public Vector3 getPosition() {
        return position;
    }

    public Rectangle getBounds(){
        return bounds;
    }

    public void dispose() {
        texture.dispose();
    }
}

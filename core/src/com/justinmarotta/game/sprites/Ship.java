package com.justinmarotta.game.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

public class Ship {
    public static final int MOVEMENT = 240;
    public Vector3 position;
    private Vector3 velocity;
    private Rectangle bounds;
    private Animation animation;
    private Texture texture;

    public Ship(float x, float y) {
        position = new Vector3(x, y, 0);
        velocity = new Vector3(0, 0, 0);
        texture = new Texture("shipanimation.png");
        animation = new Animation(new TextureRegion(texture), 3, 0.25f);
        bounds = new Rectangle(x + 10, y, (texture.getWidth() / 3) - 10, texture.getHeight());
    }

    public void update(float dt){
        animation.update(dt);
        velocity.scl(dt);
        position.add(MOVEMENT * dt, velocity.y, 0);
        position.add(0, velocity.y, 0);
        velocity.scl(1/dt);
        bounds.setPosition(position.x, position.y);
    }

    public void moveUp(float y){
        position.add(0, y, 0);
    }

    public void moveDown(float y){
        position.add(0, y, 0);
    }

    public void moveLeft(float x){
        position.add(x, 0, 0);
    }

    public void moveRight(float x){
        position.add(x, 0, 0);
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

    public boolean collides(Rectangle rect){
        return rect.overlaps(bounds);
    }

    public void dispose() {
        texture.dispose();
    }
}

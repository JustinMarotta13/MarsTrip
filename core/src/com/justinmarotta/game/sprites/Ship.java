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
    private Animation shipAnimation;
    private Texture texture;

    public Ship(int x, int y) {
        position = new Vector3(x, y, 0);
        velocity = new Vector3(0, 0, 0);
        texture = new Texture("shipanimation.png");
        shipAnimation = new Animation(new TextureRegion(texture), 3, 0.5f);
        bounds = new Rectangle(x, y, texture.getWidth() / 3, texture.getHeight());
    }

    public void update(float dt){
        shipAnimation.update(dt);
        velocity.scl(dt);
        position.add(MOVEMENT * dt, velocity.y, 0);
        position.add(0, velocity.y, 0);
        velocity.scl(1/dt);
        bounds.setPosition(position.x, position.y);
    }

    public void moveUp(){
        position.add(0, 4, 0);
    }

    public void moveDown(){
        position.add(0, -4, 0);
    }

    public void moveLeft(){
        position.add(-3, 0, 0);
    }

    public void moveRight(){
        position.add(2, 0, 0);
    }

    public TextureRegion getTexture() {
        return shipAnimation.getFrame();
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

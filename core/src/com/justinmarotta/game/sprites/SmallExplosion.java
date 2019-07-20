package com.justinmarotta.game.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;

public class SmallExplosion {
    public Vector3 position;
    private Vector3 velocity;
    private Animation animation;
    private Texture texture;
    private int movement;

    public SmallExplosion(float x, float y, int movement) {
        this.movement = movement;
        position = new Vector3(x, y, 0);
        velocity = new Vector3(0, 0, 0);
        texture = new Texture("smallexplosion.png");
        animation = new Animation(new TextureRegion(texture), 3, 0.25f);
    }

    public void update(float dt){
        animation.update(dt);
        velocity.scl(dt);
        position.add(movement * dt, velocity.y, 0);
        position.add(0, velocity.y, 0);
        velocity.scl(1/dt);
    }

    public TextureRegion getTexture() {
        return animation.getFrame();
    }

    public Vector3 getPosition() {
        return position;
    }

    public void dispose() {
        texture.dispose();
    }
}

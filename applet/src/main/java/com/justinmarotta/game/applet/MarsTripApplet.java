package com.justinmarotta.game.applet;


import com.badlogic.gdx.backends.lwjgl.LwjglApplet;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.justinmarotta.game.MarsTrip;

public class MarsTripApplet extends LwjglApplet {

    private static LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
    private static final long serialVersionUID = 1L;

    public MarsTripApplet() {
        super(new MarsTrip(), config);
        config.width = MarsTrip.WIDTH;
        config.height = MarsTrip.HEIGHT;
    }
}

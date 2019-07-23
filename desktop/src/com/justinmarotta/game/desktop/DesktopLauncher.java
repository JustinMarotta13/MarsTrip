package com.justinmarotta.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.justinmarotta.game.MarsTrip;

public class DesktopLauncher {

	public static void main (String[] arg) {

		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = MarsTrip.WIDTH;
		config.height = MarsTrip.HEIGHT;
		new LwjglApplication(new MarsTrip(), config);
	}
}

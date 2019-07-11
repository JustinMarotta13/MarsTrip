package com.justinmarotta.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.justinmarotta.game.screens.MenuScreen;

public class MarsTrip extends Game {
	public static final int WIDTH = 480;
	public static final int HEIGHT = 800;

	public SpriteBatch batch;

	public MenuScreen menuScreen;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		menuScreen = new MenuScreen(this);
		setScreen(menuScreen);
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		super.dispose();
		batch.dispose();
	}
}

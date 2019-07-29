package com.justinmarotta.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.justinmarotta.game.screens.MenuScreen;

public class MarsTrip extends Game {
	public static final int WIDTH = 480;
	public static final int HEIGHT = 800;

	public static AssetManager manager;

	public SpriteBatch batch;

	private MenuScreen menuScreen;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		manager = new AssetManager();
		manager.load("audio/music.mp3", Music.class);
		manager.load("audio/asteroid.ogg", Sound.class);
		manager.load("audio/smallexplosion.ogg", Sound.class);
		manager.load("audio/bullet.ogg", Sound.class);
		manager.load("audio/ufo.ogg", Sound.class);
		manager.load("audio/ship.ogg", Sound.class);
		manager.finishLoading();
		menuScreen = new MenuScreen(this);
		setScreen(menuScreen);
	}

	@Override
	public void render () {
		super.render();
	}

	public static void playMusic(){
		manager.get("audio/music.mp3", Music.class).setLooping(true);
		manager.get("audio/music.mp3", Music.class).play();
	}
	
	@Override
	public void dispose () {
		super.dispose();
		batch.dispose();
		manager.dispose();
	}
}

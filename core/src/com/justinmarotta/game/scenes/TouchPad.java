package com.justinmarotta.game.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.justinmarotta.game.MarsTrip;

public class TouchPad implements Disposable {
    public Stage stage;

    public Touchpad.TouchpadStyle touchpadStyle;
    public Touchpad touchpad;
    private Skin touchpadSkin;
    private Drawable touchBackground;
    private Drawable touchKnob;

    public TouchPad(SpriteBatch sb){
        Viewport viewport;
        viewport = new FitViewport(MarsTrip.WIDTH, MarsTrip.HEIGHT, new OrthographicCamera());

        //Create a touchpad skin
        touchpadSkin = new Skin();

        //Set background and knob image
        touchpadSkin.add("touchBackground", new Texture("data/touchBackground.png"));
        touchpadSkin.add("touchKnob", new Texture("data/touchKnob.png"));

        //Create TouchPad Style
        touchpadStyle = new Touchpad.TouchpadStyle();

        //Create Drawable's from TouchPad skin
        touchBackground = touchpadSkin.getDrawable("touchBackground");
        touchKnob = touchpadSkin.getDrawable("touchKnob");

        //Apply the Drawables to the TouchPad Style
        touchpadStyle.background = touchBackground;
        touchpadStyle.knob = touchKnob;

        touchpad = new Touchpad(10, touchpadStyle);
        touchpad.setBounds(15, 15, 200, 200);

        stage = new Stage(viewport, sb);
        stage.addActor(touchpad);
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}

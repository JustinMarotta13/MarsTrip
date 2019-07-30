package com.justinmarotta.game.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.justinmarotta.game.MarsTrip;
public class TouchPad implements Disposable {
    public Stage stage;

    public static Touchpad touchpad;

    public static Button fireBtn;

    public TouchPad(SpriteBatch sb){
        Viewport viewport;
        viewport = new FitViewport(MarsTrip.WIDTH, MarsTrip.HEIGHT, new OrthographicCamera());

        //Create a touchpad skin
        Skin touchpadSkin = new Skin();

        //Set background and knob image
        touchpadSkin.add("touchBackground", new Texture("touchBackground.png"));
        touchpadSkin.add("touchKnob", new Texture("touchKnob.png"));


        //Create Drawable's from TouchPad skin
        Drawable touchBackground = touchpadSkin.getDrawable("touchBackground");
        Drawable touchKnob = touchpadSkin.getDrawable("touchKnob");

        //Create TouchPad Style
        Touchpad.TouchpadStyle touchpadStyle = new Touchpad.TouchpadStyle();

        //Apply the Drawables to the TouchPad Style
        touchpadStyle.background = touchBackground;
        touchpadStyle.knob = touchKnob;

        touchpad = new Touchpad(10, touchpadStyle);
        touchpad.setBounds(15, 15, 150, 150);

        Skin fireBtnSkin = new Skin();
        fireBtnSkin.add("fireBtnUp", new Texture("fireBtnUp.png"));
        fireBtnSkin.add("fireBtnDown", new Texture("fireBtnDown.png"));
        Drawable fireBtnUp = fireBtnSkin.getDrawable("fireBtnUp");
        Drawable fireBtnDown = fireBtnSkin.getDrawable("fireBtnDown");
        fireBtn = new Button(fireBtnUp, fireBtnDown);
        fireBtn.setBounds(viewport.getWorldWidth() - (viewport.getWorldWidth() / 4), 55, 72, 72);


        stage = new Stage(viewport, sb);
        stage.addActor(touchpad);
        stage.addActor(fireBtn);
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}

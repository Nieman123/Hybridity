package com.solanum.hybridity;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.awt.*;

/**
 * @Aldous
 */
public class SplashScreen implements Screen {
    Texture tex;
    Sprite splash;
    Hybridity game;
    SpriteBatch batch;
    Sound chime;
    boolean sliding =true;
    float idleTime;

    SplashScreen(Hybridity session){
        game = (Hybridity)session;
        tex = new Texture("PantheonGames.png");
        splash = new Sprite(tex);

        splash.setPosition(Gdx.graphics.getWidth()/2-splash.getWidth()/2 ,Gdx.graphics.getHeight());

        batch = new SpriteBatch();

        chime = Gdx.audio.newSound(Gdx.files.internal("sounds/gb.wav"));

    }
    @Override
    public void render(float delta) {
        Gdx.graphics.getGL20().glClearColor(1,1,1,1);
        Gdx.graphics.getGL20().glClear( GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT );
        if(sliding){
            splash.translate(0, -150 * delta);
            if(splash.getY()<Gdx.graphics.getHeight()/2-splash.getHeight()/2){
                sliding=false;
                chime.play();

            }
        }else{
            idleTime+=delta;
            if(idleTime>5){
                game.setScreen(game.gameScreen);
                this.dispose();
            }
        }


        batch.begin();
        splash.draw(batch);
        batch.end();

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void show() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }
}

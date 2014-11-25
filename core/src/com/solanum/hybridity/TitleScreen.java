package com.solanum.hybridity;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * @Aldous
 */
public class TitleScreen implements Screen {

    Texture tex;
    Sprite sprite;
    SpriteBatch batch;
    Music music;
    int h;
    int w;

    private final Hybridity game;

    public TitleScreen(Hybridity session) {
        music = Gdx.audio.newMusic(Gdx.files.internal("sounds/title.mp3"));
        tex = new Texture("TitleLogo.png");
        sprite = new Sprite(tex);
        h = Gdx.graphics.getHeight();
        w = Gdx.graphics.getWidth();

        sprite.setPosition(w/2-sprite.getWidth()/2, (h/2)-sprite.getHeight()/2);
        game = session;

        batch = new SpriteBatch();

        music.setVolume(.5f);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        sprite.draw(batch);
        batch.end();

        if(Gdx.input.isKeyPressed(Input.Keys.SPACE)){
            game.setScreen(game.gameScreen);
        }
    }

    @Override
    public void resize(int width, int height) {
        w =width;
        h =height;
    }

    @Override
    public void show() {
        music.play();

    }

    @Override
    public void hide() {
        music.stop();

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

package com.solanum.hybridity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * @Aldous The TitleScreen is an implementation of the Screen interface and acts as an interchange for other screens to
 * be set as active. It listens for input from the space bar and starts the game.
 */

public class TitleScreen implements Screen {

    private final Hybridity game;
    private Texture tex;
    private Sprite sprite;
    private SpriteBatch batch;
    private Music music;
    private int h;
    private int w;

    /**
     * Instantiates the music and art for the screen and maintains a reference to the
     *
     * @param session
     */
    public TitleScreen(Hybridity session) {

        music = Gdx.audio.newMusic(Gdx.files.internal("sounds/title.mp3"));
        tex = new Texture("TitleLogo.png");
        sprite = new Sprite(tex);
        h = Gdx.graphics.getHeight();
        w = Gdx.graphics.getWidth();

        sprite.setPosition(w / 2 - sprite.getWidth() / 2, (h / 2) - sprite.getHeight() / 2);
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

        /**
         * Listens for input from the Space bar and starts the game
         */
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            game.setScreen(game.gameScreen);
        }
    }

    /**
     * All methods below are mandated by the Screen interface and are called when the GDX application experiences
     * certain criteria or state changes.
     */

    @Override
    public void resize(int width, int height) {
        w = width;
        h = height;
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

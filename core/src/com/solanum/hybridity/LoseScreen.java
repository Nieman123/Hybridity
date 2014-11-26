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
 * @author Aldous
 * "Crimewave" - Crystal Castles
 *
 */
class LoseScreen implements Screen{
    private Texture tex;
    private Sprite sprite;
    private SpriteBatch batch;
    Music music;
    private int h;
    private int w;

    private final Hybridity game;

    public LoseScreen(Hybridity session) {
        tex = new Texture("lose.png");
        sprite = new Sprite(tex);
        h = Gdx.graphics.getHeight();
        w = Gdx.graphics.getWidth();

        sprite.setPosition(w/2-sprite.getWidth()/2, (h/2)-sprite.getHeight()/2);
        game = session;

        batch = new SpriteBatch();

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        sprite.draw(batch);
        batch.end();

        if(Gdx.input.isKeyPressed(Input.Keys.ESCAPE)){
            Gdx.app.exit();
        }
    }

    @Override
    public void resize(int width, int height) {
        w =width;
        h =height;
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

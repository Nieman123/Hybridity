package com.solanum.hybridity;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class GameScreen implements Screen {
    Game game;
    SpriteBatch batch;
    Texture img;
    Stage gameStage;
    int numOfSeeds = 3;
    float degreeDivision;
    Mainland ml;
    Music music;


    GameScreen(Hybridity session) {
        game = session;
        gameStage = new Stage();

        Player player = new Player();
        ml = new Mainland(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
        gameStage.addActor(ml);
        gameStage.addActor(player);

        degreeDivision = 360 / numOfSeeds;


        for (int i = 1; i <= numOfSeeds; i++) {
            plantSeed(i * degreeDivision, 900 * i);
        }


        music = Gdx.audio.newMusic(Gdx.files.internal("sounds/gamePlay.mp3"));
        music.setVolume(.5f);

    }




    public void plantSeed(float angle, float distance) {

        angle = angle % 360;
        float nAngle = angle % 90;

        float adjacent;
        float opposite;
        float hypoteneuse = distance;
        double sin = Math.sin(Math.toRadians(nAngle));

        opposite = (float) sin * hypoteneuse;

        adjacent = (float) Math.sqrt((hypoteneuse * hypoteneuse) - (opposite * opposite));

        float newX;
        float newY;

        if (angle < 180) {
            if (angle >= 90) {
                newX = -opposite;
                newY = adjacent;
            } else {
                newX = adjacent;
                newY = opposite;
            }
        } else {
            if (angle >= 270) {
                newX = opposite;
                newY = -adjacent;
            } else {
                newX = -adjacent;
                newY = -opposite;
            }
        }

        gameStage.addActor(new Seeder(ml.oX + newX, ml.oY + newY, ml.oX, ml.oY));




    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        gameStage.act();
        gameStage.draw();

    }

    @Override
    public void resize(int width, int height) {

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
        music.pause();
    }

    @Override
    public void resume() {
        music.play();

    }

    @Override
    public void dispose() {

    }
}

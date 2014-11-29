package com.solanum.hybridity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;


/**
 * @author Aldous
 *         <p/>
 *         "Splinter Cell Chaos Theory" - Amon Tobin
 *         <p/>
 *         <p/>
 *         The GameScreen class contains all of the logic which runs the game loop and phases.
 *         It contains a stage, and acts as a top level game manager which will reset the game,
 *         handle enemy instantiations and can be reset if necessary in order to start a new round.
 *         <p/>
 *         Conceptually, it can best be compared the board for a boardgame which can be reset and repurposed.
 */

public class GameScreen implements Screen {


    public static int phase = 1;
    private final Hybridity game;
    private final Stage gameStage;
    private final Mainland ml;
    private final int numOfSeeds = 1;
    private final Music music = Gdx.audio.newMusic(Gdx.files.internal("sounds/gamePlay.mp3"));


    /**
     * @param session A reference to the over all GDX Game class so that it can refer back to itself and change the
     *                current screen (Such as a gameOver or optionsScreen).
     */
    GameScreen(Hybridity session) {
        game = session;
        gameStage = new Stage();

        Player player = new Player();
        ml = new Mainland(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
        gameStage.addActor(ml);
        gameStage.addActor(player);


        /**
         * Divides a 360 degree circle by the amount of specified enemies and then stargt
         */
        if (numOfSeeds > 0) {
            float degreeDivision = 360 / numOfSeeds;

            for (int i = 1; i <= numOfSeeds; i++) {
                plantSeed(i * degreeDivision, 900 * i);
            }
        }


        /**
         * Sets the music at half volume for levelling purposes.
         */
        music.setVolume(.5f);

    }


    /**
     * Places a Seeder at the specified angle relative to the origin of the Mainland and at the specified distance
     * away from the Mainland.
     *
     * @param angle
     * @param distance
     */
    void plantSeed(float angle, float distance) {

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

        gameStage.addActor(new Seeder(ml.oX + newX, ml.oY + newY, ml.oX, ml.oY, ml));


    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        gameStage.act();
        gameStage.draw();

        /**
         * Scans through all of the Actors to determine if there's is still one play.
         */
        boolean playerActive = false;
        for (Actor c : gameStage.getActors()) {
            if (c instanceof Player)
                playerActive = true;
        }

        if (!playerActive) {
            game.setScreen(game.loseScreen);
        }


    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void show() {
        //music.play();

    }

    @Override
    public void hide() {
        //music.stop();

    }

    @Override
    public void pause() {
        //music.pause();
    }

    @Override
    public void resume() {
        //music.play();

    }

    @Override
    public void dispose() {

    }
}

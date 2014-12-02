package com.solanum.hybridity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;

import java.util.ArrayList;

/**
 * @author Aldous
 *         Machinedrum - Vapor City Archives
 *         <p/>
 *         The Player class is an Actor representating the player inside of the GameBoard that listens for input from the keyboard
 *         and controller.
 *         <p/>
 *         It updates and translates the camera according to payer movement
 */
public class Player extends Actor {

    private float rotation;
    private float lastShot;
    private final float shotDelay = .1025f;

    private final float rotationSpeed = 6;
    private final Texture tex = new Texture("Player.png");
    private final Sprite sprite = new Sprite(tex);
    private final Sound laser = Gdx.audio.newSound(Gdx.files.internal("sounds/shoot-03.wav"));

    private final float deadZone = .05f;

    private float left_horz;
    private float left_vert;
    private float right_horz;
    private float right_vert;

    Controller controller;

    public Player() {

        lastShot = 0;

        sprite.setPosition(0, 0);
        setX(Gdx.graphics.getWidth() / 2);
        setY(Gdx.graphics.getHeight() / 2);

        setWidth(sprite.getWidth());
        setHeight(sprite.getHeight());


        /**
         * Controller API checks for the presence of controllers. If any are found, the first one is mapped to the
         * 'controller' variable. If not, nothing occurs.
         */
        if (Controllers.getControllers().size != 0) {
            controller = Controllers.getControllers().first();
        }

    }

    /**
     * Contains all of the logic that is to be fired each tick of the game. Is called by the Stage instance that this
     * Actor is a part of.
     *
     * @param delta The elapsed time since this function has last been called. Used for animating and speed calculating
     *              purposes.
     */
    public void act(float delta) {

        /**
         * Keeps track of the last time that a bullet was fired by this player.
         */
        lastShot += delta;


        /**
         * Saves the current position of the actor before any manipulations are made. This is done in case the Actor
         * is moved to an invalid position and needs to be reset mid-frame.
         */
        float lastX = getX();
        float lastY = getY();

        /**
         * Contains all of the logic related to playing the game using a controller. If the Controller API included
         * with LibGDX doesn't detect any active controllers, than non of these will fire.
         */
        float speed = 8;
        if (Controllers.getControllers().size != 0) {
            left_vert = controller.getAxis(2);
            left_horz = controller.getAxis(3);
            right_vert = controller.getAxis(0);
            right_horz = controller.getAxis(1);

            /** Directional movement based on Left Stick */
            if (left_horz < -0.2) {
                setPosition(getX() - speed, getY());
            }
            if (left_vert < -0.2) {
                setPosition(getX(), getY() + speed);

            }
            if (left_vert > 0.2) {
                setPosition(getX(), getY() - speed);

            }
            if (left_horz > 0.2) {
                setPosition(getX() + speed, getY());
            }

            /** Fire Buttons */
            if (controller.getButton(5) || controller.getButton(6) || controller.getButton(7) || controller.getButton(8)) {
                if (lastShot > shotDelay) {
                    this.getParent().addActor(new Bullet(sprite.getX() + sprite.getWidth() / 2, sprite.getY() + sprite.getHeight() / 2, sprite.getRotation() % 360));
                    laser.play(.1f);
                    lastShot = 0;
                }
            }

            /** Rotation based on Right Stick */
            if (right_horz > deadZone || right_horz < (-1 * deadZone) || right_vert > deadZone || right_vert < (-1 * deadZone)) {
                rotation = getStickDegree(right_horz, right_vert);
                setRotation(rotation);
                sprite.setRotation(this.getRotation());
            }
        }

        /**
         * Input polling from keyboard
         */
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            setPosition(getX() - speed, getY());
        }
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            setPosition(getX(), getY() + speed);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            setPosition(getX(), getY() - speed);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            setPosition(getX() + speed, getY());
        }
        if (Gdx.input.isKeyPressed(Input.Keys.J)) {
            sprite.rotate(rotationSpeed);
        }

        /**Rotation*/
        if (Gdx.input.isKeyPressed(Input.Keys.L)) {
            sprite.rotate(-rotationSpeed);
        }
        /** Firing */
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            if (lastShot > shotDelay) {
                this.getParent().addActor(new Bullet(sprite.getX() + sprite.getWidth() / 2, sprite.getY() + sprite.getHeight() / 2, sprite.getRotation() % 360));
                laser.play(.1f);
                lastShot = 0;
            }
        }

        /** Normalizes sprite rotation in case it is sub-360 degrees */
        if (sprite.getRotation() < 0) {
            sprite.setRotation(360 + sprite.getRotation());
        }

        /** Translates the camera based on player movement */
        getStage().getCamera().translate(this.getX() - lastX, this.getY() - lastY, 0);


        /** Kills the player if it is outside the Mainland  during phase 1 of the game*/
        if (GameScreen.phase == 1) {

            if (((Mainland) getStage().getRoot().findActor("ml")).containsPoint((int) (this.getX() + sprite.getWidth() / 2), (int) (this.getY() + sprite.getHeight() / 2))) {
                System.out.println("In");
            }
        }

        /**
         * Updates the position of the Sprite to be in line with the updated position of the Player
         */
        sprite.setPosition(getX(), getY());



    }

    /**
     * Uses information about the current graphics state to draw the assets associated with this class
     *
     * @param batch       An instance of a sprite batch, which is an object containing information about the current
     *                    OpenGL graphics state and is queried by the entity being drawn.
     * @param parentAlpha The alpha value of the immediate ascendant of this actor.
     */
    public void draw(Batch batch, float parentAlpha) {
        sprite.draw(batch);
    }

    /**
     * Called to remove this entity from the stage
     */
    void destroy() {
        this.remove();
        //Gdx.app.exit();
    }

    /**
     * Takes current information about a Joystick's position and return a degree of it's position relative to it's center.
     *
     * @param h The raw float value for the stick's horizontal axis
     * @param v The raw float value for the stick's vertical acis
     * @return The integer representation of the degree position of the x axis
     */
    int getStickDegree(float h, float v) {
        float x = h;
        float y = -1 * v;

        return (int) (Math.toDegrees(Math.atan2(y, x)));
    }

}
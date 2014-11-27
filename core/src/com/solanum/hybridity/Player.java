package com.solanum.hybridity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;

import java.util.ArrayList;


/**
 * @author Aldous
   Machinedrum - Vapor City Archives
 */
class Player extends Actor {

    private float speed = 8;
    private final ArrayList bullets;
    private float rotation;
    private final float shotDelay;
    private float lastShot;
    private final float rotationSpeed;
    private OrthographicCamera cam;
    private final Rectangle boundBox = null;
    private final Texture tex;
    private final Sprite sprite;
    private final Sound laser = Gdx.audio.newSound(Gdx.files.internal("sounds/shoot-03.wav"));

    private float deadZone = .05f;

    private float left_horz;
    private float left_vert;
    private float right_horz;
    private float right_vert;

    Controller controller;

    public Player() {
        bullets = new ArrayList();
        shotDelay = .1025f;
        lastShot = 0;
        rotationSpeed = 6;

        tex = new Texture("Player.png");
        sprite = new Sprite(tex);
        sprite.setPosition(0, 0);
        setX(Gdx.graphics.getWidth() / 2);
        setY(Gdx.graphics.getHeight() / 2);

        setWidth(sprite.getWidth());
        setHeight(sprite.getHeight());

        controller = Controllers.getControllers().first();

    }

    public void act(float delta) {

        getStage().getActors();
        //LAST SHOT FIRED
        lastShot += delta;

        //KEEPS TRACK OF LAST COORDINATES FOR OUT OF BOUNDS RESETTING AND CAMERA UPDATES
        float lastX = getX();
        float lastY = getY();

        /**
         * CONTROLLER MAPPINGS
         */
        left_vert = controller.getAxis(2);
        left_horz = controller.getAxis(3);
        right_vert = controller.getAxis(0);
        right_horz = controller.getAxis(1);



        //DIRECTIONAL INPUT
        if (Gdx.input.isKeyPressed(Input.Keys.A) || left_horz < -0.2) {
            setPosition(getX() - speed, getY());
        }
        if (Gdx.input.isKeyPressed(Input.Keys.W) || left_vert < -0.2) {
            setPosition(getX(), getY() + speed);

        }
        if (Gdx.input.isKeyPressed(Input.Keys.S) || left_vert > 0.2) {
            setPosition(getX(), getY() - speed);

        }
        if (Gdx.input.isKeyPressed(Input.Keys.D) || left_horz > 0.2) {
            setPosition(getX() + speed, getY());
        }

        if (Gdx.input.isKeyPressed(Input.Keys.J)) {
            sprite.rotate(rotationSpeed);

        }

        if (Gdx.input.isKeyPressed(Input.Keys.L)) {
            sprite.rotate(-rotationSpeed);
        }


        if (Gdx.input.isKeyPressed(Input.Keys.SPACE) || controller.getButton(5) || controller.getButton(6) || controller.getButton(7) || controller.getButton(8)) {
            if (lastShot > shotDelay) {
                this.getParent().addActor(new Bullet(sprite.getX() + sprite.getWidth() / 2, sprite.getY() + sprite.getHeight() / 2, sprite.getRotation() % 360));
                laser.play(.1f);
                lastShot = 0;
            }
        }




        /**
         * RIGHT STICK ROTATIONAL MATH
         */
        if(right_horz>deadZone || right_horz<(-1*deadZone) || right_vert > deadZone || right_vert <( -1*deadZone)){
            rotation = getStickDegree(right_horz, right_vert);
        }




        setRotation(rotation);
        sprite.setRotation(this.getRotation());

        if (sprite.getRotation() < 0) {
            sprite.setRotation(360 + sprite.getRotation());
        }

        getStage().getCamera().translate(this.getX() - lastX, this.getY() - lastY, 0);

        int centerX = (int) (getX() + (getWidth() / 2));
        int centerY = (int) (getY() + (getHeight() / 2));

        if (GameScreen.phase == 1) {
            if (!((Mainland) getStage().getRoot().findActor("ml")).containsPoint(centerX, centerY)) {
                this.destroy();
            }
        }





        sprite.setPosition((float) getX(), (float) getY());

    }

    public void draw(Batch batch, float parentAlpha) {
        sprite.draw(batch);
    }

    void destroy() {
        this.remove();
        //Gdx.app.exit();
    }

    int getStickDegree(float h, float v){
        float x = h;
        float y = -1*v;

        //System.out.println(Math.toDegrees(Math.atan2(y, x)));
        return (int)(Math.toDegrees(Math.atan2(y, x)));
    }

    Rectangle getBoundBox() {

        return boundBox;
    }
}
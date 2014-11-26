package com.solanum.hybridity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;

import java.awt.*;
import java.awt.geom.Point2D;


/**
 * @author Aldous
 * "You Know I Have To Go" - Royksopp
 */
class Seeder extends Actor {
    private ShapeRenderer render = new ShapeRenderer();
    private boolean following = true;
    private Texture tex = new Texture("Wanderer.png");
    private Sprite sprite = new Sprite(tex);
    private float timeSinceHit;
    private Sound hit = Gdx.audio.newSound(Gdx.files.internal("sounds/explosion-01.wav"));
    private Sound death = Gdx.audio.newSound(Gdx.files.internal("sounds/explosion-03.wav"));

    Polygon octagon;
    float radius;
    float startAngle;

    private int gX;
    private int gY;

    private int HP = 15;

    int speed = 3;

    Rectangle collision = new Rectangle();

    float[] v = new float[16];


    Seeder(float x, float y, int goalX, int goalY) {
        sprite.setOrigin(x, y);

        gX = goalX;
        gY = goalY;

        setX(x);
        setY(y);

        setOrigin(getX() + getHeight() / 2, getY() + getWidth() / 2);

        collision.set(sprite.getBoundingRectangle());


    }


    public void act(float delta) {
        /**
         * COLLISION CHECK
         */

        if (following) {
            setPosition(getX() - ((getX() - gX) * delta), getY() - ((getY() - gY) * delta));
            if (((Mainland) getStage().getRoot().findActor("ml")).containsPoint((int) (this.getX() + sprite.getWidth() / 2), (int) (this.getY() + sprite.getHeight() / 2))) {
                following = false;
            }
        }


        timeSinceHit += delta;

        sprite.setPosition(getX(), getY());
        collision.set(sprite.getBoundingRectangle());

        if (Gdx.input.isKeyPressed(Input.Keys.O)) {
            growOctagon();
        }

    }


    public void draw(Batch batch, float parentAlpha) {

        if (timeSinceHit < .1) {
            sprite.setColor(Color.CYAN);
        }
        sprite.draw(batch);
        sprite.setColor(Color.WHITE);

        batch.end();
        render.setProjectionMatrix(getStage().getCamera().combined);
        render.setAutoShapeType(true);
        render.begin();
        render.setColor(Color.CYAN);


        if(!following){

            int[] x = octagon.xpoints;
            int[] y= octagon.ypoints;

            for( int i = 0; i < 16; i++){
                if(i % 2 == 0 ){
                    v[i] = x[];
                } else {
                    v[i] =
                }
            }

        }


        render.polygon(v);
        render.end();


        batch.begin();

    }


    /*angle = start_angle
            angle_increment = 360 / n_sides
    for n_sides:
    x = x_centre + radius * cos(angle)
    y = y_centre + radius * sin(angle)
    angle += angle_increment*/

    public void growOctagon() {
        if (octagon == null) {
            octagon = new Polygon();
            radius = 0;
        }
        radius++;

        startAngle = 0;

        float x;
        float y;

        for (int i = 0; i < 8; i++) {
            x = (float) ((getX() + sprite.getWidth() / 2) + radius * Math.cos(Math.toRadians(startAngle)));
            y = (float) ((getY() + sprite.getHeight() / 2) + radius * Math.sin(Math.toRadians(startAngle)));

            octagon.addPoint((int) x, (int) y);

            startAngle += 360 / 8;
        }


    }

    public void hit() {
        HP--;
        hit.play(0.3f);
        timeSinceHit = 0;
        if (HP <= 0) {
            death.play(0.3f);
            this.remove();
        }
    }

}

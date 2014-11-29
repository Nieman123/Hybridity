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
import math.geom2d.Point2D;
import math.geom2d.polygon.Polygon2D;
import math.geom2d.polygon.Polygons2D;
import math.geom2d.polygon.SimplePolygon2D;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;


/**
 * @author Aldous
 *         "You Know I Have To Go" - Royksopp
 *         <p/>
 *         The Seeder class represents an enemy that is spawned outside the perimeter of the Mainland, move towards it's
 *         origin and then latches on to the outside of the perimeter. Once it has latched onto the outside of the Mainland,
 *         it will begin expanding in an octoganal shape and claiming territory inside of the Mainland.
 */
public class Seeder extends Actor {
    public final Rectangle collision = new Rectangle();
    private float[] v;
    private final ShapeRenderer render = new ShapeRenderer();
    private final Texture tex = new Texture("Wanderer.png");
    private final Sprite sprite = new Sprite(tex);
    private final Sound hit = Gdx.audio.newSound(Gdx.files.internal("sounds/explosion-01.wav"));
    private final Sound death = Gdx.audio.newSound(Gdx.files.internal("sounds/explosion-03.wav"));
    private final int gX;
    private final int gY;
    int speed = 3;
    private SimplePolygon2D octagon;
    private SimplePolygon2D territory;
    private SimplePolygon2D originalMainland;
    private float radius;
    private float startAngle;
    private boolean following = true;
    private float timeSinceHit;
    private float getTimeSinceUpdate = 0;
    private int HP = 15;
    private Mainland ml;


    /**
     * Creates a basic Seeder enemy that is spawned at a specified x and y and given an origin x and y to follow.
     * @param x The x coordinate of the point that the seeder will be spawned at
     * @param y The y coordinate of the point that the seeder will be spawned at
     * @param goalX The x coordinate of the point that the seeder will follow
     * @param goalY The y coordinate of the point that the seeder will follow
     */
    Seeder(float x, float y, int goalX, int goalY, Mainland goal) {
        sprite.setOrigin(x, y);

        gX = goalX;
        gY = goalY;

        setX(x);
        setY(y);

        setOrigin(getX() + getHeight() / 2, getY() + getWidth() / 2);

        collision.set(sprite.getBoundingRectangle());

        originalMainland = goal.area;

        territory = new SimplePolygon2D();
    }


    public void act(float delta) {
        /**
         * Checks to see if the seeder is colliding with the Mainland.
         */
        if (following) {
            setPosition(getX() - ((getX() - gX) * delta), getY() - ((getY() - gY) * delta));
            if (((Mainland) getStage().getRoot().findActor("ml")).containsPoint((int) (this.getX() + sprite.getWidth() / 2), (int) (this.getY() + sprite.getHeight() / 2))) {
                following = false;
            }
        }

        /**
         * Keeps track of the last time this seeder was hit by a bullet
         */
        timeSinceHit += delta;

        sprite.setPosition(getX(), getY());
        collision.set(sprite.getBoundingRectangle());

        getTimeSinceUpdate += delta;

        if (getTimeSinceUpdate > .1 && !following) {
            getTimeSinceUpdate = 0;
            growOctagon();
        }

        if(Gdx.input.isButtonPressed(Input.Keys.E)) {
            updateMainland();
        }




    }


    public void draw(Batch batch, float parentAlpha) {

        /**
         * Flashes the seeder whenever it is hit by an enemy laser
         */
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


        /**
         * Converts the octagon into a simplistic, drawable array and passes it to the shape renderer
         */

        if (!following && octagon != null) {

            v = new float[territory.vertexNumber()*2];
            for (int i = 0; i < territory.vertexNumber(); i++) {
                v[i * 2] = (float) territory.vertex(i).x();
                v[(i * 2) + 1] = (float) territory.vertex(i).y();
            }

           render.polygon(findIntersect());
        }

        render.end();
        batch.begin();

    }

    /**
     * Increases the radius of the territorial shape by one and updates the shape. This shape is to be considered pure.
     * Meaning that it is, in essence, an Octagon. Calculations related to overlap and intersections are perfomed
     * elsewhere.
     */
    void growOctagon() {
        if (octagon == null) {
            octagon = new SimplePolygon2D();
            radius = 0;
        }
        radius++;

        octagon.clearVertices();

        startAngle = 0;

        float x;
        float y;

        for (int i = 0; i < 8; i++) {
            x = (float) ((getX() + sprite.getWidth() / 2) + radius * Math.cos(Math.toRadians(startAngle)));
            y = (float) ((getY() + sprite.getHeight() / 2) + radius * Math.sin(Math.toRadians(startAngle)));

            octagon.addVertex(new Point2D( x, y));

            startAngle += 360 / 8;
        }


    }

    /**
     * Identifies the intersect of the Mainland polygon and this seeder's territorial polygon and returns the result
     *
     * @return A linear array of floats representing (x, y ,x1 , y1 . . .] in the polygon.
     */
    public float[] findIntersect() {

        ml = getStage().getRoot().findActor("ml");

        Polygon2D overlap = Polygons2D.intersection(originalMainland, octagon);

        territory.clearVertices();
        for(int i = 0; i < overlap.vertexNumber(); i++) {

            territory.addVertex(new Point2D(overlap.vertex(i).x() , overlap.vertex(i).y()));
        }

        float[] intersect = new float[overlap.vertexNumber()*2];

        for( int i = 0; i < overlap.vertexNumber(); i++ ) {

            intersect[i * 2 ] = (float )overlap.vertex(i).x();
            intersect[(i * 2 ) + 1] = (float )overlap.vertex(i).y();

        }


        return intersect;
    }

    /**
     * Find Difference will remove the overlap of this seeder's territorial polygon and the Mainland polygon
     */
    public void updateMainland() {

        ml = getStage().getRoot().findActor("ml");

        SimplePolygon2D newArea = new SimplePolygon2D();

        Polygon2D eOr  =  Polygons2D.exclusiveOr(territory, ml.area);

        for(int i = 0; i < eOr.vertexNumber(); i++) {

            newArea.addVertex(new Point2D(eOr.vertex(i).x() , eOr.vertex(i).y()));
        }

        ml.area = newArea;

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

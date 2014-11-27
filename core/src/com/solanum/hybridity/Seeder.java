package com.solanum.hybridity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import math.geom2d.polygon.Polygon2D;
import math.geom2d.polygon.Polygons2D;
import math.geom2d.polygon.SimplePolygon2D;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;


/**
 * @author Aldous
 *         "You Know I Have To Go" - Royksopp
 */
class Seeder extends Actor {
    public final Rectangle collision = new Rectangle();
    int speed = 3;
    private final float[] v = new float[16];
    private Polygon octagon;
    private float radius;
    private float startAngle;
    private final ShapeRenderer render = new ShapeRenderer();
    private boolean following = true;
    private final Texture tex = new Texture("Wanderer.png");
    private final Sprite sprite = new Sprite(tex);
    private float timeSinceHit;
    private float getTimeSinceUpdate = 0;
    private final Sound hit = Gdx.audio.newSound(Gdx.files.internal("sounds/explosion-01.wav"));
    private final Sound death = Gdx.audio.newSound(Gdx.files.internal("sounds/explosion-03.wav"));
    private final int gX;
    private final int gY;
    private int HP = 15;
    private Mainland ml;




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
         * Converts the octagon into a drawable array and passes it to the shape renderer
         */
        if (!following && octagon != null) {

            int[] x = octagon.xpoints;
            int[] y = octagon.ypoints;

            for (int i = 0; i < 8; i++) {
                v[i * 2] = x[i];
                v[(i * 2) + 1] = y[i];
            }
        }

        render.polygon(v);
        render.end();


        batch.begin();

    }

    /**
     * Increases the radius of the territorial shape by one and updates the shape
     */
    void growOctagon() {
        if (octagon == null) {
            octagon = new Polygon();
            radius = 0;
        }
        radius++;

        octagon.reset();

        startAngle = 0;

        float x;
        float y;

        for (int i = 0; i < 8; i++) {
            x = (float) ((getX() + sprite.getWidth() / 2) + radius * Math.cos(Math.toRadians(startAngle)));
            y = (float) ((getY() + sprite.getHeight() / 2) + radius * Math.sin(Math.toRadians(startAngle)));

            octagon.addPoint((int) x, (int) y);

            startAngle += 360 / 8;
        }

        findDifference();

    }

    /**
     * Identifies the intersect of the Mainland polygon and this seeder's territorial polygon and returns the result
     *
     * @return A linear array of floats representing (x, y ,x1 , y1 . . .] in the polygon.
     */
    public float[] findIntersect() {

        ml = getStage().getRoot().findActor("ml");

        SimplePolygon2D attackArea = new SimplePolygon2D();

        int[] x = octagon.xpoints;
        int[] y = octagon.ypoints;

        for (int i = 0; i < 8; i++) {
            attackArea.addVertex(new math.geom2d.Point2D(x[i], y[i]));
        }


        SimplePolygon2D mainlandArea = new SimplePolygon2D();

        x = ml.shape.xpoints;
        y = ml.shape.ypoints;

        for (int i = 0; i < ml.shape.npoints; i++) {
            mainlandArea.addVertex(new math.geom2d.Point2D(x[i], y[i]));
        }


        Polygon2D overlap = Polygons2D.intersection(mainlandArea, attackArea);

        ArrayList overlapArea = new ArrayList();

        Iterator i = overlap.vertices().iterator();

        while (i.hasNext()) {
            math.geom2d.Point2D c = (math.geom2d.Point2D) i.next();

            overlapArea.add(c.getX());
            overlapArea.add(c.getY());
        }

        float[] returnVerts = new float[overlapArea.size()];

        for (int j = 0; j < returnVerts.length; j++) {
            returnVerts[j] = ((Double) overlapArea.get(j)).intValue();
        }

        return returnVerts;
    }

    /**
     * Find Difference will remove the overlap of this seeder's territorial polygon and the Mainland polygon
     */
    void findDifference() {


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

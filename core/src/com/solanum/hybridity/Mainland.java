package com.solanum.hybridity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.seisw.util.geom.Poly;
import math.geom2d.Point2D;
import math.geom2d.polygon.SimplePolygon2D;

import java.awt.geom.Area;
import java.awt.geom.PathIterator;


/**
 * @author Aldous
 *         ITS Room - "Silence"
 *         <p>
 *         The Mainland is the central focus of the game. It is a polygonal shape that must be defended during the start
 *         of the game. Seeder will attatch to the outside of it's perimeter and attempt to claim territory inside of it.
 *         <p/>
 *         Leaving the Mainland during phase one will result in an instant death on behalf of the Player.
 */
public class Mainland extends Actor {
    private ShapeRenderer render = new ShapeRenderer();
    private float[] v;
    public int oX;
    public int oY;
    public java.awt.Polygon area;


    /**
     * Creates a new Mainland with a square shape
     *
     * @param x The x origin of the Mainland to be created
     * @param y The y origin of the Mainland to be created
     */
    public Mainland(int x, int y) {

        oX = x;
        oY = y;

        /**
         * Initializes the representation of this objects vertices
         */
        area = new java.awt.Polygon();

        /**
         * Initializes the four vertices of the Mainland
         */
        x=x-200;
        y=y-200;
        area.addPoint(x, y + 400);
        area.addPoint(x + 400, y + 400);
        area.addPoint(x + 400, y);
        area.addPoint(x, y);

        /**
         * Sets the name property of this Actor. This can later be used from within other Actors to find this specific
         * object with relative ease. Generally used when using a singular instance of this class or when implementing
         * serial numbers to keep track of objects. In this instance, there is only ever one Mainland.
         */
        this.setName("ml");

    }

    /**
     * Updates all of the internal logic of the stage
     *
     * @param delta The time, in milliseconds, since the last update
     */
    public void act(float delta) {

    }

    /**
     * Getter method for the contains function supplied by the SimplePolygon object. Returns true if the supplied
     * coordinates are within the area of the mainland
     * @param x The x value of the point that is to be checked.
     * @param y The y value of the point that is to be checked.
     * @return
     */
    public boolean containsPoint(int x, int y) {

        return area.contains(x , y);
    }

    public void draw(Batch batch, float parentAlpha) {
        /**
         * Beings the drawing sequence of the shape renderer and ends the Spritebatch's drawing process.
         */
        batch.end();
        render.setAutoShapeType(true);
        render.begin();

        /**
         * Fetches the projection matrix of the stage that this Actor is contained within. The Stage's camera
         * contains positional and scaling information about the perspective and will apply these characteristics
         * to the shape renderer. If the Camera were not applied to the Shape menu, than all of the output
         * of the shape renderer would appear in a static position over-top all other elements on the screen.
         */
        render.setProjectionMatrix(getStage().getCamera().combined);

        render.setColor(Color.CYAN);

        /**
         * Builds a simplistic representation of the current vertices of the Mainland so that they can be
         * drawn by the shape renderer. Converts the Polygon object to a linear array in the form of
         * [x , y . . .etc]
         */
        v = new float[area.npoints * 2];

        for (int i = 0; i < area.npoints; i++) {
            v[i * 2] = area.xpoints[i];
            v[(i * 2) + 1] = area.ypoints[i];
        }

        /**
         * Ends the drawing sequence of the shape renderer and begins the Spritebatch's drawing process again.
         */
        try{

            render.polygon(v);

        } catch (Exception e) {



        }
        render.end();

        batch.begin();

    }
}

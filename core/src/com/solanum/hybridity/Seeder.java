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
import com.seisw.util.geom.Poly;
import javafx.scene.shape.*;
import javafx.scene.shape.Polygon;
import math.geom2d.AffineTransform2D;
import math.geom2d.Point2D;
import math.geom2d.polygon.Polygon2D;
import math.geom2d.polygon.Polygons2D;
import math.geom2d.polygon.SimplePolygon2D;
import sun.applet.Main;

import java.awt.*;
import java.awt.Shape;
import java.awt.geom.*;
import java.util.ArrayList;
import java.util.Iterator;


/**
 * @author Aldous
 *         "You Know I Have To Go" - Royksopp
 *         <p/>
 *         The Seeder class represents an enemy that is spawned outside the perimeter of the Mainland, moves towards it's
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
    private java.awt.Polygon octagon;
    private Area territory = new Area();
    private float radius;
    private float startAngle;
    boolean following = true;
    private float timeSinceHit;
    private float getTimeSinceUpdate = 0;
    private int HP = 6;


    /**
     * Creates a basic Seeder enemy that is spawned at a specified x and y and given an origin x and y to follow.
     * @param x The x coordinate of the point that the seeder will be spawned at
     * @param y The y coordinate of the point that the seeder will be spawned at
     * @param goalX The x coordinate of the point that the seeder will follow
     * @param goalY The y coordinate of the point that the seeder will follow
     */
    Seeder(int x, int y, int goalX, int goalY, Mainland goal) {
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
        render.setColor(Color.RED);


        /**
         * Draws the current territory of the Seeder.
         */
        if (!following && octagon != null) {

            try{

                render.polygon(getVertices(octagon));

            } catch (Exception e ){


            }
        }
        render.end();
        batch.begin();

    }

    /**
     * Increases the radius of the territorial shape and updates the shape. This shape is to be considered pure.
     * Meaning that it is, in essence, an Octagon. Calculations related to overlap and intersections are performed
     * elsewhere.
     */
    void growOctagon() {
        if (octagon == null) {
            octagon = new java.awt.Polygon();
            radius = 5;
        }
        radius+=2;

        octagon.reset();

        startAngle = 0;

        int x;
        int y;

        for (int i = 0; i < 8; i++) {
            x = (int) ((getX() + sprite.getWidth() / 2) + radius * Math.cos(Math.toRadians(startAngle)));
            y = (int) ((getY() + sprite.getHeight() / 2) + radius * Math.sin(Math.toRadians(startAngle)));

            octagon.addPoint(x, y);

            startAngle += 360 / 8;
        }


    }

    /**
     * Returns a simplistic representation of the vertices that make up a shape. The returned array can be passed
     * directly into a ShapeRenderer for ease of use when depicting Polygons.
     * @param shape A shape from the math.2d.geom library that is to be inspected
     * @return An array of integers in the format [x, y, x, y,  . . . ], the array is technically a float however,
     * fulfill the requirements of the ShapeRenderer.polygon() method.
     */
    public float[] getVertices(java.awt.Polygon shape) {
        float[] verts = new float[shape.npoints*2];
        for (int i = 0; i < shape.npoints; i++) {
            verts[i * 2] = shape.xpoints[i];
            verts[(i * 2) + 1] = shape.ypoints[i];
        }

        return verts;
    }


    /**
     * Fired by a Bullet whenever it detects that it is hitting a Seeder. Decrements the health of the Seeder and handles
     * removing it from play if the current HP is 0.
     */
    public void hit() {
        HP--;
        hit.play(0.3f);
        timeSinceHit = 0;
        if (HP <= 0) {
            death.play(0.3f);
            Area ml = new Area(((Mainland)getStage().getRoot().findActor("ml")).area);

            Area intersect = new Area((octagon));

            intersect.intersect(ml);

            ml.subtract(intersect);


            ((Mainland)getStage().getRoot().findActor("ml")).asteroids.add(new Asteroid(areaToShape(intersect)));

            ((Mainland)getStage().getRoot().findActor("ml")).area = areaToShape(ml);
            this.remove();
        }
    }


    /**
     * Accepts a Java.Awt.Area object and iterates over it, building a more specific polygon that might be better
     * suited for interpreting or finding information about the path. This is done because the Polygon class provided
     * by the AWT package has more convenience methods for accessing data fields about the Polygon. The Area class
     * relies on a ShapeIterator in order to retrieve it's vertices, whereas the Polygon maintains its X and Y
     * points in the fields 'xPoints[]' and 'yPoints[]'
     *
     * @param area the Area object that is to be converted into a Polygon
     * @return The Polygon comprised of vertices from the Area
     */
    public java.awt.Polygon areaToShape(Area area){

        java.awt.Polygon shape = new java.awt.Polygon();
        /**
         * The Affine Transform property is used to preemptively apply a Matrix transformation to every path inside
         * of the Area. The constructor below creates a neutral AffineTransform object that returns the 'pure' points
         * without any transformations.
         */
        PathIterator path = area.getPathIterator(new AffineTransform());
        while(!path.isDone()) {

            double[] coord = new double[2];

            int type = path.currentSegment(coord);
            if (!(coord[0] == 0 && coord[1] == 0)) {

                shape.addPoint((int) coord[0], (int) coord[1]);

            }

            path.next();



        }
        return shape;

    }


    /**
     * Experimental new iterator to accurately read the line segments of a shape
     */
    public java.awt.Polygon testIterator() {

        Area area = new Area(((Mainland) getStage().getRoot().findActor("ml")).area);

        ArrayList<double[]> areaPoints = new ArrayList<double[]>();
        ArrayList<Line2D.Double> areaSegments = new ArrayList<Line2D.Double>();
        double[] coords = new double[6];

        for (PathIterator pi = area.getPathIterator(null); !pi.isDone(); pi.next()) {
            // The type will be SEG_LINETO, SEG_MOVETO, or SEG_CLOSE
            // Because the Area is composed of straight lines
            int type = pi.currentSegment(coords);
            // We record a double array of {segment type, x coord, y coord}
            double[] pathIteratorCoords = {type, coords[0], coords[1]};
            areaPoints.add(pathIteratorCoords);
        }

        double[] start = new double[3]; // To record where each polygon starts

        for (int i = 0; i < areaPoints.size(); i++) {
            // If we're not on the last point, return a line from this point to the next
            double[] currentElement = areaPoints.get(i);

            // We need a default value in case we've reached the end of the ArrayList
            double[] nextElement = {-1, -1, -1};
            if (i < areaPoints.size() - 1) {
                nextElement = areaPoints.get(i + 1);
            }

            // Make the lines
            if (currentElement[0] == PathIterator.SEG_MOVETO) {
                start = currentElement; // Record where the polygon started to close it later
            }

            if (nextElement[0] == PathIterator.SEG_LINETO) {
                areaSegments.add(
                        new Line2D.Double(
                                currentElement[1], currentElement[2],
                                nextElement[1], nextElement[2]
                        )
                );
            } else if (nextElement[0] == PathIterator.SEG_CLOSE) {
                areaSegments.add(
                        new Line2D.Double(
                                currentElement[1], currentElement[2],
                                start[1], start[2]
                        )
                );
            }
        }

        //todo: write a culling function that eliminates redundant points
        java.awt.Polygon please = new java.awt.Polygon();


        ArrayList<java.awt.geom.Point2D> points = new ArrayList<java.awt.geom.Point2D>();

        for(Line2D.Double c : areaSegments) {


            java.awt.geom.Point2D point = new java.awt.geom.Point2D.Float((float)c.x2, (float) c.y2);

            if(!points.contains(point)) {
                System.out.println("REPEAT ELIMINATED");
                points.add(point);
            }
        }


        for(java.awt.geom.Point2D pt : points) {

            please.addPoint((int) pt.getX(), (int) pt.getY());
        }

        return please;

    }
}

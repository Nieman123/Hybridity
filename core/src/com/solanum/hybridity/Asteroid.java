package com.solanum.hybridity;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.PathIterator;

/**
 * @author Aldous
 *
 * "Vanished" - Crystal Castles
 *
 * The Asteroid class models the fragment of Mainland that is created whenever a seeder is destroyed. It will float
 * away from the Mainland and contains an ammo count.
 *
 * It is not an extension of the Actor class and is not inside of the GameScreen's stage. Instead, the Mainland
 * handles all of the render and updating manually through a For- Loop within it's updating.
 */
public class Asteroid {

    Polygon body;
    int oX;
    int oY;

    int mlX;
    int mlY;

    boolean moving = false;

    float elapsedTime = 0;

    int speed = 15;

    public int ammo;

    int transX;
    int transY;

    float timeSinceRotate;

    /**
     * These values are calculated when the Asteroid finishes it's translation process. By setting a static pivot point,
     * the shape avoids a sort of recursive decay that arises from continually rotating around an updating origin.
     */
    int staticRotationOriginX;
    int staticRotationOriginY;

    Asteroid(Polygon shape){

        body = shape;
        oX = (int) body.getBounds().getCenterX();
        oY = (int) body.getBounds().getCenterY();

        /**
         * Upon instantiation, sets the ammo value for the Asteroid relative to the width of the shape.
         */
        ammo = body.getBounds().width / 8;

    }


    /** Initializes variables that assist in translating this object away from the origin of the Mainland.
     *  Is only called once and from that point forward, the Mainland calls the 'update' method of this Asteroid
     *  in order to continue moving it outward.
     *
     * @param repelX The x coordinate of the point that this Asteroid is being repelled from
     * @param repelY The y coordinate of the point that this ASteroid is being repelled from
     */
    public void disperse(int repelX, int repelY) {

        mlX = repelX;
        mlY = repelY;

        int deltaX = oX - mlX;
        int deltaY = oY - mlY;

        int angle = (int) (Math.atan2(deltaY, deltaX) * 180 / Math.PI);

        /**
         * Update the position of the Asteroid in the field
         */
        double sin = Math.sin(Math.toRadians(angle));
        double height = sin * speed;
        double width = Math.sqrt((speed * speed) - (height * height));


        /**
         * Correct the angle if it is out of an applicable range.
         */
        if (angle > 90 && angle < 270) {
            width = -width;

        }

        /**
         * Sets the global values that will be used to update the position of this object each tick
         */
        transX = (int) width ;
        transY = (int) height ;

        moving = true;
    }


    /**
     * Called externally by the Mainland each tick. Updates the positions of the Asteroid and applies speed decay over time.
     */
    public void update(float delta){

        timeSinceRotate +=delta;

        elapsedTime +=delta;

        if(moving){

            body.translate((int)(transX  + (speed*delta)),(int)(transY + (speed*delta)));

        }

        if(speed > 0) {
            if( elapsedTime>.05 ) {

                speed --;
                elapsedTime = 0;
            }

        } else {

            staticRotationOriginX = (int) body.getBounds().getCenterX();
            staticRotationOriginY = (int) body.getBounds().getCenterY();

            moving = false;
        }


        if(timeSinceRotate>.1 && !moving) {
            timeSinceRotate = 0;
            //rotate(10);
        }

    }


    /**
     * Rotates the body of this Asteroid around a pre-set origin of rotation
     * @param degrees The amount, in Degrees, that the Asteroid is to be rotated.
     */
    public void rotate(int degrees) {

        AffineTransform at = new AffineTransform();

        at.rotate(Math.toRadians(1),staticRotationOriginX, staticRotationOriginY);


        Area test = new Area(body);

        body = areaToShape(test.createTransformedArea(at));

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

}

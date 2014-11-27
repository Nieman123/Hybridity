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
import math.geom2d.polygon.Polygon2D;
import math.geom2d.polygon.Polygons2D;
import math.geom2d.polygon.SimplePolygon2D;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Iterator;


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
    private float getTimeSinceUpdate = 0;
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

    Mainland ml ;



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

        getTimeSinceUpdate+=delta;

        if (getTimeSinceUpdate>.1 && !following){
            getTimeSinceUpdate = 0;
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


        if(!following && octagon != null){

            int[] x = octagon.xpoints;
            int[] y = octagon.ypoints;

            /**
             * Fills the verts array with the information from the AWT Polygon object.
             */

            for( int i = 0; i < 8; i++){
                v[i*2] = x[i];
                v[(i*2)+1] = y[i];
            }
        }


        //render.polygon(v);

        if( !following ) {
            render.setColor(Color.RED);
            //render.polygon(findIntersect());

        }

        render.end();


        batch.begin();

    }

    public void growOctagon() {
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

    public float[] findIntersect(){

         ml = (Mainland)getStage().getRoot().findActor("ml");

        SimplePolygon2D attackArea = new SimplePolygon2D();

        int[] x = octagon.xpoints;
        int[] y = octagon.ypoints;

        for(int i = 0; i < 8; i++){
            attackArea.addVertex(new math.geom2d.Point2D(x[i], y[i]));
        }


        SimplePolygon2D mainlandArea = new SimplePolygon2D();

        x = ml.shape.xpoints;
        y = ml.shape.ypoints;

        for(int i = 0; i < ml.shape.npoints; i++){
            mainlandArea.addVertex(new math.geom2d.Point2D(x[i], y[i]));
        }


        Polygon2D overlap = Polygons2D.intersection(mainlandArea, attackArea);

        ArrayList overlapArea = new ArrayList();

        Iterator i = overlap.vertices().iterator();

        while(i.hasNext()){
            math.geom2d.Point2D c = (math.geom2d.Point2D) i.next();

            overlapArea.add(c.getX());
            overlapArea.add(c.getY());
        }

        float[] returnVerts = new float[overlapArea.size()];

        for(int j = 0; j < returnVerts.length; j++){
            returnVerts[j] = ((Double)overlapArea.get(j)).intValue();
        }

        return returnVerts;
    }

    /**
     * Used to set motherland equal to the difference between this and the motherland overlap area.
     */
    public void findDifference(){
        ml = (Mainland)getStage().getRoot().findActor("ml");

        SimplePolygon2D attackArea = new SimplePolygon2D();

        int[] x = octagon.xpoints;
        int[] y = octagon.ypoints;

        for(int i = 0; i < 8; i++){
            attackArea.addVertex(new math.geom2d.Point2D(x[i], y[i]));
        }


        SimplePolygon2D mainlandArea = new SimplePolygon2D();

        x = ml.shape.xpoints;
        y = ml.shape.ypoints;

        for(int i = 0; i < ml.shape.npoints; i++){
            mainlandArea.addVertex(new math.geom2d.Point2D(x[i], y[i]));
        }


        Polygon2D overlap = Polygons2D.intersection(mainlandArea, attackArea);

        Polygon2D diff = Polygons2D.difference(overlap, mainlandArea);

        Polygon difference = new Polygon();

        for( int i = 0; i< diff.vertexNumber(); i++) {

           difference.addPoint((int)diff.vertex(i).getX(), (int)diff.vertex(i).getY());
        }

        ml.shape= difference;



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

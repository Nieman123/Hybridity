package com.solanum.hybridity;

import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import java.awt.geom.Point2D.Float;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by Aldous
 * ITS Room - "Silence"
 */
public class Mainland extends Actor {
    ShapeRenderer render = new ShapeRenderer();
    ArrayList<Float> verts;
    float[] v;
    public int oX;
    public int oY;
    Polygon shape;

    public Mainland(int x, int y){
        render = new ShapeRenderer();
        verts = new ArrayList();

        oX = x;
        oY = y;

        /**
         * THE FLOAT CLASS USED BELOW IS IN REFERENCE TO THE POINT 2D GEOMETRY SUBSET FOR POINTS
         * THAT ARE FLOATS. NOT THE WRAPPER CLASS FOR A FLOAT PRIMITIVE
         */
        verts.add(new Float(x, y + 400));
        verts.add(new Float(x + 400, y));
        verts.add(new Float(x, y - 400));
        verts.add(new Float(x - 400, y));

        this.setName("ml");

    }

    public void act(float delta){

    }

    public boolean containsPoint(int x, int y){
        Polygon shape = new Polygon();

        for(int i = 0; i < verts.size(); i++){
            shape.addPoint((int)verts.get(i).getX(),(int)verts.get(i).getY());
        }

       return shape.contains(x,y);
    }

    public void draw(Batch batch, float parentAlpha){
        batch.end();
        render.setProjectionMatrix(getStage().getCamera().combined);
        render.setAutoShapeType(true);
        render.begin();
        render.setColor(Color.CYAN);
        v = new float[verts.size()*2];
        int i =0;
        for(Float c : verts){
            v[i]=(float)c.getX();
            v[i+1]=(float)c.getY();
            i+=2;
        }

        render.polygon(v);
        render.end();


        batch.begin();

    }
}

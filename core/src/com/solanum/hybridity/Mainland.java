package com.solanum.hybridity;

import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by Aldous
 * ITS Room - "Silence"
 */
public class Mainland extends Actor {
    ShapeRenderer render = new ShapeRenderer();
    ArrayList<Point> verts;
    float[] v;


    public Mainland(){
        render = new ShapeRenderer();
        verts = new ArrayList();

        verts.add(new Point(200,200));
        verts.add(new Point(300,300));
        verts.add(new Point(200,400));
        verts.add(new Point(100,300));
    }

    public void act(float delta){

    }

    public void draw(Batch batch, float parentAlpha){
        batch.end();
        render.setAutoShapeType(true);
        render.begin();
        render.setColor(Color.CYAN);
        v = new float[verts.size()*2];
        int i =0;
        for(Point c : verts){
            v[i]=(float)c.getX();
            v[i+1]=(float)c.getY();
            i+=2;
        }

        render.polygon(v);
        render.end();

        batch.begin();

    }
}

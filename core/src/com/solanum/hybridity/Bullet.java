package com.solanum.hybridity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * @author Aldous
 *
 * Basic projectile that can be used by multiple classes and have it's allegience switched.
 */
public class Bullet extends Actor {

    private Pixmap bulletPixmap;
    private Texture bulletTexture;
    private Sprite bulletSprite;
    private double speed;
    private double height;
    private double width;
    private double angle;
    private Rectangle bounds;
    private float timeSinceSpawn;


    public Bullet(float x, float y, float angle) {

        bulletPixmap = new Pixmap(10, 10, Pixmap.Format.RGB888);
        bulletPixmap.setColor(Color.WHITE);
        bulletPixmap.fill();
        bulletTexture = new Texture("Bullet.png");
        bulletSprite = new Sprite(bulletTexture);

        bulletSprite.setRotation(angle);

        bulletSprite.setPosition(x-bulletSprite.getWidth()/2,y-bulletSprite.getHeight()/2);

        setPosition(bulletSprite.getX(), bulletSprite.getY());
        setWidth(bulletSprite.getWidth());
        setHeight(bulletSprite.getHeight());

        this.speed = (double) 20;
        this.angle = angle;

        bounds = new Rectangle();

        setOrigin(this.getX()+bulletSprite.getWidth()/2, this.getY()+bulletSprite.getHeight()/2);

        timeSinceSpawn = 0;

    }


    public void act(float delta) {
        //Update Actor position
        double sin = Math.sin(Math.toRadians(angle));
        height = sin * speed;
        width = Math.sqrt((speed * speed) - (height * height));


        if (angle > 90 && angle < 270) {
            width = -width;

        }


        if(!((Mainland)getStage().getRoot().findActor("ml")).containsPoint((int)(getX()+bulletSprite.getWidth()/2),(int)(getY()+bulletSprite.getHeight()/2))){
           this.destroy();
           System.out.println("MISSING");

        }

        Actor[] actors = getStage().getActors().toArray();


        for(Actor c : actors){
            if(c instanceof Seeder){
                if(Intersector.overlaps(getBounds(), ((Seeder)c).getBounds())){
                    this.destroy();
                }
            }
        }


        setPosition((float)(getX()+width),(float)(getY()+height));

        bulletSprite.setPosition(getX(),getY());

        timeSinceSpawn+=delta;

        if(timeSinceSpawn>10){

            this.destroy();
        }

        bounds.set(bulletSprite.getBoundingRectangle());

    }

    public Rectangle getBounds(){
        return bulletSprite.getBoundingRectangle();
    }

    public void draw(Batch batch, float parentAlpha) {

        bulletSprite.draw(batch);

    }

    void destroy(){
        remove();
    }

}

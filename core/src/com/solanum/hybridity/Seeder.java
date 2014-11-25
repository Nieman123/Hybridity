package com.solanum.hybridity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;

import java.util.Iterator;


/**
 * @Author Aldous
 * "You Know I Have To Go" - Royksopp
 */
public class Seeder extends Actor {


    boolean following = true;
    Texture tex = new Texture("Wanderer.png");
    Sprite sprite = new Sprite(tex);

    int gX;
    int gY;

    int HP = 20;

    int incX;
    int incY;

    int speed = 3;

    Rectangle collision = new Rectangle();


    Seeder(float x, float y, int goalX, int goalY){
        sprite.setOrigin(x,y);

        gX = goalX;
        gY = goalY;

        setX(x);
        setY(y);

        setOrigin(getX()+getHeight()/2,getY()+getWidth()/2);

        collision.set(x+sprite.getWidth()/2,y+sprite.getWidth()/2,sprite.getWidth()/4,sprite.getHeight()/4);



    }


    public void act(float delta){
        /**F
        * COLLISION CHECK
        */

        collision.set(sprite.getBoundingRectangle());
        sprite.setColor(1,1,1,1);
        Actor[] actors = getStage().getActors().toArray();

        for(Actor c : actors){
            if(c instanceof Bullet){
                if(Intersector.overlaps(collision, ((Bullet) c).getBounds())){
                    HP--;
                    sprite.setColor(Color.WHITE);
                }
            }
        }

        if(following){
            setPosition(getX()-((getX()-gX)*delta),getY()-((getY()-gY)*delta));
            if(((Mainland)getStage().getRoot().findActor("ml")).containsPoint((int)(this.getX()+sprite.getWidth()/2),(int)(this.getY()+sprite.getHeight()/2))){
                following=false;
            }
        }


        sprite.setPosition(getX(),getY());

    }

    public void draw(Batch batch, float parentAlpha){
        sprite.draw(batch);
    }

    public Rectangle getBounds(){
        return sprite.getBoundingRectangle();
    }


}

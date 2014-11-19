package com.solanum.hybridity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * @author Aldous
 * Machinedrum- Vapor City Archives
 */
public class Player extends Actor{

    private float speed = 8;
    private final ArrayList bullets;
    private float rotation;
    private final float shotDelay;
    private float lastShot;
    private final float rotationSpeed;
    private OrthographicCamera cam;
    private final Rectangle boundBox = null;
    private final Texture tex;
    private final Sprite sprite;
    private final Sound laser = Gdx.audio.newSound(Gdx.files.internal("sounds/shoot-03.wav"));

    public Player(){
        bullets = new ArrayList();
        shotDelay = .15f;
        lastShot=0;
        rotationSpeed = 8;

        tex = new Texture("Player.png");
        sprite = new Sprite(tex);
        sprite.setPosition(0,0);
        setX(Gdx.graphics.getWidth()/2);
        setY(Gdx.graphics.getHeight()/2);

    }

    public void act(float delta){

        //LAST SHOT FIRED
        lastShot += delta;

        //KEEPS TRACK OF LAST COORDINATES FOR OUT OF BOUNDS RESETTING AND CAMERA UPDATES
        float lastX = getX();
        float lastY = getY();

        //DIRECTIONAL INPUT
        if(Gdx.input.isKeyPressed(Input.Keys.A)){
            setPosition(getX() - speed, getY());
        }
        if(Gdx.input.isKeyPressed(Input.Keys.W)){
            setPosition(getX(),getY()+speed);

        }
        if(Gdx.input.isKeyPressed(Input.Keys.S)){
            setPosition(getX(),getY()-speed);

        }
        if(Gdx.input.isKeyPressed(Input.Keys.D)){
            setPosition(getX() + speed, getY());

        }

        //ADD BACK ROTATION FUNCTIONS ONCE A SPRITE HAS BEEN ADDED
        if(Gdx.input.isKeyPressed(Input.Keys.J)) {
            sprite.rotate(rotationSpeed);

        }

        if(Gdx.input.isKeyPressed(Input.Keys.L)){
            sprite.rotate(-rotationSpeed);
        }

        if(Gdx.input.isKeyPressed(Input.Keys.SPACE)){
            if(lastShot>shotDelay) {
                this.getParent().addActor(new Bullet(sprite.getX() + sprite.getWidth() / 2, sprite.getY() + sprite.getHeight() / 2, sprite.getRotation()%360));
                laser.play();
                lastShot=0;
            }
        }


        if(sprite.getRotation()<0){
            sprite.setRotation(360+sprite.getRotation());
        }




        setRotation(rotation);

        getStage().getCamera().translate(this.getX()-lastX, this.getY()-lastY,0);

        sprite.setPosition((float)getX(),(float)getY());
    }

    public void draw(Batch batch, float parentAlpha){

        sprite.draw(batch);

    }

    void destroy(){
        this.remove();
        Gdx.app.exit();
    }

    Rectangle getBoundBox(){

        return boundBox;
    }
}
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

    private final float speed=3;
    private final ArrayList bullets;
    private float rotation;
    private final float shotDelay;
    private float lastShot;
    private final float rotationSpeed;
    private OrthographicCamera cam;
    private final Rectangle boundBox = null;
    private final Texture tex;
    private final Sprite sprite;
    private final Sound laser = Gdx.audio.newSound(Gdx.files.internal("sounds/shoot-01.wav"));

    public Player(){

        bullets = new ArrayList();
        shotDelay = .1f;
        lastShot=0;
        rotationSpeed = 5;

        tex = new Texture("Player.png");
        sprite = new Sprite(tex);
        sprite.setPosition(0,0);
        setX(0);
        setY(0);

    }

    public void act(float delta){

        //System.out.println(sprite.getRotation()%360);
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

        //LASER
        //todo: fix sprinkler glitch
        if(Gdx.input.isKeyPressed(Input.Keys.SPACE)){
            if(lastShot>shotDelay) {
                this.getParent().addActor(new Bullet(sprite.getX() + sprite.getWidth() / 2, sprite.getY() + sprite.getHeight() / 2, sprite.getRotation()%360));
                laser.play();
                lastShot=0;
            }
        }






        setRotation(rotation);

        //PERFORM CAMERA ROTATION LAST
        //getStage().getCamera().translate(this.getX()-lastX, this.getY()-lastY,0);

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
package com.solanum.hybridity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import java.awt.geom.Area;


/**
 * @author Aldous
 *
 * "Discontinuum" - Ital Tek
 *
 * Follows the player around the playing field and is destroyed after it's HP is depleted.
 */
public class Enemy extends Actor {

    public final Rectangle collision = new Rectangle();
    private float[] v;
    private final ShapeRenderer render = new ShapeRenderer();
    private final Texture tex = new Texture("Seeker.png");
    private Sprite sprite = new Sprite(tex);
    private final Sound hit = Gdx.audio.newSound(Gdx.files.internal("sounds/explosion-01.wav"));
    private final Sound death = Gdx.audio.newSound(Gdx.files.internal("sounds/explosion-03.wav"));
    private int gX;
    private int gY;
    int speed = 3;
    private java.awt.Polygon octagon;
    private Area territory = new Area();
    private float radius;
    private float startAngle;
    boolean following = true;
    private float timeSinceHit;
    private int HP = 3;


    /**
     * Creates a basic Seeder enemy that is spawned at a specified x and y and given an origin x and y to follow.
     * @param x The x coordinate of the point that the seeder will be spawned at
     * @param y The y coordinate of the point that the seeder will be spawned at
     * @param goalX The x coordinate of the point that the seeder will follow
     * @param goalY The y coordinate of the point that the seeder will follow
     */
    Enemy(int x, int y, int goalX, int goalY, float rotation) {
        sprite.setOrigin(x, y);
        sprite.setX(x + sprite.getWidth() / 2);
        sprite.setY(y + sprite.getHeight() / 2);

        gX = goalX;
        gY = goalY;

        setX(x);
        setY(y);

        setOrigin(getX() + getHeight() / 2, getY() + getWidth() / 2);

        collision.set(sprite.getBoundingRectangle());
        //sprite.setRotation(rotation-180);


    }


    public void act(float delta) {
        /**
         * Checks to see if the seeder is colliding with the Mainland.
         *
         */

        gX = (int )getStage().getRoot().findActor("player").getX();
        gY = (int )getStage().getRoot().findActor("player").getY();


        if (following) {
            setPosition(getX() - ((getX() - gX) * delta), getY() - ((getY() - gY) * delta));

        }

        /**
         * Keeps track of the last time this seeder was hit by a bullet
         */
        timeSinceHit += delta;

        sprite.setPosition(getX(), getY());
        collision.set(sprite.getBoundingRectangle());

        lookAt(gX, gY);



    }


    public void draw(Batch batch, float parentAlpha) {

        /**
         * Flashes the Enemy whenever it is hit by alaser
         */
        if (timeSinceHit < .1) {
            sprite.setColor(Color.CYAN);
        }



        sprite.draw(batch);
        sprite.setColor(Color.MAGENTA);


        sprite.setRotation(getRotation());




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

            this.remove();
        }
    }

    /**
     * Accepts an x and a y value and returns the quadrant the trigonometric quadrant that it is in relevant to this shell's x and y
     * @param goalX The X coordinate of the position to check.
     * @param goalY The Y coordinate of the position to check.
     * @return integer (1-4) representing the counter-clockwise quadrant of the passed in position.
     */
    public int determineQuadrant(float goalX, float goalY){
        int quadrant = 0;
        float x = getX();
        float y = getY();

        if(goalX>x){
            if(goalY>y){
                quadrant=1;
            }else{
                quadrant=4;
            }
        } else{
            if(goalY>y){
                quadrant=2;
            } else{
                quadrant=3;
            }
        }
        return quadrant;
    }


    /**
     * Function that accepts a goal object that this object is to be rotated to face.
     * Calculates the degree position of the goal relevant to this entity and
     * snaps to it immediately.
     *
     *
     */
    private void lookAt(int x , int y){



        double triangleHeight;
        double triangleWidth;

        float theta;
        int goalQuadrant;

        float partnerX = x;
        float partnerY= y;

        float thisX = getX();
        float thisY = getY();
        float goalX = (thisX+partnerX)/2;
        float goalY = (thisY+partnerY)/2;

        goalQuadrant = determineQuadrant(goalX, goalY);

        triangleWidth = Math.abs(goalX-thisX);
        triangleHeight = Math.abs(goalY-thisY);

        float opposite;
        float adjacent;

        if(goalQuadrant==1 || goalQuadrant==3){

            opposite=(float)triangleHeight;
            adjacent= (float)triangleWidth;
        } else{
            opposite = (float)triangleWidth;
            adjacent=(float)triangleHeight;
        }

        float arctan = opposite/adjacent;

        theta = (float)Math.toDegrees(Math.atan(arctan));

        float goalDegree = 90*(goalQuadrant-1)+theta;


        float rotation = getRotation();

        if(rotation<goalDegree){
            if(rotation>goalDegree-180){
                rotateBy(goalDegree-rotation);
            } else{
                rotateBy(-((360-goalDegree)+rotation));
            }
        } else{
            if(goalDegree>rotation-180){
                rotateBy(-(rotation-goalDegree));
            } else{
                rotateBy((360-rotation)+goalDegree);
            }
        }
    }
}

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

import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Line2D;
import java.awt.geom.PathIterator;
import java.util.ArrayList;

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
    private final Sprite sprite = new Sprite(tex);
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
    private float getTimeSinceUpdate = 0;
    private int HP = 6;


    /**
     * Creates a basic Seeder enemy that is spawned at a specified x and y and given an origin x and y to follow.
     * @param x The x coordinate of the point that the seeder will be spawned at
     * @param y The y coordinate of the point that the seeder will be spawned at
     * @param goalX The x coordinate of the point that the seeder will follow
     * @param goalY The y coordinate of the point that the seeder will follow
     */
    Enemy(int x, int y, int goalX, int goalY, Mainland goal) {
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
         *
         */

        gX = (int )getStage().getRoot().findActor("player").getX();
        gY = (int )getStage().getRoot().findActor("player").getY();


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

    }


    public void draw(Batch batch, float parentAlpha) {

        /**
         * Flashes the Enemy whenever it is hit by alaser
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
}

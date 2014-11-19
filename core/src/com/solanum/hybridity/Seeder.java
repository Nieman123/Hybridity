package com.solanum.hybridity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * @Author Aldous
 * "You Know I Have To Go" - Royksopp
 */
public class Seeder extends Actor {

    Texture tex = new Texture("Wanderer.png");
    Sprite sprite = new Sprite(tex);


    Seeder(float x, float y){
        sprite.setOrigin(x,y);
    }



}

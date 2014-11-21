package com.solanum.hybridity;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class Hybridity extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;
    Stage gameStage;
    int numOfSeeds = 6;
    float degreeDivision;

	@Override
	public void create () {
        gameStage = new Stage();

        Player player = new Player();
        Mainland ml = new Mainland(Gdx.graphics.getWidth()/2,Gdx.graphics.getHeight()/2);
        gameStage.addActor(ml);
        gameStage.addActor(player);

        degreeDivision=360/numOfSeeds;


        /**
         * Build circle of seeders
         */
        float speed = 800;
        float height;
        float width;
        float angle;

        int mlX = ml.oX;
        int mlY = ml.oY;

        for(int i =0;i<numOfSeeds;i++){
            float sin = (float)Math.sin(Math.toRadians(degreeDivision*i));
            height = sin * speed;
            width = (float)Math.sqrt((speed * speed) - (height * height));

            if (degreeDivision > 90 && degreeDivision < 270) {
                width = -width;

            }

            gameStage.addActor(new Seeder(mlX+width, mlY+height,mlX,mlY));

        }



	}

	@Override
	public void render () {
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        gameStage.act();
        gameStage.draw();

	}
}

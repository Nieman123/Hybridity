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
    Mainland ml;

	@Override
	public void create () {
        gameStage = new Stage();

        Player player = new Player();
        ml = new Mainland(Gdx.graphics.getWidth()/2,Gdx.graphics.getHeight()/2);
        gameStage.addActor(ml);
        gameStage.addActor(player);

        degreeDivision=360/numOfSeeds;



        for(int i=0; i<360;i++){
            plantSeed(i, 400);
        }
	}

	@Override
	public void render () {
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        gameStage.act();
        gameStage.draw();

	}


    public void plantSeed(float angle, float distance ){

        angle = angle %360;
        float nAngle = angle %90;

        float adjacent;
        float opposite;
        float hypoteneuse = distance;
        double sin = Math.sin(Math.toRadians(nAngle));

        opposite =(float) sin * hypoteneuse;

        adjacent = (float)Math.sqrt((hypoteneuse*hypoteneuse)-(opposite*opposite));

        gameStage.addActor(new Seeder(ml.oX+adjacent,ml.oY+opposite,ml.oX, ml.oY ));



    }
}

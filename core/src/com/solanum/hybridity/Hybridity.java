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

	@Override
	public void create () {
        gameStage = new Stage();

        Player player = new Player();
        Mainland ml = new Mainland(Gdx.graphics.getWidth()/2,Gdx.graphics.getHeight()/2);
        gameStage.addActor(ml);
        gameStage.addActor(player);
	}

	@Override
	public void render () {
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        gameStage.act();
        gameStage.draw();

	}
}

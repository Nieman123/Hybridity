package com.solanum.hybridity;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;

/**
 * @author Aldous
 */
public class Hybridity extends Game {
    public Screen splashScreen;
    public Screen gameScreen;
    public Screen titleScreen;
    @Override
    public void create() {
        titleScreen = new TitleScreen(this);
        gameScreen = new GameScreen(this);
        splashScreen = new SplashScreen(this);
        this.setScreen(splashScreen);
    }
}

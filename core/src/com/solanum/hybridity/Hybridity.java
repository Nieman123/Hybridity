package com.solanum.hybridity;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;

/**
 * @author Aldous
 *
 * Top level entry point into the game loop that is started by the platform launcher.
 */
public class Hybridity extends Game {
    private Screen splashScreen;
    public Screen gameScreen;
    public Screen titleScreen;
    public Screen loseScreen;

    @Override
    public void create() {
        titleScreen = new TitleScreen(this);
        gameScreen = new GameScreen(this);
        splashScreen = new SplashScreen(this);
        loseScreen = new LoseScreen(this);
        this.setScreen(splashScreen);
    }
}

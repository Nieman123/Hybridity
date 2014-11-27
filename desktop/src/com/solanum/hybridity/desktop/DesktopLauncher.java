package com.solanum.hybridity.desktop;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.solanum.hybridity.Hybridity;

class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

        config.vSyncEnabled=true;
        config.fullscreen = true;

        config.width = 1920;
        config.height = 1080;



        new LwjglApplication(new Hybridity(), config);
	}
}

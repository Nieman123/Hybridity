package com.solanum.hybridity.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.solanum.hybridity.Hybridity;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

        config.height=800;
        config.width=800;


		new LwjglApplication(new Hybridity(), config);
	}
}

package com.duckhat.game;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
//import com.duckhat.game.MyFirstGdxGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setTitle("My First GDX Game");
		config.setWindowedMode(800, 480);
		config.useVsync(true);
		config.setForegroundFPS(60);

		//new Lwjgl3Application(new MyFirstGdxGame(), config);
		new Lwjgl3Application(new Drop(), config);
	}
}

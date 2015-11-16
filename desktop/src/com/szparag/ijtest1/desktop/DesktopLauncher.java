package com.szparag.ijtest1.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.szparag.ijtest1.CivMain;

public class DesktopLauncher
{
	public static void main (String[] arg)
    {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

        config.fullscreen = true;
        config.vSyncEnabled = true;
        config.foregroundFPS = 60;
        config.width = 1920;
        config.height = 1080;


		new LwjglApplication(new CivMain(), config);
	}

}

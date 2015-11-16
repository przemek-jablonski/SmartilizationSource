package com.szparag.ijtest1.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.szparag.ijtest1.CivServer;

/**
 * Created by Szparagowy Krul 3000 on 17/05/2015.
 */
public class DesktopCivServerLauncher {

    public static void main (String[] arg)
    {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

        config.fullscreen = false;
        config.vSyncEnabled = true;
        config.backgroundFPS = 60;
        config.foregroundFPS = 60;
        config.width = 800;
        config.height = 600;


        new LwjglApplication(new CivServer(), config);
    }

}

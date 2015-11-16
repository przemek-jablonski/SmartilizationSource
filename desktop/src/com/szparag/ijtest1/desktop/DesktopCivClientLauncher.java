package com.szparag.ijtest1.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.szparag.ijtest1.CivClient;
import com.szparag.ijtest1.CivServer;

/**
 * Created by Szparagowy Krul 3000 on 18/05/2015.
 */
public class DesktopCivClientLauncher {
    public static void main (String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

        config.fullscreen = false;
        config.vSyncEnabled = true;
        config.backgroundFPS = 60;
        config.foregroundFPS = 60;
        config.width = 1920;
        config.height = 1080;


        new LwjglApplication(new CivClient(), config);
    }
}

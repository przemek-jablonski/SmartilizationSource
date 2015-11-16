package com.szparag.ijtest1.Assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;

import java.util.ArrayList;

/**
 * Created by Szparagowy Krul 3000 on 21/05/2015.
 */
public class AManager {
    private static AssetManager assetManager;
    private static ArrayList<String> texturesheets, actorsunits, actorsbuildings, vfx, particleassets, particleeffects;

    public AManager(){
        assetManager = new AssetManager();
        stringInsert();

   //     assetManager.get()
    }

    private void stringInsert(){
        texturesheets = new ArrayList<String>();
        texturesheets.add("tilesheet_light.png");
        texturesheets.add("tilesheet_med.png");
        texturesheets.add("tilesheet_heavy.png");
    }

}

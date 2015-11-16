package com.szparag.ijtest1;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Tile
{
 //   protected   Texture     TileTexture;
    protected   Sprite      TileSprite;

    public Actor.ActorType actorType;
    public ActorLand.ActorLandType actorLandType;
    public ActorBuilding.ActorBuildingType actorBuildingType;
    public ActorUnit.ActorUnitType actorUnitType;
    int landVariation;


    enum TileType {
        LAND, BUILDING, UNIT;
    }
    enum BuildingType {
        BUILDING, CASTLE, NO;
    }
    enum LandType {
        GRASS, PRERISH, ROCKY, DESERT, MOUNTAIN, WATER, NO;
    }
    enum UnitType {
        UNIT1WARRIOR, UNIT2ARCHER, NO;
    }

    public Tile(int x0, int y0, int width, int height, Actor.ActorType act, ActorLand.ActorLandType alt, int landVariation) {
      //  TileSprite = new Sprite(new TextureRegion(tilesheet, x0, y0, width, height));
      //  TileSprite.setSize(50f, 50f);
        actorType = act;
        actorLandType = alt;
        this.landVariation = landVariation;
       // setSpriteCenter();
    }

    public Tile(Actor.ActorType act, ActorBuilding.ActorBuildingType abt)
    {
    //    TileTexture = new Texture(filename);
    //    TileSprite = new Sprite(TileTexture);
//        TileSprite = new Sprite(new Texture(filename));
        actorType = act;
        actorBuildingType = abt;
      //  setSpriteCenter();
    }

    public Tile(Actor.ActorType act, ActorUnit.ActorUnitType aut)
    {
//        TileSprite = new Sprite(new Texture(filename));
        actorType = act;
        actorUnitType = aut;
     //   setSpriteCenter();
    }

    // _________________________________________________
    //      --- AKCESORY ---
    public Sprite getSprite()
    {
        return TileSprite;
    }

  //  public Texture getTexture()
  //  {
  //      return TileTexture;
  //  }

   // public boolean IsWalkable()
  //  {
 //       return walkable;
  //  }

  //  public boolean IsLand()
  //  {
  //      return land;
  //  }


}

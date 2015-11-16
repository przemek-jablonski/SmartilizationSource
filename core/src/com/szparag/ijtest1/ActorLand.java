package com.szparag.ijtest1;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

class ActorLand extends Actor
{


    private static final long serialVersionUID = -2847400855378342590L;

    /* exists in class above (Actor)
        enum ActorType {
            LAND, BUILDING, UNIT;
        }
        */
    enum ActorLandType {
        GRASS, PRERISH, ROCKY, DESERT, MOUNTAIN, WATER;
    }




    //ActorType actorType
    ActorLandType   actorLandType;
    int actorLandTypeVariation;

    //Detailed <Land> Attributes Info:
    int             terrainModifier;
    boolean         isWalkable;
    boolean         isLand;

    //stats ver2:
    float   attackModifier;
    float   defendModifier;
    float   moveCost;

    public boolean initializeStats() {
        switch (actorLandType) {
            case GRASS:
                attackModifier = 11f;
                defendModifier = 10f;
                moveCost = 0f;
                break;
            case PRERISH:
                attackModifier = 10f;
                defendModifier = 10f;
                moveCost = 0f;
                break;
            case ROCKY:
                attackModifier = 5f;
                defendModifier = 15f;
                moveCost = 0f;
                break;
            default:
                attackModifier = 7777f;
                defendModifier = 666f;
                moveCost = 100000f;
                break;
        }
        return true;
    }

    ActorLand(Vector2Integer position) {
        super();
        drawPosition = position;
        actorType = ActorType.LAND;
     //   player = CivilizationMultiMain.Player.LAND;
    }

    public void assign(Tile tile) {
        exists = true;
        actorType = tile.actorType;
        actorLandType = tile.actorLandType;
        actorLandTypeVariation = tile.landVariation;
      //  sprite = tile.getSprite();
        initializeStats();
        assignTileAttributes();
    }

    private void assignTileAttributes()
    {

        switch(actorLandType)
        {
            case GRASS:
                isWalkable = true;
                isLand = true;
                terrainModifier = 3;
                break;
            case PRERISH:
                isWalkable = true;
                isLand = true;
                terrainModifier = 3;
                break;
            case ROCKY:
                isWalkable = true;
                isLand = true;
                terrainModifier = 2;
                break;
            case DESERT:
                isWalkable = true;
                isLand = true;
                terrainModifier = 2;
                break;
            case MOUNTAIN:
                isWalkable = false;
                isLand = true;
                terrainModifier = 1;
                break;
            case WATER:
                isWalkable = false;
                isLand = false;
                terrainModifier = 1;
                break;
        }
    }

}

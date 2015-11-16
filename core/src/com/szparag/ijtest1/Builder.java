package com.szparag.ijtest1;

/**
 * Created by Szparagowy Krul 3000 on 30/05/2015.
 */
public class Builder {

    InitializerHexmap initializerHexmap;

    public Builder(InitializerHexmap initializerHexmap){
        this.initializerHexmap = initializerHexmap;
    }

    public void buildWatermill(Hex hex){
        hex.actorList.remove(hex.actorList.size()-1);
        hex.assignBuilding(initializerHexmap.Tile2buildings[3], PlayerENUM.playerONE);
    }

    public void buildArmory(Hex hex){
        hex.actorList.remove(hex.actorList.size()-1);
        hex.assignBuilding(initializerHexmap.Tile2buildings[5], PlayerENUM.playerONE);
    }

    public void buildStable(Hex hex){
        hex.actorList.remove(hex.actorList.size()-1);
        hex.assignBuilding(initializerHexmap.Tile2buildings[4], PlayerENUM.playerONE);
    }

    public void buildCastle(Hex hex){
        hex.actorList.remove(hex.actorList.size()-1);
        hex.assignBuilding(initializerHexmap.Tile2buildings[2], PlayerENUM.playerONE);
    }

}

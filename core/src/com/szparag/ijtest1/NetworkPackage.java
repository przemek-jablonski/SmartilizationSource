package com.szparag.ijtest1;

import java.io.Serializable;
import java.util.Random;

/**
 * Created by Szparagowy Krul 3000 on 05/06/2015.
 */
public class NetworkPackage implements Serializable {

    private static final long serialVersionUID = -5013077202478850790L;
    private Hex[][] hexmap;
    private boolean mapInitialized;
    private float diagNumber = 0;
    private int changesCount = 0;
    private Random random;
    private PlayerENUM activePlayer = PlayerENUM.NOTINITIALISED;
    private boolean changeturn = false;

    public void setChangeturn(boolean changeturn) {
        this.changeturn = changeturn;
    }

    public void setActivePlayer(PlayerENUM activePlayer) {
        this.activePlayer = activePlayer;
    }

    public PlayerENUM getActivePlayer() {
        return activePlayer;
    }

    public boolean isChangeturn() {
        return changeturn;
    }

    public NetworkPackage(){
        mapInitialized = false;
        random = new Random();
        incrementChangesCount();
        setNewDiagNumber();
        InitializerHexmap initializerHexmap = new InitializerHexmap(new Vector2Integer(45,45), 2);
        hexmap = initializerHexmap.testinit();
    }

    public void initializeMap(Hex[][] mapInit, Vector2Integer hexmapdimensions){
        mapInitialized = true;
        incrementChangesCount();
        setNewDiagNumber();
        hexmap = mapInit;
    }

    public void updateMap(Hex[][] mapUpdate){
        incrementChangesCount();
        setNewDiagNumber();
        hexmap = mapUpdate;
    }

    public void packageStatePrinter(){
        System.out.println("!packagestateprinter:");
        System.out.println("!changescount: " + changesCount + ". init: " + mapInitialized +  ". diagnumber: " + diagNumber);
    }

    public Hex[][] getHexmap() {
        return hexmap;
    }

    public void setHexmap(Hex[][] hexmap) {
        this.hexmap = hexmap;
    }

    public boolean isMapInitialized() {
        return mapInitialized;
    }

    public void setMapInitialized(boolean mapInitialized) {
        this.mapInitialized = mapInitialized;
    }

    public float getDiagNumber() {
        return diagNumber;
    }

    public void setNewDiagNumber() {
        this.diagNumber = (random.nextInt(10000000) * random.nextFloat());
    }

    public int getChangesCount() {
        return changesCount;
    }

    public void incrementChangesCount() {
        ++changesCount;
    }

}

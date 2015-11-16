package com.szparag.ijtest1.Configurators;

import org.ini4j.Wini;

import java.io.File;
import java.io.IOException;

/**
 * Created by Szparagowy Krul 3000 on 08/06/2015.
 */
public class GameConfig {
    /*
    public enum GCPlayerno { TWO, FOUR }
    public enum GCMapSize { S, M, L, XL }
    public enum GCTreasures { NONE, MOD, VAST }
    public enum GCTurnCount { SHORT, MEDIUM, LONG, LONGER }
    public enum GCGameType { FFA, TURNS }
    public enum GCLandType { GRASS, DES, ROCKY }
    public enum GCStatsMode { NORM, HARDCORE }

    private String  host;
    private int  port;
    private GCPlayerno gcPlayerno;
    private GCMapSize gcMapSize;
    private GCTreasures gcTreasures;
    private GCTurnCount gcTurnCount;
    private GCGameType gcGameType;
    private GCLandType gcLandType;
    private GCStatsMode gcStatsMode;
    private Wini configFile;
    */

    /*
    public GameConfig(){
        host = "localhost";
        port = 9999;
        gcPlayerno = GCPlayerno.TWO;
        gcMapSize = GCMapSize.M;
        gcTreasures = GCTreasures.NONE;
        gcTurnCount = GCTurnCount.MEDIUM;
        gcGameType = GCGameType.FFA;
        gcLandType = GCLandType.GRASS;
        gcStatsMode = GCStatsMode.NORM;
    }

    public void inireader() throws IOException{
        configFile = new Wini(new File("configgame.ini"));
        readIniFile(configFile);
    }

    public void readIniFile(Wini ini){
        String playerno = configFile.get("gameconfig", "playernumber");
        String mapsize = configFile.get("gameconfig", "mapsize");
        String treasures = configFile.get("gameconfig", "treasures");
        String turncount = configFile.get("gameconfig", "turncount");
        String gametype = configFile.get("gameconfig", "gametype");
        String landtype = configFile.get("gameconfig", "landtype");
        String statsmode = configFile.get("gameconfig", "statsmode");

        switch(playerno) {
            case "2": gcPlayerno = GCPlayerno.TWO; break;
            case "4": gcPlayerno = GCPlayerno.FOUR; break;
        }

        switch(mapsize) {
            case "s": gcMapSize = GCMapSize.S; break;
            case "m": gcMapSize = GCMapSize.M; break;
            case "l": gcMapSize = GCMapSize.S; break;
            case "xl": gcMapSize = GCMapSize.S; break;
        }

        switch(treasures) {
            case "none": gcTreasures = GCTreasures.NONE; break;
            case "med": gcTreasures = GCTreasures.MOD; break;
            case "vast": gcTreasures = GCTreasures.VAST; break;
        }

        switch(turncount) {
            case "20": gcTurnCount = GCTurnCount.SHORT; break;
            case "40": gcTurnCount = GCTurnCount.MEDIUM; break;
            case "60": gcTurnCount = GCTurnCount.LONG; break;
            case "100": gcTurnCount = GCTurnCount.LONGER; break;
        }

        switch(gametype) {
            case "FFA": gcGameType = GCGameType.FFA; break;
            case "turns": gcGameType = GCGameType.TURNS; break;
        }

        switch(landtype) {
        }
    }



    public GCPlayerno getGcPlayerno() {
        return gcPlayerno;
    }

    public void setGcPlayerno(GCPlayerno gcPlayerno) {
        this.gcPlayerno = gcPlayerno;
    }

    public GCMapSize getGcMapSize() {
        return gcMapSize;
    }

    public void setGcMapSize(GCMapSize gcMapSize) {
        this.gcMapSize = gcMapSize;
    }

    public GCTreasures getGcTreasures() {
        return gcTreasures;
    }

    public void setGcTreasures(GCTreasures gcTreasures) {
        this.gcTreasures = gcTreasures;
    }

    public GCTurnCount getGcTurnCount() {
        return gcTurnCount;
    }

    public void setGcTurnCount(GCTurnCount gcTurnCount) {
        this.gcTurnCount = gcTurnCount;
    }

    public GCGameType getGcGameType() {
        return gcGameType;
    }

    public void setGcGameType(GCGameType gcGameType) {
        this.gcGameType = gcGameType;
    }

    public GCLandType getGcLandType() {
        return gcLandType;
    }

    public void setGcLandType(GCLandType gcLandType) {
        this.gcLandType = gcLandType;
    }

    public GCStatsMode getGcStatsMode() {
        return gcStatsMode;
    }

    public void setGcStatsMode(GCStatsMode gcStatsMode) {
        this.gcStatsMode = gcStatsMode;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

*/

}

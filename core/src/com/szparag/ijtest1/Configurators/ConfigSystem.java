package com.szparag.ijtest1.Configurators;

import org.ini4j.Ini;
import org.ini4j.Profile;
import org.ini4j.Wini;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by Szparagowy Krul 3000 on 12/06/2015.
 */
public class ConfigSystem implements ConfigFileReadWrite {

    public enum GCPlayerno { TWO, FOUR }
    public enum GCMapSize { S, M, L, XL }
    public enum GCTreasures { NONE, MOD, VAST }
    public enum GCTurnCount { SHORT, MEDIUM, LONG, LONGER }
    public enum GCGameType { FFA, TURNS }
    public enum GCLandType { GRASS, DES, ROCKY }
    public enum GCStatsMode { NORM, HARDCORE }

    private Wini configgameFile;
    private Wini configsystemFile;
    private Ini.Section configgameSection;
    private Ini.Section systemgameSection;

    private String  host;
    private int     port;
    private GCPlayerno gcPlayerno = GCPlayerno.FOUR;
    private GCMapSize gcMapSize;
    private GCTreasures gcTreasures;
    private GCTurnCount gcTurnCount;
    private GCGameType gcGameType;
    private GCLandType gcLandType;
    private GCStatsMode gcStatsMode;

    private String playerno = new String();
    private String mapsize = new String();
    private String treasures = new String();
    private String turncount = new String();
    private String gametype = new String();
    private String landtype = new String();
    private String statsmode = new String();
    private boolean diagboxenabler = true;

    public ConfigSystem() throws IOException {
        fileInitialize();
    }

    public ConfigSystem(String customFileLocation) throws IOException {
        fileInitialize(customFileLocation);
    }

    @Override
    public boolean fileInitialize() throws IOException {
        configgameFile = new Wini(new File("configgame.ini"));
        configgameSection = (Profile.Section)configgameFile.get("gameconfig");
        configgameSection = (Profile.Section)configgameFile.get("systemconfig");
        configsystemFile = new Wini(new File("configsystem.ini"));
        fileRead();
        return true;
    }

    public boolean fileInitialize(String customFileLocation) throws IOException {
        configgameFile = new Wini(new File(customFileLocation));
        configgameSection = (Profile.Section)configgameFile.get("gameconfig");
        configgameSection = (Profile.Section)configgameFile.get("systemconfig");
      //  configsystemFile = new Wini(new File("configsystem.ini"));
        fileRead();
        return true;
    }

    @Override
    public boolean fileRead() throws FileNotFoundException {
        readGameStrings();
        readSystemStrings();
        updateStats();
        debugPrinter();
        return true;
    }

    @Override
    public void updateStats() {
        convertToEnumGame();
        convertToEnumSystem();
    }

    private void readGameStrings() {
        playerno = configgameFile.get("gameconfig", "playernumber");
        mapsize = configgameFile.get("gameconfig", "mapsize");
        treasures = configgameFile.get("gameconfig", "treasures");
        turncount = configgameFile.get("gameconfig", "turncount");
        gametype = configgameFile.get("gameconfig", "gametype");
        landtype = configgameFile.get("gameconfig", "landtype");
        statsmode = configgameFile.get("gameconfig", "statsmode");
        String diagbox = configgameFile.get("systemconfig", "diagbox");
        if(diagbox == "1")
            diagboxenabler = true;
        else
            diagboxenabler = false;

    }

    private void readSystemStrings() {
        /*
        ...
         */
    }

    private void convertToEnumGame() {
        switch(playerno) {
            case "2": gcPlayerno = GCPlayerno.TWO; break;
            case "4": gcPlayerno = GCPlayerno.FOUR; break;
        }

        switch(mapsize) {
            case "s": gcMapSize = GCMapSize.S; break;
            case "m": gcMapSize = GCMapSize.M; break;
            case "l": gcMapSize = GCMapSize.L; break;
            case "xl": gcMapSize = GCMapSize.XL; break;
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
            case "ffa": gcGameType = GCGameType.FFA; break;
            case "turns": gcGameType = GCGameType.TURNS; break;
        }

        switch(landtype) {
            case "grass": gcLandType = GCLandType.GRASS; break;
            case "desert": gcLandType = GCLandType.DES; break;
            case "rocky": gcLandType = GCLandType.ROCKY; break;
        }

        switch(statsmode) {
            /*
            ...
             */
        }
    }

    private void convertToEnumSystem() {

    }

    @Override
    public void fileWrite(String sectionname, String optionname, String optionvalue) throws IOException {
        configgameFile.put(sectionname, optionname, optionvalue);
        configgameFile.store();
        fileRead();
    }

    @Override
    public void fileWrite() throws IOException {
    }

    @Override
    public void debugPrinter() {
        System.out.println("*no elo, tutaj config:");
        System.out.println("*turncount to:" + gcTurnCount.toString());

    }

    public boolean isDiagboxenabler() {
        return diagboxenabler;
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

    public String getTurncountString() {
        return turncount;
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

    public String getPlayerno() {
        return playerno;
    }

    public String getMapsize() {
        return mapsize;
    }

    public String getTreasures() {
        return treasures;
    }

    public String getTurncount() {
        return turncount;
    }

    public String getGametype() {
        return gametype;
    }

    public String getLandtype() {
        return landtype;
    }

    public String getStatsmode() {
        return statsmode;
    }
}

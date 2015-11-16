package com.szparag.ijtest1;

import com.sun.org.apache.xml.internal.security.Init;

import java.io.Serializable;
import java.util.Random;

/**
 * Created by Szparagowy Krul 3000 on 21/05/2015.
 */
public class CivNetworkPacket implements Serializable {

    private static final long serialVersionUID = 5366934445747379961L;
    public Hex[][] hexmap;
    private Vector2Integer hexmapdimensions;
    private int playernumber;
    private String diagString;
    private float diagFloat;
    enum PacketState {
        NONEXISTANT, CONSTRUCTED, FIRSTSEND, HEXMAPINJECTED, ONLINE
    }



    private PacketState packetState = PacketState.NONEXISTANT;

    public PacketState getPacketState() {
        return this.packetState;
    }

    public PacketState packetStateIncrement() {
        switch(packetState){
            case NONEXISTANT:
                this.packetState = PacketState.CONSTRUCTED;
                return packetState;
            case CONSTRUCTED:
                this.packetState = PacketState.FIRSTSEND;
                return packetState;
            case FIRSTSEND:
                this.packetState = PacketState.HEXMAPINJECTED;
                return packetState;
            case HEXMAPINJECTED:
                this.packetState = PacketState.ONLINE;
                return packetState;
            case ONLINE:
                return packetState;
        }
        return PacketState.NONEXISTANT;
    }

    public CivNetworkPacket() {
    //    hexmap = new Hex[][];
        hexmapdimensions = new Vector2Integer();
        diagString = new String();
        packetStateIncrement();
    }

    public CivNetworkPacket(CivNetworkPacket civNetworkPacket){

    }

    public void initialize(InitializerHexmap initializerHexmap) {
        hexmap = initializerHexmap.init();
        Random random = new Random();
        diagFloat = random.nextFloat() * random.nextFloat() * random.nextInt(10000);
        this.hexmapdimensions = initializerHexmap.hexmapdimensions;
        this.playernumber = initializerHexmap.playernumber;
    }

    public void updatePacket(CivNetworkPacket updatedCivNetworkPacket) {
        hexmap = updatedCivNetworkPacket.getHexmap();
        hexmapdimensions.set(updatedCivNetworkPacket.getHexmapdimensions());
        playernumber = updatedCivNetworkPacket.getPlayernumber();
        diagString =  updatedCivNetworkPacket.getDiagString();
        diagFloat = updatedCivNetworkPacket.getDiagFloat();
    }

    public Hex[][] getHexmap() {
        return hexmap;
    }

    public void setHexmap(Hex[][] hexmap) {
        this.hexmap = hexmap;
    }

    public Vector2Integer getHexmapdimensions() {
        return hexmapdimensions;
    }

    public void setHexmapdimensions(Vector2Integer hexmapdimensions) {
        this.hexmapdimensions = hexmapdimensions;
    }

    public int getPlayernumber() {
        return playernumber;
    }

    public void setPlayernumber(int playernumber) {
        this.playernumber = playernumber;
    }

    public String getDiagString() {
        return diagString;
    }

    public void setDiagString(String diagString) {
        this.diagString = new String(diagString);
    }


    public float getDiagFloat() {
        return diagFloat;
    }

    public void setDiagFloat(float diagFloat) {
        this.diagFloat = diagFloat;
    }
}

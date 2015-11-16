package com.szparag.ijtest1;

import java.io.Serializable;

/**
 * Created by Szparagowy Krul 3000 on 21/05/2015.
 */
public class PacketLogin implements Serializable {
    private static final long serialVersionUID = -3668421938061160040L;

    private String msg;
    public PacketLogin(String msg)
    {
        this.msg = msg;
    }
    public String getMessage()
    {
        return msg;
    }

    public void setMessage(String msg){
        this.msg = msg;
    }


}

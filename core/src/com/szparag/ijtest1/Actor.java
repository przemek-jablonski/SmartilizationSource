package com.szparag.ijtest1;


import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.io.Serializable;

class Actor implements Serializable
{

    private static final long serialVersionUID = -4085786521180243713L;
    protected boolean   exists;
    protected ActorType actorType;
    Vector2Integer      drawPosition;
    PlayerENUM   playerenum;

    enum ActorType {
        LAND, BUILDING, UNIT;
    }

    Actor() {
        exists = false;
    }




}

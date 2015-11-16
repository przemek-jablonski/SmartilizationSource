package com.szparag.ijtest1;

import java.util.Vector;

/**
 * Created by Szparagowy Krul 3000 on 11/05/2015.
 */
public class Vector2TileCoords extends Vector2Integer{
    public int x() {
        return x;
    }

    public int y() {
        return y;
    }

    public boolean equalszero() {
        if (x == 0 && y == 0)
            return true;
        return false;
    }

    public boolean equalsnegative() {
        if (x < 0 && y < 0)
            return true;
        return false;
    }

    public void reset()
    {
        x = -1;
        y = -1;
    }
}

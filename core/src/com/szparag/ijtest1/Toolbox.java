package com.szparag.ijtest1;

import com.badlogic.gdx.Gdx;

import java.util.List;

/**
 * Created by Szparagowy Krul 3000 on 29/05/2015.
 */
public class Toolbox {
    long applicationStartTime;

    public int logbase5int(int input) {
        return (int) (Math.log(input) / Math.log(5));
    }

    public void startTimer() {
        applicationStartTime = System.currentTimeMillis();
    }

    public void startZeroTimer()  {
        applicationStartTime = 0;
    }

    public long getTimemillis() {
        return System.currentTimeMillis() - applicationStartTime;
    }

    public long getTimeseconds() {
        return (System.currentTimeMillis() - applicationStartTime) / 1000;
    }

    public float animation_x_continuous(float multiplier) {
        return -(this.getTimemillis() / multiplier);
    }

    public double animation_x_continuousd(float multiplier) {
        double translation;
        translation = -( getTimemillis() *(1/multiplier) * 0.9147234783247131235);
        return translation;
    }

    public int animation_hole_centeringx(Vector2Integer position, int hole_dim) {
        int offset;
        offset = (hole_dim - 50) / 2;
        if (offset < 0) offset = 0;
        return ( position.x() * 50 ) - offset;
    }

    public int animation_hole_centeringy(Vector2Integer position, int hole_dim) {
        int offset;
        offset = (hole_dim - 50) / 2;
        if (offset < 0) offset = 0;
        return ( position.y() * 50 ) - offset;
    }

    public Vector2Integer nearestEmptyHexStd(Hex[][] hexmap, Vector2Integer centerPosition) {
        boolean isEmpty = true;
      //  while(isEmpty)
        return new Vector2Integer();
    }

    public boolean isItemInList(Vector2Integer item, List<Vector2Integer> list) {
        for (int i = 0; i < list.size(); ++i) {
            if (item.x() == list.get(i).x() && item.y() == list.get(i).y())
                return true;
        }
        return false;
    }

    public double alphamodulation_animation(int multiplier){
        return (getTimemillis()/100);
    }
}

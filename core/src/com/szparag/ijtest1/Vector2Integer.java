package com.szparag.ijtest1;


import com.badlogic.gdx.math.Vector2;

import java.io.Serializable;
import java.util.List;
import java.util.Vector;

class Vector2Integer implements Serializable
{
    private static final long serialVersionUID = 8406958951805713677L;
    //Fields
    protected int x, y;

    //______________________________________________________________________
    //Constructors
        public Vector2Integer() {
            x = -1;
            y = -1;
        }

        public Vector2Integer(Number a, Number b) {
            x = a.intValue();
            y = b.intValue();
        }

        public Vector2Integer(Vector2Integer inputvector) {
            x = inputvector.getx();
            y = inputvector.gety();
        }

    //______________________________________________________________________
    //properties - sets:
        public Vector2Integer set(Number a, Number b){
            x = a.intValue();
            y = b.intValue();
            return this;
        }

        public Vector2Integer set(Vector2Integer vint) {
            x = vint.getx();
            y = vint.gety();
            return this;
        }
        public Vector2Integer set(Vector2 vect) {
            this.x = (int)vect.x;
            this.x = (int)vect.x;
            return this;
        }

    //______________________________________________________________________
    //properties - gets:
        public int  getx() { return x; }
        public int  gety() { return y; }
        public int  x() { return x; }
        public int  y() { return y;}

    //______________________________________________________________________
    //auxiliary - CONTROLLERACTORMOVEMENT SUBSTRACTION:
    public int moveSubstraction(Vector2Integer targetposition){
        return (Math.abs(this.x - targetposition.x)) +
                (Math.abs(this.y - targetposition.y));
    }

    //______________________________________________________________________
    //auxiliary - additions:
        public void add(Vector2Integer v2int) {
            x += v2int.x;
            y += v2int.y;
        }

        public void add(Number a, Number b) {
            x += a.intValue();
            y += b.intValue();

        }
    //______________________________________________________________________
    //auxiliary - additions:
        public Vector2Integer substract(Vector2Integer vinput){
            Vector2Integer substracted = this;
            substracted.x = substracted.x - vinput.x;
            substracted.y = substracted.y - vinput.y;
            return substracted;
        }

    //______________________________________________________________________
    //auxiliary - divider:
        public Vector2Integer divide(Number a){
            Vector2Integer temporary = new Vector2Integer(this.x / a.intValue(), this.y / a.intValue());
            return temporary;
        }

    //______________________________________________________________________
    //auxiliary - comparators:
        public boolean compare(Vector2Integer vector) {
            if (x == vector.x() && y == vector.y())
                return true;
            return false;
        }

        public boolean compare(Number a, Number b) {
            if (x == a.intValue() && y == b.intValue())
                return true;
            return false;
        }
    //______________________________________________________________________
    //auxiliary - list tools:

        public boolean searchThroughList(List<Vector2Integer> list){
            for (int i=0; i < list.size(); ++i){
                if (list.get(i).x() == this.x()
                        && list.get(i).y() == this.y())
                    return true;
            }
            return false;
        }

    public boolean removeFromListComplete(List<Vector2Integer> list) {
        boolean deletion = false;
        for (int i=0; i < list.size(); ++i){
            if (list.get(i).x() == this.x() && list.get(i).y() == this.y()) {
                list.remove(i);
                deletion = true;
            }
        }
        return deletion;
    }

    //______________________________________________________________________
    //auxiliary - 'printer':
        public String printer() {
            return new String(" [x:" + this.x() + " / y:" + this.y() + "] ");
        }

        public void printerSystem(){
            System.out.println(" [x:" + this.x() + " / y:" + this.y() + "] ");
        }

}

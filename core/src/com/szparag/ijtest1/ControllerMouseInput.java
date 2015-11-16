package com.szparag.ijtest1;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g3d.Model;

import java.util.Stack;


public class ControllerMouseInput
{

    //CONTROLLER MOUSE INPUT, MARK2:
    //_________________________________________________________________________________________________
    //ONE TIME booleans + coordinates (in Vector forms) for both mouse buttons
    boolean lmbClicked = false;
 //   boolean rmbClicked = false;
    Vector2MouseCoords lmbClickedVector = new Vector2MouseCoords();
    Vector2MouseCoords rmbClickedVector = new Vector2MouseCoords();

    Stack<Vector2Integer> lmbClickedVectorStack = new Stack<Vector2Integer>();
    Stack<Vector2Integer> lmbHexSelectedStack = new Stack<Vector2Integer>();

    //REAL TIME, on-listener mouse and tile designations
    Vector2MouseCoords mouseHooverCoords = new Vector2MouseCoords();
    Vector2TileCoords  tileHooverCoords = new Vector2TileCoords();

    //EVENT BASED, on-demand hex selection
    boolean lmbHexSelected = false;
    boolean lmbHexSelectedStroke = false;
    Vector2TileCoords lmbHexSelectedVector = new Vector2TileCoords();

    //auxiliary objects with useful data
    Vector2Integer cameralocation;
    double         camerazoom;
    int            hexPixelSize;

    //selection (selected type) auxiliary references:
    ModelActorEditor modelActorEditor;


    public ControllerMouseInput(OrthographicCamera camera, Vector2Integer cameralocation){
        this(50, camera, cameralocation);
    }


    public ControllerMouseInput(Number hexinitialSize, OrthographicCamera camera, Vector2Integer cameralocation) {
        camerazoom = camera.zoom;
        this.cameralocation = cameralocation;
        hexPixelSize = hexinitialSize.intValue();
    }

    public void addControllerActorMovement(ModelActorEditor modelActorEditor){
        this.modelActorEditor = modelActorEditor;
    }


    public void listener() {
        mouseHooverCoords.set(Gdx.input.getX(), (Gdx.graphics.getHeight() - Gdx.input.getY()));
        mouseHooverCoords.add(cameralocation);
        tileHooverCoords.set(mouseHooverCoords.divide(hexPixelSize));
        modelActorEditor.updateSelectedType();
    }


    public boolean mouseClick(int keycode) {

        if (keycode == Input.Buttons.LEFT){
                lmbClicked = true;
                lmbClickedVector.set(mouseHooverCoords);
                if(lmbClickedVectorStack.size()<2)
                    lmbClickedVectorStack.push(new Vector2Integer(lmbClickedVector));
                else
                if (lmbClickedVectorStack.size()==2) {
                    lmbClickedVectorStack.remove(0);
                    lmbClickedVectorStack.push(new Vector2Integer(lmbClickedVector));
                }

            if(lmbHexSelectedStack.size()<2)
                lmbHexSelectedStack.push(new Vector2Integer(tileHooverCoords));
            else
            if (lmbHexSelectedStack.size()==2) {
                lmbHexSelectedStack.remove(0);
                lmbHexSelectedStack.push(new Vector2Integer(tileHooverCoords));
            }

                hexSelection();
                return true;
        }

        if (keycode == Input.Buttons.RIGHT) {
                rmbClickedVector.set(mouseHooverCoords);
                return true;
        }
        return false;
    }

    //previously made sure that keycode is Buttons.LEFT
    public void hexSelection() {
        if(!lmbHexSelected){
            lmbHexSelected = true;
            lmbHexSelectedStroke = true;
            lmbHexSelectedVector.set(tileHooverCoords);

        }
        else{
            lmbHexSelected = false;
            lmbHexSelectedVector.reset();
        }
    }

    public Vector2Integer getMouseHoover() {
        return mouseHooverCoords;
    }

    public Vector2Integer getTileHoover(){
        return tileHooverCoords;
    }

    public Vector2Integer getLmbSelectedHexCoords(){
        return lmbHexSelectedVector;
    }

    public boolean getLmbSelectedHexBoolean(){
        return lmbHexSelected;
    }


}

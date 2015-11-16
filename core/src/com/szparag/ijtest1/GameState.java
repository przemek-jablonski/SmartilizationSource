package com.szparag.ijtest1;

/**
 * Created by Szparagowy Krul 3000 on 07/06/2015.
 */
public class GameState {

    ActualGameState actualState;

    public enum ActualGameState {
        GAME, GAMESINGLE, GAMEMULTI, MAINMENU, LOADING
    }

    private boolean stateGameCreated = false;
    private boolean stateMultiCreated = false;
    private boolean stateMenuCreated = false;

    public GameState(){
        /*

         */
    }

    public ActualGameState getActualState() {
        return actualState;
    }

    public void setActualState(ActualGameState actualState) {
        this.actualState = actualState;
    }

    public boolean isStateGameCreated() {
        return stateGameCreated;
    }

    public void setStateGameCreated(boolean stateGameCreated) {
        this.stateGameCreated = stateGameCreated;
    }

    public boolean isStateMultiCreated() {
        return stateMultiCreated;
    }

    public void setStateMultiCreated(boolean stateMultiCreated) {
        this.stateMultiCreated = stateMultiCreated;
    }

    public boolean isStateMenuCreated() {
        return stateMenuCreated;
    }

    public void setStateMenuCreated(boolean stateMenuCreated) {
        this.stateMenuCreated = stateMenuCreated;
    }
}

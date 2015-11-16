package com.szparag.ijtest1;

import java.util.LinkedList;
import java.util.List;

class ModelTurns {

    int turnNumber;
    int globalturnNumber;
    private PlayerENUM  actualPlayer;
    private Player      actualPlayerRef;
    Hex[][] hexmap;
    private LinkedList<Player> listPlayers = new LinkedList<>();
    private Player plone;
    private Player pltwo;
    private boolean endgame = false;
    private int turncount;
    private boolean ishexmapchanged = false;

    private boolean multiplayerinstance = false;
    private CivClient civClient;

    public void instantiateMultiplayer(CivClient civClient) {
        multiplayerinstance = true;
        this.civClient = civClient;
    }


    public void updatemap(Hex[][] hexmap){
        this.hexmap = hexmap;
    }

    public void initplayerlist(){
        plone = new Player(PlayerENUM.playerONE, hexmap);
        pltwo = new Player(PlayerENUM.playerTWO, hexmap);
        listPlayers.add(plone);
        listPlayers.add(pltwo);
    }

    public ModelTurns(int turncount) {
        turnNumber = 1;
        globalturnNumber = 1;
        this.turncount = turncount;
    }

    public void addHexmap(Hex[][] hexmap){
        this.hexmap = hexmap;
    }

    public void start()
    {
        listPlayers.get(0).setActivity(true);
        listPlayers.get(1).setActivity(false);
        actualPlayerRef = listPlayers.get(0);
        actualPlayer = actualPlayerRef.getPlayerENUM();
      //  turnUpdate();
      //  globalTurnForward();
    }

    public void resetGlobalStats() {
        for (int a=0 ; a < hexmap.length; ++a){
            for (int b=0 ; b < hexmap[0].length; ++b){
                for(int i=1; i < hexmap[a][b].actorList.size(); ++i) {
                    if (hexmap[a][b].actorList.get(i) instanceof ActorUnit)
                        unitStatsUpdate((ActorUnit)hexmap[a][b].actorList.get(i));
                    else
                        if(hexmap[a][b].actorList.get(i) instanceof ActorBuilding)
                            buildingStatsUpdate((ActorBuilding)hexmap[a][b].actorList.get(i));
                }
            }
        }
    }

    public void unitStatsUpdate(ActorUnit actorUnit){
        actorUnit.hitPoints += actorUnit.hitPointsRegeneration;
        float fl = actorUnit.movementPointsTotal;
        actorUnit.movementPoints = fl;
    }

    public void buildingStatsUpdate(ActorBuilding actorBuilding){
        actorBuilding.setHitPoints(actorBuilding.getHitPoints() + actorBuilding.getRegenerationModifier());

    }

    public void turnUpdate() {
        ++turnNumber;
        System.out.println("modelturns/before turnupdate(): ");
        if(multiplayerinstance) {
            civClient.setTurnchange(true);
            System.out.println("modelturns/turnupdate(): ");
        }
        if(actualPlayer == PlayerENUM.playerONE)
            actualPlayer = PlayerENUM.playerTWO;
        else{
            actualPlayer = PlayerENUM.playerONE;
            globalTurnForward();
        }
    }

    private void updateActorsStats(){
        for (int x=0 ; x < hexmap.length; ++x){
            for (int y=0 ; y < hexmap[0].length; ++y){
                for (int i=0; i < hexmap[x][y].actorList.size(); ++i)
                    if(hexmap[x][y].actorList.get(i) instanceof ActorUnit)
                        ((ActorUnit) hexmap[x][y].actorList.get(i)).updateOnTurnEnd();
                else if(hexmap[x][y].actorList.get(i) instanceof ActorBuilding)
                        ((ActorBuilding) hexmap[x][y].actorList.get(i)).updateOnTurnEnd();
          }
      }
    }


    private void globalTurnForward()
    {
        globalturnNumber++;
        updateActorsStats();
        if(globalturnNumber > turncount)
            endgame = true;
    }

    public boolean isIshexmapchanged() {
        return ishexmapchanged;
    }

    public void setIshexmapchanged(boolean ishexmapchanged) {
        this.ishexmapchanged = ishexmapchanged;
    }

    private void turnForward(){
        ++turnNumber;
    }

    public PlayerENUM getActivePlayer()
    {
        return this.actualPlayer;
    }



    public Player getPlone() {
        return plone;
    }

    public void setPlone(Player plone) {
        this.plone = plone;
    }

    public LinkedList<Player> getListPlayers() {
        return listPlayers;
    }

    public void setListPlayers(LinkedList<Player> listPlayers) {
        this.listPlayers = listPlayers;
    }

    public Player getPltwo() {
        return pltwo;
    }

    public void setPltwo(Player pltwo) {
        this.pltwo = pltwo;
    }

    public Player getActualPlayerRef() {
        return actualPlayerRef;
    }

    public void setActualPlayerRef(Player actualPlayerRef) {
        this.actualPlayerRef = actualPlayerRef;
    }

    public PlayerENUM getActualPlayer() {
        return actualPlayer;
    }

    public void setActualPlayer(PlayerENUM actualPlayer) {
        this.actualPlayer = actualPlayer;
    }
}

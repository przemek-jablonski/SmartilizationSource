package com.szparag.ijtest1;

import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

/**
 * Created by Szparagowy Krul 3000 on 02/06/2015.
 */
public class Player {

    private boolean activity;
    private PlayerENUM playerENUM;

    private int     totalGold;
    private int     totalSupplies;
    private int     incomeGold;
    private int     incomeSupplies;
    private int     countBuildings;
    private int     countUnits;
    private Hex[][] hexmap;

 //   private Stack<ActorUnit> stackUnit = new Stack<ActorUnit>();
//    private Stack<ActorBuilding> stackBuilding = new Stack<ActorBuilding>();

    private List<ActorUnit> listUnits = new LinkedList<>();
    private List<ActorBuilding> listBuildings = new LinkedList<>();


    public Player(PlayerENUM playerENUM, Hex[][] hexmap){
        activity = false;
        this.playerENUM = playerENUM;
        this.hexmap = hexmap;
        totalGold = 1000;
        totalSupplies = 100;
        scanMap();
        updateResources();
    }

    public void scanMap(){
        listUnits.clear();
        listBuildings.clear();
        for (int x = 0; x < hexmap.length; ++x) {
            for (int y = 0; y < hexmap[0].length; ++y) {
                for (int level = 0; level < hexmap[x][y].actorList.size(); ++level) {
                        if(hexmap[x][y].actorList.get(level).playerenum == playerENUM)
                        if (hexmap[x][y].actorList.get(level) instanceof ActorBuilding)
                            listBuildings.add((ActorBuilding)hexmap[x][y].actorList.get(level));
                            else if (hexmap[x][y].actorList.get(level) instanceof ActorUnit)
                                listUnits.add((ActorUnit)hexmap[x][y].actorList.get(level));
                }
            }
        }
        countBuildings = listBuildings.size()-1;
        countUnits = listUnits.size()-1;
    }

    public void updateResources(){
        updateIncomes();
        totalGold += incomeGold;
        totalSupplies += incomeSupplies;
    }

    public void updateIncomes(){
        for (ActorUnit unit : listUnits){
            incomeGold -= unit.getMaintenanceGold();
            incomeSupplies -= unit.getMaintenanceSupplies();
        }
        for (ActorBuilding building : listBuildings){
            incomeGold -= building.getCostMaintenanceGold();
            incomeGold += building.getIncomeGold();
            incomeSupplies += building.getIncomeSupplies();
        }
    }

    public int getTotalGold() {
        return totalGold;
    }

    public int getTotalSupplies() {
        return totalSupplies;
    }

    public int getIncomeGold() {
        return incomeGold;
    }

    public int getIncomeSupplies() {
        return incomeSupplies;
    }

    public int getCountBuildings() {
        return countBuildings;
    }

    public int getCountUnits() {
        return countUnits;
    }

    public PlayerENUM getPlayerENUM() {
        return playerENUM;
    }

    public boolean isActivity() {
        return activity;
    }

    public void setActivity(boolean activity) {
        this.activity = activity;
    }
}

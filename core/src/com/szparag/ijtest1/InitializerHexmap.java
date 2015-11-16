package com.szparag.ijtest1;

import javax.swing.text.View;
import java.util.LinkedList;
import java.util.Random;
import java.util.Stack;

public class InitializerHexmap {

    Tile[][] Tile1lands;
    Tile[] Tile2buildings;
    Tile[] Tile3units;
    Hex[][] hexmap;
    Vector2Integer hexmapdimensions;
    Random rand;
    int playernumber;
    private int terrainmod = 1;

    public void updatemap(Hex[][] hexmap){
        this.hexmap = hexmap;
    }

    private LinkedList<Vector2Integer> initialDiscoveredArray = new LinkedList<Vector2Integer>();

    public InitializerHexmap(Vector2Integer hexmapdimensions) {
        this(hexmapdimensions, 4);
        this.hexmapdimensions = hexmapdimensions;
        rand = new Random();
    }

    public InitializerHexmap(Vector2Integer hexmapdimensions, int playerNumber) {
        this.hexmapdimensions = hexmapdimensions;
        rand = new Random();
        playernumber = playerNumber;
    }

    public void addViewRender(ViewRender viewRender){
        System.out.println("inithexmap discoveryarray: " + initialDiscoveredArray.size());
        for (int size =0 ; size < initialDiscoveredArray.size() ; ++size)
            initialDiscoveredArray.get(size).printerSystem();
        viewRender.initDiscoveredArray(initialDiscoveredArray);
    }

    public Hex[][] init() {
        TileAssign();
        GenerateLandMap();
        SpawnActors();
        return hexmap;
    }

    public Hex[][] newinitsrv(int playernumber){
        TileAssign();
        GenerateLandMap();
        newSpawnActors(playernumber);
        return hexmap;
    }
    public Hex[][] newinit(int playernumber){
        TileAssign();
        GenerateLandMap();
        newSpawnActors(playernumber);
        return hexmap;
    }
    public Hex[][] newinit(int playernumber, int terrainmod){
        this.terrainmod = terrainmod;
        TileAssign();
        GenerateLandMap();
        newSpawnActors(playernumber);
        return hexmap;
    }

    public Hex[][] testinit(){
        TileAssign();
        GenerateLandMap();
        testSpawnActors();
        return hexmap;
    }

    //constructing hexes in hexmap[][]
    //according to sent size in Vector2Integer
    private void TileAssign() {
        Tile1lands = new Tile[6][5];
        Tile1lands[0][0] = new Tile(0, 128 * 0, 128, 128, Actor.ActorType.LAND, ActorLand.ActorLandType.GRASS, 0);
        Tile1lands[0][1] = new Tile(0, 128 * 1, 128, 128, Actor.ActorType.LAND, ActorLand.ActorLandType.GRASS, 1);
        Tile1lands[0][2] = new Tile(0, 128 * 2, 128, 128, Actor.ActorType.LAND, ActorLand.ActorLandType.GRASS, 2);
        Tile1lands[0][3] = new Tile(0, 128 * 3, 128, 128, Actor.ActorType.LAND, ActorLand.ActorLandType.GRASS, 3);
        Tile1lands[0][4] = new Tile(0, 128 * 4, 128, 128, Actor.ActorType.LAND, ActorLand.ActorLandType.GRASS, 4);
        Tile1lands[1][0] = new Tile(128 * 1, 128 * 0, 128, 128, Actor.ActorType.LAND, ActorLand.ActorLandType.PRERISH, 0);
        Tile1lands[1][1] = new Tile(128 * 1, 128 * 1, 128, 128, Actor.ActorType.LAND, ActorLand.ActorLandType.PRERISH, 1);
        Tile1lands[1][2] = new Tile(128 * 1, 128 * 2, 128, 128, Actor.ActorType.LAND, ActorLand.ActorLandType.PRERISH, 2);
        Tile1lands[1][3] = new Tile(128 * 1, 128 * 3, 128, 128, Actor.ActorType.LAND, ActorLand.ActorLandType.PRERISH, 3);
        Tile1lands[1][4] = new Tile(128 * 1, 128 * 4, 128, 128, Actor.ActorType.LAND, ActorLand.ActorLandType.PRERISH, 4);
        Tile1lands[2][0] = new Tile(128 * 2, 128 * 0, 128, 128, Actor.ActorType.LAND, ActorLand.ActorLandType.ROCKY, 0);
        Tile1lands[2][1] = new Tile(128 * 2, 128 * 1, 128, 128, Actor.ActorType.LAND, ActorLand.ActorLandType.ROCKY, 1);
        Tile1lands[2][2] = new Tile(128 * 2, 128 * 2, 128, 128, Actor.ActorType.LAND, ActorLand.ActorLandType.ROCKY, 2);
        Tile1lands[2][3] = new Tile(128 * 2, 128 * 3, 128, 128, Actor.ActorType.LAND, ActorLand.ActorLandType.ROCKY, 3);
        Tile1lands[2][4] = new Tile(128 * 2, 128 * 4, 128, 128, Actor.ActorType.LAND, ActorLand.ActorLandType.ROCKY, 4);
        Tile1lands[3][0] = new Tile(128 * 3, 128 * 0, 128, 128, Actor.ActorType.LAND, ActorLand.ActorLandType.DESERT, 0);
        Tile1lands[3][1] = new Tile(128 * 3, 128 * 1, 128, 128, Actor.ActorType.LAND, ActorLand.ActorLandType.DESERT, 1);
        Tile1lands[3][2] = new Tile(128 * 3, 128 * 2, 128, 128, Actor.ActorType.LAND, ActorLand.ActorLandType.DESERT, 2);
        Tile1lands[3][3] = new Tile(128 * 3, 128 * 3, 128, 128, Actor.ActorType.LAND, ActorLand.ActorLandType.DESERT, 3);
        Tile1lands[3][4] = new Tile(128 * 3, 128 * 4, 128, 128, Actor.ActorType.LAND, ActorLand.ActorLandType.DESERT, 4);
        Tile1lands[4][0] = new Tile(128 * 4, 128 * 0, 128, 128, Actor.ActorType.LAND, ActorLand.ActorLandType.MOUNTAIN, 0);
        Tile1lands[4][1] = new Tile(128 * 4, 128 * 1, 128, 128, Actor.ActorType.LAND, ActorLand.ActorLandType.MOUNTAIN, 1);
        Tile1lands[4][2] = new Tile(128 * 4, 128 * 2, 128, 128, Actor.ActorType.LAND, ActorLand.ActorLandType.MOUNTAIN, 2);
        Tile1lands[4][3] = new Tile(128 * 4, 128 * 3, 128, 128, Actor.ActorType.LAND, ActorLand.ActorLandType.MOUNTAIN, 3);
        Tile1lands[4][4] = new Tile(128 * 4, 128 * 4, 128, 128, Actor.ActorType.LAND, ActorLand.ActorLandType.MOUNTAIN, 4);
        Tile1lands[5][0] = new Tile(128 * 5, 128 * 0, 128, 128, Actor.ActorType.LAND, ActorLand.ActorLandType.WATER, 0);
        Tile1lands[5][1] = new Tile(128 * 5, 128 * 1, 128, 128, Actor.ActorType.LAND, ActorLand.ActorLandType.WATER, 1);
        Tile1lands[5][2] = new Tile(128 * 5, 128 * 2, 128, 128, Actor.ActorType.LAND, ActorLand.ActorLandType.WATER, 2);
        Tile1lands[5][3] = new Tile(128 * 5, 128 * 3, 128, 128, Actor.ActorType.LAND, ActorLand.ActorLandType.WATER, 3);
        Tile1lands[5][4] = new Tile(128 * 5, 128 * 4, 128, 128, Actor.ActorType.LAND, ActorLand.ActorLandType.WATER, 4);
        Tile2buildings = new Tile[6];
        Tile2buildings[0] = new Tile(Actor.ActorType.BUILDING, ActorBuilding.ActorBuildingType.CAPITAL);
        Tile2buildings[1] = new Tile(Actor.ActorType.BUILDING, ActorBuilding.ActorBuildingType.CASTLE);
        Tile2buildings[2] = new Tile(Actor.ActorType.BUILDING, ActorBuilding.ActorBuildingType.FORTRESS);
        Tile2buildings[3] = new Tile(Actor.ActorType.BUILDING, ActorBuilding.ActorBuildingType.WATERMILL);
        Tile2buildings[4] = new Tile(Actor.ActorType.BUILDING, ActorBuilding.ActorBuildingType.STABLE);
        Tile2buildings[5] = new Tile(Actor.ActorType.BUILDING, ActorBuilding.ActorBuildingType.UNITDISPENSER);
        Tile3units = new Tile[5];
        Tile3units[0] = new Tile(Actor.ActorType.UNIT, ActorUnit.ActorUnitType.WARRIOR);
        Tile3units[1] = new Tile(Actor.ActorType.UNIT, ActorUnit.ActorUnitType.ARCHER);
        Tile3units[2] = new Tile(Actor.ActorType.UNIT, ActorUnit.ActorUnitType.HORSEMEN);
        Tile3units[3] = new Tile(Actor.ActorType.UNIT, ActorUnit.ActorUnitType.ENGINEER);
        Tile3units[4] = new Tile(Actor.ActorType.UNIT, ActorUnit.ActorUnitType.SCOUT);
    }

    //spawning lands onto hexmap[][]
    private void GenerateLandMap() {
        //ver3:
        hexmap = new Hex[hexmapdimensions.getx()][hexmapdimensions.gety()];

        //init lands:
        for (int i = 0; i < hexmap.length; ++i) {
            for (int j = 0; j < hexmap[0].length; ++j) {
                hexmap[i][j] = new Hex(new Vector2Integer(i * 50, j * 50));
                hexmap[i][j].assignLand(Tile1lands[GaussianRandom(rand, 6)][rand.nextInt(5)]);
            }
        }
    }

    //spawning actors onto hexmap[][]
    private void SpawnActors() {

        Stack<Vector2Integer> occupiedTilesStack = new Stack<Vector2Integer>();
        Vector2Integer dimmax = new Vector2Integer(hexmapdimensions.x()/playernumber, hexmapdimensions.y()/playernumber);
        Vector2Integer dimmin = new Vector2Integer(2, 2);
        if(playernumber==2){
            for (int i = 0; i < hexmap.length / 2; ++i) {
                for (int j = 0; j < hexmap[0].length / 2; ++j) {
                    if (j % 4 == 1 && i % 5 == 1)
                        hexmap[i][j].assignBuilding(Tile2buildings[rand.nextInt(6)], PlayerENUM.playerONE);
                    if (j % 5 == 1 && i % 3 == 1)
                        hexmap[i][j].assignUnit(Tile3units[rand.nextInt(5)], PlayerENUM.playerONE);
                }
            }

            //playerTWO
            for (int i = hexmap.length / 2; i < hexmap.length; ++i) {
                for (int j = hexmap[0].length / 2; j < hexmap[0].length; ++j) {
                    if (j % 4 == 1 && i % 2 == 1)
                        hexmap[i][j].assignBuilding(Tile2buildings[rand.nextInt(6)], PlayerENUM.playerTWO);
                    if (j % 4 == 1 && i % 3 == 1)
                        hexmap[i][j].assignUnit(Tile3units[rand.nextInt(5)], PlayerENUM.playerTWO);
                }
            }

            hexmap[3][3].assignBuilding(Tile2buildings[1], PlayerENUM.playerTWO);
            hexmap[3][3].assignUnit(Tile3units[3], PlayerENUM.playerTWO);
            hexmap[3][3].getLastUnitActor().hitPoints -=35;

            hexmap[12][11].assignUnit(Tile3units[4], PlayerENUM.playerTWO);
/*
            hexmap[rand.nextInt(hexmapdimensions.x())][rand.nextInt(hexmapdimensions.y())].assignBuilding(Tile2buildings[rand.nextInt(2)], PlayerENUM.playerONE);
            hexmap[rand.nextInt(hexmapdimensions.x()-2)][rand.nextInt(hexmapdimensions.y())].assignBuilding(Tile2buildings[rand.nextInt(2)], PlayerENUM.playerONE);
            hexmap[rand.nextInt(hexmapdimensions.x())][rand.nextInt(hexmapdimensions.y())].assignBuilding(Tile2buildings[rand.nextInt(2)], PlayerENUM.playerONE);
            hexmap[rand.nextInt(hexmapdimensions.x())][rand.nextInt(hexmapdimensions.y())].assignBuilding(Tile2buildings[rand.nextInt(2)], PlayerENUM.playerTWO);
            hexmap[rand.nextInt(hexmapdimensions.x())][rand.nextInt(hexmapdimensions.y())].assignBuilding(Tile2buildings[rand.nextInt(2)], PlayerENUM.playerTWO);
            hexmap[rand.nextInt(hexmapdimensions.x())][rand.nextInt(hexmapdimensions.y())].assignBuilding(Tile2buildings[rand.nextInt(2)], PlayerENUM.playerTWO);
*/
            /*
            //playerONE:
            Vector2Integer capitalLocation = new Vector2Integer(rand.nextInt(dimmax.substract(dimmin).getx()), rand.nextInt(dimmax.substract(dimmin).gety()));
            hexmap[capitalLocation.x()][capitalLocation.y()].assignBuilding(Tile2buildings[0], PlayerENUM.playerONE);


         //   dimmin.set(dimmax);
        //    dimmax.set(hexmapdimensions.x(), hexmapdimensions.y());
            //playerTWO:
            capitalLocation.set(rand.nextInt(dimmax.substract(dimmin).getx()), rand.nextInt(dimmax.substract(dimmin).gety()));
            hexmap[capitalLocation.x()][capitalLocation.y()].assignBuilding(Tile2buildings[0], PlayerENUM.playerTWO);
            */
        } else
        if (playernumber==4) {

        /*
        //////////////////////////////
        int dimmax = hexmapdimensions.x()/playernumber;
        int dimmin = 1;

        for (int i=1; i <= playernumber ; ++i) {

            PlayerENUM playerENUM = PlayerENUM.playerONE;
            switch(playernumber){
                case 1:
                    playerENUM = PlayerENUM.playerONE;
                    break;
                case 2:
                    playerENUM = PlayerENUM.playerTWO;
                    break;
                case 3:
                    playerENUM = PlayerENUM.playerTHREE;
                    break;
                case 4:
                    playerENUM = PlayerENUM.playerFOUR;
                    break;
                default:
                    System.out.println("COS TUTAJ JEST ZJEBANO MEGA");
                    break;
            }
            int range = dimmax*1 -dimmin*1 +1;
            int buildingCount = rand.nextInt(4);
            int unitCount = rand.nextInt(7);

            Vector2Integer capitalLocation = new Vector2Integer(rand.nextInt(hexmapdimensions.x()), rand.nextInt(hexmapdimensions.y()));
            hexmap[capitalLocation.x()][capitalLocation.y()].assignBuilding(Tile2buildings[1], playerENUM);

            Stack<Vector2Integer> spawned = new Stack<Vector2Integer>();
            for(int b=1; b < buildingCount; ++b){
                int randx = rand.nextInt(20) - rand.nextInt(15);
                int randy = rand.nextInt(20) - rand.nextInt(20);

                if (randx < hexmapdimensions.x() && randx > 0
                        && randy < hexmapdimensions.y() && randy > 0) {
                    if(spawned.contains(new Vector2Integer(randx, randy)))
                            --b;
                    else{
                        spawned.add(new Vector2Integer(randx, randy));
                        hexmap[capitalLocation.x() + spawned.peek().x()][capitalLocation.y() + spawned.peek().y()].assignBuilding(Tile2buildings[rand.nextInt(2)], playerENUM);
                    }
                }
            }

            dimmin = dimmax;
            dimmax = dimmax + dimmax*playernumber;

        }
        */


            //init player1 actionactors:
            for (int i = 0; i < hexmap.length / 2; ++i) {
                for (int j = 0; j < hexmap[0].length / 2; ++j) {

                    //player1 units:
                    if (j % 5 == 1 && i % 3 == 1) {
                        hexmap[i][j].assignUnit(Tile3units[0], PlayerENUM.playerONE);
                    }

                    //player1 buildings:
                    if (j % 4 == 1 && i % 5 == 1) {
                        hexmap[i][j].assignBuilding(Tile2buildings[rand.nextInt(2)], PlayerENUM.playerONE);
                    }
                }
            }

            //playerTWO
            for (int i = hexmap.length / 2; i < hexmap.length; ++i) {
                for (int j = hexmap[0].length / 2; j < hexmap[0].length; ++j) {
                    if (j % 4 == 1 && i % 2 == 1) {
                        hexmap[i][j].assignBuilding(Tile2buildings[rand.nextInt(2)], PlayerENUM.playerTWO);

                    }
                    if (j % 4 == 1 && i % 3 == 1) {
                        hexmap[i][j].assignUnit(Tile3units[1], PlayerENUM.playerTWO);

                    }
                }
            }

            //playerTHREE
            for (int i = 0; i < hexmap.length / 2; ++i) {
                for (int j = hexmap[0].length / 2; j < hexmap[0].length; ++j) {
                    if (j % 2 == 1 && i % 2 == 1) {
                        hexmap[i][j].assignBuilding(Tile2buildings[rand.nextInt(2)], PlayerENUM.playerTHREE);

                    }
                    if (j % 4 == 1 && i % 3 == 1) {
                        hexmap[i][j].assignUnit(Tile3units[2], PlayerENUM.playerTHREE);
                    }
                }
            }

            //playerFOUR
            for (int i = hexmap.length / 2; i < hexmap.length; ++i) {
                for (int j = 0; j < hexmap[0].length / 2; ++j) {
                    if (j % 3 == 1 && i % 2 == 1) {
                        hexmap[i][j].assignBuilding(Tile2buildings[rand.nextInt(2)], PlayerENUM.playerFOUR);

                    }
                    if (j % 1 == 1 && i % 3 == 1) {
                        hexmap[i][j].assignUnit(Tile3units[3], PlayerENUM.playerFOUR);

                    }
                }
            }

        }
        }

    //temporarily only 2 player mode
    private void newSpawnActors(int playernumber) {
        LinkedList<Vector2Integer> usedHexes = new LinkedList<Vector2Integer>();
        int regionx = (int) Math.ceil((float) hexmapdimensions.x() * 0.45);
        int regiony = (int) Math.ceil((float) hexmapdimensions.y() * 0.6);
        PlayerENUM tempplayer;
            Vector2Integer capitalLocation = new Vector2Integer(rand.nextInt(regionx - 4) + 5, rand.nextInt(regiony - 4) + 5);
            usedHexes.add(capitalLocation);
            initialDiscoveredArray.add(capitalLocation);
            hexmap[capitalLocation.x()][capitalLocation.y()].assignBuilding(Tile2buildings[0], PlayerENUM.playerONE);
            initialDiscoveredArray.add(new Vector2Integer(capitalLocation.x(), capitalLocation.y()));


            Vector2Integer location = new Vector2Integer(capitalLocation.x() - 1, capitalLocation.y() - 1);
            hexmap[location.x()][location.y()].assignUnit(Tile3units[0], PlayerENUM.playerONE);
            initialDiscoveredArray.add(location);

            location = new Vector2Integer(capitalLocation.x() - 1, capitalLocation.y() + 1);
            hexmap[location.x()][location.y()].assignUnit(Tile3units[4], PlayerENUM.playerONE);
            initialDiscoveredArray.add(location);

            location = new Vector2Integer(capitalLocation.x(), capitalLocation.y() + 2);
            hexmap[location.x()][location.y()].assignUnit(Tile3units[2], PlayerENUM.playerONE);
            initialDiscoveredArray.add(location);

            location = new Vector2Integer(capitalLocation.x() + 1, capitalLocation.y());
            hexmap[location.x()][location.y()].assignUnit(Tile3units[3], PlayerENUM.playerONE);
            initialDiscoveredArray.add(location);


        capitalLocation = new Vector2Integer(rand.nextInt(hexmapdimensions.x() - regionx) + regionx- 5, rand.nextInt(hexmapdimensions.y() - regiony) + regiony-5);
        usedHexes.add(capitalLocation);
        initialDiscoveredArray.add(capitalLocation);
        hexmap[capitalLocation.x()][capitalLocation.y()].assignBuilding(Tile2buildings[0], PlayerENUM.playerTWO);
        initialDiscoveredArray.add(new Vector2Integer(capitalLocation.x(), capitalLocation.y()));

        location = new Vector2Integer(capitalLocation.x() - 1, capitalLocation.y() - 1);
        hexmap[location.x()][location.y()].assignUnit(Tile3units[0], PlayerENUM.playerTWO);
        initialDiscoveredArray.add(location);

        location = new Vector2Integer(capitalLocation.x() - 1, capitalLocation.y() + 1);
        hexmap[location.x()][location.y()].assignUnit(Tile3units[4], PlayerENUM.playerTWO);
        initialDiscoveredArray.add(location);

        location = new Vector2Integer(capitalLocation.x(), capitalLocation.y() + 2);
        hexmap[location.x()][location.y()].assignUnit(Tile3units[2], PlayerENUM.playerTWO);
        initialDiscoveredArray.add(location);

        location = new Vector2Integer(capitalLocation.x() + 1, capitalLocation.y());
        hexmap[location.x()][location.y()].assignUnit(Tile3units[3], PlayerENUM.playerTWO);
        initialDiscoveredArray.add(location);

    }

    private void testSpawnActors() {
        for (int x = 0 ; x < hexmapdimensions.x() ; ++x){
            for (int y = 0 ; y < hexmapdimensions.y() ; ++y){
                if (x % 6 == 0 && y % 5 == 0) {
                    hexmap[x][y].assignBuilding(Tile2buildings[rand.nextInt(6)], PlayerENUM.playerONE);
                    initialDiscoveredArray.add(new Vector2Integer(x, y));
                }
                if (x % 4 == 0 && y % 4 == 0) {
                    hexmap[x][y].assignUnit(Tile3units[rand.nextInt(5)], PlayerENUM.playerTWO);
                    initialDiscoveredArray.add(new Vector2Integer(x, y));
                }
            }
        }
    }

    public int GaussianRandom(Random random, int max) {
        double gauss = random.nextGaussian() + terrainmod;
        if (gauss < 0)
            gauss = random.nextInt(max);
        return (int)gauss%6;
    }

}

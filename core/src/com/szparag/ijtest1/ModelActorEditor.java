package com.szparag.ijtest1;


import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ModelActorEditor {

    Hex[][] hexmap;
    Vector2Integer hexmapDimensions;
    ControllerMouseInput controllerMouseInput;
    ModelTurns modelTurns;
    ViewRender viewRender;
    Combat combat;
    Builder builder;

    //mark2:
    //type of top selected unit
    Selection selection;
    Vector2Integer clickedTile;
    Vector2Integer previousclickedTile;
    List<Vector2Integer> movementPossibilities = new LinkedList<Vector2Integer>();
    List<Vector2Integer> combatPossibilities = new ArrayList<Vector2Integer>();

    boolean init = true;
    boolean isActionMoveSet;
    boolean isActionCombatSet;
    private boolean hexmapChanged = false;

    public void updatemap(Hex[][] hexmap){
        this.hexmap = hexmap;
    }

    public ModelActorEditor(Hex[][] hexmap, Vector2Integer hexmapdimensions, ControllerMouseInput controllerMouseInput, ModelTurns modelTurns) {
        hexmapDimensions = hexmapdimensions;
        this.hexmap = hexmap;
        this.controllerMouseInput = controllerMouseInput;
        this.modelTurns = modelTurns;
     //   isActionMoveSet = actionMoveSet;
        combat = new Combat(this.hexmap);

        clickedTile = new Vector2Integer();
        previousclickedTile = new Vector2Integer();

    }

    public void addControllerRender(ViewRender viewRender){
        this.viewRender = viewRender;
    }

    public Selection updateSelectedType() {
        Vector2Integer coords = controllerMouseInput.getLmbSelectedHexCoords();
        boolean bool = controllerMouseInput.lmbHexSelected;
        if (!bool){
            selection = Selection.OFF;
            return selection;
        }
        if(hexmap[coords.x()][coords.y()].actorList.get(hexmap[coords.x()][coords.y()].actorList.size()-1) instanceof ActorLand)
            selection = Selection.LAND;
        else
            if (hexmap[coords.x()][coords.y()].actorList.get(hexmap[coords.x()][coords.y()].actorList.size()-1) instanceof ActorBuilding)
                selection = Selection.BUILDING;
        else
            if (hexmap[coords.x()][coords.y()].actorList.get(hexmap[coords.x()][coords.y()].actorList.size()-1) instanceof ActorUnit
                    && hexmap[coords.x()][coords.y()].actorList.get(hexmap[coords.x()][coords.y()].actorList.size()-2) instanceof ActorBuilding)
                selection = Selection.UNITBUILDING;
        else
                selection = Selection.UNIT;

        return selection;
    }

    public void updateOnFortify(boolean fortify){
        if(hexmap[controllerMouseInput.lmbHexSelectedVector.x()][controllerMouseInput.lmbHexSelectedVector.y()].getLastActor() instanceof ActorUnit)
            if(fortify) {
             //   if(! ((ActorUnit)hexmap[controllerMouseInput.lmbHexSelectedVector.x()][controllerMouseInput.lmbHexSelectedVector.y()]).getL ) {
                if(!((ActorUnit) hexmap[controllerMouseInput.lmbHexSelectedVector.x()][controllerMouseInput.lmbHexSelectedVector.y()].getLastActor()).isFortified) {
                    hexmap[controllerMouseInput.lmbHexSelectedVector.x()][controllerMouseInput.lmbHexSelectedVector.y()].getLastUnitActor().defendModifier += 0.5f;
                    hexmap[controllerMouseInput.lmbHexSelectedVector.x()][controllerMouseInput.lmbHexSelectedVector.y()].getLastUnitActor().setRangedDefendModifier(hexmap[controllerMouseInput.lmbHexSelectedVector.x()][controllerMouseInput.lmbHexSelectedVector.y()].getLastUnitActor().getRangedDefendModifier() + 0.75f);
                    hexmap[controllerMouseInput.lmbHexSelectedVector.x()][controllerMouseInput.lmbHexSelectedVector.y()].getLastUnitActor().decreaseMovePtsTotal();
                    hexmap[controllerMouseInput.lmbHexSelectedVector.x()][controllerMouseInput.lmbHexSelectedVector.y()].getLastUnitActor().isFortified = true;
                }
            }
    }

    public boolean actionCombat_trigger(boolean value){
        this.isActionCombatSet = value;
      //controllerRender.costamcostam
    //    if(value)
   //         actionrangedCombat_generateList(controllerMouseInput.lmbHexSelectedVector.x(), controllerMouseInput.lmbHexSelectedVector.y());
        return isActionMoveSet;
    }

    private List<Vector2Integer> actionCombat_generateList(int x, int y){
        combatPossibilities.clear();
        combatPossibilities.add(new Vector2Integer(x+1, y+1));
        combatPossibilities.add(new Vector2Integer(x+1, y  ));
        combatPossibilities.add(new Vector2Integer(x+1, y-1));
        combatPossibilities.add(new Vector2Integer(x  , y-1));
        combatPossibilities.add(new Vector2Integer(x-1, y-1));
        combatPossibilities.add(new Vector2Integer(x-1, y  ));
        combatPossibilities.add(new Vector2Integer(x-1, y+1));
        combatPossibilities.add(new Vector2Integer(x  , y+1));


        System.out.println("combatpossibilities size: " + combatPossibilities.size());

        for(int i=0 ; i < combatPossibilities.size(); ++i)
            System.out.println(combatPossibilities.get(i).printer() + " " + Integer.toString(hexmap[combatPossibilities.get(i).x()][combatPossibilities.get(i).y()].actorList.size()));

/*
        for(int i=0 ; i < combatPossibilities.size(); ++i){
            System.out.println(combatPossibilities.get(i).printer() + " " + Integer.toString(hexmap[combatPossibilities.get(i).x()][combatPossibilities.get(i).y()].actorList.size()));
       //     if (hexmap[combatPossibilities.get(i).x()][combatPossibilities.get(i).y()].getLastActor() instanceof ActorLand ||
                    if( hexmap[combatPossibilities.get(i).x()][combatPossibilities.get(i).y()].actorList.size()==1)
                combatPossibilities.remove(i);
        }
        */

        for(int i=0 ; i < combatPossibilities.size(); ++i){
            System.out.println(combatPossibilities.get(i).printer() + " " + Integer.toString(hexmap[combatPossibilities.get(i).x()][combatPossibilities.get(i).y()].actorList.size()));
            //     if (hexmap[combatPossibilities.get(i).x()][combatPossibilities.get(i).y()].getLastActor() instanceof ActorLand ||
            if( hexmap[combatPossibilities.get(i).x()][combatPossibilities.get(i).y()].actorList.size() <= 2)
                combatPossibilities.remove(i);
        }


        System.out.println("combatpossibilities print");
        for (int i=0 ; i < combatPossibilities.size(); ++i){
            System.out.println("\t" + combatPossibilities.get(i).printer());
        }

        viewRender.combatPossibilitiesArray = combatPossibilities;
        return combatPossibilities;
    }

    private List<Vector2Integer> actionCombat_resetList(){
        combatPossibilities.clear();
        viewRender.combatPossibilitiesArray = combatPossibilities;
        return combatPossibilities;
    }

    public void actionCombat_action(Vector2Integer fromVector, Vector2Integer toVector){
        //fromVector: aktualna pozycja jednostki.
        //toVector: pozycja, na ktora jednostka chce przejsc.
        int moveDistance = toVector.moveSubstraction(fromVector);
        System.out.println("przed combatem");
        //jesli toVector jest w liscie, ktora zawiera dozwolone polozenia jednostki
      //  if(toVector.searchThroughList(combatPossibilities)){
            System.out.println("COMBAT W CHUJ!\n" + fromVector.printer() + toVector.printer());
            //skopiowanie jednostki z pola fromVector do pola toVector.
        //    hexmap[toVector.x()][toVector.y()].actorList.add(
          //          hexmap[fromVector.x()][fromVector.y()].actorList.remove(hexmap[fromVector.x()][fromVector.y()].actorList.size() - 1));
            //odjecie punktow ruchu jednostki
          //  hexmap[toVector.x()][toVector.y()].getLastUnitActor().decreaseMovePts(moveDistance);

            //VER2:
            combat.closequarters(hexmap[fromVector.x()][fromVector.y()].getLastActor(),
                                            hexmap[toVector.x()][toVector.y()].getLastActor());
            hexmapChanged = true;
     //   }

    }

    public void actionMovement_injector(boolean actionMoveSet){
        isActionMoveSet = actionMoveSet;
    }

    public boolean actionMovement_trigger(boolean value){
        //internal boolean indicating actionmove demand
        this.isActionMoveSet = value;
        //sending boolean value to Render controller
        //if it's positive, movement possibilities are drawn on the map
        viewRender.backlightEnabler = value;
        if(value==true)
            actionMovement_generateList(controllerMouseInput.lmbHexSelectedVector.x(), controllerMouseInput.lmbHexSelectedVector.y(), hexmap[controllerMouseInput.lmbHexSelectedVector.x()][controllerMouseInput.lmbHexSelectedVector.y()].actorList.size() - 1);
        return isActionMoveSet;
    }

    private List<Vector2Integer> actionMovement_generateList(int x, int y, int placement){
        int moveRadius = (int) ((ActorUnit) hexmap[x][y].actorList.get(placement)).movementPoints;
        movementPossibilities.clear();
        for (int a = 0;  a <= moveRadius; ++a) {
            for (int b = 0; b <= moveRadius - a; ++b) {
                Vector2Integer possibility;
                possibility = new Vector2Integer(x + b, y + a);
                if(!possibility.searchThroughList(movementPossibilities))
                    movementPossibilities.add(possibility);
                possibility = new Vector2Integer(x + b, y - a);
                if(!possibility.searchThroughList(movementPossibilities))
                    movementPossibilities.add(possibility);
                possibility = new Vector2Integer(x - b, y + a);
                if(!possibility.searchThroughList(movementPossibilities))
                    movementPossibilities.add(possibility);
                possibility = new Vector2Integer(x - b, y - a);
                if(!possibility.searchThroughList(movementPossibilities))
                    movementPossibilities.add(possibility);
            }
        }
        System.out.println("movementpossibilities.size: " + movementPossibilities.size());
        for(int i=0 ; i < movementPossibilities.size(); ++i){
            Vector2Integer vector = movementPossibilities.get(i);
            if(vector.x() != -1 && vector.y() != -1) {
                if (hexmap[vector.x()][vector.y()].getLastActor() instanceof ActorUnit)
                    movementPossibilities.remove(vector);
                if (((ActorLand) hexmap[vector.x()][vector.y()].actorList.get(0)).actorLandType == ActorLand.ActorLandType.WATER
                        || ((ActorLand) hexmap[vector.x()][vector.y()].actorList.get(0)).actorLandType == ActorLand.ActorLandType.MOUNTAIN)
                    movementPossibilities.remove(vector);
            }
        }

        movementPossibilities.remove(new Vector2Integer(x, y));
        System.out.println("movementpossibilities.size: " + movementPossibilities.size());
        viewRender.movementPossibilitiesArray = movementPossibilities;
        return movementPossibilities;
    }

    private List<Vector2Integer> actionMovement_resetList(){
        System.out.println("resetmovementpossibilistmethod()");
        movementPossibilities.clear();
        viewRender.movementPossibilitiesArray = movementPossibilities;
        return movementPossibilities;
    }

    public void actionMovement_prealpha(Vector2Integer fromVector, Vector2Integer toVector) {
        if( ((ActorLand)hexmap[toVector.x()][toVector.y()].actorList.get(0)).actorLandType != ActorLand.ActorLandType.WATER
                || ((ActorLand)hexmap[toVector.x()][toVector.y()].actorList.get(0)).actorLandType != ActorLand.ActorLandType.ROCKY) {
            int moveRadius = (int) ((ActorUnit) hexmap[fromVector.x()][fromVector.y()].getLastUnitActor()).movementPoints;
            int moveDistance = toVector.moveSubstraction(fromVector);
            System.out.println("moveradius: " + moveRadius + " / movedistance: " + moveDistance + " / movementpossib.size: " + movementPossibilities.size());
            if (moveDistance <= moveRadius) {
                hexmap[toVector.x()][toVector.y()].actorList.add(
                        hexmap[fromVector.x()][fromVector.y()].actorList.remove(hexmap[fromVector.x()][fromVector.y()].actorList.size() - 1));
                hexmap[toVector.x()][toVector.y()].getLastUnitActor().decreaseMovePts(moveDistance);
                hexmapChanged = true;
            }
            actionMovement_resetList();
        }
    }

    public boolean actionCombatRanged_trigger(boolean value){
      //  this.isActionCombatRangedSet = value;

      //  if(value)
        return value;
    }

    public void actionCombatRanged_action(Vector2Integer fromVector, Vector2Integer toVector){
        //fromVector: selected unit position.
        //toVector: position of attacked unit
        int moveDistance = toVector.moveSubstraction(fromVector);
        System.out.println("przed ranged combatem");
        if(moveDistance <= hexmap[fromVector.x()][fromVector.y()].getLastUnitActor().movementPoints+1) {
            System.out.println("RANGED COMBAT!\n" + fromVector.printer() + toVector.printer());
            combat.rangedcombat(hexmap[fromVector.x()][fromVector.y()].getLastActor(),
                    hexmap[toVector.x()][toVector.y()].getLastActor());
        }
    }

    public boolean isHexmapChanged() {
        return hexmapChanged;
    }

    public void setHexmapChanged(boolean hexmapChanged) {
        this.hexmapChanged = hexmapChanged;
    }
}

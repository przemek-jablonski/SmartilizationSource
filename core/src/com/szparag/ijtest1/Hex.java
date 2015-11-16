package com.szparag.ijtest1;
import java.io.Serializable;
import java.util.ArrayList;


public class Hex implements Serializable
{
    private static final long serialVersionUID = 1651726640066236383L;
    ArrayList<Actor> actorList;
    Vector2Integer   position;
    boolean          isHighlighted;


    Selection selection;
    //^ do kasacji


    public Hex(Vector2Integer pos)
    {
        actorList = new ArrayList<Actor>();
        actorList.ensureCapacity(3);
        //przy konstruowaniu listy - przekazac position (Vector2Integer)!
        //albo i nie
        //lol
        position = new Vector2Integer(pos);
     //   fxHighlight = new Sprite(new Texture("fxwronghighlight.png"));
        isHighlighted = false;
        selection = Selection.OFF;
    }

    public ActorUnit getLastUnitActor() {
        return (ActorUnit)actorList.get(actorList.size()-1);
    }
    public Actor getLastActor() {
        return actorList.get(actorList.size()-1);
    }


    public String printer(){
        StringBuilder stringbuilder = new StringBuilder();
        stringbuilder.append("position:" + this.position.printer() +
                ", ActorListSize:" + this.actorList.size());
        for (Actor actor : actorList)
        stringbuilder.append(", " + actor.actorType.toString());

        return stringbuilder.toString();
    }


    public Selection getSelection()
    {
        if (actorList.size() == 3)
            selection = Selection.UNITBUILDING;
        else
        {
            if (actorList.size() == 1)
                selection = Selection.LAND;
            else
            {
                
                if (actorList.size() ==2 && actorList.get(1) instanceof ActorBuilding)
                    selection = Selection.BUILDING;
                else if(actorList.size()==2 && actorList.get(1) instanceof ActorUnit)
                    selection = Selection.UNIT;
            }
        }

        return selection;
    }

    public void assignLand(Tile inputtile)
    {
        //OBTAIN enum / tile
        ActorLand actor = new ActorLand(position);
     //   actor.assign(inputtile, alt);
        actor.assign(inputtile);
        actorList.add(actor);
    }

    public void assignBuilding(Tile inputtile, PlayerENUM playerENUM) {
        ActorBuilding actor = new ActorBuilding(position, playerENUM);
        actor.assign(inputtile);
        actorList.add(actor);
    }

    public void assignUnit(Tile inputtile, PlayerENUM playerENUM)
    {
        ActorUnit actor = new ActorUnit(position, playerENUM);
        actor.assign(inputtile);
        actorList.add(actor);
    }

    public void delete()
    {
        actorList.remove(actorList.size()-1);
    }
}
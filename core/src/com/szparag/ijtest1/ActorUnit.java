package com.szparag.ijtest1;

class ActorUnit extends Actor
{
    private static final long serialVersionUID = 1083545683252625910L;

    public int getCostGold() {
        return costGold;
    }

    public void setCostGold(int costGold) {
        this.costGold = costGold;
    }

    public int getCostSupplies() {
        return costSupplies;
    }

    public void setCostSupplies(int costSupplies) {
        this.costSupplies = costSupplies;
    }

    public int getMaintenanceGold() {
        return maintenanceGold;
    }

    public void setMaintenanceGold(int maintenanceGold) {
        this.maintenanceGold = maintenanceGold;
    }

    public int getMaintenanceSupplies() {
        return maintenanceSupplies;
    }

    public void setMaintenanceSupplies(int maintenanceSupplies) {
        this.maintenanceSupplies = maintenanceSupplies;
    }

    public float getRangedDefendModifier() {
        return rangedDefendModifier;
    }

    public void setRangedDefendModifier(float rangedDefendModifier) {
        this.rangedDefendModifier = rangedDefendModifier;
    }

    /* exists in class above (Actor)
        enum ActorType {
            LAND, BUILDING, UNIT;
        }
        */
    enum ActorUnitType {
        WARRIOR, ARCHER, HORSEMEN, ENGINEER, SCOUT, GENERAL
    }

    ActorUnitType actorUnitType;

    //Detailed <Unit> Statistics Info:
    //init on globalturn
    float   movementPoints; //init on globalturn
    float   movementPointsTotal;
//    float  moveCost;
    float   expPoints = 10;
    float   expPointsTotal;
    float   hitPoints; //init on globalturn

    private int     costGold  = 10;
    private int     costSupplies = 1;
    private int     maintenanceGold = 5;
    private int     maintenanceSupplies = 1;


    //init on spawn
    float   hitPointsTotal;
    float   hitPointsRegeneration;
    float   attackModifier = 1;
    float   rangedAttackModifier;
    float   defendModifier = 1;
    private float   rangedDefendModifier = 1;
    boolean attackRanged;
    boolean isBuilder;
    boolean isFortified = false;

    public void updateTurnEnd() {
        movementPoints = movementPointsTotal;

    }

    public float decreaseMovePts(float decrease) {
        movementPoints -= decrease;
        if(movementPoints < 0f)
            movementPoints = 0f;
        return movementPoints;
    }

    public void decreaseMovePtsZero(){
        this.movementPoints = 0;
    }

    public float decreaseMovePtsTotal() {
        this.movementPoints = 0f;
        return movementPoints;
    }

    public void decreaseHitPoints(float hitpointloss){
        this.hitPoints -= hitpointloss;
    }

    public void updateModifiers() {

    }

    public void updateOnTurnEnd(){
        //movepoints total replenishment
        movementPoints = movementPointsTotal;
        //hitpoints replenishment (+regeneration)
        if(hitPoints<hitPointsTotal){
            if((hitPoints + hitPointsRegeneration) > hitPointsTotal)
                hitPoints = hitPointsTotal;
            else
                hitPoints += hitPointsRegeneration;
        }
        //unchecking unit fortification
        if(isFortified) {
            this.defendModifier -= 0.5f;
            this.rangedAttackModifier -= 0.75f;
            isFortified = false;
        }
    }


    public boolean initializeStats() {
        switch(actorUnitType) {
            case SCOUT:
                isBuilder = false;
                movementPointsTotal = 5;
                attackRanged = false;
                rangedAttackModifier = 0;
                attackModifier = 0.35f;
                setRangedDefendModifier(0.4f);
                defendModifier = 0.8f;
                hitPointsRegeneration = 5f;
                hitPointsTotal = 50f;
                break;
            case ENGINEER:
                isBuilder = true;
                movementPointsTotal = 2;
                attackRanged = false;
                rangedAttackModifier = 0;
                attackModifier = 0.15f;
                setRangedDefendModifier(0.5f);
                defendModifier = 1f;
                hitPointsRegeneration = 1f;
                hitPointsTotal = 50f;
                break;
            case WARRIOR:
                isBuilder = false;
                movementPointsTotal = 3;
                attackRanged = false;
                rangedAttackModifier = 0;
                attackModifier = 1.f;
                setRangedDefendModifier(1f);
                defendModifier = 1.25f;
                hitPointsRegeneration = 10f;
                hitPointsTotal = 100f;
                break;
            case ARCHER:
                isBuilder = false;
                movementPointsTotal = 4;
                attackRanged = true;
                rangedAttackModifier = 1.25f;
                attackModifier = 0.65f;
                setRangedDefendModifier(1.25f);
                defendModifier = 0.85f;
                hitPointsRegeneration = 10f;
                hitPointsTotal = 100f;
                break;
            case HORSEMEN:
                isBuilder = false;
                movementPointsTotal = 5;
                attackRanged = false;
                rangedAttackModifier = 0;
                attackModifier = 1.15f;
                setRangedDefendModifier(0.8f);
                defendModifier = 1f;
                hitPointsRegeneration = 12f;
                hitPointsTotal = 125f;
                break;
            case GENERAL:
                isBuilder = false;
                movementPointsTotal = 3;
                attackRanged = true;
                rangedAttackModifier = 2f;
                attackModifier = 2f;
                setRangedDefendModifier(2f);
                defendModifier = 2f;
                hitPointsRegeneration = 15f;
                hitPointsTotal = 200f;
                break;
            default:
                isBuilder = false;
                movementPointsTotal = 15123123;
                attackRanged = false;
                rangedAttackModifier = 0;
                attackModifier = 0;
                setRangedDefendModifier(0);
                defendModifier = 0;
                hitPointsRegeneration = 0;
                hitPointsTotal = 0;
        }
        movementPoints = movementPointsTotal;
        hitPoints = hitPointsTotal;
        expPointsTotal = 100;
        return true;
    }

    ActorUnit(Vector2Integer position, PlayerENUM playerENUM) {
        super();
        drawPosition = position;
        this.playerenum = playerENUM;
        actorType = ActorType.UNIT;
    }

    public void assign(Tile tile) {
        exists = true;
        actorType = tile.actorType;
        actorUnitType = tile.actorUnitType;
        initializeStats();
    }


    public ActorType getActorType() { return actorType; }
    public ActorUnitType getActorUnitType() { return actorUnitType; }

}

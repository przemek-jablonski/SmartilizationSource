package com.szparag.ijtest1;


class ActorBuilding extends Actor
{
    private static final long serialVersionUID = 4311012495483327860L;

    enum ActorBuildingType {
        CAPITAL, CASTLE, FORTRESS, WATERMILL, STABLE, UNITDISPENSER
    }
    ActorBuildingType actorBuildingType;
    //init on globalturn
    private float    hitPoints; //init on globalturn

    //init on spawn
    private float    hitPointsTotal;
    private float    regenerationModifier;
    private float    unitRegenerationAddition;
    private float    defendModifier;
    private float    attackAddition;
    //resources:
    private float    incomeGold;
    private float    incomeSupplies;
    private float    costGold;
    private float    costMaintenanceGold;


    public boolean initializeStats()
    {
        switch(actorBuildingType) {
            case CAPITAL:
                hitPointsTotal = 300f;
                regenerationModifier = 15f;
                unitRegenerationAddition = 10f;
                defendModifier = 1.15f;
                attackAddition = 0.1f;
                incomeGold = 100;
                incomeSupplies = 10;
                costGold = 0;
                costMaintenanceGold = 0;
                break;
            case CASTLE:
                costMaintenanceGold = 300;
                costGold = 2000;
                incomeGold = 0;
                incomeSupplies = 1;
                defendModifier = 2f;
                attackAddition = 0.15f;
                hitPointsTotal = 300;
                regenerationModifier = 25;
                unitRegenerationAddition = 5;
                break;
            case FORTRESS:
                costMaintenanceGold = 300;
                costGold = 2000;
                incomeGold = 0;
                incomeSupplies = 2;
                defendModifier = 2.25f;
                attackAddition = 0.30f;
                hitPointsTotal = 300;
                regenerationModifier = 30;
                unitRegenerationAddition = 10;
                break;
            case WATERMILL:
                costMaintenanceGold = 0;
                costGold = 500;
                incomeGold = 50;
                incomeSupplies = 25;
                defendModifier = 0.5f;
                attackAddition = 0f;
                hitPointsTotal = 50;
                regenerationModifier = 5;
                unitRegenerationAddition = 2;
                break;
            case STABLE:
                costMaintenanceGold = 150;
                costGold = 1500;
                incomeGold = 0;
                incomeSupplies = 2;
                defendModifier = 0.7f;
                attackAddition = 0f;
                hitPointsTotal = 100;
                regenerationModifier = 10;
                unitRegenerationAddition = 0;
                break;
            case UNITDISPENSER:
                costMaintenanceGold = 50;
                costGold = 500;
                incomeGold = 0;
                incomeSupplies = 1;
                defendModifier = 0.7f;
                attackAddition = 0f;
                hitPointsTotal = 100;
                regenerationModifier = 5;
                unitRegenerationAddition = 0;
                break;
            default:
                hitPointsTotal = 0;
                regenerationModifier = 0;
                unitRegenerationAddition = 0;
                defendModifier = 0;
                attackAddition = 0;
                incomeGold = 0;
                incomeSupplies = 0;
                return false;
        }
        return true;
    }

    ActorBuilding(Vector2Integer position, PlayerENUM player) {
        super();
        drawPosition = position;
        actorType = ActorType.BUILDING;
        this.playerenum = player;

    }

    public void assign(Tile tile) {
        exists = true;
        actorType = tile.actorType;
        actorBuildingType = tile.actorBuildingType;
        initializeStats();
        hitPoints = hitPointsTotal;
    }

    public void updateOnTurnEnd(){
        if(hitPoints<hitPointsTotal){
            if((hitPoints + regenerationModifier) > hitPointsTotal)
                hitPoints = hitPointsTotal;
            else
                hitPoints += regenerationModifier;
        }
    }

    public ActorType getActorType() { return actorType; }

    public ActorBuildingType getActorBuildingType() { return actorBuildingType; }

    public void setHitPointsTotal(float hitPointsTotal) {
        this.hitPointsTotal = hitPointsTotal;
    }

    public void setHitPoints(float hitPoints) {
        this.hitPoints = hitPoints;
    }

    public void setRegenerationModifier(double regenerationModifier) {
        this.regenerationModifier = (float)regenerationModifier;
    }

    public void setUnitRegenerationAddition(float unitRegenerationAddition) {
        this.unitRegenerationAddition = unitRegenerationAddition;
    }

    public void setDefendModifier(double defendModifier) {
        this.defendModifier = (float)defendModifier;
    }

    public void setAttackAddition(float attackAddition) {
        this.attackAddition = attackAddition;
    }

    public void setIncomeGold(double incomeGold) {
        this.incomeGold = (float)incomeGold;
    }

    public void setIncomeSupplies(float incomeSupplies) {
        this.incomeSupplies = incomeSupplies;
    }

    public float getAttackAddition() {
        return attackAddition;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public float getHitPoints() {
        return hitPoints;
    }

    public float getHitPointsTotal() {
        return hitPointsTotal;
    }

    public float getRegenerationModifier() {
        return regenerationModifier;
    }

    public float getUnitRegenerationAddition() {
        return unitRegenerationAddition;
    }

    public float getDefendModifier() {
        return defendModifier;
    }

    public float getIncomeGold() {
        return incomeGold;
    }

    public float getIncomeSupplies() {
        return incomeSupplies;
    }

    public float getCostGold() {
        return costGold;
    }

    public float getCostMaintenanceGold() {
        return costMaintenanceGold;
    }
}

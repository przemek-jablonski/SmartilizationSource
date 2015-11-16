package com.szparag.ijtest1;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import java.util.ArrayList;

/**
 * Created by Szparagowy Krul 3000 on 23/05/2015.
 */
public class ControllerUserInterface {

    CivMain             civMain;
    private Stage       stageUserInterface;
    private Skin        userinterfaceskin;
    private Table       tablelefttop;
    private Table       tablerightbottom;
    private Table       tablerighttop;

    private Stage       stagePopupStats;
    private Dialog      dialogPopupStats;
    private Table       tablePopupStats;
    private Stage       stageBuild;
    private Table       tableBuildUnit;
    private Table       tableBuildCapital;
    private Table       tableBuildCastle;
    private Table       tableBuildFortress;
    private Table       tableBuildWatermill;
    private Table       tableBuildStable;
    private Table       tableBuildArmory;

    public  Stage        stagepublic;

    private TextButton  buttonTurn;
    private TextButton  buttonMenu;
    private TextButton  buttonStats;
    private TextButton  buttonOverview;
    private TextButton  actionMove;
    private TextButton  actionAttack;
    private TextButton  actionPromote;
    private TextButton  actionFortify;
    private TextButton  actionBuild;
    private TextButton  actionDisband;
    private TextButton  actionAttackRanged;
    private Window      unitstats;

    private boolean     actionMoveSet = false;
    private boolean     actionCombatSet = false;
    private boolean     actionCombatRangedSet = false;
    private boolean     actionTurnSet = false;
    private boolean     actionFortifySet = false;
    private boolean     buttonStatsSet = false;
    private boolean     actionBuildSet = false;
    Dialog              nextturnPopup;

    private Label []    actorBriefStatsGuiLabel = new Label[6];
    private Label []    actorBriefStatsGuiTexts = new Label[6];
    private Label []    actorStatsLabel = new Label[15];
    private Label []    actorStatsTexts = new Label[15];

    Sprite      backgroundBlackFog;
    private ModelTurns modelTurns;
    private ControllerMouseInput controllerMouseInput;
    private ModelActorEditor modelActorEditor;
    private Hex[][]     hexmap;

    private boolean     hasHexmapChanged = false;
    private Builder builder;
    private Vector2Integer buildSelection;
    private Actor actorSelected;
    private boolean guiactivity;
    private boolean multiplayerinstance = false;
    CivClient civClient;

    public void updatemap(Hex[][] hexmap){
        this.hexmap = hexmap;
    }

    public void updateguiactivity(boolean value){
        guiactivity = value;
    }

    //region initialization (constructors + injectors)
    public ControllerUserInterface(){
        backgroundBlackFog = new Sprite(new Texture(Gdx.files.internal("blackfog_background1920.png")));
        actorSelected = new Actor();
    }

    public void inject_hexmap(Hex[][] hexmap){
        this.hexmap = hexmap;
    }

    public void inject_controllers(Object input){
        if(input instanceof CivMain) {
            this.civMain = (CivMain)input;
            this.modelActorEditor = ((CivMain)input).modelActorEditor;
            this.modelTurns = ((CivMain)input).modelTurns;
            this.controllerMouseInput = ((CivMain)input).controllerMouseInput;
        }
        else if (input instanceof CivClient) {
            multiplayerinstance = true;
            this.civClient = (CivClient)input;
            this.modelActorEditor = ((CivClient)input).modelActorEditor;
            this.modelTurns = ((CivClient)input).modelTurns;
            this.controllerMouseInput = ((CivClient)input).controllerMouseInput;
        }
    }

    public void inject_builder(Builder builder){
        this.builder = builder;
    }

    public void initialize(){
        uiBaseConstructor();
        buttonStats_creator();
    }
    //endregion

    //region creating base of ui (3 dialogues on tables on uiscene)
    private void uiBaseConstructor() {
        stageUserInterface = new Stage();
        userinterfaceskin = new Skin(Gdx.files.internal("uiskin.json"));
        nextturnPopup = new Dialog("Turn Complete?", userinterfaceskin);
        buttonsCreator();

        tablelefttop = new Table();
        tableLeftTopFill();

        tablerightbottom = new Table();
        tableRightBottomFill();

        unitStatsLabelsCreator();
        unitStatsDialogUpdate();

        unitstats = new Dialog(" ", userinterfaceskin);
        unitStatsWindowCreator();

        tablerighttop = new Table();
        tableRightTopFill();

        actionBuildUnit_constructor();
        buildArmory_tableconstructor();
        buildCapital_tableconstructor();
        buildCastle_tableconstructor();
        buildFortress_tableconstructor();
        buildStable_tableconstructor();
        buildWatermill_tableconstructor();
        buildCapital_tableconstructor();

        actionTurn_listener();
        actionAttack_listener();
        actionAttackRanged_listener();
        actionBuild_listener();
        actionDisband_listener();
        actionFortify_listener();
        actionMove_listener();
        buttonStats_listener();

        stageUserInterface.addActor(tablelefttop);
        stageUserInterface.addActor(tablerightbottom);
        stageUserInterface.addActor(tablerighttop);
    }

    private void buttonsCreator(){
        actionMove = new TextButton("Move", userinterfaceskin);
        actionMove.setSize(50, 50);
        actionMove.setColor(0.53f, 0.35f, 0.20f, 0.85f);
        actionAttack = new TextButton("Attack", userinterfaceskin);
        actionAttack.setSize(50, 50);
        actionAttack.setColor(0.53f, 0.35f, 0.20f, 0.85f);
        actionAttackRanged = new TextButton("Ranged", userinterfaceskin);
        actionAttackRanged.setSize(50, 50);
        actionAttackRanged.setColor(0.53f, 0.35f, 0.20f, 0.85f);
        actionBuild = new TextButton("Build", userinterfaceskin);
        actionBuild.setSize(50, 50);
        actionBuild.setColor(0.53f, 0.35f, 0.20f, 0.85f);
        actionFortify = new TextButton("Fortify", userinterfaceskin);
        actionFortify.setSize(50, 50);
        actionFortify.setColor(0.53f, 0.35f, 0.20f, 0.85f);
        buttonStats = new TextButton("Info", userinterfaceskin);
        buttonStats.setSize(60, 60);
        buttonStats.setColor(0.58f, 0.40f, 0.25f, 0.95f);
        actionPromote = new TextButton("lvl Up", userinterfaceskin);
        actionPromote.setSize(50, 50);
        actionPromote.setColor(0.53f, 0.35f, 0.20f, 0.85f);
        actionDisband = new TextButton("Disband", userinterfaceskin);
        actionDisband.setSize(50, 50);
        actionDisband.setColor(0.58f, 0.40f, 0.25f, 0.85f);
        buttonTurn = new TextButton("Turn", userinterfaceskin);
        buttonTurn.setSize(60, 60);
        buttonTurn.setColor(0.58f, 0.40f, 0.25f, 0.85f);
        buttonMenu = new TextButton("Menu", userinterfaceskin);
        buttonMenu.setSize(60, 60);
        buttonMenu.setColor(0.58f, 0.40f, 0.25f, 0.95f);
        buttonOverview = new TextButton("Overview", userinterfaceskin);
        buttonOverview.setSize(60, 60);
        buttonOverview.setColor(0.58f, 0.40f, 0.25f, 0.95f);
    }

    private void tableLeftTopFill(){
        tablelefttop.setWidth(stageUserInterface.getWidth());
        tablelefttop.align(Align.left | Align.top);
        tablelefttop.setPosition(0, Gdx.graphics.getHeight());
        tablelefttop.padTop(20).padLeft(10);
        tablelefttop.add(actionMove).size(55, 55).padBottom(13).row();
        tablelefttop.add(actionAttack).size(55, 55).padBottom(13).row();
        tablelefttop.add(actionAttackRanged).size(55, 55).padBottom(13).row();
        tablelefttop.add(actionBuild).size(55, 55).padBottom(13).row();
        tablelefttop.add(actionFortify).size(55, 55).padBottom(13).row();
        tablelefttop.add(buttonStats).size(55, 55).padBottom(13).row();
        tablelefttop.add(actionPromote).size(55, 55).padBottom(13).row();
        tablelefttop.add(actionDisband).size(55, 55).padBottom(13).row();
    }

    private void tableRightBottomFill(){
        tablerightbottom.setWidth(stageUserInterface.getWidth());
        tablerightbottom.setPosition(0, Gdx.graphics.getHeight());
        tablerightbottom.align(Align.right | Align.top);
        tablerightbottom.padTop(20);
        tablerightbottom.padRight(10);
        tablerightbottom.add(buttonOverview).size(80, 80).padLeft(10);
        tablerightbottom.add(buttonMenu).size(80, 80).padLeft(10);
        tablerightbottom.add(buttonTurn).size(80, 80).padLeft(10);
    }

    private void tableRightTopFill(){
        tablerighttop.setWidth(stageUserInterface.getWidth());
        tablerighttop.align(Align.left | Align.bottom);
        tablerighttop.padLeft(10).padBottom(20);
        tablerighttop.add(unitstats);
    }

    //region creating / updating leftbottom table (brief unit stats)
    private void unitStatsWindowCreator(){
        unitstats.setTitle("________________________________");
        unitstats.setMovable(false);
        unitstats.setColor(0.58f, 0.40f, 0.25f, 0.70f);
        unitstats.setModal(false);
        unitstats.setResizable(false);
        unitstats.row();

        for (int i=0 ; i < actorBriefStatsGuiLabel.length; ++i){
            unitstats.add(actorBriefStatsGuiTexts[i]).left().align(Align.left);
            unitstats.add(actorBriefStatsGuiLabel[i]).right().align(Align.right);
            unitstats.row();
        }
    }

    private void unitStatsLabelsCreator(){
        for (int i=0 ; i< actorBriefStatsGuiLabel.length; ++i) {
            if (i == 0 || i == 1) {
                actorBriefStatsGuiLabel[i] = new Label("b", userinterfaceskin, "default");
                actorBriefStatsGuiTexts[i] = new Label("a", userinterfaceskin, "default");
            }
            else {
                actorBriefStatsGuiLabel[i] = new Label("b", userinterfaceskin, "different");
                actorBriefStatsGuiTexts[i] = new Label("a", userinterfaceskin, "different");
            }
            actorBriefStatsGuiLabel[i].setAlignment(Align.right);
            actorBriefStatsGuiTexts[i].setAlignment(Align.left);
        }
    }

    public void unitStatsDialogUpdate() {
        StringBuilder temp = new StringBuilder("nic tu nie ma, nie interesuj sie");
        if (modelActorEditor.selection == Selection.UNIT || modelActorEditor.selection == Selection.UNITBUILDING){
            ArrayList<Actor> tarr = hexmap[controllerMouseInput.lmbHexSelectedVector.x()][controllerMouseInput.lmbHexSelectedVector.y()].actorList;
            actorBriefStatsGuiTexts[0].setText("Unit Name:");
            actorBriefStatsGuiTexts[1].setText("Belonging:");
            actorBriefStatsGuiTexts[2].setText("Move Points:");
            actorBriefStatsGuiTexts[3].setText("Hit Points:");
            actorBriefStatsGuiTexts[4].setText("Exp Points:");
            actorBriefStatsGuiTexts[5].setText("Is Builder:");
            actorBriefStatsGuiLabel[0].setText(((ActorUnit) tarr.get(tarr.size() - 1)).actorUnitType.toString());
            actorBriefStatsGuiLabel[1].setText(tarr.get(tarr.size() - 1).playerenum.toString());
            actorBriefStatsGuiLabel[2].setText(Float.toString(((ActorUnit) tarr.get(tarr.size() - 1)).movementPoints) + " out of " + Float.toString(((ActorUnit) tarr.get(tarr.size() - 1)).movementPointsTotal));
            actorBriefStatsGuiLabel[3].setText(Float.toString(((ActorUnit) tarr.get(tarr.size() - 1)).hitPoints) + " out of " + Float.toString(((ActorUnit) tarr.get(tarr.size() - 1)).hitPointsTotal));
            actorBriefStatsGuiLabel[4].setText(Float.toString(((ActorUnit) tarr.get(tarr.size() - 1)).expPoints) + " out of " + Float.toString(((ActorUnit) tarr.get(tarr.size() - 1)).expPointsTotal));
            if(((ActorUnit) tarr.get(tarr.size() - 1)).isBuilder)
                actorBriefStatsGuiLabel[5].setText("yes");
            else
                actorBriefStatsGuiLabel[5].setText("no");
        }
        else
        {
            if(modelActorEditor.selection == Selection.BUILDING){
                ActorBuilding tbuild = ((ActorBuilding)hexmap[controllerMouseInput.lmbHexSelectedVector.x()][controllerMouseInput.lmbHexSelectedVector.y()].actorList.get(hexmap[controllerMouseInput.lmbHexSelectedVector.x()][controllerMouseInput.lmbHexSelectedVector.y()].actorList.size()-1));
                actorBriefStatsGuiTexts[0].setText("Building:");
                actorBriefStatsGuiTexts[1].setText("Belonging:");
                actorBriefStatsGuiTexts[2].setText("Hit Points:");
                actorBriefStatsGuiTexts[3].setText("Defender Bonus:");
                actorBriefStatsGuiTexts[4].setText("Gold Income:");
                actorBriefStatsGuiTexts[5].setText("Supplies Income:");
                actorBriefStatsGuiLabel[0].setText(tbuild.actorBuildingType.toString());
                actorBriefStatsGuiLabel[1].setText(tbuild.playerenum.toString());
                actorBriefStatsGuiLabel[2].setText(tbuild.getHitPoints() + " out of " + tbuild.getHitPointsTotal());
                actorBriefStatsGuiLabel[3].setText(Float.toString(tbuild.getDefendModifier()));
                actorBriefStatsGuiLabel[4].setText(Float.toString(tbuild.getIncomeGold()));
                actorBriefStatsGuiLabel[5].setText(Float.toString(tbuild.getIncomeSupplies()));
            }
            else{
                if(modelActorEditor.selection == Selection.LAND){
                    ActorLand tland = ((ActorLand)hexmap[controllerMouseInput.lmbHexSelectedVector.x()][controllerMouseInput.lmbHexSelectedVector.y()].actorList.get(hexmap[controllerMouseInput.lmbHexSelectedVector.x()][controllerMouseInput.lmbHexSelectedVector.y()].actorList.size()-1));
                    actorBriefStatsGuiTexts[0].setText("Land:");
                    actorBriefStatsGuiTexts[4].setText(" ");
                    actorBriefStatsGuiTexts[1].setText("Move Cost:");
                    actorBriefStatsGuiTexts[2].setText("Defender Bonus:");
                    actorBriefStatsGuiTexts[3].setText("Attack Bonus:");
                    actorBriefStatsGuiTexts[5].setText(" ");
                    actorBriefStatsGuiLabel[0].setText(tland.actorLandType.toString());
                    actorBriefStatsGuiLabel[4].setText(" ");
                    actorBriefStatsGuiLabel[1].setText(Float.toString(tland.moveCost));
                    actorBriefStatsGuiLabel[2].setText(Float.toString(tland.defendModifier));
                    actorBriefStatsGuiLabel[3].setText(Float.toString(tland.attackModifier));
                    actorBriefStatsGuiLabel[5].setText(" ");
                }
                else
                {
                    for (int i=0 ; i < actorBriefStatsGuiLabel.length; ++i){
                        actorBriefStatsGuiLabel[i].setText("");
                        actorBriefStatsGuiTexts[i].setText("");
                    }
                }
            }
        }
    }
    //endregion

    //endregion

    private void actionTurn_listener(){
        buttonTurn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent ie, float x, float y) {
                if(guiactivity) {
                    System.out.println("buttonTurn clicked.");
                /*
                if(actionTurnSet)
                    actionTurnSet = false;
                else {
                    nextturnPopup.text("Are you sure you've completed your turn?" +
                            "\nYou are going to pass turn to: " + controllerTurns.playerENUM.toString());
                    nextturnPopup.button("Yp yp yp", true);
                    nextturnPopup.button("Nop nop nop", false);
                    nextturnPopup.key(Input.Keys.ENTER, true);
                    actionTurnSet = true;
                }
                */
                    modelTurns.turnUpdate();
                //    modelTurns.setIshexmapchanged(true);
                }

            }
        });
    }

    private void actionMove_listener(){
        actionMove.addListener(new ClickListener() {
           @Override
           public void clicked(InputEvent ievent, float x, float y) {
               if (guiactivity) {
                   System.out.println("actionMove clicked");
                   Vector2Integer position = new Vector2Integer();
                   if (controllerMouseInput.lmbHexSelectedStack.size() == 1)
                       position = controllerMouseInput.lmbHexSelectedStack.get(0);
                   else
                       position = controllerMouseInput.lmbHexSelectedStack.get(1);

                   if (actionMoveSet) {
                       actionMoveSet = false;
                       modelActorEditor.actionMovement_trigger(actionMoveSet);
                   } else {
                       System.out.println("before if");
                       if (hexmap[position.x()][position.y()].getLastActor() instanceof ActorUnit) {
                           System.out.println("before inner if");
                           System.out.println("modelturns:active:"+modelTurns.getActivePlayer().toString());
                           System.out.println("modelturns:lastunitbelonging:"+hexmap[position.x()][position.y()].getLastUnitActor().playerenum.toString());
                           if (hexmap[position.x()][position.y()].getLastUnitActor().playerenum == modelTurns.getActivePlayer()) {
                               System.out.println("if passed");
                               actionMoveSet = true;
                               modelActorEditor.actionMovement_trigger(actionMoveSet);
                           }
                       }
                   }
               }
           }
       }
        );
    }

    private void actionAttack_listener(){
        actionAttack.addListener(new ClickListener() {
                 @Override
                 public void clicked(InputEvent ievent, float x, float y) {
                     if (guiactivity) {
                         System.out.println("actionAttack clicked");
                         if (actionCombatSet) {
                             actionCombatSet = false;
                             modelActorEditor.actionCombat_trigger(actionCombatSet);
                         } else {
                             actionCombatSet = true;
                             modelActorEditor.actionCombat_trigger(actionCombatSet);
                         }

                     }
                 }
             }
        );

    }

    private void actionAttackRanged_listener(){
        actionAttackRanged.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent ie, float x, float y) {
                if(guiactivity) {
                    System.out.println("actionFortify clicked.");
                    if (actionCombatRangedSet) {
                        actionCombatRangedSet = false;
                        modelActorEditor.actionCombatRanged_trigger(actionCombatRangedSet);
                    } else {
                        actionCombatRangedSet = true;
                        modelActorEditor.actionCombatRanged_trigger(actionCombatRangedSet);
                    }
                }
            }
        });
    }

    private void actionBuild_listener(){
        actionBuild.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent ie, float x, float y) {
                if(guiactivity) {
                    System.out.println("actionBuild(Unit) clicked.");
                    buildSelection = new Vector2Integer();
                    buildSelection.set(controllerMouseInput.lmbHexSelectedVector);
                    actorSelected = hexmap[buildSelection.x()][buildSelection.y()].getLastActor();
                    System.out.println("actionbuild_listener: " + buildSelection.printer());
                    if (!actionBuildSet) {
                        if ((actorSelected instanceof ActorUnit && ((ActorUnit) actorSelected).isBuilder)
                                || actorSelected instanceof ActorBuilding)
                            actionBuild_trigger(actorSelected);
                    } else if (actionBuildSet)
                        actionBuild_untrigger();
                }
            }
        });
    }

    private void actionDisband_listener(){
        actionDisband.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent ie, float x, float y) {
                if (guiactivity) {
                    System.out.println("actionDisband clicked.");
                    if (hexmap[controllerMouseInput.lmbHexSelectedVector.x()][controllerMouseInput.lmbHexSelectedVector.y()].actorList.size() > 1)
                        hexmap[controllerMouseInput.lmbHexSelectedVector.x()][controllerMouseInput.lmbHexSelectedVector.y()].actorList
                                .remove(hexmap[controllerMouseInput.lmbHexSelectedVector.x()][controllerMouseInput.lmbHexSelectedVector.y()].actorList.size() - 1);

                }
            }
        });
    }

    private void actionFortify_listener(){
        actionFortify.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent ie, float x, float y) {
                if(guiactivity) {
                    System.out.println("actionFortify clicked.");
                    if (actionFortifySet) {
                        actionFortifySet = false;
                        modelActorEditor.updateOnFortify(actionFortifySet);
                    } else {
                        actionFortifySet = true;
                        modelActorEditor.updateOnFortify(actionFortifySet);
                    }
                }
            }
        });
    }

    //region button with detailed stats
    private void buttonStats_listener(){
        buttonStats.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent ie, float x, float y) {
                if(guiactivity) {
                    System.out.println("actionFortify clicked.");
                    if (!buttonStatsSet) {
                        buttonStats_trigger();
                    } else
                        buttonStats_untrigger();
                }
            }
        });
    }

    private void buttonStats_creator(){
        stagePopupStats= new Stage();
        dialogPopupStats = new Dialog("Detailed info screen", userinterfaceskin);
    }

    private void buttonStats_updateLabels(Hex hex){
        actorStatsTexts[0].setText("Position:");
        actorStatsTexts[1].setText("Type:");
        actorStatsTexts[2].setText("Subtype:");
        actorStatsTexts[3].setText("PlayerENUM:");
        actorStatsTexts[4].setText("Move Points:");
        actorStatsTexts[5].setText("Hit Points");
        actorStatsTexts[6].setText("Experience:");
        actorStatsTexts[7].setText("Combat Proficiency:");
        actorStatsTexts[8].setText("Ranged Combat:");
        actorStatsTexts[9].setText("Defend Proficiency");
        actorStatsTexts[10].setText("Range Defend");
        actorStatsTexts[11].setText("Hit Points Regeneration: ");
        actorStatsTexts[12].setText("Ranged Unit:");
        actorStatsTexts[13].setText("Builder Unit");
        actorStatsTexts[14].setText("Fortified:");

        actorStatsLabel[0].setText(hex.position.printer());
        actorStatsLabel[1].setText(hex.getLastUnitActor().actorType.toString());
        actorStatsLabel[2].setText(hex.getLastUnitActor().actorUnitType.toString());
        actorStatsLabel[3].setText(hex.getLastUnitActor().playerenum.toString());
        actorStatsLabel[4].setText(hex.getLastUnitActor().movementPoints + " / " + hex.getLastUnitActor().movementPointsTotal);
        actorStatsLabel[5].setText(hex.getLastUnitActor().hitPoints + " / " + hex.getLastUnitActor().hitPointsTotal);
        actorStatsLabel[6].setText(hex.getLastUnitActor().expPoints + " / " + hex.getLastUnitActor().expPointsTotal);
        actorStatsLabel[7].setText(Float.toString(hex.getLastUnitActor().attackModifier));
        actorStatsLabel[8].setText(Float.toString(hex.getLastUnitActor().rangedAttackModifier));
        actorStatsLabel[9].setText(Float.toString(hex.getLastUnitActor().defendModifier));
        actorStatsLabel[10].setText(Float.toString(hex.getLastUnitActor().getRangedDefendModifier()));
        actorStatsLabel[11].setText(Float.toString(hex.getLastUnitActor().hitPointsRegeneration));
        actorStatsLabel[12].setText(Boolean.toString(hex.getLastUnitActor().attackRanged));
        actorStatsLabel[13].setText(Boolean.toString(hex.getLastUnitActor().isBuilder));
        actorStatsLabel[14].setText(Boolean.toString(hex.getLastUnitActor().isFortified));
    }

    public void buttonStats_trigger(){
        Hex actualhex = hexmap[controllerMouseInput.lmbHexSelectedVector.x()][controllerMouseInput.lmbHexSelectedVector.y()];
        if(actualhex.getLastActor() instanceof ActorUnit) {
            buttonStatsSet = true;
            String underline = "___________________";
            if (buttonStatsSet == true && actualhex.getLastActor() instanceof ActorUnit) {
                //constructors:
                stagePopupStats = new Stage();
                dialogPopupStats = new Dialog(" ", userinterfaceskin);
                tablePopupStats = new Table();
                TextButton okbutton = new TextButton("OK", userinterfaceskin);
                for (int i = 0; i < actorStatsTexts.length; ++i) {
                    actorStatsTexts[i] = new Label("trol", userinterfaceskin, "different");
                    actorStatsLabel[i] = new Label("trol2", userinterfaceskin, "different");
                }

                //initial init:
                buttonStats_updateLabels(actualhex);
                okbutton.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent ie, float x, float y) {
                        System.out.println("actionstats button OK clicked.");
                        buttonStats_untrigger();
                    }
                });

                dialogPopupStats.setColor(0.78f, 0.60f, 0.45f, 0.93f);
                dialogPopupStats.setMovable(false);
                dialogPopupStats.setModal(false);
                dialogPopupStats.setResizable(false);
                dialogPopupStats.row();

                for (int i = 0; i < actorStatsTexts.length; ++i) {
                    dialogPopupStats.add(actorStatsTexts[i]).left().align(Align.left);
                    dialogPopupStats.add(actorStatsLabel[i]).right();
                    dialogPopupStats.row();
                    if (i == 3 || i == 6) dialogPopupStats.add(underline).center().align(Align.center).row();
                }
                dialogPopupStats.add(underline).row();
                dialogPopupStats.addActor(okbutton);

                tablePopupStats.setWidth(stagePopupStats.getWidth());
                //   tablePopupStats.align(Align.center);
                //    tablePopupStats.center();
                tablePopupStats.setPosition(0, Gdx.graphics.getHeight() / 2);
                tablePopupStats.add(dialogPopupStats);

                stagePopupStats.addActor(tablePopupStats);
                if(!multiplayerinstance) civMain.rewire_InputMultiplexer(CivMain.InputMultiplexerState.STATS);
                     else civClient.rewire_InputMultiplexer(CivClient.InputMultiplexerState.STATS);
            }
        }
    }

    public void buttonStats_untrigger() {
        buttonStatsSet = false;
        if(!multiplayerinstance) civMain.rewire_InputMultiplexer(CivMain.InputMultiplexerState.GAME);
        else civClient.rewire_InputMultiplexer(CivClient.InputMultiplexerState.GAME);
    }
    //endregion

    //region building button (unit)
    private void actionBuildUnit_constructor(){
        stageBuild = new Stage();
        tableBuildUnit = new Table();
        TextButton millButton = new TextButton("Build a Watermill (500)", userinterfaceskin);
        millButton.setSize(150, 50);
        millButton.setColor(0.53f, 0.35f, 0.20f, 0.85f);
        millButton.align(Align.left);
        TextButton armoryButton = new TextButton("Build an Armory (500)", userinterfaceskin);
        armoryButton.setSize(150, 50);
        armoryButton.setColor(0.53f, 0.35f, 0.20f, 0.85f);
        armoryButton.align(Align.left);
        TextButton stableButton = new TextButton("Build a Stable (1500)", userinterfaceskin);
        stableButton.setSize(150, 50);
        stableButton.setColor(0.53f, 0.35f, 0.20f, 0.85f);
        stableButton.align(Align.left);
        TextButton castleButton = new TextButton("Build a Castle (2000)", userinterfaceskin);
        castleButton.setSize(150, 50);
        castleButton.setColor(0.53f, 0.35f, 0.20f, 0.85f);
        castleButton.align(Align.left);

        millButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent ie, float x, float y) {
                System.out.println("millbutton clicked.");
                builder.buildWatermill(hexmap[buildSelection.x()][buildSelection.y()]);
            }
        });

        armoryButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent ie, float x, float y) {
                System.out.println("armoryButton clicked.");
                builder.buildArmory(hexmap[buildSelection.x()][buildSelection.y()]);
            }
        });

        stableButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent ie, float x, float y) {
                System.out.println("stableButton clicked.");
                builder.buildStable(hexmap[buildSelection.x()][buildSelection.y()]);
            }
        });

        castleButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent ie, float x, float y) {
                System.out.println("castleButton clicked.");
                builder.buildCastle(hexmap[buildSelection.x()][buildSelection.y()]);
            }
        });

        tableBuildUnit.add(millButton).size(250,50).right().row();
        tableBuildUnit.add(armoryButton).size(250,50).right().row();
        tableBuildUnit.add(stableButton).size(250,50).right().row();
        tableBuildUnit.add(castleButton).size(250,50).right().row();

    }

    private void actionBuild_untrigger(){
        actionBuildSet = false;
        if(!multiplayerinstance) civMain.rewire_InputMultiplexer(CivMain.InputMultiplexerState.GAME);
        else civClient.rewire_InputMultiplexer(CivClient.InputMultiplexerState.GAME);
    }

    private void actionBuild_trigger(Actor selected){
        actionBuildSet = true;
        if(selected instanceof ActorUnit) {
            stageBuild = new Stage();
            tableBuildUnit.align(Align.right | Align.center);
            tableBuildUnit.setWidth(stageBuild.getWidth());
            tableBuildUnit.setHeight(stageBuild.getHeight());
            stageBuild.addActor(tableBuildUnit);
        } else if (selected instanceof ActorBuilding) {
            switch(((ActorBuilding) selected).actorBuildingType){
                case CAPITAL:
                    stageBuild = new Stage();
                    tableBuildCapital.align(Align.right | Align.center);
                    tableBuildCapital.setWidth(stageBuild.getWidth());
                    tableBuildCapital.setHeight(stageBuild.getHeight());
                    stageBuild.addActor(tableBuildCapital);
                    break;
                case CASTLE:
                    stageBuild = new Stage();
                    tableBuildCastle.align(Align.right | Align.center);
                    tableBuildCastle.setWidth(stageBuild.getWidth());
                    tableBuildCastle.setHeight(stageBuild.getHeight());
                    stageBuild.addActor(tableBuildCastle);
                    break;
                case FORTRESS:
                    stageBuild = new Stage();
                    tableBuildFortress.align(Align.right | Align.center);
                    tableBuildFortress.setWidth(stageBuild.getWidth());
                    tableBuildFortress.setHeight(stageBuild.getHeight());
                    stageBuild.addActor(tableBuildFortress);
                    break;
                case WATERMILL:
                    stageBuild = new Stage();
                    tableBuildWatermill.align(Align.right | Align.center);
                    tableBuildWatermill.setWidth(stageBuild.getWidth());
                    tableBuildWatermill.setHeight(stageBuild.getHeight());
                    stageBuild.addActor(tableBuildWatermill);
                    break;
                case STABLE:
                    stageBuild = new Stage();
                    tableBuildStable.align(Align.right | Align.center);
                    tableBuildStable.setWidth(stageBuild.getWidth());
                    tableBuildStable.setHeight(stageBuild.getHeight());
                    stageBuild.addActor(tableBuildStable);
                    break;
                case UNITDISPENSER:
                    stageBuild = new Stage();
                    tableBuildArmory.align(Align.right | Align.center);
                    tableBuildArmory.setWidth(stageBuild.getWidth());
                    tableBuildArmory.setHeight(stageBuild.getHeight());
                    stageBuild.addActor(tableBuildArmory);
                    break;
            }
        }

    //    switch(){
    //    }
        //   stagepublic = new Stage();
        //   stagepublic.addActor(stageBuild)
        if(!multiplayerinstance) civMain.rewire_InputMultiplexer(CivMain.InputMultiplexerState.BUILD);
        else civClient.rewire_InputMultiplexer(CivClient.InputMultiplexerState.BUILD);
    }
    //endregion

    private void buildCapital_tableconstructor(){
        tableBuildCapital = new Table();
        TextButton defupgr = new TextButton("Defence upgrade (300) (+5%)", userinterfaceskin);
        TextButton resoupgr = new TextButton("Gold Income upgrade (500) (+5%)", userinterfaceskin);
        TextButton regenupgr = new TextButton("HP Regeneration upgrade (250) (+3)", userinterfaceskin);
        TextButton engineer = new TextButton("Train Engineer(100)", userinterfaceskin);

        defupgr.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent ie, float x, float y) {
                System.out.println("defupgr.");
                ((ActorBuilding)actorSelected).setDefendModifier(((ActorBuilding)actorSelected).getDefendModifier()*1.05);
            }
        });

        resoupgr.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent ie, float x, float y) {
                System.out.println("resoupgr.");
                ((ActorBuilding)actorSelected).setIncomeGold(((ActorBuilding) actorSelected).getIncomeGold() * 1.05);
            }
        });

        regenupgr.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent ie, float x, float y) {
                System.out.println("regenupgr.");
                ((ActorBuilding)actorSelected).setRegenerationModifier(((ActorBuilding)actorSelected).getRegenerationModifier()+3);
            }
        });

        engineer.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent ie, float x, float y) {
                System.out.println("engineer.");
            }
        });

        defupgr.setColor(0.53f, 0.35f, 0.20f, 0.85f);
        resoupgr.setColor(0.53f, 0.35f, 0.20f, 0.85f);
        regenupgr.setColor(0.53f, 0.35f, 0.20f, 0.85f);
        engineer.setColor(0.53f, 0.35f, 0.20f, 0.85f);

        tableBuildCapital.add(defupgr).size(400, 50).right().row();
        tableBuildCapital.add(resoupgr).size(400, 50).right().row();
        tableBuildCapital.add(regenupgr).size(400, 50).right().row();
        tableBuildCapital.add(engineer).size(400, 50).right().row();
    }

    private void buildCastle_tableconstructor(){
        tableBuildCastle = new Table();
        TextButton regenupgr = new TextButton("HP Regeneration upgrade \n(Cost: 400 Gold) (+3)", userinterfaceskin);
        TextButton atkupgr = new TextButton("Unit Attack Addition upgrade \n(Cost: 400 Gold) (+10%)", userinterfaceskin);
        TextButton fortress = new TextButton("Build a FORTRESS \n(Cost: 2000 Gold)", userinterfaceskin);

        regenupgr.setColor(0.53f, 0.35f, 0.20f, 0.85f);
        atkupgr.setColor(0.53f, 0.35f, 0.20f, 0.85f);
        fortress.setColor(0.57f, 0.39f, 0.24f, 0.90f);

        tableBuildCastle.add(regenupgr).size(300, 75).right().row();
        tableBuildCastle.add(atkupgr).size(300, 75).right().row();
        tableBuildCastle.add(fortress).size(300, 75).right().row();
    }

    private void buildFortress_tableconstructor(){
        tableBuildFortress = new Table();
        TextButton defupgr = new TextButton("Defence upgrade (250) (+1%)", userinterfaceskin);
        TextButton atkupgr = new TextButton("Unit Attack Addition upgrade (300) (+3%)", userinterfaceskin);

        defupgr.setColor(0.53f, 0.35f, 0.20f, 0.85f);
        atkupgr.setColor(0.53f, 0.35f, 0.20f, 0.85f);

        tableBuildFortress.add(defupgr).size(400, 50).right().row();
        tableBuildFortress.add(atkupgr).size(400, 50).right().row();
    }

    private void buildWatermill_tableconstructor(){
        tableBuildWatermill = new Table();
        TextButton income = new TextButton("Income Switch (1000) (+25GP/+50SUP)", userinterfaceskin);
        TextButton regenupgr = new TextButton("HP Regeneration upgrade (200) (+5)", userinterfaceskin);

        income.setColor(0.53f, 0.35f, 0.20f, 0.85f);
        regenupgr.setColor(0.53f, 0.35f, 0.20f, 0.85f);

        tableBuildWatermill.add(income).size(400, 50).right().row();
        tableBuildWatermill.add(regenupgr).size(400, 50).right().row();
    }

    private void buildStable_tableconstructor(){
        tableBuildStable = new Table();
        TextButton regenupgr = new TextButton("HP Regeneration upgrade (250) (+3)", userinterfaceskin);
        TextButton horsemen = new TextButton("Train Horsemen(550)", userinterfaceskin);

        regenupgr.setColor(0.53f, 0.35f, 0.20f, 0.85f);
        horsemen.setColor(0.65f, 0.35f, 0.20f, 0.85f);

        tableBuildStable.add(regenupgr).size(400, 50).right().row();
        tableBuildStable.add(horsemen).size(400, 50).right().row();
    }

    private void buildArmory_tableconstructor(){
        tableBuildArmory = new Table();
        TextButton atkupgr = new TextButton("Unit Attack Addition upgrade (200) (+3%)", userinterfaceskin);
        TextButton regenupgr = new TextButton("HP Regeneration upgrade (250) (+3)", userinterfaceskin);
        TextButton engineerz = new TextButton("Train Engineer(100)", userinterfaceskin);
        TextButton scoutz = new TextButton("Train Scout(150)", userinterfaceskin);
        TextButton warriorz = new TextButton("Train Warrior(300)", userinterfaceskin);
        TextButton archerz = new TextButton("Train Archer(450)", userinterfaceskin);

        atkupgr.setColor(0.53f, 0.35f, 0.20f, 0.85f);
        regenupgr.setColor(0.53f, 0.35f, 0.20f, 0.85f);
        engineerz.setColor(0.65f, 0.35f, 0.20f, 0.85f);
        scoutz.setColor(0.65f, 0.35f, 0.20f, 0.85f);
        warriorz.setColor(0.65f, 0.35f, 0.20f, 0.85f);
        archerz.setColor(0.65f, 0.35f, 0.20f, 0.85f);

        tableBuildArmory.add(atkupgr).size(400, 50).right().row();
        tableBuildArmory.add(regenupgr).size(400, 50).right().row();
        tableBuildArmory.add(engineerz).size(400, 50).right().row();
        tableBuildArmory.add(scoutz).size(400, 50).right().row();
        tableBuildArmory.add(warriorz).size(400, 50).right().row();
        tableBuildArmory.add(archerz).size(400, 50).right().row();
    }

    public boolean getActionMoveSet(){
        return this.actionMoveSet;
    }

    public void setActionMoveSet(boolean set){
        actionMoveSet = set;
    }

    public Stage getStageUserInterface(){
        return this.stageUserInterface;
    }

    public Sprite getBackgroundBlackFog(){
        return this.backgroundBlackFog;
    }

    public boolean getActionCombatSet(){
        return this.actionCombatSet;
    }

    public void setActionCombatSet(boolean set){
        actionCombatSet = set;
    }

    public boolean isHasHexmapChanged() {
        return hasHexmapChanged;
    }

    public void setHasHexmapChanged(boolean hasHexmapChanged) {
        this.hasHexmapChanged = hasHexmapChanged;
    }

    public boolean getActionTurnSet(){ return this.actionTurnSet; }

    public Stage getStagePopupStats() {
        return stagePopupStats;
    }

    public void setStagePopupStats(Stage stagePopupStats) {
        this.stagePopupStats = stagePopupStats;
    }

    public boolean isButtonStatsSet() {
        return buttonStatsSet;
    }

    public void setButtonStatsSet(boolean buttonStatsSet) {
        this.buttonStatsSet = buttonStatsSet;
    }

    public boolean isActionCombatRangedSet(){
        return this.actionCombatRangedSet;
    }

    public void setActionCombatRangedSet(boolean value){
        this.actionCombatRangedSet = value;
    }

    public boolean isActionBuildSet() {
        return actionBuildSet;
    }

    public void setActionBuildSet(boolean actionBuildSet) {
        this.actionBuildSet = actionBuildSet;
    }

    public Stage getStageBuild() {
        return stageBuild;
    }

    public void setStageBuild(Stage stageBuild) {
        this.stageBuild = stageBuild;
    }

}

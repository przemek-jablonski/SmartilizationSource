package com.szparag.ijtest1;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.*;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.szparag.ijtest1.Configurators.ConfigSystem;
import com.szparag.ijtest1.Configurators.GameConfig;

import java.io.IOException;

public class CivMain extends ApplicationAdapter implements InputProcessor {

    //GSTATE

    //

    private InputMultiplexer inputMultiplexer;
    private InputMultiplexerState inputMultiplexerState;
    Toolbox toolbox;
    enum InputMultiplexerState{
        GAME, STATS, OVERVIEW, BUILD, MENU, MAINMENU, LOADING
    }

    SpriteBatch guibatch;
    BitmapFont font;
    BitmapFont font2;

    GameConfig gameConfig;

    ControllerMouseInput controllerMouseInput;
    ViewRender viewRender;
    ModelTurns modelTurns;
    ModelActorEditor modelActorEditor;
    ControllerUserInterface controllerUserInterface;
    InitializerHexmap initializerHexmap;
    Hex[][] hexmap;
    Vector2Integer hexmapdimensions;
    Builder builder;
    GameState gameState = new GameState();
    ControllerUserInterfaceMenu controllerUserInterfaceMenu;
    private boolean rewire_inputmultiplexer_done = false;
    private boolean guiactivity = true;

    ConfigSystem configSystem;

    public void rewire_InputMultiplexer(InputMultiplexerState currentState){
        inputMultiplexerState = currentState;
        switch(currentState){
            case GAME:
                inputMultiplexer = new InputMultiplexer();
                inputMultiplexer.addProcessor(controllerUserInterface.getStageUserInterface());
                inputMultiplexer.addProcessor(this);
                Gdx.input.setInputProcessor(inputMultiplexer);
                break;
            case STATS:
                inputMultiplexer = new InputMultiplexer();
                inputMultiplexer.addProcessor(controllerUserInterface.getStagePopupStats());
                Gdx.input.setInputProcessor(inputMultiplexer);
                break;
            case OVERVIEW:
                inputMultiplexer = new InputMultiplexer();
           //     inputMultiplexer.addProcessor(controllerUserInterface.getOverviewPopupStats());
                Gdx.input.setInputProcessor(inputMultiplexer);
                break;
            case MENU:
                /*
                ...
                 */
                break;
            case MAINMENU:
                inputMultiplexer = new InputMultiplexer();
                inputMultiplexer.addProcessor(controllerUserInterfaceMenu.getStageMainMenu());
            //    inputMultiplexer.addProcessor(this);
                Gdx.input.setInputProcessor(inputMultiplexer);
                break;
            case BUILD:
                inputMultiplexer = new InputMultiplexer();
                inputMultiplexer.addProcessor(controllerUserInterface.getStageBuild());
                inputMultiplexer.addProcessor(controllerUserInterface.getStageUserInterface());
                Gdx.input.setInputProcessor(inputMultiplexer);
                break;
        }
    }

    @Override
    public void create() {

        try {
            configSystem = new ConfigSystem();
        } catch(IOException ioexc){
            System.out.println("configsystem initialization crashed.");
            ioexc.getCause();
            ioexc.getMessage();
        }
     //   gameConfig = new GameConfig();
        guibatch = new SpriteBatch();
        font = new BitmapFont();
        font2 = new BitmapFont(Gdx.files.internal("version.fnt"));
        font2.setColor(1f, 0.6f, 0.9f, 1f);
        font.setScale(1.75f);
        font.setColor(1f, 0.35f, 0f, 0.65f);
        Gdx.input.setInputProcessor(this);
      //  inputMultiplexer = new InputMultiplexer();
     //   inputMultiplexer.addProcessor(controllerUserInterface.getStageUserInterface());
     //   inputMultiplexer.addProcessor(this);
     //   Gdx.input.setInputProcessor(inputMultiplexer);
        toolbox = new Toolbox();
        toolbox.startTimer();

        //GSTATE

        //mandatory switch to Main Menu State:
        createMenuState();
        gameState.setActualState(GameState.ActualGameState.MAINMENU);
        inputMultiplexerState = InputMultiplexerState.MAINMENU;
        rewire_InputMultiplexer(inputMultiplexerState);
        //   createGameState();

        /*
        int playernumber = 2;
        hexmapdimensions = new Vector2Integer(45, 45);
        initializerHexmap = new InitializerHexmap(hexmapdimensions, playernumber);
        hexmap = initializerHexmap.newinit(playernumber);
        builder = new Builder(initializerHexmap);
        modelTurns = new ModelTurns(playernumber);
        modelTurns.addHexmap(hexmap);
        modelTurns.start();
        viewRender = new ViewRender(hexmap);
        initializerHexmap.addViewRender(viewRender);
        controllerMouseInput = new ControllerMouseInput(viewRender.camera, viewRender.cameralocation); //default constructor - tilesize = 50px.
        modelActorEditor = new ModelActorEditor(hexmap, hexmapdimensions, controllerMouseInput, modelTurns);
        viewRender.addControllerMouseInput(controllerMouseInput);
        controllerMouseInput.addControllerActorMovement(modelActorEditor);
        modelActorEditor.addControllerRender(viewRender);
        controllerUserInterface = new ControllerUserInterface();
        controllerUserInterface.inject_controllers(this);
        controllerUserInterface.inject_hexmap(hexmap);
     //   controllerActorMovement.actionMovement_injector(controllerGui.getActionMoveSet());
     //   controllerActorMovement.actionCombat_injector(controllerGui.getActionCombatSet());
        controllerUserInterface.initialize();
        controllerUserInterface.inject_builder(builder);

        viewRender.addCivMainComponent(this);

      //  controllerUserInterfaceMenu.
      */

    }

    public void createGameState(){


        int playernumber = Integer.decode(configSystem.getPlayerno());
        hexmapdimensions = new Vector2Integer(45, 45);
        initializerHexmap = new InitializerHexmap(hexmapdimensions, playernumber);
        int terrmod = 1;
        if(configSystem.getGcLandType() == ConfigSystem.GCLandType.GRASS) terrmod = 1;
        else if(configSystem.getGcLandType() == ConfigSystem.GCLandType.DES) terrmod = 3;
        else if(configSystem.getGcLandType() == ConfigSystem.GCLandType.ROCKY) terrmod = 5;
        hexmap = initializerHexmap.newinit(playernumber, terrmod);
        builder = new Builder(initializerHexmap);
        modelTurns = new ModelTurns(Integer.decode(configSystem.getTurncount()));
        modelTurns.addHexmap(hexmap);
        modelTurns.initplayerlist();
        modelTurns.start();
        viewRender = new ViewRender(hexmap);
        initializerHexmap.addViewRender(viewRender);
        controllerMouseInput = new ControllerMouseInput(viewRender.camera, viewRender.cameralocation); //default constructor - tilesize = 50px.
        modelActorEditor = new ModelActorEditor(hexmap, hexmapdimensions, controllerMouseInput, modelTurns);
        viewRender.addControllerMouseInput(controllerMouseInput);
        controllerMouseInput.addControllerActorMovement(modelActorEditor);
        modelActorEditor.addControllerRender(viewRender);
        controllerUserInterface = new ControllerUserInterface();
        controllerUserInterface.inject_controllers(this);
        controllerUserInterface.inject_hexmap(hexmap);
        //   controllerActorMovement.actionMovement_injector(controllerGui.getActionMoveSet());
        //   controllerActorMovement.actionCombat_injector(controllerGui.getActionCombatSet());
        controllerUserInterface.initialize();
        controllerUserInterface.inject_builder(builder);
        controllerUserInterface.updateguiactivity(guiactivity);
        viewRender.addGUI(controllerUserInterface);

        viewRender.addCivMainComponent(this);
        gameState.setStateGameCreated(true);
        inputMultiplexerState = InputMultiplexerState.GAME;
        rewire_InputMultiplexer(inputMultiplexerState);

        //  civMain.rewire_InputMultiplexer(CivMain.InputMultiplexerState.GAME);
        gameState.setActualState(GameState.ActualGameState.GAME);
        configSystem.updateStats();
   /*
        int playernumber = 2;
        hexmapdimensions = new Vector2Integer(45, 45);
        initializerHexmap = new InitializerHexmap(hexmapdimensions, playernumber);
        hexmap = initializerHexmap.newinit(playernumber);
        builder = new Builder(initializerHexmap);
        modelTurns = new ModelTurns(playernumber);
        modelTurns.addHexmap(hexmap);
        modelTurns.initplayerlist();
        modelTurns.start();
        viewRender = new ViewRender(hexmap);
        initializerHexmap.addViewRender(viewRender);
        controllerMouseInput = new ControllerMouseInput(viewRender.camera, viewRender.cameralocation); //default constructor - tilesize = 50px.
        modelActorEditor = new ModelActorEditor(hexmap, hexmapdimensions, controllerMouseInput, modelTurns);
        viewRender.addControllerMouseInput(controllerMouseInput);
        controllerMouseInput.addControllerActorMovement(modelActorEditor);
        modelActorEditor.addControllerRender(viewRender);
        controllerUserInterface = new ControllerUserInterface();
        controllerUserInterface.inject_controllers(this);
        controllerUserInterface.inject_hexmap(hexmap);
        //   controllerActorMovement.actionMovement_injector(controllerGui.getActionMoveSet());
        //   controllerActorMovement.actionCombat_injector(controllerGui.getActionCombatSet());
        controllerUserInterface.initialize();
        controllerUserInterface.inject_builder(builder);

        viewRender.addCivMainComponent(this);
        gameState.setStateGameCreated(true);
        inputMultiplexerState = InputMultiplexerState.GAME;
        rewire_InputMultiplexer(inputMultiplexerState);

        //  civMain.rewire_InputMultiplexer(CivMain.InputMultiplexerState.GAME);
        gameState.setActualState(GameState.ActualGameState.GAME);
            */
    }

    public void createMenuState(){
        gameState.setStateMenuCreated(true);
        controllerUserInterfaceMenu = new ControllerUserInterfaceMenu(configSystem);
        controllerUserInterfaceMenu.init();
        controllerUserInterfaceMenu.addCivMainComponent(this);


    }


    public void createMultiplayerState(){
        /*
        System.out.print("create multi");
        gameState.setStateMultiCreated(true);
        CivClient launcher = new CivClient();
        */
    }

    @Override
    public void render() {
        if(gameState.getActualState() == GameState.ActualGameState.MAINMENU){
             //   this.rewire_InputMultiplexer(InputMultiplexerState.MAINMENU);
                Gdx.gl.glClearColor(0, 0, 0, 1);
                Gdx.gl.glClearDepthf(1);
                Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

                Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
                Gdx.gl.glDepthFunc(GL20.GL_ALWAYS);
                guibatch.enableBlending();
                    if(controllerUserInterfaceMenu.isStateChanged()){
                        controllerUserInterfaceMenu.setStateChanged(false);
                        rewire_InputMultiplexer(InputMultiplexerState.MAINMENU);
                    }
             //   controllerMouseInput.listener();
                guibatch.begin();
                controllerUserInterfaceMenu.drawbackground(guibatch);

                //     Diagnostic();
//                controllerUserInterface.getBackgroundBlackFog().draw(guibatch, 0.80f);
                versionSig();

                if (controllerUserInterfaceMenu.getDustfront().isComplete()){
                    controllerUserInterfaceMenu.getDustfront().reset();
                    controllerUserInterfaceMenu.getDustfront().start();
                }
             //   controllerUserInterfaceMenu.getDustfront().update(Gdx.graphics.getDeltaTime());
                controllerUserInterfaceMenu.getDustfront().draw(guibatch, Gdx.graphics.getDeltaTime());
                guibatch.end();
                controllerUserInterfaceMenu.getStageMainMenu().act(Gdx.graphics.getDeltaTime());
                controllerUserInterfaceMenu.getStageMainMenu().draw();


        } else if(gameState.getActualState() == GameState.ActualGameState.GAME){
         //   rewire_InputMultiplexer(InputMultiplexerState.GAME);
            controllerMouseInput.listener();
            viewRender.render();
            controllerUserInterface.unitStatsDialogUpdate();

            //GUI STUFF:
            guibatch.begin();
           // if(configSystem.isDiagboxenabler())
                 Diagnostic();
            controllerUserInterface.getBackgroundBlackFog().draw(guibatch, 0.80f);
            versionSig();

            guibatch.end();
            controllerUserInterface.getStageUserInterface().act(Gdx.graphics.getDeltaTime());
            controllerUserInterface.getStageUserInterface().draw();
            if(controllerUserInterface.isButtonStatsSet()) {
                //      controllerGui.getTablePopupStats().setVisible(true);
                controllerUserInterface.getStagePopupStats().act(Gdx.graphics.getDeltaTime());
                controllerUserInterface.getStagePopupStats().draw();
            }

            if(controllerUserInterface.isActionBuildSet()){
                controllerUserInterface.getStageBuild().act(Gdx.graphics.getDeltaTime());
                controllerUserInterface.getStageBuild().draw();
            }
            //END OF GUI STUFF.

        }
    }

    //________________________
    //_________________________

    //diagnostic tool
    //printing more relevant and fragile app output on the screen
    private void Diagnostic() {
        String [] sbuilder = new String[10];
        sbuilder[0] ="ControllerMouseInput diag: \n";
        sbuilder[1] ="\n lmbClicked(in cmi): " + controllerMouseInput.lmbClicked;
        sbuilder[2] ="\nlmbClickedVector:" + controllerMouseInput.lmbClickedVector.printer();
        sbuilder[3] ="\nrmbClickedVector:" + controllerMouseInput.rmbClickedVector.printer();
        sbuilder[4] = "mouseHooverCoords:" + controllerMouseInput.mouseHooverCoords.printer();
        sbuilder[5] ="tileHooverCoords:" + controllerMouseInput.tileHooverCoords.printer();
        sbuilder[6] ="lmbHexSelectedBoolean: " + controllerMouseInput.lmbHexSelected;
        sbuilder[7] ="lmbHexSelectedCoords:" + controllerMouseInput.lmbHexSelectedVector.printer();
        sbuilder[8] = "camzoom: " + controllerMouseInput.camerazoom + "//camlocation:" + controllerMouseInput.cameralocation.printer();
        sbuilder[9] = "SelectedHex (selectionType): " + modelActorEditor.selection;

        for (int i = 0 ; i < sbuilder.length; ++i)
            font.draw(guibatch, sbuilder[i], 1250, (1040 - i*30));

        String diag = new String();
        diag = "ActionMoveSet: " + modelActorEditor.isActionMoveSet + " // backlightEnabler " + viewRender.backlightEnabler;
        font.draw(guibatch, diag, 1250, 735);
        diag = "Active PlayerENUM enum: " + modelTurns.getActivePlayer();
        font.draw(guibatch, diag, 1250, 705);
        diag = "Main Loop Update Time: " + Gdx.graphics.getRawDeltaTime() + " / FPS: " + Gdx.graphics.getFramesPerSecond();
        font.draw(guibatch, diag, 1250, 675);
        diag = "ControllerTurns diagnostic: " + "turns:" + modelTurns.turnNumber +", global turns: "+ modelTurns.globalturnNumber;
        font.draw(guibatch, diag, 1250, 645);
        diag = "[0,0] tile diag: CivMain: " + hexmap[0][0].actorList.size() + ", ControllerActorMovement: " + modelActorEditor.hexmap[0][0].actorList.size();
        font.draw(guibatch, diag, 1250, 615);
        diag = "lmbClickedVectorStack.get(0)";
        String diag2 = new String();
        switch(controllerMouseInput.lmbHexSelectedStack.size()){
            case 0: diag2 = "size = 0, wtf";
                font.draw(guibatch, diag+diag2, 1250, 585);
                break;
            case 2: diag2 = "size2: " + controllerMouseInput.lmbHexSelectedStack.get(1).printer();
                font.draw(guibatch, diag+diag2, 1250, 585);
                diag2 = "size1: " + controllerMouseInput.lmbHexSelectedStack.get(0).printer();
                font.draw(guibatch, diag+diag2, 1250, 555);
                break;
            case 1: diag2 = "size1: " + controllerMouseInput.lmbHexSelectedStack.get(0).printer();
                font.draw(guibatch, diag+diag2, 1250, 585);
                break;
            default: diag2 = "cos zjebano, za duzy (za maly?) size VectorStacka";
                font.draw(guibatch, diag+diag2, 1250, 585);
                break;
        }

        diag = "Time(millis):" + toolbox.getTimemillis() + " / Time (secs): " + toolbox.getTimeseconds();
        font.draw(guibatch, diag, 1250, 525);

        diag = "viewrender cam: " + viewRender.cameralocation.printer();
        font.draw(guibatch, diag, 1250, 495);
    }

    private void versionSig(){
        String version = new String();
        String shipping = new String();
        String lib = new String();
        version = "0.2.2 SINGLEPLAYER ONLY BUILD";
        shipping = "JSDK 1.8 + ANDROID NDK 4.4";
        lib = "gdx 1.6 on opengl2.0";
        font2.setScale(0.68f);
        font2.draw(guibatch, version, Gdx.graphics.getWidth() * 0.65f, Gdx.graphics.getHeight() * 0.2f);
        font2.setScale(0.65f);
        font2.draw(guibatch, shipping, Gdx.graphics.getWidth() * 0.65f, Gdx.graphics.getHeight() * 0.2f - 50);
        font2.setScale(0.50f);
        font2.draw(guibatch, lib, Gdx.graphics.getWidth() * 0.65f, Gdx.graphics.getHeight() * 0.2f - 100);
    }

    public boolean isGuiactivity() {
        return guiactivity;
    }

    public void setGuiactivity(boolean guiactivity) {
        this.guiactivity = guiactivity;
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.Q || keycode == Input.Keys.E || keycode == Input.Keys.R) {
            viewRender.zoommapKeyPressed = true;
            viewRender.zoommapKeyType = keycode;
            return true;
        }

        if(keycode == Input.Keys.W || keycode == Input.Keys.S || keycode == Input.Keys.A || keycode == Input.Keys.D) {
            viewRender.movemapKeyPressed = true;
            viewRender.movemapKeyType = keycode;
            return true;
        }

        if(keycode == Input.Keys.ENTER && guiactivity){
          //  viewRender.screenshadeactivate();
            if(gameState.getActualState() == GameState.ActualGameState.GAME) {
                gameState.setActualState(GameState.ActualGameState.MAINMENU);
                rewire_InputMultiplexer(InputMultiplexerState.MAINMENU);
            }
            else if (gameState.getActualState() == GameState.ActualGameState.MAINMENU) {
                gameState.setActualState(GameState.ActualGameState.GAME);
                rewire_InputMultiplexer(InputMultiplexerState.GAME);
            }
        }

        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        if(keycode == Input.Keys.W || keycode == Input.Keys.S || keycode == Input.Keys.A || keycode == Input.Keys.D) {
            viewRender.movemapKeyPressed = false;
            return true;
        }
        if (keycode == Input.Keys.E || keycode == Input.Keys.Q || keycode == Input.Keys.R) {
            viewRender.zoommapKeyPressed = false;
            return true;
        }

        return false;
    }

    //handling selection, should be here
    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (guiactivity) {
            controllerMouseInput.mouseClick(button);
            if (controllerUserInterface.getActionMoveSet()) {
                //    controllerActorMovement.actionMovement_prealpha(controllerMouseInput.lmbHexSelectedStack.get(0), controllerMouseInput.lmbHexSelectedStack.get(1));
                controllerUserInterface.setActionMoveSet(false);
                viewRender.addToDiscoveredArray(controllerMouseInput.lmbHexSelectedStack.get(0));
                modelActorEditor.actionMovement_trigger(controllerUserInterface.getActionMoveSet());
                modelActorEditor.actionMovement_prealpha(controllerMouseInput.lmbHexSelectedStack.get(0), controllerMouseInput.lmbHexSelectedStack.get(1));
            }

            if (controllerUserInterface.getActionCombatSet() &&
                    hexmap[controllerMouseInput.lmbHexSelectedStack.get(0).x()][controllerMouseInput.lmbHexSelectedStack.get(0).y()].getLastUnitActor().movementPoints > 0) {
                controllerUserInterface.setActionCombatSet(false);
                viewRender.addToDiscoveredArray(controllerMouseInput.lmbHexSelectedStack.get(0));
                modelActorEditor.actionCombat_trigger(controllerUserInterface.getActionCombatSet());
                modelActorEditor.actionCombat_action(controllerMouseInput.lmbHexSelectedStack.get(0), controllerMouseInput.lmbHexSelectedStack.get(1));
                hexmap[controllerMouseInput.lmbHexSelectedStack.get(0).x()][controllerMouseInput.lmbHexSelectedStack.get(0).y()].getLastUnitActor().decreaseMovePtsZero();
                if (hexmap[controllerMouseInput.lmbHexSelectedStack.get(1).x()][controllerMouseInput.lmbHexSelectedStack.get(1).y()].getLastUnitActor().hitPoints <= 0)
                    hexmap[controllerMouseInput.lmbHexSelectedStack.get(1).x()][controllerMouseInput.lmbHexSelectedStack.get(1).y()].actorList
                            .remove(hexmap[controllerMouseInput.lmbHexSelectedStack.get(1).x()][controllerMouseInput.lmbHexSelectedStack.get(1).y()].actorList.size() - 1);
            }

            if (controllerUserInterface.isActionCombatRangedSet()
                    && hexmap[controllerMouseInput.lmbHexSelectedStack.get(0).x()][controllerMouseInput.lmbHexSelectedStack.get(0).y()].getLastUnitActor().movementPoints > 0
                    && hexmap[controllerMouseInput.lmbHexSelectedStack.get(0).x()][controllerMouseInput.lmbHexSelectedStack.get(0).y()].getLastUnitActor().attackRanged) {
                controllerUserInterface.setActionCombatRangedSet(false);
                viewRender.addToDiscoveredArray(controllerMouseInput.lmbHexSelectedStack.get(0));
                modelActorEditor.actionCombatRanged_action(controllerMouseInput.lmbHexSelectedStack.get(0), controllerMouseInput.lmbHexSelectedStack.get(1));
                hexmap[controllerMouseInput.lmbHexSelectedStack.get(0).x()][controllerMouseInput.lmbHexSelectedStack.get(0).y()].getLastUnitActor().decreaseMovePtsZero();
                if (hexmap[controllerMouseInput.lmbHexSelectedStack.get(1).x()][controllerMouseInput.lmbHexSelectedStack.get(1).y()].getLastUnitActor().hitPoints <= 0)
                    hexmap[controllerMouseInput.lmbHexSelectedStack.get(1).x()][controllerMouseInput.lmbHexSelectedStack.get(1).y()].actorList
                            .remove(hexmap[controllerMouseInput.lmbHexSelectedStack.get(1).x()][controllerMouseInput.lmbHexSelectedStack.get(1).y()].actorList.size() - 1);
            }

            return true;
        } return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return true;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
package com.szparag.ijtest1;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.szparag.ijtest1.Configurators.ConfigSystem;
import com.szparag.ijtest1.Configurators.GameConfig;

import java.io.*;
import java.net.Socket;
import java.util.Random;

public class CivClient extends ApplicationAdapter implements Runnable, InputProcessor {

    private NetworkPackage actualPackage;

    //_________________________________________________
    //Networking fields:
    private Socket socket              = null;
    private Thread thread              = null;
    private ObjectInputStream streamIn = null;
    private ObjectOutputStream streamOut = null;
    private CivClientThread client = null;
    //   private CivNetworkPacket civNetworkPacket;
    //   private CivNetworkPacket.PacketState packetState;
    private Random rand;
    private boolean mapModificated = false;
    private boolean firstPacketwasReceived = false;
    //   private boolean firstHexmapUpdate = false;
    boolean initialized = false;

    private enum FirstPacketState{
        OFFLINE, CONSTRUCTED, FIRSTSEND, RECEIVEDHEXMAP, CONSTRUCTEDCONTROLLERS, ONLINE
    }
    FirstPacketState firstPacketState = FirstPacketState.OFFLINE;
    //_________________________________________________


    //_________________________________________________
    //Game model fields:
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
    private boolean guiactivity = false;
    private PlayerENUM clientPlayerEnum = PlayerENUM.NOTINITIALISED;
    private boolean turnchange = false;
    ConfigSystem configSystem;



    //network stuff:
    //____________________________________________________________
    public static ObjectOutputStream createObjectStreamOut(Socket socket) throws IOException {
        OutputStream stream = socket.getOutputStream();
        return new ObjectOutputStream(stream);
    }

    public void handle(Object object) {
        //   if (!firstPacketwasReceived && object instanceof NetworkPackage) {
        //      firstPacketwasReceived = true;
        //       actualPackage = (NetworkPackage)object;
        //       actualPackage.packageStatePrinter();
        //   } else {
        if(object instanceof NetworkPackage) {
            System.out.println(Long.toString(System.nanoTime()) + " CivClient: packet incoming handle(); ");
            actualPackage = (NetworkPackage) object;
            actualPackage.packageStatePrinter();
            modelTurns.setActualPlayer(actualPackage.getActivePlayer());
            //      hexmap = ((NetworkPackage) object).getHexmap();
            hexmap = actualPackage.getHexmap();
            updatemap(hexmap);
            modelTurns.setActualPlayer(clientPlayerEnum);
            //  actualPackage = (NetworkPackage) object;
            //  actualPackage.updateMap(((NetworkPackage) object).getHexmap());
            //   actualPackage.packageStatePrinter();
            //   this.updatemap(actualPackage.getHexmap());
        }
        if (object instanceof Boolean) {
            guiactivity = (boolean) object;
            controllerUserInterface.updateguiactivity(guiactivity);
        }
        if (object instanceof PlayerENUM) {
            clientPlayerEnum = (PlayerENUM) object;
            modelTurns.setActualPlayer(clientPlayerEnum);
        }
        if(object instanceof Integer){
            if ((int)object == 1) modelTurns.setActualPlayer(PlayerENUM.playerONE);
            else if ((int)object == 2) modelTurns.setActualPlayer(PlayerENUM.playerTWO);
        }
        modelTurns.setActualPlayer(clientPlayerEnum);
    }

    public void stop() throws IOException {
        System.out.println(Long.toString(System.nanoTime()) + " CivClient: stop();");
        if(socket != null)
            socket.close();
        if(streamOut != null)
            streamOut.close();
        if(streamIn != null)
            streamIn.close();
    }

    public void updatemap(Hex[][] hexmap){
        initializerHexmap.updatemap(hexmap);
        modelTurns.updatemap(hexmap);
        viewRender.updatemap(hexmap);
        modelActorEditor.updatemap(hexmap);
        controllerUserInterface.updatemap(hexmap);
    }


    //graphic stuff:
    //____________________________________________________________

    //
    @Override
    public void create() {
        // creation();
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
        //
        inputMultiplexerState = InputMultiplexerState.MAINMENU;
        rewire_InputMultiplexer(inputMultiplexerState);

    }

    @Override
    public void run() {
        System.out.println(Long.toString(System.nanoTime()) + " CivClient: run();");
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


        }
        else if(gameState.getActualState() == GameState.ActualGameState.GAMEMULTI) {
            if(this.turnchange){
                actualPackage.setChangeturn(true);
                this.setTurnchange(false);
                try {
                    streamOut.writeObject(actualPackage);
                } catch(Exception e){}
            }
            if (modelActorEditor.isHexmapChanged()) {
                try {
                    System.out.println(Long.toString(System.nanoTime()) + "hexmap was modified from here.");
                    hexmap = modelActorEditor.hexmap;
                    actualPackage.setHexmap(this.hexmap);
                    actualPackage.packageStatePrinter();
                    streamOut.writeObject(actualPackage);
                    modelActorEditor.setHexmapChanged(false);
                    modelTurns.setIshexmapchanged(false);
                } catch (IOException exc) {
                    exc.printStackTrace();
                    System.err.println(exc.getCause());
                    System.err.println(exc.getMessage());
                }
            }
            if(turnchange){
                actualPackage.setChangeturn(true);
                actualPackage.setActivePlayer(modelTurns.getActivePlayer());
            }


            controllerMouseInput.listener();
            viewRender.render();
            controllerUserInterface.unitStatsDialogUpdate();

            //GUI STUFF:
            guibatch.begin();
            Diagnostic();
            controllerUserInterface.getBackgroundBlackFog().draw(guibatch, 0.80f);
            versionSig();
            guibatch.end();
            controllerUserInterface.getStageUserInterface().act(Gdx.graphics.getDeltaTime());
            controllerUserInterface.getStageUserInterface().draw();
            if (controllerUserInterface.isButtonStatsSet()) {
                controllerUserInterface.getStagePopupStats().act(Gdx.graphics.getDeltaTime());
                controllerUserInterface.getStagePopupStats().draw();
            }

            if (controllerUserInterface.isActionBuildSet()) {
                controllerUserInterface.getStageBuild().act(Gdx.graphics.getDeltaTime());
                controllerUserInterface.getStageBuild().draw();
            }
            //END OF GUI STUFF.
        }
    }

    private void creation() {
        int playernumber = Integer.decode(configSystem.getPlayerno());
        hexmapdimensions = new Vector2Integer(45, 45);
        initializerHexmap = new InitializerHexmap(hexmapdimensions, playernumber);
        int terrmod = 1;
        if(configSystem.getGcLandType() == ConfigSystem.GCLandType.GRASS) terrmod = 1;
        else if(configSystem.getGcLandType() == ConfigSystem.GCLandType.DES) terrmod = 3;
        else if(configSystem.getGcLandType() == ConfigSystem.GCLandType.ROCKY) terrmod = 5;
        hexmap = initializerHexmap.newinit(playernumber, 9);
        builder = new Builder(initializerHexmap);
        modelTurns = new ModelTurns(Integer.decode(configSystem.getTurncount()));
        modelTurns.addHexmap(hexmap);
        modelTurns.initplayerlist();
        modelTurns.start();
        modelTurns.instantiateMultiplayer(this);
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

        //   viewRender.addCivMainComponent(this);
        gameState.setStateGameCreated(true);
        inputMultiplexerState = InputMultiplexerState.GAME;
        rewire_InputMultiplexer(inputMultiplexerState);

        //  civMain.rewire_InputMultiplexer(CivMain.InputMultiplexerState.GAME);
        gameState.setActualState(GameState.ActualGameState.GAME);
        configSystem.updateStats();
    }

    private void versionSig(){
        String version = new String();
        String shipping = new String();
        String lib = new String();
        version = "0.2.2 MULTIPLAYER ONLY BUILD";
        shipping = "JSDK 1.8 + ANDROID SDK 4.4";
        lib = "gdx 1.6 on opengl2.0";
        font2.setScale(0.68f);
        font2.draw(guibatch, version, Gdx.graphics.getWidth() * 0.65f, Gdx.graphics.getHeight() * 0.2f);
        font2.setScale(0.65f);
        font2.draw(guibatch, shipping, Gdx.graphics.getWidth() * 0.65f, Gdx.graphics.getHeight() * 0.2f - 50);
        font2.setScale(0.50f);
        font2.draw(guibatch, lib, Gdx.graphics.getWidth() * 0.65f, Gdx.graphics.getHeight() * 0.2f - 100);
    }

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

        diag = "MULTI: guiactivity: " + this.guiactivity + "/client: " + this.clientPlayerEnum.toString();
        font.draw(guibatch,diag, 1250, 465);

        diag = "MULTI: active: " + modelTurns.getActualPlayer().toString();
        font.draw(guibatch, diag, 1250, 435);
    }

    //game logic stuff:
    //____________________________________________________________
    public void createGameState(){

    }

    public void createMenuState(){
        gameState.setStateMenuCreated(true);
        controllerUserInterfaceMenu = new ControllerUserInterfaceMenu(configSystem);
        controllerUserInterfaceMenu.init();
        controllerUserInterfaceMenu.addCivMainComponent(this);
    }

    public void createMultiState(){
        try {
            creation();
            System.out.println(Long.toString(System.nanoTime()) + " CivClient: constructor;");
            rand = new Random();
            //creating package + creating map
            actualPackage = new NetworkPackage();
            actualPackage.packageStatePrinter();
            //getting map
            hexmap = actualPackage.getHexmap();
            socket = new Socket(configSystem.getHost(), configSystem.getPort());
            streamOut = createObjectStreamOut(socket);
            if (thread == null) {
                client = new CivClientThread(this, socket);
                thread = new Thread(this);
                thread.start();

            }
        } catch (IOException exc) {
            exc.printStackTrace();
            System.err.println(exc.getCause());
            System.err.println(exc.getMessage());
        }

        gameState.setActualState(GameState.ActualGameState.GAMEMULTI);
    }

    public void setTurnchange(boolean turnchange) {
        this.turnchange = turnchange;
    }

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


    //region Methods overriden from interface InputProcessor
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

        if(keycode == Input.Keys.ENTER){
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
        controllerMouseInput.mouseClick(button);
        if(controllerUserInterface.getActionMoveSet()) {
            //    controllerActorMovement.actionMovement_prealpha(controllerMouseInput.lmbHexSelectedStack.get(0), controllerMouseInput.lmbHexSelectedStack.get(1));
            controllerUserInterface.setActionMoveSet(false);
            viewRender.addToDiscoveredArray(controllerMouseInput.lmbHexSelectedStack.get(0));
            modelActorEditor.actionMovement_trigger(controllerUserInterface.getActionMoveSet());
            modelActorEditor.actionMovement_prealpha(controllerMouseInput.lmbHexSelectedStack.get(0), controllerMouseInput.lmbHexSelectedStack.get(1));
        }

        if(controllerUserInterface.getActionCombatSet() &&
                hexmap[controllerMouseInput.lmbHexSelectedStack.get(0).x()][controllerMouseInput.lmbHexSelectedStack.get(0).y()].getLastUnitActor().movementPoints > 0){
            controllerUserInterface.setActionCombatSet(false);
            viewRender.addToDiscoveredArray(controllerMouseInput.lmbHexSelectedStack.get(0));
            modelActorEditor.actionCombat_trigger(controllerUserInterface.getActionCombatSet());
            modelActorEditor.actionCombat_action(controllerMouseInput.lmbHexSelectedStack.get(0), controllerMouseInput.lmbHexSelectedStack.get(1));
            hexmap[controllerMouseInput.lmbHexSelectedStack.get(0).x()][controllerMouseInput.lmbHexSelectedStack.get(0).y()].getLastUnitActor().decreaseMovePtsZero();
            if(hexmap[controllerMouseInput.lmbHexSelectedStack.get(1).x()][controllerMouseInput.lmbHexSelectedStack.get(1).y()].getLastUnitActor().hitPoints <= 0)
                hexmap[controllerMouseInput.lmbHexSelectedStack.get(1).x()][controllerMouseInput.lmbHexSelectedStack.get(1).y()].actorList
                        .remove(hexmap[controllerMouseInput.lmbHexSelectedStack.get(1).x()][controllerMouseInput.lmbHexSelectedStack.get(1).y()].actorList.size()-1);
        }

        if(controllerUserInterface.isActionCombatRangedSet()
                && hexmap[controllerMouseInput.lmbHexSelectedStack.get(0).x()][controllerMouseInput.lmbHexSelectedStack.get(0).y()].getLastUnitActor().movementPoints > 0
                && hexmap[controllerMouseInput.lmbHexSelectedStack.get(0).x()][controllerMouseInput.lmbHexSelectedStack.get(0).y()].getLastUnitActor().attackRanged){
            controllerUserInterface.setActionCombatRangedSet(false);
            viewRender.addToDiscoveredArray(controllerMouseInput.lmbHexSelectedStack.get(0));
            modelActorEditor.actionCombatRanged_action(controllerMouseInput.lmbHexSelectedStack.get(0), controllerMouseInput.lmbHexSelectedStack.get(1));
            hexmap[controllerMouseInput.lmbHexSelectedStack.get(0).x()][controllerMouseInput.lmbHexSelectedStack.get(0).y()].getLastUnitActor().decreaseMovePtsZero();
            if(hexmap[controllerMouseInput.lmbHexSelectedStack.get(1).x()][controllerMouseInput.lmbHexSelectedStack.get(1).y()].getLastUnitActor().hitPoints <= 0)
                hexmap[controllerMouseInput.lmbHexSelectedStack.get(1).x()][controllerMouseInput.lmbHexSelectedStack.get(1).y()].actorList
                        .remove(hexmap[controllerMouseInput.lmbHexSelectedStack.get(1).x()][controllerMouseInput.lmbHexSelectedStack.get(1).y()].actorList.size()-1);
        }

        return true;
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
    //endregion



    //inputProcessor implementation:
    //________________________________________________________________________________________________________
    //________________________________________________________________________________________________________

    /**
     * Called when a key was pressed
     *
     * @param keycode one of the constants in {@link Input.Keys}
     * @return whether the input was processed
     */
    /*

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
        return false;
    }

    /**
     * Called when a key was released
     *
     * @param keycode one of the constants in {@link Input.Keys}
     * @return whether the input was processed
     */
    /*
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

    /**
     * Called when a key was typed
     *
     * @param character The character
     * @return whether the input was processed
     */
    //  @Override
    /*
    public boolean keyTyped(char character) {
        return false;
    }

    /**
     * Called when the screen was touched or a mouse button was pressed. The button parameter will be {@link Buttons#LEFT} on iOS.
     *
     * @param screenX The x coordinate, origin is in the upper left corner
     * @param screenY The y coordinate, origin is in the upper left corner
     * @param pointer the pointer for the event.
     * @param button  the button
     * @return whether the input was processed
     */
    /*
    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        controllerMouseInput.mouseClick(button);
        if(controllerUserInterface.getActionMoveSet()) {
            //    controllerActorMovement.actionMovement_prealpha(controllerMouseInput.lmbHexSelectedStack.get(0), controllerMouseInput.lmbHexSelectedStack.get(1));
            controllerUserInterface.setActionMoveSet(false);
            modelActorEditor.actionMovement_trigger(controllerUserInterface.getActionMoveSet());
            modelActorEditor.actionMovement_prealpha(controllerMouseInput.lmbHexSelectedStack.get(0), controllerMouseInput.lmbHexSelectedStack.get(1));
        }
        return true;
    }

    /**
     * Called when a finger was lifted or a mouse button was released. The button parameter will be {@link Buttons#LEFT} on iOS.
     *
     * @param screenX
     * @param screenY
     * @param pointer the pointer for the event.
     * @param button  the button   @return whether the input was processed
     */
    /*
    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    /**
     * Called when a finger or the mouse was dragged.
     *
     * @param screenX
     * @param screenY
     * @param pointer the pointer for the event.  @return whether the input was processed
     */
    /*
    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    /**
     * Called when the mouse was moved without any buttons being pressed. Will not be called on iOS.
     *
     * @param screenX
     * @param screenY
     * @return whether the input was processed
     */
    /*
    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    /**
     * Called when the mouse wheel was scrolled. Will not be called on iOS.
     *
     * @param amount the scroll amount, -1 or 1 depending on the direction the wheel was scrolled.
     * @return whether the input was processed.
     */
    /*
    @Override
    public boolean scrolled(int amount) {
        return false;
    }
*/
}

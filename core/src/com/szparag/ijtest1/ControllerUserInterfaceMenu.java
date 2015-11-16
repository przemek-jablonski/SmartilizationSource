package com.szparag.ijtest1;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.szparag.ijtest1.Configurators.ConfigSystem;
import com.szparag.ijtest1.Configurators.GameConfig;

import java.io.IOException;
import java.util.LinkedList;


/**
 * Created by Szparagowy Krul 3000 on 07/06/2015.
 */
public class ControllerUserInterfaceMenu extends ApplicationAdapter{
    Texture[] backgroundt;
    ParticleEffect background1 = new ParticleEffect();
    ParticleEffect background2 = new ParticleEffect();
    private ParticleEffect dustback = new ParticleEffect();
    private ParticleEffect dustback2 = new ParticleEffect();
    private ParticleEffect dustback3 = new ParticleEffect();
    private ParticleEffect dustfront = new ParticleEffect();
    Array<ParticleEffectPool.PooledEffect> arr1 = new Array();
    Array<ParticleEffectPool.PooledEffect> arr2 = new Array();
    ParticleEffectPool background1pool;
    ParticleEffectPool background2pool;
    private Skin userinterfaceskin;
    private Stage stageMainMenu;
    private Table tableMainMenu;
    private Table tablegamesettingsp, tablegamesettingmp, tableconfig, tabledatabase, tableloadsure, tableexitsure;
    private MenuState menuState;
    private Toolbox toolbox;
    private boolean stateChanged = false;


    //new singleplayer gui components
    Label       spplayerno;
    TextButton  spplayer2;
    TextButton  spplayer4;
    Label       spmapsize;
    TextButton  spmaps;
    TextButton  spmapm;
    TextButton  spmapl;
    TextButton  spmapxl;
    Label       sptreasure;
    TextButton  sptreasnone;
    TextButton  sptreasmoder;
    TextButton  sptreasvast;
    Label       spturnnumber;
    TextButton  spturn20;
    TextButton  spturn40;
    TextButton  spturn60;
    TextButton  spturn100;
    Label       spgametype;
    TextButton  sptypeffa;
    TextButton  sptypeturn;
    Label       splandtype;
    TextButton  splandgrass;
    TextButton  splanddesert;
    TextButton  splandrocky;
    TextButton  spback;
    TextButton  spok;


    //new multiplayer gui components
    Label       mpplayerno;
    TextButton  mpplayer2;
    TextButton  mpplayer4;
    Label       mpmapsize;
    TextButton  mpmaps;
    TextButton  mpmapm;
    TextButton  mpmapl;
    TextButton  mpmapxl;
    Label       mptreasure;
    TextButton  mptreasnone;
    TextButton  mptreasmoder;
    TextButton  mptreasvast;
    Label       mpturnnumber;
    TextButton  mpturn20;
    TextButton  mpturn40;
    TextButton  mpturn60;
    TextButton  mpturn100;
    Label       mpgametype;
    TextButton  mptypeffa;
    TextButton  mptypeturn;
    Label       mplandtype;
    TextButton  mplandgrass;
    TextButton  mplanddesert;
    TextButton  mplandrocky;
    TextButton  mpback;
    TextButton  mpok;

    LinkedList<Actor> spbuttons = new LinkedList<>();
    LinkedList<Actor> mpbuttons = new LinkedList<>();

    enum ComponentSent{
        NONE, SINGLEMAIN, MULTICLIENT
    }
    ComponentSent componentSent = ComponentSent.NONE;
    CivMain civMain;
    CivClient civClient;
    ConfigSystem configSystem;

    public void addCivMainComponent(CivMain civmain){
        this.civMain = civmain;
        componentSent = ComponentSent.SINGLEMAIN;
    }

    public void addCivMainComponent(CivClient civClient){
        this.civClient = civClient;
        componentSent = ComponentSent.MULTICLIENT;
    }

    public enum MenuState{
        SPLASH, MAINMENU, GAMESETTINGSP, GAMESETTINGMP, CONFIG, DATABASE, LOADSURE, EXITSURE
    }

    public ControllerUserInterfaceMenu(ConfigSystem configSystem){
        this.configSystem = configSystem;
        toolbox = new Toolbox();
        toolbox.startTimer();
        menuState = MenuState.MAINMENU;
    }

    public void init(){
        backgroundt = new Texture[6];
        backgroundt[0] = new Texture(Gdx.files.internal("MAINMENUzabrocki/L1.png"));
        backgroundt[1] = new Texture(Gdx.files.internal("MAINMENUzabrocki/L2.png"));
        backgroundt[2] = new Texture(Gdx.files.internal("MAINMENUzabrocki/L3.png"));
        backgroundt[3] = new Texture(Gdx.files.internal("MAINMENUzabrocki/L4.png"));
        backgroundt[4] = new Texture(Gdx.files.internal("MAINMENUzabrocki/L5.png"));
        backgroundt[5] = new Texture(Gdx.files.internal("MAINMENUzabrocki/L6.png"));
        background1.load(Gdx.files.internal("godray_selections2/background1.p"), Gdx.files.internal("godray_selections2/"));
        background2.load(Gdx.files.internal("particlesdemo1/background2.p"), Gdx.files.internal("particlesdemo1/"));
        background1pool = new ParticleEffectPool(background1, 3, 4);
        background2pool = new ParticleEffectPool(background2, 3, 4);
        dustback.load(Gdx.files.internal("particlesdemo1/menudust.p"), Gdx.files.internal("particlesdemo1/"));
        dustback2.load(Gdx.files.internal("particlesdemo1/menudust2.p"), Gdx.files.internal("particlesdemo1/"));
        dustback3.load(Gdx.files.internal("particlesdemo1/menudust2.p"), Gdx.files.internal("particlesdemo1/"));
        dustfront.load(Gdx.files.internal("particlesdemo1/menudustfront.p"), Gdx.files.internal("particlesdemo1/"));
        dustback.setPosition(Gdx.graphics.getWidth()/2 + 250, Gdx.graphics.getHeight()/2 - 100);
        dustback2.setPosition(Gdx.graphics.getWidth()/2 + 100,Gdx.graphics.getHeight()/2 + 150);
        dustback3.setPosition(Gdx.graphics.getWidth()/2 -250,Gdx.graphics.getHeight()/2 - 150);
        dustback.start();
        dustback2.start();
        dustback3.start();
        dustfront.setPosition(Gdx.graphics.getWidth()/2 - 100, Gdx.graphics.getHeight()/2 + 100);
        dustfront.start();
        userinterfaceskin = new Skin(Gdx.files.internal("uiskin.json"));
        tableinit();
    }

    private void tableinit(){
        tablemainmenuinit();
        tablesettingspinit();
        tablesettingmpinit();
        listButtonsInit();
        stageUpdate();
        updateColours();
    }

    private void tablemainmenuinit(){
        tableMainMenu = new Table();
        TextButton newgamesp = new TextButton("New Singleplayer Game", userinterfaceskin);
        TextButton newgamemp = new TextButton("New Multiplayer Game", userinterfaceskin);
        TextButton loadgamesp = new TextButton("Load Singleplayer Game", userinterfaceskin);
        TextButton options = new TextButton("Configuration Options", userinterfaceskin);
        TextButton exit = new TextButton("Exit", userinterfaceskin);
        newgamesp.setColor(0.53f, 0.35f, 0.20f, 0.85f);
        newgamemp.setColor(0.53f, 0.35f, 0.20f, 0.85f);
        loadgamesp.setColor(0.53f, 0.35f, 0.20f, 0.85f);
        options.setColor(0.53f, 0.35f, 0.20f, 0.85f);
        exit.setColor(0.53f, 0.35f, 0.20f, 0.85f);
        tableMainMenu.add(newgamesp).size(350, 50).row();
        tableMainMenu.add(newgamemp).size(350, 50).row();
        tableMainMenu.add(loadgamesp).size(350, 50).row();
        tableMainMenu.add(options).size(350, 50).row();
        tableMainMenu.add(exit).size(350, 50).row();

        newgamesp.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent ie, float x, float y) {
                menuState = MenuState.GAMESETTINGSP;
                stageUpdate();
                stateChanged = true;
            }
        });

        newgamemp.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent ie, float x, float y) {
                menuState = MenuState.GAMESETTINGMP;
                stageUpdate();
                stateChanged = true;
            }
        });

        exit.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent ie, float x, float y) {
                Gdx.app.exit();
            }
        });
    }

    private void tablesettingspinit(){
        tablegamesettingsp = new Table();

        spplayerno = new Label("Player number:", userinterfaceskin);
        spplayer2 = new TextButton("TWO", userinterfaceskin);
        spplayer4 = new TextButton("FOUR", userinterfaceskin);
        spmapsize = new Label("Map Size:", userinterfaceskin);
        spmaps = new TextButton("S", userinterfaceskin);
        spmapm = new TextButton("M", userinterfaceskin);
        spmapl = new TextButton("L", userinterfaceskin);
        spmapxl = new TextButton("XL", userinterfaceskin);
        sptreasure = new Label("Treasures:", userinterfaceskin);
        sptreasnone = new TextButton("NONE", userinterfaceskin);
        sptreasmoder = new TextButton("MODERATE", userinterfaceskin);
        sptreasvast = new TextButton("VAST", userinterfaceskin);
        spturnnumber = new Label("Turn Count:", userinterfaceskin);
        spturn20 = new TextButton("20", userinterfaceskin);
        spturn40 = new TextButton("40", userinterfaceskin);
        spturn60 = new TextButton("60", userinterfaceskin);
        spturn100 = new TextButton("100", userinterfaceskin);
        spgametype = new Label("Game Type:", userinterfaceskin);
        sptypeffa = new TextButton("FFA", userinterfaceskin);
        sptypeturn = new TextButton("TURN-BASED", userinterfaceskin);
        splandtype = new Label("Land Type:", userinterfaceskin);
        splandgrass = new TextButton("GRASS", userinterfaceskin);
        splanddesert = new TextButton("DESERT", userinterfaceskin);
        splandrocky = new TextButton("ROCKY", userinterfaceskin);

        spback = new TextButton("Go Back.", userinterfaceskin);
        spok = new TextButton("All Set.", userinterfaceskin);


        spplayer2.setColor(0.53f, 0.35f, 0.20f, 0.85f);
        spplayer4.setColor(0.53f, 0.35f, 0.20f, 0.85f);

        spmaps.setColor(0.53f, 0.35f, 0.20f, 0.85f);
        spmapm.setColor(0.53f, 0.35f, 0.20f, 0.85f);
        spmapl.setColor(0.53f, 0.35f, 0.20f, 0.85f);
        spmapxl.setColor(0.53f, 0.35f, 0.20f, 0.85f);

        sptreasnone.setColor(0.53f, 0.35f, 0.20f, 0.85f);
        sptreasmoder.setColor(0.53f, 0.35f, 0.20f, 0.85f);
        sptreasvast.setColor(0.53f, 0.35f, 0.20f, 0.85f);

        spturn20.setColor(0.53f, 0.35f, 0.20f, 0.85f);
        spturn40.setColor(0.53f, 0.35f, 0.20f, 0.85f);
        spturn60.setColor(0.53f, 0.35f, 0.20f, 0.85f);
        spturn100.setColor(0.53f, 0.35f, 0.20f, 0.85f);

        sptypeffa.setColor(0.53f, 0.35f, 0.20f, 0.85f);
        sptypeturn.setColor(0.53f, 0.35f, 0.20f, 0.85f);

        splandgrass.setColor(0.53f, 0.35f, 0.20f, 0.85f);
        splanddesert.setColor(0.53f, 0.35f, 0.20f, 0.85f);
        splandrocky.setColor(0.53f, 0.35f, 0.20f, 0.85f);

        spback.setColor(0.73f, 0.25f, 0.20f, 0.85f);
        spok.setColor(0.43f, 0.55f, 0.20f, 0.85f);


        tablegamesettingsp.add(spplayerno).right().center();
        tablegamesettingsp.add(spplayer2).size(120, 50).right().center();
        tablegamesettingsp.add(spplayer4).size(120, 50).right().center().row();

        tablegamesettingsp.add(spmapsize).left().center();
        tablegamesettingsp.add(spmaps).size(120, 50).right().center();
        tablegamesettingsp.add(spmapm).size(120, 50).right().center();
        tablegamesettingsp.add(spmapl).size(120, 50).right().center();
        tablegamesettingsp.add(spmapxl).size(120, 50).right().center().row();

        tablegamesettingsp.add(sptreasure).left().center();
        tablegamesettingsp.add(sptreasnone).size(120, 50).right().center();
        tablegamesettingsp.add(sptreasmoder).size(120, 50).right().center();
        tablegamesettingsp.add(sptreasvast).size(120, 50).right().center().row();

        tablegamesettingsp.add(spturnnumber).left().center();
        tablegamesettingsp.add(spturn20).size(120, 50).right().center();
        tablegamesettingsp.add(spturn40).size(120, 50).right().center();
        tablegamesettingsp.add(spturn60).size(120, 50).right().center();
        tablegamesettingsp.add(spturn100).size(120, 50).right().center().row();

        tablegamesettingsp.add(spgametype).left().center();
        tablegamesettingsp.add(sptypeffa).size(120, 50).right().center();
        tablegamesettingsp.add(sptypeturn).size(120, 50).right().center().row();

        tablegamesettingsp.add(splandtype).left().center();
        tablegamesettingsp.add(splandgrass).size(120, 50).right().center();
        tablegamesettingsp.add(splanddesert).size(120, 50).left().center();
        tablegamesettingsp.add(splandrocky).size(120, 50).right().center().row();
        tablegamesettingsp.setColor(0.53f, 0.35f, 0.20f, 0.85f);

        tablegamesettingsp.row();
        tablegamesettingsp.add(spback).size(225, 50).left().row();
        tablegamesettingsp.add(spok).size(225, 50).right().row();



        spplayer2.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent ie, float x, float y) {
                try {
                    configSystem.fileWrite("gameconfig", "playernumber", "2");
                } catch (IOException ioexc) {
                    //
                }
                updateColours();
                stateChanged = true;
            }
        });
        spplayer4.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent ie, float x, float y) {
                try {
                    configSystem.fileWrite("gameconfig", "playernumber", "4");
                } catch (IOException ioexc) {
                    //
                }
                updateColours();
                stateChanged = true;
            }
        });
        spmaps.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent ie, float x, float y) {
                try {
                    configSystem.fileWrite("gameconfig", "mapsize", "s");
                } catch (IOException ioexc) {
                    //
                }
                updateColours();
                stateChanged = true;
            }
        });
        spmapm.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent ie, float x, float y) {
                try {
                    configSystem.fileWrite("gameconfig", "mapsize", "m");
                } catch (IOException ioexc) {
                    //
                }
                updateColours();
                stateChanged = true;
            }
        });
        spmapl.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent ie, float x, float y) {
                try {
                    configSystem.fileWrite("gameconfig", "mapsize", "l");
                } catch (IOException ioexc) {
                    //
                }
                updateColours();
                stateChanged = true;
            }
        });
        spmapxl.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent ie, float x, float y) {
                try {
                    configSystem.fileWrite("gameconfig", "mapsize", "xl");
                } catch (IOException ioexc) {
                    //
                }

                updateColours();
                stateChanged = true;
            }
        });
        sptreasnone.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent ie, float x, float y) {
                try {
                    configSystem.fileWrite("gameconfig", "treasures", "none");
                } catch (IOException ioexc) {
                    //
                }

                updateColours();
                stateChanged = true;
            }
        });
        sptreasmoder.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent ie, float x, float y) {
                try {
                    configSystem.fileWrite("gameconfig", "treasures", "med");
                } catch (IOException ioexc) {
                    //
                }

                updateColours();
                stateChanged = true;
            }
        });
        sptreasvast.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent ie, float x, float y) {
                try {
                    configSystem.fileWrite("gameconfig", "treasures", "vast");
                } catch (IOException ioexc) {
                    //
                }

                updateColours();
                stateChanged = true;
            }
        });
        spturn20.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent ie, float x, float y) {
                try {
                    configSystem.fileWrite("gameconfig", "turncount", "20");
                } catch (IOException ioexc) {
                    //
                }

                updateColours();
                stateChanged = true;
            }
        });
        spturn40.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent ie, float x, float y) {
                try {
                    configSystem.fileWrite("gameconfig", "turncount", "40");
                } catch (IOException ioexc) {
                    //
                }

                updateColours();
                stateChanged = true;
            }
        });
        spturn60.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent ie, float x, float y) {
                try {
                    configSystem.fileWrite("gameconfig", "turncount", "60");
                } catch (IOException ioexc) {
                    //
                }

                updateColours();
                stateChanged = true;
            }
        });
        spturn100.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent ie, float x, float y) {
                try {
                    configSystem.fileWrite("gameconfig", "turncount", "100");
                } catch (IOException ioexc) {
                    //
                }

                updateColours();
                stateChanged = true;
            }
        });
        sptypeffa.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent ie, float x, float y) {
                try {
                    configSystem.fileWrite("gameconfig", "gametype", "ffa");
                } catch (IOException ioexc) {
                    //
                }

                updateColours();
                stateChanged = true;
            }
        });
        sptypeturn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent ie, float x, float y) {
                try {
                    configSystem.fileWrite("gameconfig", "gametype", "turns");
                } catch (IOException ioexc) {
                    //
                }

                updateColours();
                stateChanged = true;
            }
        });
        splandgrass.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent ie, float x, float y) {
                try {
                    configSystem.fileWrite("gameconfig", "landtype", "grass");
                } catch (IOException ioexc) {
                    //
                }

                updateColours();
                stateChanged = true;
            }
        });
        splanddesert.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent ie, float x, float y) {
                try {
                    configSystem.fileWrite("gameconfig", "landtype", "desert");
                } catch (IOException ioexc) {
                    //
                }

                updateColours();
                stateChanged = true;
            }
        });
        splandrocky.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent ie, float x, float y) {
                try {
                    configSystem.fileWrite("gameconfig", "landtype", "rocky");
                } catch (IOException ioexc) {
                    //
                }

                updateColours();
                stateChanged = true;
            }
        });
        spback.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent ie, float x, float y) {
                menuState = MenuState.MAINMENU;
                stageUpdate();
                stateChanged = true;
            }
        });
        spok.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent ie, float x, float y) {
              //  civMain.rewire_InputMultiplexer(CivMain.InputMultiplexerState.GAME);
              //  civMain.gameState.setActualState(GameState.ActualGameState.GAME);
                if(componentSent == ComponentSent.SINGLEMAIN) civMain.createGameState();
                else if(componentSent == ComponentSent.MULTICLIENT) civClient.createGameState();
            }
        });
    }

    private void tablesettingmpinit(){
        tablegamesettingmp = new Table();
        mpplayerno = new Label("Player number:", userinterfaceskin);
        mpplayer2 = new TextButton("TWO", userinterfaceskin);
        mpplayer4 = new TextButton("FOUR", userinterfaceskin);
        mpmapsize = new Label("Map Size:", userinterfaceskin);
        mpmaps = new TextButton("S", userinterfaceskin);
        mpmapm = new TextButton("M", userinterfaceskin);
        mpmapl = new TextButton("L", userinterfaceskin);
        mpmapxl = new TextButton("XL", userinterfaceskin);
        mptreasure = new Label("Treasures:", userinterfaceskin);
        mptreasnone = new TextButton("NONE", userinterfaceskin);
        mptreasmoder = new TextButton("MODERATE", userinterfaceskin);
        mptreasvast = new TextButton("VAST", userinterfaceskin);
        mpturnnumber = new Label("Turn Count:", userinterfaceskin);
        mpturn20 = new TextButton("20", userinterfaceskin);
        mpturn40 = new TextButton("40", userinterfaceskin);
        mpturn60 = new TextButton("60", userinterfaceskin);
        mpturn100 = new TextButton("100", userinterfaceskin);
        mpgametype = new Label("Game Type:", userinterfaceskin);
        mptypeffa = new TextButton("FFA", userinterfaceskin);
        mptypeturn = new TextButton("TURN-BASED", userinterfaceskin);
        mplandtype = new Label("Land Type:", userinterfaceskin);
        mplandgrass = new TextButton("GRASS", userinterfaceskin);
        mplanddesert = new TextButton("DESERT", userinterfaceskin);
        mplandrocky = new TextButton("ROCKY", userinterfaceskin);

        mpback = new TextButton("Go Back.", userinterfaceskin);
        mpok = new TextButton("All Set.", userinterfaceskin);

        Label host;
        host = new Label("(MP) Host: ", userinterfaceskin);
        Label port;
        port = new Label("(MP) Port: ", userinterfaceskin);
        TextField hostfield;
        hostfield = new TextField("localhost", userinterfaceskin);
        TextField portfield;
        portfield = new TextField("9999", userinterfaceskin);

        TextButton back;
        back = new TextButton("Go Back.", userinterfaceskin);
        TextButton ok;
        ok = new TextButton("All Set, Go.", userinterfaceskin);


        mpplayer2.setColor(0.53f, 0.35f, 0.20f, 0.85f);
        mpplayer4.setColor(0.53f, 0.35f, 0.20f, 0.85f);

        mpmaps.setColor(0.53f, 0.35f, 0.20f, 0.85f);
        mpmapm.setColor(0.53f, 0.35f, 0.20f, 0.85f);
        mpmapl.setColor(0.53f, 0.35f, 0.20f, 0.85f);
        mpmapxl.setColor(0.53f, 0.35f, 0.20f, 0.85f);

        mptreasnone.setColor(0.53f, 0.35f, 0.20f, 0.85f);
        mptreasmoder.setColor(0.53f, 0.35f, 0.20f, 0.85f);
        mptreasvast.setColor(0.53f, 0.35f, 0.20f, 0.85f);

        mpturn20.setColor(0.53f, 0.35f, 0.20f, 0.85f);
        mpturn40.setColor(0.53f, 0.35f, 0.20f, 0.85f);
        mpturn60.setColor(0.53f, 0.35f, 0.20f, 0.85f);
        mpturn100.setColor(0.53f, 0.35f, 0.20f, 0.85f);

        mptypeffa.setColor(0.53f, 0.35f, 0.20f, 0.85f);
        mptypeturn.setColor(0.53f, 0.35f, 0.20f, 0.85f);

        mplandgrass.setColor(0.53f, 0.35f, 0.20f, 0.85f);
        mplanddesert.setColor(0.53f, 0.35f, 0.20f, 0.85f);
        mplandrocky.setColor(0.53f, 0.35f, 0.20f, 0.85f);

        mplandrocky.setColor(0.53f, 0.35f, 0.20f, 0.85f);
        mplandrocky.setColor(0.53f, 0.35f, 0.20f, 0.85f);

        hostfield.setColor(0.40f, 0.60f, 0.25f, 0.95f);
        portfield.setColor(0.40f, 0.60f, 0.25f, 0.95f);

        back.setColor(0.73f, 0.25f, 0.20f, 0.85f);
        ok.setColor(0.43f, 0.55f, 0.20f, 0.85f);


        tablegamesettingmp.add(mpplayerno).right().center();
        tablegamesettingmp.add(mpplayer2).size(120, 50).right().center();
        tablegamesettingmp.add(mpplayer4).size(120, 50).right().center().row();

        tablegamesettingmp.add(mpmapsize).left().center();
        tablegamesettingmp.add(mpmaps).size(120, 50).right().center();
        tablegamesettingmp.add(mpmapm).size(120, 50).right().center();
        tablegamesettingmp.add(mpmapl).size(120, 50).right().center();
        tablegamesettingmp.add(mpmapxl).size(120, 50).right().center().row();

        tablegamesettingmp.add(mptreasure).left().center();
        tablegamesettingmp.add(mptreasnone).size(120, 50).right().center();
        tablegamesettingmp.add(mptreasmoder).size(120, 50).right().center();
        tablegamesettingmp.add(mptreasvast).size(120, 50).right().center().row();

        tablegamesettingmp.add(mpturnnumber).left().center();
        tablegamesettingmp.add(mpturn20).size(120, 50).right().center();
        tablegamesettingmp.add(mpturn40).size(120, 50).right().center();
        tablegamesettingmp.add(mpturn60).size(120, 50).right().center();
        tablegamesettingmp.add(mpturn100).size(120, 50).right().center().row();

        tablegamesettingmp.add(mpgametype).left().center();
        tablegamesettingmp.add(mptypeffa).size(120, 50).right().center();
        tablegamesettingmp.add(mptypeturn).size(120, 50).right().center().row();

        tablegamesettingmp.add(mplandtype).left().center();
        tablegamesettingmp.add(mplandgrass).size(120, 50).right().center();
        tablegamesettingmp.add(mplanddesert).size(120, 50).left().center();
        tablegamesettingmp.add(mplandrocky).size(120, 50).right().center().row();
        tablegamesettingmp.setColor(0.53f, 0.35f, 0.20f, 0.85f);

        tablegamesettingmp.add(host).left().center();
        tablegamesettingmp.add(hostfield).size(120, 50).right().row();
        tablegamesettingmp.add(port).left().center();
        tablegamesettingmp.add(portfield).size(120, 50).right().row();

      //  tablegamesettingsp.row();
        tablegamesettingmp.add(back).size(225, 50).left().row();
        tablegamesettingmp.add(ok).size(225, 50).right().row();

        mpplayer2.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent ie, float x, float y) {
                try {
                    configSystem.fileWrite("gameconfig", "playernumber", "2");
                } catch (IOException ioexc) {
                    //
                }
                updateColours();
                stateChanged = true;
            }
        });
        mpplayer4.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent ie, float x, float y) {
                try {
                    configSystem.fileWrite("gameconfig", "playernumber", "4");
                } catch (IOException ioexc) {
                    //
                }
                updateColours();
                stateChanged = true;
            }
        });
        mpmaps.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent ie, float x, float y) {
                try {
                    configSystem.fileWrite("gameconfig", "mapsize", "s");
                } catch (IOException ioexc) {
                    //
                }
                updateColours();
                stateChanged = true;
            }
        });
        mpmapm.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent ie, float x, float y) {
                try {
                    configSystem.fileWrite("gameconfig", "mapsize", "m");
                } catch (IOException ioexc) {
                    //
                }
                updateColours();
                stateChanged = true;
            }
        });
        mpmapl.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent ie, float x, float y) {
                try {
                    configSystem.fileWrite("gameconfig", "mapsize", "l");
                } catch (IOException ioexc) {
                    //
                }
                updateColours();
                stateChanged = true;
            }
        });
        mpmapxl.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent ie, float x, float y) {
                try {
                    configSystem.fileWrite("gameconfig", "mapsize", "xl");
                } catch (IOException ioexc) {
                    //
                }

                updateColours();
                stateChanged = true;
            }
        });
        mptreasnone.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent ie, float x, float y) {
                try {
                    configSystem.fileWrite("gameconfig", "treasures", "none");
                } catch (IOException ioexc) {
                    //
                }

                updateColours();
                stateChanged = true;
            }
        });
        mptreasmoder.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent ie, float x, float y) {
                try {
                    configSystem.fileWrite("gameconfig", "treasures", "med");
                } catch (IOException ioexc) {
                    //
                }

                updateColours();
                stateChanged = true;
            }
        });
        mptreasvast.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent ie, float x, float y) {
                try {
                    configSystem.fileWrite("gameconfig", "treasures", "vast");
                } catch (IOException ioexc) {
                    //
                }

                updateColours();
                stateChanged = true;
            }
        });
        mpturn20.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent ie, float x, float y) {
                try {
                    configSystem.fileWrite("gameconfig", "turncount", "20");
                } catch (IOException ioexc) {
                    //
                }

                updateColours();
                stateChanged = true;
            }
        });
        mpturn40.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent ie, float x, float y) {
                try {
                    configSystem.fileWrite("gameconfig", "turncount", "40");
                } catch (IOException ioexc) {
                    //
                }

                updateColours();
                stateChanged = true;
            }
        });
        mpturn60.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent ie, float x, float y) {
                try {
                    configSystem.fileWrite("gameconfig", "turncount", "60");
                } catch (IOException ioexc) {
                    //
                }

                updateColours();
                stateChanged = true;
            }
        });
        mpturn100.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent ie, float x, float y) {
                try {
                    configSystem.fileWrite("gameconfig", "turncount", "100");
                } catch (IOException ioexc) {
                    //
                }

                updateColours();
                stateChanged = true;
            }
        });
        mptypeffa.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent ie, float x, float y) {
                try {
                    configSystem.fileWrite("gameconfig", "gametype", "ffa");
                } catch (IOException ioexc) {
                    //
                }

                updateColours();
                stateChanged = true;
            }
        });
        mptypeturn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent ie, float x, float y) {
                try {
                    configSystem.fileWrite("gameconfig", "gametype", "turns");
                } catch (IOException ioexc) {
                    //
                }

                updateColours();
                stateChanged = true;
            }
        });
        mplandgrass.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent ie, float x, float y) {
                try {
                    configSystem.fileWrite("gameconfig", "landtype", "grass");
                } catch (IOException ioexc) {
                    //
                }

                updateColours();
                stateChanged = true;
            }
        });
        mplanddesert.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent ie, float x, float y) {
                try {
                    configSystem.fileWrite("gameconfig", "landtype", "desert");
                } catch (IOException ioexc) {
                    //
                }

                updateColours();
                stateChanged = true;
            }
        });
        mplandrocky.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent ie, float x, float y) {
                try {
                    configSystem.fileWrite("gameconfig", "landtype", "rocky");
                } catch (IOException ioexc) {
                    //
                }

                updateColours();
                stateChanged = true;
            }
        });

        back.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent ie, float x, float y) {
                menuState = MenuState.MAINMENU;
                stageUpdate();
                stateChanged = true;
            }
        });

        ok.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent ie, float x, float y) {
                System.out.println("ok mp clicked");
                System.out.println("textfield1: " + hostfield.getText());
                configSystem.setHost(hostfield.getText());
                System.out.println("textfield2: " + Integer.decode(portfield.getText()));
                configSystem.setPort(Integer.decode(portfield.getText()));
                if(componentSent == ComponentSent.SINGLEMAIN) civMain.createMultiplayerState();
                else if(componentSent == ComponentSent.MULTICLIENT) civClient.createMultiState();
             //___________________________________________________________________________________________--
             //   menuState = MenuState.MAINMENU;
             //   try {
             //      configSystem.fileWrite("gameconfig", "turncount", "100");
             // //   } catch (IOException ioexc) {
             //       //
             //   }
             //   stageUpdate();
             //   stateChanged = true;
            }
        });
    }

    public void listButtonsInit(){
        spbuttons.add(spplayerno);
        spbuttons.add(spplayer2);
        spbuttons.add(spplayer4);
        spbuttons.add(spmapsize);
        spbuttons.add(spmaps);
        spbuttons.add(spmapm);
        spbuttons.add(spmapl);
        spbuttons.add(spmapxl);
        spbuttons.add(sptreasure);
        spbuttons.add(sptreasnone);
        spbuttons.add(sptreasmoder);
        spbuttons.add(sptreasvast);
        spbuttons.add(spturn20);
        spbuttons.add(spturn40);
        spbuttons.add(spturn60);
        spbuttons.add(spturn100);
        spbuttons.add(spgametype);
        spbuttons.add(sptypeffa);
        spbuttons.add(sptypeturn);
        spbuttons.add(splandtype);
        spbuttons.add(splandgrass);
        spbuttons.add(splanddesert);
        spbuttons.add(splandrocky);

        mpbuttons.add(mpplayerno);
        mpbuttons.add(mpplayer2);
        mpbuttons.add(mpplayer4);
        mpbuttons.add(mpmapsize);
        mpbuttons.add(mpmaps);
        mpbuttons.add(mpmapm);
        mpbuttons.add(mpmapl);
        mpbuttons.add(mpmapxl);
        mpbuttons.add(mptreasure);
        mpbuttons.add(mptreasnone);
        mpbuttons.add(mptreasmoder);
        mpbuttons.add(mptreasvast);
        mpbuttons.add(mpturn20);
        mpbuttons.add(mpturn40);
        mpbuttons.add(mpturn60);
        mpbuttons.add(mpturn100);
        mpbuttons.add(mpgametype);
        mpbuttons.add(mptypeffa);
        mpbuttons.add(mptypeturn);
        mpbuttons.add(mplandtype);
        mpbuttons.add(mplandgrass);
        mpbuttons.add(mplanddesert);
        mpbuttons.add(mplandrocky);
    }

    public void updateColours(){
        stageUpdate();

        for(Actor actor : spbuttons)
            actor.setColor(0.53f, 0.35f, 0.20f, 0.85f);
        for(Actor actor : mpbuttons)
            actor.setColor(0.53f, 0.35f, 0.20f, 0.85f);

        switch(configSystem.getGcPlayerno()){
            case TWO:
                spplayer2.setColor(0.43f, 0.55f, 0.20f, 0.85f);
                mpplayer2.setColor(0.43f, 0.55f, 0.20f, 0.85f);
                break;
            case FOUR:
                spplayer4.setColor(0.43f, 0.55f, 0.20f, 0.85f);
                mpplayer4.setColor(0.43f, 0.55f, 0.20f, 0.85f);
                break;
        }
        switch(configSystem.getGcMapSize()){
            case S:
                spmaps.setColor(0.43f, 0.55f, 0.20f, 0.85f);
                mpmaps.setColor(0.43f, 0.55f, 0.20f, 0.85f);
                break;
            case M:
                spmapm.setColor(0.43f, 0.55f, 0.20f, 0.85f);
                mpmapm.setColor(0.43f, 0.55f, 0.20f, 0.85f);
                break;
            case L:
                spmapl.setColor(0.43f, 0.55f, 0.20f, 0.85f);
                mpmapl.setColor(0.43f, 0.55f, 0.20f, 0.85f);
                break;
            case XL:
                spmapxl.setColor(0.43f, 0.55f, 0.20f, 0.85f);
                mpmapxl.setColor(0.43f, 0.55f, 0.20f, 0.85f);
                break;
        }

        switch(configSystem.getGcTreasures()){
            case NONE:
                sptreasnone.setColor(0.43f, 0.55f, 0.20f, 0.85f);
                mptreasnone.setColor(0.43f, 0.55f, 0.20f, 0.85f);
                break;
            case MOD:
                sptreasmoder.setColor(0.43f, 0.55f, 0.20f, 0.85f);
                mptreasmoder.setColor(0.43f, 0.55f, 0.20f, 0.85f);
                break;
            case VAST:
                sptreasvast.setColor(0.43f, 0.55f, 0.20f, 0.85f);
                mptreasvast.setColor(0.43f, 0.55f, 0.20f, 0.85f);
                break;
        }

        switch(configSystem.getGcTurnCount()){
            case SHORT:
                spturn20.setColor(0.43f, 0.55f, 0.20f, 0.85f);
                mpturn20.setColor(0.43f, 0.55f, 0.20f, 0.85f);
                break;
            case MEDIUM:
                spturn40.setColor(0.43f, 0.55f, 0.20f, 0.85f);
                mpturn40.setColor(0.43f, 0.55f, 0.20f, 0.85f);
                break;
            case LONG:
                spturn60.setColor(0.43f, 0.55f, 0.20f, 0.85f);
                mpturn60.setColor(0.43f, 0.55f, 0.20f, 0.85f);
                break;
            case LONGER:
                spturn100.setColor(0.43f, 0.55f, 0.20f, 0.85f);
                mpturn100.setColor(0.43f, 0.55f, 0.20f, 0.85f);
                break;
        }

        switch(configSystem.getGcGameType()){
            case FFA:
                sptypeffa.setColor(0.43f, 0.55f, 0.20f, 0.85f);
                mptypeffa.setColor(0.43f, 0.55f, 0.20f, 0.85f);
                break;
            case TURNS:
                sptypeturn.setColor(0.43f, 0.55f, 0.20f, 0.85f);
                mptypeturn.setColor(0.43f, 0.55f, 0.20f, 0.85f);
                break;
        }

        switch(configSystem.getGcLandType()){
            case GRASS:
                splandgrass.setColor(0.43f, 0.55f, 0.20f, 0.85f);
                mplandgrass.setColor(0.43f, 0.55f, 0.20f, 0.85f);
                break;
            case DES:
                splanddesert.setColor(0.43f, 0.55f, 0.20f, 0.85f);
                mplanddesert.setColor(0.43f, 0.55f, 0.20f, 0.85f);
                break;
            case ROCKY:
                splandrocky.setColor(0.43f, 0.55f, 0.20f, 0.85f);
                mplandrocky.setColor(0.43f, 0.55f, 0.20f, 0.85f);
                break;
        }


    }

    public void stageUpdate() {
        switch (menuState) {
            case MAINMENU:
                stageMainMenu = new Stage();
                tableMainMenu.align(Align.right | Align.center);
                tableMainMenu.padRight(200f);
                tableMainMenu.setWidth(stageMainMenu.getWidth());
                tableMainMenu.setHeight(stageMainMenu.getHeight());
                stageMainMenu.addActor(tableMainMenu);
                break;
            case GAMESETTINGSP:
                stageMainMenu = new Stage();
                tablegamesettingsp.align(Align.right | Align.center);
                tablegamesettingsp.padRight(200f);
                tablegamesettingsp.setWidth(stageMainMenu.getWidth());
                tablegamesettingsp.setHeight(stageMainMenu.getHeight());
                stageMainMenu.addActor(tablegamesettingsp);
                break;
            case GAMESETTINGMP:
                stageMainMenu = new Stage();
                tablegamesettingmp.align(Align.right | Align.center);
                tablegamesettingmp.padRight(200f);
                tablegamesettingmp.setWidth(stageMainMenu.getWidth());
                tablegamesettingmp.setHeight(stageMainMenu.getHeight());
                stageMainMenu.addActor(tablegamesettingmp);
                break;
        }
    }

    public void drawbackground(SpriteBatch sbatch){
      //  background[0].setPosition(0,0);
      //  background[0].draw(sbatch);
        float backgroundoffset;
        if( ((float)toolbox.animation_x_continuousd(75.4f)+250) < -400)
            backgroundoffset = -400;
        else
            backgroundoffset = ((float)toolbox.animation_x_continuousd(75.4f)+250);
        sbatch.draw(backgroundt[0], backgroundoffset,0);
        sbatch.flush();

        if(dustback.isComplete()) {
            dustback.reset();
            dustback.start();
        }
        if(dustback2.isComplete()){
            dustback2.reset();
            dustback2.start();
        }
        dustback.update(Gdx.graphics.getDeltaTime());
        dustback.draw(sbatch);

      //  background[1].setPosition(toolbox.animation_x_continuous(155),0);
      //  background[1].draw(sbatch);
        sbatch.draw(backgroundt[1], (float)toolbox.animation_x_continuousd(67.84f),0);
        sbatch.flush();
        dustback2.update(Gdx.graphics.getDeltaTime());
        dustback2.draw(sbatch);
        dustback3.update(Gdx.graphics.getDeltaTime());
        dustback3.draw(sbatch);
      //  background[2].setPosition(toolbox.animation_x_continuous(155),0);
      //  background[2].draw(sbatch);
        sbatch.draw(backgroundt[2], (float)toolbox.animation_x_continuousd(56.84f) ,0);
        sbatch.flush();
      //  background[3].setPosition(toolbox.animation_x_continuous(170),0);
      //  background[3].draw(sbatch);
        sbatch.draw(backgroundt[3], (float)toolbox.animation_x_continuousd(85.2f),0);
        sbatch.flush();

      //  background[4].setPosition(toolbox.animation_x_continuous(100),0);
     //   background[4].draw(sbatch);
        sbatch.draw(backgroundt[4], (float)toolbox.animation_x_continuousd(117.4f),0);
        sbatch.flush();
     //   background[5].setPosition(toolbox.animation_x_continuous(100),0);
     //   background[5].draw(sbatch);
        sbatch.draw(backgroundt[5], (float)toolbox.animation_x_continuousd(34.922f),0);
        sbatch.flush();
    }

    public Stage getStageMainMenu() {
        return stageMainMenu;
    }

    public void setStageMainMenu(Stage stageMainMenu) {
        this.stageMainMenu = stageMainMenu;
    }

    public MenuState getMenuState() {
        return menuState;
    }

    public void setMenuState(MenuState menuState) {
        this.menuState = menuState;
    }

    public boolean isStateChanged() {
        return stateChanged;
    }

    public void setStateChanged(boolean stateChanged) {
        this.stateChanged = stateChanged;
    }

    public ParticleEffect getDustback() {
        return dustback;
    }

    public void setDustback(ParticleEffect dustback) {
        this.dustback = dustback;
    }

    public ParticleEffect getDustfront() {
        return dustfront;
    }

    public void setDustfront(ParticleEffect dustfront) {
        this.dustfront = dustfront;
    }
}
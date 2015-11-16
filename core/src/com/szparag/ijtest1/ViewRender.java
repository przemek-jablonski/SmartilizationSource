package com.szparag.ijtest1;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.utils.Array;

import java.util.*;

/**
 * Created by Szparagowy Krul 3000 on 17/04/2015.
 */
public class ViewRender
{
    //! ogarnac czy nie lepiej byloby przeslac CivMain i wyciagac civ.drawmap, civ.batch itede
    //trolololo
    //

    SpriteBatch batch;
    SpriteBatch guibatch;
    Sprite greenoverlay = new Sprite(new Texture(Gdx.files.internal("overlay_green.png")));
    Sprite redoverlay = new Sprite(new Texture(Gdx.files.internal("overlay_red.png")));
    Sprite yellowoverlay = new Sprite(new Texture(Gdx.files.internal("overlay_yellow.png")));


    private ParticleEffect lmbMapClickParticle;
    private ParticleEffect lmbAnywhereClickParticle;
    private ParticleEffect godRayParticle;
    private ParticleEffect lmbSelectedTileParticle;
    private ParticleEffect lmbUnitScalingBoundaries;
    private ParticleEffect movebacklightDust;

    private boolean [] particleController = new boolean[6];

    Hex[][] hexmap;
    OrthographicCamera camera;
    Sprite fxhighlight, fxhighlightcursor, fxhighlightred, fxhighlightyellow;
    Sprite godrayback, godrayfront;
    Vector2Integer cameralocation;
    ControllerMouseInput controllerMouseInput;

    private LinkedList<Vector2Integer> discoveredArray;

    boolean movemapKeyPressed = false;
    boolean zoommapKeyPressed = false;
    boolean leftmousebuttonPressed = false;
    Vector2Integer leftmousevector = new Vector2Integer();
    int movemapKeyType = -1;
    int zoommapKeyType = -1;
    Texture landsheet = new Texture(Gdx.files.internal("tilesheet_med.png"));
    Sprite actualLandSprite = new Sprite();
    Sprite[] buildingSprite;
    Sprite[] unitSprite;

    Stack<Actor> renderLandStack = new Stack<Actor>();
    Stack<Actor> renderBuildingStack = new Stack<Actor>();
    Stack<Actor> renderUnitStack = new Stack<Actor>();

    List<Vector2Integer> movementPossibilitiesArray = new LinkedList<Vector2Integer>();
    List<Vector2Integer> combatPossibilitiesArray = new LinkedList<Vector2Integer>();
    boolean backlightEnabler = false;
    Vector2Integer hexmapTotalSizePx;
    Toolbox toolbox;

    Texture[] holes;
    Texture[] fogofwar;
    int stackleveldimension_xaxis;
    int stackleveldimension_yaxis;

    int particlemapresolution = 1000;

    ParticleEffectPool[] fogofwarParticleArrayPool = new ParticleEffectPool[5];
    Array<ParticleEffectPool.PooledEffect> fogofwarSpawnsArray = new Array();
    LinkedList<Vector2Integer> particleSpawnPositions = new LinkedList<Vector2Integer>();

    LinkedList<ParticleEffect> fogofwarparticles = new LinkedList<ParticleEffect>();

    CivMain civMain;

    private boolean screenshadeboolean = false;
    private boolean screenshadetimerstart = false;
    private Sprite screenshade = new Sprite(new Texture(Gdx.files.internal("screenshade.png")));
    private Toolbox screenshadetoolbox = new Toolbox();

    ParticleEffectPool  godrayPool;
    ParticleEffect      godrayParticle;
    LinkedList<ParticleEffectPool.PooledEffect> godrayContainer = new LinkedList<>();
    Vector2Integer godrayPosition = new Vector2Integer();
    boolean        godraypositionboolean = false;
    private ControllerUserInterface controllerUserInterface;

    public void addGUI(ControllerUserInterface gui){
        controllerUserInterface = gui;
    }

    public void addCivMainComponent(CivMain civmain){
        this.civMain = civmain;
    }

    public void updatemap(Hex[][] hexmap){
        this.hexmap = hexmap;
    }

    public ViewRender(Hex[][] hexmap) {
        this();
        discoveredArray = new LinkedList<Vector2Integer>();
        toolbox = new Toolbox();
        toolbox.startTimer();
        this.hexmap = hexmap;
        hexmapTotalSizePx = new Vector2Integer();
        hexmapTotalSizePx.set((hexmap.length * 50) + 50, (hexmap[0].length * 50) + 50);

        holes = new Texture[7];
        fogofwar = new Texture[7];
        holes[0] = new Texture(Gdx.files.internal("hole100.png"));
        holes[1] = new Texture(Gdx.files.internal("hole50.png"));
        holes[2] = new Texture(Gdx.files.internal("hole50.png"));
        holes[3] = new Texture(Gdx.files.internal("hole150.png"));
        holes[4] = new Texture(Gdx.files.internal("hole250.png"));
        holes[5] = new Texture(Gdx.files.internal("hole450.png"));
        holes[6] = new Texture(Gdx.files.internal("hole550.png"));
        fogofwar[0] = new Texture(Gdx.files.internal("fog/fog_background.png"));
        fogofwar[1] = new Texture(Gdx.files.internal("fog/fog_layer5.png"));
        fogofwar[2] = new Texture(Gdx.files.internal("fog/fog_layer1.png"));
        fogofwar[3] = new Texture(Gdx.files.internal("fog/fog_layer2.png"));
        fogofwar[4] = new Texture(Gdx.files.internal("fog/fog_layer3.png"));
        fogofwar[5] = new Texture(Gdx.files.internal("fog/fog_layer4.png"));
        fogofwar[6] = new Texture(Gdx.files.internal("fog/fog_layer5.png"));
        System.out.println("hexmaptotalsize(viewRender): " + hexmapTotalSizePx.printer());
        System.out.println("fogofwar layer dimensions: " + fogofwar[2].getHeight() + " / " + fogofwar[2].getWidth());
        fogofwarStackCreator();
    }

    public void particlePoolingInit(){
        godrayParticle = new ParticleEffect();
        godrayParticle.load(Gdx.files.internal("particlesdemo1/GODRAY_alpha3.p"), Gdx.files.internal("particlesdemo1/"));
        godrayPool = new ParticleEffectPool(godrayParticle, 1, 3);
    }

    public void godrayAdd(Vector2Integer position){
        ParticleEffectPool.PooledEffect pooled = godrayPool.obtain();
        pooled.setPosition(position.x()*50+25, position.y()*50+25);
        pooled.start();
        godrayContainer.add(pooled);
    }

    private void fogofwarInitializer(){
        ParticleEffect template = new ParticleEffect();
        template.load(Gdx.files.internal("particlesdemo1/fogl1.p"), Gdx.files.internal("particlesdemo1/"));
        fogofwarParticleArrayPool[0] = new ParticleEffectPool(template, 1, 2);
    }

    private void fogofwarStackCreator() {
        stackleveldimension_xaxis = (int)Math.ceil((float)hexmapTotalSizePx.x() / fogofwar[2].getHeight());
        stackleveldimension_yaxis = (int)Math.ceil((float)hexmapTotalSizePx.y() / fogofwar[2].getWidth());
        int particlemapresolution_xaxis = (int)Math.ceil((float)hexmapTotalSizePx.x() / particlemapresolution);
        int particlemapresolution_yaxis = (int)Math.ceil((float)hexmapTotalSizePx.y() / particlemapresolution);
        for (int x = 0 ; x < particlemapresolution_xaxis ; ++x){
            for (int y = 0 ; y < particlemapresolution_yaxis; ++y){
                particleSpawnPositions.add(new Vector2Integer((x+1)*particlemapresolution, (y+1)*particlemapresolution));
            }
        }
        System.out.println("x axis space: " + stackleveldimension_xaxis);
        System.out.println("y axis space: " + stackleveldimension_yaxis);
        System.out.println("particlespawnpositions:");
        for (Vector2Integer vector : particleSpawnPositions){
            vector.printerSystem();
        }
    }

    public void addControllerMouseInput(ControllerMouseInput cmi) {
        controllerMouseInput = cmi;
    }

    private void drawBacklightPixel(List<Vector2Integer> coordinatesContainer){
        for (int i=0; i < coordinatesContainer.size(); ++i){
            fxhighlightyellow.setPosition(coordinatesContainer.get(i).x(), coordinatesContainer.get(i).y());
            fxhighlightyellow.draw(batch);
        }
    }

    private void drawBacklightTile(List<Vector2Integer> coordinatesContainer){
        for (int i=0; i < coordinatesContainer.size(); ++i){
            fxhighlightyellow.setPosition(coordinatesContainer.get(i).x()*50, coordinatesContainer.get(i).y()*50);
            fxhighlightyellow.draw(batch);
        }
    }

    public ViewRender()
    {
        batch = new SpriteBatch();
        batch.enableBlending();

        camera = new OrthographicCamera(1920, 1080);
        camera.translate(camera.viewportWidth / 2, camera.viewportHeight / 2, 0f);
        camera.update();

        cameralocation = new Vector2Integer(0,0);

        //vfx
        fxhighlight = new Sprite(new Texture("fxhighlight.png"));
        fxhighlightcursor = new Sprite(new Texture("fxhighlight.png"));
        fxhighlightred = new Sprite(new Texture("fxhighlight.png"));
        fxhighlightred.setColor(1f, 0.25f, 0.25f, 0.5f);
        fxhighlightcursor.setColor(0.85f, 1f, 1f, 0.15f);
        fxhighlightyellow = new Sprite(new Texture("fxhighlight.png"));
        fxhighlightyellow.setColor(1f, 0.70f, 0f, 0.25f);
        godrayback = new Sprite(new Texture(Gdx.files.internal("godray_highalpha.png")));
        godrayfront = new Sprite(new Texture(Gdx.files.internal("godray_lowalpha.png")));


        lmbMapClickParticle = new ParticleEffect();
        lmbAnywhereClickParticle = new ParticleEffect();
        godRayParticle = new ParticleEffect();
        lmbSelectedTileParticle = new ParticleEffect();
        lmbUnitScalingBoundaries = new ParticleEffect();
        movebacklightDust = new ParticleEffect();

        lmbMapClickParticle.load(Gdx.files.internal("particlesdemo1/lpm_colorfuldots_a1"), Gdx.files.internal("particlesdemo1"));
        lmbMapClickParticle.scaleEffect(0.50f);
        lmbAnywhereClickParticle.load(Gdx.files.internal("particlesdemo1/lpmclick_alpha.p"), Gdx.files.internal("particlesdemo1"));
        lmbSelectedTileParticle.load(Gdx.files.internal("particlesdemo1/selected_roundcircle.p"), Gdx.files.internal("particlesdemo1"));
        godRayParticle.load(Gdx.files.internal("particlesdemo1/GODRAY_alpha2.p"), Gdx.files.internal("particlesdemo1"));
        lmbUnitScalingBoundaries.load(Gdx.files.internal("particlesdemo1/lpm_unitborderinggreenblue2"), Gdx.files.internal("particlesdemo1"));
        lmbUnitScalingBoundaries.scaleEffect(1.20f);
        movebacklightDust.load(Gdx.files.internal("godray_selections2/particle_sparksuponselection3.p"), Gdx.files.internal("godray_selections2"));
        movebacklightDust.scaleEffect(0.70f);

        Arrays.fill(particleController, false);

        buildingSprite = new Sprite[6];
        buildingSprite[0] = new Sprite(new Texture(Gdx.files.internal("bg_capital.png")));
        buildingSprite[1] = new Sprite(new Texture(Gdx.files.internal("bg_castle.png")));
        buildingSprite[2] = new Sprite(new Texture(Gdx.files.internal("bg_fortress.png")));
        buildingSprite[3] = new Sprite(new Texture(Gdx.files.internal("bg_watermill.png")));
        buildingSprite[4] = new Sprite(new Texture(Gdx.files.internal("bg_stable.png")));
        buildingSprite[5] = new Sprite(new Texture(Gdx.files.internal("bg_armory.png")));

        unitSprite = new Sprite[5];
        unitSprite[0] = new Sprite(new Texture(Gdx.files.internal("un_warrior.png")));
        unitSprite[1] = new Sprite(new Texture(Gdx.files.internal("un_horsemen.png")));
        unitSprite[2] = new Sprite(new Texture(Gdx.files.internal("un_scout.png")));
        unitSprite[3] = new Sprite(new Texture(Gdx.files.internal("un_archer.png")));
        unitSprite[4] = new Sprite(new Texture(Gdx.files.internal("un_engi.png")));
        redoverlay.setPosition(0,0);
        yellowoverlay.setPosition(0,0);
        greenoverlay.setPosition(0,0);

        particlePoolingInit();
    }

    public void render() {
        //particles:
        if (controllerMouseInput.lmbHexSelected) {
            //godray ver2:
            if(!godraypositionboolean) godrayPosition.set(controllerMouseInput.getLmbSelectedHexCoords());

            if(!godrayPosition.compare(controllerMouseInput.getLmbSelectedHexCoords())){
                godrayAdd(controllerMouseInput.getLmbSelectedHexCoords());
            }

            for (int i = 0; i < godrayContainer.size(); ++i){
                ParticleEffectPool.PooledEffect pooled = godrayContainer.get(i);
                pooled.draw(batch, Gdx.graphics.getDeltaTime());
                if(pooled.isComplete()){
                    pooled.dispose();
                    godrayContainer.remove(pooled);
                }
            }


            if (lmbMapClickParticle.isComplete()) {
                lmbMapClickParticle.setPosition(controllerMouseInput.getLmbSelectedHexCoords().getx() * 50 + 25, controllerMouseInput.getLmbSelectedHexCoords().gety() * 50 + 25);
                lmbMapClickParticle.start();
            }
            if(lmbAnywhereClickParticle.isComplete()) {
                lmbAnywhereClickParticle.setPosition(controllerMouseInput.getLmbSelectedHexCoords().getx() * 50 + 25, controllerMouseInput.getLmbSelectedHexCoords().gety() * 50 + 25);
                lmbAnywhereClickParticle.start();
            }

            if(godRayParticle.isComplete()) {
                godRayParticle.setPosition(controllerMouseInput.getLmbSelectedHexCoords().getx() * 50 + 25, controllerMouseInput.getLmbSelectedHexCoords().gety() * 50 + 25);
                godRayParticle.start();
            }


            lmbSelectedTileParticle.setPosition(controllerMouseInput.getLmbSelectedHexCoords().getx() * 50 + 25, controllerMouseInput.getLmbSelectedHexCoords().gety() * 50 + 25);
            lmbSelectedTileParticle.start();
            lmbUnitScalingBoundaries.setPosition(controllerMouseInput.getLmbSelectedHexCoords().getx() * 50 + 25, controllerMouseInput.getLmbSelectedHexCoords().gety() * 50 + 25);
            lmbUnitScalingBoundaries.start();
        }
        zoomMap(zoommapKeyPressed, zoommapKeyType);
        moveMap(movemapKeyPressed, movemapKeyType);



        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClearDepthf(1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
        Gdx.gl.glDepthFunc(GL20.GL_ALWAYS);
      //  Gdx.gl.glDepthMask(false);

        batch.enableBlending();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();



            //region RENDERING MAP LAYER ON DA SCREEN
        if(controllerMouseInput.lmbHexSelected) {
            godrayback.setPosition((controllerMouseInput.getLmbSelectedHexCoords().getx()*50)-25, (controllerMouseInput.getLmbSelectedHexCoords().gety()*50)-100);
            godrayback.draw(batch);
        }


        renderhexmapLandLevel();
        renderhexmapActorLevel();


        //FOGOFWAR:
        /*
        for (int i= fow1container.size - 1; i >= 0 ; --i){
            ParticleEffectPool.PooledEffect pooledeff = fow1container.get(i);
            pooledeff.draw(batch, Gdx.graphics.getDeltaTime());
            if(pooledeff.isComplete()){
                pooledeff.reset();
            }
        }

        for (int i= fow2container.size - 1; i >= 0 ; --i){
            ParticleEffectPool.PooledEffect pooledeff = fow2container.get(i);
            pooledeff.draw(batch, Gdx.graphics.getDeltaTime());
            if(pooledeff.isComplete()){
                pooledeff.reset();
            }
        }

        for (int i= fow3container.size - 1; i >= 0 ; --i){
            ParticleEffectPool.PooledEffect pooledeff = fow3container.get(i);
            pooledeff.draw(batch, Gdx.graphics.getDeltaTime());
            if(pooledeff.isComplete()){
                pooledeff.reset();
            }
        }

        for (int i= fow4container.size - 1; i >= 0 ; --i){
            ParticleEffectPool.PooledEffect pooledeff = fow4container.get(i);
            pooledeff.draw(batch, Gdx.graphics.getDeltaTime());
            if(pooledeff.isComplete()){
                pooledeff.reset();
            }
        }

        for (int i= fow5container.size - 1; i >= 0 ; --i){
            ParticleEffectPool.PooledEffect pooledeff = fow5container.get(i);
            pooledeff.draw(batch, Gdx.graphics.getDeltaTime());
            if(pooledeff.isComplete()){
                pooledeff.reset();
            }
        }
        */

        for (int i = 2 ; i < holes.length ; ++i){
            Gdx.gl.glDepthMask(true);
            for (int a=0 ; a < discoveredArray.size(); ++a)
                batch.draw(holes[i], toolbox.animation_hole_centeringx(discoveredArray.get(a), holes[i].getHeight()), toolbox.animation_hole_centeringy(discoveredArray.get(a), holes[i].getHeight()));
            batch.flush();
            Gdx.gl.glDepthMask(false);
            Gdx.gl.glDepthFunc(GL20.GL_LESS);
            for (int x = 0 ; x < stackleveldimension_xaxis; ++x) {
                switch (i) {
                    case 2: batch.draw(fogofwar[i], toolbox.animation_x_continuous(80), fogofwar[i].getHeight() * x - 30);break;
                    case 3: batch.draw(fogofwar[i], toolbox.animation_x_continuous(105), fogofwar[i].getHeight() * x - 30);break;
                    case 4: batch.draw(fogofwar[i], toolbox.animation_x_continuous(120), fogofwar[i].getHeight() * x - 30);break;
                    case 5: batch.draw(fogofwar[i], toolbox.animation_x_continuous(90), fogofwar[i].getHeight() * x - 30);break;
                    case 6: batch.draw(fogofwar[i], toolbox.animation_x_continuous(65), fogofwar[i].getHeight() * x - 30);break;
                }
            }
            batch.flush();
            Gdx.gl.glDepthFunc(GL20.GL_ALWAYS);
            Gdx.gl.glDepthMask(true);
        }





        if (backlightEnabler)
            drawBacklightTile(movementPossibilitiesArray);



            //    ontoStacks(hexmap);
    //    renderStacks();

        if (controllerMouseInput.lmbHexSelected) {

            fxhighlightyellow.setPosition(controllerMouseInput.getLmbSelectedHexCoords().getx()*50, controllerMouseInput.getLmbSelectedHexCoords().gety()*50);
            fxhighlightyellow.draw(batch);
            godrayfront.setPosition((controllerMouseInput.getLmbSelectedHexCoords().getx()*50)-25, (controllerMouseInput.getLmbSelectedHexCoords().gety()*50)-100);
            godrayfront.draw(batch);

        }

      //  if()
        if(controllerUserInterface.getActionMoveSet()){
            yellowoverlay.draw(batch);
        }

        if(controllerUserInterface.isActionBuildSet()){
            greenoverlay.draw(batch);
        }

        if((controllerUserInterface.getActionCombatSet() || controllerUserInterface.isActionCombatRangedSet())) {
            //  redoverlay.setPosit
            redoverlay.draw(batch);
        }

        lmbMapClickParticle.draw(batch, Gdx.graphics.getDeltaTime());
        lmbAnywhereClickParticle.draw(batch, Gdx.graphics.getDeltaTime());
        godRayParticle.draw(batch, Gdx.graphics.getDeltaTime());
    //    lmbSelectedTileParticle.draw(batch, Gdx.graphics.getDeltaTime());
        lmbUnitScalingBoundaries.draw(batch, Gdx.graphics.getDeltaTime());
            //endregion





        batch.flush();

        //gltranslate (z=1)
        /* working version below:
        Gdx.gl.glDepthMask(true);
            //batch.draw() cos na osi z=1
            for (int i=0 ; i < discoveredArray.size(); ++i)
            batch.draw(holes[0], discoveredArray.get(i).x() * 50, discoveredArray.get(i).y() * 50);


        batch.flush();
        Gdx.gl.glDepthMask(false);
        Gdx.gl.glDepthFunc(GL20.GL_LESS);
                batch.draw(fogofwar[0], toolbox.animation_x_continuous(80), 0);
                batch.draw(fogofwar[1], toolbox.animation_x_continuous(105), 0);
                batch.draw(fogofwar[2], toolbox.animation_x_continuous(120), 0);
                batch.draw(fogofwar[3], toolbox.animation_x_continuous(90), 0);
                batch.draw(fogofwar[4], toolbox.animation_x_continuous(65), 0);
                batch.flush();
        Gdx.gl.glDepthFunc(GL20.GL_ALWAYS);
        Gdx.gl.glDepthMask(true);
        */
/*
        for (int i = 2 ; i < holes.length ; ++i){
            Gdx.gl.glDepthMask(true);
                for (int a=0 ; a < discoveredArray.size(); ++a)
                    batch.draw(holes[i], toolbox.animation_hole_centeringx(discoveredArray.get(a), holes[i].getHeight()), toolbox.animation_hole_centeringy(discoveredArray.get(a), holes[i].getHeight()));
                batch.flush();
            Gdx.gl.glDepthMask(false);
            Gdx.gl.glDepthFunc(GL20.GL_LESS);
            for (int x = 0 ; x < stackleveldimension_xaxis; ++x) {
                switch (i) {
                    case 2:batch.draw(fogofwar[i], toolbox.animation_x_continuous(80), fogofwar[i].getHeight() * x - 30);break;
                    case 3:batch.draw(fogofwar[i], toolbox.animation_x_continuous(105), fogofwar[i].getHeight() * x - 30);break;
                    case 4:batch.draw(fogofwar[i], toolbox.animation_x_continuous(120), fogofwar[i].getHeight() * x - 30);break;
                    case 5:batch.draw(fogofwar[i], toolbox.animation_x_continuous(90), fogofwar[i].getHeight() * x - 30);break;
                    case 6:batch.draw(fogofwar[i], toolbox.animation_x_continuous(65), fogofwar[i].getHeight() * x - 30);break;
                }
            }
                batch.flush();
            Gdx.gl.glDepthFunc(GL20.GL_ALWAYS);
            Gdx.gl.glDepthMask(true);
        }
        */
/*
        if (screenshadeboolean){
            screenshade.draw(batch, (float)toolbox.alphamodulation_animation(100));
            System.out.println((float)toolbox.alphamodulation_animation(100));
        }
*/


        batch.end();
    }

    private void renderhexmapLandLevel() {
            for (int x = 0 ; x < hexmap.length ; ++x) {
                for (int y = 0 ; y < hexmap[0].length; ++y) {
                    if(x*50 < cameralocation.x()+1920 + 75 && x*50 > cameralocation.x() - 75
                        && y*50 < cameralocation.y()+1920+75 && y*50 > cameralocation.x() - 75) {
                            int actorLandType = defineActorType(hexmap[x][y].actorList.get(0));
                            int actorLandTypeVariation = defineActorLANDTypeVariation(hexmap[x][y]);
                            actualLandSprite.setRegion(new TextureRegion
                                    (landsheet, 128 * actorLandType, 128 * actorLandTypeVariation, 128, 128));
                            actualLandSprite.setPosition(hexmap[x][y].position.x(), hexmap[x][y].position.y());
                            actualLandSprite.setSize(50f, 50f);
                            actualLandSprite.draw(batch);
                    }

                }
            }
    }

    private void renderhexmapActorLevel(){
        for (int x = 0 ; x < hexmap.length ; ++x) {
            for (int y = 0; y < hexmap[0].length; ++y) {
                if (x * 50 < cameralocation.x() + 1920 + 75 && x * 50 > cameralocation.x() - 75
                        && y * 50 < cameralocation.y() + 1920 + 75 && y * 50 > cameralocation.x() - 75) {
                    for (int i = 1; i < hexmap[x][y].actorList.size(); ++i) {
                        if (hexmap[x][y].actorList.get(i) instanceof ActorBuilding) {
                            int actorBuildingType = defineActorType(hexmap[x][y].actorList.get(i));
                            buildingSprite[actorBuildingType].setPosition(hexmap[x][y].position.x(), hexmap[x][y].position.y());
                            buildingSprite[actorBuildingType].setSize(50f, 50f);
                            buildingSprite[actorBuildingType].draw(batch);
                        } else {
                            int actorUnitType = defineActorType(hexmap[x][y].actorList.get(i));
                            unitSprite[actorUnitType].setPosition(hexmap[x][y].position.x() + 4, hexmap[x][y].position.y() + 4);
                            unitSprite[actorUnitType].setSize(45f, 45f);
                            unitSprite[actorUnitType].draw(batch);
                        }
                    }
                }
            }
        }
    }

    private int defineActorType(Actor unidentifiedActor){
        if(unidentifiedActor instanceof ActorLand)
            switch(((ActorLand)unidentifiedActor).actorLandType){
                case GRASS:     return 0;
                case PRERISH:   return 1;
                case ROCKY:     return 2;
                case DESERT:    return 3;
                case MOUNTAIN:  return 4;
                case WATER:     return 5;
                default:        return 5;
            }
        else if (unidentifiedActor instanceof ActorBuilding)
            switch(((ActorBuilding)unidentifiedActor).actorBuildingType){
                case CAPITAL: return 0;
                case CASTLE: return 1;
                case FORTRESS: return 2;
                case WATERMILL: return 3;
                case STABLE: return 4;
                case UNITDISPENSER: return 5;

            }
        else if (unidentifiedActor instanceof ActorUnit)
            switch(((ActorUnit)unidentifiedActor).actorUnitType){
                case WARRIOR:
                    return 0;
                case HORSEMEN:
                    return 1;
                case SCOUT:
                    return 2;
                case ARCHER:
                    return 3;
                case ENGINEER:
                    return 4;
            }
            return 0;
    }

    private int defineActorLANDTypeVariation(Hex landhex){
        return ((ActorLand)landhex.actorList.get(0)).actorLandTypeVariation;
    }
/*
    private int defineActorLandType(ActorLand actorLand) {
        switch(actorLand.actorLandType){
            case GRASS:
                return 0;
            case PRERISH:
                return 1;
            case ROCKY:
                return 2;
            case DESERT:
                return 3;
            case MOUNTAIN:
                return 4;
            case WATER:
                return 5;
            default:
                return 5;
        }
    }



    private int defineActorBuildingType(ActorBuilding actorBuilding){
        switch(actorBuilding.actorBuildingType){
            case UNITDISPENSER:
                return 0;
            case CASTLE:
                return 1;
        }
    }

    private int defineActorUnitType(ActorUnit actorUnit){
        switch(actorUnit.actorUnitType){
            case WARRIOR:
                return 0;
            case HORSEMEN:
                return 1;
            case SCOUT:
                return 2;
            case ARCHER:
                return 3;
        }
    }
    */


    private void ontoStacks(Hex[][] hexmap)
    {
        Vector2Integer boundaries = new Vector2Integer();
        boundaries.set((controllerMouseInput.getMouseHoover().getx() - controllerMouseInput.cameralocation.getx()),
                (controllerMouseInput.getMouseHoover().gety() - controllerMouseInput.cameralocation.gety()));

        for (int i=0; i < hexmap.length; ++i)
            for (int j=0; j < hexmap[0].length; ++j) {

                  //  if (hexmap[i][j].position.getx() <= boundaries.getx() && hexmap[i][j].position.getx() >= 0 && hexmap[i][j].position.gety() <= boundaries.gety() && hexmap[i][j].position.gety() >= 0)
                       for (int k=0; k< hexmap[i][j].actorList.size(); ++k)
                       {
                           if (hexmap[i][j].actorList.get(k) instanceof ActorLand)
                               renderLandStack.push(hexmap[i][j].actorList.get(k));
                           else
                               if (hexmap[i][j].actorList.get(k) instanceof ActorBuilding)
                                   renderBuildingStack.push(hexmap[i][j].actorList.get(k));
                           else
                               if (hexmap[i][j].actorList.get(k) instanceof ActorUnit)
                                   renderUnitStack.push(hexmap[i][j].actorList.get(k));
                           else
                               System.out.println("cos zjebales w chuj w pushowaniu na stacki");
                       }


            }
    }


    private void moveMap(boolean keypressed, int keycode) {
        if (keypressed)
        {
            switch(keycode)
            {
                case Input.Keys.W:
                    cameralocation.add(0, -10);
                    camera.translate(0, -10, 0);
                    camera.update();
                    break;
                case Input.Keys.S:
                    cameralocation.add(0, 10);
                    camera.translate(0, 10, 0);
                    camera.update();
                    break;
                case Input.Keys.A:
                    cameralocation.add(10, 0);
                    camera.translate(10, 0, 0);
                    camera.update();
                    break;
                case Input.Keys.D:
                    cameralocation.add(-10, 0);
                    camera.translate(-10, 0, 0);
                    camera.update();
                    break;
            }
        }
    }

    private void zoomMap(boolean keypressed, int keycode) {
        if (keypressed) {
            switch (keycode) {
                case Input.Keys.R:
                    camera.zoom = 1f;
                    break;
                case Input.Keys.E:
                    if (camera.zoom >= 0.1f)
                        camera.zoom -= 0.02f;
                    break;
                case Input.Keys.Q:
                    camera.zoom += 0.02f;
                    break;
            }
            camera.update();
        }
    }

    public LinkedList<Vector2Integer> getDiscoveredArray() {
        return discoveredArray;
    }

    public boolean addToDiscoveredArray(Vector2Integer discoveredHex) {
        System.out.println("addtodiscoveredarray started.");
        if(toolbox.isItemInList(discoveredHex, discoveredArray)){
            System.out.println("if correct");
            return true;
        } else {
            discoveredArray.add(discoveredHex);
            System.out.println("if killed");
            return false;
        }
    }

    public boolean initDiscoveredArray(List<Vector2Integer> list) {
        System.out.println("initdiscoveredarray started.");
        for (int i = 0; i < list.size(); ++i)
            discoveredArray.add(list.get(i));

        System.out.println("Final printer:");
        for (int i = 0; i < discoveredArray.size(); ++i)
            discoveredArray.get(i).printerSystem();
        return true;
    }
}

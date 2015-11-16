package com.szparag.ijtest1;


import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.awt.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class CivServer extends ApplicationAdapter implements Runnable {


    private ArrayList<CivServerThread> clients = new ArrayList<CivServerThread>();
    private ServerSocket server = null;
    private Thread thread = null;
    private int clientCount = 0;
    SpriteBatch batch;
    Sprite background;
    ParticleEffect serverOnlineanimation = new ParticleEffect();
    BitmapFont font;
    private boolean online = true;
    private boolean offlinetrigger = false;
    CharSequence conninfochar;
    private boolean initplayer1 = false;
    private boolean initplayer2 = false;
    private int priviledges = 0;
    private NetworkPackage actualPackage;

    //constructor
    public CivServer() {
        conninfochar = "Listening on port: 9999.";
        try {
            //diagnostic time and event information logging
            //+constructing server on port 9999
            System.out.println(Long.toString(System.nanoTime()) + " CivServer: start();");
            server = new ServerSocket(9999);

            //constructing packet elements
            //that is - initializer and injecting it onto newly created NetworkPackage
            InitializerHexmap initializerHexmap = new InitializerHexmap(new Vector2Integer(50, 50),2);
            actualPackage = new NetworkPackage();
            actualPackage.packageStatePrinter();
            actualPackage.initializeMap(initializerHexmap.newinitsrv(2), new Vector2Integer(45, 45));
            actualPackage.packageStatePrinter();

            //calling run() method from Runnable interface
            if(thread == null) {
                thread = new Thread(this);
                thread.start();
            }
        } catch (IOException exc) {
            exc.printStackTrace();
            online = false;
            conninfochar = exc.getMessage();
            System.err.println(exc.getCause());
            System.err.println(exc.getMessage());
        }
    }

    public void run() {
        //loop containing holder-method - server.accept()
        //program execution (in this thread) is hold until new client connection is discovered
        System.out.println(Long.toString(System.nanoTime()) + " CivServer: run();");
        while(thread != null) {
            try {
                addThread(server.accept());
            } catch (IOException exc) {
                exc.printStackTrace();
                conninfochar = exc.getMessage();
                System.err.println(exc.getCause());
                System.err.println(exc.getMessage());
                stop();
            }
        }
    }

    private void addThread(Socket socket) {
        try {
            System.out.println(Long.toString(System.nanoTime()) + " CivServer: addThread();");
            if(clients.size() < 3) {
                conninfochar = "New Thread";
                clients.add(new CivServerThread(this, socket, clientCount));
                clients.get(clientCount).open();
                clients.get(clientCount).start();
                ++clientCount;
                conninfochar = "New Client added, total number: " + clientCount;
                if(clients.size() == 2) {
                    sendBacktoAll(actualPackage);
                    clients.get(0).send(false);
                    clients.get(0).send(PlayerENUM.playerTWO);
                    clients.get(1).send(true);
                    clients.get(1).send(PlayerENUM.playerONE);
                    priviledges = 1;
                    actualPackage.setActivePlayer(PlayerENUM.playerONE);
                    sendBacktoAll(actualPackage);
                }
            }
        } catch (IOException exc) {
            setOnline(false);
            exc.printStackTrace();
            conninfochar = exc.getMessage();
            System.err.println(exc.getCause());
            System.err.println(exc.getMessage());
            stop();
        }
        sendBacktoAll(actualPackage);
    }

    public void changeTurns() {
        System.out.println("changeturns on server");
        if(priviledges==1){
            clients.get(0).send(true);  //   clients.get(0).send(PlayerENUM.playerTWO);
            clients.get(1).send(false); //   clients.get(1).send(PlayerENUM.playerONE);
            priviledges=2;
        } else if(priviledges == 2) {
            clients.get(0).send(false); //   clients.get(0).send(PlayerENUM.playerTWO);
            clients.get(1).send(true);  //   clients.get(1).send(PlayerENUM.playerONE);
            priviledges = 1;
        }
        /*
        if(actualPackage.getActivePlayer() == PlayerENUM.playerONE) {
            actualPackage.setActivePlayer(PlayerENUM.playerTWO);
            clients.get(0).send(true);  //   clients.get(0).send(PlayerENUM.playerTWO);
            clients.get(1).send(false); //   clients.get(1).send(PlayerENUM.playerONE);
        } else if(actualPackage.getActivePlayer() == PlayerENUM.playerTWO) {
            actualPackage.setActivePlayer(PlayerENUM.playerONE);
            clients.get(0).send(false); //   clients.get(0).send(PlayerENUM.playerTWO);
            clients.get(1).send(true);  //   clients.get(1).send(PlayerENUM.playerONE);
        }
        */
    }

    public void stop() {
        System.out.println(Long.toString(System.nanoTime()) + " CivServer: stop();");
        if(thread != null) {
            thread.stop();
            thread = null;
        }
    }

    public void handle(Object object) {
        System.out.println(Long.toString(System.nanoTime()) + " CivServer: handle()");
        actualPackage = (NetworkPackage)object;
        actualPackage.packageStatePrinter();

        if(actualPackage.isChangeturn()) {
            System.out.println("RECEIVED CHANGETURN FROM PACKAGE:" + actualPackage.isChangeturn());
            changeTurns();
            actualPackage.setChangeturn(false);
            sendBacktoAll(actualPackage);
        }
        /*
        if(clients.size() == 2 && !initplayer2){
            clients.get(0).send(false);
            clients.get(0).send(PlayerENUM.playerTWO);
            clients.get(1).send(true);
            clients.get(1).send(PlayerENUM.playerONE);
            initplayer2 = true;
        }
        */
        sendBacktoAll(actualPackage);
    }

    //changing turn status, passing turn from playerone to playertwo
    //and from playertwo to playerone



    public void sendBacktoAll(Object object){
        System.out.println(Long.toString(System.nanoTime()) + " CivServer: sendBacktoAll()");
        for (int i=0 ; i < clientCount ; ++i) {
            clients.get(i).send(object);
            System.out.println(Long.toString(System.nanoTime()) + " \tsendBacktoAll: object sent to client no: " + i + " succesfully.");
        }
    }

    //region ApplicationAdapter overriden methods - create(), render() + setParticleOffline()
    //constructor handling graphical frontend components
    @Override
    public void create(){
        //creating graphical side (front-end)
        //of a server application
        System.out.println("create (gdx)");
        batch = new SpriteBatch();
        background = new Sprite(new Texture("building2.png"));
        background.setPosition(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2);
        if(online)
            serverOnlineanimation.load(Gdx.files.internal("particlesdemo1/serverworking.p"), Gdx.files.internal("particlesdemo1/"));
        else
            serverOnlineanimation.load(Gdx.files.internal("particlesdemo1/serveroffline.p"), Gdx.files.internal("particlesdemo1/"));
        serverOnlineanimation.setPosition(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
        serverOnlineanimation.scaleEffect(8);
        serverOnlineanimation.start();
        font = new BitmapFont(Gdx.files.internal("version.fnt"));
        font.setColor(0.75f, 0.85f, 0.90f, 0.85f);
    }

    //switching frontend to 'offline' version
    private void setParticleOffline(){
        //due to occured exception, change front-end
        //to indicate server being offline
        //reddish particle version loading and positioning
        serverOnlineanimation.load(Gdx.files.internal("particlesdemo1/serveroffline.p"), Gdx.files.internal("particlesdemo1/"));
        serverOnlineanimation.setPosition(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
        serverOnlineanimation.scaleEffect(8);
        serverOnlineanimation.start();
    }

    //rendering graphical front-end of server application
    @Override
    public void render(){

        //clearing window
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        //batch enable
        batch.begin();
        //if exception occurs, here are booleans which are indicating it
        //changing particle to visualize server going offline
        if(!online && !offlinetrigger){
            offlinetrigger = true;
            setParticleOffline();
        }

        //restarting particle if animation reached it's end
        if(serverOnlineanimation.isComplete()) {
            serverOnlineanimation.reset();
            serverOnlineanimation.start();
        }
        //rendering single frame of particle
        //drawing texts
        serverOnlineanimation.draw(batch, Gdx.graphics.getDeltaTime());
        font.setScale(0.85f);
        if (online) font.draw(batch, "Desktop Server is online.", 125f, Gdx.graphics.getHeight() / 2 + 25f);
        else font.draw(batch, "Desktop Server is OFFLINE.", 100f, Gdx.graphics.getHeight()/2+25f);
        font.setScale(0.6f);
        font.draw(batch, conninfochar, 200f, Gdx.graphics.getHeight()/2-25f);
        batch.end();
    }
    //endregion

    //region Accesors - setters and getters for package, connectioninfo chars and state booleans
    public NetworkPackage getActualPackage() {
        return actualPackage;
    }

    public void setActualPackage(NetworkPackage actualPackage) {
        this.actualPackage = actualPackage;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public void setConninfochar(CharSequence conninfochar) {
        this.conninfochar = conninfochar;
    }
    //endregion

}
package com.szparag.ijtest1;


import java.io.*;
import java.net.Socket;

public class CivServerThread extends Thread
{

    private CivServer server = null;
    private Socket socket = null;
    private  int ID = -1;
    private  ObjectInputStream streamIn = null;
    private  ObjectOutputStream streamOut = null;
    private int playerCount;
    private boolean onetime = true;

    public CivServerThread(CivServer server,Socket socket, int playerCount) {
        System.out.println(Long.toString(System.nanoTime()) + " CivServerThread: constructor();");
        this.server = server;
        this.socket = socket;
        ID = socket.getPort();
        this.playerCount = playerCount;
    }

    public void open() throws  IOException {
        System.out.println(Long.toString(System.nanoTime()) + " CivServerThread: open();");
        streamIn = CivServerThread.createObjectStreamIn(socket);
        streamOut = CivServerThread.createObjectStreamOut(socket);
    }

    public static ObjectOutputStream createObjectStreamOut(Socket socket) throws IOException {
        OutputStream stream = socket.getOutputStream();
        return  new ObjectOutputStream(stream);

    }

    public  static  ObjectInputStream createObjectStreamIn(Socket socket) throws  IOException {
        InputStream stream = socket.getInputStream();
        return  new ObjectInputStream(stream);
    }

    public void run() {
        System.out.println(Long.toString(System.nanoTime()) + " CivServerThread: run();");
        //!
     //   send(server.getActualPackage());
        while (true) {
            try {
                System.err.println(Long.toString(System.nanoTime()) + " CivSrvThread: Object received from pipe;");
                server.handle(streamIn.readObject());
            } catch (IOException e) {
                server.setConninfochar(e.getMessage());
                server.setOnline(false);
                e.printStackTrace();
                stop();
            } catch (ClassNotFoundException e) {
                server.setConninfochar(e.getMessage());
                server.setOnline(false);
                e.printStackTrace();
                stop();
            }
        }
    }

   public void send(Object object){
       System.out.println(Long.toString(System.nanoTime()) + " CivServerThread: send();");
       try {
           streamOut.writeObject(object);
       } catch (IOException e) {
           server.setConninfochar(e.getMessage());
           server.setOnline(false);
           e.printStackTrace();
       }
   }

    public void close()throws  IOException {
        System.out.println(Long.toString(System.nanoTime()) + " CivServerThread: close();");
        if(socket != null)
            socket.close();
        if(streamOut != null)
            streamOut.close();
        if(streamIn != null)
            streamIn.close();
    }

}

package com.szparag.ijtest1;


import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.Socket;

public class CivClientThread extends Thread {

    private Socket              socket   = null;
    private CivClient           client   = null;
    private ObjectInputStream   streamIn = null;

    public CivClientThread(CivClient client, Socket socket) {
        try {
            System.out.println(Long.toString(System.nanoTime()) + " CivClientThread: constructor;");
            this.client = client;
            this.socket = socket;
            streamIn = createObjectStreamIn(socket);
            this.start();
        } catch (IOException exc) {
            exc.printStackTrace();
            System.err.println(exc.getCause());
            System.err.println(exc.getMessage());
            close();
        }
    }

    public static ObjectInputStream createObjectStreamIn(Socket socket) throws IOException {
        InputStream stream = socket.getInputStream();
        return new ObjectInputStream(stream);
    }

    @Override
    public void run() {
        System.out.println(Long.toString(System.nanoTime()) + " CivClientThread: run();");
        while(true) {
            try {
                System.out.println(Long.toString(System.nanoTime()) + " CivClientThread: Object received from pipe;");
                client.handle(streamIn.readObject());
            } catch (Exception exc) {
                exc.printStackTrace();
                System.err.println(exc.getCause());
                System.err.println(exc.getMessage());
                close();
                return;
            }
        }
    }

    public void close() {
        System.out.println(Long.toString(System.nanoTime()) + " CivClientThread: close();");
        try {
            if(streamIn != null)
                streamIn.close();
        } catch (IOException exc) {
            exc.printStackTrace();
            System.err.println(exc.getCause());
            System.err.println(exc.getMessage());
        }
    }


}
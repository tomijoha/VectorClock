/*
*
*   Distributed Systems Project 582665
*   Vector clocks and causal multicast
*   tomijoha
*
*/

import java.io.*;
import java.lang.*;
import java.net.*;
import java.util.*;

public class ServerSocketVC {
    
    private ServerSocket server;
    private int port = 0;
    private int event = 0;
    Socket socket = null;
    VClock message;
    ObjectInputStream ois = null;
    ObjectOutputStream oos = null;
    boolean msgOK = false;
    boolean eventOK = false;

    //New socket
    public ServerSocketVC(int port, int event) {
        this.port = port;
        this.event = event;
        try {
            server = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //New connection and return recieved vector clocks
    public VClock handleConnection() {
        //System.out.println("Waiting for message begin...");

            try {
                socket = server.accept();

            } catch (IOException e) {
                e.printStackTrace();
            }
            
            try{
              ois = new ObjectInputStream(socket.getInputStream());
              oos = new ObjectOutputStream(socket.getOutputStream());
            } catch (IOException e){
                System.out.println("Error input/output");
            }


            while(msgOK==false){
            try
            {
            //
            // Read a message sent by client/sender application
            //
            message = (VClock) ois.readObject();
            //send ack to sender about the transfer
            if(message.getEvent() == event)  {
                       message.setEventOK(true);
                       msgOK = true;
                       //System.out.println("I received:  " + message.getEvent());

                       //
                       // Send a response information to the client application
                       //
                       oos.writeObject("OK...");
                       //System.out.println("I send OK ");
                       
            }else if (message.getEvent() != event){
                       message.setEventOK(false);
                       msgOK = true;
                       //
                       // Send a response information to the client application
                       //
                       oos.writeObject("NOT");
                       //System.out.println("I send NOT ");
            }

            //System.out.println("Waiting for message end...");

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        }//while

        return message;
    }//metodi


     public void finalize(){
     //Clean up
        try{
           ois.close();
           oos.close();
           socket.close();
           server.close();
      } catch (IOException e) {
          System.out.println("Could not close.");

      }

    }




}


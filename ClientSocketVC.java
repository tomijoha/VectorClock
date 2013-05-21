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

public class ClientSocketVC {
  
       private int port = 0;
       private String RHost = null;
       private int event = 0;
       private VClock clocks = null;

       public ClientSocketVC(int port, String RHost, int event, VClock clocks){
          this.port = port;
          this.RHost = RHost;
          this.event = event;
          this.clocks = clocks;
          
          this.clocks.setEvent(this.event);

       }

       public boolean createConnection() {
        boolean eventOK = false;
        Socket socket = null;

        try {
            //
            // Create a connection to the server socket on the server application
            //
            InetAddress host = InetAddress.getByName(RHost);

            socket = new Socket(host.getHostName(), port);

            //
            // Send a message to the receiver application
            //
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            //System.out.println("I send: " + event);
            oos.writeObject(clocks);
            //
            // Read and display the response message sent by server/sender application
            //
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            String message = (String) ois.readObject();
            //System.out.println("I received: " + message);
            //ack OK, stop sending
            if (message.equals("OK..."))
               eventOK = true;


            oos.close();
            ois.close();

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            //e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return eventOK;
    }
}

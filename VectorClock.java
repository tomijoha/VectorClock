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

public class VectorClock {
        //File reader for config and input
  public Vector<String> fileReader(String file){

		Vector<String> t = new Vector<String>();
		
		try {
	            FileReader fr = new FileReader(file);
                    BufferedReader syote = new BufferedReader(fr);
	         
			 
	         String row= "";
	     
	         while (row != null) {
	            row = syote.readLine();
	            //System.out.println(row);
	            t.add(row);

	         }

	         } catch (FileNotFoundException virhe) {
	            System.out.println("File " + file + " not in dic");
	         } catch (IOException virhe) {
	            System.out.println("Error in file");	
	         }
           return t;
	}



	/**
	 * @param args
	 */
	public static void main(String[] args) {

                Vector<String> conf = null;
		int line = -1;
		Vector<String> input = null;
		boolean sOK = true;
                VClock mvc = null;
		String[] temp;

		VectorClock vc = new VectorClock();



		if (args.length == 3){
		
		conf = vc.fileReader(args[0]);
		line = Integer.parseInt(args[1]);

		input = vc.fileReader(args[2]);

                VClock clocks = new VClock(line, conf.size());
                //clocks.printClocks();
                
                //Start to go through the input file
                for(int i = 0; i < (input.size()-1); i++)
                {
                  sOK = true;
                  clocks.setEventOK(true);

                  //System.out.println("EVENT " + i + " : " + input.get(i));

                  temp = input.get(i).split(" ");
                  
                  //check if this event is for me
                  if( Integer.parseInt(temp[0]) == line ) {

                    //advance my own clock
                    if ( temp[1].equals("L") ) {
                       clocks.increment(Integer.parseInt(temp[2]));
                       //clocks.printClocks();

                    }
                    else{
                     //Parse conmmunication config to send and increase vector clock
                     String IPport[] = conf.get(Integer.parseInt(temp[2])-1).split(" ");
                     //System.out.println("I Send -> Receive: " + IPport[0] + " " + IPport[1]);
                     clocks.increment(1);
                     //clocks.printClocks();
                     
                     //New connection
                     do{

                       ClientSocketVC sender = new ClientSocketVC(Integer.parseInt(IPport[1]), IPport[0], i, clocks);

                       sOK = sender.createConnection();

                     }while(sOK==false);
                    }
                  }

                  else if( temp[1].equals("M") && Integer.parseInt(temp[2]) == line ){
                    //Parse conmmunication config to receive
                    String IPportR[] = conf.get(line-1).split(" ");
                    
                    do{
                      //System.out.println("I receive at port: " + IPportR[1] );

                      ServerSocketVC receiver = new ServerSocketVC(Integer.parseInt(IPportR[1]), i);

                      mvc = receiver.handleConnection();
                      receiver.finalize();

                    }while((mvc.getEventOK()) == false);

                   clocks.increment(1);
                   //clocks.printClocks();
                   clocks.compare(mvc);
                   //clocks.printClocks();
                  }

                }

                clocks.printClocks();
			
		}else{
		System.out.println("Error with input args!");
		}
		
	}
	


}

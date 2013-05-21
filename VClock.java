/*
*
*   Distributed Systems Project 582665
*   Vector clocks and causal multicast
*   tomijoha
*
*/

import java.util.*;
import java.io.*;

public class VClock implements Serializable {
        
        private int[] clocks = null;
        private int own = 0;
        private int event = 0;
        private boolean eventOK = false;

        /**
         *  Constructor
         */
        public VClock(int own, int size){

                clocks = new int[size];
                
                for (int i = 0; i < clocks.length; i++)
                        clocks[i] = 0;

                this.own = own;
        }
        
        public void setEvent(int event){
               this.event = event;
        }
        
        public int getEvent(){
               return this.event;
        }
        
        public void setEventOK(boolean eventOK){
               this.eventOK = eventOK;
        }
        
        public boolean getEventOK(){
               return this.eventOK;
        }

        public int[] getVector(){
                return clocks;
        }
        
        public void increment(int ticks){
                clocks[own] = clocks[own] + ticks;
        }
        
        public void compare(VClock c){
                for(int i = 1; i < c.clocks.length; i++){
                        if(c.clocks[i] > this.clocks[i])
                           this.clocks[i] = c.clocks[i];

                }
        }
        public void printClocks(){
                for(int i = 1; i < clocks.length; i++)
                {
                System.out.print(clocks[i] + " ");
                }
        }

}

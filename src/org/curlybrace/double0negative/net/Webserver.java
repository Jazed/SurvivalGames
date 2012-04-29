package org.curlybrace.double0negative.net;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Webserver extends Thread{






    public void run(){
        ServerSocket st;
        try {
            st = new ServerSocket(880);
        } catch (IOException e1) { }

        while(true){

            try{

                skt.getOutputStream().write(s.getBytes());


            }catch(Exception e){e.printStackTrace();}
        }
    }
}

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

                Socket skt = st.accept();

                String s = "HTTP/1.0 ";
                s = s + "200 OK";
                s = s + "\r\n"; 
                s = s + "Connection: close\r\n"; 
                s = s + "Server: SimpleHTTPtutorial v0\r\n"; 
                s = s + "Content-Type: text/html\r\n";
                s = s + "\r\n<html><b>test</b></html>";

                skt.getOutputStream().write(s.getBytes());


            }catch(Exception e){e.printStackTrace();}
        }
    }
}

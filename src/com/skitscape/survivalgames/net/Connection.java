package com.skitscape.survivalgames.net;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.HashMap;

import com.skitscape.survivalgames.SurvivalGames;

public class Connection extends Thread{

    BufferedReader in;
    DataOutputStream out;
    HashMap<String, String>html = new HashMap<String, String>();

    public Connection(Socket skt){
        try{
            this.in = new BufferedReader(new InputStreamReader(skt.getInputStream()));
            this.out = new DataOutputStream(skt.getOutputStream());
        }catch(Exception e){}
    }

    public void run(){
        try{
           // System.out.println(in.read());
            write("ADFSADFDSAF",out);
        }catch(Exception e){e.printStackTrace();}


    }




    public void getHTML(String pageName){

    }

    public void parseHTML(String page){


    }
    
    public void write(String str, OutputStream out){
        String s = "HTTP/1.0 ";
        s = s + "200 OK";
        s = s + "\r\n"; 
        s = s + "Connection: close\r\n"; 
        s = s + "Server: SimpleHTTPtutorial v0\r\n"; 
        s = s + "Content-Type: text/html\r\n";
        s = s + "\r\n";
        
        str = s + str;
        
        try {
            out.w
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }


}

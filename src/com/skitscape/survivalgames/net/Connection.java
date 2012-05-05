package com.skitscape.survivalgames.net;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;

import me.beechboy2000.survivalgames.SurvivalGames;

public class Connection extends Thread{

    BufferedInputStream in;
    BufferedOutputStream out;
    HashMap<String, String>html = new HashMap<String, String>();
    
    public Connection(InputStream in, OutputStream out){
        this.in  = new BufferedInputStream(in);
        this.out = new BufferedOutputStream(out);
        this.start();
    }
    
    public void run(){
        getHTML()
        
        m
    }
    
    
    
    
    public void getHTML(String pageName){
        
    }
    
    public void parseHTML(String page){
        
        
    }
    
    
}

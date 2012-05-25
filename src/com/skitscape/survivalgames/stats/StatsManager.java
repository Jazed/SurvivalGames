package com.skitscape.survivalgames.stats;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Date;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import com.skitscape.survivalgames.GameManager;
import com.skitscape.survivalgames.SurvivalGames;
import com.skitscape.survivalgames.logging.BlockData;

public class StatsManager {

    
    private static StatsManager instance = new StatsManager();

    private ArrayList<BlockData> queue = new ArrayList<BlockData>();
    
    
    private StatsManager(){
        
    }
    
    public static StatsManager getInstance(){
        return instance;
    }
    
    
    
    public void add(String query){
        queue.add(data);
        if(!dumper.isAlive()){
            dumper = new DatabaseDumper();
            dumper.start();
        }
    }
    
    
    class DatabaseDumper extends Thread{
        PreparedStatement s;

        public void run(){
            while(SurvivalGames.isActive()){
                while(queue.size() == 0){
                    try{sleep(1000);}catch(Exception e){}
                }

                s =  dbman.createStatement("INSERT INTO SurvivalGames VALUES (?,?,?,?,?,?,?,?,?,?)");
                while(queue.size()>0){
                    /*for(int z=0; z<50;z++){
                    sleep = 100L -   ((int)(Math.log10(queue.size())+1) * 15);
                    sleep = (sleep>0)? sleep : 1 ;*/
                    BlockData b = queue.remove(0);
                    try{
                        s.setInt(1, GameManager.getInstance().getBlockGameId(new Location(Bukkit.getWorld(b.getWorld()),b.getX(), b.getY(), b.getZ())));
                        s.setString(2,b.getWorld());
                        s.setInt(3, b.getPrevid());
                        s.setByte(4, b.getPrevdata());
                        s.setInt(5, b.getNewid());
                        s.setByte(6, b.getNewdata());
                        s.setInt(7, b.getX());
                        s.setInt(8, b.getY());
                        s.setInt(9, b.getZ());
                        s.setLong(10, new Date().getTime());
                        s.execute();
                    }catch(Exception e){e.printStackTrace();}
                    // }
                    // try{sleep(sleep);}catch(Exception e){}
                }
            }
        }
    }
    
    
    
    
    
}

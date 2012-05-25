package com.skitscape.survivalgames.logging;

import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;


import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

import com.skitscape.survivalgames.Game;
import com.skitscape.survivalgames.GameManager;
import com.skitscape.survivalgames.SettingsManager;
import com.skitscape.survivalgames.SurvivalGames;
import com.skitscape.survivalgames.util.DatabaseManager;
import com.skitscape.survivalgames.util.GameReset;


public class QueueManager {

    private static QueueManager instance = new QueueManager();
    private DatabaseDumper dumper = new DatabaseDumper();
    private ArrayList<BlockData> queue = new ArrayList<BlockData>();
    private Plugin p;
    private long sleep = 100;
    private  Logger log;
    private DatabaseManager dbman = DatabaseManager.getInstance();
    private QueueManager(){

    }

    public static QueueManager getInstance(){
        return instance;
    }


    public void setup(Plugin p) throws SQLException{
        PreparedStatement s = dbman.createStatement(" CREATE TABLE SurvivalGames(gameid int, world varchar(255),previd int,prevdata int,newid int, newdata int, x int, y int, z int, time long)");
        DatabaseMetaData dbm = dbman.getMysqlConnection().getMetaData();
        ResultSet tables = dbm.getTables(null, null, "SurvivalGames", null);
        if (tables.next()) {

        }
        else {
            s.execute();
        }


        this.p = p;
        log = p.getLogger();
        log.info("Connected to database.");

    }


    public void rollback(GameReset r,int id){
        new preRollback(r, id).start();
    }

    public void add(BlockData data){
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
                    }catch(Exception e){}
                    // }
                    // try{sleep(sleep);}catch(Exception e){}
                }
            }
        }
    }
    
    
    class preRollback extends Thread{
        int id;
        Statement s;
        ResultSet result;
        GameReset r;
        Game game;
        int taskId = 0;
        private preRollback(GameReset r, int game){
            this.r = r;
            this.id = game;
            boolean done = false;
            this.game  = GameManager.getInstance().getGame(id);
        }
        public void run(){
            
            while(queue.size()>0){
                game.setRBPercent(queue.size());
                try{sleep(10);}catch(Exception e){}
            }
            
            
            
            try{
            String query = "SELECT * FROM SurvivalGames WHERE gameid="+id+" ORDER BY time DESC";
            Statement s = dbman.createStatement();
            result = s.executeQuery(query);
            
                List<Entity> list = SettingsManager.getGameWorld(id).getEntities();
                for (Iterator<Entity> entities = list.iterator();entities.hasNext();){
                    if (entities.hasNext()){
                        Entity entity = entities.next();
                        if (entity instanceof Item){
                            Item iteme = (Item) entity;
                                Location loce = entity.getLocation();
                                if(GameManager.getInstance().getBlockGameId(loce) == id){
                                    iteme.remove();
                            }
                        }
                    }
                }
                Rollback rb = new Rollback(result, r, id);
                int taskid =   Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(p,rb , 0, 1);
                rb.setTaskId(taskid);
                


            }catch(Exception e){}
            
            
            
            
        }
            
            
        }
        

    

    class Rollback extends Thread{
        int id;
        Statement s;
        ResultSet result;
        GameReset r;
        Game game;
        int taskID = 0;
        private Rollback(ResultSet rs, GameReset r, int game){
            this.result = rs;
            this.r = r;
            this.id = game;
            boolean done = false;
            this.game  = GameManager.getInstance().getGame(id);
        }

        public void setTaskId(int t){
            taskID = t;
        }

        int rbblocks = 0;
        int total = 0;
        int run = 0;
        
        public void run(){
            try{
                if(run == 0){
                    result.last();
                    total = result.getRow();
                    result.beforeFirst();
                    run++;
                }
                
                int i = 1;
                boolean done = false;
                try{
                    while(i != 100 && !done ){
                        if(!result.next()){break;}

                        Location l = new Location(p.getServer().getWorld(result.getString(2)), result.getInt(7), result.getInt(8), result.getInt(9));
                        Block b = l.getBlock();
                        b.setTypeId(result.getInt(3));
                        b.setData(result.getByte(4));
                        b.getState().update();
                        i++;
                        rbblocks++;
                    }
                    game.setRBPercent(((0.0 + rbblocks) / (0.0+ total))*100);
                    if(i<100 && !done){
                        r.rollbackFinishedCallback();
                        done = true;
                        Bukkit.getScheduler().cancelTask(taskID);
                        Statement s1 = dbman.createStatement(); 
                        s1.execute("DELETE FROM SurvivalGames WHERE gameid="+id);
                        System.out.println("Arena "+id+" reset. Rolled back "+rbblocks+" blocks");
                    }
                }catch(Exception e){e.printStackTrace();}
            }catch(Exception e){e.printStackTrace();}
        }
        

    }
}



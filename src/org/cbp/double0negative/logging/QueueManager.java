package org.cbp.double0negative.logging;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.plugin.Plugin;


public class QueueManager {

    private static QueueManager instance = new QueueManager();
    private DatabaseDumper dumper = new DatabaseDumper();
    private ArrayList<BlockData> queue = new ArrayList<BlockData>();
    private Plugin p;
    private long sleep = 100;
    private DatabaseManager dbman = DatabaseManager.getInstance();
    private QueueManager(){

    }

    public static QueueManager getInstance(){
        return instance;
    }


    public void setup(Plugin p){
        PreparedStatement s = dbman.createStatement(" CREATE TABLE blocks(id int NOT NULL AUTO_INCREMENT, world int,previd int,prevdata int,newid int, newdata,int, x int, y int, z int, PRIMARY KEY (id))");
        try {
            s.execute();
        } catch (SQLException e) {
        }

        this.p = p;

    }


    public void rollback(){
        new rollback().start();
    }

    public void add(BlockData data){
        queue.add(data);
        if(!dumper.isAlive()){
            dumper.start();
        }
    }

    class DatabaseDumper extends Thread{
        PreparedStatement s = dbman.createStatement("INSERT INTO blocks VALUES (?,?,?,?,?,?,?,?)");

        public void run(){
            while(queue.size()>0){
                for(int z=0; z<50;z++){
                    sleep = 100L -   ((int)(Math.log10(queue.size())+1) * 15);
                    sleep = (sleep>0)? sleep : 1 ;
                    BlockData b = queue.remove(0);
                    try{
                        s.setString(1,b.getWorld());
                        s.setInt(2, b.getPrevid());
                        s.setByte(3, b.getPrevdata());
                        s.setInt(4, b.getNewid());
                        s.setByte(5, b.getNewdata());
                        s.setInt(6, b.getX());
                        s.setInt(7, b.getY());
                        s.setInt(8, b.getZ());

                        s.execute();

                    }catch(Exception e){}
                }
                try{sleep(sleep);}catch(Exception e){}
            }
        }
    }

    class rollback extends Thread{
        public void run(){
            PreparedStatement s = dbman.createStatement("SELECT * FROM blocks");
            try{
                ResultSet result = s.getResultSet();
                while(result.next()){
                    Location l = new Location(p.getServer().getWorld(result.getString(1)), result.getInt(6), result.getInt(7), result.getInt(8));
                    Block b = l.getBlock();
                    b.setTypeId(result.getInt(2));
                }
                s = dbman.createStatement("TRUNCATE TABLE blocks");
                s.execute();
            }catch(Exception e){}
        }
    }
}



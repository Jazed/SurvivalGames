package me.beechboy2000.survivalgames;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

public class SettingsManager {

    //makes the config easily accessible

    private static SettingsManager instance = new SettingsManager();
    private static Plugin p;
    private FileConfiguration spawns;
    private FileConfiguration system;

    private File f;
    private File f2;

    private SettingsManager(){

    }

    public static SettingsManager getInstance(){
        return instance;
    }

    public void setup(Plugin p){
        this.p = p;

        p.getConfig().options().copyDefaults(true);
        p.saveDefaultConfig();
        
        f = new File(p.getDataFolder(),"spawns.yml");
        f2 = new File(p.getDataFolder(),"system.yml");
        try{
        if(!f.exists())
            f.createNewFile();
        if(!f2.exists())
            f2.createNewFile();
        }catch(Exception e){}
        reloadSystem();
        reloadSpawns();
    }

    public void set(String arg0, Object arg1){
        p.getConfig().set(arg0, arg1);
    }
    
    public FileConfiguration getConfig(){
        return p.getConfig();
    }
    
    public FileConfiguration getSystemConfig(){
        return system;
    }
    
    public void saveConfig(){
       // p.saveConfig();
    }
    
    public static World getGameWorld(){
        if(SettingsManager.getInstance().getSystemConfig().getString("sg-system.world") == null)
            return null;
        return p.getServer().getWorld(SettingsManager.getInstance().getSystemConfig().getString("sg-system.world"));
    }
    
    public void reloadSpawns(){
        spawns = YamlConfiguration.loadConfiguration(f);
    }
    
    public void reloadSystem(){
        system = YamlConfiguration.loadConfiguration(f2);
    }
    
    public void saveSystemConfig(){
        try {
            system.save(f2);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    public void saveSpawns(){
        try {
            spawns.save(f);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    public int getSpawnCount(int gameid){
        return spawns.getInt("spawns."+gameid+".count");
    }
    
    public Location getSpawnPoint(int gameid, int spawnid){
        return new Location(getGameWorld(), 
                spawns.getInt("spawns."+gameid+"."+spawnid+".x"), 
                spawns.getInt("spawns."+gameid+"."+spawnid+".y"),
                spawns.getInt("spawns."+gameid+"."+spawnid+".z"));
    }
    public void setSpawn(int gameid, int spawnid, Vector v){
        spawns.set("spawns."+gameid+"."+spawnid+".x", v.getBlockX());
        spawns.set("spawns."+gameid+"."+spawnid+".y", v.getBlockY());
        spawns.set("spawns."+gameid+"."+spawnid+".Z", v.getBlockZ());
        if(spawnid>spawns.getInt("spawns."+gameid+".count")){
            spawns.set("spawns."+gameid+".count", spawnid);
        }
        try {
            spawns.save(f);
        } catch (IOException e) {
            
        }
    }
}

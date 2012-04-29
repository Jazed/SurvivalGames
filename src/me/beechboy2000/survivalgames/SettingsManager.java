package me.beechboy2000.survivalgames;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

public class SettingsManager {
    
    private static SettingsManager instance = new SettingsManager();
    Plugin p;
    private SettingsManager(){
        
    }
    
    public static SettingsManager getInstance(){
        return instance;
    }
    
    public void setup(Plugin p){
        this.p = p;
    }
    
    public FileConfiguration getConfig(){
        return p.getConfig();
    }
    
    
    
    
}

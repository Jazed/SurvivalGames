package com.skitscape.survivalgames;

import java.io.File;
import java.util.logging.Logger;

import org.bukkit.World;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.skitscape.survivalgames.Events.*;
import com.skitscape.survivalgames.logging.DatabaseManager;
import com.skitscape.survivalgames.logging.LoggingManager;
import com.skitscape.survivalgames.logging.QueueManager;


public class SurvivalGames extends JavaPlugin {
    Logger logger = Logger.getLogger("Minecraft");
    private PlaceEvent blockPlaceEvent;
    private BreakEvent blockBreakEvent;
    private DeathEvent playerDeathEvent;
    private static File datafolder;
    private static boolean active = false;

    public void onDisable() {
        active = false;
        PluginDescriptionFile pdfFile = this.getDescription();
        GameStatus.gameRunning = false;
        SettingsManager.getInstance().saveSpawns();
        SettingsManager.getInstance().saveSystemConfig();
        this.logger.info("The" + pdfFile.getName() + "version" + pdfFile.getVersion() + "has now been disabled and reset");
    }

    public void onEnable() {
        active = true;
        PluginManager pm = getServer().getPluginManager();
        //QueueManager.getInstance().setup(this);
        //DatabaseManager.getInstance().setup(this);


        pm.registerEvents(new PlaceEvent(), this);
        pm.registerEvents(new BreakEvent(), this);
        pm.registerEvents(new DeathEvent(), this);
        pm.registerEvents(new MoveEvent(), this);
        pm.registerEvents(new CommandCatch(), this);

       // pm.registerEvents(LoggingManager.getInstance(), this);


        SettingsManager.getInstance().setup(this);
        GameManager.getInstance().setup(this);
        LobbyManager.getInstance().setup(this);
        
        setCommands();



    }

    public void setCommands(){
        getCommand("survivalgames").setExecutor(new CommandHandler(this));
    }




    public static File getPluginDataFolder(){
        return datafolder;
    }

    public static boolean isActive() {
        return active;
    }

    public  WorldEditPlugin getWorldEdit()  {
        Plugin worldEdit = getServer().getPluginManager().getPlugin("WorldEdit");
        if (worldEdit instanceof WorldEditPlugin) {
            return (WorldEditPlugin) worldEdit;
        } else {
            return null;
        }
    }


}

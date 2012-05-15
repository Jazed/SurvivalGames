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
import com.skitscape.survivalgames.net.Webserver;
import com.skitscape.survivalgames.util.ChestRatioStorage;


public class SurvivalGames extends JavaPlugin {
    Logger logger;
    private static File datafolder;
    private static boolean active = false;
    public static boolean dbcon = false;

    public void onDisable() {
        active = false;
        PluginDescriptionFile pdfFile = this.getDescription();
        SettingsManager.getInstance().saveSpawns();
        SettingsManager.getInstance().saveSystemConfig();
        this.logger.info("The" + pdfFile.getName() + "version" + pdfFile.getVersion() + "has now been disabled and reset");
    }

    public void onEnable() {
        logger = this.getLogger();
        active = true;
        datafolder = this.getDataFolder();
        PluginManager pm = getServer().getPluginManager();
        setCommands();


        SettingsManager.getInstance().setup(this);

        try{
            DatabaseManager.getInstance().setup(this);
            QueueManager.getInstance().setup(this);
            dbcon = true;
        }
        catch(Exception e){
            dbcon = false;
            logger.severe("!!!Failed to connect to the database. Please check the settings and try again!!!");
            return;
        }
        finally{
            LobbyManager.getInstance().setup(this);

        }
        ChestRatioStorage.getInstance().setup();

        
        pm.registerEvents(new PlaceEvent(), this);
        pm.registerEvents(new BreakEvent(), this);
        pm.registerEvents(new DeathEvent(), this);
        pm.registerEvents(new MoveEvent(), this);
        pm.registerEvents(new CommandCatch(), this);
        pm.registerEvents(new SignClickEvent(), this);
        pm.registerEvents(new ChestReplaceEvent(), this);
        pm.registerEvents(new LogoutEvent(), this);
        pm.registerEvents(new JoinEvent(), this);
        pm.registerEvents(new TeleportEvent(), this);
        pm.registerEvents(LoggingManager.getInstance(), this);



      //  new Webserver().start();
        

        GameManager.getInstance().setup(this);



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

package com.skitscape.survivalgames;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.skitscape.survivalgames.Events.*;
import com.skitscape.survivalgames.logging.LoggingManager;
import com.skitscape.survivalgames.logging.QueueManager;
import com.skitscape.survivalgames.net.Webserver;
import com.skitscape.survivalgames.util.ChestRatioStorage;
import com.skitscape.survivalgames.util.DatabaseManager;


public class SurvivalGames extends JavaPlugin {
    Logger logger;
    private static File datafolder;
    private static boolean active = false;
    public static boolean dbcon = false;
    
    public static List<String> auth = Arrays.asList(new String[]{"Double0negative","iMalo","beechboy2000", "Medic0987","alex_markey", "skitscape","Antvenom", "YoshiGenius", "pimpinpsp", "WinryR", "Jazed2011"});
    
    SurvivalGames p = this;
    public void onDisable() {
        active = false;
        PluginDescriptionFile pdfFile = p.getDescription();
        SettingsManager.getInstance().saveSpawns();
        SettingsManager.getInstance().saveSystemConfig();
        for(Game g: GameManager.getInstance().getGames()){
            g.disable();
        }
               
        logger.info("The" + pdfFile.getName() + "version" + pdfFile.getVersion() + "has now been disabled and reset");
    }

    public void onEnable() {
        //endsure that all worlds are loaded. Fixes some issues with Multiverse loading after this plugin had started
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(this, new Startup(), 40);

    }
    
    class Startup implements Runnable{
        
        public void run(){
            logger = p.getLogger();
            active = true;
            datafolder = p.getDataFolder();
            PluginManager pm = getServer().getPluginManager();
            setCommands();


            SettingsManager.getInstance().setup(p);

            try{
                DatabaseManager.getInstance().setup(p);
                QueueManager.getInstance().setup(p);
                dbcon = true;
            }
            catch(Exception e){
                dbcon = false;
                logger.severe("!!!Failed to connect to the database. Please check the settings and try again!!!");
                return;
            }
            finally{
                GameManager.getInstance().setup(p);
                
                LobbyManager.getInstance().setup(p);

            }
            ChestRatioStorage.getInstance().setup();

            
            pm.registerEvents(new PlaceEvent(), p);
            pm.registerEvents(new BreakEvent(), p);
            pm.registerEvents(new DeathEvent(), p);
            pm.registerEvents(new MoveEvent(), p);
            pm.registerEvents(new CommandCatch(), p);
            pm.registerEvents(new SignClickEvent(), p);
            pm.registerEvents(new ChestReplaceEvent(), p);
            pm.registerEvents(new LogoutEvent(), p);
            pm.registerEvents(new JoinEvent(p), p);
            pm.registerEvents(new TeleportEvent(), p);
            pm.registerEvents(LoggingManager.getInstance(), p);
            pm.registerEvents(new SpectatorEvents(), p);



          //  new Webserver().start();
            




        }
    }

    public void setCommands(){
        getCommand("survivalgames").setExecutor(new CommandHandler(p));
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

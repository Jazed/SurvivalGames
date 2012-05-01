package me.beechboy2000.survivalgames;

import java.io.File;
import java.util.logging.Logger;

import org.bukkit.World;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.curlybrace.double0negative.logging.DatabaseManager;
import org.curlybrace.double0negative.logging.LoggingManager;
import org.curlybrace.double0negative.logging.QueueManager;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;

import me.beechboy2000.survivalgames.Events.*;
import me.beechboy2000.survivalgames.commands.AddPlayer;
import me.beechboy2000.survivalgames.commands.CreateArena;
import me.beechboy2000.survivalgames.commands.Join;
import me.beechboy2000.survivalgames.commands.Leave;
import me.beechboy2000.survivalgames.commands.SetLobby;
import me.beechboy2000.survivalgames.commands.SetSpawn;
import me.beechboy2000.survivalgames.commands.Start;
import me.beechboy2000.survivalgames.commands.Stop;

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

        pm.registerEvents(LoggingManager.getInstance(), this);

        setCommands();

        SettingsManager.getInstance().setup(this);
        GameManager.getInstance().setup(this);


    }

    public void setCommands(){
        getCommand("survivalgames").setExecutor(new AddPlayer());
        getCommand("survivalgames").setExecutor(new CreateArena());
        getCommand("survivalgames").setExecutor(new Join());
        //    getCommand("survivalgames").setExecutor(new Leave());
        getCommand("survivalgames").setExecutor(new SetLobby());
        //     getCommand("survivalgames").setExecutor(new SetSpawn());
        getCommand("survivalgames").setExecutor(new Start());
        getCommand("survivalgames").setExecutor(new Stop());

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

package me.beechboy2000.survivalgames;

import java.util.logging.Logger;

import org.bukkit.World;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.cbp.double0negative.logging.DatabaseManager;
import org.cbp.double0negative.logging.LoggingManager;
import org.cbp.double0negative.logging.QueueManager;

import me.beechboy2000.survivalgames.Events.*;

public class SurvivalGames extends JavaPlugin {
	Logger logger = Logger.getLogger("Minecraft");
	private PlaceEvent blockPlaceEvent;
	private BreakEvent blockBreakEvent;
	private DeathEvent playerDeathEvent;
	
	 public void onDisable() {
		PluginDescriptionFile pdfFile = this.getDescription();
		GameStatus.gameRunning = false;
		this.logger.info("The" + pdfFile.getName() + "version" + pdfFile.getVersion() + "has now been disabled and reset");
	}
	 
	 public void onEnable() {
		PluginManager pm = getServer().getPluginManager();
		GameStatus.gameRunning = false;
		QueueManager.getInstance().setup(this);
		DatabaseManager.getInstance().setup(this);
		
		this.blockPlaceEvent = new PlaceEvent();
		this.blockBreakEvent = new BreakEvent();
		this.playerDeathEvent = new DeathEvent();
		
		pm.registerEvents(blockPlaceEvent, this);
		pm.registerEvents(blockBreakEvent, this);
		pm.registerEvents(playerDeathEvent, this);
	    pm.registerEvents(LoggingManager.getInstance(), this);

	 }
	 
	 
	 
	 /**
	  * @TODO: return active survivalgames world.
	  * 
	  */
	 public static World getGameWorld(){
	     return null;
	 }
	
}

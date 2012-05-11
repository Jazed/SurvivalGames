package com.skitscape.survivalgames.Events;


import java.util.ArrayList;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import com.skitscape.survivalgames.Game;
import com.skitscape.survivalgames.GameManager;
import com.skitscape.survivalgames.SettingsManager;

public class PlaceEvent implements Listener {
	
	public  ArrayList<Integer> allowedPlace = new ArrayList<Integer>();
	
	public PlaceEvent(){
	   allowedPlace.addAll( SettingsManager.getInstance().getConfig().getIntegerList("block.place.whitelist"));
	}
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
	    Player p = event.getPlayer();
	    int id  = GameManager.getInstance().getPlayerGameId(p);
	    if(id == -1)return;
	    Game g = GameManager.getInstance().getGame(id);
	    if(g.getMode() == Game.GameMode.DISABLED)return;
	    if(g.getMode() != Game.GameMode.INGAME){event.setCancelled(true);return;}
	    if(!allowedPlace.contains(event.getBlock().getTypeId()))event.setCancelled(true);
	    
	}
}
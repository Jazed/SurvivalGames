package com.skitscape.survivalgames.Events;


import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import com.skitscape.survivalgames.Game;
import com.skitscape.survivalgames.GameManager;
import com.skitscape.survivalgames.SettingsManager;

public class BreakEvent implements Listener {
	
	public ArrayList<Integer> allowedBreak =  new ArrayList<Integer>();;
	
	public BreakEvent(){
	   allowedBreak.addAll( SettingsManager.getInstance().getConfig().getIntegerList("block.break.whitelist"));
	}
	
    @EventHandler(priority = EventPriority.HIGHEST)
	public void onBlockBreak(BlockBreakEvent event) {
	    Player p = event.getPlayer();
        int id  = GameManager.getInstance().getPlayerGameId(p);
        if(id == -1)return;
        Game g = GameManager.getInstance().getGame(id);
        if(g.isPlayerinactive(p))return;
        if(g.getMode() == Game.GameMode.DISABLED)return;
        if(g.getMode() != Game.GameMode.INGAME){event.setCancelled(true);return;}
        if(!allowedBreak.contains(event.getBlock().getTypeId()))event.setCancelled(true);
	}
}
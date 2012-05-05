package com.skitscape.survivalgames.Events;


import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import com.skitscape.survivalgames.GameStatus;

public class BreakEvent implements Listener {
	
	public static Material[] allowedBreak = {Material.LEAVES, Material.RED_MUSHROOM, Material.BROWN_MUSHROOM};
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		Player player = event.getPlayer();
		Material block = event.getBlock().getType();
		if(GameStatus.gameRunning) {
		if(!player.hasPermission("survivalgames.break.bypass")) {
			for (Material allowedBlock: allowedBreak) {
				if(allowedBlock == block) {
					event.setCancelled(false);
					//added that becuz I can :P
				} else {
					if(GameStatus.gameRunning) {
					player.sendMessage(ChatColor.GREEN + "[SurvivalGames] " + ChatColor.RED + "Sorry, you cannot break this block while playing a game");
					event.setCancelled(true);
					} else {
						event.setCancelled(false);
					}
				}
			}
		} else {
			event.setCancelled(false);
		}
	}
	
}
}
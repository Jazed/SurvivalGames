package me.beechboy2000.survivalgames.Events;

import me.beechboy2000.survivalgames.GameStatus;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class PlaceEvent implements Listener {
	
	public static Material[] allowedPlace = {Material.WORKBENCH, Material.LEAVES, Material.RED_MUSHROOM, Material.BROWN_MUSHROOM};
	
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		Player player = event.getPlayer();
		Material block = event.getBlock().getType();
		if(GameStatus.gameRunning) {
		if(!player.hasPermission("survivalgames.place.bypass")) {
			for (Material allowedBlock: allowedPlace) {
				if(allowedBlock == block) {
					event.setCancelled(false);
					//added that becuz I can :P
				} else {
					if(GameStatus.gameRunning) {
					player.sendMessage(ChatColor.GREEN + "[SurvivalGames] " + ChatColor.RED + "Sorry, you cannot place this block while playing a game");
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

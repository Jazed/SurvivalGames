package com.skitscape.survivalgames.Events;


import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import com.skitscape.survivalgames.Game;
import com.skitscape.survivalgames.GameManager;
import com.skitscape.survivalgames.GameStatus;
import com.skitscape.survivalgames.SettingsManager;

public class DeathEvent implements Listener {
	
	public void deathCheck() {
		if(GameStatus.playersLeft == 1) {
			
		}
	}
	
	@EventHandler
	public void onPlayerDieEvent(EntityDamageEvent event) {
	    if(event.getEntity() instanceof Player){}
	    else 
	        return;
		Player player = (Player)event.getEntity();
		int gameid = GameManager.getInstance().getPlayerGameId(player);
		if(gameid==-1)
		    return;
        if(!GameManager.getInstance().isPlayerActive(player))
            return;
		Game game = GameManager.getInstance().getGame(gameid);
		if(game.getMode() != Game.GameMode.INGAME){
		    event.setCancelled(true);
		    return;
		}
		if(game.isProtectionOn()){
		    event.setCancelled(true);
		    return;
		}
		if(player.getHealth() <= event.getDamage()){
		    event.setCancelled(true);
		    player.setHealth(20);
		    player.setFoodLevel(20);
		    player.setFireTicks(0);
		    PlayerInventory inv = player.getInventory();
		    Location l = player.getLocation();
            player.teleport(SettingsManager.getInstance().getLobbySpawn());

		    for(ItemStack i: inv.getContents()){
		        if(i!=null)
		            l.getWorld().dropItemNaturally(l, i);
		    }
	          for(ItemStack i: inv.getArmorContents()){
	                if(i!=null && i.getTypeId() !=0)
	                    l.getWorld().dropItemNaturally(l, i);
	            }
		    
		      GameManager.getInstance().getGame(GameManager.getInstance().getPlayerGameId(player)).killPlayer(player);

		}
	}

	
	
	
	
	
}
package com.skitscape.survivalgames.Events;


import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import com.skitscape.survivalgames.GameManager;
import com.skitscape.survivalgames.GameStatus;

public class DeathEvent implements Listener {
	
	public void deathCheck() {
		if(GameStatus.playersLeft == 1) {
			
		}
	}
	
	@EventHandler
	public void onPlayerDieEvent(PlayerDeathEvent event) {
		Player player = (Player)event.getEntity();
		if(GameManager.getInstance().getPlayerGameId(player)==-1)
		    return;
		GameManager.getInstance().getGame(GameManager.getInstance().getPlayerGameId(player)).killPlayer(player);
	}
}
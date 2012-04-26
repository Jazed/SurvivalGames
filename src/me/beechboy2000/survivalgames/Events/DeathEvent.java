package me.beechboy2000.survivalgames.Events;

import me.beechboy2000.survivalgames.GameStatus;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class DeathEvent implements Listener {
	
	public void deathCheck() {
		if(GameStatus.playersLeft == 1) {
			
		}
	}
	
	@EventHandler
	public void onPlayerDieEvent(PlayerDeathEvent event) {
		Player player = (Player)event.getEntity();
		String playerName = player.getDisplayName();
		if(GameStatus.gameRunning) {
		Bukkit.getServer().broadcastMessage(playerName + "has died! there are currently " + GameStatus.playersLeft + " players left!");
		GameStatus.playersLeft--;
		deathCheck();
		}
	}
}
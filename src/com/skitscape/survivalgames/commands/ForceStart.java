package com.skitscape.survivalgames.commands;


import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.skitscape.survivalgames.GameManager;
import com.skitscape.survivalgames.GameStatus;

public class ForceStart implements SubCommand {
	
    public boolean onCommand(Player player, String[] args) {
        int game  = GameManager.getInstance().getPlayerGameId(player);
        if(game == -1){
            player.sendMessage(ChatColor.RED+"Must be in a game!");
            return true;
        }
        if(GameManager.getInstance().getGame(game).getActivePlayers() < 2){
            player.sendMessage(ChatColor.RED+"Needs at least 2 players to start!");
            return true;
        }
		GameManager.getInstance().getGame(GameManager.getInstance().getPlayerGameId(player)).countdown(10);
		
		return true;
	}


}

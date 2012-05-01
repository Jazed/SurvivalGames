package me.beechboy2000.survivalgames.commands;

import me.beechboy2000.survivalgames.GameManager;
import me.beechboy2000.survivalgames.GameStatus;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Start implements SubCommand {
	
    public boolean onCommand(Player player, String[] args) {
		    GameManager.getInstance().startGame(GameManager.getInstance().getPlayerGameId(player));
		
		return false;
	}


}

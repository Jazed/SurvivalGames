package com.skitscape.survivalgames.commands;


import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.skitscape.survivalgames.GameStatus;

public class Stop implements CommandExecutor {
	
	@Override public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(cmd.getName().equalsIgnoreCase("sgstop")) {
			Bukkit.getServer().broadcastMessage(ChatColor.GREEN + "[SurvivalGames]" + ChatColor.YELLOW + " The game is stopping now!!!");
			GameStatus.gameRunning = false;
		}
		return false;
	}

}

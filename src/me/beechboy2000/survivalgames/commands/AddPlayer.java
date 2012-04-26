package me.beechboy2000.survivalgames.commands;

import me.beechboy2000.survivalgames.GameStatus;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class AddPlayer implements CommandExecutor {
	
	@Override public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(cmd.getName().equalsIgnoreCase("sgjoin")) {
			if(GameStatus.gameRunning) {
			Bukkit.getServer().broadcastMessage(ChatColor.GREEN + "[SurvivalGames] " + ChatColor.YELLOW + sender.getName() + " has joined the game");
			GameStatus.playersLeft++;
		}
	}
		return false;
}
}

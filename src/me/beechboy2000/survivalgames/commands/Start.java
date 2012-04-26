package me.beechboy2000.survivalgames.commands;

import me.beechboy2000.survivalgames.GameStatus;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class Start implements CommandExecutor {
	
	@Override public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(cmd.getName().equalsIgnoreCase("sgstart")) {
			Bukkit.getServer().broadcastMessage(ChatColor.GREEN + "[SurvivalGames]" + ChatColor.YELLOW + " The game is starting now!!!");
			startGame();
		}
		return false;
	}

	public void startGame() {
		GameStatus.gameRunning = true;
	}
}

package me.beechboy2000.survivalgames.commands;

import me.beechboy2000.survivalgames.GameStatus;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AddPlayer implements SubCommand {

    public boolean onCommand(Player player, String[] args) {
        if(GameStatus.gameRunning) {
            Bukkit.getServer().broadcastMessage(ChatColor.GREEN + "[SurvivalGames] " + ChatColor.YELLOW + player.getName() + " has joined the game");
            GameStatus.playersLeft++;
        }
        return false;

    }

}

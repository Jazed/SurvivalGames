package com.skitscape.survivalgames.commands;


import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.skitscape.survivalgames.GameStatus;

public class AddPlayer implements SubCommand {

    public boolean onCommand(Player player, String[] args) {
        if(GameStatus.gameRunning) {
            Bukkit.getServer().broadcastMessage(ChatColor.GREEN + "[SurvivalGames] " + ChatColor.YELLOW + player.getName() + " has joined the game");
            GameStatus.playersLeft++;
        }
        return false;

    }
    @Override
    public String help(Player p) {
        return "/sg setlobbywall - Setings the lobby stats wall";
    }

}

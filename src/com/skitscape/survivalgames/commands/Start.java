package com.skitscape.survivalgames.commands;


import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.skitscape.survivalgames.GameManager;
import com.skitscape.survivalgames.GameStatus;

public class Start implements SubCommand {
    
    public boolean onCommand(Player player, String[] args) {
        int game  = GameManager.getInstance().getPlayerGameId(player);
        if(game == -1){
            player.sendMessage(ChatColor.RED+"Must be in a game!");
            return true;
        }
        
        GameManager.getInstance().getGame(GameManager.getInstance().getPlayerGameId(player)).vote();
        player.sendMessage(ChatColor.GREEN+"Voted to start the game!");

        return true;
    }


}

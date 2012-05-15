package com.skitscape.survivalgames.commands;


import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.skitscape.survivalgames.GameManager;

public class CreateArena implements SubCommand{

    public boolean onCommand(Player player, String[] args) {
        if(!player.hasPermission("sg.arena.create") && !player.isOp()){
            player.sendMessage(ChatColor.RED+"No Permission");
            return true;
        }
        GameManager.getInstance().createArenaFromSelection(player);

        return true;
    }

    @Override
    public String help(Player p) {
        return "/sg createarena - Creates a new arena in the current world edit selection";
    }

}

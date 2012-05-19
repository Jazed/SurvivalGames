package com.skitscape.survivalgames.commands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.skitscape.survivalgames.SettingsManager;

public class ResetSpawns implements SubCommand{

    public boolean onCommand(Player player, String[] args) {
        
        if(!player.hasPermission("sg.arena.resetspawns") && !player.isOp()){
            player.sendMessage(ChatColor.RED+ "No Permission");
            return true;
        }
        SettingsManager.getInstance().getSpawns().set("spawns."+Integer.parseInt(args[0]), null);
        return true;
    }   

    @Override
    public String help(Player p) {
        return "/sg resetspawns <id> - resets spawns for an arena";
    }
}

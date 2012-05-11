package com.skitscape.survivalgames.commands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.skitscape.survivalgames.GameManager;

public class Disable implements SubCommand{

    @Override
    public boolean onCommand(Player player, String[] args) {        
        if(!player.hasPermission("sg.arena.disable") && !player.isOp()){
            player.sendMessage(ChatColor.RED+"No Permission");
            return true;
        }
        GameManager.getInstance().disableGame(Integer.parseInt(args[0]));
        player.sendMessage(ChatColor.GREEN+"Game "+args[0]+" disabled");
        return false;
    }

}

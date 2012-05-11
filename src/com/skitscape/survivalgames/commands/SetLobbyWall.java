package com.skitscape.survivalgames.commands;


import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.skitscape.survivalgames.LobbyManager;

public class SetLobbyWall implements SubCommand{

    @Override
    public boolean onCommand(Player player, String[] args) {
        if(!player.hasPermission("sg.lobby.set") && !player.isOp()){
            player.sendMessage(ChatColor.RED+"No Permission");
            return true;
        }
       LobbyManager.getInstance().setLobbySignsFromSelection(player);
       return true;
    }

    //TODO: TAKE A W.E SELECTIONA AND SET THE LOBBY. ALSO SET LOBBY WALL
    
    
    
    
    
    
}

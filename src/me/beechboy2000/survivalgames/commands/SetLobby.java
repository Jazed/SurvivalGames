package me.beechboy2000.survivalgames.commands;

import me.beechboy2000.survivalgames.LobbyManager;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetLobby implements SubCommand{

    @Override
    public boolean onCommand(Player player, String[] args) {

       LobbyManager.getInstance().setLobbySignsFromSelection(player);
       return true;
    }

    //TODO: TAKE A W.E SELECTIONA AND SET THE LOBBY. ALSO SET LOBBY WALL
    
    
    
    
    
    
}

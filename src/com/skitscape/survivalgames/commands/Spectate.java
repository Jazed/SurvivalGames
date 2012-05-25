package com.skitscape.survivalgames.commands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.skitscape.survivalgames.GameManager;
import com.skitscape.survivalgames.SettingsManager;

public class Spectate implements SubCommand{

    @Override
    public boolean onCommand(Player player, String[] args) {
        
        if(!player.hasPermission("sg.arena.spectate") && !player.isOp()){
            player.sendMessage(ChatColor.RED+"No Permission");
            return true;
        }
        
        if(args.length == 0){
            if(GameManager.getInstance().isSpectator(player)){
                GameManager.getInstance().removeSpectator(player);
                return true;
            }
            else{
                player.sendMessage(ChatColor.RED+"You are not spectating a game. Use /sg spectate <arenaid> to spectate!");
                return true;
            }
        }
        if(SettingsManager.getInstance().getSpawnCount(Integer.parseInt(args[0])) == 0){
            player.sendMessage(ChatColor.RED+"No spawns set!");
            return true;
        }
        if(GameManager.getInstance().isPlayerActive(player)){
            player.sendMessage(ChatColor.RED+"Cannot spectate while ingame!");
            return true;
        }
        GameManager.getInstance().getGame(Integer.parseInt(args[0])).addSpectator(player);
        player.sendMessage(ChatColor.GREEN+"You are now spectating! /sg spectate again to return to lobby");
        return true;
    }

    @Override
    public String help(Player p) {
        return "/sg sepctate <id> - Allows you to spectate a game";
    }

}

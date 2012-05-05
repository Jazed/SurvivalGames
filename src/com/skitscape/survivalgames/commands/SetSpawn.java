package com.skitscape.survivalgames.commands;

import java.util.HashMap;


import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.skitscape.survivalgames.GameManager;
import com.skitscape.survivalgames.SettingsManager;

public class SetSpawn implements SubCommand{

    HashMap<Integer, Integer>next = new HashMap<Integer,Integer>();

    
    public SetSpawn(){
    }

    public void loadNextSpawn(){
        for(int a = 1; a<=GameManager.getInstance().getGameCount();a++){
            next.put(a, SettingsManager.getInstance().getSpawnCount(a)+1);
        }
    }
    
    public boolean onCommand(Player player, String[] args) {
        loadNextSpawn();
        System.out.println("settings spawn");
        Location l =  player.getLocation();
        int game =  GameManager.getInstance().getBlockGameId(l.toVector());
        System.out.println(game+" "+next.size());
        if(game == -1){
            player.sendMessage(ChatColor.RED+"Must be in an arena!");
        }
        int i = 0;
        if(args[0].equalsIgnoreCase("next")){
            i = next.get(game);
            next.put(game, next.get(game)+1);
        }
        else{
            try{
            i = Integer.parseInt(args[1]);
            }catch(Exception e){
                player.sendMessage(ChatColor.RED+"Malformed input. Must be \"next\" or a number");return false;
            }
        }
        if(i == -1){
            player.sendMessage(ChatColor.RED+"You must be inside an arnea");
            return true;
        }
        SettingsManager.getInstance().setSpawn(game, i, l.toVector());
        player.sendMessage(ChatColor.GREEN+"Spawn "+i +" in arena "+game+" set!");

        return true;
    }
}

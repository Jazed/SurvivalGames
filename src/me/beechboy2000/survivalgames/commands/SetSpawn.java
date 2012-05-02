package me.beechboy2000.survivalgames.commands;

import java.util.HashMap;

import me.beechboy2000.survivalgames.GameManager;
import me.beechboy2000.survivalgames.SettingsManager;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetSpawn implements SubCommand{

    HashMap<Integer, Integer>next = new HashMap<Integer,Integer>();

    
    public SetSpawn(){
        for(int a = 1; a<=GameManager.getInstance().getGameCount();a++){
            next.put(a, SettingsManager.getInstance().getSpawnCount(a)+1);
        }
    }

    public boolean onCommand(Player player, String[] args) {
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
        else if(args[0].equals("([0-9])")){
            i = Integer.parseInt(args[1]);
        }
        if(i == -1){
            player.sendMessage(ChatColor.RED+"You must be inside an arnea");
            return true;
        }
        if(player.hasPermission("survivalgames.admin.setspawn")) {
        SettingsManager.getInstance().setSpawn(game, i, l.toVector());
        player.sendMessage(ChatColor.GREEN+"Spawn "+i +" in arena "+game+" set!");
        }
        return true;
    }
}

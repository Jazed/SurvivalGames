package me.beechboy2000.survivalgames.commands;

import java.util.HashMap;

import me.beechboy2000.survivalgames.GameManager;
import me.beechboy2000.survivalgames.SettingsManager;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetSpawn {

    public SetSpawn(){
        for(int a = 0; a<GameManager.getInstance().getGameCount();a++){
            next.put(a, 0);
        }
    }
    HashMap<Integer, Integer>next = new HashMap<Integer,Integer>();

    public boolean onCommand(CommandSender sender, Command cmd1, String commandLabel, String[] args){
        String cmd = cmd1.getName();
        Player player = null;
        if (sender instanceof Player) {
            player = (Player) sender;
        }

        if(args[0].equalsIgnoreCase("setspawn")){ 
            Location l =  player.getLocation();
            int game =  GameManager.getInstance().getBlockGameId(l.toVector());
            int i = 0;
            if(args[1].equalsIgnoreCase("next")){
                i = next.get(game);
                next.put(game, next.get(game)+1);
            }
            else if(args[1].equals("([0-9])")){
                i = Integer.parseInt(args[1]);
            }
            if(i == -1){
                player.sendMessage(ChatColor.RED+"You must be inside an arnea");
                return true;
            }
            SettingsManager.getInstance().setSpawn(game, i, l.toVector());
            player.sendMessage(ChatColor.GREEN+"Spawn "+i +" in arena "+game+" set!");
        }
        return true;
    }
}

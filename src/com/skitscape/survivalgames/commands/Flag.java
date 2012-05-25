package com.skitscape.survivalgames.commands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.skitscape.survivalgames.Game;
import com.skitscape.survivalgames.GameManager;
import com.skitscape.survivalgames.SettingsManager;

public class Flag implements SubCommand {

    @Override
    public boolean onCommand(Player player, String[] args) {
        
        if(!player.hasPermission("sg.arena.flag")){
            player.sendMessage(ChatColor.RED + "No Permission");
            return true;
        }
        if(args.length < 2){
            player.sendMessage(help(player));
            return true;
        }
        
        Game g = GameManager.getInstance().getGame(Integer.parseInt(args[0]));
        
        if(g == null){
            player.sendMessage(ChatColor.RED+"Arena does not exist!");
            return true;
        }
        
        if(args[1].equals("vip")){
           try{ 
               SettingsManager.getInstance().getSystemConfig().set("sg-system.arenas."+args[2]+".vip", args[3]);
               player.sendMessage(ChatColor.GREEN+"Vip"+((args[3].equalsIgnoreCase("true"))?"enabled":"disabled") +" on arena "+args[0]);
               return true;
           }catch(Exception e){player.sendMessage(help(player)); return true;}
        
        
        }
        
        
        return false;
    }

    @Override
    public String help(Player p) {
        return "/sg flag <id> <flag> <value> - Settings an arena specific setting";
    }

    
    
    
}

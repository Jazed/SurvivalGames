package com.skitscape.survivalgames.commands;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import com.skitscape.survivalgames.Game;
import com.skitscape.survivalgames.GameManager;
import com.skitscape.survivalgames.SettingsManager;

public class DelArena implements SubCommand{

    @Override
    public boolean onCommand(Player player, String[] args) {
        
        if(args.length != 1){
            player.sendMessage(ChatColor.RED+"Please specify an arena");
            return true;
        }
        
        FileConfiguration s = SettingsManager.getInstance().getSystemConfig();
        FileConfiguration spawn = SettingsManager.getInstance().getSpawns();
        for(Game g: GameManager.getInstance().getGames()){
            g.disable();
        }
        int arena = Integer.parseInt(args[0]);
        s.set("sg-system.arenas."+arena, null);
        s.set("sg-system.arenano", s.getInt("sg-system.arenano") - 1);
        spawn.set("spawns."+arena, null);
        player.sendMessage(ChatColor.GREEN+"Arena deleted");

        SettingsManager.getInstance().saveSystemConfig();
        GameManager.getInstance().reloadGames();
       
        return true;
    }

    @Override
    public String help(Player p) {
        return "/sg delarena <id> - Deletes an arena";
        
    }

    
    
    
    
}

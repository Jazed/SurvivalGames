package com.skitscape.survivalgames.commands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.skitscape.survivalgames.Game;
import com.skitscape.survivalgames.GameManager;

public class Disable implements SubCommand{

    @Override
    public boolean onCommand(Player player, String[] args) {        
        if(!player.hasPermission("sg.arena.disable") && !player.isOp()){
            player.sendMessage(ChatColor.RED+"No Permission");
            return true;
        }

        if(args.length == 0){
            for(Game g: GameManager.getInstance().getGames()){
                g.disable();
            }
            player.sendMessage(ChatColor.GREEN+"All Arenas disabled");

        }else{

            GameManager.getInstance().disableGame(Integer.parseInt(args[0]));
            player.sendMessage(ChatColor.GREEN+"Arena "+args[0]+" disabled");
        }
        return true;
    }
    @Override
    public String help(Player p) {
        return "/sg disable <id> - Disabled arena <id>";
    }
}

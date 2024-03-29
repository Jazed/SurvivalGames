package com.skitscape.survivalgames.commands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.skitscape.survivalgames.GameManager;

public class Enable implements SubCommand{

        @Override
        public boolean onCommand(Player player, String[] args) {        
            if(!player.hasPermission("sg.arena.enable") && !player.isOp()){
                player.sendMessage(ChatColor.RED+"No Permission");
                return true;
            }
            GameManager.getInstance().enableGame(Integer.parseInt(args[0]));
            player.sendMessage(ChatColor.GREEN+"Arena "+ args[0]+ " Enabled");
            return false;
        }

        @Override
        public String help(Player p) {
            return "/sg enable <id> - enables a the arena <id>";
        }
    }


package com.skitscape.survivalgames.commands;

import org.bukkit.entity.Player;

import com.skitscape.survivalgames.GameManager;

public class Leave implements SubCommand {

    public boolean onCommand(Player player, String[] args) {
        GameManager.getInstance().removePlayer(player);
        return true;
    }
    
    @Override
    public String help(Player p) {
        return "/sg leave - Leaves the game";
    }

}

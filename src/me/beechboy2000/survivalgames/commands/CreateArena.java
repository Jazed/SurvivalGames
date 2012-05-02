package me.beechboy2000.survivalgames.commands;

import me.beechboy2000.survivalgames.GameManager;

import org.bukkit.entity.Player;

public class CreateArena implements SubCommand{

    public boolean onCommand(Player player, String[] args) {
    	if(player.hasPermission("survivalgames.admin.createarena")) {
        GameManager.getInstance().createArenaFromSelection(player);
    	}
        return true;
    }


}

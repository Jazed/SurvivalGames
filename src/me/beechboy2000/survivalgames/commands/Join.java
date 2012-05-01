package me.beechboy2000.survivalgames.commands;

import me.beechboy2000.survivalgames.GameManager;

import org.bukkit.entity.Player;

public class Join implements SubCommand{

    public boolean onCommand(Player player, String[] args) {

        GameManager.getInstance().autoAddPlayer(player);

        return true;


    }

}


package com.skitscape.survivalgames.commands;

import org.bukkit.entity.Player;

import com.skitscape.survivalgames.GameManager;

public class Leave implements SubCommand {

    public boolean onCommand(Player player, String[] args) {
        System.out.println("ASFasdfsadfsdafasdfasdfsdf");
        GameManager.getInstance().removePlayer(player);
        return true;


    }

}

package com.skitscape.survivalgames.commands;


import org.bukkit.entity.Player;

import com.skitscape.survivalgames.GameManager;
import com.skitscape.survivalgames.SettingsManager;

public class Join implements SubCommand{

    public boolean onCommand(Player player, String[] args) {

        player.teleport(SettingsManager.getInstance().getLobbySpawn());
        return true;


    }

    @Override
    public String help(Player p) {
        return "/sg join - Join the lobby";
    }
}


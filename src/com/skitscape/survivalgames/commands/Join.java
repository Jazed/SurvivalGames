package com.skitscape.survivalgames.commands;


import org.bukkit.entity.Player;

import com.skitscape.survivalgames.GameManager;

public class Join implements SubCommand{

    public boolean onCommand(Player player, String[] args) {

        GameManager.getInstance().autoAddPlayer(player);
        return true;


    }

    @Override
    public String help(Player p) {
        return "/sg setlobbywall - Setings the lobby stats wall";
    }
}


package com.skitscape.survivalgames.commands;


import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.skitscape.survivalgames.GameManager;

public class CreateArena implements SubCommand{

    public boolean onCommand(Player player, String[] args) {

        GameManager.getInstance().createArenaFromSelection(player);

        return true;
    }


}

package me.beechboy2000.survivalgames.commands;

import me.beechboy2000.survivalgames.GameManager;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CreateArena implements SubCommand{

    public boolean onCommand(Player player, String[] args) {

        GameManager.getInstance().createArenaFromSelection(player);

        return true;
    }


}

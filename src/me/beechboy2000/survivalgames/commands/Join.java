package me.beechboy2000.survivalgames.commands;

import me.beechboy2000.survivalgames.GameManager;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Join implements CommandExecutor{

    public boolean onCommand(CommandSender sender, Command cmd1, String commandLabel, String[] args){
        String cmd = cmd1.getName();
        Player player = null;
        if (sender instanceof Player) {
            player = (Player) sender;
        }

        if(args[0].equalsIgnoreCase("join")){

            GameManager.getInstance().autoAddPlayer(player);

        }


        return true;


    }

}


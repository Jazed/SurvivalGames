package com.skitscape.survivalgames;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;



import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.skitscape.survivalgames.commands.AddPlayer;
import com.skitscape.survivalgames.commands.CreateArena;
import com.skitscape.survivalgames.commands.GameCount;
import com.skitscape.survivalgames.commands.Join;
import com.skitscape.survivalgames.commands.SetLobbyWall;
import com.skitscape.survivalgames.commands.SetSpawn;
import com.skitscape.survivalgames.commands.SubCommand;

public class CommandHandler implements CommandExecutor
{
    private Plugin plugin;
    private HashMap<String, SubCommand> commands;

    public CommandHandler(Plugin plugin)
    {
        this.plugin = plugin;
        commands = new HashMap<String, SubCommand>();
        loadCommands();
    }

    private void loadCommands()
    {
        commands.put("createarena", new CreateArena());
        commands.put("addplayer", new AddPlayer());
        // commands.put("leave", new Leave());
        commands.put("join", new Join());
        commands.put("setlobbywall", new SetLobbyWall());
        commands.put("setspawn", new SetSpawn());
        commands.put("getcount", new GameCount());

    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd1, String commandLabel, String[] args){
        String cmd = cmd1.getName();
        Player player = null;
        if (sender instanceof Player) {
            player = (Player) sender;
        }

        if(cmd.equalsIgnoreCase("survivalgames")){ 
            if(args == null || args[0] == null)
                return false;
            String sub = args[0];

            Vector<String> l  = new Vector<String>();
            l.addAll(Arrays.asList(args));
            l.remove(0);
            args = (String[]) l.toArray(new String[0]);
            commands.get(sub).onCommand(
                    player, 
                    args);
            return true;
        }
        return false;
    }
}

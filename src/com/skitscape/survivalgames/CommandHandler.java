package com.skitscape.survivalgames;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;



import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;

import com.skitscape.survivalgames.commands.CreateArena;
import com.skitscape.survivalgames.commands.DelArena;
import com.skitscape.survivalgames.commands.Disable;
import com.skitscape.survivalgames.commands.Enable;
import com.skitscape.survivalgames.commands.Flag;
import com.skitscape.survivalgames.commands.ForceStart;
import com.skitscape.survivalgames.commands.GameCount;
import com.skitscape.survivalgames.commands.Join;
import com.skitscape.survivalgames.commands.Leave;
import com.skitscape.survivalgames.commands.ResetSpawns;
import com.skitscape.survivalgames.commands.SetLobbySpawn;
import com.skitscape.survivalgames.commands.SetLobbyWall;
import com.skitscape.survivalgames.commands.SetSpawn;
import com.skitscape.survivalgames.commands.Spectate;
import com.skitscape.survivalgames.commands.Start;
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
        commands.put("join", new Join());
        commands.put("setlobbywall", new SetLobbyWall());
        commands.put("setspawn", new SetSpawn());
        commands.put("getcount", new GameCount());
        commands.put("disable", new Disable());
        commands.put("start", new ForceStart());
        commands.put("enable", new Enable());
        commands.put("vote", new Start());
        commands.put("leave", new Leave());
        commands.put("setlobbyspawn", new SetLobbySpawn());
        commands.put("resetspawns", new ResetSpawns());
        commands.put("delarena", new DelArena());
        commands.put("flag", new Flag());
        commands.put("spectate", new Spectate());


    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd1, String commandLabel, String[] args){
        String cmd = cmd1.getName();
        PluginDescriptionFile pdfFile = plugin.getDescription();

        Player player = null;
        if (sender instanceof Player) {
            player = (Player) sender;
        }

        if(SurvivalGames.dbcon == false){
            player.sendMessage("Could not connect to the database. Plugin disabled");
            return true;
        }
        
        
        if(cmd.equalsIgnoreCase("survivalgames")){ 
            if(args == null || args.length < 1){
                player.sendMessage(ChatColor.GOLD +""+ ChatColor.BOLD +"Survival Games "+ChatColor.RESET+  ChatColor.YELLOW +" Version: "+ pdfFile.getVersion() );
                player.sendMessage(ChatColor.GOLD +"Type /sg help for help" );

                return true;
            }
            if(args[0].equalsIgnoreCase("help")){
                help(player);
                return true;
            }
            String sub = args[0];

            Vector<String> l  = new Vector<String>();
            l.addAll(Arrays.asList(args));
            l.remove(0);
            args = (String[]) l.toArray(new String[0]);
            if(!commands.containsKey(sub)){
                player.sendMessage(ChatColor.RED+"Command dosent exist.");
                player.sendMessage(ChatColor.DARK_AQUA +"Type /sg help for help" );
                return true;
            }
            try{
                
            commands.get(sub).onCommand( player,  args);
            }catch(Exception e){e.printStackTrace(); player.sendMessage(ChatColor.RED+"An error occured while executing the command. Check the      console");                player.sendMessage(ChatColor.BLUE +"Type /sg help for help" );
}
            return true;
        }
        return false;
    }
    
    public void help(Player p){
        p.sendMessage("/sg <command> <args>");
        for(SubCommand v: commands.values()){
            p.sendMessage(ChatColor.AQUA +v.help(p));
        }
    }
}

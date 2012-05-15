package com.skitscape.survivalgames.Events;


import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import com.skitscape.survivalgames.GameManager;
import com.skitscape.survivalgames.SettingsManager;

public class CommandCatch implements Listener{

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerDieEvent(PlayerCommandPreprocessEvent event) {
        String m = event.getMessage();

        if(!GameManager.getInstance().isPlayerActive(event.getPlayer()) && !GameManager.getInstance().isPlayerInactive(event.getPlayer()))
            return;
        if(m.equalsIgnoreCase("/list")){
            String act = ChatColor.AQUA+"[Active]";
            String deact = ChatColor.RED +"[Inactive]";
            for(Player p:GameManager.getInstance().getGame(GameManager.getInstance().getPlayerGameId(event.getPlayer())).getPlayers()[0]){
                act += p.getName()+", ";
            }
            for(Player p:GameManager.getInstance().getGame(GameManager.getInstance().getPlayerGameId(event.getPlayer())).getPlayers()[1]){
                deact += p.getName()+", ";
            }

            event.getPlayer().sendMessage(act);
            event.getPlayer().sendMessage(deact);
        }
        if(!SettingsManager.getInstance().getConfig().getBoolean("disallow-commands"))
            return;
        else if(m.startsWith("/sg") || m.startsWith("/survivalgames")|| m.startsWith("/hg")||m.startsWith("/hungergames")){
            return;
        }
        event.setCancelled(true);
    }
}

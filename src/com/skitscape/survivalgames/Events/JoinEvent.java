package com.skitscape.survivalgames.Events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import com.skitscape.survivalgames.GameManager;
import com.skitscape.survivalgames.SettingsManager;

public class JoinEvent implements Listener {
    @EventHandler
    public void PlayerJoin(PlayerJoinEvent e){
        Player p = e.getPlayer();
        if(GameManager.getInstance().getBlockGameId(p.getLocation()) != -1){
            p.teleport(SettingsManager.getInstance().getLobbySpawn());
        }
    }
    
}

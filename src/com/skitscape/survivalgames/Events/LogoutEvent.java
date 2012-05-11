package com.skitscape.survivalgames.Events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import com.skitscape.survivalgames.GameManager;

public class LogoutEvent implements Listener{

    
    @EventHandler
    public void PlayerLoggout(PlayerQuitEvent e){
        Player p = e.getPlayer();
        int id = GameManager.getInstance().getPlayerGameId(p);
        if(id == -1) return;
        GameManager.getInstance().getGame(id).killPlayer(p);
    }
    
}

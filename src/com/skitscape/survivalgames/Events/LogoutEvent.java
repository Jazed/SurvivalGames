package com.skitscape.survivalgames.Events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import com.skitscape.survivalgames.Game;
import com.skitscape.survivalgames.GameManager;

public class LogoutEvent implements Listener{

    
    @EventHandler
    public void PlayerLoggout(PlayerQuitEvent e){
        Player p = e.getPlayer();
        int id = GameManager.getInstance().getPlayerGameId(p);
        if(id == -1) return;
        if(GameManager.getInstance().getGameMode(id)==Game.GameMode.INGAME)
            GameManager.getInstance().getGame(id).killPlayer(p, true);
        else
            GameManager.getInstance().getGame(id).removePlayer(p);
    }
    
}

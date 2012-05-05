package com.skitscape.survivalgames.Events;


import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import com.skitscape.survivalgames.Game;
import com.skitscape.survivalgames.GameManager;
import com.skitscape.survivalgames.SettingsManager;
import com.skitscape.survivalgames.SurvivalGames;

public class MoveEvent implements Listener{

    @EventHandler(priority = EventPriority.HIGHEST)
    public void outOfBoundsHandler(PlayerMoveEvent e){
        if(SettingsManager.getGameWorld() == null)
            return;
        if(e.getPlayer().getWorld()!=SettingsManager.getGameWorld())
            return;
        if(!GameManager.getInstance().isPlayerActive(e.getPlayer()))
            return;
        if(GameManager.getInstance().getBlockGameId(e.getPlayer().getLocation().toVector())==-1 && GameManager.getInstance().getGameMode(GameManager.getInstance().getPlayerGameId(e.getPlayer())) == Game.GameMode.WAITING)
            return;
        else
            e.setCancelled(true);

    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void frozenSpawnHandler(PlayerMoveEvent e){
        if(e.getPlayer().getWorld()!=SettingsManager.getGameWorld())
            return;
        if(GameManager.getInstance().isPlayerActive(e.getPlayer()) && GameManager.getInstance().getGameMode(GameManager.getInstance().getPlayerGameId(e.getPlayer())) != Game.GameMode.INGAME)
            e.setCancelled(true);
    }
}

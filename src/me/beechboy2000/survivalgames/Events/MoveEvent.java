package me.beechboy2000.survivalgames.Events;

import me.beechboy2000.survivalgames.Game;
import me.beechboy2000.survivalgames.GameManager;
import me.beechboy2000.survivalgames.SettingsManager;
import me.beechboy2000.survivalgames.SurvivalGames;

import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class MoveEvent implements Listener{

    @EventHandler(priority = EventPriority.HIGHEST)
    public void outOfBoundsHandler(PlayerMoveEvent e){
        if(e.getPlayer().getWorld()!=SettingsManager.getGameWorld())
            return;
        if(!GameManager.getInstance().isPlayerActive(e.getPlayer()))
            return;
        if(GameManager.getInstance().getBlockGameId(e.getPlayer().getLocation().toVector())==-1)
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

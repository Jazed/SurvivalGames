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
      /*  Optimization for single game world. No longer works since support for multiple worlds was added
       * if(SettingsManager.getGameWorld() == null)
            return;
        if(e.getPlayer().getWorld()!=SettingsManager.getGameWorld())
            return;*/
        if(!GameManager.getInstance().isPlayerActive(e.getPlayer()))
            return;
        if(GameManager.getInstance().getBlockGameId(e.getPlayer().getLocation().toVector())==-1 && GameManager.getInstance().getGameMode(GameManager.getInstance().getPlayerGameId(e.getPlayer())) == Game.GameMode.WAITING)
            return;
        else
            e.setCancelled(true);

    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void frozenSpawnHandler(PlayerMoveEvent e){
<<<<<<< HEAD
        /*  Optimization for single game world. No longer works since support for multiple worlds was added
        *if(e.getPlayer().getWorld()!=SettingsManager.getGameWorld())
            return;*/
        if(GameManager.getInstance().getPlayerGameId(e.getPlayer()) == -1)
            return;
        if(GameManager.getInstance().getGame(GameManager.getInstance().getPlayerGameId(e.getPlayer())).getMode() == Game.GameMode.INGAME)
            return;
        if(GameManager.getInstance().isPlayerActive(e.getPlayer()) && GameManager.getInstance().getGameMode(GameManager.getInstance().getPlayerGameId(e.getPlayer())) != Game.GameMode.INGAME){
            Location l = e.getPlayer().getLocation();
            l.setX(l.getBlockX());
            l.setY(l.getBlockY());
            l.setZ(l.getBlockZ());
            l.setYaw(e.getPlayer().getLocation().getYaw());
            l.setPitch(e.getPlayer().getLocation().getPitch());
            e.getPlayer().teleport(l);
        }
=======
        if(e.getPlayer().getWorld()!=SettingsManager.getGameWorld())
            return;
        if(GameManager.getInstance().isPlayerActive(e.getPlayer()) && GameManager.getInstance().getGameMode(GameManager.getInstance().getPlayerGameId(e.getPlayer())) != Game.GameMode.INGAME)
            e.setCancelled(true);
>>>>>>> 879e91097d00b53f529c2afc3532bd5f5d8e077a
    }
}

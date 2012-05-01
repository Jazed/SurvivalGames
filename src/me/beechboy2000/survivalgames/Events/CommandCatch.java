package me.beechboy2000.survivalgames.Events;

import me.beechboy2000.survivalgames.GameManager;
import me.beechboy2000.survivalgames.SettingsManager;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class CommandCatch implements Listener{

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerDieEvent(PlayerCommandPreprocessEvent event) {
        String m = event.getMessage();
        if(!SettingsManager.getInstance().getConfig().getBoolean("disallow-commands"))
            return;
        if(!GameManager.getInstance().isPlayerActive(event.getPlayer()))
            return;
        else if(m.equalsIgnoreCase("/sg") || m.equalsIgnoreCase("/survivalgames")|| m.equalsIgnoreCase("/hg")||m.equalsIgnoreCase("/hungergames")){
            return;
        }
        event.setCancelled(true);
    }
}

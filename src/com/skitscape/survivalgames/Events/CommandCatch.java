package com.skitscape.survivalgames.Events;


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

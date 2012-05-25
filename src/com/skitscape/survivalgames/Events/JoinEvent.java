package com.skitscape.survivalgames.Events;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;

import com.skitscape.survivalgames.GameManager;
import com.skitscape.survivalgames.SettingsManager;
import com.skitscape.survivalgames.util.UpdateChecker;

public class JoinEvent implements Listener {
    
    Plugin plugin;
    
    public JoinEvent(Plugin plugin){
        this.plugin = plugin;
    }
    
    @EventHandler
    public void PlayerJoin(PlayerJoinEvent e){
        final Player p = e.getPlayer();
        if(GameManager.getInstance().getBlockGameId(p.getLocation()) != -1){
            p.teleport(SettingsManager.getInstance().getLobbySpawn());
        }
        if(p.isOp() || p.hasPermission("sg.system.updatenotify")){
            Bukkit.getServer().getScheduler().scheduleAsyncDelayedTask(plugin, new Runnable() {

                public void run() {
                    System.out.println("Checking for updates");
                    new UpdateChecker().check(p, plugin);
                }
             }, 60L);
        }
    }
    
}

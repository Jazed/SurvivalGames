package org.curlybrace.double0negative.logging;

import me.beechboy2000.survivalgames.SettingsManager;
import me.beechboy2000.survivalgames.SurvivalGames;

import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.block.BlockFormEvent;
import org.bukkit.event.block.BlockGrowEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class LoggingManager implements  Listener{

    private static LoggingManager instance = new LoggingManager();

    private LoggingManager(){

    }

    public static LoggingManager getInstance(){
        return instance;
    }


    @EventHandler(priority = EventPriority.MONITOR)
    public void blockChanged(BlockBreakEvent e){
        logBlockDestoryed(e.getBlock());
    }
    @EventHandler(priority = EventPriority.MONITOR)
    public void blockChanged(BlockPlaceEvent e){
        logBlockCreated(e.getBlock());
    }
   /* @EventHandler(priority = EventPriority.MONITOR)
    public void blockChanged(BlockPhysicsEvent e){
        logBlockCreated(e.getBlock());
    }*/
    @EventHandler(priority = EventPriority.MONITOR)
    public void blockChanged(BlockFadeEvent e){
        logBlockDestoryed(e.getBlock());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void blockChanged(BlockBurnEvent e){
        logBlockDestoryed(e.getBlock());
    }
    @EventHandler(priority = EventPriority.MONITOR)
    public void blockChanged(BlockGrowEvent e){
        logBlockCreated(e.getBlock());
    }
    @EventHandler(priority = EventPriority.MONITOR)
    public void blockChanged(BlockFormEvent e){
        logBlockCreated(e.getBlock());
    }

    public void logBlockCreated(Block b){
        System.out.println(b.getTypeId());
        if(b.getWorld() == SettingsManager.getGameWorld()){
            QueueManager.getInstance().add(
                    new BlockData( 
                            b.getWorld().getName(),
                            0,
                            (byte)0,
                            b.getTypeId(),
                            b.getData(),
                            b.getX(),
                            b.getY(),
                            b.getZ()));
        }
    }

    public void logBlockDestoryed(Block b){
        if(b.getWorld() == SettingsManager.getGameWorld()){

            QueueManager.getInstance().add(
                    new BlockData( 
                            b.getWorld().getName(),
                            b.getTypeId(),
                            b.getData(),
                            0,
                            (byte)0,
                            b.getX(),
                            b.getY(),
                            b.getZ()));
        }
    }

}

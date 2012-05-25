package com.skitscape.survivalgames.logging;


import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.block.BlockFormEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockGrowEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.util.Vector;

import com.skitscape.survivalgames.Game;
import com.skitscape.survivalgames.GameManager;
import com.skitscape.survivalgames.SettingsManager;
import com.skitscape.survivalgames.SurvivalGames;

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
    public void blockChange(EntityExplodeEvent e){
        for(Block b :e.blockList()){
            logBlockDestoryed(b);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void blockChange(BlockIgniteEvent e){
        logBlockCreated(e.getBlock());

    }
    @EventHandler(priority = EventPriority.MONITOR)
    public void blockChanged(BlockBurnEvent e){
        logBlockDestoryed(e.getBlock() );
    }
    @EventHandler(priority = EventPriority.MONITOR)
    public void blockChanged(BlockGrowEvent e){
        logBlockCreated(e.getBlock());
    }
    @EventHandler(priority = EventPriority.MONITOR)
    public void blockChanged(BlockFormEvent e){
        logBlockCreated(e.getBlock());
    }
    @EventHandler(priority = EventPriority.MONITOR)
    public void blockChanged(BlockRedstoneEvent e){
        logBlockDestoryed(e.getBlock());
    }
    @EventHandler(priority = EventPriority.MONITOR)
    public void blockChanged(LeavesDecayEvent e){
        logBlockDestoryed(e.getBlock());
    }
    @EventHandler(priority = EventPriority.MONITOR)
    public void blockChanged(BlockFromToEvent e){
            logBlockDestoryed(e.getBlock());
    }
    
    public void blockChange(BlockPistonExtendEvent e){
        for(Block b: e.getBlocks()){
            logBlockCreated(b);
        }
    }

    public void logBlockCreated(Block b){
        if(GameManager.getInstance().getBlockGameId(b.getLocation()) == -1)
            return;
        if( GameManager.getInstance().getGameMode(GameManager.getInstance().getBlockGameId(b.getLocation())) == Game.GameMode.DISABLED)
            return ;
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


    public void logBlockDestoryed(Block b){
        if(GameManager.getInstance().getBlockGameId(b.getLocation()) == -1)
            return;
        if( GameManager.getInstance().getGameMode(GameManager.getInstance().getBlockGameId(b.getLocation())) == Game.GameMode.DISABLED)
            return ;
        if(b.getTypeId() == 51)
            return;
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

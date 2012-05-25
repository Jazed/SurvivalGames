package com.skitscape.survivalgames.util;

import java.util.Iterator;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;

import com.skitscape.survivalgames.Game;
import com.skitscape.survivalgames.GameManager;
import com.skitscape.survivalgames.SettingsManager;
import com.skitscape.survivalgames.logging.QueueManager;

public class GameReset {

    int gameid;
    Game g;

    public GameReset(Game g){
        this.gameid = g.getID();
        this.g = g;
    }

    public void resetArena(){
        QueueManager.getInstance().rollback(this,gameid);

    }

    public void rollbackFinishedCallback(){
        g.resetCallback();
    }


}

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
        removeDrops();
        QueueManager.getInstance().rollback(this,gameid);

    }

    public void rollbackFinishedCallback(){
        g.resetCallback();
    }

    public void removeDrops(){
        List<Entity> list = SettingsManager.getGameWorld(gameid).getEntities();
        for (Iterator<Entity> entities = list.iterator();entities.hasNext();){
            if (entities.hasNext()){
                Entity entity = entities.next();
                if (entity instanceof Item){
                    Item iteme = (Item) entity;
                        Location loce = entity.getLocation();
                        if(GameManager.getInstance().getBlockGameId(loce) == gameid){
                            iteme.remove();
                    }
                }
            }
        }
    }
}

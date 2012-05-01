package me.beechboy2000.survivalgames;

import java.util.ArrayList;

import me.beechboy2000.survivalgames.Game.GameMode;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.bukkit.selections.Selection;

public class GameManager {

    static GameManager instance = new GameManager();
    private ArrayList<Game>games = new ArrayList<Game>();
    private SurvivalGames p;
    
    
    private GameManager(){
        
    }
    
    public static GameManager getInstance(){
        return instance;
    }
    
    public void setup(SurvivalGames plugin){
        p = plugin;
        LoadGames();
    }
    
    
    public void LoadGames(){
       FileConfiguration c = SettingsManager.getInstance().getConfig();
       for(int a = 1; a<= c.getInt("system.arenano",0); a++){
           games.add(new Game(a));
       }
    }
    
    
    public int getBlockGameId(Vector v){
        for(Game g: games){
            if(g.isBlockInArena(v)){
                return g.getID();
            }
        }
        return -1;
    }
    
    public int getPlayerGameId(Player p){
        for(Game g:games){
            if(g.hasPlayer(p)){
                return g.getID();
            }
        }
        return -1;
    }

    public boolean isPlayerActive(Player player) {
        for(Game g:games){
            if(g.isPlayerActive(player))
                return true;
        }
        return false;
    }
    
    public int getGameCount(){
        return games.size();
    }
    
    public GameMode getGameMode(int a){
        for(Game g: games){
            if(g.getID() == 4){
                return g.getMode();
            }
        }
        return null;
    }
    
    public void autoAddPlayer(Player pl){
        ArrayList<Game>qg = new ArrayList<Game>(5);
        for(Game g: games){
            if(g.getMode() == Game.GameMode.WAITING)
                qg.add(g);
        }
        //TODO: fancy auto balance algorithm
        
        if(qg.size() == 0){
            pl.sendMessage(ChatColor.RED+"No games to join");
            return;
        }
        
        qg.get(0).addPlayer(pl);
        
    }
    
    public void createArenaFromSelection(Player pl){
        FileConfiguration c = SettingsManager.getInstance().getConfig();
        if(c.getString("system.world") == null){
            c.set("system.world", pl.getWorld());
        } 
        
        WorldEditPlugin we = p.getWorldEdit();
        Selection sel = we.getSelection(pl);
        if(sel == null){
            pl.sendMessage(ChatColor.RED+"You must make a WorldEdit Selection first");
            return;
        }
        Location max = sel.getMaximumPoint();
        Location min = sel.getMinimumPoint();
        
        if(max.getWorld()!=SettingsManager.getGameWorld() || min.getWorld()!=SettingsManager.getGameWorld()){
            pl.sendMessage(ChatColor.RED+"Wrong World!");
            return;
        }
        

        int no = c.getInt("system.arenano") + 1;
        c.set("system.arenano", no);
        c.set("system.arenas."+no+"x1", max.getBlockX());
        c.set("system.arenas."+no+"y1", max.getBlockY());
        c.set("system.arenas."+no+"z1", max.getBlockZ());
        c.set("system.arenas."+no+"x2", min.getBlockX());
        c.set("system.arenas."+no+"y2", min.getBlockY());
        c.set("system.arenas."+no+"z2", min.getBlockZ());
        
        
        
        hotAddArena(no);
        
        p.saveConfig();
        
        pl.sendMessage(ChatColor.GREEN+"Arena ID "+no+"Succesfully added");
        
    }

    private void hotAddArena(int no) {
        games.add(new Game(no));
    }
    
    
}

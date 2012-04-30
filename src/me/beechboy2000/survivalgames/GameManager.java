package me.beechboy2000.survivalgames;

import java.util.ArrayList;

import me.beechboy2000.survivalgames.Game.GameMode;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class GameManager {

    static GameManager instance = new GameManager();
    private ArrayList<Game>games = new ArrayList<Game>();
    
    
    
    private GameManager(){
        
    }
    
    public static GameManager getInstance(){
        return instance;
    }
    
    public void LoadGames(){
       FileConfiguration c = SettingsManager.getInstance().getConfig();
       for(int a = 1; a<= c.getInt("max-games", 1); a++){
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
    
    
}

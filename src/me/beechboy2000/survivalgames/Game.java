package me.beechboy2000.survivalgames;

import java.util.ArrayList;

import org.bukkit.entity.Player;


//Data container for a game
public class Game {

    public enum GameMode {
        DISABLED ,LOADING, INACTIVE, WAITING, 
        STARTING, INGAME, FINISHING, RESETING
    }
    
    private GameMode mode = GameMode.DISABLED;
    private ArrayList<Player> activePlayers = new ArrayList<Player>();
    private ArrayList<Player> inactivePlayers = new ArrayList<Player>();
    private SettingsManager settings = SettingsManager.getInstance();
    
    public Game(){
        //mode = GameMode.LOADING;
        
        
        
        
    }
    
    
    
    public GameMode getGameMode(){
        return mode;
    }
    
    public boolean addPlayer(Player p){
        if(mode == GameMode.WAITING){
            activePlayers.add(p);
            return true;
        }
        return false;
    }
    
    public void removePlayer(Player p){
        activePlayers.remove(p);
        inactivePlayers.remove(p);
    }
    
    
}

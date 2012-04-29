package me.beechboy2000.survivalgames;

import java.util.ArrayList;

public class GameManager {

    static GameManager instance = new GameManager();
    private ArrayList<Game>games = new ArrayList<Game>();
    
    
    
    private GameManager(){
        
    }
    
    public static GameManager getInstance(){
        return instance;
    }
    
    public void LoadGames(){
       SettingsManager.getInstance();
    }
    
    
    public int getBlockGameId(){
        for(Game g: games){
            if(g.isBlockInArena()){
                return g.getID();
            }
        }
        return -1;
    }
    
    
    
    
    
}

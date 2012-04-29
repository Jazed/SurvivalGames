package me.beechboy2000.survivalgames;

import java.util.ArrayList;

public class GameManager {

    
    GameManager instance = new GameManager();
    private ArrayList<Game>games = new ArrayList<Game>();
    
    private GameManager(){
        
    }
    
    public GameManager getInstance(){
        return instance;
    }
    
    
    
    
}

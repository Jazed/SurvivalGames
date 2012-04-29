package me.beechboy2000.survivalgames;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;


//Data container for a game

public class Game {

    public enum GameMode {
        DISABLED ,LOADING, INACTIVE, WAITING, 
        STARTING, INGAME, FINISHING, RESETING
    }

    private GameMode mode = GameMode.DISABLED;
    private ArrayList<Player> activePlayers = new ArrayList<Player>();
    private ArrayList<Player> inactivePlayers = new ArrayList<Player>();
    private HashMap<String, Integer>gameSettings;
    private Arena arena;
    private int gameID;
    private FileConfiguration c;




    public Game(int gameid, HashMap<String, Integer> settings){
        gameID = gameid;
        gameSettings = settings;
        c = SettingsManager.getInstance().getConfig();


    }


    public void setup(){
        mode = GameMode.LOADING;


        int x = c.getInt("system.games."+gameID+"maxx");
        int y = c.getInt("system.games."+gameID+"maxy");
        int z = c.getInt("system.games."+gameID+"maxz");
        Vector max = new Vector(x,y,z);
        x     = c.getInt("system.games."+gameID+"minx");
        y     = c.getInt("system.games."+gameID+"miny");
        z     = c.getInt("system.games."+gameID+"minz");
        Vector min = new Vector(x,y,z);

        arena = new Arena(min,max);





    }



    public GameMode getGameMode(){
        return mode;
    }

    public boolean addPlayer(Player p){
        if(mode == GameMode.WAITING){
            activePlayers.add(p);
            if(activePlayers.size() >= gameSettings.get("START_PlayersNeeded"))
                countdown(gameSettings.get("START_CountdownTime"));
            return true;
        }
        return false;
    }

    public void removePlayer(Player p){
        activePlayers.remove(p);
        inactivePlayers.remove(p);
    }


    public int getID() {
        return gameID;
    }


    public boolean isBlockInArena(Vector v) {
        return arena.containsBlock(v);
    }

    public void startGame(){
        
    }
    public void countdown(int time){
        
        for(Player p: activePlayers){
            p.sendMessage("Game Starting in "+time);
        }
        if(SurvivalGames.isActive() && time > 1){
            new CountdownThread(time).start();
        }
        else if(SurvivalGames.isActive() && time-1 == 0){
            startGame();
        }
        else
            return;
    }

    class CountdownThread extends Thread{

        int time;
        public CountdownThread(int t){
            int time = t;
        }
        public void run() {
            try{Thread.sleep(1000);}catch(Exception e){}
            countdown(time-1);
        }

    }


}



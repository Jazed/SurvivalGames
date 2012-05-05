package com.skitscape.survivalgames;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;


//Data container for a game

public class Game {

    public static enum GameMode {
        DISABLED ,LOADING, INACTIVE, WAITING, 
        STARTING, INGAME, FINISHING, RESETING
    }

    private GameMode mode = GameMode.DISABLED;
    private ArrayList<Player> activePlayers = new ArrayList<Player>();
    private ArrayList<Player> inactivePlayers = new ArrayList<Player>();

    private Arena arena;
    private int gameID;
    private FileConfiguration c;
    private FileConfiguration s;
    private HashMap<Integer, Boolean>spawns = new HashMap<Integer, Boolean>();

    private int spawnCount = 0;



    public Game(int gameid){
        gameID = gameid;
        c = SettingsManager.getInstance().getConfig();

        s = SettingsManager.getInstance().getSystemConfig();
        setup();
    }


    public void setup(){
        mode = GameMode.LOADING;


        int x = s.getInt("sg-system.arenas."+gameID+".x1");
        int y = s.getInt("sg-system.arenas."+gameID+".y1");
        int z = s.getInt("sg-system.arenas."+gameID+".z1");
        System.out.println(x+ " "+ y +" "+z);
        Vector max = new Vector(x,y,z);
        x     = s.getInt("sg-system.arenas."+gameID+".x2");
        y     = s.getInt("sg-system.arenas."+gameID+".y2");
        z     = s.getInt("sg-system.arenas."+gameID+".z2");
        System.out.println(x+ " "+ y +" "+z);
        Vector min = new Vector(x,y,z);

        arena = new Arena(min,max);
        
        for(int a = 1; a<SettingsManager.getInstance().getSpawnCount(gameID); a++){
            spawns.put(a, false);
            spawnCount = a;
        }

        mode = GameMode.WAITING;
    }

    public void addSpawn(){
        spawnCount++;
        spawns.put(spawnCount, false);
    }


    public GameMode getGameMode(){
        return mode;
    }

    public boolean addPlayer(Player p){
        
        p.sendMessage(gameID+" "+ SettingsManager.getInstance().getSpawnCount(gameID)+" "+activePlayers.size());
        if(mode == GameMode.WAITING){
            if(activePlayers.size() < SettingsManager.getInstance().getSpawnCount(gameID)){
                activePlayers.add(p);
                p.sendMessage("Joining Arena " + gameID);
                boolean placed = false;
                for(int a = 1; a<SettingsManager.getInstance().getSpawnCount(gameID); a++){
                    if(!spawns.get(a)){
                        p.teleport(SettingsManager.getInstance().getSpawnPoint(gameID, a));
                        placed = true;
                    }
                }
                if(!placed){
                    p.sendMessage(ChatColor.RED + "Game "+gameID+" Is Full!");
                    activePlayers.remove(p);
                    return false;
                }
                
            }
            if(activePlayers.size() >= c.getInt("auto-start-players"))
                countdown(c.getInt("auto-start-time"));
            return true;
        }
        return false;
    }

    public int getActivePlayers(){
        return activePlayers.size();
    }
    
    public Player[][] getPlayers(){
        return new Player[][]{activePlayers.toArray(new Player[0]), inactivePlayers.toArray(new Player[0])};
    }

    public void removePlayer(Player p){
        activePlayers.remove(p);
        inactivePlayers.remove(p);
    }


    public int getID() {
        return gameID;
    }
    
    public void killPlayer(Player p){
        activePlayers.remove(p);
        inactivePlayers.add(p);
    }


    public boolean isBlockInArena(Vector v) {
        return arena.containsBlock(v);
    }

    public void startGame(){
        mode = GameMode.INGAME;
    }
    public void countdown(int time){
        if(time<10){
            for(Player p: activePlayers){
                p.sendMessage("Game Starting in "+time);
            }
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

    public boolean isPlayerActive(Player player) {
        return activePlayers.contains(player);
    }
    public boolean hasPlayer(Player p){
        return activePlayers.contains(p) || inactivePlayers.contains(p);
    }
    public GameMode getMode(){
        return mode;
    }


}



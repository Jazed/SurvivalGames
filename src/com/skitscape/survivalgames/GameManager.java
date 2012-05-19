package com.skitscape.survivalgames;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;


import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.bukkit.selections.Selection;
import com.skitscape.survivalgames.Game.GameMode;

public class GameManager {

    static GameManager instance = new GameManager();
    private ArrayList<Game>games = new ArrayList<Game>();
    private SurvivalGames p;
    public static HashMap<Integer, HashSet<Block>>openedChest = new HashMap<Integer, HashSet<Block>>();
    private ArrayList<Integer>gamemap = new ArrayList<Integer>();

    private GameManager(){

    }

    public static GameManager getInstance(){
        return instance;
    }

    public void setup(SurvivalGames plugin){
        p = plugin;
        LoadGames();
        for(Game g:getGames()){
            openedChest.put(g.getID(), new HashSet<Block>());
        }
    }
    
    public Plugin getPlugin(){
        return p;
    }

    public void reloadGames(){
        LoadGames();
    }
    

    public void LoadGames(){
        FileConfiguration c = SettingsManager.getInstance().getSystemConfig();
        games.clear();
        int no = c.getInt("sg-system.arenano", 0);
        int loaded = 0;
        int a = 1;
        while(loaded < no){
            if(c.isSet("sg-system.arenas."+a+".x1")){
                System.out.println("Loading Arena: "+a);
                loaded ++;
                games.add(new Game(a));
               // gamemap.add(loaded);
            }
            a++;
        }
        LobbyManager.getInstance().clearSigns();
    }


    public int getBlockGameId(Location v){
        for(Game g: games){
            if(g.isBlockInArena(v)){
                return g.getID();
            }
        }
        return -1;
    }

    public int getPlayerGameId(Player p){
        
        for(Game g:games){
            if(g.isPlayerActive(p)){
                return g.getID();
            }
        }
        return -1;
    }

    public boolean isPlayerActive(Player player) {
        for(Game g:games){
            if(g.isPlayerActive(player)){
                return true;
            }
        }
        return false;
    }
    public boolean isPlayerInactive(Player player) {
        for(Game g:games){
            if(g.isPlayerActive(player)){
                return true;
            }
        }
        return false;
    }

    public int getGameCount(){
        return games.size();
    }

    public Game getGame(int a){
        //int t = gamemap.get(a);
        for(Game g: games){
            if(g.getID() == a){
                return g;
            }
        }
        return null;
    }

    public void removePlayer(Player p){           
        getGame(getPlayerGameId(p)).removePlayer(p);
    }
    
    public void disableGame(int id){
        getGame(id).disable();
    }
    
    public void enableGame(int id){
        getGame(id).enable();
    }
    
    
    public ArrayList<Game> getGames(){
        return games;
    }
    public GameMode getGameMode(int a){
        for(Game g: games){
            if(g.getID() == a){
                return g.getMode();
            }
        }
        return null;
    }

    //TODO: Actually make this countdown correctly
    public void startGame(int a){
        getGame(a).countdown(10);
    }

    public void addPlayer(Player p, int g){
        Game game = getGame(g);
        if(game == null){
            p.sendMessage(ChatColor.RED+"Game does not exist");
            return;
        }
        getGame(g).addPlayer(p);
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

    public WorldEditPlugin getWorldEdit(){
        return p.getWorldEdit();
    }

    public void createArenaFromSelection(Player pl){
        FileConfiguration c = SettingsManager.getInstance().getSystemConfig();
        SettingsManager s = SettingsManager.getInstance();


        WorldEditPlugin we = p.getWorldEdit();
        Selection sel = we.getSelection(pl);
        if(sel == null){
            pl.sendMessage(ChatColor.RED+"You must make a WorldEdit Selection first");
            return;
        }
        Location max = sel.getMaximumPoint();
        Location min = sel.getMinimumPoint();

       /* if(max.getWorld()!=SettingsManager.getGameWorld() || min.getWorld()!=SettingsManager.getGameWorld()){
            pl.sendMessage(ChatColor.RED+"Wrong World!");
            return;
        }*/


        int no = c.getInt("sg-system.arenano") + 1;
        c.set("sg-system.arenano", no);
        if(games.size() == 0){
            no = 1;
        }
        else
            no = games.get(games.size()-1).getID() + 1;
        c.set("sg-system.arenas."+no+".world", max.getWorld().getName());
        c.set("sg-system.arenas."+no+".x1", max.getBlockX());
        c.set("sg-system.arenas."+no+".y1", max.getBlockY());
        c.set("sg-system.arenas."+no+".z1", max.getBlockZ());
        c.set("sg-system.arenas."+no+".x2", min.getBlockX());
        c.set("sg-system.arenas."+no+".y2", min.getBlockY());
        c.set("sg-system.arenas."+no+".z2", min.getBlockZ());

        SettingsManager.getInstance().saveSystemConfig();

        hotAddArena(no);

       
        pl.sendMessage(ChatColor.GREEN+"Arena ID "+no+" Succesfully added");

    }

    private void hotAddArena(int no) {
        games.add(new Game(no));
        System.out.println("game added "+ games.size()+" "+SettingsManager.getInstance().getSystemConfig().getInt("gs-system.arenano"));
    }
    
    public void gameEndCallBack(int id){
        openedChest.put(id, new HashSet<Block>());
    }


}

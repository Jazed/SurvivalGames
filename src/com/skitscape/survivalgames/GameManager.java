package com.skitscape.survivalgames;

import java.util.ArrayList;


import org.bukkit.ChatColor;
import org.bukkit.Location;
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
        FileConfiguration c = SettingsManager.getInstance().getSystemConfig();
        for(int a = 1; a<= c.getInt("sg-system.arenano",0); a++){
            System.out.println("Loading Arena: "+a);
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
        for(Game g: games){
            if(g.getID() == a){
                return g;
            }
        }
        return null;
    }
<<<<<<< HEAD

    public ArrayList<Game> getGames(){
        return games;
    }
=======
>>>>>>> 879e91097d00b53f529c2afc3532bd5f5d8e077a

    public GameMode getGameMode(int a){
        for(Game g: games){
            if(g.getID() == a){
                return g.getMode();
            }
        }
        return null;
    }

    public void startGame(int a){
        getGame(a).startGame();
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
<<<<<<< HEAD

    public WorldEditPlugin getWorldEdit(){
        return p.getWorldEdit();
    }
=======
>>>>>>> 879e91097d00b53f529c2afc3532bd5f5d8e077a

    public void createArenaFromSelection(Player pl){
        FileConfiguration c = SettingsManager.getInstance().getSystemConfig();
        SettingsManager s = SettingsManager.getInstance();
        if(c.getString("sg-system.world") == null){
            c.set("sg-system.world", pl.getWorld().getName());
            s.saveSystemConfig();
        } 

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
        c.set("sg-system.arenas."+no+".world", max.getWorld().getName());
        c.set("sg-system.arenas."+no+".x1", max.getBlockX());
        c.set("sg-system.arenas."+no+".y1", max.getBlockY());
        c.set("sg-system.arenas."+no+".z1", max.getBlockZ());
        c.set("sg-system.arenas."+no+".x2", min.getBlockX());
        c.set("sg-system.arenas."+no+".y2", min.getBlockY());
        c.set("sg-system.arenas."+no+".z2", min.getBlockZ());

        SettingsManager.getInstance().saveSystemConfig();

        hotAddArena(no);

        if(no == 1)
            pl.sendMessage(ChatColor.GREEN+"SurvivalGame World set to "+ c.getString("sg-system.world"));
        pl.sendMessage(ChatColor.GREEN+"Arena ID "+no+" Succesfully added");

    }

    private void hotAddArena(int no) {
        games.add(new Game(no));
        System.out.println("game added "+ games.size()+" "+SettingsManager.getInstance().getSystemConfig().getInt("gs-system.arenano"));
    }


}

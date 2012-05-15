package com.skitscape.survivalgames;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import com.skitscape.survivalgames.util.GameReset;


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
    private int gcount = 0;
    private FileConfiguration c;
    private FileConfiguration s;
    private HashMap<Integer, Player>spawns = new HashMap<Integer, Player>();
    private HashMap<Player, ItemStack[][]>inv_store = new HashMap<Player, ItemStack[][]>();
    private int spawnCount = 0;
    private int vote = 0;
    private boolean disabled = false;


    private long startTime = 0;

    public Game(int gameid){
        gameID = gameid;
        c = SettingsManager.getInstance().getConfig();

        s = SettingsManager.getInstance().getSystemConfig();
        setup();
    }


    public void setup(){
        mode = GameMode.LOADING;


        int x      = s.getInt("sg-system.arenas."+gameID+".x1");
        int y      = s.getInt("sg-system.arenas."+gameID+".y1");
        int z      = s.getInt("sg-system.arenas."+gameID+".z1");
        System.out.println(x+ " "+ y +" "+z);
        int x1     = s.getInt("sg-system.arenas."+gameID+".x2");
        int y1     = s.getInt("sg-system.arenas."+gameID+".y2");
        int z1     = s.getInt("sg-system.arenas."+gameID+".z2");
        System.out.println(x1+ " "+ y1 +" "+z1);
        Location max = new Location(SettingsManager.getGameWorld(gameID),Math.max(x, x1),Math.max(y, y1),Math.max(z, z1));
        Location min = new Location(SettingsManager.getGameWorld(gameID),Math.min(x, x1),Math.min(y, y1),Math.min(z, z1));


        arena = new Arena(min,max);

        loadspawns();

        mode = GameMode.WAITING;
    }

    public void loadspawns(){
        for(int a = 1; a<=SettingsManager.getInstance().getSpawnCount(gameID); a++){
            spawns.put(a, null);
            spawnCount = a;
        }
    }

    public void addSpawn(){
        spawnCount++;
        spawns.put(spawnCount, null);
    }


    public GameMode getGameMode(){
        return mode;
    }

    public Arena getArena(){
        return arena;
    }

    public void disable(){
        for(int a = 0;a<activePlayers.size(); a = 0){
            System.out.println(a + activePlayers.size());
            Player p = activePlayers.get(a);
            p.sendMessage(ChatColor.RED+"Game disabled");
            removePlayer(p);
        }
        for(int a = 0;a<inactivePlayers.size(); a = 0){
            Player p = inactivePlayers.get(a);
            p.sendMessage(ChatColor.RED+"Game disabled");
            removePlayer(p);
        }
        mode = GameMode.DISABLED;
        disabled = true;
        resetArena();
    }

    public void resetArena(){
        vote = 0;
        voted.clear();
        mode = GameMode.RESETING;
        GameManager.getInstance().gameEndCallBack(gameID);
        GameReset r = new GameReset(this);
        r.resetArena();
    }

    public void resetCallback(){
        if(!disabled)
            enable();
        else
            mode = GameMode.DISABLED;
    }


    public void enable(){
        mode = GameMode.WAITING;
        disabled = false;
    }

    public boolean addPlayer(Player p){

        // p.sendMessage(gameID+" "+ SettingsManager.getInstance().getSpawnCount(gameID)+" "+activePlayers.size());
        if(GameManager.getInstance().getPlayerGameId(p) != -1){
            if(GameManager.getInstance().isPlayerActive(p)){
                p.sendMessage(ChatColor.RED+"Cannot join multiple games!");
                return false;
            }
        }
        if(mode == GameMode.WAITING || mode == GameMode.STARTING){
            if(activePlayers.size() < SettingsManager.getInstance().getSpawnCount(gameID)){
                p.sendMessage("Joining Arena " + gameID);
                boolean placed = false;

                for(int a = 1; a<=SettingsManager.getInstance().getSpawnCount(gameID); a++){
                    System.out.println(spawns.get(a) == null);
                    if(spawns.get(a) == null){
                        p.teleport(SettingsManager.getInstance().getSpawnPoint(gameID, a));
                        placed = true;
                        p.setHealth(20);p.setFoodLevel(20);
                        spawns.put(a, p);
                        saveInv(p);
                        clearInv(p);
                        p.setGameMode(org.bukkit.GameMode.SURVIVAL);
                        activePlayers.add(p);

                        break;
                    }
                }
                if(!placed){
                    p.sendMessage(ChatColor.RED + "Game "+gameID+" Is Full!");
                    return false;
                }

            }
            else{
                p.sendMessage(ChatColor.RED + "No spawns set for Arena "+gameID+"!");
                return false;
            }
            for(Player pl:activePlayers){
                pl.sendMessage(ChatColor.GREEN+p.getName()+" joined the game!");
            }
            if(activePlayers.size() >= c.getInt("auto-start-players"))
                countdown(c.getInt("auto-start-time"));
            return true;
        }
        if(mode == GameMode.INGAME)
            p.sendMessage(ChatColor.RED+"Game already started!");
        else if(mode == GameMode.DISABLED)
            p.sendMessage(ChatColor.RED+"Arena disabled!");
        else if(mode == GameMode.RESETING)
            p.sendMessage(ChatColor.RED+"The arena is resting!");
        else
            p.sendMessage(ChatColor.RED+"Cannot join the game!");

        return false;
    }

    public int getActivePlayers(){
        return activePlayers.size();
    }

    public int getInactivePlayers(){
        return inactivePlayers.size();
    }

    public Player[][] getPlayers(){
        return new Player[][]{activePlayers.toArray(new Player[0]), inactivePlayers.toArray(new Player[0])};
    }

    public ArrayList<Player> getAllPlayers(){
        ArrayList<Player>all = new ArrayList<Player>();
        all.addAll(activePlayers);
        all.addAll(inactivePlayers);
        return all;
    }
    public void removePlayer(Player p){
        if(mode == GameMode.INGAME){
            killPlayer(p);
            p.teleport(SettingsManager.getInstance().getLobbySpawn());

        }
        else{
            restoreInv(p);
            activePlayers.remove(p);
            inactivePlayers.remove(p);
            p.teleport(SettingsManager.getInstance().getLobbySpawn());
            for(Object in: spawns.keySet().toArray()){
                if(spawns.get(in) == p)spawns.remove(in);
            }
            LobbyManager.getInstance().clearSigns();
        }
    }

    public int getID() {
        return gameID;
    }

    public void killPlayer(Player p){
        if(!activePlayers.contains(p))
            return;
        if(!p.isOnline())
            restoreInvOffline(p.getName());
        else
            restoreInv(p);

        activePlayers.remove(p);
        inactivePlayers.add(p);
        if(mode != GameMode.WAITING && getActivePlayers() >1){
            for(Player pl: getAllPlayers()){
                pl.sendMessage(ChatColor.YELLOW+"Player "+p.getName()+" died. There are now "+getActivePlayers()+" players remaining!");
            }
        }

        for(Player pe: activePlayers){
            Location l = pe.getLocation();
            l.setY(l.getWorld().getMaxHeight());
            l.getWorld().strikeLightningEffect(l);
        }



        if(activePlayers.size() == 1 && mode != GameMode.WAITING){
            playerWin(p);
            endGame();
        }
    }

    public void playerWin(Player p){
        Player win = activePlayers.get(0);
        restoreInv(win);

        Bukkit.getServer().broadcastMessage(ChatColor.DARK_AQUA+win.getName()+" won the Survival Games on arena "+gameID);
        win.teleport(SettingsManager.getInstance().getLobbySpawn());
        LobbyManager.getInstance().showMessage(new String[]{ChatColor.DARK_RED+win.getName(),"",ChatColor.DARK_RED+"Won on arena "+gameID+"!"});

        activePlayers.clear();
        inactivePlayers.clear();

        spawns.clear();
        loadspawns();
    }

    public boolean isBlockInArena(Location v) {
        return arena.containsBlock(v);
    }

    public boolean isProtectionOn(){
        long t = startTime / 1000;
        long l = SettingsManager.getInstance().getConfig().getLong("damage-time-limit");
        long d = new Date().getTime() /1000;

        if((d - t) < l)return true;
        return false;

    }

    public void endGame(){
        mode = GameMode.WAITING;
        resetArena();
        LobbyManager.getInstance().clearSigns();

    }

    ArrayList<Player>voted = new ArrayList<Player>();
    public void vote(Player pl){
        if(GameMode.STARTING == mode)return;
        if(voted.contains(pl)){
            pl.sendMessage(ChatColor.RED+"You already voted!");
            return;
        }
        vote++;
        voted.add(pl);
        for(Player p: activePlayers){
            p.sendMessage(ChatColor.AQUA+pl.getName()+" Voted to start the game!");
        }
        // Bukkit.getServer().broadcastMessage((vote +0.0) / (getActivePlayers() +0.0) +"% voted, needs "+(c.getInt("auto-start-vote")+0.0)/100);
        if(((vote +0.0) / (getActivePlayers() +0.0))>(c.getInt("auto-start-vote")+0.0)/100 && getActivePlayers()>2){
            countdown(c.getInt("auto-start-time"));
            for(Player p: activePlayers){
                p.sendMessage("Game Starting in "+c.getInt("auto-start-time"));
            }
        }
    }

    public void saveInv(Player p){
        ItemStack[] [] store = new ItemStack[2][1];

        store[0] = p.getInventory().getContents();
        store[1] = p.getInventory().getArmorContents();

        inv_store.put(p, store);

    }

    public void restoreInvOffline(String p){
        restoreInv(Bukkit.getOfflinePlayer(p).getPlayer());
    }


    @SuppressWarnings("deprecation")
    public void restoreInv(Player p){
        clearInv(p);
        p.getInventory().setContents(inv_store.get(p)[0]);
        p.getInventory().setArmorContents(inv_store.get(p)[1]);
        inv_store.remove(p);
        p.updateInventory();
    }

    @SuppressWarnings("deprecation")
    public void clearInv(Player p){
        ItemStack[] inv = p.getInventory().getContents();
        for (int i = 0; i < inv.length; i++) {
            inv[i]= null;
        }
        p.getInventory().setContents(inv);
        inv = p.getInventory().getArmorContents();
        for (int i = 0; i < inv.length; i++) {
            inv[i]= null;
        }
        p.getInventory().setArmorContents(inv);
        p.updateInventory();

    }

    public void startGame(){
        if(activePlayers.size() <= 1){
            for(Player pl: activePlayers){
                pl.sendMessage(ChatColor.RED+"Not Enought Players!");
            }
            return;
        }
        else{
            startTime = new Date().getTime();
            for(Player pl: activePlayers){
                pl.sendMessage(ChatColor.AQUA+"Good Luck!");
            }
            if(SettingsManager.getInstance().getConfig().getBoolean("restock-chest")){
                SettingsManager.getGameWorld(gameID).setTime(0);
                gcount++;
                new NightChecker().start();
            }

            mode = GameMode.INGAME;
        }

    }

    public int getCountdownTime(){
        return counttime;
    }
    int counttime = 0;
    int threadsync = 0;

    public void countdown(int time){
        threadsync++;
        mode = GameMode.STARTING;
        counttime = time;
        if(time<11){
            for(Player p: activePlayers){
                p.sendMessage(ChatColor.DARK_BLUE+"Game Starting in "+time);
            }
        }
        if(SurvivalGames.isActive() && time > 0){
            new CountdownThread(time).start();
        }
        else if(SurvivalGames.isActive() && time <= 0){
            startGame();
        }
        else
            return;
    }

    class CountdownThread extends Thread{

        int time;
        int trun = threadsync;
        public CountdownThread(int t){
            this.time = t;
        }
        public void run() {
            time--;
            try{Thread.sleep(1000);}catch(Exception e){}
            if(trun == threadsync)
                countdown(time);

        }

    }

    class NightChecker extends Thread{
        boolean reset = false;
        int tgc = gcount;
        public void run(){
            while(!reset && mode == GameMode.INGAME && tgc == gcount){
                try{Thread.sleep(5000);}catch(Exception e){}
                if(SettingsManager.getGameWorld(gameID).getTime()>14000){
                    for(Player pl:activePlayers){
                        pl.sendMessage(ChatColor.AQUA+"Chest have been restocked!");
                    }
                    GameManager.openedChest.get(gameID).clear();
                    reset = true;
                }

            }
        }


    }

    public boolean isPlayerActive(Player player) {
        return activePlayers.contains(player);
    }
    public boolean isPlayerinactive(Player player) {
        return inactivePlayers.contains(player);
    }
    public boolean hasPlayer(Player p){
        return activePlayers.contains(p) || inactivePlayers.contains(p);
    }
    public GameMode getMode(){
        return mode;
    }



    /*public void randomTrap() {



        World world = SettingsManager.getGameWorld(gameID);

        double xcord;
        double zcord;
        double ycord = 80;
        Random rand = new Random();
        xcord = rand.nextInt(1000);
        zcord = rand.nextInt(1000);
        Location trap = new Location(world, xcord, ycord, zcord);
        boolean isAir = true;

        while(isAir == true) {
            ycord--;
            Byte blockData = trap.getBlock().getData();
            if(blockData != 0) {
                trap.getBlock().setType(Material.AIR);
                ycord--;
                trap.getBlock().setType(Material.AIR);
                ycord--;
                trap.getBlock().setType(Material.AIR);
                ycord--;
                trap.getBlock().setType(Material.LAVA);
                isAir = false;
            } else {
                isAir = true;
            }
        }

    }*/
}


package com.skitscape.survivalgames;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
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
    private ArrayList<Player> spectators = new ArrayList<Player>();

    private Arena arena;
    private int gameID;
    private int arenano;
    private int gcount = 0;
    private FileConfiguration c;
    private FileConfiguration s;
    private HashMap<Integer, Player>spawns = new HashMap<Integer, Player>();
    private HashMap<Player, ItemStack[][]>inv_store = new HashMap<Player, ItemStack[][]>();
    private int spawnCount = 0;
    private int vote = 0;
    private boolean disabled = false;
    private int endgameTaskID = 0;
    private boolean endgameRunning = false;

    private double rbpercent = 0;
    
    private long startTime = 0;
    private boolean countdownRunning;

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
        System.out.println(max.toString());
        Location min = new Location(SettingsManager.getGameWorld(gameID),Math.min(x, x1),Math.min(y, y1),Math.min(z, z1));
        System.out.println(min.toString());


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

    public void addSpectator(Player p){
        p.setAllowFlight(true);
        p.setFlying(true);
        saveInv(p);
        clearInv(p);
        for(Player pl : activePlayers.toArray(new Player[0])){
            pl.hidePlayer(p);
        }
        p.teleport(SettingsManager.getInstance().getSpawnPoint(gameID, 1));
        spectators.add(p);
    }

    public void removeSpectator(Player p){
        ArrayList<Player>players = new ArrayList<Player>();
        players.addAll(activePlayers);
        players.addAll(inactivePlayers);

        for(Player pl:players){
            pl.showPlayer(p);
        }

        restoreInv(p);

        p.teleport(SettingsManager.getInstance().getLobbySpawn());
        spectators.remove(p);
    }

    public boolean isSpectator(Player p){
        return spectators.contains(p);
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
        endgameRunning = false;
        Bukkit.getScheduler().cancelTask(endgameTaskID);
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
            else if(SettingsManager.getInstance().getSpawnCount(gameID) == 0){
                p.sendMessage(ChatColor.RED + "No spawns set for Arena "+gameID+"!");
                return false;
            }
            else{
                p.sendMessage(ChatColor.RED + "Game "+gameID+" Is Full!");
                return false;
            }
            for(Player pl:activePlayers){
                pl.sendMessage(ChatColor.GREEN+p.getName()+" joined the game! "+getActivePlayers() + "/"+SettingsManager.getInstance().getSpawnCount(gameID));
            }
            if(activePlayers.size() >= c.getInt("auto-start-players") && !countdownRunning)
                countdown(c.getInt("auto-start-time"));
            return true;
        }
        if(mode == GameMode.INGAME)
            p.sendMessage(ChatColor.RED+"Game already started!");
        else if(mode == GameMode.DISABLED)
            p.sendMessage(ChatColor.RED+"Arena disabled!");
        else if(mode == GameMode.RESETING)
            p.sendMessage(ChatColor.RED+"The arena is reseting!");
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
            killPlayer(p, true);
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


    public void playerLeave(Player p){


    }

    public void killPlayer(Player p, boolean left){
        if(!activePlayers.contains(p))
            return;
        if(!p.isOnline())
            restoreInvOffline(p.getName());
        else
            restoreInv(p);

        activePlayers.remove(p);
        inactivePlayers.add(p);
        if(left){
            for(Player pl: getAllPlayers()){
                pl.sendMessage(ChatColor.YELLOW+p.getName()+" left the arena");
            }
        }
        else{
            if(mode != GameMode.WAITING && getActivePlayers() >1){
                String damagemsg = "";
                switch(p.getLastDamageCause().getCause()){
                case BLOCK_EXPLOSION: damagemsg = "{player} Exploded!";
                break;
                case DROWNING: damagemsg = "{player} Drowned!";
                break;
                case ENTITY_ATTACK: damagemsg = EntDmgMsg(p, p.getLastDamageCause());
                break;
                case FALL: damagemsg = "{player} hit the ground too hard!";
                break;
                case LAVA: damagemsg = "{player} burned in lava!";
                break;
                case FIRE: damagemsg = "{player} burned to death!";
                break;
                case FIRE_TICK: damagemsg = "{player} burned to death!";
                break;
                case STARVATION: damagemsg = "{player} starved to death!";
                break;
                case ENTITY_EXPLOSION: damagemsg = "{player} was creeper bombed!";
                break;
                default: damagemsg = "{player} died!";
                break;
                }
                damagemsg = damagemsg.replace("{player}", (SurvivalGames.auth.contains(p.getName())?ChatColor.DARK_RED +""+ChatColor.BOLD:"") + p.getName() +ChatColor.RESET+""+ ChatColor.YELLOW);

                for(Player pl: getAllPlayers()){
                    pl.sendMessage(ChatColor.YELLOW+damagemsg +"."+getActivePlayers()+" players remaining!");
                }
            }
        }

        for(Player pe: activePlayers){
            Location l = pe.getLocation();
            l.setY(l.getWorld().getMaxHeight());
            l.getWorld().strikeLightningEffect(l);
        }

        if(getActivePlayers() <= c.getInt("endgame.players") && c.getBoolean("endgame.fire-lighting.enabled")){
            endgameRunning = true;

            new EndgameManager().start();
        }



        if(activePlayers.size() == 1 && mode != GameMode.WAITING){
            playerWin(p);
            endGame();
        }
    }

    public String EntDmgMsg(Player p, EntityDamageEvent e){
        if(e.getEntityType() == EntityType.PLAYER){
            try{
                Player e1 = p.getKiller();
                Material m = e1.getItemInHand().getType();
                return (SurvivalGames.auth.contains(e1.getName())?ChatColor.DARK_RED +""+ChatColor.BOLD:"")+e1.getName()+ChatColor.RESET+""+ChatColor.YELLOW +" Killed {player} with "+ m;
            }
            catch(Exception e7){
                return "{player} was killed. ";
            }

        }
        else{
            String msg = "";
            switch(e.getEntityType()){
            case CREEPER:
                msg = "{player} was Creeper bombed!";
                break;
            case GHAST:
                msg = "{player} was fireballed by a ghast!";
                break;
            case ARROW:
                Player p5 = (Player)e;
                msg = p5.getName() + " shot {player}!";
                break;
            case LIGHTNING:
                msg = "{player} was electrocuted!";
                break;
            case CAVE_SPIDER:
                msg = "{player} was killed by a Cave Spider!";
                break;
            default:
                msg = "{player} was killed by a " + e.getEntityType().toString().toLowerCase()+"!";
                break;
            }
            return msg;
        }

    }






    public void playerWin(Player p){
        Player win = activePlayers.get(0);
        restoreInv(win);

        Bukkit.getServer().broadcastMessage(ChatColor.DARK_AQUA+win.getName()+" won the Survival Games on arena "+gameID);
        win.teleport(SettingsManager.getInstance().getLobbySpawn());
        LobbyManager.getInstance().showMessage(new String[]{win.getName(),"","Won the ","Survival Games!"});

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
        long l = SettingsManager.getInstance().getConfig().getLong("grace-period");
        long d = new Date().getTime() /1000;

        if((d - t) < l)return true;
        return false;

    }

    public void endGame(){
        mode = GameMode.WAITING;
        resetArena();
        LobbyManager.getInstance().clearSigns();
        endgameRunning = false;
    }

    ArrayList<Player>voted = new ArrayList<Player>();
    public void vote(Player pl){
        
        if(GameMode.STARTING == mode){pl.sendMessage(ChatColor.GREEN+"Game already starting!");return;}
        if(GameMode.WAITING != mode){pl.sendMessage(ChatColor.GREEN+"Game already started!");return;}
        if(voted.contains(pl)){
            pl.sendMessage(ChatColor.RED+"You already voted!");
            return;
        }
        vote++;
        voted.add(pl);
        pl.sendMessage(ChatColor.GREEN+"Voted to start the game!");
        for(Player p: activePlayers){
            p.sendMessage(ChatColor.AQUA+pl.getName()+" Voted to start the game!");
        }
        // Bukkit.getServer().broadcastMessage((vote +0.0) / (getActivePlayers() +0.0) +"% voted, needs "+(c.getInt("auto-start-vote")+0.0)/100);
        if(((vote +0.0) / (getActivePlayers() +0.0))>(c.getInt("auto-start-vote")+0.0)/100 && getActivePlayers()>2){
            countdown(c.getInt("auto-start-time"));
            for(Player p: activePlayers){
                p.sendMessage(ChatColor.LIGHT_PURPLE+"Game Starting in "+c.getInt("auto-start-time"));
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
            if(SettingsManager.getInstance().getConfig().getInt("grace-period") !=0){
                for(Player play: activePlayers){
                    play.sendMessage(ChatColor.LIGHT_PURPLE+"You have a "+SettingsManager.getInstance().getConfig().getInt("grace-period")+" second grace period!");
                }
                Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(GameManager.getInstance().getPlugin(), new Runnable(){
                    public void run(){
                        for(Player play: activePlayers){
                            play.sendMessage(ChatColor.LIGHT_PURPLE+"Grace period has ended!");
                        }
                    }}, SettingsManager.getInstance().getConfig().getInt("grace-period") * 20);
            }

        }

        mode = GameMode.INGAME;
    }



    public int getCountdownTime(){
        return counttime;
    }
    int counttime = 0;
    int threadsync = 0;

    public void countdown(int time){
        threadsync++;
        mode = GameMode.STARTING;
        countdownRunning = true;
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
            countdownRunning = false;
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

    class EndgameManager extends Thread{

        @Override
        public void run() {
            while(endgameRunning){
                for(Player player:activePlayers.toArray(new Player[0])){
                    Location l = player.getLocation();
                    l.add(0, 5, 0);
                    player.getWorld().strikeLightningEffect(l);
                }
                try{Thread.sleep(SettingsManager.getInstance().getConfig().getInt("endgame.fire-lighting.interval") * 1000);}catch(Exception e){};

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

    public synchronized void setRBPercent(double d){
        rbpercent = d;
    }


    public double getRBPercent() {
       return rbpercent;
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



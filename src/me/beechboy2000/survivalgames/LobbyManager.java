package me.beechboy2000.survivalgames;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.block.Sign;

import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.bukkit.selections.Selection;

public class LobbyManager  implements Listener{


    //will manage lobby game status signs
    Sign[][] signs;
    SurvivalGames p;
    private int runningThread = 0;
    private static LobbyManager instance = new LobbyManager();

    private LobbyManager(){

    }

    public static LobbyManager getInstance(){
        return instance;

    }


    public void setup(SurvivalGames p){
        this.p = p;
        loadSigns();
    }

    public void loadSigns(){
        System.out.println("setting sign");

        FileConfiguration c = SettingsManager.getInstance().getSystemConfig();
        if(!c.getBoolean("sg-system.lobby.sign.set"))
            return;
        System.out.println("setting sign");

        boolean usingx = false;
        int hdiff = 0;
        int x1 = c.getInt("sg-system.lobby.sign.x1");
        int y1 = c.getInt("sg-system.lobby.sign.y1");
        int z1 = c.getInt("sg-system.lobby.sign.z1");
        int x2 = c.getInt("sg-system.lobby.sign.x2");
        int y2 = c.getInt("sg-system.lobby.sign.y2");
        int z2 = c.getInt("sg-system.lobby.sign.z2");
        Location l = new Location(SettingsManager.getGameWorld(),x1,y1,z1);


        usingx = ((x2 - x1) != 0)? true : false;
        if(usingx){
            hdiff = (x1 - x2)+1;
            System.out.println("usingx");
        }
        else{
            hdiff = (z1 - z2) +1;
        }
        int vdiff = (y1 - y2 ) + 1;
        
        
        System.out.println(vdiff+"              "+hdiff);
        signs = new Sign[vdiff][hdiff];
        for(int y = vdiff-1; y>=0; y--){
            for(int x = hdiff-1; x>=0;x--){

                System.out.println(l);
                signs[y][x] = (Sign) SettingsManager.getGameWorld().getBlockAt(l).getState();
                System.out.println("setting sign");
                if(usingx)
                    l = l.add(-1, 0, 0);
                else
                    l = l.add(0, 0, -1);
                //l.getBlock().setTypeId(323);
            }
            l = l.add(0, -1, 0);
            l.setX(x1);
            l.setZ(z1);
        }
        runningThread ++;
        new LobbySignUpdater().start();
    }

    public int[] getSignMidPoint(){
        double x = signs[0].length;
        double y = signs.length;

        return new int[]{(int)x,(int)y};
    }



    public void setLobbySignsFromSelection(Player pl){
        FileConfiguration c = SettingsManager.getInstance().getSystemConfig();
        SettingsManager s = SettingsManager.getInstance();
        if(!c.getBoolean("sg-system.lobby.sign.set", false)){
            c.set("sg-system.lobby.sign.set", true);
            s.saveSystemConfig();
        }

        WorldEditPlugin we = p.getWorldEdit();
        Selection sel = we.getSelection(pl);
        if(sel == null){
            pl.sendMessage(ChatColor.RED+"You must make a WorldEdit Selection first");
            return;
        }
        if( (sel.getNativeMaximumPoint().getBlockX()  - sel.getNativeMaximumPoint().getBlockX()) != 0 && (sel.getNativeMaximumPoint().getBlockY()  - sel.getNativeMaximumPoint().getBlockY() !=0)){
            pl.sendMessage(ChatColor.RED +" Must be in a straight line!");
            return;
        }

        Vector max = sel.getNativeMaximumPoint();
        Vector min = sel.getNativeMinimumPoint();

        c.set("sg-system.lobby.sign.world", pl.getWorld().getName());
        c.set("sg-system.lobby.sign.x1", max.getBlockX());
        c.set("sg-system.lobby.sign.y1", max.getBlockY());
        c.set("sg-system.lobby.sign.z1", max.getBlockZ());
        c.set("sg-system.lobby.sign.x2", min.getBlockX());
        c.set("sg-system.lobby.sign.y2", min.getBlockY());
        c.set("sg-system.lobby.sign.z2", min.getBlockZ());

        pl.sendMessage(ChatColor.GREEN+"Lobby Status wall successfuly created");

        loadSigns();


    }

    public void signStartUp(){
        for(int y = signs.length-1; y!=-1; y--){
                for(int a = 0; a<4; a++){
                    for(int x = 0; x!=signs[0].length; x++){

                    System.out.println(signs[y][x]);
                    Sign sig = signs[y][x];
                    sig.setLine(a, "----------------------------------------------------------");
                    sig.update();
                    
                }
                try{Thread.sleep(1000);}catch(Exception e){}

            }

        }
    }

    class LobbySignUpdater extends Thread{
        public void run(){
            int trun = runningThread;
            signStartUp();
            while(SurvivalGames.isActive() && trun == runningThread){
                try{Thread.sleep(1000);}catch(Exception e){}

            }
        }
    }





}

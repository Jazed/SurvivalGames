package com.skitscape.survivalgames;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;

import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.bukkit.selections.Selection;

public class LobbyManager  implements Listener{


    //TODO: Possibly clean up
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
        int inc = 0;
        Location l;
        System.out.println(x1+"  "+y1+"  "+z1);
        byte temp = ((Sign)new Location(p.getServer().getWorld(c.getString("sg-system.lobby.sign.world")),x1,y1,z1).getBlock().getState()).getData().getData();
        System.out.println("facing "+temp);
        if(temp == 3 || temp == 4){
            l = new Location(SettingsManager.getGameWorld(),x1,y1,z1);
            inc = -1;
        }else{
            l = new Location(SettingsManager.getGameWorld(),x2,y1,z2);
            inc = 1;
        }


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
                
                BlockState b =   p.getServer().getWorld(SettingsManager.getInstance().getSystemConfig().getString("sg-system.lobby.sign.world")).getBlockAt(l).getState();
                if(b instanceof Sign){
                    signs[y][x] = (Sign)b;
                }
                System.out.println("setting sign");
                if(usingx)
                    l = l.add(inc, 0, 0);
                else
                    l = l.add(0, 0, inc);
                //l.getBlock().setTypeId(323);
            }
            l = l.add(0, -1, 0);
            if(inc == -1){
            l.setX(x1);
            l.setZ(z1);
            }
            else{
                l.setX(x2);
                l.setZ(z2);
            }
        }
        runningThread ++;
        new LobbySignUpdater().start();
    }

    public int[] getSignMidPoint(){
        double x = (signs[0].length * 8);
        double y = (signs.length * 2);

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
        s.saveSystemConfig();
        loadSigns();


    }

    boolean showingMessage = false;
    ArrayList<String[]>messagequeue = new ArrayList<String[]>(3);

    public void signShowMessage(String[] msg9){
        messagequeue.add(msg9);
        if(showingMessage)
            return;
        showingMessage = true;
        for(int y = signs.length-1; y!=-1; y--){
            for(int a = 0; a<4; a++){

                for(int x = 0; x!=signs[0].length; x++){

                    System.out.println(signs[y][x]);
                    Sign sig = signs[y][x];
                    sig.setLine(a, "==================================================");
                    sig.update();

                }
                try{Thread.sleep(50);}catch(Exception e){}

            }
        }


        for(int y = signs.length-1; y!=-1; y--){
            for(int a = 0; a<4; a++){
                for(int x = 0; x!=signs[0].length; x++){

                    Sign sig = signs[y][x];
                    if((y == signs.length - 1 && a ==0) || y == 0 && a == 3){
                        sig.setLine(a,"===========================================");
                    }
                    else{
                        sig.setLine(a, "");
                        signs[y][0].setLine(a, "|                         ");
                        signs[y][0].update();
                        signs[y][signs[0].length-1].setLine(a, "              |");
                        signs[y][signs[0].length-1].update();
                    }
                    sig.update();

                }
                try{Thread.sleep(50);}catch(Exception e){}

            }
        }

        for(String[] msg:messagequeue){
            int x = getSignMidPoint()[1] - (msg.length / 2);
            int lineno = x%3;
            x = x / 4;
            System.out.println(x);
            for(int a = msg.length-1; a>-1;a--){
                int y = getSignMidPoint()[0] - (msg[a].length() / 2);

                System.out.println(msg[a]);
                char[] line =  msg[a].toCharArray();
                for(int b = 0;b<line.length;b++){

                    System.out.println(y/16+"    "+x/4+"     "+(3-x)%4+"     "+x);
                    Sign sig = signs [x][((y)/16)];
                    sig.setLine(lineno,sig.getLine(lineno)+line[b]);
                    System.out.println(sig.getLine(x%4));
                    signs [x][((y)/16)].update();

                    y++;
                }
                if(lineno == 0){
                    lineno = 3;
                    x++;
                }
                else 
                    lineno--;


            }
        }
        showingMessage = false;
    }


    class LobbySignUpdater extends Thread{
        public void run(){
            int trun = runningThread;
            signShowMessage(new String[]{"", "Survival Games","","","Double0negative                    Beechboy200" ,"skitscape.com"});
            try{Thread.sleep(4000);}catch(Exception e){}
            clearSigns();
            while(SurvivalGames.isActive() && trun == runningThread){
                try{Thread.sleep(1000);}catch(Exception e){}
                updateGameStatus();
            }
        }
    }


    public void updateGameStatus(){
        int b = signs.length-1;
        if(GameManager.getInstance().getGameCount() == 0)
            return;
        for(int a = 1; a<=GameManager.getInstance().getGameCount();a++){
            signs[b][0].setLine(0, "[SurvivalGames]");
            signs[b][0].setLine(1, "Click to join");
            signs[b][1].setLine(0, "Arena "+a);
            signs[b][1].setLine(1, GameManager.getInstance().getGameMode(a)+"");
            signs[b][1].setLine(2, GameManager.getInstance().getGame(a).getActivePlayers()+"/"+SettingsManager.getInstance().getSpawnCount(a));

            signs[b][0].update();
            signs[b][1].update();
            
            int signno = 2;
            int line = 0;
            Player[] active = GameManager.getInstance().getGame(a).getPlayers()[0];
            Player[] inactive = GameManager.getInstance().getGame(a).getPlayers()[1];
            for(Player p:active){
                signs[b][signno].setLine(line, p.getName());
                signs[b][signno].update();

                line++;
                if(line == 4){
                    line = 0;
                    signno++;
                }
            }
            for(Player p:inactive){
                signs[b][signno].setLine(line, ChatColor.GRAY +p.getName());
                signs[b][signno].update();
                line++;
                if(line == 4){
                    line = 0;
                    signno++;
                    
                }
            }

            b--;
        }

    }



    public void clearSigns(){
        for(int y = signs.length-1; y!=-1; y--){
            for(int a = 0; a<4; a++){

                for(int x = 0; x!=signs[0].length; x++){

                    System.out.println(signs[y][x]);
                    Sign sig = signs[y][x];
                    sig.setLine(a, "");
                    sig.update();

                }

            }
        }
    }

}

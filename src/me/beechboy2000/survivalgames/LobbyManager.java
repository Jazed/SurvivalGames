package me.beechboy2000.survivalgames;

import org.bukkit.Location;
import org.bukkit.block.Sign;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

public class LobbyManager extends Thread implements Listener{


    //will manage lobby game status signs
    Sign[][] signs;
    Plugin p;



    public void loadSigns(){
        FileConfiguration c = SettingsManager.getInstance().getConfig();
        if(!c.getBoolean("system.lobby.sign.set"))
                return;
        boolean usingx = false;
        int hdiff = 0;
        int hstart = 0;
        int vstart = 0;
        int x1 = c.getInt("system.lobby.sign.x1");
        int y1 = c.getInt("system.lobby.sign.y1");
        int z1 = c.getInt("system.lobby.sign.z1");
        int x2 = c.getInt("system.lobby.sign.x2");
        int y2 = c.getInt("system.lobby.sign.y2");
        int z2 = c.getInt("system.lobby.sign.z2");
        Location l = new Location(SurvivalGames.getGameWorld(),x1,y1,z1);


        usingx = ((x2 - x1) == 0)? true : false;
        if(usingx){
            hdiff = x2 - x1;
            hstart = x1;
        }
        else{
            hdiff = z2 - z1;
            hstart = z1;
        }
        int vdiff = y2 - y1;
        vstart = y1;

        signs = new Sign[hdiff][vdiff];
        for(int y = 0; y<vdiff; y++){
            for(int x = 0; x<hdiff;x++){
                if(usingx)
                    l = l.add(hdiff/hdiff, 0, 0);
                else
                    l = l.add(0, 0, hdiff/hdiff);
                signs[x][y] = (Sign) SurvivalGames.getGameWorld().getBlockAt(l);
            }
            l = l.add(0, vdiff/vdiff, 0);
        }
    }

    public int[] getSignMidPoint(){
        double x = signs[0].length;
        double y = signs.length;
       
        return new int[]{(int)x,(int)y};
    }


    public void run(){
        signStartUp();
        while(SurvivalGames.isActive()){
            
        }
    }
    
    
    
    public void signStartUp(){
        for(int y = 0; y<signs.length; y++){
            for(int x = 0; x<signs[0].length; x++){
                for(int a = 0; a<4; a++){
                    signs[y][x].setLine(a, "---------------------------------");
                }
            }
        }
    }







}

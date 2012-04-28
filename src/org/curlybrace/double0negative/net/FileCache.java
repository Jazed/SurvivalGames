package org.curlybrace.double0negative.net;

import java.io.File;
import java.util.HashMap;
import java.util.Scanner;

import me.beechboy2000.survivalgames.SurvivalGames;

public class FileCache {

    private static FileCache instance = new FileCache();
    private static HashMap<String, String>html = new HashMap<String, String>();
    private FileCache(){

    }

    public static FileCache getInstance(){
        return instance;
    }

    public static String getHTML(String pagename, boolean template){
        if(html.get(pagename)== null){
            loadPage(pagename, template);
        }

        return html.get(pagename);

    }

    public static void loadPage(String pagename, boolean template){
        Scanner scan = null;
        try{
            scan = new Scanner(new File(SurvivalGames.getPluginDataFolder()+((template)?"\\template.html": "\\pages\\"+pagename)));
        }catch(Exception e){System.out.println("Survival Games webstats - Could not load page: " + pagename);
            String data = "";
        
        if(scan == null)
            return;
        while(scan.hasNext()){
            data = data + scan.nextLine();
        }
        html.put(pagename, data);
        }
    }
}

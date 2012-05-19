package com.skitscape.survivalgames.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import com.skitscape.survivalgames.SurvivalGames;

public class ChestRatioStorage {



    HashMap<Integer, ArrayList<ItemStore>>lvlstore = new HashMap<Integer, ArrayList<ItemStore>>();
    public static ChestRatioStorage instance = new ChestRatioStorage();
    int ratio = 2;

    private ChestRatioStorage(){

    }

    public void setup(){


        File f = new File(SurvivalGames.getPluginDataFolder()+"/chest.yml");
        if(!f.exists()){
            try {f.createNewFile();
            FileWriter out = new FileWriter(f);
            InputStream is = getClass().getResourceAsStream("chest.yml");
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line;
            while ((line = br.readLine()) != null) {
                out.write(line+"\n");
              //  System.out.println(line+"\n");
            }
            
            
            is.close();
            isr.close();
            br.close();
            out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        FileConfiguration conf = YamlConfiguration.loadConfiguration(f);

        for(int a = 1; a<5;a++){
            ArrayList<ItemStore> lvl = new ArrayList<ItemStore>();
            List<String>list = conf.getStringList("chest.lvl"+a);

            for(int b = 0; b<list.size();b++){
                String [] arg = list.get(b).split(",");
                
                ItemStore i = new ItemStore(Integer.parseInt(arg[0]), Integer.parseInt(arg[1]));
              //  System.out.println(i.toString()+"\n");
                
                lvl.add(i);

            }

            lvlstore.put(a, lvl);

        }

        ratio = conf.getInt("chest.ratio") + 1;
        
    }

    public static ChestRatioStorage getInstance(){
        return instance;
    }

    class ItemStore{

        public int getId() {
            return id;
        }

        public int getMax() {
            return max;
        }

        public boolean isEnchant() {
            return enchant;
        }

        int id, max;
        boolean enchant = false;

        public ItemStore(int id, int max, boolean enchant){
            setup(id, max, enchant);
        }

        public ItemStore(int id, int max){
            setup(id,max, false);
        }

        public ItemStore(int id){
            setup(id, 1, false);
        }

        public ItemStore(int id, boolean enchant){
            setup(id, 1, enchant);
        }

        public void setup(int id, int max, boolean enchant){
            this.id = id;
            this.max = max;
            this.enchant = enchant;
        }

        @Override
        public String toString(){
            return "ID: "+id+" AMOUNT: "+max;
        }

    }

    public ArrayList<ItemStack> getItems(){
        Random r = new Random();
        ArrayList<ItemStack>items = new ArrayList<ItemStack>();
        for(int a = 0; a< r.nextInt(7)+5; a++){
            if(r.nextBoolean() == true){
                int i = 1;
                while(i<5 &&  r.nextInt(ratio) == 1){
                    i++;
                }

                ArrayList<ItemStore>lvl = lvlstore.get(i);
                ItemStore item = lvl.get(r.nextInt(lvl.size()));
                i = 0;
                ItemStack stack = new ItemStack(item.getId(), r.nextInt(item.getMax())+1);
                if(r.nextBoolean() == true && item.isEnchant()){
                    //TODO: Enchantments
                }

                items.add(stack);
            }

        }
        
        //Bukkit.broadcastMessage(items+"");
        return items;



    }




}

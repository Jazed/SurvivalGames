package com.skitscape.survivalgames.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import org.bukkit.inventory.ItemStack;

public class ChestRatioStorage {

    //Seperate class so it can be easily added to a config file later


    HashMap<Integer, ArrayList<ItemStore>>lvlstore = new HashMap<Integer, ArrayList<ItemStore>>();

    ArrayList<ItemStore> lvl1 = new ArrayList<ItemStore>();
    ArrayList<ItemStore> lvl2 = new ArrayList<ItemStore>();
    ArrayList<ItemStore> lvl3 = new ArrayList<ItemStore>();
    ArrayList<ItemStore> lvl4 = new ArrayList<ItemStore>();
    ArrayList<ItemStore> lvl5 = new ArrayList<ItemStore>();


    public ChestRatioStorage(){

        lvl1.add(new ItemStore(268));
        lvl1.add(new ItemStore(298));
        lvl1.add(new ItemStore(299));
        lvl1.add(new ItemStore(300));
        lvl1.add(new ItemStore(301));
        lvl1.add(new ItemStore(319, 3));
        lvl1.add(new ItemStore(349, 3));
        lvl1.add(new ItemStore(352, 3));
        lvl1.add(new ItemStore(360, 4));
        lvl1.add(new ItemStore(367, 4));
        lvl2.add(new ItemStore(50, 5));

        lvl2.add(new ItemStore(261));
        lvl2.add(new ItemStore(268));
        lvl2.add(new ItemStore(314));
        lvl2.add(new ItemStore(315));
        lvl2.add(new ItemStore(316));
        lvl2.add(new ItemStore(317));
        lvl2.add(new ItemStore(320, 3));
        lvl2.add(new ItemStore(281, 4));
        lvl2.add(new ItemStore(260, 2));
        lvl2.add(new ItemStore(363, 4));
        lvl2.add(new ItemStore(364, 2));
        lvl2.add(new ItemStore(365, 4));
        lvl2.add(new ItemStore(366, 2));
        lvl2.add(new ItemStore(368, 3));
        lvl2.add(new ItemStore(282, 2));

        lvl3.add(new ItemStore(283));
        lvl3.add(new ItemStore(103, 4));
        lvl3.add(new ItemStore(306));
        lvl3.add(new ItemStore(307));
        lvl3.add(new ItemStore(308));
        lvl3.add(new ItemStore(309));
        lvl3.add(new ItemStore(302));
        lvl3.add(new ItemStore(303));
        lvl3.add(new ItemStore(305));
        lvl3.add(new ItemStore(306));
        lvl3.add(new ItemStore(322,2));
        lvl3.add(new ItemStore(259));
        lvl3.add(new ItemStore(327));
        
        lvl5.add(new ItemStore(276));
        lvl5.add(new ItemStore(310));
        lvl5.add(new ItemStore(311));
        lvl5.add(new ItemStore(312));
        lvl5.add(new ItemStore(313));
        lvl5.add(new ItemStore(354, 2));




        lvlstore.put(1, lvl1);
        lvlstore.put(2, lvl2);
        lvlstore.put(3, lvl3);
        lvlstore.put(4, lvl4);
        lvlstore.put(5, lvl5);














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



    }

    public ArrayList<ItemStack> getItems(){
        Random r = new Random();
        ArrayList<ItemStack>items = new ArrayList<ItemStack>();
        for(int a = 0; a< r.nextInt(7)+5; a++){
            if(r.nextBoolean() == true){
                int i = 1;
                while(i<5 && r.nextBoolean() == true){
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
        return items;



    }



}

package com.skitscape.survivalgames.commands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.bukkit.selections.Selection;
import com.skitscape.survivalgames.GameManager;
import com.skitscape.survivalgames.util.ArenaDuplicator;

public class DupeArena implements SubCommand{

    @Override
    public boolean onCommand(Player player, String[] args) {
        WorldEditPlugin we = GameManager.getInstance().getWorldEdit();
        Selection sel = we.getSelection(player);
        if(sel == null){
            player.sendMessage(ChatColor.RED+"You must make a WorldEdit Selection first");
            return true;
        }

        ArenaDuplicator a = new ArenaDuplicator();
        Vector v1 =sel.getMinimumPoint().toVector();
        Vector v2 = sel.getMaximumPoint().toVector();

        a.startDupe(v1,v2);
        return true;

    }


}

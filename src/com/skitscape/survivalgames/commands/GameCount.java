package com.skitscape.survivalgames.commands;


import org.bukkit.entity.Player;

import com.skitscape.survivalgames.GameManager;

public class GameCount implements SubCommand{
    public boolean onCommand(Player player, String[] args) {
        player.sendMessage(GameManager.getInstance().getGameCount()+"");
    
    return false;
}
    
    @Override
    public String help(Player p) {
        return "/sg setlobbywall - Setings the lobby stats wall";
    }

}

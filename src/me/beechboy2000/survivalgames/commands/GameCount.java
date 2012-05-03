package me.beechboy2000.survivalgames.commands;

import me.beechboy2000.survivalgames.GameManager;

import org.bukkit.entity.Player;

public class GameCount implements SubCommand{
    public boolean onCommand(Player player, String[] args) {
        player.sendMessage(GameManager.getInstance().getGameCount()+"");
    
    return false;
}

}

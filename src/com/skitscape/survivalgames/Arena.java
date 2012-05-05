package com.skitscape.survivalgames;

import org.bukkit.util.Vector;

public class Arena {

    Vector min;
    Vector max;

    public Arena(Vector min, Vector max){
        this.max = max;
        this.min = min;

    }

    public boolean containsBlock(Vector v){
        final double x = v.getX();
        final double y = v.getY();
        final double z = v.getZ();
        return x >= min.getBlockX() && x < max.getBlockX()+1
                && y >= min.getBlockY() && y < max.getBlockY()+1
                && z >= min.getBlockZ() && z < max.getBlockZ()+1;
    }

    public Vector getMin() {
        return min;
    }

    public Vector getMax() {
        return max;
    }
    
    
    
    
    
    
}



package org.cbp.double0negative.logging;

public class BlockData {

    private String world;
    private int previd;
    private int newid;
    private byte prevdata,newdata;
    private int x,y,z;
    
    
    /**
     * 
     * @param previd
     * @param newid
     * @param x
     * @param y
     * @param z
     * 
     * Provides a object for holding the data for block changes
     */
    public BlockData(String world, int previd,byte prevdata, int newid,byte newdata, int x, int y, int z){
        this.world = world;
        this.previd = previd;
        this.prevdata = prevdata;
        this.newid = newid;
        this.newdata = newdata;
        this.x = x;
        this.y = y;
        this.z = z;
        
    }
    
    public String getWorld(){
        return world;
    }

    public byte getPrevdata() {
        return prevdata;
    }

    public byte getNewdata() {
        return newdata;
    }

    public int getPrevid() {
        return previd;
    }

    public int getNewid() {
        return newid;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }
    
}

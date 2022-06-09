package com.client;

public final class StaticObject {

    public StaticObject() {
    }

    int anInt517;
    int tileHeight;
    int xPos;
    int yPos;
    public Renderable renderable;
    public int turnValue;
    int anInt523;
    int xLocHigh;
    int anInt525;
    int yLocLow;
    int anInt527;
    int anInt528;
    public int uid;
    byte mask;
int newuid;
	
	/**
	 * Mutator method for the newuid variable
	 * @param newUIDReplacement the value assigned towards the newuid variable 
	 * @return newuid new universal identification
	 */
	
	public int setNewUID(int newUIDReplacement)
	{
		return newuid = newUIDReplacement;
	}
	
	/**
	 * Gets the new uid
	 * @return newuid the new universal identifier
	 */
	public int getNewUID()
	{
		return newuid;
	}
}

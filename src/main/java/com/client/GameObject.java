package com.client;

public final class GameObject {

    public GameObject() {
    }

    int zLoc;
    int tileHeight;
    int xPos;
    int yPos;
    public Renderable renderable;
    public int turnValue;
    int xLocLow;
    int xLocHigh;
    int yLocHigh;
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

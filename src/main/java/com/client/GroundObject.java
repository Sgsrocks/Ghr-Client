package com.client;
// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

public final class GroundObject {

	public GroundObject() {
	}

	int anInt811;
	int anInt812;
	int anInt813;
	public Renderable renderable;
	public int uid;
	int groundDecorUID;
	byte aByte816;
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

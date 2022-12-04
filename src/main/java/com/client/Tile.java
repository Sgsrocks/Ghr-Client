package com.client;
// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

public final class Tile extends Node {

	public Tile(int i, int j, int k) {
		gameObjects = new GameObject[5];
		gameObjectsChanged = new int[5];
		renderLevel = z1AnInt1307 = i;
		anInt1308 = j;
		anInt1309 = k;
	}

	int z1AnInt1307;
	final int anInt1308;
	final int anInt1309;
	final int renderLevel;
	public SimpleTile mySimpleTile;
	public ShapedTile myShapedTile;
	public WallObject wallObject;
	public WallDecoration obj2;
	public GroundObject groundDecoration;
	public Object4 obj4;
	int gameObjectIndex;
	public final GameObject[] gameObjects;
	final int[] gameObjectsChanged;
	int totalTiledObjectMask;
	int logicHeight;
	boolean aBoolean1322;
	boolean aBoolean1323;
	boolean aBoolean1324;
	int anInt1325;
	int anInt1326;
	int anInt1327;
	int anInt1328;
	public Tile firstFloorTile;
}

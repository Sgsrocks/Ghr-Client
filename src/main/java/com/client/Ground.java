package com.client;
// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

public final class Ground extends Node {

	public Ground(int i, int j, int k) {
		obj5Array = new StaticObject[5];
		gameObjectsChanged = new int[5];
		anInt1310 = anInt1307 = i;
		anInt1308 = j;
		anInt1309 = k;
	}

	int anInt1307;
	final int anInt1308;
	final int anInt1309;
	final int anInt1310;
	public SceneTilePaint aClass43_1311;
	public SceneTileModel aClass40_1312;
	public WallObject wallObject;
	public Object2 obj2;
	public GroundObject groundObject;
	public Object4 obj4;
	int anInt1317;
	public final StaticObject[] obj5Array;
	final int[] gameObjectsChanged;
	int totalTiledObjectMask;
	int anInt1321;
	boolean aBoolean1322;
	boolean aBoolean1323;
	boolean aBoolean1324;
	int anInt1325;
	int anInt1326;
	int anInt1327;
	int anInt1328;
	public Ground aClass30_Sub3_1329;
}

package com.client;
// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

final class SceneTilePaint {

	public SceneTilePaint(int i, int j, int k, int l, int i1, int j1) {
		northEastColor = i;
		northColor = j;
		centerColor = k;
		eastColor = l;
		texture = i1;
		colorRGB = j1;
		this.flat = this.texture != -1;
	}

	final int northEastColor;
	final int northColor;
	final int centerColor;
	final int eastColor;
	final int texture;
	boolean flat;
	final int colorRGB;
}

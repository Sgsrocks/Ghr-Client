package com.client;
// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

final class SimpleTile {

	public SimpleTile(int northEastColor, int northColor, int centerColor, int eastColor, int texture, int colorRGB, boolean flat) {
		this.northEastColor = northEastColor;
		this.northColor = northColor;
		this.centerColor = centerColor;
		this.eastColor = eastColor;
		this.texture = texture;
		this.colorRGB = colorRGB;
		this.flat = flat;
	}

	final int northEastColor;
	final int northColor;
	final int centerColor;
	final int eastColor;
	final int texture;
	boolean flat;
	final int colorRGB;
}

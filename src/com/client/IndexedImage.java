package com.client;
// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

public final class IndexedImage extends Rasterizer2D {

	public IndexedImage(FileArchive streamLoader, String s, int i) {
		Stream stream = new Stream(streamLoader.getDataForName(s + ".dat"));
		Stream stream_1 = new Stream(streamLoader.getDataForName("index.dat"));
		stream_1.currentPosition = stream.readUnsignedShort();
		resizeWidth = stream_1.readUnsignedShort();
		resizeHeight = stream_1.readUnsignedShort();
		int j = stream_1.readUnsignedByte();
		palette = new int[j];
		for (int k = 0; k < j - 1; k++)
			palette[k + 1] = stream_1.read3Bytes();

		for (int l = 0; l < i; l++) {
			stream_1.currentPosition += 2;
			stream.currentPosition += stream_1.readUnsignedShort()
					* stream_1.readUnsignedShort();
			stream_1.currentPosition++;
		}

		drawOffsetX = stream_1.readUnsignedByte();
		drawOffsetY = stream_1.readUnsignedByte();
		width = stream_1.readUnsignedShort();
		height = stream_1.readUnsignedShort();
		int i1 = stream_1.readUnsignedByte();
		int j1 = width * height;
		palettePixels = new byte[j1];
		if (i1 == 0) {
			for (int k1 = 0; k1 < j1; k1++)
				palettePixels[k1] = stream.readSignedByte();

			return;
		}
		if (i1 == 1) {
			for (int l1 = 0; l1 < width; l1++) {
				for (int i2 = 0; i2 < height; i2++)
					palettePixels[l1 + i2 * width] = stream
							.readSignedByte();

			}

		}
	}

	public void method356() {
		resizeWidth /= 2;
		resizeHeight /= 2;
		byte abyte0[] = new byte[resizeWidth * resizeHeight];
		int i = 0;
		for (int j = 0; j < height; j++) {
			for (int k = 0; k < width; k++)
				abyte0[(k + drawOffsetX >> 1) + (j + drawOffsetY >> 1) * resizeWidth] = palettePixels[i++];

		}

		palettePixels = abyte0;
		width = resizeWidth;
		height = resizeHeight;
		drawOffsetX = 0;
		drawOffsetY = 0;
	}

	public void method357() {
		if (width == resizeWidth && height == resizeHeight)
			return;
		byte abyte0[] = new byte[resizeWidth * resizeHeight];
		int i = 0;
		for (int j = 0; j < height; j++) {
			for (int k = 0; k < width; k++)
				abyte0[k + drawOffsetX + (j + drawOffsetY) * resizeWidth] = palettePixels[i++];

		}

		palettePixels = abyte0;
		width = resizeWidth;
		height = resizeHeight;
		drawOffsetX = 0;
		drawOffsetY = 0;
	}
	public void downscale() {
		resizeWidth /= 2;
		resizeHeight /= 2;
		byte raster[] = new byte[resizeWidth * resizeHeight];
		int sourceIndex = 0;
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				raster[(x + drawOffsetX >> 1)
						+ (y + drawOffsetY >> 1) * resizeWidth] = raster[sourceIndex++];
			}
		}
		this.palettePixels = raster;
		width = resizeWidth;
		height = resizeHeight;
		drawOffsetX = 0;
		drawOffsetY = 0;
	}

	public void resize() {
		if (width == resizeWidth && height == resizeHeight) {
			return;
		}

		byte raster[] = new byte[resizeWidth * resizeHeight];

		int i = 0;
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				raster[x + drawOffsetX + (y + drawOffsetY) * resizeWidth] = raster[i++];
			}
		}
		this.palettePixels = raster;
		width = resizeWidth;
		height = resizeHeight;
		drawOffsetX = 0;
		drawOffsetY = 0;
	}

	public void method358() {
		byte abyte0[] = new byte[width * height];
		int j = 0;
		for (int k = 0; k < height; k++) {
			for (int l = width - 1; l >= 0; l--)
				abyte0[j++] = palettePixels[l + k * width];

		}

		palettePixels = abyte0;
		drawOffsetX = resizeWidth - width - drawOffsetX;
	}

	public void method359() {
		byte abyte0[] = new byte[width * height];
		int i = 0;
		for (int j = height - 1; j >= 0; j--) {
			for (int k = 0; k < width; k++)
				abyte0[i++] = palettePixels[k + j * width];

		}

		palettePixels = abyte0;
		drawOffsetY = resizeHeight - height - drawOffsetY;
	}

	public void method360(int i, int j, int k) {
		for (int i1 = 0; i1 < palette.length; i1++) {
			int j1 = palette[i1] >> 16 & 0xff;
			j1 += i;
			if (j1 < 0)
				j1 = 0;
			else if (j1 > 255)
				j1 = 255;
			int k1 = palette[i1] >> 8 & 0xff;
			k1 += j;
			if (k1 < 0)
				k1 = 0;
			else if (k1 > 255)
				k1 = 255;
			int l1 = palette[i1] & 0xff;
			l1 += k;
			if (l1 < 0)
				l1 = 0;
			else if (l1 > 255)
				l1 = 255;
			palette[i1] = (j1 << 16) + (k1 << 8) + l1;
		}
	}

	public void drawBackground(int i, int k) {
		i += drawOffsetX;
		k += drawOffsetY;
		int l = i + k * Rasterizer2D.width;
		int i1 = 0;
		int j1 = height;
		int k1 = width;
		int l1 = Rasterizer2D.width - k1;
		int i2 = 0;
		if (k < Rasterizer2D.clip_top) {
			int j2 = Rasterizer2D.clip_top - k;
			j1 -= j2;
			k = Rasterizer2D.clip_top;
			i1 += j2 * k1;
			l += j2 * Rasterizer2D.width;
		}
		if (k + j1 > Rasterizer2D.clip_bottom)
			j1 -= (k + j1) - Rasterizer2D.clip_bottom;
		if (i < Rasterizer2D.clip_left) {
			int k2 = Rasterizer2D.clip_left - i;
			k1 -= k2;
			i = Rasterizer2D.clip_left;
			i1 += k2;
			l += k2;
			i2 += k2;
			l1 += k2;
		}
		if (i + k1 > Rasterizer2D.clip_right) {
			int l2 = (i + k1) - Rasterizer2D.clip_right;
			k1 -= l2;
			i2 += l2;
			l1 += l2;
		}
		if (!(k1 <= 0 || j1 <= 0)) {
			method362(j1, Rasterizer2D.pixels, palettePixels, l1, l, k1, i1,
                    palette, i2);
		}
	}

	private void method362(int i, int ai[], byte abyte0[], int j, int k, int l,
			int i1, int ai1[], int j1) {
		int k1 = -(l >> 2);
		l = -(l & 3);
		for (int l1 = -i; l1 < 0; l1++) {
			for (int i2 = k1; i2 < 0; i2++) {
				byte byte1 = abyte0[i1++];
				if (byte1 != 0)
					ai[k++] = ai1[byte1 & 0xff];
				else
					k++;
				byte1 = abyte0[i1++];
				if (byte1 != 0)
					ai[k++] = ai1[byte1 & 0xff];
				else
					k++;
				byte1 = abyte0[i1++];
				if (byte1 != 0)
					ai[k++] = ai1[byte1 & 0xff];
				else
					k++;
				byte1 = abyte0[i1++];
				if (byte1 != 0)
					ai[k++] = ai1[byte1 & 0xff];
				else
					k++;
			}

			for (int j2 = l; j2 < 0; j2++) {
				byte byte2 = abyte0[i1++];
				if (byte2 != 0)
					ai[k++] = ai1[byte2 & 0xff];
				else
					k++;
			}

			k += j;
			i1 += j1;
		}

	}

	public void setTransparency(int transRed, int transGreen, int transBlue) {
		for (int index = 0; index < palettePixels.length; index++) {
			int pixel = palette[palettePixels[index]];
			if(pixel == 0xff00ff) {
				palettePixels[index] = 0;
			}
		}
	}
	

	public byte palettePixels[];
	public final int[] palette;
	public int width;
	public int height;
	public int drawOffsetX;
	public int drawOffsetY;
	public int resizeWidth;
	private int resizeHeight;
}

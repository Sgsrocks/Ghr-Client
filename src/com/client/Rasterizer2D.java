package com.client;


public class Rasterizer2D extends NodeSub {

	public static float[] depthBuffer;

	public static void initDrawingArea(int i, int j, int[] ai, float[] depth) {
		depthBuffer = depth;
		pixels = ai;
		width = j;
		height = i;
		setDrawingArea(i, 0, j, 0);
	}

	public static void drawTransparentBox(int leftX, int topY, int width, int height, int rgbColour, int opacity){
		if(leftX < Rasterizer2D.clip_left){
			width -= Rasterizer2D.clip_left - leftX;
			leftX = Rasterizer2D.clip_left;
		}
		if(topY < Rasterizer2D.clip_top){
			height -= Rasterizer2D.clip_top - topY;
			topY = Rasterizer2D.clip_top;
		}
		if(leftX + width > clip_right)
			width = clip_right - leftX;
		if(topY + height > clip_bottom)
			height = clip_bottom - topY;
		int transparency = 256 - opacity;
		int red = (rgbColour >> 16 & 0xff) * opacity;
		int green = (rgbColour >> 8 & 0xff) * opacity;
		int blue = (rgbColour & 0xff) * opacity;
		int leftOver = Rasterizer2D.width - width;
		int pixelIndex = leftX + topY * Rasterizer2D.width;
		for(int rowIndex = 0; rowIndex < height; rowIndex++){
			for(int columnIndex = 0; columnIndex < width; columnIndex++){
				int otherRed = (pixels[pixelIndex] >> 16 & 0xff) * transparency;
				int otherGreen = (pixels[pixelIndex] >> 8 & 0xff) * transparency;
				int otherBlue = (pixels[pixelIndex] & 0xff) * transparency;
				int transparentColour = ((red + otherRed >> 8) << 16) + ((green + otherGreen >> 8) << 8) + (blue + otherBlue >> 8);
				pixels[pixelIndex++] = transparentColour;
			}
			pixelIndex += leftOver;
		}
	}
	public static void clear(int color) {
		int length = width * height;
		int mod = length - (length & 0x7);
		int offset = 0;
		while (offset < mod) {
			pixels[(offset++)] = color;
			pixels[(offset++)] = color;
			pixels[(offset++)] = color;
			pixels[(offset++)] = color;
			pixels[(offset++)] = color;
			pixels[(offset++)] = color;
			pixels[(offset++)] = color;
			pixels[(offset++)] = color;
		}
		while (offset < length) {
			pixels[(offset++)] = color;
		}
	}

	public static void method336(int i, int j, int k, int l, int i1) {
		if (k < clip_left) {
			i1 -= clip_left - k;
			k = clip_left;
		}
		if (j < clip_top) {
			i -= clip_top - j;
			j = clip_top;
		}
		if (k + i1 > clip_right)
			i1 = clip_right - k;
		if (j + i > clip_bottom)
			i = clip_bottom - j;
		int k1 = width - i1;
		int l1 = k + j * width;
		for (int i2 = -i; i2 < 0; i2++) {
			for (int j2 = -i1; j2 < 0; j2++)
				pixels[l1++] = l;

			l1 += k1;
		}

	}
    public static void drawRoundedRectangle(int x, int y, int width, int height, int color,
                                            int alpha, boolean filled, boolean shadowed) {
        if (shadowed)
            drawRoundedRectangle(x + 1, y + 1, width, height, 0, alpha, filled,
                    false);
        if (alpha == -1) {
            if (filled) {
                drawHorizontalLine(y + 1, color, width - 4, x + 2);// method339
                drawHorizontalLine(y + height - 2, color, width - 4, x + 2);// method339
                drawPixels(height - 4, y + 2, x + 1, color, width - 2);// method336
            }
            drawHorizontalLine(y, color, width - 4, x + 2);// method339
            drawHorizontalLine(y + height - 1, color, width - 4, x + 2);// method339
            method341(y + 2, color, height - 4, x);// method341
            method341(y + 2, color, height - 4, x + width - 1);// method341
            drawPixels(1, y + 1, x + 1, color, 1);// method336
            drawPixels(1, y + 1, x + width - 2, color, 1);// method336
            drawPixels(1, y + height - 2, x + width - 2, color, 1);// method336
            drawPixels(1, y + height - 2, x + 1, color, 1);// method336
        } else if (alpha != -1) {
            if (filled) {
                method340(color, width - 4, y + 1, alpha, x + 2);// method340
                method340(color, width - 4, y + height - 2, alpha, x + 2);// method340
                method335(color, y + 2, width - 2, height - 4, alpha, x + 1);// method335
            }
            method340(color, width - 4, y, alpha, x + 2);// method340
            method340(color, width - 4, y + height - 1, alpha, x + 2);// method340
            method342(color, x, alpha, y + 2, height - 4);// method342
            method342(color, x + width - 1, alpha, y + 2, height - 4);// method342
            method335(color, y + 1, 1, 1, alpha, x + 1);// method335
            method335(color, y + 1, 1, 1, alpha, x + width - 2);// method335
            method335(color, y + height - 2, 1, 1, alpha, x + 1);// method335
            method335(color, y + height - 2, 1, 1, alpha, x + width - 2);// method335
        }
    }

    public static void drawPixelsWithOpacity(int color, int yPos, int pixelWidth, int pixelHeight, int opacityLevel,
											 int xPos) {
		if (xPos < clip_left) {
			pixelWidth -= clip_left - xPos;
			xPos = clip_left;
		}
		if (yPos < clip_top) {
			pixelHeight -= clip_top - yPos;
			yPos = clip_top;
		}
		if (xPos + pixelWidth > clip_right) {
			pixelWidth = clip_right - xPos;
		}
		if (yPos + pixelHeight > clip_bottom) {
			pixelHeight = clip_bottom - yPos;
		}
		int l1 = 256 - opacityLevel;
		int i2 = (color >> 16 & 0xFF) * opacityLevel;
		int j2 = (color >> 8 & 0xFF) * opacityLevel;
		int k2 = (color & 0xFF) * opacityLevel;
		int k3 = width - pixelWidth;
		int l3 = xPos + yPos * width;
		if (l3 > pixels.length - 1) {
			l3 = pixels.length - 1;
		}
		for (int i4 = 0; i4 < pixelHeight; i4++) {
			for (int j4 = -pixelWidth; j4 < 0; j4++) {
				int l2 = (pixels[l3] >> 16 & 0xFF) * l1;
				int i3 = (pixels[l3] >> 8 & 0xFF) * l1;
				int j3 = (pixels[l3] & 0xFF) * l1;
				int k4 = (i2 + l2 >> 8 << 16) + (j2 + i3 >> 8 << 8) + (k2 + j3 >> 8);
				pixels[(l3++)] = k4;
			}
			l3 += k3;
		}
	}

	public static void method338(int i, int j, int k, int l, int i1, int j1) {
		method340(l, i1, i, k, j1);
		method340(l, i1, (i + j) - 1, k, j1);
		if (j >= 3) {
			method342(l, j1, k, i + 1, j - 2);
			method342(l, (j1 + i1) - 1, k, i + 1, j - 2);
		}
	}

	public static void method339(int i, int j, int k, int l) {
		if (i < clip_top || i >= clip_bottom)
			return;
		if (l < clip_left) {
			k -= clip_left - l;
			l = clip_left;
		}
		if (l + k > clip_right)
			k = clip_right - l;
		int i1 = l + i * width;
		for (int j1 = 0; j1 < k; j1++)
			pixels[i1 + j1] = j;

	}

	private static void method340(int i, int j, int k, int l, int i1) {
		if (k < clip_top || k >= clip_bottom)
			return;
		if (i1 < clip_left) {
			j -= clip_left - i1;
			i1 = clip_left;
		}
		if (i1 + j > clip_right)
			j = clip_right - i1;
		int j1 = 256 - l;
		int k1 = (i >> 16 & 0xff) * l;
		int l1 = (i >> 8 & 0xff) * l;
		int i2 = (i & 0xff) * l;
		int i3 = i1 + k * width;
		for (int j3 = 0; j3 < j; j3++) {
			int j2 = (pixels[i3] >> 16 & 0xff) * j1;
			int k2 = (pixels[i3] >> 8 & 0xff) * j1;
			int l2 = (pixels[i3] & 0xff) * j1;
			int k3 = ((k1 + j2 >> 8) << 16) + ((l1 + k2 >> 8) << 8) + (i2 + l2 >> 8);
			pixels[i3++] = k3;
		}

	}

	public static void method341(int i, int j, int k, int l) {
		if (l < clip_left || l >= clip_right)
			return;
		if (i < clip_top) {
			k -= clip_top - i;
			i = clip_top;
		}
		if (i + k > clip_bottom)
			k = clip_bottom - i;
		int j1 = l + i * width;
		for (int k1 = 0; k1 < k; k1++)
			pixels[j1 + k1 * width] = j;

	}

	private static void method342(int i, int j, int k, int l, int i1) {
		if (j < clip_left || j >= clip_right)
			return;
		if (l < clip_top) {
			i1 -= clip_top - l;
			l = clip_top;
		}
		if (l + i1 > clip_bottom)
			i1 = clip_bottom - l;
		int j1 = 256 - k;
		int k1 = (i >> 16 & 0xff) * k;
		int l1 = (i >> 8 & 0xff) * k;
		int i2 = (i & 0xff) * k;
		int i3 = j + l * width;
		for (int j3 = 0; j3 < i1; j3++) {
			int j2 = (pixels[i3] >> 16 & 0xff) * j1;
			int k2 = (pixels[i3] >> 8 & 0xff) * j1;
			int l2 = (pixels[i3] & 0xff) * j1;
			int k3 = ((k1 + j2 >> 8) << 16) + ((l1 + k2 >> 8) << 8) + (i2 + l2 >> 8);
			pixels[i3] = k3;
			i3 += width;
		}
	}
	public static void drawBox(int leftX, int topY, int width, int height, int rgbColour) {
		if (leftX < Rasterizer2D.clip_left) {
			width -= Rasterizer2D.clip_left - leftX;
			leftX = Rasterizer2D.clip_left;
		}
		if (topY < Rasterizer2D.clip_top) {
			height -= Rasterizer2D.clip_top - topY;
			topY = Rasterizer2D.clip_top;
		}
		if (leftX + width > clip_right)
			width = clip_right - leftX;
		if (topY + height > clip_bottom)
			height = clip_bottom - topY;
		int leftOver = Rasterizer2D.width - width;
		int pixelIndex = leftX + topY * Rasterizer2D.width;
		for (int rowIndex = 0; rowIndex < height; rowIndex++) {
			for (int columnIndex = 0; columnIndex < width; columnIndex++)
				pixels[pixelIndex++] = rgbColour;
			pixelIndex += leftOver;
		}
	}

	public static void drawVerticalLine2(int xPosition, int yPosition, int height, int rgbColour){
		if(xPosition < clip_left || xPosition >= clip_right)
			return;
		if(yPosition < clip_top){
			height -= clip_top - yPosition;
			yPosition = clip_top;
		}
		if(yPosition + height > clip_bottom)
			height = clip_bottom - yPosition;
		int pixelIndex = xPosition + yPosition * width;
		for(int rowIndex = 0; rowIndex < height; rowIndex++)
			pixels[pixelIndex + rowIndex * width] = rgbColour;
	}

	public static void drawHorizontalLine2(int xPosition, int yPosition, int width, int rgbColour){
		if(yPosition < clip_top || yPosition >= clip_bottom)
			return;
		if(xPosition < clip_left){
			width -= clip_left - xPosition;
			xPosition = clip_left;
		}
		if(xPosition + width > clip_right)
			width = clip_right - xPosition;
		int pixelIndex = xPosition + yPosition * Rasterizer2D.width;
		for(int i = 0; i < width; i++)
			pixels[pixelIndex + i] = rgbColour;
	}

	public static void drawBoxOutline(int leftX, int topY, int width, int height, int rgbColour){
		drawHorizontalLine2(leftX, topY, width, rgbColour);
		drawHorizontalLine2(leftX, (topY + height) - 1, width, rgbColour);
		drawVerticalLine2(leftX, topY, height, rgbColour);
		drawVerticalLine2((leftX + width) - 1, topY, height, rgbColour);
	}

	public static void drawVerticalLine(int xPosition, int yPosition, int height, int rgbColour) {
		if (xPosition < clip_left || xPosition >= clip_right)
			return;
		if (yPosition < clip_top) {
			height -= clip_top - yPosition;
			yPosition = clip_top;
		}
		if (yPosition + height > clip_bottom)
			height = clip_bottom - yPosition;
		int pixelIndex = xPosition + yPosition * width;
		for (int rowIndex = 0; rowIndex < height; rowIndex++)
			pixels[pixelIndex + rowIndex * width] = rgbColour;
	}

	public static void drawAlphaBox(int x, int y, int lineWidth, int lineHeight, int color, int alpha) {// drawAlphaHorizontalLine
		if (y < clip_top) {
			if (y > (clip_top - lineHeight)) {
				lineHeight -= (clip_top - y);
				y += (clip_top - y);
			} else {
				return;
			}
		}
		if (y + lineHeight > clip_bottom) {
			lineHeight -= y + lineHeight - clip_bottom;
		}
		//if (y >= bottomY - lineHeight)
		//return;
		if (x < clip_left) {
			lineWidth -= clip_left - x;
			x = clip_left;
		}
		if (x + lineWidth > clip_right)
			lineWidth = clip_right - x;
		for(int yOff = 0; yOff < lineHeight; yOff++) {
			int i3 = x + (y + (yOff)) * width;
			for (int j3 = 0; j3 < lineWidth; j3++) {
				//int alpha2 = (lineWidth-j3) / (lineWidth/alpha);
				int j1 = 256 - alpha;//alpha2 is for gradient
				int k1 = (color >> 16 & 0xff) * alpha;
				int l1 = (color >> 8 & 0xff) * alpha;
				int i2 = (color & 0xff) * alpha;
				int j2 = (pixels[i3] >> 16 & 0xff) * j1;
				int k2 = (pixels[i3] >> 8 & 0xff) * j1;
				int l2 = (pixels[i3] & 0xff) * j1;
				int k3 = ((k1 + j2 >> 8) << 16) + ((l1 + k2 >> 8) << 8)
						+ (i2 + l2 >> 8);
				pixels[i3++] = k3;
			}
		}
	}

	public static void defaultDrawingAreaSize() {
		clip_left = 0;
		clip_top = 0;
		clip_right = width;
		clip_bottom = height;
		lastX = clip_right;
		viewportCenterX = clip_right / 2;
	}

	public void drawAlphaGradient(int x, int y, int gradientWidth,
								  int gradientHeight, int startColor, int endColor, int alpha) {
		int k1 = 0;
		int l1 = 0x10000 / gradientHeight;
		if (x < clip_left) {
			gradientWidth -= clip_left - x;
			x = clip_left;
		}
		if (y < clip_top) {
			k1 += (clip_top - y) * l1;
			gradientHeight -= clip_top - y;
			y = clip_top;
		}
		if (x + gradientWidth > clip_right)
			gradientWidth = clip_right - x;
		if (y + gradientHeight > clip_bottom)
			gradientHeight = clip_bottom - y;
		int i2 = width - gradientWidth;
		int result_alpha = 256 - alpha;
		int total_pixels = x + y * width;
		for (int k2 = -gradientHeight; k2 < 0; k2++) {
			int gradient1 = 0x10000 - k1 >> 8;
			int gradient2 = k1 >> 8;
			int gradient_color = ((startColor & 0xff00ff) * gradient1
					+ (endColor & 0xff00ff) * gradient2 & 0xff00ff00)
					+ ((startColor & 0xff00) * gradient1 + (endColor & 0xff00)
					* gradient2 & 0xff0000) >>> 8;
			int color = ((gradient_color & 0xff00ff) * alpha >> 8 & 0xff00ff)
					+ ((gradient_color & 0xff00) * alpha >> 8 & 0xff00);
			for (int k3 = -gradientWidth; k3 < 0; k3++) {
				int colored_pixel = pixels[total_pixels];
				colored_pixel = ((colored_pixel & 0xff00ff) * result_alpha >> 8 & 0xff00ff)
						+ ((colored_pixel & 0xff00) * result_alpha >> 8 & 0xff00);
				pixels[total_pixels++] = color + colored_pixel;
			}
			total_pixels += i2;
			k1 += l1;
		}
	}

	public void drawAlphaGradientOnSprite(Sprite sprite, int x, int y, int gradientWidth,
										  int gradientHeight, int startColor, int endColor, int alpha) {
		int k1 = 0;
		int l1 = 0x10000 / gradientHeight;
		if (x < clip_left) {
			gradientWidth -= clip_left - x;
			x = clip_left;
		}
		if (y < clip_top) {
			k1 += (clip_top - y) * l1;
			gradientHeight -= clip_top - y;
			y = clip_top;
		}
		if (x + gradientWidth > clip_right)
			gradientWidth = clip_right - x;
		if (y + gradientHeight > clip_bottom)
			gradientHeight = clip_bottom - y;
		int i2 = width - gradientWidth;
		int result_alpha = 256 - alpha;
		int total_pixels = x + y * width;
		for (int k2 = -gradientHeight; k2 < 0; k2++) {
			int gradient1 = 0x10000 - k1 >> 8;
			int gradient2 = k1 >> 8;
			int gradient_color = ((startColor & 0xff00ff) * gradient1
					+ (endColor & 0xff00ff) * gradient2 & 0xff00ff00)
					+ ((startColor & 0xff00) * gradient1 + (endColor & 0xff00)
					* gradient2 & 0xff0000) >>> 8;
			int color = ((gradient_color & 0xff00ff) * alpha >> 8 & 0xff00ff)
					+ ((gradient_color & 0xff00) * alpha >> 8 & 0xff00);
			for (int k3 = -gradientWidth; k3 < 0; k3++) {
				int colored_pixel = pixels[total_pixels];
				colored_pixel = ((colored_pixel & 0xff00ff) * result_alpha >> 8 & 0xff00ff)
						+ ((colored_pixel & 0xff00) * result_alpha >> 8 & 0xff00);
				pixels[total_pixels++] = color + colored_pixel;
			}
			total_pixels += i2;
			k1 += l1;
		}
	}



	public static void setDrawingArea(int i, int j, int k, int l) {
		if (j < 0)
			j = 0;
		if (l < 0)
			l = 0;
		if (k > width)
			k = width;
		if (i > height)
			i = height;
		clip_left = j;
		clip_top = l;
		clip_right = k;
		clip_bottom = i;
		lastX = clip_right;
		viewportCenterX = clip_right / 2;
		viewportCenterY = clip_bottom / 2;
	}
	public static void drawAlpha(int[] pixels, int index, int color, int alpha) {
		if (alpha <= 0) {
			return;
		}
		int prevColor = pixels[index];

		if ((prevColor & 0xFF000000) == 0 || alpha == 255) {
			// No transparency, so we can cheat to save CPU resources
			pixels[index] = (color & 0xFFFFFF) | (alpha << 24);
			return;
		}

		if ((prevColor & 0xFF000000) == 0xFF000000) {
			// When the background is opaque, the result will also be opaque,
			// so we can simply use the value calculated by Jagex.
			pixels[index] = color | 0xFF000000;
			return;
		}

		int prevAlpha = (prevColor >>> 24) * (255 - alpha) >>> 8;
		int finalAlpha = alpha + prevAlpha;

		// Scale alphas so (relativeAlpha >>> 8) is approximately equal to (alpha /
		// finalAlpha).
		// Avoiding extra divisions increase performance by quite a bit.
		// And with divisions we get a problems if dividing a number where
		// the last bit is 1 (as it will become negative).
		int relativeAlpha1 = (alpha << 8) / finalAlpha;
		int relativeAlpha2 = (prevAlpha << 8) / finalAlpha;

		// Red and blue are calculated at the same time to save CPU cycles
		int finalColor = (((color & 0xFF00FF) * relativeAlpha1 + (prevColor & 0xFF00FF) * relativeAlpha2 & 0xFF00FF00) | ((color & 0x00FF00) * relativeAlpha1 + (prevColor & 0x00FF00) * relativeAlpha2 & 0x00FF0000)) >>> 8;

		pixels[index] = finalColor | (finalAlpha << 24);
	}
	public static void drawAlpha2(int[] pixels, int index, int value, int color, int alpha) {
		if (alpha == 0) {
			return;
		}

		int prevColor = pixels[index];

		if ((prevColor & 0xFF000000) == 0 || alpha == 255) {
			// No transparency, so we can cheat to save CPU resources
			pixels[index] = (color & 0xFFFFFF) | (alpha << 24);
			return;
		}

		if ((prevColor & 0xFF000000) == 0xFF000000) {
			// When the background is opaque, the result will also be opaque,
			// so we can simply use the value calculated by Jagex.
			pixels[index] = value | 0xFF000000;
			return;
		}

		int prevAlpha = (prevColor >>> 24) * (255 - alpha) >>> 8;
		int finalAlpha = alpha + prevAlpha;

		// Scale alphas so (relativeAlpha >>> 8) is approximately equal to (alpha /
		// finalAlpha).
		// Avoiding extra divisions increase performance by quite a bit.
		// And with divisions we get a problems if dividing a number where
		// the last bit is 1 (as it will become negative).
		int relativeAlpha1 = (alpha << 8) / finalAlpha;
		int relativeAlpha2 = (prevAlpha << 8) / finalAlpha;

		// Red and blue are calculated at the same time to save CPU cycles
		int finalColor = (((color & 0xFF00FF) * relativeAlpha1 + (prevColor & 0xFF00FF) * relativeAlpha2 & 0xFF00FF00) | ((color & 0x00FF00) * relativeAlpha1 + (prevColor & 0x00FF00) * relativeAlpha2 & 0x00FF0000)) >>> 8;

		pixels[index] = finalColor | (finalAlpha << 24);
	}

	public static void drawAlpha(byte[] pixels, int index, byte color, int alpha) {

		if (alpha <= 0) {
			return;
		}

		alpha += (pixels[index] >>> 24) * (255 - alpha) >>> 8;
		pixels[index] = (byte) (color & 16777215 | alpha << 24);
	}

	public static void setAllPixelsToZero() {
		int i = width * height;
		for (int j = 0; j < i; j++) {
			pixels[j] = 0;
			//depthBuffer[j] = Float.MAX_VALUE;
		}
	}

	public static boolean drawHorizontalLine(int yPos, int lineColor, int lineWidth, int xPos) {// method339
		if (yPos < clip_top || yPos >= clip_bottom) {
			return false;
		}
		if (xPos < clip_left) {
			lineWidth -= clip_left - xPos;
			xPos = clip_left;
		}
		if (xPos + lineWidth > clip_right)
			lineWidth = clip_right - xPos;
		int i1 = xPos + yPos * width;
		for (int j1 = 0; j1 < lineWidth; j1++)
			pixels[i1 + j1] = lineColor;
		return true;
	}

	public static void method335(int i, int j, int k, int l, int i1, int k1) {
		if (k1 < clip_left) {
			k -= clip_left - k1;
			k1 = clip_left;
		}
		if (j < clip_top) {
			l -= clip_top - j;
			j = clip_top;
		}
		if (k1 + k > clip_right)
			k = clip_right - k1;
		if (j + l > clip_bottom)
			l = clip_bottom - j;
		int l1 = 256 - i1;
		int i2 = (i >> 16 & 0xff) * i1;
		int j2 = (i >> 8 & 0xff) * i1;
		int k2 = (i & 0xff) * i1;
		int k3 = width - k;
		int l3 = k1 + j * width;
		for (int i4 = 0; i4 < l; i4++) {
			for (int j4 = -k; j4 < 0; j4++) {
				int l2 = (pixels[l3] >> 16 & 0xff) * l1;
				int i3 = (pixels[l3] >> 8 & 0xff) * l1;
				int j3 = (pixels[l3] & 0xff) * l1;
				int k4 = ((i2 + l2 >> 8) << 16) + ((j2 + i3 >> 8) << 8)
						+ (k2 + j3 >> 8);
				pixels[l3++] = k4;
			}

			l3 += k3;
		}
	}

	public static void drawPixels(int i, int j, int k, int l, int i1) {
		if (k < clip_left) {
			i1 -= clip_left - k;
			k = clip_left;
		}
		if (j < clip_top) {
			i -= clip_top - j;
			j = clip_top;
		}
		if (k + i1 > clip_right)
			i1 = clip_right - k;
		if (j + i > clip_bottom)
			i = clip_bottom - j;
		int k1 = width - i1;
		int l1 = k + j * width;
		for (int i2 = -i; i2 < 0; i2++) {
			for (int j2 = -i1; j2 < 0; j2++)
				pixels[l1++] = l;

			l1 += k1;
		}

	}

	public static void fillRectangle(int x, int y, int width, int height, int colour) {
		if (x < clip_left) {
			width -= clip_left - x;
			x = clip_left;
		}
		if (y < clip_top) {
			height -= clip_top - y;
			y = clip_top;
		}
		if (x + width > clip_right)
			width = clip_right - x;
		if (y + height > clip_bottom)
			height = clip_bottom - y;
		int k1 = Rasterizer2D.width - width;
		int l1 = x + y * Rasterizer2D.width;
		if (l1 > pixels.length - 1) {
			l1 = pixels.length - 1;
		}
		for (int i2 = -height; i2 < 0; i2++) {
			for (int j2 = -width; j2 < 0; j2++)
				pixels[l1++] = colour;

			l1 += k1;
		}

	}

	public static void fillPixels(int i, int j, int k, int l, int i1) {
		method339(i1, l, j, i);
		method339((i1 + k) - 1, l, j, i);
		method341(i1, l, k, i);
		method341(i1, l, k, (i + j) - 1);
	}


	Rasterizer2D() {
	}

	public static int pixels[];
	public static int width;
	public static int height;
	public static int clip_top;
	public static int clip_bottom;
	public static int clip_left;
	public static int clip_right;
	public static int lastX;
	public static int viewportCenterX;
	public static int viewportCenterY;

}

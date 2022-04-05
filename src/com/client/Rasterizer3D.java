package com.client;

import static com.client.ObjectManager.lowMem;

public final class Rasterizer3D extends Rasterizer2D {

    public static boolean saveDepth;
    public static float[] depthBuffer;
    private static int mipMapLevel;
    public static int textureAmount = 131;
    static boolean textureOutOfDrawingBounds;
    private static boolean aBoolean1463;
    public static boolean aBoolean1464 = true;
    public static int alpha;
    public static int originViewX;
    public static int originViewY;
    public static int textureInt3;
    public static int textureInt4;
    private static int[] anIntArray1468;
    public static final int[] anIntArray1469;
    public static int anIntArray1470[];
    public static int COSINE[];
    public static int scanOffsets[];
    private static int textureCount;
    public static IndexedImage textures[] = new IndexedImage[textureAmount];
    private static boolean[] textureIsTransparant = new boolean[textureAmount];
    private static int[] averageTextureColours = new int[textureAmount];
    private static int textureRequestBufferPointer;
    private static int[][] textureRequestPixelBuffer;
    private static int[][] texturesPixelBuffer = new int[textureAmount][];
    public static int textureLastUsed[] = new int[textureAmount];
    public static int lastTextureRetrievalCount;
    public static int hslToRgb[] = new int[0x10000];
    private static int[][] currentPalette = new int[textureAmount][];

    static {
        anIntArray1468 = new int[512];
        anIntArray1469 = new int[2048];
        anIntArray1470 = new int[2048];
        COSINE = new int[2048];
        for (int i = 1; i < 512; i++) {
            anIntArray1468[i] = 32768 / i;
        }
        for (int j = 1; j < 2048; j++) {
            anIntArray1469[j] = 0x10000 / j;
        }
        for (int k = 0; k < 2048; k++) {
            anIntArray1470[k] = (int) (65536D * Math.sin((double) k * 0.0030679614999999999D));
            COSINE[k] = (int) (65536D * Math.cos((double) k * 0.0030679614999999999D));
        }
    }

    public static void nullLoader() {
        anIntArray1468 = null;
        anIntArray1468 = null;
        anIntArray1470 = null;
        COSINE = null;
        scanOffsets = null;
        textures = null;
        textureIsTransparant = null;
        averageTextureColours = null;
        textureRequestPixelBuffer = null;
        texturesPixelBuffer = null;
        textureLastUsed = null;
        hslToRgb = null;
        currentPalette = null;
    }

    public static void useViewport() {
        scanOffsets = new int[Rasterizer2D.height];
        for (int j = 0; j < Rasterizer2D.height; j++) {
            scanOffsets[j] = Rasterizer2D.width * j;
        }
        originViewX = Rasterizer2D.width / 2;
        originViewY = Rasterizer2D.height / 2;
    }

    public static void reposition(int width, int height) {
        scanOffsets = new int[height];
        for (int l = 0; l < height; l++) {
            scanOffsets[l] = width * l;
        }
        originViewX = width / 2;
        originViewY = height / 2;
    }

    public static void drawFog(int rgb, int begin, int end) {
        float length = end - begin;// Store as a float for division later
        for (int index = 0; index < pixels.length; index++) {
            float factor = (depthBuffer[index] - begin) / length;
            pixels[index] = blend(pixels[index], rgb, factor);
        }
    }

    private static int blend(int c1, int c2, float factor) {
        if (factor >= 1f) {
            return c2;
        }
        if (factor <= 0f) {
            return c1;
        }

        int r1 = (c1 >> 16) & 0xff;
        int g1 = (c1 >> 8) & 0xff;
        int b1 = (c1) & 0xff;

        int r2 = (c2 >> 16) & 0xff;
        int g2 = (c2 >> 8) & 0xff;
        int b2 = (c2) & 0xff;

        int r3 = r2 - r1;
        int g3 = g2 - g1;
        int b3 = b2 - b1;

        int r = (int) (r1 + (r3 * factor));
        int g = (int) (g1 + (g3 * factor));
        int b = (int) (b1 + (b3 * factor));

        return (r << 16) + (g << 8) + b;
    }

    public static int texelPos(int defaultIndex) {
        int x = (defaultIndex & 127) >> mipMapLevel;
        int y = (defaultIndex >> 7) >> mipMapLevel;
        return x + (y << (7 - mipMapLevel));
    }

    public static boolean enableMipmapping = true;
    public static boolean enableDistanceFog = true;

    private static final int[] ids = { 17, 31, 34, 40, 53, 54, 56, 57, 58, 59 };

    public static void setMipmapLevel(int y1, int y2, int y3, int x1, int x2, int x3, int tex) {
        if (!enableMipmapping) {
            if (mipMapLevel != 0) {
                mipMapLevel = 0;
            }
            return;
        }
        for (int tex2 : ids) {
            if (tex == tex2) {
                mipMapLevel = 0;
                return;
            }
        }
        int textureArea = x1 * (y2 - y3) + x2 * (y3 - y1) + x3 * (y1 - y2) >> 1;
        if (textureArea < 0) {
            textureArea = -textureArea;
        }
        if (textureArea > 16384) {
            mipMapLevel = 0;
        } else if (textureArea > 4096) {
            mipMapLevel = 1;
        } else if (textureArea > 1024) {
            mipMapLevel = 1;
        } else if (textureArea > 256) {
            mipMapLevel = 2;
        } else if (textureArea > 64) {
            mipMapLevel = 3;
        } else if (textureArea > 16) {
            mipMapLevel = 4;
        } else if (textureArea > 4) {
            mipMapLevel = 5;
        } else if (textureArea > 1) {
            mipMapLevel = 6;
        } else {
            mipMapLevel = 7;
        }
    }

    public static void method366() {
        textureRequestPixelBuffer = null;
        for (int j = 0; j < textureAmount; j++) {
            texturesPixelBuffer[j] = null;
        }
    }

    public static void method367() {
        if (textureRequestPixelBuffer == null) {
            textureRequestBufferPointer = 20;
            textureRequestPixelBuffer = new int[textureRequestBufferPointer][0x10000];
            for (int k = 0; k < textureAmount; k++) {
                texturesPixelBuffer[k] = null;
            }
        }
    }

    public static void loadTextures(FileArchive archive) {
        textureCount = 0;
        for (int index = 0; index < textureAmount; index++) {
            try {
                textures[index] = new IndexedImage(archive, String.valueOf(index), 0);
                if (lowMem && textures[index].resizeWidth == 128) {
                    textures[index].downscale();
                } else {
                    textures[index].resize();
                    textures[index].setTransparency(255, 0, 255);
                }
                textureCount++;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public static int method369(int texture) {
        if (averageTextureColours[texture] != 0) {
            return averageTextureColours[texture];
        }
        int r = 0;
        int g = 0;
        int b = 0;
        final int textureColorCount = currentPalette[texture].length;
        for (int index = 0; index < textureColorCount; index++) {
            r += currentPalette[texture][index] >> 16 & 0xff;
            g += currentPalette[texture][index] >> 8 & 0xff;
            b += currentPalette[texture][index] & 0xff;
        }
        int color = (r / textureColorCount << 16) + (g / textureColorCount << 8) + b / textureColorCount;
        color = adjustBrightness(color, 1.3999999999999999D);
        if (color == 0) {
            color = 1;
        }
        averageTextureColours[texture] = color;
        return color;
    }

    public static void method370(int texture) {
        try {
            if (texturesPixelBuffer[texture] == null) {
                return;
            }
            textureRequestPixelBuffer[textureRequestBufferPointer++] = texturesPixelBuffer[texture];
            texturesPixelBuffer[texture] = null;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    public static int getOverallColour(int textureId) {
        if (averageTextureColours[textureId] != 0) {
            return averageTextureColours[textureId];
        }
        int totalRed = 0;
        int totalGreen = 0;
        int totalBlue = 0;
        int colourCount = currentPalette[textureId].length;
        for (int ptr = 0; ptr < colourCount; ptr++) {
            totalRed += currentPalette[textureId][ptr] >> 16 & 0xff;
            totalGreen += currentPalette[textureId][ptr] >> 8 & 0xff;
            totalBlue += currentPalette[textureId][ptr] & 0xff;
        }

        int avgPaletteColour = (totalRed / colourCount << 16) + (totalGreen / colourCount << 8)
                + totalBlue / colourCount;
        avgPaletteColour = adjustBrightness(avgPaletteColour, 1.3999999999999999D);
        if (avgPaletteColour == 0) {
            avgPaletteColour = 1;
        }
        averageTextureColours[textureId] = avgPaletteColour;
        return avgPaletteColour;
    }

    public static void requestTextureUpdate(int textureId) {
        if (texturesPixelBuffer[textureId] == null) {
            return;
        }
        textureRequestPixelBuffer[textureRequestBufferPointer++] = texturesPixelBuffer[textureId];
        texturesPixelBuffer[textureId] = null;
    }

    public static int[] getTexturePixels(int textureId) {
        textureLastUsed[textureId] = lastTextureRetrievalCount++;
        if (texturesPixelBuffer[textureId] != null) {
            return texturesPixelBuffer[textureId];
        }
        int texturePixels[];
        if (textureRequestBufferPointer > 0) {
            texturePixels = textureRequestPixelBuffer[--textureRequestBufferPointer];
            textureRequestPixelBuffer[textureRequestBufferPointer] = null;
        } else {
            int lastUsed = 0;
            int target = -1;
            for (int l = 0; l < textureCount; l++) {
                if (texturesPixelBuffer[l] != null && (textureLastUsed[l] < lastUsed || target == -1)) {
                    lastUsed = textureLastUsed[l];
                    target = l;
                }
            }

            texturePixels = texturesPixelBuffer[target];
            texturesPixelBuffer[target] = null;
        }
        texturesPixelBuffer[textureId] = texturePixels;
        IndexedImage background = textures[textureId];
        int texturePalette[] = currentPalette[textureId];
        if (lowMem) {
            textureIsTransparant[textureId] = false;
            for (int i1 = 0; i1 < 4096; i1++) {
                int colour = texturePixels[i1] = texturePalette[background.palettePixels[i1]] & 0xf8f8ff;
                if (colour == 0) {
                    textureIsTransparant[textureId] = true;
                }
                texturePixels[4096 + i1] = colour - (colour >>> 3) & 0xf8f8ff;
                texturePixels[8192 + i1] = colour - (colour >>> 2) & 0xf8f8ff;
                texturePixels[12288 + i1] = colour - (colour >>> 2) - (colour >>> 3) & 0xf8f8ff;
            }

        } else {
            if (background.width == 64) {
                for (int x = 0; x < 128; x++) {
                    for (int y = 0; y < 128; y++) {
                        texturePixels[y
                                + (x << 7)] = texturePalette[background.palettePixels[(y >> 1) + ((x >> 1) << 6)]];
                    }
                }
            } else {
                for (int i = 0; i < 16384; i++) {
                    try {
                        texturePixels[i] = texturePalette[background.palettePixels[i]];
                    } catch (Exception e) {

                    }

                }
            }
            textureIsTransparant[textureId] = false;
            for (int i = 0; i < 16384; i++) {
                texturePixels[i] &= 0xf8f8ff;
                int colour = texturePixels[i];
                if (colour == 0) {
                    textureIsTransparant[textureId] = true;
                }
                texturePixels[16384 + i] = colour - (colour >>> 3) & 0xf8f8ff;
                texturePixels[32768 + i] = colour - (colour >>> 2) & 0xf8f8ff;
                texturePixels[49152 + i] = colour - (colour >>> 2) - (colour >>> 3) & 0xf8f8ff;
            }

        }
        return texturePixels;
    }

    public static double brightness = 0;
    public static void setBrightness(double bright) {
        brightness = bright;
        int j = 0;
        for (int k = 0; k < 512; k++) {
            double d1 = k / 8 / 64D + 0.0078125D;
            double d2 = (k & 7) / 8D + 0.0625D;
            for (int k1 = 0; k1 < 128; k1++) {
                double d3 = k1 / 128D;
                double r = d3;
                double g = d3;
                double b = d3;
                if (d2 != 0.0D) {
                    double d7;
                    if (d3 < 0.5D) {
                        d7 = d3 * (1.0D + d2);
                    } else {
                        d7 = (d3 + d2) - d3 * d2;
                    }
                    double d8 = 2D * d3 - d7;
                    double d9 = d1 + 0.33333333333333331D;
                    if (d9 > 1.0D) {
                        d9--;
                    }
                    double d10 = d1;
                    double d11 = d1 - 0.33333333333333331D;
                    if (d11 < 0.0D) {
                        d11++;
                    }
                    if (6D * d9 < 1.0D) {
                        r = d8 + (d7 - d8) * 6D * d9;
                    } else if (2D * d9 < 1.0D) {
                        r = d7;
                    } else if (3D * d9 < 2D) {
                        r = d8 + (d7 - d8) * (0.66666666666666663D - d9) * 6D;
                    } else {
                        r = d8;
                    }
                    if (6D * d10 < 1.0D) {
                        g = d8 + (d7 - d8) * 6D * d10;
                    } else if (2D * d10 < 1.0D) {
                        g = d7;
                    } else if (3D * d10 < 2D) {
                        g = d8 + (d7 - d8) * (0.66666666666666663D - d10) * 6D;
                    } else {
                        g = d8;
                    }
                    if (6D * d11 < 1.0D) {
                        b = d8 + (d7 - d8) * 6D * d11;
                    } else if (2D * d11 < 1.0D) {
                        b = d7;
                    } else if (3D * d11 < 2D) {
                        b = d8 + (d7 - d8) * (0.66666666666666663D - d11) * 6D;
                    } else {
                        b = d8;
                    }
                }
                int byteR = (int) (r * 256D);
                int byteG = (int) (g * 256D);
                int byteB = (int) (b * 256D);
                int rgb = (byteR << 16) + (byteG << 8) + byteB;
                rgb = adjustBrightness(rgb, bright);
                if (rgb == 0) {
                    rgb = 1;
                }
                hslToRgb[j++] = rgb;
            }

        }

        for (int textureId = 0; textureId < textureAmount; textureId++) {
            if (textures[textureId] != null) {
                int originalPalette[] = textures[textureId].palette;
                currentPalette[textureId] = new int[originalPalette.length];
                for (int colourId = 0; colourId < originalPalette.length; colourId++) {
                    currentPalette[textureId][colourId] = adjustBrightness(originalPalette[colourId],
                            bright);
                    if ((currentPalette[textureId][colourId] & 0xf8f8ff) == 0 && colourId != 0) {
                        currentPalette[textureId][colourId] = 1;
                    }
                }

            }
        }

        for (int textureId = 0; textureId < textureAmount; textureId++) {
            requestTextureUpdate(textureId);
        }

    }

    static int adjustBrightness(int color, double amt) {
        double red = (color >> 16) / 256D;
        double green = (color >> 8 & 0xff) / 256D;
        double blue = (color & 0xff) / 256D;
        red = Math.pow(red, amt);
        green = Math.pow(green, amt);
        blue = Math.pow(blue, amt);
        final int red2 = (int) (red * 256D);
        final int green2 = (int) (green * 256D);
        final int blue2 = (int) (blue * 256D);
        return (red2 << 16) + (green2 << 8) + blue2;
    }

    public static boolean enableHDTextures = false;

    public static void drawMaterializedTriangle(int y1, int y2, int y3, int x1, int x2, int x3, int hsl1, int hsl2,
                                                int hsl3, int tx1, int tx2, int tx3, int ty1, int ty2, int ty3, int tz1, int tz2, int tz3, int tex, float z1,
                                                float z2, float z3) {
        if (!enableHDTextures || Texture.get(tex) == null) {
            drawGouraudTriangle(y1, y2, y3, x1, x2, x3, hsl1, hsl2, hsl3, z1, z2, z3);
            return;
        }
        setMipmapLevel(y1, y2, y3, x1, x2, x3, tex);
        int[] texels = Texture.get(tex).mipmaps[mipMapLevel];
        tx2 = tx1 - tx2;
        ty2 = ty1 - ty2;
        tz2 = tz1 - tz2;
        tx3 -= tx1;
        ty3 -= ty1;
        tz3 -= tz1;
        int l4 = (tx3 * ty1 - ty3 * tx1) * WorldController.focalLength << 5;
        int i5 = ty3 * tz1 - tz3 * ty1 << 8;
        int j5 = tz3 * tx1 - tx3 * tz1 << 5;
        int k5 = (tx2 * ty1 - ty2 * tx1) * WorldController.focalLength << 5;
        int l5 = ty2 * tz1 - tz2 * ty1 << 8;
        int i6 = tz2 * tx1 - tx2 * tz1 << 5;
        int j6 = (ty2 * tx3 - tx2 * ty3) * WorldController.focalLength << 5;
        int k6 = tz2 * ty3 - ty2 * tz3 << 8;
        int l6 = tx2 * tz3 - tz2 * tx3 << 5;
        int i7 = 0;
        int j7 = 0;
        if (y2 != y1) {
            i7 = (x2 - x1 << 16) / (y2 - y1);
            j7 = (hsl2 - hsl1 << 15) / (y2 - y1);
        }
        int k7 = 0;
        int l7 = 0;
        if (y3 != y2) {
            k7 = (x3 - x2 << 16) / (y3 - y2);
            l7 = (hsl3 - hsl2 << 15) / (y3 - y2);
        }
        int i8 = 0;
        int j8 = 0;
        if (y3 != y1) {
            i8 = (x1 - x3 << 16) / (y1 - y3);
            j8 = (hsl1 - hsl3 << 15) / (y1 - y3);
        }

        float x21 = x2 - x1;
        float y32 = y2 - y1;
        float x31 = x3 - x1;
        float y31 = y3 - y1;
        float z21 = z2 - z1;
        float z31 = z3 - z1;

        float div = x21 * y31 - x31 * y32;
        float depthSlope = (z21 * y31 - z31 * y32) / div;
        float depthScale = (z31 * x21 - z21 * x31) / div;

        if (y1 <= y2 && y1 <= y3) {
            if (y1 >= Rasterizer2D.clip_bottom) {
                return;
            }
            if (y2 > Rasterizer2D.clip_bottom) {
                y2 = Rasterizer2D.clip_bottom;
            }
            if (y3 > Rasterizer2D.clip_bottom) {
                y3 = Rasterizer2D.clip_bottom;
            }
            z1 = z1 - depthSlope * x1 + depthSlope;
            if (y2 < y3) {
                x3 = x1 <<= 16;
                hsl3 = hsl1 <<= 15;
                if (y1 < 0) {
                    x3 -= i8 * y1;
                    x1 -= i7 * y1;
                    z1 -= depthScale * y1;
                    hsl3 -= j8 * y1;
                    hsl1 -= j7 * y1;
                    y1 = 0;
                }
                x2 <<= 16;
                hsl2 <<= 15;
                if (y2 < 0) {
                    x2 -= k7 * y2;
                    hsl2 -= l7 * y2;
                    y2 = 0;
                }
                int k8 = y1 - originViewY;
                l4 += j5 * k8;
                k5 += i6 * k8;
                j6 += l6 * k8;
                if (y1 != y2 && i8 < i7 || y1 == y2 && i8 > k7) {
                    y3 -= y2;
                    y2 -= y1;
                    y1 = scanOffsets[y1];
                    while (--y2 >= 0) {
                        drawMaterializedScanline(Rasterizer2D.pixels, texels, y1, x3 >> 16, x1 >> 16, hsl3 >> 7,
                                hsl1 >> 7, l4, k5, j6, i5, l5, k6, z1, depthSlope);
                        x3 += i8;
                        x1 += i7;
                        z1 += depthScale;
                        hsl3 += j8;
                        hsl1 += j7;
                        y1 += Rasterizer2D.width;
                        l4 += j5;
                        k5 += i6;
                        j6 += l6;
                    }
                    while (--y3 >= 0) {
                        drawMaterializedScanline(Rasterizer2D.pixels, texels, y1, x3 >> 16, x2 >> 16, hsl3 >> 7,
                                hsl2 >> 7, l4, k5, j6, i5, l5, k6, z1, depthSlope);
                        x3 += i8;
                        x2 += k7;
                        z1 += depthScale;
                        hsl3 += j8;
                        hsl2 += l7;
                        y1 += Rasterizer2D.width;
                        l4 += j5;
                        k5 += i6;
                        j6 += l6;
                    }
                    texels = null;
                    return;
                }
                y3 -= y2;
                y2 -= y1;
                y1 = scanOffsets[y1];
                while (--y2 >= 0) {
                    drawMaterializedScanline(Rasterizer2D.pixels, texels, y1, x1 >> 16, x3 >> 16, hsl1 >> 7, hsl3 >> 7,
                            l4, k5, j6, i5, l5, k6, z1, depthSlope);
                    x3 += i8;
                    x1 += i7;
                    z1 += depthScale;
                    hsl3 += j8;
                    hsl1 += j7;
                    y1 += Rasterizer2D.width;
                    l4 += j5;
                    k5 += i6;
                    j6 += l6;
                }
                while (--y3 >= 0) {
                    drawMaterializedScanline(Rasterizer2D.pixels, texels, y1, x2 >> 16, x3 >> 16, hsl2 >> 7, hsl3 >> 7,
                            l4, k5, j6, i5, l5, k6, z1, depthSlope);
                    x3 += i8;
                    x2 += k7;
                    z1 += depthScale;
                    hsl3 += j8;
                    hsl2 += l7;
                    y1 += Rasterizer2D.width;
                    l4 += j5;
                    k5 += i6;
                    j6 += l6;
                }
                texels = null;
                return;
            }
            x2 = x1 <<= 16;
            hsl2 = hsl1 <<= 15;
            if (y1 < 0) {
                x2 -= i8 * y1;
                x1 -= i7 * y1;
                z1 -= depthScale * y1;
                hsl2 -= j8 * y1;
                hsl1 -= j7 * y1;
                y1 = 0;
            }
            x3 <<= 16;
            hsl3 <<= 15;
            if (y3 < 0) {
                x3 -= k7 * y3;
                hsl3 -= l7 * y3;
                y3 = 0;
            }
            int l8 = y1 - originViewY;
            l4 += j5 * l8;
            k5 += i6 * l8;
            j6 += l6 * l8;
            if (y1 != y3 && i8 < i7 || y1 == y3 && k7 > i7) {
                y2 -= y3;
                y3 -= y1;
                y1 = scanOffsets[y1];
                while (--y3 >= 0) {
                    drawMaterializedScanline(Rasterizer2D.pixels, texels, y1, x2 >> 16, x1 >> 16, hsl2 >> 7, hsl1 >> 7,
                            l4, k5, j6, i5, l5, k6, z1, depthSlope);
                    x2 += i8;
                    x1 += i7;
                    z1 += depthScale;
                    hsl2 += j8;
                    hsl1 += j7;
                    y1 += Rasterizer2D.width;
                    l4 += j5;
                    k5 += i6;
                    j6 += l6;
                }
                while (--y2 >= 0) {
                    drawMaterializedScanline(Rasterizer2D.pixels, texels, y1, x3 >> 16, x1 >> 16, hsl3 >> 7, hsl1 >> 7,
                            l4, k5, j6, i5, l5, k6, z1, depthSlope);
                    x3 += k7;
                    x1 += i7;
                    z1 += depthScale;
                    hsl3 += l7;
                    hsl1 += j7;
                    y1 += Rasterizer2D.width;
                    l4 += j5;
                    k5 += i6;
                    j6 += l6;
                }
                texels = null;
                return;
            }
            y2 -= y3;
            y3 -= y1;
            y1 = scanOffsets[y1];
            while (--y3 >= 0) {
                drawMaterializedScanline(Rasterizer2D.pixels, texels, y1, x1 >> 16, x2 >> 16, hsl1 >> 7, hsl2 >> 7, l4,
                        k5, j6, i5, l5, k6, z1, depthSlope);
                x2 += i8;
                x1 += i7;
                z1 += depthScale;
                hsl2 += j8;
                hsl1 += j7;
                y1 += Rasterizer2D.width;
                l4 += j5;
                k5 += i6;
                j6 += l6;
            }
            while (--y2 >= 0) {
                drawMaterializedScanline(Rasterizer2D.pixels, texels, y1, x1 >> 16, x3 >> 16, hsl1 >> 7, hsl3 >> 7, l4,
                        k5, j6, i5, l5, k6, z1, depthSlope);
                x3 += k7;
                x1 += i7;
                z1 += depthScale;
                hsl3 += l7;
                hsl1 += j7;
                y1 += Rasterizer2D.width;
                l4 += j5;
                k5 += i6;
                j6 += l6;
            }
            texels = null;
            return;
        }
        if (y2 <= y3) {
            if (y2 >= Rasterizer2D.clip_bottom) {
                return;
            }
            if (y3 > Rasterizer2D.clip_bottom) {
                y3 = Rasterizer2D.clip_bottom;
            }
            if (y1 > Rasterizer2D.clip_bottom) {
                y1 = Rasterizer2D.clip_bottom;
            }
            z2 = z2 - depthSlope * x2 + depthSlope;
            if (y3 < y1) {
                x1 = x2 <<= 16;
                hsl1 = hsl2 <<= 15;
                if (y2 < 0) {
                    x1 -= i7 * y2;
                    x2 -= k7 * y2;
                    z2 -= depthScale * y2;
                    hsl1 -= j7 * y2;
                    hsl2 -= l7 * y2;
                    y2 = 0;
                }
                x3 <<= 16;
                hsl3 <<= 15;
                if (y3 < 0) {
                    x3 -= i8 * y3;
                    hsl3 -= j8 * y3;
                    y3 = 0;
                }
                int i9 = y2 - originViewY;
                l4 += j5 * i9;
                k5 += i6 * i9;
                j6 += l6 * i9;
                if (y2 != y3 && i7 < k7 || y2 == y3 && i7 > i8) {
                    y1 -= y3;
                    y3 -= y2;
                    y2 = scanOffsets[y2];
                    while (--y3 >= 0) {
                        drawMaterializedScanline(Rasterizer2D.pixels, texels, y2, x1 >> 16, x2 >> 16, hsl1 >> 7,
                                hsl2 >> 7, l4, k5, j6, i5, l5, k6, z2, depthSlope);
                        x1 += i7;
                        x2 += k7;
                        z2 += depthScale;
                        hsl1 += j7;
                        hsl2 += l7;
                        y2 += Rasterizer2D.width;
                        l4 += j5;
                        k5 += i6;
                        j6 += l6;
                    }
                    while (--y1 >= 0) {
                        drawMaterializedScanline(Rasterizer2D.pixels, texels, y2, x1 >> 16, x3 >> 16, hsl1 >> 7,
                                hsl3 >> 7, l4, k5, j6, i5, l5, k6, z2, depthSlope);
                        x1 += i7;
                        x3 += i8;
                        z2 += depthScale;
                        hsl1 += j7;
                        hsl3 += j8;
                        y2 += Rasterizer2D.width;
                        l4 += j5;
                        k5 += i6;
                        j6 += l6;
                    }
                    texels = null;
                    return;
                }
                y1 -= y3;
                y3 -= y2;
                y2 = scanOffsets[y2];
                while (--y3 >= 0) {
                    drawMaterializedScanline(Rasterizer2D.pixels, texels, y2, x2 >> 16, x1 >> 16, hsl2 >> 7, hsl1 >> 7,
                            l4, k5, j6, i5, l5, k6, z2, depthSlope);
                    x1 += i7;
                    x2 += k7;
                    z2 += depthScale;
                    hsl1 += j7;
                    hsl2 += l7;
                    y2 += Rasterizer2D.width;
                    l4 += j5;
                    k5 += i6;
                    j6 += l6;
                }
                while (--y1 >= 0) {
                    drawMaterializedScanline(Rasterizer2D.pixels, texels, y2, x3 >> 16, x1 >> 16, hsl3 >> 7, hsl1 >> 7,
                            l4, k5, j6, i5, l5, k6, z2, depthSlope);
                    x1 += i7;
                    x3 += i8;
                    z2 += depthScale;
                    hsl1 += j7;
                    hsl3 += j8;
                    y2 += Rasterizer2D.width;
                    l4 += j5;
                    k5 += i6;
                    j6 += l6;
                }
                texels = null;
                return;
            }
            x3 = x2 <<= 16;
            hsl3 = hsl2 <<= 15;
            if (y2 < 0) {
                x3 -= i7 * y2;
                x2 -= k7 * y2;
                z2 -= depthScale * y2;
                hsl3 -= j7 * y2;
                hsl2 -= l7 * y2;
                y2 = 0;
            }
            x1 <<= 16;
            hsl1 <<= 15;
            if (y1 < 0) {
                x1 -= i8 * y1;
                hsl1 -= j8 * y1;
                y1 = 0;
            }
            int j9 = y2 - originViewY;
            l4 += j5 * j9;
            k5 += i6 * j9;
            j6 += l6 * j9;
            if (i7 < k7) {
                y3 -= y1;
                y1 -= y2;
                y2 = scanOffsets[y2];
                while (--y1 >= 0) {
                    drawMaterializedScanline(Rasterizer2D.pixels, texels, y2, x3 >> 16, x2 >> 16, hsl3 >> 7, hsl2 >> 7,
                            l4, k5, j6, i5, l5, k6, z2, depthSlope);
                    x3 += i7;
                    x2 += k7;
                    z2 += depthScale;
                    hsl3 += j7;
                    hsl2 += l7;
                    y2 += Rasterizer2D.width;
                    l4 += j5;
                    k5 += i6;
                    j6 += l6;
                }
                while (--y3 >= 0) {
                    drawMaterializedScanline(Rasterizer2D.pixels, texels, y2, x1 >> 16, x2 >> 16, hsl1 >> 7, hsl2 >> 7,
                            l4, k5, j6, i5, l5, k6, z2, depthSlope);
                    x1 += i8;
                    x2 += k7;
                    z2 += depthScale;
                    hsl1 += j8;
                    hsl2 += l7;
                    y2 += Rasterizer2D.width;
                    l4 += j5;
                    k5 += i6;
                    j6 += l6;
                }
                texels = null;
                return;
            }
            y3 -= y1;
            y1 -= y2;
            y2 = scanOffsets[y2];
            while (--y1 >= 0) {
                drawMaterializedScanline(Rasterizer2D.pixels, texels, y2, x2 >> 16, x3 >> 16, hsl2 >> 7, hsl3 >> 7, l4,
                        k5, j6, i5, l5, k6, z2, depthSlope);
                x3 += i7;
                x2 += k7;
                z2 += depthScale;
                hsl3 += j7;
                hsl2 += l7;
                y2 += Rasterizer2D.width;
                l4 += j5;
                k5 += i6;
                j6 += l6;
            }
            while (--y3 >= 0) {
                drawMaterializedScanline(Rasterizer2D.pixels, texels, y2, x2 >> 16, x1 >> 16, hsl2 >> 7, hsl1 >> 7, l4,
                        k5, j6, i5, l5, k6, z2, depthSlope);
                x1 += i8;
                x2 += k7;
                z2 += depthScale;
                hsl1 += j8;
                hsl2 += l7;
                y2 += Rasterizer2D.width;
                l4 += j5;
                k5 += i6;
                j6 += l6;
            }
            texels = null;
            return;
        }
        if (y3 >= Rasterizer2D.clip_bottom) {
            return;
        }
        if (y1 > Rasterizer2D.clip_bottom) {
            y1 = Rasterizer2D.clip_bottom;
        }
        if (y2 > Rasterizer2D.clip_bottom) {
            y2 = Rasterizer2D.clip_bottom;
        }
        z3 = z3 - depthSlope * x3 + depthSlope;
        if (y1 < y2) {
            x2 = x3 <<= 16;
            hsl2 = hsl3 <<= 15;
            if (y3 < 0) {
                x2 -= k7 * y3;
                x3 -= i8 * y3;
                z3 -= depthScale * y3;
                hsl2 -= l7 * y3;
                hsl3 -= j8 * y3;
                y3 = 0;
            }
            x1 <<= 16;
            hsl1 <<= 15;
            if (y1 < 0) {
                x1 -= i7 * y1;
                hsl1 -= j7 * y1;
                y1 = 0;
            }
            int k9 = y3 - originViewY;
            l4 += j5 * k9;
            k5 += i6 * k9;
            j6 += l6 * k9;
            if (k7 < i8) {
                y2 -= y1;
                y1 -= y3;
                y3 = scanOffsets[y3];
                while (--y1 >= 0) {
                    drawMaterializedScanline(Rasterizer2D.pixels, texels, y3, x2 >> 16, x3 >> 16, hsl2 >> 7, hsl3 >> 7,
                            l4, k5, j6, i5, l5, k6, z3, depthSlope);
                    x2 += k7;
                    x3 += i8;
                    z3 += depthScale;
                    hsl2 += l7;
                    hsl3 += j8;
                    y3 += Rasterizer2D.width;
                    l4 += j5;
                    k5 += i6;
                    j6 += l6;
                }
                while (--y2 >= 0) {
                    drawMaterializedScanline(Rasterizer2D.pixels, texels, y3, x2 >> 16, x1 >> 16, hsl2 >> 7, hsl1 >> 7,
                            l4, k5, j6, i5, l5, k6, z3, depthSlope);
                    x2 += k7;
                    x1 += i7;
                    z3 += depthScale;
                    hsl2 += l7;
                    hsl1 += j7;
                    y3 += Rasterizer2D.width;
                    l4 += j5;
                    k5 += i6;
                    j6 += l6;
                }
                texels = null;
                return;
            }
            y2 -= y1;
            y1 -= y3;
            y3 = scanOffsets[y3];
            while (--y1 >= 0) {
                drawMaterializedScanline(Rasterizer2D.pixels, texels, y3, x3 >> 16, x2 >> 16, hsl3 >> 7, hsl2 >> 7, l4,
                        k5, j6, i5, l5, k6, z3, depthSlope);
                x2 += k7;
                x3 += i8;
                z3 += depthScale;
                hsl2 += l7;
                hsl3 += j8;
                y3 += Rasterizer2D.width;
                l4 += j5;
                k5 += i6;
                j6 += l6;
            }
            while (--y2 >= 0) {
                drawMaterializedScanline(Rasterizer2D.pixels, texels, y3, x1 >> 16, x2 >> 16, hsl1 >> 7, hsl2 >> 7, l4,
                        k5, j6, i5, l5, k6, z3, depthSlope);
                x2 += k7;
                x1 += i7;
                z3 += depthScale;
                hsl2 += l7;
                hsl1 += j7;
                y3 += Rasterizer2D.width;
                l4 += j5;
                k5 += i6;
                j6 += l6;
            }
            texels = null;
            return;
        }
        x1 = x3 <<= 16;
        hsl1 = hsl3 <<= 15;
        if (y3 < 0) {
            x1 -= k7 * y3;
            x3 -= i8 * y3;
            z3 -= depthScale * y3;
            hsl1 -= l7 * y3;
            hsl3 -= j8 * y3;
            y3 = 0;
        }
        x2 <<= 16;
        hsl2 <<= 15;
        if (y2 < 0) {
            x2 -= i7 * y2;
            hsl2 -= j7 * y2;
            y2 = 0;
        }
        int l9 = y3 - originViewY;
        l4 += j5 * l9;
        k5 += i6 * l9;
        j6 += l6 * l9;
        if (k7 < i8) {
            y1 -= y2;
            y2 -= y3;
            y3 = scanOffsets[y3];
            while (--y2 >= 0) {
                drawMaterializedScanline(Rasterizer2D.pixels, texels, y3, x1 >> 16, x3 >> 16, hsl1 >> 7, hsl3 >> 7, l4,
                        k5, j6, i5, l5, k6, z3, depthSlope);
                x1 += k7;
                x3 += i8;
                z3 += depthScale;
                hsl1 += l7;
                hsl3 += j8;
                y3 += Rasterizer2D.width;
                l4 += j5;
                k5 += i6;
                j6 += l6;
            }
            while (--y1 >= 0) {
                drawMaterializedScanline(Rasterizer2D.pixels, texels, y3, x2 >> 16, x3 >> 16, hsl2 >> 7, hsl3 >> 7, l4,
                        k5, j6, i5, l5, k6, z3, depthSlope);
                x2 += i7;
                x3 += i8;
                z3 += depthScale;
                hsl2 += j7;
                hsl3 += j8;
                y3 += Rasterizer2D.width;
                l4 += j5;
                k5 += i6;
                j6 += l6;
            }
            texels = null;
            return;
        }
        y1 -= y2;
        y2 -= y3;
        y3 = scanOffsets[y3];
        while (--y2 >= 0) {
            drawMaterializedScanline(Rasterizer2D.pixels, texels, y3, x3 >> 16, x1 >> 16, hsl3 >> 7, hsl1 >> 7, l4, k5,
                    j6, i5, l5, k6, z3, depthSlope);
            x1 += k7;
            x3 += i8;
            z3 += depthScale;
            hsl1 += l7;
            hsl3 += j8;
            y3 += Rasterizer2D.width;
            l4 += j5;
            k5 += i6;
            j6 += l6;
        }
        while (--y1 >= 0) {
            drawMaterializedScanline(Rasterizer2D.pixels, texels, y3, x3 >> 16, x2 >> 16, hsl3 >> 7, hsl2 >> 7, l4, k5,
                    j6, i5, l5, k6, z3, depthSlope);
            x2 += i7;
            x3 += i8;
            z3 += depthScale;
            hsl2 += j7;
            hsl3 += j8;
            y3 += Rasterizer2D.width;
            l4 += j5;
            k5 += i6;
            j6 += l6;
        }
        texels = null;
    }

    private static final void drawMaterializedScanline(int[] dest, int[] texels, int offset, int x1, int x2, int hsl1,
                                                       int hsl2, int t1, int t2, int t3, int t4, int t5, int t6, float z1, float z2) {
        if (x2 <= x1) {
            return;
        }
        int texPos = 0;
        int rgb = 0;
        if (textureOutOfDrawingBounds) {
            if (x2 > Rasterizer2D.lastX) {
                x2 = Rasterizer2D.lastX;
            }
            if (x1 < 0) {
                x1 = 0;
            }
        }
        if (x1 < x2) {
            offset += x1;
            z1 += z2 * (float) x1;
            int n = x2 - x1 >> 2;
            int dhsl = 0;
            if (n > 0) {
                dhsl = (hsl2 - hsl1) * anIntArray1468[n] >> 15;
            }
            int dist = x1 - originViewX;
            t1 += (t4 >> 3) * dist;
            t2 += (t5 >> 3) * dist;
            t3 += (t6 >> 3) * dist;
            int i_57_ = t3 >> 14;
            int i_58_;
            int i_59_;
            if (i_57_ != 0) {
                i_58_ = t1 / i_57_;
                i_59_ = t2 / i_57_;
            } else {
                i_58_ = 0;
                i_59_ = 0;
            }
            t1 += t4;
            t2 += t5;
            t3 += t6;
            i_57_ = t3 >> 14;
            int i_60_;
            int i_61_;
            if (i_57_ != 0) {
                i_60_ = t1 / i_57_;
                i_61_ = t2 / i_57_;
            } else {
                i_60_ = 0;
                i_61_ = 0;
            }
            texPos = (i_58_ << 18) + i_59_;
            int dtexPos = (i_60_ - i_58_ >> 3 << 18) + (i_61_ - i_59_ >> 3);
            n >>= 1;
            int light;
            if (n > 0) {
                do {
                    hsl1 += dhsl;
                    rgb = texels[texelPos((texPos & 0x3f80) + (texPos >>> 25))];
                    light = ((hsl1 >> 8 & 0x7f) << 1) * (((rgb >> 16 & 0xff) + (rgb >> 8 & 0xff) + (rgb & 0xff)) / 3)
                            / 384;
                    if (light > 127) {
                        light = 127;
                    }
                    texPos += dtexPos;
                    dest[offset] = hslToRgb[(hsl1 >> 8 & 0xff80) | light];
                    if (saveDepth) {
                        depthBuffer[offset] = z1;
                    }
                    z1 += z2;
                    offset++;
                    rgb = texels[texelPos((texPos & 0x3f80) + (texPos >>> 25))];
                    light = ((hsl1 >> 8 & 0x7f) << 1) * (((rgb >> 16 & 0xff) + (rgb >> 8 & 0xff) + (rgb & 0xff)) / 3)
                            / 384;
                    if (light > 127) {
                        light = 127;
                    }
                    texPos += dtexPos;
                    dest[offset] = hslToRgb[(hsl1 >> 8 & 0xff80) | light];
                    if (saveDepth) {
                        depthBuffer[offset] = z1;
                    }
                    z1 += z2;
                    offset++;
                    rgb = texels[texelPos((texPos & 0x3f80) + (texPos >>> 25))];
                    light = ((hsl1 >> 8 & 0x7f) << 1) * (((rgb >> 16 & 0xff) + (rgb >> 8 & 0xff) + (rgb & 0xff)) / 3)
                            / 384;
                    if (light > 127) {
                        light = 127;
                    }
                    texPos += dtexPos;
                    dest[offset] = hslToRgb[(hsl1 >> 8 & 0xff80) | light];
                    if (saveDepth) {
                        depthBuffer[offset] = z1;
                    }
                    z1 += z2;
                    offset++;
                    rgb = texels[texelPos((texPos & 0x3f80) + (texPos >>> 25))];
                    light = ((hsl1 >> 8 & 0x7f) << 1) * (((rgb >> 16 & 0xff) + (rgb >> 8 & 0xff) + (rgb & 0xff)) / 3)
                            / 384;
                    if (light > 127) {
                        light = 127;
                    }
                    texPos += dtexPos;
                    dest[offset] = hslToRgb[(hsl1 >> 8 & 0xff80) | light];
                    if (saveDepth) {
                        depthBuffer[offset] = z1;
                    }
                    z1 += z2;
                    offset++;
                    hsl1 += dhsl;
                    rgb = texels[texelPos((texPos & 0x3f80) + (texPos >>> 25))];
                    light = ((hsl1 >> 8 & 0x7f) << 1) * (((rgb >> 16 & 0xff) + (rgb >> 8 & 0xff) + (rgb & 0xff)) / 3)
                            / 384;
                    if (light > 127) {
                        light = 127;
                    }
                    texPos += dtexPos;
                    dest[offset] = hslToRgb[(hsl1 >> 8 & 0xff80) | light];
                    if (saveDepth) {
                        depthBuffer[offset] = z1;
                    }
                    z1 += z2;
                    offset++;
                    rgb = texels[texelPos((texPos & 0x3f80) + (texPos >>> 25))];
                    light = ((hsl1 >> 8 & 0x7f) << 1) * (((rgb >> 16 & 0xff) + (rgb >> 8 & 0xff) + (rgb & 0xff)) / 3)
                            / 384;
                    if (light > 127) {
                        light = 127;
                    }
                    texPos += dtexPos;
                    dest[offset] = hslToRgb[(hsl1 >> 8 & 0xff80) | light];
                    if (saveDepth) {
                        depthBuffer[offset] = z1;
                    }
                    z1 += z2;
                    offset++;
                    rgb = texels[texelPos((texPos & 0x3f80) + (texPos >>> 25))];
                    light = ((hsl1 >> 8 & 0x7f) << 1) * (((rgb >> 16 & 0xff) + (rgb >> 8 & 0xff) + (rgb & 0xff)) / 3)
                            / 384;
                    if (light > 127) {
                        light = 127;
                    }
                    texPos += dtexPos;
                    dest[offset] = hslToRgb[(hsl1 >> 8 & 0xff80) | light];
                    if (saveDepth) {
                        depthBuffer[offset] = z1;
                    }
                    z1 += z2;
                    offset++;
                    rgb = texels[texelPos((texPos & 0x3f80) + (texPos >>> 25))];
                    light = ((hsl1 >> 8 & 0x7f) << 1) * (((rgb >> 16 & 0xff) + (rgb >> 8 & 0xff) + (rgb & 0xff)) / 3)
                            / 384;
                    if (light > 127) {
                        light = 127;
                    }
                    texPos += dtexPos;
                    dest[offset] = hslToRgb[(hsl1 >> 8 & 0xff80) | light];
                    if (saveDepth) {
                        depthBuffer[offset] = z1;
                    }
                    z1 += z2;
                    offset++;
                    i_58_ = i_60_;
                    i_59_ = i_61_;
                    t1 += t4;
                    t2 += t5;
                    t3 += t6;
                    i_57_ = t3 >> 14;
                    if (i_57_ != 0) {
                        i_60_ = t1 / i_57_;
                        i_61_ = t2 / i_57_;
                    } else {
                        i_60_ = 0;
                        i_61_ = 0;
                    }
                    texPos = (i_58_ << 18) + i_59_;
                    dtexPos = (i_60_ - i_58_ >> 3 << 18) + (i_61_ - i_59_ >> 3);
                } while (--n > 0);
            }
            n = x2 - x1 & 7;
            if (n > 0) {
                do {
                    if ((n & 3) == 0) {
                        hsl1 += dhsl;
                    }
                    rgb = texels[texelPos((texPos & 0x3f80) + (texPos >>> 25))];
                    light = ((hsl1 >> 8 & 0x7f) << 1) * (((rgb >> 16 & 0xff) + (rgb >> 8 & 0xff) + (rgb & 0xff)) / 3)
                            / 384;
                    if (light > 127) {
                        light = 127;
                    }
                    texPos += dtexPos;
                    dest[offset] = hslToRgb[(hsl1 >> 8 & 0xff80) | light];
                    if (saveDepth) {
                        depthBuffer[offset] = z1;
                    }
                    z1 += z2;
                    offset++;
                } while (--n > 0);
            }
        }
        texels = dest = null;
    }

    public static void drawGouraudTriangle(int y1, int y2, int y3, int x1, int x2, int x3, int hsl1, int hsl2, int hsl3,
    		float z1, float z2, float z3) {
        if (Configuration.enableSmoothShading && aBoolean1464) {
            drawHDGouraudTriangle(y1, y2, y3, x1, x2, x3, hsl1, hsl2, hsl3, z1, z2, z3);
        } else {
            drawLDGouraudTriangle(y1, y2, y3, x1, x2, x3, hsl1, hsl2, hsl3, z1, z2, z3);
        }
    }

    public static void drawLDGouraudTriangle(int y1, int y2, int y3, int x1, int x2, int x3, int hsl1, int hsl2,
                                             int hsl3, float z1, float z2, float z3) {
        if (!saveDepth) {
            z1 = z2 = z3 = 0;
        }
        int dx1 = 0;
        int dhsl1 = 0;
        if (y2 != y1) {
            dx1 = (x2 - x1 << 16) / (y2 - y1);
            dhsl1 = (hsl2 - hsl1 << 15) / (y2 - y1);
        }
        int dx2 = 0;
        int dhsl2 = 0;
        if (y3 != y2) {
            dx2 = (x3 - x2 << 16) / (y3 - y2);
            dhsl2 = (hsl3 - hsl2 << 15) / (y3 - y2);
        }
        int dx3 = 0;
        int dhsl3 = 0;
        if (y3 != y1) {
            dx3 = (x1 - x3 << 16) / (y1 - y3);
            dhsl3 = (hsl1 - hsl3 << 15) / (y1 - y3);
        }

        float x21 = x2 - x1;
        float y32 = y2 - y1;
        float x31 = x3 - x1;
        float y31 = y3 - y1;
        float z21 = z2 - z1;
        float z31 = z3 - z1;

        float div = x21 * y31 - x31 * y32;
        float depthSlope = (z21 * y31 - z31 * y32) / div;
        float depthScale = (z31 * x21 - z21 * x31) / div;

        if (y1 <= y2 && y1 <= y3) {
            if (y1 >= Rasterizer2D.clip_bottom) {
                return;
            }
            if (y2 > Rasterizer2D.clip_bottom) {
                y2 = Rasterizer2D.clip_bottom;
            }
            if (y3 > Rasterizer2D.clip_bottom) {
                y3 = Rasterizer2D.clip_bottom;
            }
            z1 = z1 - depthSlope * x1 + depthSlope;
            if (y2 < y3) {
                x3 = x1 <<= 16;
                hsl3 = hsl1 <<= 15;
                if (y1 < 0) {
                    y1 -= 0;
                    x3 -= dx3 * y1;
                    x1 -= dx1 * y1;
                    z1 -= depthScale * y1;
                    hsl3 -= dhsl3 * y1;
                    hsl1 -= dhsl1 * y1;
                    y1 = 0;
                }
                x2 <<= 16;
                hsl2 <<= 15;
                if (y2 < 0) {
                    y2 -= 0;
                    x2 -= dx2 * y2;
                    hsl2 -= dhsl2 * y2;
                    y2 = 0;
                }
                if (y1 != y2 && dx3 < dx1 || y1 == y2 && dx3 > dx2) {
                    y3 -= y2;
                    y2 -= y1;
                    for (y1 = scanOffsets[y1]; --y2 >= 0; y1 += Rasterizer2D.width) {
                        drawLDGouraudScanline(Rasterizer2D.pixels, y1, x3 >> 16, x1 >> 16, hsl3 >> 7, hsl1 >> 7, z1,
                                depthSlope);
                        z1 += depthScale;
                        x3 += dx3;
                        x1 += dx1;
                        hsl3 += dhsl3;
                        hsl1 += dhsl1;
                    }
                    while (--y3 >= 0) {
                        drawLDGouraudScanline(Rasterizer2D.pixels, y1, x3 >> 16, x2 >> 16, hsl3 >> 7, hsl2 >> 7, z1,
                                depthSlope);
                        z1 += depthScale;
                        x3 += dx3;
                        x2 += dx2;
                        hsl3 += dhsl3;
                        hsl2 += dhsl2;
                        y1 += Rasterizer2D.width;
                    }
                    return;
                }
                y3 -= y2;
                y2 -= y1;
                for (y1 = scanOffsets[y1]; --y2 >= 0; y1 += Rasterizer2D.width) {
                    drawLDGouraudScanline(Rasterizer2D.pixels, y1, x1 >> 16, x3 >> 16, hsl1 >> 7, hsl3 >> 7, z1,
                            depthSlope);
                    z1 += depthScale;
                    x3 += dx3;
                    x1 += dx1;
                    hsl3 += dhsl3;
                    hsl1 += dhsl1;
                }
                while (--y3 >= 0) {
                    drawLDGouraudScanline(Rasterizer2D.pixels, y1, x2 >> 16, x3 >> 16, hsl2 >> 7, hsl3 >> 7, z1,
                            depthSlope);
                    z1 += depthScale;
                    x3 += dx3;
                    x2 += dx2;
                    hsl3 += dhsl3;
                    hsl2 += dhsl2;
                    y1 += Rasterizer2D.width;
                }
                return;
            }
            x2 = x1 <<= 16;
            hsl2 = hsl1 <<= 15;
            if (y1 < 0) {
                y1 -= 0;
                x2 -= dx3 * y1;
                x1 -= dx1 * y1;
                z1 -= depthScale * y1;
                hsl2 -= dhsl3 * y1;
                hsl1 -= dhsl1 * y1;
                y1 = 0;
            }
            x3 <<= 16;
            hsl3 <<= 15;
            if (y3 < 0) {
                y3 -= 0;
                x3 -= dx2 * y3;
                hsl3 -= dhsl2 * y3;
                y3 = 0;
            }
            if (y1 != y3 && dx3 < dx1 || y1 == y3 && dx2 > dx1) {
                y2 -= y3;
                y3 -= y1;
                for (y1 = scanOffsets[y1]; --y3 >= 0; y1 += Rasterizer2D.width) {
                    drawLDGouraudScanline(Rasterizer2D.pixels, y1, x2 >> 16, x1 >> 16, hsl2 >> 7, hsl1 >> 7, z1,
                            depthSlope);
                    z1 += depthScale;
                    x2 += dx3;
                    x1 += dx1;
                    hsl2 += dhsl3;
                    hsl1 += dhsl1;
                }
                while (--y2 >= 0) {
                    drawLDGouraudScanline(Rasterizer2D.pixels, y1, x3 >> 16, x1 >> 16, hsl3 >> 7, hsl1 >> 7, z1,
                            depthSlope);
                    z1 += depthScale;
                    x3 += dx2;
                    x1 += dx1;
                    hsl3 += dhsl2;
                    hsl1 += dhsl1;
                    y1 += Rasterizer2D.width;
                }
                return;
            }
            y2 -= y3;
            y3 -= y1;
            for (y1 = scanOffsets[y1]; --y3 >= 0; y1 += Rasterizer2D.width) {
                drawLDGouraudScanline(Rasterizer2D.pixels, y1, x1 >> 16, x2 >> 16, hsl1 >> 7, hsl2 >> 7, z1, depthSlope);
                z1 += depthScale;
                x2 += dx3;
                x1 += dx1;
                hsl2 += dhsl3;
                hsl1 += dhsl1;
            }
            while (--y2 >= 0) {
                drawLDGouraudScanline(Rasterizer2D.pixels, y1, x1 >> 16, x3 >> 16, hsl1 >> 7, hsl3 >> 7, z1, depthSlope);
                z1 += depthScale;
                x3 += dx2;
                x1 += dx1;
                hsl3 += dhsl2;
                hsl1 += dhsl1;
                y1 += Rasterizer2D.width;
            }
            return;
        }
        if (y2 <= y3) {
            if (y2 >= Rasterizer2D.clip_bottom) {
                return;
            }
            if (y3 > Rasterizer2D.clip_bottom) {
                y3 = Rasterizer2D.clip_bottom;
            }
            if (y1 > Rasterizer2D.clip_bottom) {
                y1 = Rasterizer2D.clip_bottom;
            }
            z2 = z2 - depthSlope * x2 + depthSlope;
            if (y3 < y1) {
                x1 = x2 <<= 16;
                hsl1 = hsl2 <<= 15;
                if (y2 < 0) {
                    y2 -= 0;
                    x1 -= dx1 * y2;
                    x2 -= dx2 * y2;
                    z1 -= depthScale * y2;
                    hsl1 -= dhsl1 * y2;
                    hsl2 -= dhsl2 * y2;
                    y2 = 0;
                }
                x3 <<= 16;
                hsl3 <<= 15;
                if (y3 < 0) {
                    y3 -= 0;
                    x3 -= dx3 * y3;
                    hsl3 -= dhsl3 * y3;
                    y3 = 0;
                }
                if (y2 != y3 && dx1 < dx2 || y2 == y3 && dx1 > dx3) {
                    y1 -= y3;
                    y3 -= y2;
                    for (y2 = scanOffsets[y2]; --y3 >= 0; y2 += Rasterizer2D.width) {
                        drawLDGouraudScanline(Rasterizer2D.pixels, y2, x1 >> 16, x2 >> 16, hsl1 >> 7, hsl2 >> 7, z2,
                                depthSlope);
                        z2 += depthScale;
                        x1 += dx1;
                        x2 += dx2;
                        hsl1 += dhsl1;
                        hsl2 += dhsl2;
                    }

                    while (--y1 >= 0) {
                        drawLDGouraudScanline(Rasterizer2D.pixels, y2, x1 >> 16, x3 >> 16, hsl1 >> 7, hsl3 >> 7, z2,
                                depthSlope);
                        z2 += depthScale;
                        x1 += dx1;
                        x3 += dx3;
                        hsl1 += dhsl1;
                        hsl3 += dhsl3;
                        y2 += Rasterizer2D.width;
                    }
                    return;
                }
                y1 -= y3;
                y3 -= y2;
                for (y2 = scanOffsets[y2]; --y3 >= 0; y2 += Rasterizer2D.width) {
                    drawLDGouraudScanline(Rasterizer2D.pixels, y2, x2 >> 16, x1 >> 16, hsl2 >> 7, hsl1 >> 7, z2,
                            depthSlope);
                    z2 += depthScale;
                    x1 += dx1;
                    x2 += dx2;
                    hsl1 += dhsl1;
                    hsl2 += dhsl2;
                }

                while (--y1 >= 0) {
                    drawLDGouraudScanline(Rasterizer2D.pixels, y2, x3 >> 16, x1 >> 16, hsl3 >> 7, hsl1 >> 7, z2,
                            depthSlope);
                    z2 += depthScale;
                    x1 += dx1;
                    x3 += dx3;
                    hsl1 += dhsl1;
                    hsl3 += dhsl3;
                    y2 += Rasterizer2D.width;
                }
                return;
            }
            x3 = x2 <<= 16;
            hsl3 = hsl2 <<= 15;
            if (y2 < 0) {
                y2 -= 0;
                x3 -= dx1 * y2;
                x2 -= dx2 * y2;
                z2 -= depthScale * y2;
                hsl3 -= dhsl1 * y2;
                hsl2 -= dhsl2 * y2;
                y2 = 0;
            }
            x1 <<= 16;
            hsl1 <<= 15;
            if (y1 < 0) {
                y1 -= 0;
                x1 -= dx3 * y1;
                hsl1 -= dhsl3 * y1;
                y1 = 0;
            }
            if (dx1 < dx2) {
                y3 -= y1;
                y1 -= y2;
                for (y2 = scanOffsets[y2]; --y1 >= 0; y2 += Rasterizer2D.width) {
                    drawLDGouraudScanline(Rasterizer2D.pixels, y2, x3 >> 16, x2 >> 16, hsl3 >> 7, hsl2 >> 7, z2,
                            depthSlope);
                    z2 += depthScale;
                    x3 += dx1;
                    x2 += dx2;
                    hsl3 += dhsl1;
                    hsl2 += dhsl2;
                }
                while (--y3 >= 0) {
                    drawLDGouraudScanline(Rasterizer2D.pixels, y2, x1 >> 16, x2 >> 16, hsl1 >> 7, hsl2 >> 7, z2,
                            depthSlope);
                    z2 += depthScale;
                    x1 += dx3;
                    x2 += dx2;
                    hsl1 += dhsl3;
                    hsl2 += dhsl2;
                    y2 += Rasterizer2D.width;
                }
                return;
            }
            y3 -= y1;
            y1 -= y2;
            for (y2 = scanOffsets[y2]; --y1 >= 0; y2 += Rasterizer2D.width) {
                drawLDGouraudScanline(Rasterizer2D.pixels, y2, x2 >> 16, x3 >> 16, hsl2 >> 7, hsl3 >> 7, z2, depthSlope);
                z2 += depthScale;
                x3 += dx1;
                x2 += dx2;
                hsl3 += dhsl1;
                hsl2 += dhsl2;
            }

            while (--y3 >= 0) {
                drawLDGouraudScanline(Rasterizer2D.pixels, y2, x2 >> 16, x1 >> 16, hsl2 >> 7, hsl1 >> 7, z2, depthSlope);
                z2 += depthScale;
                x1 += dx3;
                x2 += dx2;
                hsl1 += dhsl3;
                hsl2 += dhsl2;
                y2 += Rasterizer2D.width;
            }
            return;
        }
        if (y3 >= Rasterizer2D.clip_bottom) {
            return;
        }
        if (y1 > Rasterizer2D.clip_bottom) {
            y1 = Rasterizer2D.clip_bottom;
        }
        if (y2 > Rasterizer2D.clip_bottom) {
            y2 = Rasterizer2D.clip_bottom;
        }
        z3 = z3 - depthSlope * x3 + depthSlope;
        if (y1 < y2) {
            x2 = x3 <<= 16;
            hsl2 = hsl3 <<= 15;
            if (y3 < 0) {
                y3 -= 0;
                x2 -= dx2 * y3;
                x3 -= dx3 * y3;
                z3 -= depthScale * y3;
                hsl2 -= dhsl2 * y3;
                hsl3 -= dhsl3 * y3;
                y3 = 0;
            }
            x1 <<= 16;
            hsl1 <<= 15;
            if (y1 < 0) {
                y1 -= 0;
                x1 -= dx1 * y1;
                hsl1 -= dhsl1 * y1;
                y1 = 0;
            }
            if (dx2 < dx3) {
                y2 -= y1;
                y1 -= y3;
                for (y3 = scanOffsets[y3]; --y1 >= 0; y3 += Rasterizer2D.width) {
                    drawLDGouraudScanline(Rasterizer2D.pixels, y3, x2 >> 16, x3 >> 16, hsl2 >> 7, hsl3 >> 7, z3,
                            depthSlope);
                    z3 += depthScale;
                    x2 += dx2;
                    x3 += dx3;
                    hsl2 += dhsl2;
                    hsl3 += dhsl3;
                }
                while (--y2 >= 0) {
                    drawLDGouraudScanline(Rasterizer2D.pixels, y3, x2 >> 16, x1 >> 16, hsl2 >> 7, hsl1 >> 7, z3,
                            depthSlope);
                    z3 += depthScale;
                    x2 += dx2;
                    x1 += dx1;
                    hsl2 += dhsl2;
                    hsl1 += dhsl1;
                    y3 += Rasterizer2D.width;
                }
                return;
            }
            y2 -= y1;
            y1 -= y3;
            for (y3 = scanOffsets[y3]; --y1 >= 0; y3 += Rasterizer2D.width) {
                drawLDGouraudScanline(Rasterizer2D.pixels, y3, x3 >> 16, x2 >> 16, hsl3 >> 7, hsl2 >> 7, z3, depthSlope);
                z3 += depthScale;
                x2 += dx2;
                x3 += dx3;
                hsl2 += dhsl2;
                hsl3 += dhsl3;
            }

            while (--y2 >= 0) {
                drawLDGouraudScanline(Rasterizer2D.pixels, y3, x1 >> 16, x2 >> 16, hsl1 >> 7, hsl2 >> 7, z3, depthSlope);
                z3 += depthScale;
                x2 += dx2;
                x1 += dx1;
                hsl2 += dhsl2;
                hsl1 += dhsl1;
                y3 += Rasterizer2D.width;
            }
            return;
        }
        x1 = x3 <<= 16;
        hsl1 = hsl3 <<= 15;
        if (y3 < 0) {
            y3 -= 0;
            x1 -= dx2 * y3;
            x3 -= dx3 * y3;
            z3 -= depthScale * y3;
            hsl1 -= dhsl2 * y3;
            hsl3 -= dhsl3 * y3;
            y3 = 0;
        }
        x2 <<= 16;
        hsl2 <<= 15;
        if (y2 < 0) {
            y2 -= 0;
            x2 -= dx1 * y2;
            hsl2 -= dhsl1 * y2;
            y2 = 0;
        }
        if (dx2 < dx3) {
            y1 -= y2;
            y2 -= y3;
            for (y3 = scanOffsets[y3]; --y2 >= 0; y3 += Rasterizer2D.width) {
                drawLDGouraudScanline(Rasterizer2D.pixels, y3, x1 >> 16, x3 >> 16, hsl1 >> 7, hsl3 >> 7, z3, depthSlope);
                z3 += depthScale;
                x1 += dx2;
                x3 += dx3;
                hsl1 += dhsl2;
                hsl3 += dhsl3;
            }
            while (--y1 >= 0) {
                drawLDGouraudScanline(Rasterizer2D.pixels, y3, x2 >> 16, x3 >> 16, hsl2 >> 7, hsl3 >> 7, z3, depthSlope);
                z3 += depthScale;
                x2 += dx1;
                x3 += dx3;
                hsl2 += dhsl1;
                hsl3 += dhsl3;
                y3 += Rasterizer2D.width;
            }
            return;
        }
        y1 -= y2;
        y2 -= y3;
        for (y3 = scanOffsets[y3]; --y2 >= 0; y3 += Rasterizer2D.width) {
            drawLDGouraudScanline(Rasterizer2D.pixels, y3, x3 >> 16, x1 >> 16, hsl3 >> 7, hsl1 >> 7, z3, depthSlope);
            z3 += depthScale;
            x1 += dx2;
            x3 += dx3;
            hsl1 += dhsl2;
            hsl3 += dhsl3;
        }
        while (--y1 >= 0) {
            drawLDGouraudScanline(Rasterizer2D.pixels, y3, x3 >> 16, x2 >> 16, hsl3 >> 7, hsl2 >> 7, z3, depthSlope);
            z3 += depthScale;
            x2 += dx1;
            x3 += dx3;
            hsl2 += dhsl1;
            hsl3 += dhsl3;
            y3 += Rasterizer2D.width;
        }
    }

    private static void drawLDGouraudScanline(int dest[], int offset, int x1, int x2, int hsl1, int hsl2, float z1,
                                              float z2) {
        int rgb;
        int div;
        int dhsl;
        if (aBoolean1464) {
            if (textureOutOfDrawingBounds) {
                if (x2 - x1 > 3) {
                    dhsl = (hsl2 - hsl1) / (x2 - x1);
                } else {
                    dhsl = 0;
                }
                if (x2 > Rasterizer2D.lastX) {
                    x2 = Rasterizer2D.lastX;
                }
                if (x1 < 0) {
                    hsl1 -= x1 * dhsl;
                    x1 = 0;
                }
                if (x1 >= x2) {
                    return;
                }
                offset += x1 - 1;
                div = x2 - x1 >> 2;
                z1 += z2 * x1;
                dhsl <<= 2;
            } else {
                if (x1 >= x2) {
                    return;
                }
                offset += x1 - 1;
                div = x2 - x1 >> 2;
                z1 += z2 * x1;
                if (div > 0) {
                    dhsl = (hsl2 - hsl1) * anIntArray1468[div] >> 15;
                } else {
                    dhsl = 0;
                }
            }
            if (alpha == 0) {
                while (--div >= 0) {
                    rgb = hslToRgb[hsl1 >> 8];
                    hsl1 += dhsl;
                    offset++;
                    dest[offset] = rgb;
                    if (saveDepth) {
                        depthBuffer[offset] = z1;
                    }
                    z1 += z2;
                    offset++;
                    dest[offset] = rgb;
                    if (saveDepth) {
                        depthBuffer[offset] = z1;
                    }
                    z1 += z2;
                    offset++;
                    dest[offset] = rgb;
                    if (saveDepth) {
                        depthBuffer[offset] = z1;
                    }
                    z1 += z2;
                    offset++;
                    dest[offset] = rgb;
                    if (saveDepth) {
                        depthBuffer[offset] = z1;
                    }
                    z1 += z2;
                }
                div = x2 - x1 & 3;
                if (div > 0) {
                    rgb = hslToRgb[hsl1 >> 8];
                    do {
                        offset++;
                        dest[offset] = rgb;
                        if (saveDepth) {
                            depthBuffer[offset] = z1;
                        }
                        z1 += z2;
                    } while (--div > 0);
                    return;
                }
            } else {
                int a1 = alpha;
                int a2 = 256 - alpha;
                while (--div >= 0) {
                    rgb = hslToRgb[hsl1 >> 8];
                    hsl1 += dhsl;
                    rgb = ((rgb & 0xff00ff) * a2 >> 8 & 0xff00ff) + ((rgb & 0xff00) * a2 >> 8 & 0xff00);
                    dest[offset] = rgb + ((dest[offset] & 0xff00ff) * a1 >> 8 & 0xff00ff)
                            + ((dest[offset] & 0xff00) * a1 >> 8 & 0xff00);
                    if (saveDepth) {
                        depthBuffer[offset] = z1;
                    }
                    offset++;
                    z1 += z2;
                    dest[offset] = rgb + ((dest[offset] & 0xff00ff) * a1 >> 8 & 0xff00ff)
                            + ((dest[offset] & 0xff00) * a1 >> 8 & 0xff00);
                    if (saveDepth) {
                        depthBuffer[offset] = z1;
                    }
                    offset++;
                    z1 += z2;
                    dest[offset] = rgb + ((dest[offset] & 0xff00ff) * a1 >> 8 & 0xff00ff)
                            + ((dest[offset] & 0xff00) * a1 >> 8 & 0xff00);
                    if (saveDepth) {
                        depthBuffer[offset] = z1;
                    }
                    offset++;
                    z1 += z2;
                    dest[offset] = rgb + ((dest[offset] & 0xff00ff) * a1 >> 8 & 0xff00ff)
                            + ((dest[offset] & 0xff00) * a1 >> 8 & 0xff00);
                    if (saveDepth) {
                        depthBuffer[offset] = z1;
                    }
                    offset++;
                    z1 += z2;
                }
                div = x2 - x1 & 3;
                if (div > 0) {
                    rgb = hslToRgb[hsl1 >> 8];
                    rgb = ((rgb & 0xff00ff) * a2 >> 8 & 0xff00ff) + ((rgb & 0xff00) * a2 >> 8 & 0xff00);
                    do {
                        dest[offset] = rgb + ((dest[offset] & 0xff00ff) * a1 >> 8 & 0xff00ff)
                                + ((dest[offset] & 0xff00) * a1 >> 8 & 0xff00);
                        if (saveDepth) {
                            depthBuffer[offset] = z1;
                        }
                        offset++;
                        z1 += z2;
                    } while (--div > 0);
                }
            }
            return;
        }
        if (x1 >= x2) {
            return;
        }
        int dhsl2 = (hsl2 - hsl1) / (x2 - x1);
        if (textureOutOfDrawingBounds) {
            if (x2 > Rasterizer2D.lastX) {
                x2 = Rasterizer2D.lastX;
            }
            if (x1 < 0) {
                hsl1 -= x1 * dhsl2;
                x1 = 0;
            }
            if (x1 >= x2) {
                return;
            }
        }
        offset += x1;
        div = x2 - x1;
        if (alpha == 0) {
            do {
                dest[offset] = hslToRgb[hsl1 >> 8];
                if (saveDepth) {
                    depthBuffer[offset] = z1;
                }
                offset++;
                z1 += z2;
                hsl1 += dhsl2;
            } while (--div > 0);
            return;
        }
        int a1 = alpha;
        int a2 = 256 - alpha;
        do {
            rgb = hslToRgb[hsl1 >> 8];
            hsl1 += dhsl2;
            rgb = ((rgb & 0xff00ff) * a2 >> 8 & 0xff00ff) + ((rgb & 0xff00) * a2 >> 8 & 0xff00);
            dest[offset] = rgb + ((dest[offset] & 0xff00ff) * a1 >> 8 & 0xff00ff)
                    + ((dest[offset] & 0xff00) * a1 >> 8 & 0xff00);
            if (saveDepth) {
                depthBuffer[offset] = z1;
            }
            offset++;
            z1 += z2;
        } while (--div > 0);
    }
    public static void drawShadedTriangle(int y_a, int y_b, int y_c, int x_a, int x_b, int x_c,
                                          int hsl1, int hsl2, int hsl3, float z_a, float z_b, float z_c) {
        if (z_a < 0 || z_b < 0 || z_c < 0) {
            return;
        }
        int rgb1 = hslToRgb[hsl1];
        int rgb2 = hslToRgb[hsl2];
        int rgb3 = hslToRgb[hsl3];
        int r1 = rgb1 >> 16 & 0xff;
        int g1 = rgb1 >> 8 & 0xff;
        int b1 = rgb1 & 0xff;
        int r2 = rgb2 >> 16 & 0xff;
        int g2 = rgb2 >> 8 & 0xff;
        int b2 = rgb2 & 0xff;
        int r3 = rgb3 >> 16 & 0xff;
        int g3 = rgb3 >> 8 & 0xff;
        int b3 = rgb3 & 0xff;
        int a_to_b = 0;
        int dr1 = 0;
        int dg1 = 0;
        int db1 = 0;
        if (y_b != y_a) {
            a_to_b = (x_b - x_a << 16) / (y_b - y_a);
            dr1 = (r2 - r1 << 16) / (y_b - y_a);
            dg1 = (g2 - g1 << 16) / (y_b - y_a);
            db1 = (b2 - b1 << 16) / (y_b - y_a);
        }
        int b_to_c = 0;
        int dr2 = 0;
        int dg2 = 0;
        int db2 = 0;
        if (y_c != y_b) {
            b_to_c = (x_c - x_b << 16) / (y_c - y_b);
            dr2 = (r3 - r2 << 16) / (y_c - y_b);
            dg2 = (g3 - g2 << 16) / (y_c - y_b);
            db2 = (b3 - b2 << 16) / (y_c - y_b);
        }
        int c_to_a = 0;
        int dr3 = 0;
        int dg3 = 0;
        int db3 = 0;
        if (y_c != y_a) {
            c_to_a = (x_a - x_c << 16) / (y_a - y_c);
            dr3 = (r1 - r3 << 16) / (y_a - y_c);
            dg3 = (g1 - g3 << 16) / (y_a - y_c);
            db3 = (b1 - b3 << 16) / (y_a - y_c);
        }
        float b_aX = x_b - x_a;
        float b_aY = y_b - y_a;
        float c_aX = x_c - x_a;
        float c_aY = y_c - y_a;
        float b_aZ = z_b - z_a;
        float c_aZ = z_c - z_a;

        float div = b_aX * c_aY - c_aX * b_aY;
        float depth_slope = (b_aZ * c_aY - c_aZ * b_aY) / div;
        float depth_increment = (c_aZ * b_aX - b_aZ * c_aX) / div;
        if (y_a <= y_b && y_a <= y_c) {
            if (y_a >= Rasterizer2D.clip_bottom) {
                return;
            }
            if (y_b > Rasterizer2D.clip_bottom) {
                y_b = Rasterizer2D.clip_bottom;
            }
            if (y_c > Rasterizer2D.clip_bottom) {
                y_c = Rasterizer2D.clip_bottom;
            }
            z_a = z_a - depth_slope * x_a + depth_slope;
            if (y_b < y_c) {
                x_c = x_a <<= 16;
                r3 = r1 <<= 16;
                g3 = g1 <<= 16;
                b3 = b1 <<= 16;
                if (y_a < 0) {
                    x_c -= c_to_a * y_a;
                    x_a -= a_to_b * y_a;
                    r3 -= dr3 * y_a;
                    g3 -= dg3 * y_a;
                    b3 -= db3 * y_a;
                    r1 -= dr1 * y_a;
                    g1 -= dg1 * y_a;
                    b1 -= db1 * y_a;
                    z_a -= depth_increment * y_a;
                    y_a = 0;
                }
                x_b <<= 16;
                r2 <<= 16;
                g2 <<= 16;
                b2 <<= 16;
                if (y_b < 0) {
                    x_b -= b_to_c * y_b;
                    r2 -= dr2 * y_b;
                    g2 -= dg2 * y_b;
                    b2 -= db2 * y_b;
                    y_b = 0;
                }
                if (y_a != y_b && c_to_a < a_to_b || y_a == y_b && c_to_a > b_to_c) {
                    y_c -= y_b;
                    y_b -= y_a;
                    for (y_a = scanOffsets[y_a]; --y_b >= 0; y_a += Rasterizer2D.width) {
                        drawShadedScanline(Rasterizer2D.pixels, y_a, x_c >> 16, x_a >> 16, r3, g3, b3, r1, g1,
                                b1, z_a, depth_slope);
                        x_c += c_to_a;
                        x_a += a_to_b;
                        r3 += dr3;
                        g3 += dg3;
                        b3 += db3;
                        r1 += dr1;
                        g1 += dg1;
                        b1 += db1;
                        z_a += depth_increment;
                    }
                    while (--y_c >= 0) {
                        drawShadedScanline(Rasterizer2D.pixels, y_a, x_c >> 16, x_b >> 16, r3, g3, b3, r2, g2,
                                b2, z_a, depth_slope);
                        x_c += c_to_a;
                        x_b += b_to_c;
                        r3 += dr3;
                        g3 += dg3;
                        b3 += db3;
                        r2 += dr2;
                        g2 += dg2;
                        b2 += db2;
                        y_a += Rasterizer2D.width;
                        z_a += depth_increment;
                    }
                    return;
                }
                y_c -= y_b;
                y_b -= y_a;
                for (y_a = scanOffsets[y_a]; --y_b >= 0; y_a += Rasterizer2D.width) {
                    drawShadedScanline(Rasterizer2D.pixels, y_a, x_a >> 16, x_c >> 16, r1, g1, b1, r3, g3, b3,
                            z_a, depth_slope);
                    x_c += c_to_a;
                    x_a += a_to_b;
                    r3 += dr3;
                    g3 += dg3;
                    b3 += db3;
                    r1 += dr1;
                    g1 += dg1;
                    b1 += db1;
                    z_a += depth_increment;
                }
                while (--y_c >= 0) {
                    drawShadedScanline(Rasterizer2D.pixels, y_a, x_b >> 16, x_c >> 16, r2, g2, b2, r3, g3, b3,
                            z_a, depth_slope);
                    x_c += c_to_a;
                    x_b += b_to_c;
                    r3 += dr3;
                    g3 += dg3;
                    b3 += db3;
                    r2 += dr2;
                    g2 += dg2;
                    b2 += db2;
                    y_a += Rasterizer2D.width;
                    z_a += depth_increment;
                }
                return;
            }
            x_b = x_a <<= 16;
            r2 = r1 <<= 16;
            g2 = g1 <<= 16;
            b2 = b1 <<= 16;
            if (y_a < 0) {
                x_b -= c_to_a * y_a;
                x_a -= a_to_b * y_a;
                r2 -= dr3 * y_a;
                g2 -= dg3 * y_a;
                b2 -= db3 * y_a;
                r1 -= dr1 * y_a;
                g1 -= dg1 * y_a;
                b1 -= db1 * y_a;
                z_a -= depth_increment * y_a;
                y_a = 0;
            }
            x_c <<= 16;
            r3 <<= 16;
            g3 <<= 16;
            b3 <<= 16;
            if (y_c < 0) {
                x_c -= b_to_c * y_c;
                r3 -= dr2 * y_c;
                g3 -= dg2 * y_c;
                b3 -= db2 * y_c;
                y_c = 0;
            }
            if (y_a != y_c && c_to_a < a_to_b || y_a == y_c && b_to_c > a_to_b) {
                y_b -= y_c;
                y_c -= y_a;
                for (y_a = scanOffsets[y_a]; --y_c >= 0; y_a += Rasterizer2D.width) {
                    drawShadedScanline(Rasterizer2D.pixels, y_a, x_b >> 16, x_a >> 16, r2, g2, b2, r1, g1, b1,
                            z_a, depth_slope);
                    x_b += c_to_a;
                    x_a += a_to_b;
                    r2 += dr3;
                    g2 += dg3;
                    b2 += db3;
                    r1 += dr1;
                    g1 += dg1;
                    b1 += db1;
                    z_a += depth_increment;
                }
                while (--y_b >= 0) {
                    drawShadedScanline(Rasterizer2D.pixels, y_a, x_c >> 16, x_a >> 16, r3, g3, b3, r1, g1, b1,
                            z_a, depth_slope);
                    x_c += b_to_c;
                    x_a += a_to_b;
                    r3 += dr2;
                    g3 += dg2;
                    b3 += db2;
                    r1 += dr1;
                    g1 += dg1;
                    b1 += db1;
                    y_a += Rasterizer2D.width;
                    z_a += depth_increment;
                }
                return;
            }
            y_b -= y_c;
            y_c -= y_a;
            for (y_a = scanOffsets[y_a]; --y_c >= 0; y_a += Rasterizer2D.width) {
                drawShadedScanline(Rasterizer2D.pixels, y_a, x_a >> 16, x_b >> 16, r1, g1, b1, r2, g2, b2,
                        z_a, depth_slope);
                x_b += c_to_a;
                x_a += a_to_b;
                r2 += dr3;
                g2 += dg3;
                b2 += db3;
                r1 += dr1;
                g1 += dg1;
                b1 += db1;
                z_a += depth_increment;
            }
            while (--y_b >= 0) {
                drawShadedScanline(Rasterizer2D.pixels, y_a, x_a >> 16, x_c >> 16, r1, g1, b1, r3, g3, b3,
                        z_a, depth_slope);
                x_c += b_to_c;
                x_a += a_to_b;
                r3 += dr2;
                g3 += dg2;
                b3 += db2;
                r1 += dr1;
                g1 += dg1;
                b1 += db1;
                y_a += Rasterizer2D.width;
                z_a += depth_increment;
            }
            return;
        }
        if (y_b <= y_c) {
            if (y_b >= Rasterizer2D.clip_bottom) {
                return;
            }
            if (y_c > Rasterizer2D.clip_bottom) {
                y_c = Rasterizer2D.clip_bottom;
            }
            if (y_a > Rasterizer2D.clip_bottom) {
                y_a = Rasterizer2D.clip_bottom;
            }
            z_b = z_b - depth_slope * x_b + depth_slope;
            if (y_c < y_a) {
                x_a = x_b <<= 16;
                r1 = r2 <<= 16;
                g1 = g2 <<= 16;
                b1 = b2 <<= 16;
                if (y_b < 0) {
                    x_a -= a_to_b * y_b;
                    x_b -= b_to_c * y_b;
                    r1 -= dr1 * y_b;
                    g1 -= dg1 * y_b;
                    b1 -= db1 * y_b;
                    r2 -= dr2 * y_b;
                    g2 -= dg2 * y_b;
                    b2 -= db2 * y_b;
                    z_b -= depth_increment * y_b;
                    y_b = 0;
                }
                x_c <<= 16;
                r3 <<= 16;
                g3 <<= 16;
                b3 <<= 16;
                if (y_c < 0) {
                    x_c -= c_to_a * y_c;
                    r3 -= dr3 * y_c;
                    g3 -= dg3 * y_c;
                    b3 -= db3 * y_c;
                    y_c = 0;
                }
                if (y_b != y_c && a_to_b < b_to_c || y_b == y_c && a_to_b > c_to_a) {
                    y_a -= y_c;
                    y_c -= y_b;
                    for (y_b = scanOffsets[y_b]; --y_c >= 0; y_b += Rasterizer2D.width) {
                        drawShadedScanline(Rasterizer2D.pixels, y_b, x_a >> 16, x_b >> 16, r1, g1, b1, r2, g2,
                                b2, z_b, depth_slope);
                        x_a += a_to_b;
                        x_b += b_to_c;
                        r1 += dr1;
                        g1 += dg1;
                        b1 += db1;
                        r2 += dr2;
                        g2 += dg2;
                        b2 += db2;
                        z_b += depth_increment;
                    }
                    while (--y_a >= 0) {
                        drawShadedScanline(Rasterizer2D.pixels, y_b, x_a >> 16, x_c >> 16, r1, g1, b1, r3, g3,
                                b3, z_b, depth_slope);
                        x_a += a_to_b;
                        x_c += c_to_a;
                        r1 += dr1;
                        g1 += dg1;
                        b1 += db1;
                        r3 += dr3;
                        g3 += dg3;
                        b3 += db3;
                        y_b += Rasterizer2D.width;
                        z_b += depth_increment;
                    }
                    return;
                }
                y_a -= y_c;
                y_c -= y_b;
                for (y_b = scanOffsets[y_b]; --y_c >= 0; y_b += Rasterizer2D.width) {
                    drawShadedScanline(Rasterizer2D.pixels, y_b, x_b >> 16, x_a >> 16, r2, g2, b2, r1, g1, b1,
                            z_b, depth_slope);
                    x_a += a_to_b;
                    x_b += b_to_c;
                    r1 += dr1;
                    g1 += dg1;
                    b1 += db1;
                    r2 += dr2;
                    g2 += dg2;
                    b2 += db2;
                    z_b += depth_increment;
                }
                while (--y_a >= 0) {
                    drawShadedScanline(Rasterizer2D.pixels, y_b, x_c >> 16, x_a >> 16, r3, g3, b3, r1, g1, b1,
                            z_b, depth_slope);
                    x_a += a_to_b;
                    x_c += c_to_a;
                    r1 += dr1;
                    g1 += dg1;
                    b1 += db1;
                    r3 += dr3;
                    g3 += dg3;
                    b3 += db3;
                    y_b += Rasterizer2D.width;
                    z_b += depth_increment;
                }
                return;
            }
            x_c = x_b <<= 16;
            r3 = r2 <<= 16;
            g3 = g2 <<= 16;
            b3 = b2 <<= 16;
            if (y_b < 0) {
                x_c -= a_to_b * y_b;
                x_b -= b_to_c * y_b;
                r3 -= dr1 * y_b;
                g3 -= dg1 * y_b;
                b3 -= db1 * y_b;
                r2 -= dr2 * y_b;
                g2 -= dg2 * y_b;
                b2 -= db2 * y_b;
                z_b -= depth_increment * y_b;
                y_b = 0;
            }
            x_a <<= 16;
            r1 <<= 16;
            g1 <<= 16;
            b1 <<= 16;
            if (y_a < 0) {
                x_a -= c_to_a * y_a;
                r1 -= dr3 * y_a;
                g1 -= dg3 * y_a;
                b1 -= db3 * y_a;
                y_a = 0;
            }
            if (a_to_b < b_to_c) {
                y_c -= y_a;
                y_a -= y_b;
                for (y_b = scanOffsets[y_b]; --y_a >= 0; y_b += Rasterizer2D.width) {
                    drawShadedScanline(Rasterizer2D.pixels, y_b, x_c >> 16, x_b >> 16, r3, g3, b3, r2, g2, b2,
                            z_b, depth_slope);
                    x_c += a_to_b;
                    x_b += b_to_c;
                    r3 += dr1;
                    g3 += dg1;
                    b3 += db1;
                    r2 += dr2;
                    g2 += dg2;
                    b2 += db2;
                    z_b += depth_increment;
                }
                while (--y_c >= 0) {
                    drawShadedScanline(Rasterizer2D.pixels, y_b, x_a >> 16, x_b >> 16, r1, g1, b1, r2, g2, b2,
                            z_b, depth_slope);
                    x_a += c_to_a;
                    x_b += b_to_c;
                    r1 += dr3;
                    g1 += dg3;
                    b1 += db3;
                    r2 += dr2;
                    g2 += dg2;
                    b2 += db2;
                    y_b += Rasterizer2D.width;
                    z_b += depth_increment;
                }
                return;
            }
            y_c -= y_a;
            y_a -= y_b;
            for (y_b = scanOffsets[y_b]; --y_a >= 0; y_b += Rasterizer2D.width) {
                drawShadedScanline(Rasterizer2D.pixels, y_b, x_b >> 16, x_c >> 16, r2, g2, b2, r3, g3, b3,
                        z_b, depth_slope);
                x_c += a_to_b;
                x_b += b_to_c;
                r3 += dr1;
                g3 += dg1;
                b3 += db1;
                r2 += dr2;
                g2 += dg2;
                b2 += db2;
                z_b += depth_increment;
            }
            while (--y_c >= 0) {
                drawShadedScanline(Rasterizer2D.pixels, y_b, x_b >> 16, x_a >> 16, r2, g2, b2, r1, g1, b1,
                        z_b, depth_slope);
                x_a += c_to_a;
                x_b += b_to_c;
                r1 += dr3;
                g1 += dg3;
                b1 += db3;
                r2 += dr2;
                g2 += dg2;
                b2 += db2;
                y_b += Rasterizer2D.width;
                z_b += depth_increment;
            }
            return;
        }
        if (y_c >= Rasterizer2D.clip_bottom) {
            return;
        }
        if (y_a > Rasterizer2D.clip_bottom) {
            y_a = Rasterizer2D.clip_bottom;
        }
        if (y_b > Rasterizer2D.clip_bottom) {
            y_b = Rasterizer2D.clip_bottom;
        }
        z_c = z_c - depth_slope * x_c + depth_slope;
        if (y_a < y_b) {
            x_b = x_c <<= 16;
            r2 = r3 <<= 16;
            g2 = g3 <<= 16;
            b2 = b3 <<= 16;
            if (y_c < 0) {
                x_b -= b_to_c * y_c;
                x_c -= c_to_a * y_c;
                r2 -= dr2 * y_c;
                g2 -= dg2 * y_c;
                b2 -= db2 * y_c;
                r3 -= dr3 * y_c;
                g3 -= dg3 * y_c;
                b3 -= db3 * y_c;
                z_c -= depth_increment * y_c;
                y_c = 0;
            }
            x_a <<= 16;
            r1 <<= 16;
            g1 <<= 16;
            b1 <<= 16;
            if (y_a < 0) {
                x_a -= a_to_b * y_a;
                r1 -= dr1 * y_a;
                g1 -= dg1 * y_a;
                b1 -= db1 * y_a;
                y_a = 0;
            }
            if (b_to_c < c_to_a) {
                y_b -= y_a;
                y_a -= y_c;
                for (y_c = scanOffsets[y_c]; --y_a >= 0; y_c += Rasterizer2D.width) {
                    drawShadedScanline(Rasterizer2D.pixels, y_c, x_b >> 16, x_c >> 16, r2, g2, b2, r3, g3, b3,
                            z_c, depth_slope);
                    x_b += b_to_c;
                    x_c += c_to_a;
                    r2 += dr2;
                    g2 += dg2;
                    b2 += db2;
                    r3 += dr3;
                    g3 += dg3;
                    b3 += db3;
                    z_c += depth_increment;
                }
                while (--y_b >= 0) {
                    drawShadedScanline(Rasterizer2D.pixels, y_c, x_b >> 16, x_a >> 16, r2, g2, b2, r1, g1, b1,
                            z_c, depth_slope);
                    x_b += b_to_c;
                    x_a += a_to_b;
                    r2 += dr2;
                    g2 += dg2;
                    b2 += db2;
                    r1 += dr1;
                    g1 += dg1;
                    b1 += db1;
                    y_c += Rasterizer2D.width;
                    z_c += depth_increment;
                }
                return;
            }
            y_b -= y_a;
            y_a -= y_c;
            for (y_c = scanOffsets[y_c]; --y_a >= 0; y_c += Rasterizer2D.width) {
                drawShadedScanline(Rasterizer2D.pixels, y_c, x_c >> 16, x_b >> 16, r3, g3, b3, r2, g2, b2,
                        z_c, depth_slope);
                x_b += b_to_c;
                x_c += c_to_a;
                r2 += dr2;
                g2 += dg2;
                b2 += db2;
                r3 += dr3;
                g3 += dg3;
                b3 += db3;
                z_c += depth_increment;
            }
            while (--y_b >= 0) {
                drawShadedScanline(Rasterizer2D.pixels, y_c, x_a >> 16, x_b >> 16, r1, g1, b1, r2, g2, b2,
                        z_c, depth_slope);
                x_b += b_to_c;
                x_a += a_to_b;
                r2 += dr2;
                g2 += dg2;
                b2 += db2;
                r1 += dr1;
                g1 += dg1;
                b1 += db1;
                z_c += depth_increment;
                y_c += Rasterizer2D.width;
            }
            return;
        }
        x_a = x_c <<= 16;
        r1 = r3 <<= 16;
        g1 = g3 <<= 16;
        b1 = b3 <<= 16;
        if (y_c < 0) {
            x_a -= b_to_c * y_c;
            x_c -= c_to_a * y_c;
            r1 -= dr2 * y_c;
            g1 -= dg2 * y_c;
            b1 -= db2 * y_c;
            r3 -= dr3 * y_c;
            g3 -= dg3 * y_c;
            b3 -= db3 * y_c;
            z_c -= depth_increment * y_c;
            y_c = 0;
        }
        x_b <<= 16;
        r2 <<= 16;
        g2 <<= 16;
        b2 <<= 16;
        if (y_b < 0) {
            x_b -= a_to_b * y_b;
            r2 -= dr1 * y_b;
            g2 -= dg1 * y_b;
            b2 -= db1 * y_b;
            y_b = 0;
        }
        if (b_to_c < c_to_a) {
            y_a -= y_b;
            y_b -= y_c;
            for (y_c = scanOffsets[y_c]; --y_b >= 0; y_c += Rasterizer2D.width) {
                drawShadedScanline(Rasterizer2D.pixels, y_c, x_a >> 16, x_c >> 16, r1, g1, b1, r3, g3, b3,
                        z_c, depth_slope);
                x_a += b_to_c;
                x_c += c_to_a;
                r1 += dr2;
                g1 += dg2;
                b1 += db2;
                r3 += dr3;
                g3 += dg3;
                b3 += db3;
                z_c += depth_increment;
            }
            while (--y_a >= 0) {
                drawShadedScanline(Rasterizer2D.pixels, y_c, x_b >> 16, x_c >> 16, r2, g2, b2, r3, g3, b3,
                        z_c, depth_slope);
                x_b += a_to_b;
                x_c += c_to_a;
                r2 += dr1;
                g2 += dg1;
                b2 += db1;
                r3 += dr3;
                g3 += dg3;
                b3 += db3;
                z_c += depth_increment;
                y_c += Rasterizer2D.width;
            }
            return;
        }
        y_a -= y_b;
        y_b -= y_c;
        for (y_c = scanOffsets[y_c]; --y_b >= 0; y_c += Rasterizer2D.width) {
            drawShadedScanline(Rasterizer2D.pixels, y_c, x_c >> 16, x_a >> 16, r3, g3, b3, r1, g1, b1,
                    z_c, depth_slope);
            x_a += b_to_c;
            x_c += c_to_a;
            r1 += dr2;
            g1 += dg2;
            b1 += db2;
            r3 += dr3;
            g3 += dg3;
            b3 += db3;
            z_c += depth_increment;
        }
        while (--y_a >= 0) {
            drawShadedScanline(Rasterizer2D.pixels, y_c, x_c >> 16, x_b >> 16, r3, g3, b3, r2, g2, b2,
                    z_c, depth_slope);
            x_b += a_to_b;
            x_c += c_to_a;
            r2 += dr1;
            g2 += dg1;
            b2 += db1;
            r3 += dr3;
            g3 += dg3;
            b3 += db3;
            y_c += Rasterizer2D.width;
            z_c += depth_increment;
        }
    }

    public static void drawShadedScanline(int[] dest, int offset, int x1, int x2, int r1, int g1,
                                          int b1, int r2, int g2, int b2, float depth, float depth_slope) {
        int n = x2 - x1;
        if (n <= 0) {
            return;
        }
        r2 = (r2 - r1) / n;
        g2 = (g2 - g1) / n;
        b2 = (b2 - b1) / n;
        if (textureOutOfDrawingBounds) {
            if (x2 > Rasterizer2D.lastX) {
                n -= x2 - Rasterizer2D.lastX;
                x2 = Rasterizer2D.lastX;
            }
            if (x1 < 0) {
                n = x2;
                r1 -= x1 * r2;
                g1 -= x1 * g2;
                b1 -= x1 * b2;
                x1 = 0;
            }
        }
        if (x1 < x2) {
            offset += x1;
            depth += depth_slope * x1;
            if (alpha == 0) {
                while (--n >= 0) {
                    if (true) {
                        drawAlpha(
                                dest,
                                offset,
                                (r1 & 0xff0000) | (g1 >> 8 & 0xff00) | (b1 >> 16 & 0xff),
                                255);
                        Rasterizer2D.depthBuffer[offset] = depth;
                    }
                    depth += depth_slope;
                    r1 += r2;
                    g1 += g2;
                    b1 += b2;
                    offset++;
                }
            } else {
                final int a1 = alpha;
                final int a2 = 256 - alpha;
                int rgb;
                int dst;
                while (--n >= 0) {
                    rgb = (r1 & 0xff0000) | (g1 >> 8 & 0xff00) | (b1 >> 16 & 0xff);
                    rgb = ((rgb & 0xff00ff) * a2 >> 8 & 0xff00ff) + ((rgb & 0xff00) * a2 >> 8 & 0xff00);
                    dst = dest[offset];
                    if (true) {
                        drawAlpha(dest, offset, rgb + ((dst & 0xff00ff) * a1 >> 8 & 0xff00ff)
                                + ((dst & 0xff00) * a1 >> 8 & 0xff00), 255);
                        Rasterizer2D.depthBuffer[offset] = depth;
                    }
                    depth += depth_slope;
                    r1 += r2;
                    g1 += g2;
                    b1 += b2;
                    offset++;
                }
            }
        }
    }


    public static void drawHDGouraudTriangle(int y1, int y2, int y3, int x1, int x2, int x3, int hsl1, int hsl2,
                                             int hsl3, float z1,float z2, float z3) {
        if (!saveDepth) {
            z1 = z2 = z3 = 0;
        }
        int rgb1 = hslToRgb[hsl1];
        int rgb2 = hslToRgb[hsl2];
        int rgb3 = hslToRgb[hsl3];
        int r1 = rgb1 >> 16 & 0xff;
        int g1 = rgb1 >> 8 & 0xff;
        int b1 = rgb1 & 0xff;
        int r2 = rgb2 >> 16 & 0xff;
        int g2 = rgb2 >> 8 & 0xff;
        int b2 = rgb2 & 0xff;
        int r3 = rgb3 >> 16 & 0xff;
        int g3 = rgb3 >> 8 & 0xff;
        int b3 = rgb3 & 0xff;
        int dx1 = 0;
        int dr1 = 0;
        int dg1 = 0;
        int db1 = 0;
        if (y2 != y1) {
            dx1 = (x2 - x1 << 16) / (y2 - y1);
            dr1 = (r2 - r1 << 16) / (y2 - y1);
            dg1 = (g2 - g1 << 16) / (y2 - y1);
            db1 = (b2 - b1 << 16) / (y2 - y1);
        }
        int dx2 = 0;
        int dr2 = 0;
        int dg2 = 0;
        int db2 = 0;
        if (y3 != y2) {
            dx2 = (x3 - x2 << 16) / (y3 - y2);
            dr2 = (r3 - r2 << 16) / (y3 - y2);
            dg2 = (g3 - g2 << 16) / (y3 - y2);
            db2 = (b3 - b2 << 16) / (y3 - y2);
        }
        int dx3 = 0;
        int dr3 = 0;
        int dg3 = 0;
        int db3 = 0;
        if (y3 != y1) {
            dx3 = (x1 - x3 << 16) / (y1 - y3);
            dr3 = (r1 - r3 << 16) / (y1 - y3);
            dg3 = (g1 - g3 << 16) / (y1 - y3);
            db3 = (b1 - b3 << 16) / (y1 - y3);
        }

        float x21 = x2 - x1;
        float y32 = y2 - y1;
        float x31 = x3 - x1;
        float y31 = y3 - y1;
        float z21 = z2 - z1;
        float z31 = z3 - z1;

        float div = x21 * y31 - x31 * y32;
        float depthSlope = (z21 * y31 - z31 * y32) / div;
        float depthScale = (z31 * x21 - z21 * x31) / div;

        if (y1 <= y2 && y1 <= y3) {
            if (y1 >= Rasterizer2D.clip_bottom) {
                return;
            }
            if (y2 > Rasterizer2D.clip_bottom) {
                y2 = Rasterizer2D.clip_bottom;
            }
            if (y3 > Rasterizer2D.clip_bottom) {
                y3 = Rasterizer2D.clip_bottom;
            }
            z1 = z1 - depthSlope * x1 + depthSlope;
            if (y2 < y3) {
                x3 = x1 <<= 16;
                r3 = r1 <<= 16;
                g3 = g1 <<= 16;
                b3 = b1 <<= 16;
                if (y1 < 0) {
                    x3 -= dx3 * y1;
                    x1 -= dx1 * y1;
                    r3 -= dr3 * y1;
                    g3 -= dg3 * y1;
                    b3 -= db3 * y1;
                    r1 -= dr1 * y1;
                    g1 -= dg1 * y1;
                    b1 -= db1 * y1;
                    z1 -= depthScale * y1;
                    y1 = 0;
                }
                x2 <<= 16;
                r2 <<= 16;
                g2 <<= 16;
                b2 <<= 16;
                if (y2 < 0) {
                    x2 -= dx2 * y2;
                    r2 -= dr2 * y2;
                    g2 -= dg2 * y2;
                    b2 -= db2 * y2;
                    y2 = 0;
                }
                if (y1 != y2 && dx3 < dx1 || y1 == y2 && dx3 > dx2) {
                    y3 -= y2;
                    y2 -= y1;
                    for (y1 = scanOffsets[y1]; --y2 >= 0; y1 += Rasterizer2D.width) {
                        drawHDGouraudScanline(Rasterizer2D.pixels, y1, x3 >> 16, x1 >> 16, r3, g3, b3, r1, g1, b1, z1,
                                depthSlope);
                        x3 += dx3;
                        x1 += dx1;
                        r3 += dr3;
                        g3 += dg3;
                        b3 += db3;
                        r1 += dr1;
                        g1 += dg1;
                        b1 += db1;
                        z1 += depthScale;
                    }
                    while (--y3 >= 0) {
                        drawHDGouraudScanline(Rasterizer2D.pixels, y1, x3 >> 16, x2 >> 16, r3, g3, b3, r2, g2, b2, z1,
                                depthSlope);
                        x3 += dx3;
                        x2 += dx2;
                        r3 += dr3;
                        g3 += dg3;
                        b3 += db3;
                        r2 += dr2;
                        g2 += dg2;
                        b2 += db2;
                        y1 += Rasterizer2D.width;
                        z1 += depthScale;
                    }
                    return;
                }
                y3 -= y2;
                y2 -= y1;
                for (y1 = scanOffsets[y1]; --y2 >= 0; y1 += Rasterizer2D.width) {
                    drawHDGouraudScanline(Rasterizer2D.pixels, y1, x1 >> 16, x3 >> 16, r1, g1, b1, r3, g3, b3, z1,
                            depthSlope);
                    x3 += dx3;
                    x1 += dx1;
                    r3 += dr3;
                    g3 += dg3;
                    b3 += db3;
                    r1 += dr1;
                    g1 += dg1;
                    b1 += db1;
                    z1 += depthScale;
                }
                while (--y3 >= 0) {
                    drawHDGouraudScanline(Rasterizer2D.pixels, y1, x2 >> 16, x3 >> 16, r2, g2, b2, r3, g3, b3, z1,
                            depthSlope);
                    x3 += dx3;
                    x2 += dx2;
                    r3 += dr3;
                    g3 += dg3;
                    b3 += db3;
                    r2 += dr2;
                    g2 += dg2;
                    b2 += db2;
                    y1 += Rasterizer2D.width;
                    z1 += depthScale;
                }
                return;
            }
            x2 = x1 <<= 16;
            r2 = r1 <<= 16;
            g2 = g1 <<= 16;
            b2 = b1 <<= 16;
            if (y1 < 0) {
                x2 -= dx3 * y1;
                x1 -= dx1 * y1;
                r2 -= dr3 * y1;
                g2 -= dg3 * y1;
                b2 -= db3 * y1;
                r1 -= dr1 * y1;
                g1 -= dg1 * y1;
                b1 -= db1 * y1;
                z1 -= depthScale * y1;
                y1 = 0;
            }
            x3 <<= 16;
            r3 <<= 16;
            g3 <<= 16;
            b3 <<= 16;
            if (y3 < 0) {
                x3 -= dx2 * y3;
                r3 -= dr2 * y3;
                g3 -= dg2 * y3;
                b3 -= db2 * y3;
                y3 = 0;
            }
            if (y1 != y3 && dx3 < dx1 || y1 == y3 && dx2 > dx1) {
                y2 -= y3;
                y3 -= y1;
                for (y1 = scanOffsets[y1]; --y3 >= 0; y1 += Rasterizer2D.width) {
                    drawHDGouraudScanline(Rasterizer2D.pixels, y1, x2 >> 16, x1 >> 16, r2, g2, b2, r1, g1, b1, z1,
                            depthSlope);
                    x2 += dx3;
                    x1 += dx1;
                    r2 += dr3;
                    g2 += dg3;
                    b2 += db3;
                    r1 += dr1;
                    g1 += dg1;
                    b1 += db1;
                    z1 += depthScale;
                }
                while (--y2 >= 0) {
                    drawHDGouraudScanline(Rasterizer2D.pixels, y1, x3 >> 16, x1 >> 16, r3, g3, b3, r1, g1, b1, z1,
                            depthSlope);
                    x3 += dx2;
                    x1 += dx1;
                    r3 += dr2;
                    g3 += dg2;
                    b3 += db2;
                    r1 += dr1;
                    g1 += dg1;
                    b1 += db1;
                    y1 += Rasterizer2D.width;
                    z1 += depthScale;
                }
                return;
            }
            y2 -= y3;
            y3 -= y1;
            for (y1 = scanOffsets[y1]; --y3 >= 0; y1 += Rasterizer2D.width) {
                drawHDGouraudScanline(Rasterizer2D.pixels, y1, x1 >> 16, x2 >> 16, r1, g1, b1, r2, g2, b2, z1,
                        depthSlope);
                x2 += dx3;
                x1 += dx1;
                r2 += dr3;
                g2 += dg3;
                b2 += db3;
                r1 += dr1;
                g1 += dg1;
                b1 += db1;
                z1 += depthScale;
            }
            while (--y2 >= 0) {
                drawHDGouraudScanline(Rasterizer2D.pixels, y1, x1 >> 16, x3 >> 16, r1, g1, b1, r3, g3, b3, z1,
                        depthSlope);
                x3 += dx2;
                x1 += dx1;
                r3 += dr2;
                g3 += dg2;
                b3 += db2;
                r1 += dr1;
                g1 += dg1;
                b1 += db1;
                y1 += Rasterizer2D.width;
                z1 += depthScale;
            }
            return;
        }
        if (y2 <= y3) {
            if (y2 >= Rasterizer2D.clip_bottom) {
                return;
            }
            if (y3 > Rasterizer2D.clip_bottom) {
                y3 = Rasterizer2D.clip_bottom;
            }
            if (y1 > Rasterizer2D.clip_bottom) {
                y1 = Rasterizer2D.clip_bottom;
            }
            z2 = z2 - depthSlope * x2 + depthSlope;
            if (y3 < y1) {
                x1 = x2 <<= 16;
                r1 = r2 <<= 16;
                g1 = g2 <<= 16;
                b1 = b2 <<= 16;
                if (y2 < 0) {
                    x1 -= dx1 * y2;
                    x2 -= dx2 * y2;
                    r1 -= dr1 * y2;
                    g1 -= dg1 * y2;
                    b1 -= db1 * y2;
                    r2 -= dr2 * y2;
                    g2 -= dg2 * y2;
                    b2 -= db2 * y2;
                    z2 -= depthScale * y2;
                    y2 = 0;
                }
                x3 <<= 16;
                r3 <<= 16;
                g3 <<= 16;
                b3 <<= 16;
                if (y3 < 0) {
                    x3 -= dx3 * y3;
                    r3 -= dr3 * y3;
                    g3 -= dg3 * y3;
                    b3 -= db3 * y3;
                    y3 = 0;
                }
                if (y2 != y3 && dx1 < dx2 || y2 == y3 && dx1 > dx3) {
                    y1 -= y3;
                    y3 -= y2;
                    for (y2 = scanOffsets[y2]; --y3 >= 0; y2 += Rasterizer2D.width) {
                        drawHDGouraudScanline(Rasterizer2D.pixels, y2, x1 >> 16, x2 >> 16, r1, g1, b1, r2, g2, b2, z2,
                                depthSlope);
                        x1 += dx1;
                        x2 += dx2;
                        r1 += dr1;
                        g1 += dg1;
                        b1 += db1;
                        r2 += dr2;
                        g2 += dg2;
                        b2 += db2;
                        z2 += depthScale;
                    }
                    while (--y1 >= 0) {
                        drawHDGouraudScanline(Rasterizer2D.pixels, y2, x1 >> 16, x3 >> 16, r1, g1, b1, r3, g3, b3, z2,
                                depthSlope);
                        x1 += dx1;
                        x3 += dx3;
                        r1 += dr1;
                        g1 += dg1;
                        b1 += db1;
                        r3 += dr3;
                        g3 += dg3;
                        b3 += db3;
                        y2 += Rasterizer2D.width;
                        z2 += depthScale;
                    }
                    return;
                }
                y1 -= y3;
                y3 -= y2;
                for (y2 = scanOffsets[y2]; --y3 >= 0; y2 += Rasterizer2D.width) {
                    drawHDGouraudScanline(Rasterizer2D.pixels, y2, x2 >> 16, x1 >> 16, r2, g2, b2, r1, g1, b1, z2,
                            depthSlope);
                    x1 += dx1;
                    x2 += dx2;
                    r1 += dr1;
                    g1 += dg1;
                    b1 += db1;
                    r2 += dr2;
                    g2 += dg2;
                    b2 += db2;
                    z2 += depthScale;
                }
                while (--y1 >= 0) {
                    drawHDGouraudScanline(Rasterizer2D.pixels, y2, x3 >> 16, x1 >> 16, r3, g3, b3, r1, g1, b1, z2,
                            depthSlope);
                    x1 += dx1;
                    x3 += dx3;
                    r1 += dr1;
                    g1 += dg1;
                    b1 += db1;
                    r3 += dr3;
                    g3 += dg3;
                    b3 += db3;
                    y2 += Rasterizer2D.width;
                    z2 += depthScale;
                }
                return;
            }
            x3 = x2 <<= 16;
            r3 = r2 <<= 16;
            g3 = g2 <<= 16;
            b3 = b2 <<= 16;
            if (y2 < 0) {
                x3 -= dx1 * y2;
                x2 -= dx2 * y2;
                r3 -= dr1 * y2;
                g3 -= dg1 * y2;
                b3 -= db1 * y2;
                r2 -= dr2 * y2;
                g2 -= dg2 * y2;
                b2 -= db2 * y2;
                z2 -= depthScale * y2;
                y2 = 0;
            }
            x1 <<= 16;
            r1 <<= 16;
            g1 <<= 16;
            b1 <<= 16;
            if (y1 < 0) {
                x1 -= dx3 * y1;
                r1 -= dr3 * y1;
                g1 -= dg3 * y1;
                b1 -= db3 * y1;
                y1 = 0;
            }
            if (dx1 < dx2) {
                y3 -= y1;
                y1 -= y2;
                for (y2 = scanOffsets[y2]; --y1 >= 0; y2 += Rasterizer2D.width) {
                    drawHDGouraudScanline(Rasterizer2D.pixels, y2, x3 >> 16, x2 >> 16, r3, g3, b3, r2, g2, b2, z2,
                            depthSlope);
                    x3 += dx1;
                    x2 += dx2;
                    r3 += dr1;
                    g3 += dg1;
                    b3 += db1;
                    r2 += dr2;
                    g2 += dg2;
                    b2 += db2;
                    z2 += depthScale;
                }
                while (--y3 >= 0) {
                    drawHDGouraudScanline(Rasterizer2D.pixels, y2, x1 >> 16, x2 >> 16, r1, g1, b1, r2, g2, b2, z2,
                            depthSlope);
                    x1 += dx3;
                    x2 += dx2;
                    r1 += dr3;
                    g1 += dg3;
                    b1 += db3;
                    r2 += dr2;
                    g2 += dg2;
                    b2 += db2;
                    y2 += Rasterizer2D.width;
                    z2 += depthScale;
                }
                return;
            }
            y3 -= y1;
            y1 -= y2;
            for (y2 = scanOffsets[y2]; --y1 >= 0; y2 += Rasterizer2D.width) {
                drawHDGouraudScanline(Rasterizer2D.pixels, y2, x2 >> 16, x3 >> 16, r2, g2, b2, r3, g3, b3, z2,
                        depthSlope);
                x3 += dx1;
                x2 += dx2;
                r3 += dr1;
                g3 += dg1;
                b3 += db1;
                r2 += dr2;
                g2 += dg2;
                b2 += db2;
                z2 += depthScale;
            }
            while (--y3 >= 0) {
                drawHDGouraudScanline(Rasterizer2D.pixels, y2, x2 >> 16, x1 >> 16, r2, g2, b2, r1, g1, b1, z2,
                        depthSlope);
                x1 += dx3;
                x2 += dx2;
                r1 += dr3;
                g1 += dg3;
                b1 += db3;
                r2 += dr2;
                g2 += dg2;
                b2 += db2;
                y2 += Rasterizer2D.width;
                z2 += depthScale;
            }
            return;
        }
        if (y3 >= Rasterizer2D.clip_bottom) {
            return;
        }
        if (y1 > Rasterizer2D.clip_bottom) {
            y1 = Rasterizer2D.clip_bottom;
        }
        if (y2 > Rasterizer2D.clip_bottom) {
            y2 = Rasterizer2D.clip_bottom;
        }
        z3 = z3 - depthSlope * x3 + depthSlope;
        if (y1 < y2) {
            x2 = x3 <<= 16;
            r2 = r3 <<= 16;
            g2 = g3 <<= 16;
            b2 = b3 <<= 16;
            if (y3 < 0) {
                x2 -= dx2 * y3;
                x3 -= dx3 * y3;
                r2 -= dr2 * y3;
                g2 -= dg2 * y3;
                b2 -= db2 * y3;
                r3 -= dr3 * y3;
                g3 -= dg3 * y3;
                b3 -= db3 * y3;
                z3 -= depthScale * y3;
                y3 = 0;
            }
            x1 <<= 16;
            r1 <<= 16;
            g1 <<= 16;
            b1 <<= 16;
            if (y1 < 0) {
                x1 -= dx1 * y1;
                r1 -= dr1 * y1;
                g1 -= dg1 * y1;
                b1 -= db1 * y1;
                y1 = 0;
            }
            if (dx2 < dx3) {
                y2 -= y1;
                y1 -= y3;
                for (y3 = scanOffsets[y3]; --y1 >= 0; y3 += Rasterizer2D.width) {
                    drawHDGouraudScanline(Rasterizer2D.pixels, y3, x2 >> 16, x3 >> 16, r2, g2, b2, r3, g3, b3, z3,
                            depthSlope);
                    x2 += dx2;
                    x3 += dx3;
                    r2 += dr2;
                    g2 += dg2;
                    b2 += db2;
                    r3 += dr3;
                    g3 += dg3;
                    b3 += db3;
                    z3 += depthScale;
                }
                while (--y2 >= 0) {
                    drawHDGouraudScanline(Rasterizer2D.pixels, y3, x2 >> 16, x1 >> 16, r2, g2, b2, r1, g1, b1, z3,
                            depthSlope);
                    x2 += dx2;
                    x1 += dx1;
                    r2 += dr2;
                    g2 += dg2;
                    b2 += db2;
                    r1 += dr1;
                    g1 += dg1;
                    b1 += db1;
                    y3 += Rasterizer2D.width;
                    z3 += depthScale;
                }
                return;
            }
            y2 -= y1;
            y1 -= y3;
            for (y3 = scanOffsets[y3]; --y1 >= 0; y3 += Rasterizer2D.width) {
                drawHDGouraudScanline(Rasterizer2D.pixels, y3, x3 >> 16, x2 >> 16, r3, g3, b3, r2, g2, b2, z3,
                        depthSlope);
                x2 += dx2;
                x3 += dx3;
                r2 += dr2;
                g2 += dg2;
                b2 += db2;
                r3 += dr3;
                g3 += dg3;
                b3 += db3;
                z3 += depthScale;
            }
            while (--y2 >= 0) {
                drawHDGouraudScanline(Rasterizer2D.pixels, y3, x1 >> 16, x2 >> 16, r1, g1, b1, r2, g2, b2, z3,
                        depthSlope);
                x2 += dx2;
                x1 += dx1;
                r2 += dr2;
                g2 += dg2;
                b2 += db2;
                r1 += dr1;
                g1 += dg1;
                b1 += db1;
                z3 += depthScale;
                y3 += Rasterizer2D.width;
            }
            return;
        }
        x1 = x3 <<= 16;
        r1 = r3 <<= 16;
        g1 = g3 <<= 16;
        b1 = b3 <<= 16;
        if (y3 < 0) {
            x1 -= dx2 * y3;
            x3 -= dx3 * y3;
            r1 -= dr2 * y3;
            g1 -= dg2 * y3;
            b1 -= db2 * y3;
            r3 -= dr3 * y3;
            g3 -= dg3 * y3;
            b3 -= db3 * y3;
            z3 -= depthScale * y3;
            y3 = 0;
        }
        x2 <<= 16;
        r2 <<= 16;
        g2 <<= 16;
        b2 <<= 16;
        if (y2 < 0) {
            x2 -= dx1 * y2;
            r2 -= dr1 * y2;
            g2 -= dg1 * y2;
            b2 -= db1 * y2;
            y2 = 0;
        }
        if (dx2 < dx3) {
            y1 -= y2;
            y2 -= y3;
            for (y3 = scanOffsets[y3]; --y2 >= 0; y3 += Rasterizer2D.width) {
                drawHDGouraudScanline(Rasterizer2D.pixels, y3, x1 >> 16, x3 >> 16, r1, g1, b1, r3, g3, b3, z3,
                        depthSlope);
                x1 += dx2;
                x3 += dx3;
                r1 += dr2;
                g1 += dg2;
                b1 += db2;
                r3 += dr3;
                g3 += dg3;
                b3 += db3;
                z3 += depthScale;
            }
            while (--y1 >= 0) {
                drawHDGouraudScanline(Rasterizer2D.pixels, y3, x2 >> 16, x3 >> 16, r2, g2, b2, r3, g3, b3, z3,
                        depthSlope);
                x2 += dx1;
                x3 += dx3;
                r2 += dr1;
                g2 += dg1;
                b2 += db1;
                r3 += dr3;
                g3 += dg3;
                b3 += db3;
                z3 += depthScale;
                y3 += Rasterizer2D.width;
            }
            return;
        }
        y1 -= y2;
        y2 -= y3;
        for (y3 = scanOffsets[y3]; --y2 >= 0; y3 += Rasterizer2D.width) {
            drawHDGouraudScanline(Rasterizer2D.pixels, y3, x3 >> 16, x1 >> 16, r3, g3, b3, r1, g1, b1, z3, depthSlope);
            x1 += dx2;
            x3 += dx3;
            r1 += dr2;
            g1 += dg2;
            b1 += db2;
            r3 += dr3;
            g3 += dg3;
            b3 += db3;
            z3 += depthScale;
        }
        while (--y1 >= 0) {
            drawHDGouraudScanline(Rasterizer2D.pixels, y3, x3 >> 16, x2 >> 16, r3, g3, b3, r2, g2, b2, z3, depthSlope);
            x2 += dx1;
            x3 += dx3;
            r2 += dr1;
            g2 += dg1;
            b2 += db1;
            r3 += dr3;
            g3 += dg3;
            b3 += db3;
            y3 += Rasterizer2D.width;
            z3 += depthScale;
        }
    }

    public static void drawHDGouraudScanline(int[] dest, int offset, int x1, int x2, int r1, int g1, int b1, int r2,
                                             int g2, int b2, float z1, float z2) {
        int n = x2 - x1;
        if (n <= 0) {
            return;
        }
        r2 = (r2 - r1) / n;
        g2 = (g2 - g1) / n;
        b2 = (b2 - b1) / n;
        if (textureOutOfDrawingBounds) {
            if (x2 > Rasterizer2D.lastX) {
                n -= x2 - Rasterizer2D.lastX;
                x2 = Rasterizer2D.lastX;
            }
            if (x1 < 0) {
                n = x2;
                r1 -= x1 * r2;
                g1 -= x1 * g2;
                b1 -= x1 * b2;
                x1 = 0;
            }
        }
        if (x1 < x2) {
            offset += x1;
            z1 += z2 * x1;
            if (alpha == 0) {
                while (--n >= 0) {
                    dest[offset] = (r1 & 0xff0000) | (g1 >> 8 & 0xff00) | (b1 >> 16 & 0xff);
                    if (saveDepth) {
                        depthBuffer[offset] = z1;
                    }
                    z1 += z2;
                    r1 += r2;
                    g1 += g2;
                    b1 += b2;
                    offset++;
                }
            } else {
                final int a1 = alpha;
                final int a2 = 256 - alpha;
                int rgb;
                while (--n >= 0) {
                    rgb = r1 & 0xff0000 | g1 >> 8 & 0xff00 | b1 >> 16 & 0xff;
                    rgb = ((rgb & 0xff00ff) * a2 >> 8 & 0xff00ff) + ((rgb & 0xff00) * a2 >> 8 & 0xff00);
                    int dst = dest[offset];
                    dest[offset] = rgb + ((dst & 0xff00ff) * a1 >> 8 & 0xff00ff) + ((dst & 0xff00) * a1 >> 8 & 0xff00);
                    if (saveDepth) {
                        z1 = depthBuffer[offset];
                    }
                    offset++;
                    z1 += z2;
                    r1 += r2;
                    g1 += g2;
                    b1 += b2;
                }
            }
        }
    }

    public static void drawFlatTriangle(int y1, int y2, int y3, int x1, int x2, int x3, int rgb, float z1, float z2,
    		float z3) {
        if (!saveDepth) {
            z1 = z2 = z3 = 0;
        }
        int dx1 = 0;
        if (y2 != y1) {
            final int d = (y2 - y1);
            dx1 = (x2 - x1 << 16) / d;
        }
        int dx2 = 0;
        if (y3 != y2) {
            final int d = (y3 - y2);
            dx2 = (x3 - x2 << 16) / d;
        }
        int dx3 = 0;
        if (y3 != y1) {
            final int d = (y1 - y3);
            dx3 = (x1 - x3 << 16) / d;
        }

        float x21 = x2 - x1;
        float y32 = y2 - y1;
        float x31 = x3 - x1;
        float y31 = y3 - y1;
        float z21 = z2 - z1;
        float z31 = z3 - z1;

        float div = x21 * y31 - x31 * y32;
        float depthSlope = (z21 * y31 - z31 * y32) / div;
        float depthScale = (z31 * x21 - z21 * x31) / div;

        if (y1 <= y2 && y1 <= y3) {
            if (y1 >= Rasterizer2D.clip_bottom) {
                return;
            }
            if (y2 > Rasterizer2D.clip_bottom) {
                y2 = Rasterizer2D.clip_bottom;
            }
            if (y3 > Rasterizer2D.clip_bottom) {
                y3 = Rasterizer2D.clip_bottom;
            }
            z1 = z1 - depthSlope * x1 + depthSlope;
            if (y2 < y3) {
                x3 = x1 <<= 16;
                if (y1 < 0) {
                    x3 -= dx3 * y1;
                    x1 -= dx1 * y1;
                    z1 -= depthScale * y1;
                    y1 = 0;
                }
                x2 <<= 16;
                if (y2 < 0) {
                    x2 -= dx2 * y2;
                    y2 = 0;
                }
                if (y1 != y2 && dx3 < dx1 || y1 == y2 && dx3 > dx2) {
                    y3 -= y2;
                    y2 -= y1;
                    for (y1 = scanOffsets[y1]; --y2 >= 0; y1 += Rasterizer2D.width) {
                        drawFlatScanline(Rasterizer2D.pixels, y1, rgb, x3 >> 16, x1 >> 16, z1, depthSlope);
                        z1 += depthScale;
                        x3 += dx3;
                        x1 += dx1;
                    }
                    while (--y3 >= 0) {
                        drawFlatScanline(Rasterizer2D.pixels, y1, rgb, x3 >> 16, x2 >> 16, z1, depthSlope);
                        z1 += depthScale;
                        x3 += dx3;
                        x2 += dx2;
                        y1 += Rasterizer2D.width;
                    }
                    return;
                }
                y3 -= y2;
                y2 -= y1;
                for (y1 = scanOffsets[y1]; --y2 >= 0; y1 += Rasterizer2D.width) {
                    drawFlatScanline(Rasterizer2D.pixels, y1, rgb, x1 >> 16, x3 >> 16, z1, depthSlope);
                    z1 += depthScale;
                    x3 += dx3;
                    x1 += dx1;
                }
                while (--y3 >= 0) {
                    drawFlatScanline(Rasterizer2D.pixels, y1, rgb, x2 >> 16, x3 >> 16, z1, depthSlope);
                    z1 += depthScale;
                    x3 += dx3;
                    x2 += dx2;
                    y1 += Rasterizer2D.width;
                }
                return;
            }
            x2 = x1 <<= 16;
            if (y1 < 0) {
                x2 -= dx3 * y1;
                x1 -= dx1 * y1;
                z1 -= depthScale * y1;
                y1 = 0;
            }
            x3 <<= 16;
            if (y3 < 0) {
                x3 -= dx2 * y3;
                y3 = 0;
            }
            if (y1 != y3 && dx3 < dx1 || y1 == y3 && dx2 > dx1) {
                y2 -= y3;
                y3 -= y1;
                for (y1 = scanOffsets[y1]; --y3 >= 0; y1 += Rasterizer2D.width) {
                    drawFlatScanline(Rasterizer2D.pixels, y1, rgb, x2 >> 16, x1 >> 16, z1, depthSlope);
                    z1 += depthScale;
                    x2 += dx3;
                    x1 += dx1;
                }
                while (--y2 >= 0) {
                    drawFlatScanline(Rasterizer2D.pixels, y1, rgb, x3 >> 16, x1 >> 16, z1, depthSlope);
                    z1 += depthScale;
                    x3 += dx2;
                    x1 += dx1;
                    y1 += Rasterizer2D.width;
                }
                return;
            }
            y2 -= y3;
            y3 -= y1;
            for (y1 = scanOffsets[y1]; --y3 >= 0; y1 += Rasterizer2D.width) {
                drawFlatScanline(Rasterizer2D.pixels, y1, rgb, x1 >> 16, x2 >> 16, z1, depthSlope);
                z1 += depthScale;
                x2 += dx3;
                x1 += dx1;
            }
            while (--y2 >= 0) {
                drawFlatScanline(Rasterizer2D.pixels, y1, rgb, x1 >> 16, x3 >> 16, z1, depthSlope);
                z1 += depthScale;
                x3 += dx2;
                x1 += dx1;
                y1 += Rasterizer2D.width;
            }
            return;
        }
        if (y2 <= y3) {
            if (y2 >= Rasterizer2D.clip_bottom) {
                return;
            }
            if (y3 > Rasterizer2D.clip_bottom) {
                y3 = Rasterizer2D.clip_bottom;
            }
            if (y1 > Rasterizer2D.clip_bottom) {
                y1 = Rasterizer2D.clip_bottom;
            }
            z2 = z2 - depthSlope * x2 + depthSlope;
            if (y3 < y1) {
                x1 = x2 <<= 16;
                if (y2 < 0) {
                    x1 -= dx1 * y2;
                    x2 -= dx2 * y2;
                    z2 -= depthScale * y2;
                    y2 = 0;
                }
                x3 <<= 16;
                if (y3 < 0) {
                    x3 -= dx3 * y3;
                    y3 = 0;
                }
                if (y2 != y3 && dx1 < dx2 || y2 == y3 && dx1 > dx3) {
                    y1 -= y3;
                    y3 -= y2;
                    for (y2 = scanOffsets[y2]; --y3 >= 0; y2 += Rasterizer2D.width) {
                        drawFlatScanline(Rasterizer2D.pixels, y2, rgb, x1 >> 16, x2 >> 16, z2, depthSlope);
                        z2 += depthScale;
                        x1 += dx1;
                        x2 += dx2;
                    }
                    while (--y1 >= 0) {
                        drawFlatScanline(Rasterizer2D.pixels, y2, rgb, x1 >> 16, x3 >> 16, z2, depthSlope);
                        z2 += depthScale;
                        x1 += dx1;
                        x3 += dx3;
                        y2 += Rasterizer2D.width;
                    }
                    return;
                }
                y1 -= y3;
                y3 -= y2;
                for (y2 = scanOffsets[y2]; --y3 >= 0; y2 += Rasterizer2D.width) {
                    drawFlatScanline(Rasterizer2D.pixels, y2, rgb, x2 >> 16, x1 >> 16, z2, depthSlope);
                    z2 += depthScale;
                    x1 += dx1;
                    x2 += dx2;
                }
                while (--y1 >= 0) {
                    drawFlatScanline(Rasterizer2D.pixels, y2, rgb, x3 >> 16, x1 >> 16, z2, depthSlope);
                    z2 += depthScale;
                    x1 += dx1;
                    x3 += dx3;
                    y2 += Rasterizer2D.width;
                }
                return;
            }
            x3 = x2 <<= 16;
            if (y2 < 0) {
                x3 -= dx1 * y2;
                x2 -= dx2 * y2;
                z2 -= depthScale * y2;
                y2 = 0;
            }
            x1 <<= 16;
            if (y1 < 0) {
                x1 -= dx3 * y1;
                y1 = 0;
            }
            if (dx1 < dx2) {
                y3 -= y1;
                y1 -= y2;
                for (y2 = scanOffsets[y2]; --y1 >= 0; y2 += Rasterizer2D.width) {
                    drawFlatScanline(Rasterizer2D.pixels, y2, rgb, x3 >> 16, x2 >> 16, z2, depthSlope);
                    z2 += depthScale;
                    x3 += dx1;
                    x2 += dx2;
                }
                while (--y3 >= 0) {
                    drawFlatScanline(Rasterizer2D.pixels, y2, rgb, x1 >> 16, x2 >> 16, z2, depthSlope);
                    z2 += depthScale;
                    x1 += dx3;
                    x2 += dx2;
                    y2 += Rasterizer2D.width;
                }
                return;
            }
            y3 -= y1;
            y1 -= y2;
            for (y2 = scanOffsets[y2]; --y1 >= 0; y2 += Rasterizer2D.width) {
                drawFlatScanline(Rasterizer2D.pixels, y2, rgb, x2 >> 16, x3 >> 16, z2, depthSlope);
                z2 += depthScale;
                x3 += dx1;
                x2 += dx2;
            }
            while (--y3 >= 0) {
                drawFlatScanline(Rasterizer2D.pixels, y2, rgb, x2 >> 16, x1 >> 16, z2, depthSlope);
                z2 += depthScale;
                x1 += dx3;
                x2 += dx2;
                y2 += Rasterizer2D.width;
            }
            return;
        }
        if (y3 >= Rasterizer2D.clip_bottom) {
            return;
        }
        if (y1 > Rasterizer2D.clip_bottom) {
            y1 = Rasterizer2D.clip_bottom;
        }
        if (y2 > Rasterizer2D.clip_bottom) {
            y2 = Rasterizer2D.clip_bottom;
        }
        z3 = z3 - depthSlope * x3 + depthSlope;
        if (y1 < y2) {
            x2 = x3 <<= 16;
            if (y3 < 0) {
                x2 -= dx2 * y3;
                x3 -= dx3 * y3;
                z3 -= depthScale * y3;
                y3 = 0;
            }
            x1 <<= 16;
            if (y1 < 0) {
                x1 -= dx1 * y1;
                y1 = 0;
            }
            if (dx2 < dx3) {
                y2 -= y1;
                y1 -= y3;
                for (y3 = scanOffsets[y3]; --y1 >= 0; y3 += Rasterizer2D.width) {
                    drawFlatScanline(Rasterizer2D.pixels, y3, rgb, x2 >> 16, x3 >> 16, z3, depthSlope);
                    z3 += depthScale;
                    x2 += dx2;
                    x3 += dx3;
                }
                while (--y2 >= 0) {
                    drawFlatScanline(Rasterizer2D.pixels, y3, rgb, x2 >> 16, x1 >> 16, z3, depthSlope);
                    z3 += depthScale;
                    x2 += dx2;
                    x1 += dx1;
                    y3 += Rasterizer2D.width;
                }
                return;
            }
            y2 -= y1;
            y1 -= y3;
            for (y3 = scanOffsets[y3]; --y1 >= 0; y3 += Rasterizer2D.width) {
                drawFlatScanline(Rasterizer2D.pixels, y3, rgb, x3 >> 16, x2 >> 16, z3, depthSlope);
                z3 += depthScale;
                x2 += dx2;
                x3 += dx3;
            }
            while (--y2 >= 0) {
                drawFlatScanline(Rasterizer2D.pixels, y3, rgb, x1 >> 16, x2 >> 16, z3, depthSlope);
                z3 += depthScale;
                x2 += dx2;
                x1 += dx1;
                y3 += Rasterizer2D.width;
            }
            return;
        }
        x1 = x3 <<= 16;
        if (y3 < 0) {
            x1 -= dx2 * y3;
            x3 -= dx3 * y3;
            z3 -= depthScale * y3;
            y3 = 0;
        }
        x2 <<= 16;
        if (y2 < 0) {
            x2 -= dx1 * y2;
            y2 = 0;
        }
        if (dx2 < dx3) {
            y1 -= y2;
            y2 -= y3;
            for (y3 = scanOffsets[y3]; --y2 >= 0; y3 += Rasterizer2D.width) {
                drawFlatScanline(Rasterizer2D.pixels, y3, rgb, x1 >> 16, x3 >> 16, z3, depthSlope);
                z3 += depthScale;
                x1 += dx2;
                x3 += dx3;
            }
            while (--y1 >= 0) {
                drawFlatScanline(Rasterizer2D.pixels, y3, rgb, x2 >> 16, x3 >> 16, z3, depthSlope);
                z3 += depthScale;
                x2 += dx1;
                x3 += dx3;
                y3 += Rasterizer2D.width;
            }
            return;
        }
        y1 -= y2;
        y2 -= y3;
        for (y3 = scanOffsets[y3]; --y2 >= 0; y3 += Rasterizer2D.width) {
            drawFlatScanline(Rasterizer2D.pixels, y3, rgb, x3 >> 16, x1 >> 16, z3, depthSlope);
            z3 += depthScale;
            x1 += dx2;
            x3 += dx3;
        }
        while (--y1 >= 0) {
            drawFlatScanline(Rasterizer2D.pixels, y3, rgb, x3 >> 16, x2 >> 16, z3, depthSlope);
            z3 += depthScale;
            x2 += dx1;
            x3 += dx3;
            y3 += Rasterizer2D.width;
        }
    }

    private static void drawFlatScanline(int[] dest, int offset, int rgb, int x1, int x2, float z1, float z2) {
        if (x1 >= x2) {
            return;
        }
        if (textureOutOfDrawingBounds) {
            if (x2 > Rasterizer2D.lastX) {
                x2 = Rasterizer2D.lastX;
            }
            if (x1 < 0) {
                x1 = 0;
            }
        }
        if (x1 >= x2) {
            return;
        }
        offset += x1;
        z1 += z2 * x1;
        int n = x2 - x1;
        if (alpha == 0) {
            while (--n >= 0) {
                dest[offset] = rgb;
                if (saveDepth) {
                    depthBuffer[offset] = z1;
                }
                z1 += z2;
                offset++;
            }
        } else {
            final int a1 = alpha;
            final int a2 = 256 - alpha;
            rgb = ((rgb & 0xff00ff) * a2 >> 8 & 0xff00ff) + ((rgb & 0xff00) * a2 >> 8 & 0xff00);
            while (--n >= 0) {
                dest[offset] = rgb + ((dest[offset] & 0xff00ff) * a1 >> 8 & 0xff00ff)
                        + ((dest[offset] & 0xff00) * a1 >> 8 & 0xff00);
                if (saveDepth) {
                    depthBuffer[offset] = z1;
                }
                z1 += z2;
                offset++;
            }
        }
    }

    public static void drawTexturedTriangle317(int y1, int y2, int y3, int x1, int x2, int x3, int c1, int c2, int c3,
                                               int t1, int t2, int t3, int t4, int t5, int t6, int t7, int t8, int t9, int tex, float z1, float z2, float z3) {
        if (!saveDepth) {
            z1 = z2 = z3 = 0;
        }
        int[] texels = getTexturePixels(tex);
        aBoolean1463 = !textureIsTransparant[tex];
        t2 = t1 - t2;
        t5 = t4 - t5;
        t8 = t7 - t8;
        t3 -= t1;
        t6 -= t4;
        t9 -= t7;
        int l4 = (t3 * t4 - t6 * t1) * WorldController.focalLength << 5;
        int i5 = t6 * t7 - t9 * t4 << 8;
        int j5 = t9 * t1 - t3 * t7 << 5;
        int k5 = (t2 * t4 - t5 * t1) * WorldController.focalLength << 5;
        int l5 = t5 * t7 - t8 * t4 << 8;
        int i6 = t8 * t1 - t2 * t7 << 5;
        int j6 = (t5 * t3 - t2 * t6) * WorldController.focalLength << 5;
        int k6 = t8 * t6 - t5 * t9 << 8;
        int l6 = t2 * t9 - t8 * t3 << 5;
        int i7 = 0;
        int j7 = 0;
        if (y2 != y1) {
            i7 = (x2 - x1 << 16) / (y2 - y1);
            j7 = (c2 - c1 << 16) / (y2 - y1);
        }
        int k7 = 0;
        int l7 = 0;
        if (y3 != y2) {
            k7 = (x3 - x2 << 16) / (y3 - y2);
            l7 = (c3 - c2 << 16) / (y3 - y2);
        }
        int i8 = 0;
        int j8 = 0;
        if (y3 != y1) {
            i8 = (x1 - x3 << 16) / (y1 - y3);
            j8 = (c1 - c3 << 16) / (y1 - y3);
        }

        float x21 = x2 - x1;
        float y32 = y2 - y1;
        float x31 = x3 - x1;
        float y31 = y3 - y1;
        float z21 = z2 - z1;
        float z31 = z3 - z1;

        float div = x21 * y31 - x31 * y32;
        float depthSlope = (z21 * y31 - z31 * y32) / div;
        float depthScale = (z31 * x21 - z21 * x31) / div;

        if (y1 <= y2 && y1 <= y3) {
            if (y1 >= Rasterizer2D.clip_bottom) {
                return;
            }
            if (y2 > Rasterizer2D.clip_bottom) {
                y2 = Rasterizer2D.clip_bottom;
            }
            if (y3 > Rasterizer2D.clip_bottom) {
                y3 = Rasterizer2D.clip_bottom;
            }
            z1 = z1 - depthSlope * x1 + depthSlope;
            if (y2 < y3) {
                x3 = x1 <<= 16;
                c3 = c1 <<= 16;
                if (y1 < 0) {
                    y1 -= 0;
                    x3 -= i8 * y1;
                    x1 -= i7 * y1;
                    z1 -= depthScale * y1;
                    c3 -= j8 * y1;
                    c1 -= j7 * y1;
                    y1 = 0;
                }
                x2 <<= 16;
                c2 <<= 16;
                if (y2 < 0) {
                    y2 -= 0;
                    x2 -= k7 * y2;
                    c2 -= l7 * y2;
                    y2 = 0;
                }
                int k8 = y1 - originViewY;
                l4 += j5 * k8;
                k5 += i6 * k8;
                j6 += l6 * k8;
                if (y1 != y2 && i8 < i7 || y1 == y2 && i8 > k7) {
                    y3 -= y2;
                    y2 -= y1;
                    y1 = scanOffsets[y1];
                    while (--y2 >= 0) {
                        drawTexturedScanline317(Rasterizer2D.pixels, texels, y1, x3 >> 16, x1 >> 16, c3 >> 8, c1 >> 8,
                                l4, k5, j6, i5, l5, k6, z1, depthSlope);
                        z1 += depthScale;
                        x3 += i8;
                        x1 += i7;
                        c3 += j8;
                        c1 += j7;
                        y1 += Rasterizer2D.width;
                        l4 += j5;
                        k5 += i6;
                        j6 += l6;
                    }
                    while (--y3 >= 0) {
                        drawTexturedScanline317(Rasterizer2D.pixels, texels, y1, x3 >> 16, x2 >> 16, c3 >> 8, c2 >> 8,
                                l4, k5, j6, i5, l5, k6, z1, depthSlope);
                        z1 += depthScale;
                        x3 += i8;
                        x2 += k7;
                        c3 += j8;
                        c2 += l7;
                        y1 += Rasterizer2D.width;
                        l4 += j5;
                        k5 += i6;
                        j6 += l6;
                    }
                    return;
                }
                y3 -= y2;
                y2 -= y1;
                y1 = scanOffsets[y1];
                while (--y2 >= 0) {
                    drawTexturedScanline317(Rasterizer2D.pixels, texels, y1, x1 >> 16, x3 >> 16, c1 >> 8, c3 >> 8, l4,
                            k5, j6, i5, l5, k6, z1, depthSlope);
                    z1 += depthScale;
                    x3 += i8;
                    x1 += i7;
                    c3 += j8;
                    c1 += j7;
                    y1 += Rasterizer2D.width;
                    l4 += j5;
                    k5 += i6;
                    j6 += l6;
                }
                while (--y3 >= 0) {
                    drawTexturedScanline317(Rasterizer2D.pixels, texels, y1, x2 >> 16, x3 >> 16, c2 >> 8, c3 >> 8, l4,
                            k5, j6, i5, l5, k6, z1, depthSlope);
                    z1 += depthScale;
                    x3 += i8;
                    x2 += k7;
                    c3 += j8;
                    c2 += l7;
                    y1 += Rasterizer2D.width;
                    l4 += j5;
                    k5 += i6;
                    j6 += l6;
                }
                return;
            }
            x2 = x1 <<= 16;
            c2 = c1 <<= 16;
            if (y1 < 0) {
                y1 -= 0;
                x2 -= i8 * y1;
                z1 -= depthScale * y1;
                x1 -= i7 * y1;
                c2 -= j8 * y1;
                c1 -= j7 * y1;
                y1 = 0;
            }
            x3 <<= 16;
            c3 <<= 16;
            if (y3 < 0) {
                y3 -= 0;
                x3 -= k7 * y3;
                c3 -= l7 * y3;
                y3 = 0;
            }
            int l8 = y1 - originViewY;
            l4 += j5 * l8;
            k5 += i6 * l8;
            j6 += l6 * l8;
            if (y1 != y3 && i8 < i7 || y1 == y3 && k7 > i7) {
                y2 -= y3;
                y3 -= y1;
                y1 = scanOffsets[y1];
                while (--y3 >= 0) {
                    drawTexturedScanline317(Rasterizer2D.pixels, texels, y1, x2 >> 16, x1 >> 16, c2 >> 8, c1 >> 8, l4,
                            k5, j6, i5, l5, k6, z1, depthSlope);
                    z1 += depthScale;
                    x2 += i8;
                    x1 += i7;
                    c2 += j8;
                    c1 += j7;
                    y1 += Rasterizer2D.width;
                    l4 += j5;
                    k5 += i6;
                    j6 += l6;
                }
                while (--y2 >= 0) {
                    drawTexturedScanline317(Rasterizer2D.pixels, texels, y1, x3 >> 16, x1 >> 16, c3 >> 8, c1 >> 8, l4,
                            k5, j6, i5, l5, k6, z1, depthSlope);
                    z1 += depthScale;
                    x3 += k7;
                    x1 += i7;
                    c3 += l7;
                    c1 += j7;
                    y1 += Rasterizer2D.width;
                    l4 += j5;
                    k5 += i6;
                    j6 += l6;
                }
                return;
            }
            y2 -= y3;
            y3 -= y1;
            y1 = scanOffsets[y1];
            while (--y3 >= 0) {
                drawTexturedScanline317(Rasterizer2D.pixels, texels, y1, x1 >> 16, x2 >> 16, c1 >> 8, c2 >> 8, l4, k5,
                        j6, i5, l5, k6, z1, depthSlope);
                z1 += depthScale;
                x2 += i8;
                x1 += i7;
                c2 += j8;
                c1 += j7;
                y1 += Rasterizer2D.width;
                l4 += j5;
                k5 += i6;
                j6 += l6;
            }
            while (--y2 >= 0) {
                drawTexturedScanline317(Rasterizer2D.pixels, texels, y1, x1 >> 16, x3 >> 16, c1 >> 8, c3 >> 8, l4, k5,
                        j6, i5, l5, k6, z1, depthSlope);
                z1 += depthScale;
                x3 += k7;
                x1 += i7;
                c3 += l7;
                c1 += j7;
                y1 += Rasterizer2D.width;
                l4 += j5;
                k5 += i6;
                j6 += l6;
            }
            return;
        }
        if (y2 <= y3) {
            if (y2 >= Rasterizer2D.clip_bottom) {
                return;
            }
            if (y3 > Rasterizer2D.clip_bottom) {
                y3 = Rasterizer2D.clip_bottom;
            }
            if (y1 > Rasterizer2D.clip_bottom) {
                y1 = Rasterizer2D.clip_bottom;
            }
            z2 = z2 - depthSlope * x2 + depthSlope;
            if (y3 < y1) {
                x1 = x2 <<= 16;
                c1 = c2 <<= 16;
                if (y2 < 0) {
                    y2 -= 0;
                    x1 -= i7 * y2;
                    x2 -= k7 * y2;
                    z2 -= depthScale * y2;
                    c1 -= j7 * y2;
                    c2 -= l7 * y2;
                    y2 = 0;
                }
                x3 <<= 16;
                c3 <<= 16;
                if (y3 < 0) {
                    y3 -= 0;
                    x3 -= i8 * y3;
                    c3 -= j8 * y3;
                    y3 = 0;
                }
                int i9 = y2 - originViewY;
                l4 += j5 * i9;
                k5 += i6 * i9;
                j6 += l6 * i9;
                if (y2 != y3 && i7 < k7 || y2 == y3 && i7 > i8) {
                    y1 -= y3;
                    y3 -= y2;
                    y2 = scanOffsets[y2];
                    while (--y3 >= 0) {
                        drawTexturedScanline317(Rasterizer2D.pixels, texels, y2, x1 >> 16, x2 >> 16, c1 >> 8, c2 >> 8,
                                l4, k5, j6, i5, l5, k6, z2, depthSlope);
                        z2 += depthScale;
                        x1 += i7;
                        x2 += k7;
                        c1 += j7;
                        c2 += l7;
                        y2 += Rasterizer2D.width;
                        l4 += j5;
                        k5 += i6;
                        j6 += l6;
                    }
                    while (--y1 >= 0) {
                        drawTexturedScanline317(Rasterizer2D.pixels, texels, y2, x1 >> 16, x3 >> 16, c1 >> 8, c3 >> 8,
                                l4, k5, j6, i5, l5, k6, z2, depthSlope);
                        z2 += depthScale;
                        x1 += i7;
                        x3 += i8;
                        c1 += j7;
                        c3 += j8;
                        y2 += Rasterizer2D.width;
                        l4 += j5;
                        k5 += i6;
                        j6 += l6;
                    }
                    return;
                }
                y1 -= y3;
                y3 -= y2;
                y2 = scanOffsets[y2];
                while (--y3 >= 0) {
                    drawTexturedScanline317(Rasterizer2D.pixels, texels, y2, x2 >> 16, x1 >> 16, c2 >> 8, c1 >> 8, l4,
                            k5, j6, i5, l5, k6, z2, depthSlope);
                    z2 += depthScale;
                    x1 += i7;
                    x2 += k7;
                    c1 += j7;
                    c2 += l7;
                    y2 += Rasterizer2D.width;
                    l4 += j5;
                    k5 += i6;
                    j6 += l6;
                }
                while (--y1 >= 0) {
                    drawTexturedScanline317(Rasterizer2D.pixels, texels, y2, x3 >> 16, x1 >> 16, c3 >> 8, c1 >> 8, l4,
                            k5, j6, i5, l5, k6, z2, depthSlope);
                    z2 += depthScale;
                    x1 += i7;
                    x3 += i8;
                    c1 += j7;
                    c3 += j8;
                    y2 += Rasterizer2D.width;
                    l4 += j5;
                    k5 += i6;
                    j6 += l6;
                }
                return;
            }
            x3 = x2 <<= 16;
            c3 = c2 <<= 16;
            if (y2 < 0) {
                y2 -= 0;
                x3 -= i7 * y2;
                z2 -= depthScale * y2;
                x2 -= k7 * y2;
                c3 -= j7 * y2;
                c2 -= l7 * y2;
                y2 = 0;
            }
            x1 <<= 16;
            c1 <<= 16;
            if (y1 < 0) {
                y1 -= 0;
                x1 -= i8 * y1;
                c1 -= j8 * y1;
                y1 = 0;
            }
            int j9 = y2 - originViewY;
            l4 += j5 * j9;
            k5 += i6 * j9;
            j6 += l6 * j9;
            if (i7 < k7) {
                y3 -= y1;
                y1 -= y2;
                y2 = scanOffsets[y2];
                while (--y1 >= 0) {
                    drawTexturedScanline317(Rasterizer2D.pixels, texels, y2, x3 >> 16, x2 >> 16, c3 >> 8, c2 >> 8, l4,
                            k5, j6, i5, l5, k6, z2, depthSlope);
                    z2 += depthScale;
                    x3 += i7;
                    x2 += k7;
                    c3 += j7;
                    c2 += l7;
                    y2 += Rasterizer2D.width;
                    l4 += j5;
                    k5 += i6;
                    j6 += l6;
                }
                while (--y3 >= 0) {
                    drawTexturedScanline317(Rasterizer2D.pixels, texels, y2, x1 >> 16, x2 >> 16, c1 >> 8, c2 >> 8, l4,
                            k5, j6, i5, l5, k6, z2, depthSlope);
                    z2 += depthScale;
                    x1 += i8;
                    x2 += k7;
                    c1 += j8;
                    c2 += l7;
                    y2 += Rasterizer2D.width;
                    l4 += j5;
                    k5 += i6;
                    j6 += l6;
                }
                return;
            }
            y3 -= y1;
            y1 -= y2;
            y2 = scanOffsets[y2];
            while (--y1 >= 0) {
                drawTexturedScanline317(Rasterizer2D.pixels, texels, y2, x2 >> 16, x3 >> 16, c2 >> 8, c3 >> 8, l4, k5,
                        j6, i5, l5, k6, z2, depthSlope);
                z2 += depthScale;
                x3 += i7;
                x2 += k7;
                c3 += j7;
                c2 += l7;
                y2 += Rasterizer2D.width;
                l4 += j5;
                k5 += i6;
                j6 += l6;
            }
            while (--y3 >= 0) {
                drawTexturedScanline317(Rasterizer2D.pixels, texels, y2, x2 >> 16, x1 >> 16, c2 >> 8, c1 >> 8, l4, k5,
                        j6, i5, l5, k6, z2, depthSlope);
                z2 += depthScale;
                x1 += i8;
                x2 += k7;
                c1 += j8;
                c2 += l7;
                y2 += Rasterizer2D.width;
                l4 += j5;
                k5 += i6;
                j6 += l6;
            }
            return;
        }
        if (y3 >= Rasterizer2D.clip_bottom) {
            return;
        }
        if (y1 > Rasterizer2D.clip_bottom) {
            y1 = Rasterizer2D.clip_bottom;
        }
        if (y2 > Rasterizer2D.clip_bottom) {
            y2 = Rasterizer2D.clip_bottom;
        }
        z3 = z3 - depthSlope * x3 + depthSlope;
        if (y1 < y2) {
            x2 = x3 <<= 16;
            c2 = c3 <<= 16;
            if (y3 < 0) {
                y3 -= 0;
                x2 -= k7 * y3;
                z3 -= depthScale * y3;
                x3 -= i8 * y3;
                c2 -= l7 * y3;
                c3 -= j8 * y3;
                y3 = 0;
            }
            x1 <<= 16;
            c1 <<= 16;
            if (y1 < 0) {
                y1 -= 0;
                x1 -= i7 * y1;
                c1 -= j7 * y1;
                y1 = 0;
            }
            final int k9 = y3 - originViewY;
            l4 += j5 * k9;
            k5 += i6 * k9;
            j6 += l6 * k9;
            if (k7 < i8) {
                y2 -= y1;
                y1 -= y3;
                y3 = scanOffsets[y3];
                while (--y1 >= 0) {
                    drawTexturedScanline317(Rasterizer2D.pixels, texels, y3, x2 >> 16, x3 >> 16, c2 >> 8, c3 >> 8, l4,
                            k5, j6, i5, l5, k6, z3, depthSlope);
                    z3 += depthScale;
                    x2 += k7;
                    x3 += i8;
                    c2 += l7;
                    c3 += j8;
                    y3 += Rasterizer2D.width;
                    l4 += j5;
                    k5 += i6;
                    j6 += l6;
                }
                while (--y2 >= 0) {
                    drawTexturedScanline317(Rasterizer2D.pixels, texels, y3, x2 >> 16, x1 >> 16, c2 >> 8, c1 >> 8, l4,
                            k5, j6, i5, l5, k6, z3, depthSlope);
                    z3 += depthScale;
                    x2 += k7;
                    x1 += i7;
                    c2 += l7;
                    c1 += j7;
                    y3 += Rasterizer2D.width;
                    l4 += j5;
                    k5 += i6;
                    j6 += l6;
                }
                return;
            }
            y2 -= y1;
            y1 -= y3;
            y3 = scanOffsets[y3];
            while (--y1 >= 0) {
                drawTexturedScanline317(Rasterizer2D.pixels, texels, y3, x3 >> 16, x2 >> 16, c3 >> 8, c2 >> 8, l4, k5,
                        j6, i5, l5, k6, z3, depthSlope);
                z3 += depthScale;
                x2 += k7;
                x3 += i8;
                c2 += l7;
                c3 += j8;
                y3 += Rasterizer2D.width;
                l4 += j5;
                k5 += i6;
                j6 += l6;
            }
            while (--y2 >= 0) {
                drawTexturedScanline317(Rasterizer2D.pixels, texels, y3, x1 >> 16, x2 >> 16, c1 >> 8, c2 >> 8, l4, k5,
                        j6, i5, l5, k6, z3, depthSlope);
                z3 += depthScale;
                x2 += k7;
                x1 += i7;
                c2 += l7;
                c1 += j7;
                y3 += Rasterizer2D.width;
                l4 += j5;
                k5 += i6;
                j6 += l6;
            }
            return;
        }
        x1 = x3 <<= 16;
        c1 = c3 <<= 16;
        if (y3 < 0) {
            y3 -= 0;
            x1 -= k7 * y3;
            z3 -= depthScale * y3;
            x3 -= i8 * y3;
            c1 -= l7 * y3;
            c3 -= j8 * y3;
            y3 = 0;
        }
        x2 <<= 16;
        c2 <<= 16;
        if (y2 < 0) {
            y2 -= 0;
            x2 -= i7 * y2;
            c2 -= j7 * y2;
            y2 = 0;
        }
        int l9 = y3 - originViewY;
        l4 += j5 * l9;
        k5 += i6 * l9;
        j6 += l6 * l9;
        if (k7 < i8) {
            y1 -= y2;
            y2 -= y3;
            y3 = scanOffsets[y3];
            while (--y2 >= 0) {
                drawTexturedScanline317(Rasterizer2D.pixels, texels, y3, x1 >> 16, x3 >> 16, c1 >> 8, c3 >> 8, l4, k5,
                        j6, i5, l5, k6, z3, depthSlope);
                z3 += depthScale;
                x1 += k7;
                x3 += i8;
                c1 += l7;
                c3 += j8;
                y3 += Rasterizer2D.width;
                l4 += j5;
                k5 += i6;
                j6 += l6;
            }
            while (--y1 >= 0) {
                drawTexturedScanline317(Rasterizer2D.pixels, texels, y3, x2 >> 16, x3 >> 16, c2 >> 8, c3 >> 8, l4, k5,
                        j6, i5, l5, k6, z3, depthSlope);
                z3 += depthScale;
                x2 += i7;
                x3 += i8;
                c2 += j7;
                c3 += j8;
                y3 += Rasterizer2D.width;
                l4 += j5;
                k5 += i6;
                j6 += l6;
            }
            return;
        }
        y1 -= y2;
        y2 -= y3;
        y3 = scanOffsets[y3];
        while (--y2 >= 0) {
            drawTexturedScanline317(Rasterizer2D.pixels, texels, y3, x3 >> 16, x1 >> 16, c3 >> 8, c1 >> 8, l4, k5, j6,
                    i5, l5, k6, z3, depthSlope);
            z3 += depthScale;
            x1 += k7;
            x3 += i8;
            c1 += l7;
            c3 += j8;
            y3 += Rasterizer2D.width;
            l4 += j5;
            k5 += i6;
            j6 += l6;
        }
        while (--y1 >= 0) {
            drawTexturedScanline317(Rasterizer2D.pixels, texels, y3, x3 >> 16, x2 >> 16, c3 >> 8, c2 >> 8, l4, k5, j6,
                    i5, l5, k6, z3, depthSlope);
            z3 += depthScale;
            x2 += i7;
            x3 += i8;
            c2 += j7;
            c3 += j8;
            y3 += Rasterizer2D.width;
            l4 += j5;
            k5 += i6;
            j6 += l6;
        }
    }

    private static void drawTexturedScanline317(int[] dest, int[] src, int offset, int x1, int x2, int hsl1, int hsl2,
                                                int t1, int t2, int t3, int t4, int t5, int t6, float z1, float z2) {
        int rgb = 0;
        int pos = 0;
        if (x1 >= x2) {
            return;
        }
        int rotation;
        int n;
        if (textureOutOfDrawingBounds) {
            rotation = (hsl2 - hsl1) / (x2 - x1);
            if (x2 > Rasterizer2D.lastX) {
                x2 = Rasterizer2D.lastX;
            }
            if (x1 < 0) {
                x1 -= 0;
                hsl1 -= x1 * rotation;
                x1 = 0;
            }
            if (x1 >= x2) {
                return;
            }
            n = x2 - x1 >> 3;
            rotation <<= 12;
            hsl1 <<= 9;
        } else {
            if (x2 - x1 > 7) {
                n = x2 - x1 >> 3;
                rotation = (hsl2 - hsl1) * anIntArray1468[n] >> 6;
            } else {
                n = 0;
                rotation = 0;
            }
            hsl1 <<= 9;
        }
        offset += x1;
        z1 += z2 * x1;
        int j4 = 0;
        int l4 = 0;
        int l6 = x1 - originViewX;
        t1 += (t4 >> 3) * l6;
        t2 += (t5 >> 3) * l6;
        t3 += (t6 >> 3) * l6;
        int l5 = t3 >> 14;
        if (l5 != 0) {
            rgb = t1 / l5;
            pos = t2 / l5;
            if (rgb < 0) {
                rgb = 0;
            } else if (rgb > 16256) {
                rgb = 16256;
            }
        }
        t1 += t4;
        t2 += t5;
        t3 += t6;
        l5 = t3 >> 14;
        if (l5 != 0) {
            j4 = t1 / l5;
            l4 = t2 / l5;
            if (j4 < 7) {
                j4 = 7;
            } else if (j4 > 16256) {
                j4 = 16256;
            }
        }
        int pixelBrightness = j4 - rgb >> 3;
        int pixelPos = l4 - pos >> 3;
        rgb += hsl1 & 0x600000;
        int brightness = hsl1 >> 23;
        if (aBoolean1463) {
            while (n-- > 0) {
                dest[offset] = src[(pos & 0x3f80) + (rgb >> 7)] >>> brightness;
                if (saveDepth) {
                    depthBuffer[offset] = z1;
                }
                offset++;
                z1 += z2;
                rgb += pixelBrightness;
                pos += pixelPos;
                dest[offset] = src[(pos & 0x3f80) + (rgb >> 7)] >>> brightness;
                if (saveDepth) {
                    depthBuffer[offset] = z1;
                }
                offset++;
                z1 += z2;
                rgb += pixelBrightness;
                pos += pixelPos;
                dest[offset] = src[(pos & 0x3f80) + (rgb >> 7)] >>> brightness;
                if (saveDepth) {
                    depthBuffer[offset] = z1;
                }
                offset++;
                z1 += z2;
                rgb += pixelBrightness;
                pos += pixelPos;
                dest[offset] = src[(pos & 0x3f80) + (rgb >> 7)] >>> brightness;
                if (saveDepth) {
                    depthBuffer[offset] = z1;
                }
                offset++;
                z1 += z2;
                rgb += pixelBrightness;
                pos += pixelPos;
                dest[offset] = src[(pos & 0x3f80) + (rgb >> 7)] >>> brightness;
                if (saveDepth) {
                    depthBuffer[offset] = z1;
                }
                offset++;
                z1 += z2;
                rgb += pixelBrightness;
                pos += pixelPos;
                dest[offset] = src[(pos & 0x3f80) + (rgb >> 7)] >>> brightness;
                if (saveDepth) {
                    depthBuffer[offset] = z1;
                }
                offset++;
                z1 += z2;
                rgb += pixelBrightness;
                pos += pixelPos;
                dest[offset] = src[(pos & 0x3f80) + (rgb >> 7)] >>> brightness;
                if (saveDepth) {
                    depthBuffer[offset] = z1;
                }
                offset++;
                z1 += z2;
                rgb += pixelBrightness;
                pos += pixelPos;
                dest[offset] = src[(pos & 0x3f80) + (rgb >> 7)] >>> brightness;
                if (saveDepth) {
                    depthBuffer[offset] = z1;
                }
                offset++;
                z1 += z2;
                rgb = j4;
                pos = l4;
                t1 += t4;
                t2 += t5;
                t3 += t6;
                final int i6 = t3 >> 14;
                if (i6 != 0) {
                    j4 = t1 / i6;
                    l4 = t2 / i6;
                    if (j4 < 7) {
                        j4 = 7;
                    } else if (j4 > 16256) {
                        j4 = 16256;
                    }
                }
                pixelBrightness = j4 - rgb >> 3;
                pixelPos = l4 - pos >> 3;
                hsl1 += rotation;
                rgb += hsl1 & 0x600000;
                brightness = hsl1 >> 23;
            }
            for (n = x2 - x1 & 7; n-- > 0;) {
                dest[offset] = src[(pos & 0x3f80) + (rgb >> 7)] >>> brightness;
                if (saveDepth) {
                    depthBuffer[offset] = z1;
                }
                z1 += z2;
                offset++;
                rgb += pixelBrightness;
                pos += pixelPos;
            }
            return;
        }
        while (n-- > 0) {
            int color;
            if ((color = src[(pos & 0x3f80) + (rgb >> 7)] >>> brightness) != 0) {
                dest[offset] = color;
                if (saveDepth) {
                    depthBuffer[offset] = z1;
                }
            }
            z1 += z2;
            offset++;
            rgb += pixelBrightness;
            pos += pixelPos;
            if ((color = src[(pos & 0x3f80) + (rgb >> 7)] >>> brightness) != 0) {
                dest[offset] = color;
                if (saveDepth) {
                    depthBuffer[offset] = z1;
                }
            }
            z1 += z2;
            offset++;
            rgb += pixelBrightness;
            pos += pixelPos;
            if ((color = src[(pos & 0x3f80) + (rgb >> 7)] >>> brightness) != 0) {
                dest[offset] = color;
                if (saveDepth) {
                    depthBuffer[offset] = z1;
                }
            }
            z1 += z2;
            offset++;
            rgb += pixelBrightness;
            pos += pixelPos;
            if ((color = src[(pos & 0x3f80) + (rgb >> 7)] >>> brightness) != 0) {
                dest[offset] = color;
                if (saveDepth) {
                    depthBuffer[offset] = z1;
                }
            }
            z1 += z2;
            offset++;
            rgb += pixelBrightness;
            pos += pixelPos;
            if ((color = src[(pos & 0x3f80) + (rgb >> 7)] >>> brightness) != 0) {
                dest[offset] = color;
                if (saveDepth) {
                    depthBuffer[offset] = z1;
                }
            }
            z1 += z2;
            offset++;
            rgb += pixelBrightness;
            pos += pixelPos;
            if ((color = src[(pos & 0x3f80) + (rgb >> 7)] >>> brightness) != 0) {
                dest[offset] = color;
                if (saveDepth) {
                    depthBuffer[offset] = z1;
                }
            }
            z1 += z2;
            offset++;
            rgb += pixelBrightness;
            pos += pixelPos;
            if ((color = src[(pos & 0x3f80) + (rgb >> 7)] >>> brightness) != 0) {
                dest[offset] = color;
                if (saveDepth) {
                    depthBuffer[offset] = z1;
                }
            }
            z1 += z2;
            offset++;
            rgb += pixelBrightness;
            pos += pixelPos;
            if ((color = src[(pos & 0x3f80) + (rgb >> 7)] >>> brightness) != 0) {
                dest[offset] = color;
                if (saveDepth) {
                    depthBuffer[offset] = z1;
                }
            }
            z1 += z2;
            offset++;
            rgb = j4;
            pos = l4;
            t1 += t4;
            t2 += t5;
            t3 += t6;
            final int j6 = t3 >> 14;
            if (j6 != 0) {
                j4 = t1 / j6;
                l4 = t2 / j6;
                if (j4 < 7) {
                    j4 = 7;
                } else if (j4 > 16256) {
                    j4 = 16256;
                }
            }
            pixelBrightness = j4 - rgb >> 3;
            pixelPos = l4 - pos >> 3;
            hsl1 += rotation;
            rgb += hsl1 & 0x600000;
            brightness = hsl1 >> 23;
        }
        for (int l3 = x2 - x1 & 7; l3-- > 0;) {
            int color;
            if ((color = src[(pos & 0x3f80) + (rgb >> 7)] >>> brightness) != 0) {
                dest[offset] = color;
                if (saveDepth) {
                    depthBuffer[offset] = z1;
                }
            }
            z1 += z2;
            offset++;
            rgb += pixelBrightness;
            pos += pixelPos;
        }
    }

    public static void drawTexturedTriangle(int y_a, int y_b, int y_c, int x_a, int x_b, int x_c,
                                            int k1, int l1, int i2, int Px, int Mx, int Nx, int Pz, int Mz, int Nz, int Py, int My,
                                            int Ny, int k4, float z_a, float z_b, float z_c) {
        if (z_a < 0 || z_b < 0 || z_c < 0) {
            return;
        }
        int texture[] = getTexturePixels(k4);
        aBoolean1463 = !textureIsTransparant[k4];
        Mx = Px - Mx;
        Mz = Pz - Mz;
        My = Py - My;
        Nx -= Px;
        Nz -= Pz;
        Ny -= Py;
        int Oa = (Nx * Pz - Nz * Px) * WorldController.focalLength << 5;
        int Ha = Nz * Py - Ny * Pz << 8;
        int Va = Ny * Px - Nx * Py << 5;

        int Ob = (Mx * Pz - Mz * Px) * WorldController.focalLength << 5;
        int Hb = Mz * Py - My * Pz << 8;
        int Vb = My * Px - Mx * Py << 5;

        int Oc = (Mz * Nx - Mx * Nz) * WorldController.focalLength << 5;
        int Hc = My * Nz - Mz * Ny << 8;
        int Vc = Mx * Ny - My * Nx << 5;
        int a_to_b = 0;
        int grad_a_off = 0;
        if (y_b != y_a) {
            a_to_b = (x_b - x_a << 16) / (y_b - y_a);
            grad_a_off = (l1 - k1 << 16) / (y_b - y_a);
        }
        int b_to_c = 0;
        int grad_b_off = 0;
        if (y_c != y_b) {
            b_to_c = (x_c - x_b << 16) / (y_c - y_b);
            grad_b_off = (i2 - l1 << 16) / (y_c - y_b);
        }
        int c_to_a = 0;
        int grad_c_off = 0;
        if (y_c != y_a) {
            c_to_a = (x_a - x_c << 16) / (y_a - y_c);
            grad_c_off = (k1 - i2 << 16) / (y_a - y_c);
        }
        float b_aX = x_b - x_a;
        float b_aY = y_b - y_a;
        float c_aX = x_c - x_a;
        float c_aY = y_c - y_a;
        float b_aZ = z_b - z_a;
        float c_aZ = z_c - z_a;

        float div = b_aX * c_aY - c_aX * b_aY;
        float depth_slope = (b_aZ * c_aY - c_aZ * b_aY) / div;
        float depth_increment = (c_aZ * b_aX - b_aZ * c_aX) / div;
        if (y_a <= y_b && y_a <= y_c) {
            if (y_a >= Rasterizer2D.clip_bottom) {
                return;
            }
            if (y_b > Rasterizer2D.clip_bottom) {
                y_b = Rasterizer2D.clip_bottom;
            }
            if (y_c > Rasterizer2D.clip_bottom) {
                y_c = Rasterizer2D.clip_bottom;
            }
            z_a = z_a - depth_slope * x_a + depth_slope;
            if (y_b < y_c) {
                x_c = x_a <<= 16;
                i2 = k1 <<= 16;
                if (y_a < 0) {
                    x_c -= c_to_a * y_a;
                    x_a -= a_to_b * y_a;
                    z_a -= depth_increment * y_a;
                    i2 -= grad_c_off * y_a;
                    k1 -= grad_a_off * y_a;
                    y_a = 0;
                }
                x_b <<= 16;
                l1 <<= 16;
                if (y_b < 0) {
                    x_b -= b_to_c * y_b;
                    l1 -= grad_b_off * y_b;
                    y_b = 0;
                }
                int k8 = y_a - originViewY;
                Oa += Va * k8;
                Ob += Vb * k8;
                Oc += Vc * k8;
                if (y_a != y_b && c_to_a < a_to_b || y_a == y_b && c_to_a > b_to_c) {
                    y_c -= y_b;
                    y_b -= y_a;
                    y_a = scanOffsets[y_a];
                    while (--y_b >= 0) {
                        drawTexturedScanline(Rasterizer2D.pixels, texture, y_a, x_c >> 16, x_a >> 16, i2 >> 8,
                                k1 >> 8, Oa, Ob, Oc, Ha, Hb, Hc, z_a, depth_slope);
                        x_c += c_to_a;
                        x_a += a_to_b;
                        z_a += depth_increment;
                        i2 += grad_c_off;
                        k1 += grad_a_off;
                        y_a += Rasterizer2D.width;
                        Oa += Va;
                        Ob += Vb;
                        Oc += Vc;
                    }
                    while (--y_c >= 0) {
                        drawTexturedScanline(Rasterizer2D.pixels, texture, y_a, x_c >> 16, x_b >> 16, i2 >> 8,
                                l1 >> 8, Oa, Ob, Oc, Ha, Hb, Hc, z_a, depth_slope);
                        x_c += c_to_a;
                        x_b += b_to_c;
                        z_a += depth_increment;
                        i2 += grad_c_off;
                        l1 += grad_b_off;
                        y_a += Rasterizer2D.width;
                        Oa += Va;
                        Ob += Vb;
                        Oc += Vc;
                    }
                    return;
                }
                y_c -= y_b;
                y_b -= y_a;
                y_a = scanOffsets[y_a];
                while (--y_b >= 0) {
                    drawTexturedScanline(Rasterizer2D.pixels, texture, y_a, x_a >> 16, x_c >> 16, k1 >> 8,
                            i2 >> 8, Oa, Ob, Oc, Ha, Hb, Hc, z_a, depth_slope);
                    x_c += c_to_a;
                    x_a += a_to_b;
                    z_a += depth_increment;
                    i2 += grad_c_off;
                    k1 += grad_a_off;
                    y_a += Rasterizer2D.width;
                    Oa += Va;
                    Ob += Vb;
                    Oc += Vc;
                }
                while (--y_c >= 0) {
                    drawTexturedScanline(Rasterizer2D.pixels, texture, y_a, x_b >> 16, x_c >> 16, l1 >> 8,
                            i2 >> 8, Oa, Ob, Oc, Ha, Hb, Hc, z_a, depth_slope);
                    x_c += c_to_a;
                    x_b += b_to_c;
                    z_a += depth_increment;
                    i2 += grad_c_off;
                    l1 += grad_b_off;
                    y_a += Rasterizer2D.width;
                    Oa += Va;
                    Ob += Vb;
                    Oc += Vc;
                }
                return;
            }
            x_b = x_a <<= 16;
            l1 = k1 <<= 16;
            if (y_a < 0) {
                x_b -= c_to_a * y_a;
                x_a -= a_to_b * y_a;
                z_a -= depth_increment * y_a;
                l1 -= grad_c_off * y_a;
                k1 -= grad_a_off * y_a;
                y_a = 0;
            }
            x_c <<= 16;
            i2 <<= 16;
            if (y_c < 0) {
                x_c -= b_to_c * y_c;
                i2 -= grad_b_off * y_c;
                y_c = 0;
            }
            int l8 = y_a - originViewY;
            Oa += Va * l8;
            Ob += Vb * l8;
            Oc += Vc * l8;
            if (y_a != y_c && c_to_a < a_to_b || y_a == y_c && b_to_c > a_to_b) {
                y_b -= y_c;
                y_c -= y_a;
                y_a = scanOffsets[y_a];
                while (--y_c >= 0) {
                    drawTexturedScanline(Rasterizer2D.pixels, texture, y_a, x_b >> 16, x_a >> 16, l1 >> 8,
                            k1 >> 8, Oa, Ob, Oc, Ha, Hb, Hc, z_a, depth_slope);
                    x_b += c_to_a;
                    x_a += a_to_b;
                    l1 += grad_c_off;
                    k1 += grad_a_off;
                    z_a += depth_increment;
                    y_a += Rasterizer2D.width;
                    Oa += Va;
                    Ob += Vb;
                    Oc += Vc;
                }
                while (--y_b >= 0) {
                    drawTexturedScanline(Rasterizer2D.pixels, texture, y_a, x_c >> 16, x_a >> 16, i2 >> 8,
                            k1 >> 8, Oa, Ob, Oc, Ha, Hb, Hc, z_a, depth_slope);
                    x_c += b_to_c;
                    x_a += a_to_b;
                    i2 += grad_b_off;
                    k1 += grad_a_off;
                    z_a += depth_increment;
                    y_a += Rasterizer2D.width;
                    Oa += Va;
                    Ob += Vb;
                    Oc += Vc;
                }
                return;
            }
            y_b -= y_c;
            y_c -= y_a;
            y_a = scanOffsets[y_a];
            while (--y_c >= 0) {
                drawTexturedScanline(Rasterizer2D.pixels, texture, y_a, x_a >> 16, x_b >> 16, k1 >> 8,
                        l1 >> 8, Oa, Ob, Oc, Ha, Hb, Hc, z_a, depth_slope);
                x_b += c_to_a;
                x_a += a_to_b;
                l1 += grad_c_off;
                k1 += grad_a_off;
                z_a += depth_increment;
                y_a += Rasterizer2D.width;
                Oa += Va;
                Ob += Vb;
                Oc += Vc;
            }
            while (--y_b >= 0) {
                drawTexturedScanline(Rasterizer2D.pixels, texture, y_a, x_a >> 16, x_c >> 16, k1 >> 8,
                        i2 >> 8, Oa, Ob, Oc, Ha, Hb, Hc, z_a, depth_slope);
                x_c += b_to_c;
                x_a += a_to_b;
                i2 += grad_b_off;
                k1 += grad_a_off;
                z_a += depth_increment;
                y_a += Rasterizer2D.width;
                Oa += Va;
                Ob += Vb;
                Oc += Vc;
            }
            return;
        }
        if (y_b <= y_c) {
            if (y_b >= Rasterizer2D.clip_bottom) {
                return;
            }
            if (y_c > Rasterizer2D.clip_bottom) {
                y_c = Rasterizer2D.clip_bottom;
            }
            if (y_a > Rasterizer2D.clip_bottom) {
                y_a = Rasterizer2D.clip_bottom;
            }
            z_b = z_b - depth_slope * x_b + depth_slope;
            if (y_c < y_a) {
                x_a = x_b <<= 16;
                k1 = l1 <<= 16;
                if (y_b < 0) {
                    x_a -= a_to_b * y_b;
                    x_b -= b_to_c * y_b;
                    z_b -= depth_increment * y_b;
                    k1 -= grad_a_off * y_b;
                    l1 -= grad_b_off * y_b;
                    y_b = 0;
                }
                x_c <<= 16;
                i2 <<= 16;
                if (y_c < 0) {
                    x_c -= c_to_a * y_c;
                    i2 -= grad_c_off * y_c;
                    y_c = 0;
                }
                int i9 = y_b - originViewY;
                Oa += Va * i9;
                Ob += Vb * i9;
                Oc += Vc * i9;
                if (y_b != y_c && a_to_b < b_to_c || y_b == y_c && a_to_b > c_to_a) {
                    y_a -= y_c;
                    y_c -= y_b;
                    y_b = scanOffsets[y_b];
                    while (--y_c >= 0) {
                        drawTexturedScanline(Rasterizer2D.pixels, texture, y_b, x_a >> 16, x_b >> 16, k1 >> 8,
                                l1 >> 8, Oa, Ob, Oc, Ha, Hb, Hc, z_b, depth_slope);
                        x_a += a_to_b;
                        x_b += b_to_c;
                        k1 += grad_a_off;
                        l1 += grad_b_off;
                        z_b += depth_increment;
                        y_b += Rasterizer2D.width;
                        Oa += Va;
                        Ob += Vb;
                        Oc += Vc;
                    }
                    while (--y_a >= 0) {
                        drawTexturedScanline(Rasterizer2D.pixels, texture, y_b, x_a >> 16, x_c >> 16, k1 >> 8,
                                i2 >> 8, Oa, Ob, Oc, Ha, Hb, Hc, z_b, depth_slope);
                        x_a += a_to_b;
                        x_c += c_to_a;
                        k1 += grad_a_off;
                        i2 += grad_c_off;
                        z_b += depth_increment;
                        y_b += Rasterizer2D.width;
                        Oa += Va;
                        Ob += Vb;
                        Oc += Vc;
                    }
                    return;
                }
                y_a -= y_c;
                y_c -= y_b;
                y_b = scanOffsets[y_b];
                while (--y_c >= 0) {
                    drawTexturedScanline(Rasterizer2D.pixels, texture, y_b, x_b >> 16, x_a >> 16, l1 >> 8,
                            k1 >> 8, Oa, Ob, Oc, Ha, Hb, Hc, z_b, depth_slope);
                    x_a += a_to_b;
                    x_b += b_to_c;
                    k1 += grad_a_off;
                    l1 += grad_b_off;
                    z_b += depth_increment;
                    y_b += Rasterizer2D.width;
                    Oa += Va;
                    Ob += Vb;
                    Oc += Vc;
                }
                while (--y_a >= 0) {
                    drawTexturedScanline(Rasterizer2D.pixels, texture, y_b, x_c >> 16, x_a >> 16, i2 >> 8,
                            k1 >> 8, Oa, Ob, Oc, Ha, Hb, Hc, z_b, depth_slope);
                    x_a += a_to_b;
                    x_c += c_to_a;
                    k1 += grad_a_off;
                    i2 += grad_c_off;
                    z_b += depth_increment;
                    y_b += Rasterizer2D.width;
                    Oa += Va;
                    Ob += Vb;
                    Oc += Vc;
                }
                return;
            }
            x_c = x_b <<= 16;
            i2 = l1 <<= 16;
            if (y_b < 0) {
                x_c -= a_to_b * y_b;
                x_b -= b_to_c * y_b;
                z_b -= depth_increment * y_b;
                i2 -= grad_a_off * y_b;
                l1 -= grad_b_off * y_b;
                y_b = 0;
            }
            x_a <<= 16;
            k1 <<= 16;
            if (y_a < 0) {
                x_a -= c_to_a * y_a;
                k1 -= grad_c_off * y_a;
                y_a = 0;
            }
            int j9 = y_b - originViewY;
            Oa += Va * j9;
            Ob += Vb * j9;
            Oc += Vc * j9;
            if (a_to_b < b_to_c) {
                y_c -= y_a;
                y_a -= y_b;
                y_b = scanOffsets[y_b];
                while (--y_a >= 0) {
                    drawTexturedScanline(Rasterizer2D.pixels, texture, y_b, x_c >> 16, x_b >> 16, i2 >> 8,
                            l1 >> 8, Oa, Ob, Oc, Ha, Hb, Hc, z_b, depth_slope);
                    x_c += a_to_b;
                    x_b += b_to_c;
                    i2 += grad_a_off;
                    l1 += grad_b_off;
                    z_b += depth_increment;
                    y_b += Rasterizer2D.width;
                    Oa += Va;
                    Ob += Vb;
                    Oc += Vc;
                }
                while (--y_c >= 0) {
                    drawTexturedScanline(Rasterizer2D.pixels, texture, y_b, x_a >> 16, x_b >> 16, k1 >> 8,
                            l1 >> 8, Oa, Ob, Oc, Ha, Hb, Hc, z_b, depth_slope);
                    x_a += c_to_a;
                    x_b += b_to_c;
                    k1 += grad_c_off;
                    l1 += grad_b_off;
                    z_b += depth_increment;
                    y_b += Rasterizer2D.width;
                    Oa += Va;
                    Ob += Vb;
                    Oc += Vc;
                }
                return;
            }
            y_c -= y_a;
            y_a -= y_b;
            y_b = scanOffsets[y_b];
            while (--y_a >= 0) {
                drawTexturedScanline(Rasterizer2D.pixels, texture, y_b, x_b >> 16, x_c >> 16, l1 >> 8,
                        i2 >> 8, Oa, Ob, Oc, Ha, Hb, Hc, z_b, depth_slope);
                x_c += a_to_b;
                x_b += b_to_c;
                i2 += grad_a_off;
                l1 += grad_b_off;
                z_b += depth_increment;
                y_b += Rasterizer2D.width;
                Oa += Va;
                Ob += Vb;
                Oc += Vc;
            }
            while (--y_c >= 0) {
                drawTexturedScanline(Rasterizer2D.pixels, texture, y_b, x_b >> 16, x_a >> 16, l1 >> 8,
                        k1 >> 8, Oa, Ob, Oc, Ha, Hb, Hc, z_b, depth_slope);
                x_a += c_to_a;
                x_b += b_to_c;
                k1 += grad_c_off;
                l1 += grad_b_off;
                z_b += depth_increment;
                y_b += Rasterizer2D.width;
                Oa += Va;
                Ob += Vb;
                Oc += Vc;
            }
            return;
        }
        if (y_c >= Rasterizer2D.clip_bottom) {
            return;
        }
        if (y_a > Rasterizer2D.clip_bottom) {
            y_a = Rasterizer2D.clip_bottom;
        }
        if (y_b > Rasterizer2D.clip_bottom) {
            y_b = Rasterizer2D.clip_bottom;
        }
        z_c = z_c - depth_slope * x_c + depth_slope;
        if (y_a < y_b) {
            x_b = x_c <<= 16;
            l1 = i2 <<= 16;
            if (y_c < 0) {
                x_b -= b_to_c * y_c;
                x_c -= c_to_a * y_c;
                z_c -= depth_increment * y_c;
                l1 -= grad_b_off * y_c;
                i2 -= grad_c_off * y_c;
                y_c = 0;
            }
            x_a <<= 16;
            k1 <<= 16;
            if (y_a < 0) {
                x_a -= a_to_b * y_a;
                k1 -= grad_a_off * y_a;
                y_a = 0;
            }
            int k9 = y_c - originViewY;
            Oa += Va * k9;
            Ob += Vb * k9;
            Oc += Vc * k9;
            if (b_to_c < c_to_a) {
                y_b -= y_a;
                y_a -= y_c;
                y_c = scanOffsets[y_c];
                while (--y_a >= 0) {
                    drawTexturedScanline(Rasterizer2D.pixels, texture, y_c, x_b >> 16, x_c >> 16, l1 >> 8,
                            i2 >> 8, Oa, Ob, Oc, Ha, Hb, Hc, z_c, depth_slope);
                    x_b += b_to_c;
                    x_c += c_to_a;
                    l1 += grad_b_off;
                    i2 += grad_c_off;
                    z_c += depth_increment;
                    y_c += Rasterizer2D.width;
                    Oa += Va;
                    Ob += Vb;
                    Oc += Vc;
                }
                while (--y_b >= 0) {
                    drawTexturedScanline(Rasterizer2D.pixels, texture, y_c, x_b >> 16, x_a >> 16, l1 >> 8,
                            k1 >> 8, Oa, Ob, Oc, Ha, Hb, Hc, z_c, depth_slope);
                    x_b += b_to_c;
                    x_a += a_to_b;
                    l1 += grad_b_off;
                    k1 += grad_a_off;
                    z_c += depth_increment;
                    y_c += Rasterizer2D.width;
                    Oa += Va;
                    Ob += Vb;
                    Oc += Vc;
                }
                return;
            }
            y_b -= y_a;
            y_a -= y_c;
            y_c = scanOffsets[y_c];
            while (--y_a >= 0) {
                drawTexturedScanline(Rasterizer2D.pixels, texture, y_c, x_c >> 16, x_b >> 16, i2 >> 8,
                        l1 >> 8, Oa, Ob, Oc, Ha, Hb, Hc, z_c, depth_slope);
                x_b += b_to_c;
                x_c += c_to_a;
                l1 += grad_b_off;
                i2 += grad_c_off;
                z_c += depth_increment;
                y_c += Rasterizer2D.width;
                Oa += Va;
                Ob += Vb;
                Oc += Vc;
            }
            while (--y_b >= 0) {
                drawTexturedScanline(Rasterizer2D.pixels, texture, y_c, x_a >> 16, x_b >> 16, k1 >> 8,
                        l1 >> 8, Oa, Ob, Oc, Ha, Hb, Hc, z_c, depth_slope);
                x_b += b_to_c;
                x_a += a_to_b;
                l1 += grad_b_off;
                k1 += grad_a_off;
                z_c += depth_increment;
                y_c += Rasterizer2D.width;
                Oa += Va;
                Ob += Vb;
                Oc += Vc;
            }
            return;
        }
        x_a = x_c <<= 16;
        k1 = i2 <<= 16;
        if (y_c < 0) {
            x_a -= b_to_c * y_c;
            x_c -= c_to_a * y_c;
            z_c -= depth_increment * y_c;
            k1 -= grad_b_off * y_c;
            i2 -= grad_c_off * y_c;
            y_c = 0;
        }
        x_b <<= 16;
        l1 <<= 16;
        if (y_b < 0) {
            x_b -= a_to_b * y_b;
            l1 -= grad_a_off * y_b;
            y_b = 0;
        }
        int l9 = y_c - originViewY;
        Oa += Va * l9;
        Ob += Vb * l9;
        Oc += Vc * l9;
        if (b_to_c < c_to_a) {
            y_a -= y_b;
            y_b -= y_c;
            y_c = scanOffsets[y_c];
            while (--y_b >= 0) {
                drawTexturedScanline(Rasterizer2D.pixels, texture, y_c, x_a >> 16, x_c >> 16, k1 >> 8,
                        i2 >> 8, Oa, Ob, Oc, Ha, Hb, Hc, z_c, depth_slope);
                x_a += b_to_c;
                x_c += c_to_a;
                k1 += grad_b_off;
                i2 += grad_c_off;
                z_c += depth_increment;
                y_c += Rasterizer2D.width;
                Oa += Va;
                Ob += Vb;
                Oc += Vc;
            }
            while (--y_a >= 0) {
                drawTexturedScanline(Rasterizer2D.pixels, texture, y_c, x_b >> 16, x_c >> 16, l1 >> 8,
                        i2 >> 8, Oa, Ob, Oc, Ha, Hb, Hc, z_c, depth_slope);
                x_b += a_to_b;
                x_c += c_to_a;
                l1 += grad_a_off;
                i2 += grad_c_off;
                z_c += depth_increment;
                y_c += Rasterizer2D.width;
                Oa += Va;
                Ob += Vb;
                Oc += Vc;
            }
            return;
        }
        y_a -= y_b;
        y_b -= y_c;
        y_c = scanOffsets[y_c];
        while (--y_b >= 0) {
            drawTexturedScanline(Rasterizer2D.pixels, texture, y_c, x_c >> 16, x_a >> 16, i2 >> 8,
                    k1 >> 8, Oa, Ob, Oc, Ha, Hb, Hc, z_c, depth_slope);
            x_a += b_to_c;
            x_c += c_to_a;
            k1 += grad_b_off;
            i2 += grad_c_off;
            z_c += depth_increment;
            y_c += Rasterizer2D.width;
            Oa += Va;
            Ob += Vb;
            Oc += Vc;
        }
        while (--y_a >= 0) {
            drawTexturedScanline(Rasterizer2D.pixels, texture, y_c, x_c >> 16, x_b >> 16, i2 >> 8,
                    l1 >> 8, Oa, Ob, Oc, Ha, Hb, Hc, z_c, depth_slope);
            x_b += a_to_b;
            x_c += c_to_a;
            l1 += grad_a_off;
            i2 += grad_c_off;
            z_c += depth_increment;
            y_c += Rasterizer2D.width;
            Oa += Va;
            Ob += Vb;
            Oc += Vc;
        }
    }

    public static void drawTexturedScanline(int dest[], int texture[], int dest_off, int start_x,
                                            int end_x, int shadeValue, int gradient, int l1, int i2, int j2, int k2, int l2, int i3,
                                            float depth, float depth_slope) {
        int rgb = 0;
        int loops = 0;
        if (start_x >= end_x) {
            return;
        }
        int j3;
        int k3;
        if (textureOutOfDrawingBounds) {
            j3 = (gradient - shadeValue) / (end_x - start_x);
            if (end_x > Rasterizer2D.lastX) {
                end_x = Rasterizer2D.lastX;
            }
            if (start_x < 0) {
                shadeValue -= start_x * j3;
                start_x = 0;
            }
            if (start_x >= end_x) {
                return;
            }
            k3 = end_x - start_x >> 3;
            j3 <<= 12;
            shadeValue <<= 9;
        } else {
            if (end_x - start_x > 7) {
                k3 = end_x - start_x >> 3;
                j3 = (gradient - shadeValue) * anIntArray1468[k3] >> 6;
            } else {
                k3 = 0;
                j3 = 0;
            }
            shadeValue <<= 9;
        }
        dest_off += start_x;
        depth += depth_slope * start_x;
        if (lowMem) {
            int i4 = 0;
            int k4 = 0;
            int k6 = start_x - originViewX;
            l1 += (k2 >> 3) * k6;
            i2 += (l2 >> 3) * k6;
            j2 += (i3 >> 3) * k6;
            int i5 = j2 >> 12;
            if (i5 != 0) {
                rgb = l1 / i5;
                loops = i2 / i5;
                if (rgb < 0) {
                    rgb = 0;
                } else if (rgb > 4032) {
                    rgb = 4032;
                }
            }
            l1 += k2;
            i2 += l2;
            j2 += i3;
            i5 = j2 >> 12;
            if (i5 != 0) {
                i4 = l1 / i5;
                k4 = i2 / i5;
                if (i4 < 7) {
                    i4 = 7;
                } else if (i4 > 4032) {
                    i4 = 4032;
                }
            }
            int i7 = i4 - rgb >> 3;
            int k7 = k4 - loops >> 3;
            rgb += (shadeValue & 0x600000) >> 3;
            int i8 = shadeValue >> 23;
            if (aBoolean1463) {
                while (k3-- > 0) {
                    for (int i = 0; i < 8; i++) {
                        if (true) {
                            drawAlpha(
                                    dest,
                                    dest_off,
                                    texture[(loops & 0xfc0) + (rgb >> 6)] >>> i8,
                                    255);
                            Rasterizer2D.depthBuffer[dest_off] = depth;
                        }
                        dest_off++;
                        depth += depth_slope;
                        rgb += i7;
                        loops += k7;
                    }
                    rgb = i4;
                    loops = k4;
                    l1 += k2;
                    i2 += l2;
                    j2 += i3;
                    int j5 = j2 >> 12;
                    if (j5 != 0) {
                        i4 = l1 / j5;
                        k4 = i2 / j5;
                        if (i4 < 7) {
                            i4 = 7;
                        } else if (i4 > 4032) {
                            i4 = 4032;
                        }
                    }
                    i7 = i4 - rgb >> 3;
                    k7 = k4 - loops >> 3;
                    shadeValue += j3;
                    rgb += (shadeValue & 0x600000) >> 3;
                    i8 = shadeValue >> 23;
                }
                for (k3 = end_x - start_x & 7; k3-- > 0;) {
                    if (true) {
                        drawAlpha(
                                dest,
                                dest_off,
                                texture[(loops & 0xfc0) + (rgb >> 6)] >>> i8,
                                255);
                        Rasterizer2D.depthBuffer[dest_off] = depth;
                    }
                    dest_off++;
                    depth += depth_slope;
                    rgb += i7;
                    loops += k7;
                }

                return;
            }
            while (k3-- > 0) {
                int k8;
                for (int i = 0; i < 8; i++) {
                    if ((k8 = texture[(loops & 0xfc0) + (rgb >> 6)] >>> i8) != 0) {
                        drawAlpha(
                                dest,
                                dest_off,
                                k8,
                                255);
                        Rasterizer2D.depthBuffer[dest_off] = depth;
                    }
                    dest_off++;
                    depth += depth_slope;
                    rgb += i7;
                    loops += k7;
                }

                rgb = i4;
                loops = k4;
                l1 += k2;
                i2 += l2;
                j2 += i3;
                int k5 = j2 >> 12;
                if (k5 != 0) {
                    i4 = l1 / k5;
                    k4 = i2 / k5;
                    if (i4 < 7) {
                        i4 = 7;
                    } else if (i4 > 4032) {
                        i4 = 4032;
                    }
                }
                i7 = i4 - rgb >> 3;
                k7 = k4 - loops >> 3;
                shadeValue += j3;
                rgb += (shadeValue & 0x600000) >> 3;
                i8 = shadeValue >> 23;
            }
            for (k3 = end_x - start_x & 7; k3-- > 0;) {
                int l8;
                if ((l8 = texture[(loops & 0xfc0) + (rgb >> 6)] >>> i8) != 0) {
                    drawAlpha(
                            dest,
                            dest_off,
                            l8,
                            255);
                    Rasterizer2D.depthBuffer[dest_off] = depth;
                }
                dest_off++;
                depth += depth_slope;
                rgb += i7;
                loops += k7;
            }

            return;
        }
        int j4 = 0;
        int l4 = 0;
        int l6 = start_x - originViewX;
        l1 += (k2 >> 3) * l6;
        i2 += (l2 >> 3) * l6;
        j2 += (i3 >> 3) * l6;
        int l5 = j2 >> 14;
        if (l5 != 0) {
            rgb = l1 / l5;
            loops = i2 / l5;
            if (rgb < 0) {
                rgb = 0;
            } else if (rgb > 16256) {
                rgb = 16256;
            }
        }
        l1 += k2;
        i2 += l2;
        j2 += i3;
        l5 = j2 >> 14;
        if (l5 != 0) {
            j4 = l1 / l5;
            l4 = i2 / l5;
            if (j4 < 7) {
                j4 = 7;
            } else if (j4 > 16256) {
                j4 = 16256;
            }
        }
        int j7 = j4 - rgb >> 3;
        int l7 = l4 - loops >> 3;
        rgb += shadeValue & 0x600000;
        int j8 = shadeValue >> 23;
        if (aBoolean1463) {
            while (k3-- > 0) {
                for (int i = 0; i < 8; i++) {
                    if (true) {
                        drawAlpha(
                                dest,
                                dest_off,
                                texture[(loops & 0x3f80) + (rgb >> 7)] >>> j8,
                                255);
                        Rasterizer2D.depthBuffer[dest_off] = depth;
                    }
                    depth += depth_slope;
                    dest_off++;
                    rgb += j7;
                    loops += l7;
                }
                rgb = j4;
                loops = l4;
                l1 += k2;
                i2 += l2;
                j2 += i3;
                int i6 = j2 >> 14;
                if (i6 != 0) {
                    j4 = l1 / i6;
                    l4 = i2 / i6;
                    if (j4 < 7) {
                        j4 = 7;
                    } else if (j4 > 16256) {
                        j4 = 16256;
                    }
                }
                j7 = j4 - rgb >> 3;
                l7 = l4 - loops >> 3;
                shadeValue += j3;
                rgb += shadeValue & 0x600000;
                j8 = shadeValue >> 23;
            }
            for (k3 = end_x - start_x & 7; k3-- > 0;) {
                if (true) {
                    drawAlpha(
                            dest,
                            dest_off,
                            texture[(loops & 0x3f80) + (rgb >> 7)] >>> j8,
                            255);
                    Rasterizer2D.depthBuffer[dest_off] = depth;
                }
                dest_off++;
                depth += depth_slope;
                rgb += j7;
                loops += l7;
            }

            return;
        }
        while (k3-- > 0) {
            int i9;
            for (int i = 0; i < 8; i++) {
                if ((i9 = texture[(loops & 0x3f80) + (rgb >> 7)] >>> j8) != 0) {
                    drawAlpha(
                            dest,
                            dest_off,
                            i9,
                            255);
                    Rasterizer2D.depthBuffer[dest_off] = depth;
                }
                dest_off++;
                depth += depth_slope;
                rgb += j7;
                loops += l7;
            }
            rgb = j4;
            loops = l4;
            l1 += k2;
            i2 += l2;
            j2 += i3;
            int j6 = j2 >> 14;
            if (j6 != 0) {
                j4 = l1 / j6;
                l4 = i2 / j6;
                if (j4 < 7) {
                    j4 = 7;
                } else if (j4 > 16256) {
                    j4 = 16256;
                }
            }
            j7 = j4 - rgb >> 3;
            l7 = l4 - loops >> 3;
            shadeValue += j3;
            rgb += shadeValue & 0x600000;
            j8 = shadeValue >> 23;
        }
        for (int l3 = end_x - start_x & 7; l3-- > 0;) {
            int j9;
            if ((j9 = texture[(loops & 0x3f80) + (rgb >> 7)] >>> j8) != 0) {
                drawAlpha(
                        dest,
                        dest_off,
                        j9,
                        255);
                Rasterizer2D.depthBuffer[dest_off] = depth;
            }
            depth += depth_slope;
            dest_off++;
            rgb += j7;
            loops += l7;
        }
    }
}

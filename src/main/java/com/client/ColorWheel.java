package com.client;

import com.client.graphics.interfaces.RSInterface;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ColorWheel {
    private static final int RADIUS = 100;
    private static final int ADJUST = 60;

    private static int getCenterX() {
        return 260;
    }

    private static int getCenterY() {
        return 160;
    }

    public static void proccessInterfaceClick(int paddingX, int paddingY) {
        if(Client.openInterfaceID == 625) {
            Client client = Client.getInstance();
            int centerX = getCenterX();
            int centerY = getCenterY();
            if(client.clickX >= centerX - 100 && client.clickX <= 100 + centerX && client.clickY >= centerY - 100 && client.clickY <= centerY + 100) {
                RSInterface box = RSInterface.interfaceCache[632];
                int x = centerX - client.clickX;
                int y = centerY - client.clickY;
                int red = (int)(255.0D - Math.hypot((double)(100 - x), (double)(0 - y)));
                int green = (int)(255.0D - Math.hypot((double)(-65 - x), (double)(y - 75)));
                int blue = (int)(255.0D - Math.hypot((double)(-56 - x), (double)(-79 - y)));
                if(red >= 230) {
                    green -= 60;
                    blue -= 60;
                } else if(green >= 230) {
                    red -= 60;
                    blue -= 60;
                } else if(blue >= 230) {
                    red -= 60;
                    green -= 60;
                }

                int rgb = convertRBG(red, green, blue);
                box.textColor = covertHSB(rgb);
                //client.stream.writePacketID(245);
                //client.stream.writeInt(rgb);
                client.clickX = -1;
                client.clickY = -1;
            }

        }
    }

    public static int convertRBG(int red, int green, int blue) {
        float[] HSB = Color.RGBtoHSB(red, green, blue, (float[])null);
        float hue = HSB[0];
        float saturation = HSB[1];
        float brightness = HSB[2];
        int encode_hue = (int)(hue * 63.0F);
        int encode_saturation = (int)(saturation * 7.0F);
        int encode_brightness = (int)(brightness * 127.0F);
        return (encode_hue << 10) + (encode_saturation << 7) + encode_brightness;
    }

    public static int covertHSB(int RS2HSB) {
        int decode_hue = RS2HSB >> 10 & 63;
        int decode_saturation = RS2HSB >> 7 & 7;
        int decode_brightness = RS2HSB & 127;
        return Color.HSBtoRGB((float)decode_hue / 63.0F, (float)decode_saturation / 7.0F, (float)decode_brightness / 127.0F);
    }
}

package com.client.definitions;

import com.client.FileArchive;

import java.nio.ByteBuffer;

public class FloorDefinition {
    public int texture = -1;
    public boolean occlude = true;
    public static FloorDefinition[] underlays;
    public static FloorDefinition[] overlays;
    public int anotherHue;
    public int anotherSaturation;
    public int anotherLuminance;
    public int rgb;
    public int anotherRgb;
    public int hue;
    public int saturation;
    public int lumiance;
    public int blendHueMultiplier;
    public int blendHue;
    public int hsl16;
    public static boolean snowOn = false;

    public static void unpackConfig(FileArchive streamLoader) {
        ByteBuffer buffer = ByteBuffer.wrap(streamLoader.readFile("flo.dat"));
        short underlayAmount = buffer.getShort();
        System.out.println("Underlay Floors Loaded: " + underlayAmount);
        underlays = new FloorDefinition[underlayAmount];

        for(int var5 = 0; var5 < underlayAmount; ++var5) {
            if(underlays[var5] == null) {
                underlays[var5] = new FloorDefinition();
            }
            underlays[var5].readValuesUnderlay(buffer);
            underlays[var5].generateHsl();
        }

        short var51 = buffer.getShort();
        System.out.println("Overlay Floors Loaded: " + var51);
        overlays = new FloorDefinition[var51];

        for(int i = 0; i < var51; ++i) {
            if(overlays[i] == null) {
                overlays[i] = new FloorDefinition();
            }
            //System.out.println("OverLay: "+ (overlays[i]));
            overlays[i].readValuesOverlay(buffer);
            overlays[i].generateHsl();
        }

    }

    private void generateHsl() {
        if(this.anotherRgb != -1) {
            this.rgbToHsl(this.anotherRgb);
            this.anotherHue = this.hue;
            this.anotherSaturation = this.saturation;
            this.anotherLuminance = this.lumiance;
        }

        this.rgbToHsl(this.rgb);
    }

    private void readValuesUnderlay(ByteBuffer buffer) {
        while(true) {
            byte opcode = buffer.get();
            if(opcode == 0) {
                return;
            }

            if(opcode == 1) {
                this.rgb = ((buffer.get() & 255) << 16) + ((buffer.get() & 255) << 8) + (buffer.get() & 255);
                if(snowOn && (this.rgb == 3502602 || this.rgb == 5269515 || this.rgb == 7890955 || this.rgb == 7121936 || this.rgb == 8492337)) {
                    this.rgb = 16777215;
                }
            } else {
                System.out.println("Error unrecognised underlay code: " + opcode);
            }
        }
    }

    private void readValuesOverlay(ByteBuffer buffer) {
        while(true) {
            byte opcode = buffer.get();
            if(opcode == 0) {
                return;
            }

            if(opcode == 1) {
                this.rgb = ((buffer.get() & 255) << 16) + ((buffer.get() & 255) << 8) + (buffer.get() & 255);
            } else if(opcode == 2) {
                this.texture = buffer.get() & 255;
            } else if(opcode == 5) {
                this.occlude = false;
            } else if(opcode == 7) {
                this.anotherRgb = ((buffer.get() & 255) << 16) + ((buffer.get() & 255) << 8) + (buffer.get() & 255);
            } else {
                System.out.println("Error unrecognised overlay code: " + opcode);
            }
        }
    }

    private void rgbToHsl(int rgb) {
        double r = (double)(rgb >> 16 & 255) / 256.0D;
        double g = (double)(rgb >> 8 & 255) / 256.0D;
        double b = (double)(rgb & 255) / 256.0D;
        double min = r;
        if(g < r) {
            min = g;
        }

        if(b < min) {
            min = b;
        }

        double max = r;
        if(g > r) {
            max = g;
        }

        if(b > max) {
            max = b;
        }

        double h = 0.0D;
        double s = 0.0D;
        double l = (min + max) / 2.0D;
        if(min != max) {
            if(l < 0.5D) {
                s = (max - min) / (max + min);
            }

            if(l >= 0.5D) {
                s = (max - min) / (2.0D - max - min);
            }

            if(r == max) {
                h = (g - b) / (max - min);
            } else if(g == max) {
                h = 2.0D + (b - r) / (max - min);
            } else if(b == max) {
                h = 4.0D + (r - g) / (max - min);
            }
        }

        h /= 6.0D;
        this.hue = (int)(h * 256.0D);
        this.saturation = (int)(s * 256.0D);
        this.lumiance = (int)(l * 256.0D);
        if(this.saturation < 0) {
            this.saturation = 0;
        } else if(this.saturation > 255) {
            this.saturation = 255;
        }

        if(this.lumiance < 0) {
            this.lumiance = 0;
        } else if(this.lumiance > 255) {
            this.lumiance = 255;
        }

        if(l > 0.5D) {
            this.blendHueMultiplier = (int)((1.0D - l) * s * 512.0D);
        } else {
            this.blendHueMultiplier = (int)(l * s * 512.0D);
        }

        if(this.blendHueMultiplier < 1) {
            this.blendHueMultiplier = 1;
        }

        this.blendHue = (int)(h * (double)this.blendHueMultiplier);
        this.hsl16 = hsl24to16(this.hue, this.saturation, this.lumiance);
    }

    public static final int hsl24to16(int h, int s, int l) {
        if(l > 179) {
            s /= 2;
        }

        if(l > 192) {
            s /= 2;
        }

        if(l > 217) {
            s /= 2;
        }

        if(l > 243) {
            s /= 2;
        }

        return (h / 4 << 10) + (s / 32 << 7) + l / 2;
    }
}
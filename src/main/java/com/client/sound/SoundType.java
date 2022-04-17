package com.client.sound;

public enum SoundType {
    MUSIC, SOUND, AREA_SOUND
    ;

    public double getVolume() {
        switch (this) {
            case MUSIC:
                return 5;
            case SOUND:
                return 5;
            case AREA_SOUND:
                return 5;
            default:
                throw new IllegalStateException("Didn't handle " + toString());
        }
    }
}

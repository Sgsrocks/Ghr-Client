package com.client.features;

public enum FishingSpotData {

    SMALL_NET_OR_BAIT(1530, 315),
    SMALL_NET_OR_BAIT2(1523, 315),
    SMALL_NET_OR_BAIT3(1525, 315),
    SMALL_NET_OR_BAIT4(1521, 315),
    LURE_OR_BAIT(1527, 333),
    LURE_OR_BAIT2(1526, 333),
    CAGE_OR_HARPOON(1522, 379),
    LARGE_NET_OR_HARPOON(1520, 385),  
    HARPOON_OR_SMALL_NET(1534, 7946),
    MANTA_RAY(3019, 391),
    KARAMBWAN(635, 3144),
    DARK_CRAB(1536, 11936);

    private final int spot_id;
    private final int fish_id;

    FishingSpotData(int spot_id, int fish_id) {
        this.spot_id = spot_id;
        this.fish_id = fish_id;
    }

    public int getId() {
        return spot_id;
    }

    public int getFishId() {
        return fish_id;
    }

    public static FishingSpotData forId(int id) {
        for (FishingSpotData texture : FishingSpotData.values()) {
            if (texture.getId() == id) {
                return texture;
            }
        }
        return null;
    }


}
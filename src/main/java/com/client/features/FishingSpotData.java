package com.client.features;

public enum FishingSpotData {

    SMALL_NET_OR_BAIT(1530, 317),
    SMALL_NET_OR_BAIT2(1523, 317),
    SMALL_NET_OR_BAIT3(1525, 317),
    SMALL_NET_OR_BAIT4(1521, 317),
    LURE_OR_BAIT(1508, 335),
    LURE_OR_BAIT2(1527, 335),
    LURE_OR_BAIT3(1526, 335),
    CAGE_OR_HARPOON(1522, 379),
    LARGE_NET_OR_HARPOON(1520, 385),  
    HARPOON_OR_SMALL_NET(1534, 7946),
    MANTA_RAY(3019, 391),
    KARAMBWAN(4712, 3142),
    KARAMBWAN2(4713, 3142),
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
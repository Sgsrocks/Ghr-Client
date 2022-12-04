package com.client;

import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRichPresence;

public class RichPresence {
    private static final String CLIENT_ID = "908569866133459016";
    private static club.minnced.discord.rpc.DiscordRPC lib;
    private static DiscordRichPresence presence;

    public static void initiate() {
        lib = club.minnced.discord.rpc.DiscordRPC.INSTANCE;
        DiscordEventHandlers handlers = new DiscordEventHandlers();
        lib.Discord_Initialize("908569866133459016", handlers, true, "");
        presence = new DiscordRichPresence();
        presence.startTimestamp = System.currentTimeMillis() / 1000L;
        presence.state = "Working on GodzHell ";
        presence.largeImageKey = "godzhell";
        presence.details = "Bring back one of the greatest servers there was.";
        updatePresence();
        (new Thread(() -> {
            while(!Thread.currentThread().isInterrupted()) {
                lib.Discord_RunCallbacks();

                try {
                    Thread.sleep(2000L);
                } catch (InterruptedException var1) {
                    ;
                }
            }

        }, "RPC-Callback-Handler")).start();
    }

    public boolean presenceIsNull() {
        return presence == null;
    }

    public static void updateDetails(String details) {
        presence.details = details;
        updatePresence();
    }

    public void updateState(String state) {
        presence.state = state;
        updatePresence();
    }

    public void updateSmallImageKey(String key) {
        presence.smallImageKey = key;
        updatePresence();
    }

    private static void updatePresence() {
        lib.Discord_UpdatePresence(presence);
    }
}

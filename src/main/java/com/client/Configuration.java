package com.client;

public class Configuration {
	
	public static boolean newFonts = false;

	/**
	 * Attack option priorities 0 -> Depends on combat level 1 -> Always right-click
	 * 2 -> Left-click where available 3 -> Hidden
	 */
	public static int playerAttackOptionPriority = 0;
	public static int npcAttackOptionPriority = 2;

	public static boolean debug = false;
	public static final boolean DUMP_SPRITES = false;
	public static final boolean DUMP_SPRITES2 = false;

	public static int xpSize = 0;
	public static int xpSpeed = 0;
	public static int xpDuration = 0;
	public static int xpColour = 0;
	public static int xpGroup = 0;

	public static boolean enableSmoothShading = true;
	public static boolean enableTileBlending = true;
	public static boolean enableAntiAliasing = false;
	public static boolean enableFogRendering = false;
    public static boolean enableRainbowFog = false;
    public static boolean showfish = true;
	public static boolean bountyHunter = true;
	public static boolean playerNames = false;
	public static int chatColor = 0;
    public static int fogColor = 0xDCDBDF;
	public static long fogDelay = 500;
	public static final int CLIENT_VERSION = 5;
	public static Boolean LIVE_SERVER = false;
	public static final int PORT = 5555;

	public static final String CLIENT_TITLE =  Configuration.LIVE_SERVER ? "GodzHell" : "GodzHell - LocalHost";
	public static Boolean DUMP_DATA = false;
	public static int dumpID = 204;
	public static Boolean DUMP_OTHER = false;

	/**
	 * Used to repack indexes Index 1 = Models Index 2 = Animations Index 3 =
	 * Sounds/Music Index 4 = Maps You can only do up to 300 files at a time
	 */

	public static final String CACHE_NAME = ".GodzHell";

	public static boolean 
			repackIndexOne = true,
			repackIndexTwo = true,
			repackIndexThree = false,
			repackIndexFour = true,
			repackIndexFive = false,
			repackIndexSix = false;


	public static final boolean DUMP_OBJECTS = false;
	
	/**
	 * Seasonal Events
	 */
	public static boolean HALLOWEEN = false;
	public static boolean CHRISTMAS = false;
	public static boolean CHRISTMAS_EVENT = false;
	public static boolean EASTER = false;

	public static boolean osbuddyGameframe = false;

	public static boolean oldGameframe = false;
	public static int gameWorld = 1;

	public static int xpPosition;
	public static boolean escapeCloseInterface = false;
	public static boolean inventoryContextMenu = true;
	public static int statMenuColor = 0x00FFFF;//0xFF00FF;
	public static boolean alwaysLeftClickAttack;
	public static boolean hideCombatOverlay;

	public static boolean statusBars = false;

    public static boolean snow = false;
    public static boolean dumpAnimationData = false;
}

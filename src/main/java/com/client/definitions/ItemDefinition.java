package com.client.definitions;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.HashMap;

import com.client.Rasterizer2D;
import com.client.MRUNodes;
import com.client.Model;
import com.client.Rasterizer3D;
import com.client.Sprite;
import com.client.Buffer;
import com.client.FileArchive;
import com.client.definitions.items.*;
import com.client.sign.Signlink;

public final class ItemDefinition {

	private int opcode94;
	private String opcode150;

	public static void unpackConfig(final FileArchive streamLoader) {
		stream = new Buffer(streamLoader.readFile("obj.dat"));
		Buffer stream = new Buffer(streamLoader.readFile("obj.idx"));
		//stream = new Stream(FileOperations.readFile(Signlink.getCacheDirectory() + "/data/obj.dat"));
		//final Stream stream = new Stream(FileOperations.readFile(Signlink.getCacheDirectory() + "/data/obj.idx"));

		totalItems = stream.readUnsignedShort();
		streamIndices = new int[totalItems + 90000];
		int i = 2;
		for (int j = 0; j < totalItems; j++) {
			streamIndices[j] = i;
			i += stream.readUnsignedShort();
		}

		cache = new ItemDefinition[10];
		for (int index = 0; index < 10; index++) {
			cache[index] = new ItemDefinition();
		}
		//dumpList();
		//dumpNpcList();
		//dumpNotableList();
		//dumpNotes();
		//dumpStackable();
		//dumpStackableList();
	}

	public static ItemDefinition forID(int itemId) {
		for (int j = 0; j < 10; j++) {
			if (cache[j].id == itemId) {
				return cache[j];
			}
		}

		if (itemId == -1)
			itemId = 0;
		if (itemId > streamIndices.length)
			itemId = 0;

		cacheIndex = (cacheIndex + 1) % 10;
		ItemDefinition itemDef = cache[cacheIndex];
		stream.currentPosition = streamIndices[itemId];
		itemDef.id = itemId;
		itemDef.setDefaults();
		itemDef.readValues(stream);
		if (itemDef.certTemplateID != -1) {
			itemDef.updateNote(forID(itemDef.certTemplateID), forID(itemDef.certID));
		}

		if (itemDef.notedId != -1) {
			itemDef.method2789(forID(itemDef.notedId), forID(itemDef.unnotedId));
		}

		if (itemDef.placeholderTemplateId != -1) {
			itemDef.method2790(forID(itemDef.placeholderTemplateId), forID(itemDef.placeholderId));
		}
		customItems(itemId);
		switch (itemId) {

		}
		return itemDef;
	}
	private int currentcolors;
	private int currenttextures;
	//Start item dump
	public static void dumpItems2() {
		for(int i = 0; i < totalItems; i++) {
			ItemDefinition class8 = forID(i);
			BufferedWriter bw = null;

			try {
				class8.currentcolors = 0;
				bw = new BufferedWriter(new FileWriter(Signlink.getCacheDirectory() + "/dumps/201itemdump.txt", true));

				bw.newLine();
				bw.write("	if(i == "+i+") //ID");
				bw.newLine();
				bw.write("		{");
				bw.newLine();
				bw.write("			class8.itemActions = new String[] {"+Arrays.toString(class8.itemActions)+"};");
				bw.newLine();
				bw.write("			class8.groundActions = new String[] {"+Arrays.toString(class8.groundActions)+"};");
				bw.newLine();
				bw.write("			class8.name = \""+class8.name+"\"; //Name");
				bw.newLine();
				bw.write("			class8.description = \"Its an "+class8.name+"\"; //Description");
				bw.newLine();
				if(class8.modifiedModelColors != null) {
					for(int i2 = 0; i2 < class8.modifiedModelColors.length; i2++) {
						if(i2 == 0) {
						}
						if(i2 != class8.modifiedModelColors.length - 1) {
							class8.currentcolors += 1;
						} else {
							class8.currentcolors += 1;                         									if(class8.currentcolors != 0)
							{
								bw.write("			class8.modifiedModelColors = new int["+class8.currentcolors+"];");
								bw.newLine();
								bw.write("			class8.originalModelColors = new int["+class8.currentcolors+"];");
								bw.newLine();
							}
							class8.currentcolors = 0;
						}
					}
				}
				if(class8.modifiedModelColors != null) {
					for(int i2 = 0; i2 < class8.modifiedModelColors.length; i2++) {
						if(i2 == 0) {
						}
						if(i2 != class8.modifiedModelColors.length - 1) {                             	bw.write("			class8.modifiedModelColors["+class8.currentcolors+"] = " +class8.modifiedModelColors[i2]+";");
							class8.currentcolors += 1;
							bw.newLine();
						} else {                            						bw.write("			class8.modifiedModelColors["+class8.currentcolors+"] = " +class8.modifiedModelColors[i2]+";");
							class8.currentcolors = 0;
							bw.newLine();
						}
					}
				}
				if(class8.originalModelColors != null) {
					for(int i2 = 0; i2 < class8.originalModelColors.length; i2++) {
						if(i2 == 0) {
						}
						if(i2 != class8.originalModelColors.length - 1) {                             	bw.write("			class8.originalModelColors["+class8.currentcolors+"] = " +class8.originalModelColors[i2]+";");
							class8.currentcolors += 1;
							bw.newLine();
						} else {                            						bw.write("			class8.originalModelColors["+class8.currentcolors+"] = " +class8.originalModelColors[i2]+";");
							class8.currentcolors = 0;
							bw.newLine();
						}
					}
					if(class8.modifiedTextureColors != null) {
						for(int i2 = 0; i2 < class8.modifiedTextureColors.length; i2++) {
							if(i2 == 0) {
							}
							if(i2 != class8.modifiedTextureColors.length - 1) {
								class8.currenttextures += 1;
							} else {
								class8.currenttextures += 1;                         									if(class8.currenttextures != 0)
								{
									bw.write("			class8.modifiedTextureColors = new int["+class8.currenttextures+"];");
									bw.newLine();
									bw.write("			class8.originalTextureColors = new int["+class8.currenttextures+"];");
									bw.newLine();
								}
								class8.currenttextures = 0;
							}
						}
					}
					if(class8.modifiedTextureColors != null) {
						for(int i2 = 0; i2 < class8.modifiedTextureColors.length; i2++) {
							if(i2 == 0) {
							}
							if(i2 != class8.modifiedTextureColors.length - 1) {                             	bw.write("			class8.modifiedTextureColors["+class8.currenttextures+"] = " +class8.modifiedTextureColors[i2]+";");
								class8.currenttextures += 1;
								bw.newLine();
							} else {                            						bw.write("			class8.modifiedTextureColors["+class8.currenttextures+"] = " +class8.modifiedTextureColors[i2]+";");
								class8.currenttextures = 0;
								bw.newLine();
							}
						}
					}
					if(class8.originalTextureColors != null) {
						for(int i2 = 0; i2 < class8.originalTextureColors.length; i2++) {
							if(i2 == 0) {
							}
							if(i2 != class8.originalTextureColors.length - 1) {                             	bw.write("			class8.originalTextureColors["+class8.currenttextures+"] = " +class8.originalTextureColors[i2]+";");
								class8.currenttextures += 1;
								bw.newLine();
							} else {                            						bw.write("			class8.originalTextureColors["+class8.currenttextures+"] = " +class8.originalTextureColors[i2]+";");
								class8.currenttextures = 0;
								bw.newLine();
							}
						}
					}
					if(class8.stackAmounts != null) {
						for(int i2 = 0; i2 < class8.stackAmounts.length; i2++) {
							if(i2 == 0) {
							}
							if(i2 != class8.stackAmounts.length - 1) {
								class8.currentcolors += 1;
							} else {
								class8.currentcolors += 1;                         									if(class8.currentcolors != 0)
								{
									bw.write("			class8.stackAmounts = new int["+class8.currentcolors+"];");
									bw.newLine();
									bw.write("			class8.stackIds = new int["+class8.currentcolors+"];");
									bw.newLine();
								}
								class8.currentcolors = 0;
							}
						}
					}

					if(class8.stackAmounts != null) {
						for(int i2 = 0; i2 < class8.stackAmounts.length; i2++) {
							if(i2 == 0) {
							}
							if(i2 != class8.stackAmounts.length - 1) {                             	bw.write("			class8.stackAmounts["+class8.currentcolors+"] = " +class8.stackAmounts[i2]+";");
								class8.currentcolors += 1;
								bw.newLine();
							} else {                            						bw.write("			class8.stackAmounts["+class8.currentcolors+"] = " +class8.stackAmounts[i2]+";");
								class8.currentcolors = 0;
								bw.newLine();
							}
						}
					}
					if(class8.stackIDs != null) {
						for(int i2 = 0; i2 < class8.stackIDs.length; i2++) {
							if(i2 == 0) {
							}
							if(i2 != class8.stackIDs.length - 1) {                             	bw.write("			class8.stackIds["+class8.currentcolors+"] = " +class8.stackIDs[i2]+";");
								class8.currentcolors += 1;
								bw.newLine();
							} else {                            						bw.write("			class8.stackIds["+class8.currentcolors+"] = " +class8.stackIDs[i2]+";");
								class8.currentcolors = 0;
								bw.newLine();
							}
						}
					}
				}
				bw.write("			class8.modelId = "+class8.modelId+";");
				bw.newLine();
				bw.write("			class8.spriteScale = "+class8.spriteScale+";");
				bw.newLine();
				bw.write("			class8.spritePitch = "+class8.spritePitch+";");
				bw.newLine();
				bw.write("			class8.spriteCameraRoll = "+class8.spriteCameraRoll+";");
				bw.newLine();
				bw.write("			class8.spriteCameraYaw = "+class8.spriteCameraYaw+";");
				bw.newLine();
				bw.write("			class8.spriteTranslateX = "+class8.spriteTranslateX+";");
				bw.newLine();
				bw.write("			class8.spriteTranslateY = "+class8.spriteTranslateY+";");
				bw.newLine();
				bw.write("			class8.primaryMaleModel = "+class8.primaryMaleModel+";");
				bw.newLine();
				bw.write("			class8.primaryFemaleModel = "+class8.primaryFemaleModel+";");
				bw.newLine();
				bw.write("			class8.secondaryMaleModel = "+class8.secondaryMaleModel+";");
				bw.newLine();
				bw.write("			class8.secondaryFemaleModel = "+class8.secondaryFemaleModel+";");
				bw.newLine();
				bw.write("			class8.primaryMaleHeadPiece = "+class8.primaryMaleHeadPiece+";");
				bw.newLine();
				bw.write("			class8.primaryFemaleHeadPiece = "+class8.primaryFemaleHeadPiece+";");
				bw.newLine();
				bw.write("			class8.value = "+class8.value+";");
				bw.newLine();
				bw.write("			class8.unnotedId = " + class8.unnotedId + ";");
				bw.newLine();
				bw.write("			class8.notedId = " + class8.notedId + ";");
				bw.newLine();
				bw.write("			class8.certID = " + class8.certID + ";");
				bw.newLine();
				bw.write("			class8.certTemplateID = " + class8.certTemplateID + ";");
				bw.newLine();
				bw.write("			class8.stackable = " + class8.stackable + ";");
				bw.newLine();
				bw.write("			class8.placeholderId = " + class8.placeholderId + ";");
				bw.newLine();
				bw.write("			class8.placeholderTemplateId = " + class8.placeholderTemplateId + ";");
				bw.newLine();
				bw.write("		}");
				bw.newLine();
				bw.newLine();
				bw.flush();
			} catch (IOException ioe) {
				ioe.printStackTrace();
			} finally {
				if (bw != null) try {
					bw.close();
				} catch (IOException ioe2) {
				}
			}
		}
	}
	private static void customItems(int itemId) {
		try {
			ItemDefinition itemDef = forID(itemId);
			ItemDefinition_Sub1.itemDef(itemId, itemDef);
			ItemDefinition_Sub1_Sub1.itemDef(itemId, itemDef);
			ItemDefinition_Sub2.itemDef(itemId, itemDef);
			ItemDefinition_Sub2_Sub1.itemDef(itemId, itemDef);
			ItemDefinition_Sub3.itemDef(itemId, itemDef);
			ItemDefinition_Sub4.itemDef(itemId, itemDef);
		} catch (ArrayIndexOutOfBoundsException e){
			e.printStackTrace();
		}
		switch (itemId) {
		}
	}
	public static void dumpNpcList() {
		for(int i = 28213; i < 30000; ++i) {
			ItemDefinition class5 = forID(i);
			BufferedWriter bw = null;

			try {
				bw = new BufferedWriter(new FileWriter(Signlink.getCacheDirectory() + "/dumps/customItemExamine.txt", true));
				if(class5.name != null) {
					bw.write("{");
					bw.newLine();
					bw.write("\"id\":" + i + ",");
					bw.newLine();
					bw.write("\"examine\": \"" + class5.description + "\"");
					bw.newLine();
					bw.write("},");
					bw.newLine();
					bw.flush();
					bw.close();
				}
			} catch (IOException var4) {
			}
		}

	}
	void updateNote(ItemDefinition itemcomposition_1, ItemDefinition itemcomposition_2)
	{
		modelId = itemcomposition_1.modelId;
		spriteScale = itemcomposition_1.spriteScale;
		spritePitch = itemcomposition_1.spritePitch;
		spriteCameraRoll = itemcomposition_1.spriteCameraRoll;
		spriteCameraYaw = itemcomposition_1.spriteCameraYaw;
		spriteTranslateX = itemcomposition_1.spriteTranslateX;
		spriteTranslateY = itemcomposition_1.spriteTranslateY;
		originalModelColors = itemcomposition_1.originalModelColors;
		modifiedModelColors = itemcomposition_1.modifiedModelColors;
		originalTextureColors = itemcomposition_1.originalTextureColors;
		modifiedTextureColors = itemcomposition_1.modifiedTextureColors;
		name = itemcomposition_2.name;
		membersObject = itemcomposition_2.membersObject;
		value = itemcomposition_2.value;
		description = ("Swap this note at any bank for a " + itemcomposition_2.name + ".");
		stackable = true;
	}
	public static void dumpCfg() {
		boolean delete = (new File("item.cfg")).delete();
		for(int i = 0; i < 30000; i++) {
			ItemDefinition class8 = forID(i);
			BufferedWriter bw = null;
			String des = "";
			if(class8.description != null)
				des = new String(class8.description.replace(" ", "_"));
			else
				des = "Its a "+class8.name;
			try {
				BufferedWriter bufferedwriter;
				bufferedwriter = null;
				bufferedwriter = new BufferedWriter(new FileWriter("item.cfg", true));
				bufferedwriter.write((new StringBuilder()).append("item = ").append(i).append("	").append(class8.name.replace(" ", "_")).append("	").append(des.replace(" ", "_")).append("	").append(class8.value).append("	").append(class8.value).append("	").append(class8.value).append("	").append("0").append("	").append("0").append("	").append("0").append("	").append("0").append("	").append("0").append("	").append("0").append("	").append("0").append("	").append("0").append("	").append("0").append("	").append("0").append("	").append("0").append("	").append("0").toString());
				bufferedwriter.newLine();
				bufferedwriter.flush();
			} catch(Exception e) {
			}
		}
	}
	void method2789(ItemDefinition var1, ItemDefinition var2) {
		modelId = var1.modelId * 1;
		spriteScale = var1.spriteScale * 1;
		spritePitch = 1 * var1.spritePitch;
		spriteCameraRoll = 1 * var1.spriteCameraRoll;
		spriteCameraYaw = 1 * var1.spriteCameraYaw;
		spriteTranslateX = 1 * var1.spriteTranslateX;
		spriteTranslateY = var1.spriteTranslateY * 1;
		originalModelColors = var2.originalModelColors;
		modifiedModelColors = var2.modifiedModelColors;
		originalTextureColors = var2.originalTextureColors;
		modifiedTextureColors = var2.modifiedTextureColors;
		name = var2.name;
		membersObject = var2.membersObject;
		stackable = var2.stackable;
		primaryMaleModel = 1 * var2.primaryMaleModel;
		secondaryMaleModel = 1 * var2.secondaryMaleModel;
		tertiaryMaleEquipmentModel = 1 * var2.tertiaryMaleEquipmentModel;
		primaryFemaleModel = var2.primaryFemaleModel * 1;
		secondaryFemaleModel = var2.secondaryFemaleModel * 1;
		tertiaryFemaleEquipmentModel = 1 * var2.tertiaryFemaleEquipmentModel;
		primaryMaleHeadPiece = 1 * var2.primaryMaleHeadPiece;
		secondaryMaleHeadPiece = var2.secondaryMaleHeadPiece * 1;
		primaryFemaleHeadPiece = var2.primaryFemaleHeadPiece * 1;
		secondaryFemaleHeadPiece = var2.secondaryFemaleHeadPiece * 1;
		team = var2.team * 1;
		groundActions = var2.groundActions;
		itemActions = new String[5];
		equipActions = new String[5];
		if (null != var2.itemActions) {
			for (int var4 = 0; var4 < 4; ++var4) {
				itemActions[var4] = var2.itemActions[var4];
			}
		}

		itemActions[4] = "Discard";
		value = 0;
	}

	void method2790(ItemDefinition var1, ItemDefinition var2) {
		modelId = var1.modelId * 1;
		spriteScale = 1 * var1.spriteScale;
		spritePitch = var1.spritePitch * 1;
		spriteCameraRoll = var1.spriteCameraRoll * 1;
		spriteCameraYaw = var1.spriteCameraYaw * 1;
		spriteTranslateX = 1 * var1.spriteTranslateX;
		spriteTranslateY = var1.spriteTranslateY * 1;
		originalModelColors = var1.originalModelColors;
		modifiedModelColors = var1.modifiedModelColors;
		originalTextureColors = var1.originalTextureColors;
		modifiedTextureColors = var1.modifiedTextureColors;
		stackable = var1.stackable;
		name = var2.name;
		value = 0;
	}

	private void readValues(Buffer stream) {
		while (true) {
			int opcode = stream.readUnsignedByte();
			if (opcode == 0)
				return;
			if (opcode == 1)
				modelId = stream.readUnsignedShort();
			else if (opcode == 2)
				name = stream.readString();
			else if (opcode == 3)
				description = stream.readString();
			else if (opcode == 4)
				spriteScale = stream.readUnsignedShort();
			else if (opcode == 5)
				spritePitch = stream.readUnsignedShort();
			else if (opcode == 6)
				spriteCameraRoll = stream.readUnsignedShort();
			else if (opcode == 7) {
				spriteTranslateX = stream.readUnsignedShort();
				if (spriteTranslateX > 32767)
					spriteTranslateX -= 0x10000;
			} else if (opcode == 8) {
				spriteTranslateY = stream.readUnsignedShort();
				if (spriteTranslateY > 32767)
					spriteTranslateY -= 0x10000;
			} else if (opcode == 11)
				stackable = true;
			else if (opcode == 12)
				value = stream.readDWord();
			else if (opcode == 16)
				membersObject = true;
			else if (opcode == 23) {
				primaryMaleModel = stream.readUnsignedShort();
				maleTranslation = stream.readSignedByte();
			} else if (opcode == 24)
				secondaryMaleModel = stream.readUnsignedShort();
			else if (opcode == 25) {
				primaryFemaleModel = stream.readUnsignedShort();
				femaleTranslation = stream.readSignedByte();
			} else if (opcode == 26)
				secondaryFemaleModel = stream.readUnsignedShort();
			else if (opcode >= 30 && opcode < 35) {
				if (groundActions == null)
					groundActions = new String[5];
				groundActions[opcode - 30] = stream.readString();
				if (groundActions[opcode - 30].equalsIgnoreCase("hidden"))
					groundActions[opcode - 30] = null;
			} else if (opcode >= 35 && opcode < 40) {
				if (itemActions == null)
					itemActions = new String[5];
				itemActions[opcode - 35] = stream.readString();
			} else if (opcode == 40) {
				int size = stream.readUnsignedByte();
				originalModelColors = new int[size];
				modifiedModelColors = new int[size];
				for (int index = 0; index < size; index++) {
					originalModelColors[index] = stream.readUnsignedShort();
					modifiedModelColors[index] = stream.readUnsignedShort();
				}
			} else if (opcode == 41) {
				int size = stream.readUnsignedByte();
				originalTextureColors = new short[size];
				modifiedTextureColors = new short[size];
				for (int index = 0; index < size; index++) {
					originalTextureColors[index] = (short) stream.readUnsignedShort();
					modifiedTextureColors[index] = (short) stream.readUnsignedShort();
				}
			} else if (opcode == 42) {
				shiftClickIndex = stream.readUnsignedByte();
			} else if (opcode == 65) {
				searchable = true;
			} else if (opcode == 78)
				tertiaryMaleEquipmentModel = stream.readUnsignedShort();
			else if (opcode == 79)
				tertiaryFemaleEquipmentModel = stream.readUnsignedShort();
			else if (opcode == 90)
				primaryMaleHeadPiece = stream.readUnsignedShort();
			else if (opcode == 91)
				primaryFemaleHeadPiece = stream.readUnsignedShort();
			else if (opcode == 92)
				secondaryMaleHeadPiece = stream.readUnsignedShort();
			else if (opcode == 93)
				secondaryFemaleHeadPiece = stream.readUnsignedShort();
			else if (opcode == 94)
				opcode94 = stream.readUnsignedShort();
			else if (opcode == 95)
				spriteCameraYaw = stream.readUnsignedShort();
			else if (opcode == 97)
				certID = stream.readUnsignedShort();
			else if (opcode == 98)
				certTemplateID = stream.readUnsignedShort();
			else if (opcode >= 100 && opcode < 110) {
				if (stackIDs == null) {
					stackIDs = new int[10];
					stackAmounts = new int[10];
				}
				stackIDs[opcode - 100] = stream.readUnsignedShort();
				stackAmounts[opcode - 100] = stream.readUnsignedShort();
			} else if (opcode == 110)
				groundScaleX = stream.readUnsignedShort();
			else if (opcode == 111)
				groundScaleY = stream.readUnsignedShort();
			else if (opcode == 112)
				groundScaleZ = stream.readUnsignedShort();
			else if (opcode == 113)
				ambient = stream.readSignedByte();
			else if (opcode == 114)
				contrast = stream.readSignedByte() * 5;
			else if (opcode == 115)
				team = stream.readUnsignedByte();
			else if (opcode == 139)
				unnotedId = stream.readUnsignedShort();
			else if (opcode == 140)
				notedId = stream.readUnsignedShort();
			else if (opcode == 148)
				placeholderId = stream.readUnsignedShort();
			else if (opcode == 149) {
				placeholderTemplateId = stream.readUShort();
			} else if (opcode == 150) {
					opcode150 = stream.readString();
			} else if (opcode == 249) {
				int length = stream.readUnsignedByte();

				params = new HashMap<>(length);

				for (int i = 0; i < length; i++) {
					boolean isString = stream.readUnsignedByte() == 1;
					int key = stream.read24Int();
					Object value;

					if (isString) {
						value = stream.readString();
					} else {
						value = stream.readInt();
					}

					params.put(key, value);
				}
			} else {
				System.err.println(String.format("Error unrecognised {OBJ} opcode: %d%n", opcode));
			}
		}
	}

	public int unnotedId, notedId, placeholderId, placeholderTemplateId;

	public static void nullLoader() {
		mruNodes2 = null;
		mruNodes1 = null;
		streamIndices = null;
		cache = null;
		stream = null;
	}

	public boolean method192(int j) {
		int k = primaryMaleHeadPiece;
		int l = secondaryMaleHeadPiece;
		if (j == 1) {
			k = primaryFemaleHeadPiece;
			l = secondaryFemaleHeadPiece;
		}
		if (k == -1)
			return true;
		boolean flag = true;
		if (!Model.isCached(k))
			flag = false;
		if (l != -1 && !Model.isCached(l))
			flag = false;
		return flag;
	}

	public Model method194(int j) {
		int k = primaryMaleHeadPiece;
		int l = secondaryMaleHeadPiece;
		if (j == 1) {
			k = primaryFemaleHeadPiece;
			l = secondaryFemaleHeadPiece;
		}
		if (k == -1)
			return null;
		Model model = Model.getModel(k);
		if (l != -1) {
			Model model_1 = Model.getModel(l);
			Model aclass30_sub2_sub4_sub6s[] = { model, model_1 };
			model = new Model(2, aclass30_sub2_sub4_sub6s);
		}
		if (modifiedModelColors != null) {
			for (int i1 = 0; i1 < modifiedModelColors.length; i1++)
				model.recolor(modifiedModelColors[i1], originalModelColors[i1]);

		}
		if (originalTextureColors != null) {
			for (int k2 = 0; k2 < originalTextureColors.length; k2++)
				model.retexture(originalTextureColors[k2], modifiedTextureColors[k2]);

		}
		return model;
	}

	public boolean method195(int j) {
		int k = primaryMaleModel;
		int l = secondaryMaleModel;
		int i1 = tertiaryMaleEquipmentModel;
		if (j == 1) {
			k = primaryFemaleModel;
			l = secondaryFemaleModel;
			i1 = tertiaryFemaleEquipmentModel;
		}
		if (k == -1)
			return true;
		boolean flag = true;
		if (!Model.isCached(k))
			flag = false;
		if (l != -1 && !Model.isCached(l))
			flag = false;
		if (i1 != -1 && !Model.isCached(i1))
			flag = false;
		return flag;
	}

	public Model method196(int i) {
		int j = primaryMaleModel;
		int k = secondaryMaleModel;
		int l = tertiaryMaleEquipmentModel;
		if (i == 1) {
			j = primaryFemaleModel;
			k = secondaryFemaleModel;
			l = tertiaryFemaleEquipmentModel;
		}
		if (j == -1)
			return null;
		Model model = Model.getModel(j);
		if (k != -1)
			if (l != -1) {
				Model model_1 = Model.getModel(k);
				Model model_3 = Model.getModel(l);
				Model aclass30_sub2_sub4_sub6_1s[] = { model, model_1, model_3 };
				model = new Model(3, aclass30_sub2_sub4_sub6_1s);
			} else {
				Model model_2 = Model.getModel(k);
				Model aclass30_sub2_sub4_sub6s[] = { model, model_2 };
				model = new Model(2, aclass30_sub2_sub4_sub6s);
			}
		if (i == 0 && maleTranslation != 0)
			model.translate(0, maleTranslation, 0);
		if (i == 1 && femaleTranslation != 0)
			model.translate(0, femaleTranslation, 0);
		if (modifiedModelColors != null) {
			for (int i1 = 0; i1 < modifiedModelColors.length; i1++)
				model.recolor(modifiedModelColors[i1], originalModelColors[i1]);

		}
		if (originalTextureColors != null) {
			for (int k2 = 0; k2 < originalTextureColors.length; k2++)
				model.retexture(originalTextureColors[k2], modifiedTextureColors[k2]);

		}
		return model;
	}

	private void setDefaults() {
		// equipActions = new String[6];
		equipActions = new String[] { "Remove", null, "Operate", null, null };
		modelId = 0;
		name = null;
		description = null;
		modifiedModelColors = null;
		originalModelColors = null;
		modifiedTextureColors = null;
		originalTextureColors = null;
		spriteScale = 2000;
		spritePitch = 0;
		spriteCameraRoll = 0;
		spriteCameraYaw = 0;
		spriteTranslateX = 0;
		spriteTranslateY = 0;
		stackable = false;
		value = 1;
		membersObject = false;
		groundActions = new String[] {null, null, "Take", null, null};
		itemActions = null;
		primaryMaleModel = -1;
		secondaryMaleModel = -1;
		maleTranslation = 0;
		primaryFemaleModel = -1;
		secondaryFemaleModel = -1;
		femaleTranslation = 0;
		tertiaryMaleEquipmentModel = -1;
		tertiaryFemaleEquipmentModel = -1;
		primaryMaleHeadPiece = -1;
		secondaryMaleHeadPiece = -1;
		primaryFemaleHeadPiece = -1;
		secondaryFemaleHeadPiece = -1;
		stackIDs = null;
		stackAmounts = null;
		certID = -1;
		certTemplateID = -1;
		groundScaleX = 128;
		groundScaleY = 128;
		groundScaleZ = 128;
		ambient = 0;
		contrast = 0;
		team = 0;

		notedId = -1;
		unnotedId = -1;
		placeholderId = -1;
		placeholderTemplateId = -1;

		searchable = false;
	}

	public static void dumpBonuses() {
		int[] bonuses = new int[14];
		int bonus = 0;
		int amount = 0;
		for (int i = 0; i < totalItems; i++) {
			ItemDefinition item = ItemDefinition.forID(i);
			URL url;
			try {
				try {
					try {
						url = new URL("http://oldschoolrunescape.fandom.com/wiki/" + item.name.replaceAll(" ", "_"));
						URLConnection con = url.openConnection();
						BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
						String line;
						BufferedWriter writer = new BufferedWriter(new FileWriter("item.cfg", true));
						while ((line = in.readLine()) != null) {
							try {
								if (line.contains("<td style=\"text-align: center; width: 35px;\">")) {
									line = line.replace("</td>", "").replace("%", "").replace("?", "")
											.replace("\"\"", "")
											.replace("<td style=\"text-align: center; width: 35px;\">", "");
									bonuses[bonus] = Integer.parseInt(line);
									bonus++;
								} else if (line.contains("<td style=\"text-align: center; width: 30px;\">")) {
									line = line.replace("</td>", "").replace("%", "").replace("?", "").replace("%", "")
											.replace("<td style=\"text-align: center; width: 30px;\">", "");
									bonuses[bonus] = Integer.parseInt(line);
									bonus++;
								}
							} catch (NumberFormatException e) {

							}
							if (bonus >= 13)
								bonus = 0;
							// in.close();
						}
						in.close();
						writer.write("item	=	" + i + "	" + item.name.replace(" ", "_") + "	"
								+ item.description.replace(" ", "_") + "	" + item.value + "	" + item.value + "	"
								+ item.value + "	" + bonuses[0] + "	" + bonuses[1] + "	" + bonuses[2] + "	"
								+ bonuses[3] + "	" + bonuses[4] + "	" + bonuses[5] + "	" + bonuses[6] + "	"
								+ bonuses[7] + "	" + bonuses[8] + "	" + bonuses[9] + "	" + bonuses[10] + "	"
								+ bonuses[13]);
						bonuses[0] = bonuses[1] = bonuses[2] = bonuses[3] = bonuses[4] = bonuses[5] = bonuses[6] = bonuses[7] = bonuses[8] = bonuses[9] = bonuses[10] = bonuses[13] = 0;
						writer.newLine();
						amount++;
						writer.close();
					} catch (NullPointerException e) {

					}
				} catch (FileNotFoundException e) {

				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		System.out.println("Done dumping " + amount + " item bonuses!");
	}

	public static void dumpBonus() {
		final int[] wikiBonuses = new int[18];
		int bonus = 0;
		int amount = 0;
		System.out.println("Starting to dump item bonuses...");
		for (int i = 20000; i < totalItems; i++) {
			ItemDefinition item = ItemDefinition.forID(i);
			try {
				try {
					try {
						final URL url = new URL(
								"http://2007.runescape.wikia.com/wiki/" + item.name.replaceAll(" ", "_"));
						URLConnection con = url.openConnection();
						BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
						String line;
						writer = new BufferedWriter(new FileWriter("item.cfg", true));
						while ((line = in.readLine()) != null) {
							try {
								if (line.contains("<td style=\"text-align: center; width: 35px;\">")) {
									line = line.replace("</td>", "").replace("%", "").replace("?", "")
											.replace("\"\"", "")
											.replace("<td style=\"text-align: center; width: 35px;\">", "");
									wikiBonuses[bonus] = Integer.parseInt(line);
									bonus++;
								} else if (line.contains("<td style=\"text-align: center; width: 30px;\">")) {
									line = line.replace("</td>", "").replace("%", "").replace("?", "").replace("%", "")
											.replace("<td style=\"text-align: center; width: 30px;\">", "");
									wikiBonuses[bonus] = Integer.parseInt(line);
									bonus++;
								}
							} catch (NumberFormatException e) {
								e.printStackTrace();
							}
							in.close();
							writer.write("item = " + i + "	" + item.name.replace(" ", "_") + "	"
									+ item.description.replace(" ", "_") + "	" + item.value + "	" + item.value
									+ "	" + item.value + "	" + wikiBonuses[0] + "	" + wikiBonuses[1] + "	"
									+ wikiBonuses[2] + "	" + wikiBonuses[3] + "	" + wikiBonuses[4] + "	"
									+ wikiBonuses[5] + "	" + wikiBonuses[6] + "	" + wikiBonuses[7] + "	"
									+ wikiBonuses[8] + "	" + wikiBonuses[9] + "	" + wikiBonuses[10] + "	"
									+ wikiBonuses[13]);
							amount++;
							wikiBonuses[0] = wikiBonuses[1] = wikiBonuses[2] = wikiBonuses[3] = wikiBonuses[4] = wikiBonuses[5] = wikiBonuses[6] = wikiBonuses[7] = wikiBonuses[8] = wikiBonuses[9] = wikiBonuses[10] = wikiBonuses[11] = wikiBonuses[13] = 0;
							writer.newLine();
							writer.close();
						}
					} catch (NullPointerException e) {
						e.printStackTrace();
					}
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.out.println("Done dumping " + amount + " item bonuses!");
		}
	}

	public static void dumpItemDefs() {
		final int[] wikiBonuses = new int[18];
		int bonus = 0;
		int amount = 0;
		int value = 0;
		int slot = -1;
		// Testing Variables just so i know format is correct
		String fullmask = "false";
		// boolean stackable1 = false;
		String stackable = "false";
		// boolean noteable1 = false;
		String noteable = "true";
		// boolean tradeable1 = false;
		String tradeable = "true";
		// boolean wearable1 = false;
		String wearable = "true";
		String showBeard = "true";
		String members = "true";
		boolean twoHanded = false;
		System.out.println("Starting to dump item definitions...");
		for (int i = 22328; i < totalItems; i++) {
			ItemDefinition item = ItemDefinition.forID(i);
			try {
				try {
					try {
						final URL url = new URL("http://oldschoolrunescape.wikia.com/wiki/" + item.name.replaceAll(" ", "_"));
						URLConnection con = url.openConnection();
						BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
						String line;
						writer = new BufferedWriter(new FileWriter("itemDefs.json", true));
						while ((line = in.readLine()) != null) {
							try {
								if (line.contains("<td style=\"text-align: center; width: 35px;\">")) {
									line = line.replace("</td>", "").replace("%", "").replace("?", "")
											.replace("\"\"", "")
											.replace("<td style=\"text-align: center; width: 35px;\">", "");
									wikiBonuses[bonus] = Integer.parseInt(line);
									bonus++;
								} else if (line.contains("<td style=\"text-align: center; width: 30px;\">")) {
									line = line.replace("</td>", "").replace("%", "").replace("?", "").replace("%", "")
											.replace("<td style=\"text-align: center; width: 30px;\">", "");
									wikiBonuses[bonus] = Integer.parseInt(line);
									bonus++;
								}
								if (line.contains("<div id=\"GEPCalcResult\" style=\"display:inline;\">")) {
									line = line.replace("</div>", "").replace("%", "").replace("?", "").replace("%", "")
											.replace("<div id=\"GEPCalcResult\" style=\"display:inline;\">", "");
									value = Integer.parseInt(line);
								}

							} catch (NumberFormatException e) {
								e.printStackTrace();
							}
							in.close();
							// fw.write("ItemID: "+itemDefinition.id+" - "+itemDefinition.name);
							// fw.write(System.getProperty("line.separator"));
							// writer.write("[\n");
							writer.write("  {\n\t\"id\": " + item.id + ",\n\t\"name\": \"" + item.name
									+ "\",\n\t\"desc\": \"" + item.name + "\",\n\t\"value\": "
									+ value + ",\n\t\"dropValue\": " + value + ",\n\t\"bonus\": [\n\t  "
									+ wikiBonuses[0] + ",\n\t  " + wikiBonuses[1] + ",\n\t  " + wikiBonuses[2]
									+ ",\n\t  " + wikiBonuses[3] + ",\n\t  " + wikiBonuses[4] + ",\n\t  "
									+ wikiBonuses[5] + ",\n\t  " + wikiBonuses[6] + ",\n\t  " + wikiBonuses[7]
									+ ",\n\t  " + wikiBonuses[8] + ",\n\t  " + wikiBonuses[9] + ",\n\t  "
									+ wikiBonuses[10] + ",\n\t  " + wikiBonuses[13] + ",\n\t],\n\t\"slot\": " + slot
									+ ",\n\t\"fullmask\": " + fullmask + ",\n\t\"stackable\": " + stackable
									+ ",\n\t\"noteable\": " + noteable + ",\n\t\"tradeable\": " + tradeable
									+ ",\n\t\"wearable\": " + wearable + ",\n\t\"showBeard\": " + showBeard
									+ ",\n\t\"members\": " + members + ",\n\t\"twoHanded\": " + twoHanded
									+ ",\n\t\"requirements\": [\n\t  0,\n\t  0,\n\t  0,\n\t  0,\n\t  0,\n\t  0,\n\t  0,\n\t  0,\n\t  0,\n\t  0,\n\t  0,\n\t  0,\n\t  0,\n\t  0,\n\t  0,\n\t  0,\n\t  0,\n\t  0,\n\t  0,\n\t  0,\n\t  0,\n\t]\n  },\n");
							/*
							 * writer.write("item = " + i + "	" + item.name.replace(" ", "_") + "	" +
							 * item.description.replace(" ", "_") + "	" + item.value + "	" + item.value +
							 * "	" + item.value + "	" + wikiBonuses[0] + "	" + wikiBonuses[1] + "	" +
							 * wikiBonuses[2] + "	" + wikiBonuses[3] + "	" + wikiBonuses[4] + "	" +
							 * wikiBonuses[5] + "	" + wikiBonuses[6] + "	" + wikiBonuses[7] + "	" +
							 * wikiBonuses[8] + "	" + wikiBonuses[9] + "	" + wikiBonuses[10] + "	" +
							 * wikiBonuses[13]);
							 */
							amount++;
							wikiBonuses[0] = wikiBonuses[1] = wikiBonuses[2] = wikiBonuses[3] = wikiBonuses[4] = wikiBonuses[5] = wikiBonuses[6] = wikiBonuses[7] = wikiBonuses[8] = wikiBonuses[9] = wikiBonuses[10] = wikiBonuses[11] = wikiBonuses[13] = 0;
							writer.newLine();
							writer.close();
						}
					} catch (NullPointerException e) {
						e.printStackTrace();
					}
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.out.println("Done dumping " + amount + " item definitions!");
		}
	}

	public static void itemDump() {
		try {
			FileWriter fw = new FileWriter(System.getProperty("user.home") + "/Desktop/Item Dump.txt");
			for (int i = totalItems - 9000; i < totalItems; i++) {
				ItemDefinition item = ItemDefinition.forID(i);
				fw.write("case " + i + ":");
				fw.write(System.getProperty("line.separator"));
				fw.write("itemDef.name = \"" + item.name + "\";");
				fw.write(System.getProperty("line.separator"));
				fw.write("itemDef.modelID= " + item.modelId + ";");
				fw.write(System.getProperty("line.separator"));
				fw.write("itemDef.primaryMaleModel= " + item.primaryMaleModel + ";");
				fw.write(System.getProperty("line.separator"));
				fw.write("itemDef.primaryFemaleModel= " + item.primaryFemaleModel + ";");
				fw.write(System.getProperty("line.separator"));
				fw.write("itemDef.spriteScale = " + item.spriteScale + ";");
				fw.write(System.getProperty("line.separator"));
				fw.write("itemDef.modelRotationX = " + item.spritePitch + ";");
				fw.write(System.getProperty("line.separator"));
				fw.write("itemDef.modelRotationY = " + item.spriteCameraRoll + ";");
				fw.write(System.getProperty("line.separator"));
				fw.write("itemDef.spriteTranslateX = " + item.spriteTranslateX + ";");
				fw.write(System.getProperty("line.separator"));
				fw.write("itemDef.spriteTranslateY = " + item.spriteTranslateY + ";");
				fw.write(System.getProperty("line.separator"));
				fw.write("itemDef.description = \"" + item.description + "\";");
				fw.write(System.getProperty("line.separator"));

				fw.write(System.getProperty("line.separator"));
				fw.write("itemDef.value = " + item.value + ";");
				fw.write(System.getProperty("line.separator"));
				fw.write("itemDef.team = " + item.team + ";");
				fw.write(System.getProperty("line.separator"));
				fw.write("break;");
				fw.write(System.getProperty("line.separator"));
				fw.write(System.getProperty("line.separator"));
			}
			fw.close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public static void dumpList() {
		try {
			BufferedWriter fw = new BufferedWriter(new FileWriter(Signlink.getCacheDirectory() + "/dumps/205itemlist.txt"));
			for (int i = 0; i < 30000; i++) {
				ItemDefinition itemDefinition = ItemDefinition.forID(i);
				fw.write("id: " + itemDefinition.id + " - " + itemDefinition.name + "\n");
				fw.newLine();
			}
			fw.close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public static void dumpStackableList() {
		try {
			File file = new File("stackables.dat");

			if (!file.exists()) {
				file.createNewFile();
			}

			try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
				for (int i = 0; i < totalItems; i++) {
					ItemDefinition definition = forID(i);
					if (definition != null) {
						writer.write(definition.id + "\t" + definition.stackable);
						writer.newLine();
					} else {
						writer.write(i + "\tfalse");
						writer.newLine();
					}
				}
			}

			System.out.println("Finished dumping noted items definitions.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public static int[] unNoteable = {};

	public static void dumpNotes() {
		try {
			FileOutputStream out = new FileOutputStream(new File("notes.dat"));
			for (int j = 0; j < totalItems; j++) {
				ItemDefinition item = ItemDefinition.forID(j);
				out.write(item.certTemplateID != -1 ? 0 : 1);
			}
			out.write(-1);
			out.close();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	public static void dumpStackable() {
		try {
			FileOutputStream out = new FileOutputStream(new File("stackable.dat"));
			for (int j = 0; j < totalItems; j++) {
				ItemDefinition item = ItemDefinition.forID(j);
				out.write(item.stackable ? 1 : 0);
			}
			out.write(-1);
			out.close();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	public static void dumpNotableList() {
		try {
			File file = new File("note_ids.dat");

			if (!file.exists()) {
				file.createNewFile();
			}

			try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
				for (int i = 0; i < totalItems; i++) {
					ItemDefinition definition = ItemDefinition.forID(i);
					if (definition != null) {
						writer.write(definition.id + "\t" + definition.certID);
						writer.newLine();
					} else {
						writer.write(i + "\t-1");
						writer.newLine();
					}
				}
			}

			System.out.println("Finished dumping noted items definitions.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void toNote() {
		ItemDefinition itemDef = forID(certTemplateID);
		modelId = itemDef.modelId;
		spriteScale = itemDef.spriteScale;
		spritePitch = itemDef.spritePitch;
		spriteCameraRoll = itemDef.spriteCameraRoll;

		spriteCameraYaw = itemDef.spriteCameraYaw;
		spriteTranslateX = itemDef.spriteTranslateX;
		spriteTranslateY = itemDef.spriteTranslateY;
		modifiedModelColors = itemDef.modifiedModelColors;
		originalModelColors = itemDef.originalModelColors;
		ItemDefinition itemDef_1 = forID(certID);
		name = itemDef_1.name;
		membersObject = itemDef_1.membersObject;
		value = itemDef_1.value;
		String s = "a";
		char c = itemDef_1.name.charAt(0);
		if (c == 'A' || c == 'E' || c == 'I' || c == 'O' || c == 'U')
			s = "an";
		description = ("Swap this note at any bank for " + s + " " + itemDef_1.name + ".");
		stackable = true;
	}

	public static Sprite getSmallSprite(int itemId) {
		ItemDefinition itemDef = forID(itemId);
		Model model = itemDef.method201(1);
		if (model == null) {
			return null;
		}
		Sprite sprite1 = null;
		if (itemDef.certTemplateID != -1) {
			sprite1 = getSprite(itemDef.certID, 10, -1);
			if (sprite1 == null) {
				return null;
			}
		}
		Sprite enabledSprite = new Sprite(18, 18);
		int k1 = Rasterizer3D.originViewX;
		int l1 = Rasterizer3D.originViewY;
		int ai[] = Rasterizer3D.scanOffsets;
		int ai1[] = Rasterizer2D.pixels;
		int i2 = Rasterizer2D.width;
		int j2 = Rasterizer2D.height;
		int k2 = Rasterizer2D.clip_left;
		int l2 = Rasterizer2D.clip_right;
		int i3 = Rasterizer2D.clip_top;
		int j3 = Rasterizer2D.clip_bottom;
		Rasterizer3D.aBoolean1464 = false;
		Rasterizer2D.initDrawingArea(18, 18, enabledSprite.myPixels, new float[1024]);
		Rasterizer2D.method336(18, 0, 0, 0, 18);
		Rasterizer3D.useViewport();
		int k3 = (int) (itemDef.spriteScale * 1.6D);
		int l3 = Rasterizer3D.anIntArray1470[itemDef.spritePitch] * k3 >> 16;
		int i4 = Rasterizer3D.COSINE[itemDef.spritePitch] * k3 >> 16;
		model.method482(itemDef.spriteCameraRoll, itemDef.spriteCameraYaw, itemDef.spritePitch, itemDef.spriteTranslateX,
				l3 + model.modelBaseY / 2 + itemDef.spriteTranslateY, i4 + itemDef.spriteTranslateY);
		if (itemDef.certTemplateID != -1) {
			int l5 = sprite1.maxWidth;
			int j6 = sprite1.maxHeight;
			sprite1.maxWidth = 18;
			sprite1.maxHeight = 18;
			sprite1.drawSprite(0, 0);
			sprite1.maxWidth = l5;
			sprite1.maxHeight = j6;
		}
		Rasterizer2D.initDrawingArea(j2, i2, ai1, new float[1024]);
		Rasterizer2D.setDrawingArea(j3, k2, l2, i3);
		Rasterizer3D.originViewX = k1;
		Rasterizer3D.originViewY = l1;
		Rasterizer3D.scanOffsets = ai;
		Rasterizer3D.aBoolean1464 = true;

		enabledSprite.maxWidth = 18;
		enabledSprite.maxHeight = 18;

		return enabledSprite;
	}

	public static Sprite getSprite(int itemId, int itemAmount, int outlineColor) {
		if (outlineColor == 0) {
			Sprite sprite = (Sprite) mruNodes1.insertFromCache(itemId);
			if (sprite != null && sprite.maxHeight != itemAmount && sprite.maxHeight != -1) {
				sprite.unlink();
				sprite = null;
			}
			if (sprite != null)
				return sprite;
		}
		ItemDefinition itemDef = forID(itemId);
		if (itemDef.stackIDs == null)
			itemAmount = -1;
		if (itemAmount > 1) {
			int i1 = -1;
			for (int j1 = 0; j1 < 10; j1++)
				if (itemAmount >= itemDef.stackAmounts[j1] && itemDef.stackAmounts[j1] != 0)
					i1 = itemDef.stackIDs[j1];

			if (i1 != -1)
				itemDef = forID(i1);
		}
		Model model = itemDef.method201(1);
		if (model == null)
			return null;
		Sprite sprite = null;
		if (itemDef.certTemplateID != -1) {
			sprite = getSprite(itemDef.certID, 10, -1);
			if (sprite == null)
				return null;
		} else if (itemDef.notedId != -1) {
			sprite = getSprite(itemDef.unnotedId, itemAmount, -1);
			if (sprite == null)
				return null;
		} else if (itemDef.placeholderTemplateId != -1) {
			sprite = getSprite(itemDef.placeholderId, itemAmount, -1);
			if (sprite == null)
				return null;
		}
		Sprite sprite2 = new Sprite(32, 32);
		int k1 = Rasterizer3D.originViewX;
		int l1 = Rasterizer3D.originViewY;
		int ai[] = Rasterizer3D.scanOffsets;
		int ai1[] = Rasterizer2D.pixels;
		int i2 = Rasterizer2D.width;
		int j2 = Rasterizer2D.height;
		int k2 = Rasterizer2D.clip_left;
		int l2 = Rasterizer2D.clip_right;
		int i3 = Rasterizer2D.clip_top;
		int j3 = Rasterizer2D.clip_bottom;
		Rasterizer3D.aBoolean1464 = false;
		Rasterizer2D.initDrawingArea(32, 32, sprite2.myPixels, new float[1024]);
		Rasterizer2D.method336(32, 0, 0, 0, 32);
		Rasterizer3D.useViewport();
		if (itemDef.placeholderTemplateId != -1) {
			int l5 = sprite.maxWidth;
			int j6 = sprite.maxHeight;
			sprite.maxWidth = 32;
			sprite.maxHeight = 32;
			sprite.drawSprite(0, 0);
			sprite.maxWidth = l5;
			sprite.maxHeight = j6;
		}
		int k3 = itemDef.spriteScale;
		if (outlineColor == -1)
			k3 = (int) ((double) k3 * 1.5D);
		if (outlineColor > 0)
			k3 = (int) ((double) k3 * 1.04D);
		int l3 = Rasterizer3D.anIntArray1470[itemDef.spritePitch] * k3 >> 16;
		int i4 = Rasterizer3D.COSINE[itemDef.spritePitch] * k3 >> 16;
		model.method482(itemDef.spriteCameraRoll, itemDef.spriteCameraYaw, itemDef.spritePitch, itemDef.spriteTranslateX, l3 + model.modelBaseY / 2 + itemDef.spriteTranslateY, i4 + itemDef.spriteTranslateY);

		for (int i5 = 31; i5 >= 0; i5--) {
			for (int j4 = 31; j4 >= 0; j4--)
				if (sprite2.myPixels[i5 + j4 * 32] == 0)
					if (i5 > 0 && sprite2.myPixels[(i5 - 1) + j4 * 32] > 1)
						sprite2.myPixels[i5 + j4 * 32] = 1;
					else if (j4 > 0 && sprite2.myPixels[i5 + (j4 - 1) * 32] > 1)
						sprite2.myPixels[i5 + j4 * 32] = 1;
					else if (i5 < 31 && sprite2.myPixels[i5 + 1 + j4 * 32] > 1)
						sprite2.myPixels[i5 + j4 * 32] = 1;
					else if (j4 < 31 && sprite2.myPixels[i5 + (j4 + 1) * 32] > 1)
						sprite2.myPixels[i5 + j4 * 32] = 1;

		}

		if (outlineColor > 0) {
			for (int j5 = 31; j5 >= 0; j5--) {
				for (int k4 = 31; k4 >= 0; k4--)
					if (sprite2.myPixels[j5 + k4 * 32] == 0)
						if (j5 > 0 && sprite2.myPixels[(j5 - 1) + k4 * 32] == 1)
							sprite2.myPixels[j5 + k4 * 32] = outlineColor;
						else if (k4 > 0 && sprite2.myPixels[j5 + (k4 - 1) * 32] == 1)
							sprite2.myPixels[j5 + k4 * 32] = outlineColor;
						else if (j5 < 31 && sprite2.myPixels[j5 + 1 + k4 * 32] == 1)
							sprite2.myPixels[j5 + k4 * 32] = outlineColor;
						else if (k4 < 31 && sprite2.myPixels[j5 + (k4 + 1) * 32] == 1)
							sprite2.myPixels[j5 + k4 * 32] = outlineColor;

			}

		} else if (outlineColor == 0) {
			for (int k5 = 31; k5 >= 0; k5--) {
				for (int l4 = 31; l4 >= 0; l4--)
					if (sprite2.myPixels[k5 + l4 * 32] == 0 && k5 > 0 && l4 > 0
							&& sprite2.myPixels[(k5 - 1) + (l4 - 1) * 32] > 0)
						sprite2.myPixels[k5 + l4 * 32] = 0x302020;

			}

		}
		if (itemDef.notedId != -1) {
			int l5 = sprite.maxWidth;
			int j6 = sprite.maxHeight;
			sprite.maxWidth = 32;
			sprite.maxHeight = 32;
			sprite.drawSprite(0, 0);
			sprite.maxWidth = l5;
			sprite.maxHeight = j6;
		}
		if (itemDef.certTemplateID != -1) {
			int l5 = sprite.maxWidth;
			int j6 = sprite.maxHeight;
			sprite.maxWidth = 32;
			sprite.maxHeight = 32;
			sprite.drawSprite(0, 0);
			sprite.maxWidth = l5;
			sprite.maxHeight = j6;
		}
		if (outlineColor == 0) {
			mruNodes1.removeFromCache(sprite2, itemId);
		}
		sprite2.outline(1);
		if (outlineColor > 0) {
			sprite2.outline(16777215);
		}
		if (outlineColor == 0) {
			sprite2.shadow(3153952);
		}
		Rasterizer2D.initDrawingArea(j2, i2, ai1, new float[1024]);
		Rasterizer2D.setDrawingArea(j3, k2, l2, i3);
		Rasterizer3D.originViewX = k1;
		Rasterizer3D.originViewY = l1;
		Rasterizer3D.scanOffsets = ai;
		Rasterizer3D.aBoolean1464 = true;
		if (itemDef.stackable)
			sprite2.maxWidth = 33;
		else
			sprite2.maxWidth = 32;
		sprite2.maxHeight = itemAmount;
		return sprite2;
	}

	public Model method201(int i) {
		if (stackIDs != null && i > 1) {
			int j = -1;
			for (int k = 0; k < 10; k++)
				if (i >= stackAmounts[k] && stackAmounts[k] != 0)
					j = stackIDs[k];

			if (j != -1)
				return forID(j).method201(1);
		}
		Model model = (Model) mruNodes2.insertFromCache(id);
		if (model != null)
			return model;
		model = Model.getModel(modelId);
		if (model == null)
			return null;
		if (groundScaleX != 128 || groundScaleY != 128 || groundScaleZ != 128)
			model.scale(groundScaleX, groundScaleZ, groundScaleY);
		if (modifiedModelColors != null) {
			for (int l = 0; l < modifiedModelColors.length; l++)
				model.recolor(modifiedModelColors[l], originalModelColors[l]);

		}
		if (originalTextureColors != null) {
			for (int k2 = 0; k2 < originalTextureColors.length; k2++)
				model.retexture(originalTextureColors[k2], modifiedTextureColors[k2]);

		}
		int lightInt = 64 + ambient;
		int lightMag = 768 + contrast;
		try {
			model.light(lightInt, lightMag, -50, -10, -50, true);
		} catch(ArrayIndexOutOfBoundsException e) {
			e.printStackTrace();
		}
		model.fits_on_single_square = true;
		mruNodes2.removeFromCache(model, id);
		return model;
	}

	public Model method202(int i) {
		if (stackIDs != null && i > 1) {
			int j = -1;
			for (int k = 0; k < 10; k++)
				if (i >= stackAmounts[k] && stackAmounts[k] != 0)
					j = stackIDs[k];

			if (j != -1)
				return forID(j).method202(1);
		}
		Model model = Model.getModel(modelId);
		if (model == null)
			return null;
		if (modifiedModelColors != null) {
			for (int l = 0; l < modifiedModelColors.length; l++)
				model.recolor(modifiedModelColors[l], originalModelColors[l]);

		}
		if (originalTextureColors != null) {
			for (int k2 = 0; k2 < originalTextureColors.length; k2++)
				model.retexture(originalTextureColors[k2], modifiedTextureColors[k2]);

		}
		return model;
	}

	private ItemDefinition() {
		id = -1;
	}
	public static void Models(int i, int j, int k) {
		ItemDefinition class8 = cache[cacheIndex];
		class8.modelId = i;
		class8.primaryMaleModel = j;
		class8.primaryFemaleModel = k;
	}

	public static void NewColor(int i, int j, int k) {
		ItemDefinition class8 = cache[cacheIndex];
		class8.modifiedModelColors[k] = i;
		class8.originalModelColors[k] = j;
	}

	public static void NEO(String s, String s1, String s2) {
		ItemDefinition class8 = cache[cacheIndex];
		class8.itemActions = new String[5];
		class8.itemActions[1] = s2;
		class8.name = s;
		class8.description = s1;
	}

	public static void Zoom(int i, int j, int k, int l, int i1, boolean flag) {
		ItemDefinition class8 = cache[cacheIndex];
		class8.spriteScale = i;
		class8.spritePitch = l;
		class8.spriteCameraRoll = i1;
		class8.spriteTranslateX = k;
		class8.spriteTranslateY = j;
		class8.stackable = flag;
	}

	public static void Jukkycolors(int i, int j, int k) {
		ItemDefinition class8 = cache[cacheIndex];
		class8.modifiedModelColors[k] = i;
		class8.originalModelColors[k] = j;
	}

	public static void Jukkyzoom(int i, int j, int k, int l, int i1, int j1, int k1, int l1, boolean flag) {
		ItemDefinition class8 = cache[cacheIndex];
		class8.spriteScale = i;
		class8.spritePitch = j;
		class8.spriteCameraRoll = k;
		class8.spriteCameraYaw = l;
		class8.spriteTranslateX = i1;
		class8.spriteTranslateY = j1;
		class8.stackable = flag;
		class8.primaryMaleHeadPiece = k1;
		class8.primaryFemaleHeadPiece = l1;
	}

	public static void Jukkyname(String s, String s1) {
		ItemDefinition class8 = cache[cacheIndex];
		class8.itemActions = new String[5];
		class8.itemActions[1] = "Wear";
		class8.name = s;
		class8.description = s1;
	}

	public static void JukkyModels(int male, int malearms, int female, int femalearms, int dropmdl) {
		ItemDefinition class8 = cache[cacheIndex];
		class8.primaryMaleModel = male;
		class8.secondaryMaleModel = malearms;
		class8.primaryFemaleModel = female;
		class8.secondaryFemaleModel = femalearms;
		class8.modelId = dropmdl;
	}

	private byte femaleTranslation;
	public int value;
	public int[] originalModelColors;
	public int[] modifiedModelColors;

	public short[] originalTextureColors;
	public short[] modifiedTextureColors;

	public int id;
	public static MRUNodes mruNodes1 = new MRUNodes(100);
	public static MRUNodes mruNodes2 = new MRUNodes(50);

	public boolean membersObject;
	private int tertiaryFemaleEquipmentModel;
	public int certTemplateID;
	public int secondaryFemaleModel;
	public int primaryMaleModel;
	private int secondaryMaleHeadPiece;
	private int groundScaleX;
	public String groundActions[];
	public int spriteTranslateX;
	public String name;
	private static ItemDefinition[] cache;
	private int secondaryFemaleHeadPiece;
	public int modelId;
	public int primaryMaleHeadPiece;
	public boolean stackable;
	public String description;
	public int certID;
	private static int cacheIndex;
	public int spriteScale;
	private static Buffer stream;
	private int contrast;
	private int tertiaryMaleEquipmentModel;
	public int secondaryMaleModel;
	public String itemActions[];
	public String equipActions[];
	public int spritePitch;
	private int groundScaleZ;
	private int groundScaleY;
	public int[] stackIDs;
	public int spriteTranslateY;
	private static int[] streamIndices;
	private int ambient;
	public int primaryFemaleHeadPiece;
	public int spriteCameraRoll;
	public int primaryFemaleModel;
	public int[] stackAmounts;
	private int shiftClickIndex = -2;
	public int team;
	public static int totalItems;
	public int spriteCameraYaw;
	private byte maleTranslation;
	HashMap params;
	public boolean searchable;
	private static BufferedWriter writer;

}
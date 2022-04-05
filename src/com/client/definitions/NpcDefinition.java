package com.client.definitions;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
//import java.io.FileNotFoundException;
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.util.Arrays;
//
//import org.apache.commons.io.FileUtils;
import java.io.IOException;

import com.client.Frame;
import com.client.Client;
import com.client.Configuration;
import com.client.MRUNodes;
import com.client.Model;
import com.client.Stream;
import com.client.FileArchive;
import com.client.sign.Signlink;

public final class NpcDefinition {



	public static NpcDefinition forID(int i) {
		for (int j = 0; j < 20; j++)
			if (cache[j].interfaceType == i)
				return cache[j];

		anInt56 = (anInt56 + 1) % 20;
		NpcDefinition entityDef = cache[anInt56] = new NpcDefinition();
		stream.currentPosition = streamIndices[i];
		entityDef.interfaceType = i;
		entityDef.readValues(stream);


		if (i== 4625){
			entityDef.name = "Donator shop";
			entityDef.actions = new String[] { "Talk-to", null, "Trade", null, null };
		}
	
		if (i == 8480) {
			entityDef.name = "Mystical Wizard";
			entityDef.actions = new String[] { "Teleport", "Previous Location", null, null, null };
			entityDef.description = "This wizard has the power to teleport you to many locations.";
			entityDef.standAnim = 808;
			entityDef.walkAnim = 819;
		}

		return entityDef;
	}

	public static int totalAmount;

	public static void unpackConfig(FileArchive streamLoader) {
		stream = new Stream(streamLoader.getDataForName("npc.dat"));
		Stream stream = new Stream(streamLoader.getDataForName("npc.idx"));
		totalAmount = stream.readUnsignedShort();
		streamIndices = new int[totalAmount];
		int i = 2;
		for (int j = 0; j < totalAmount; j++) {
			streamIndices[j] = i;
			i += stream.readUnsignedShort();
		}

		cache = new NpcDefinition[20];
		for (int k = 0; k < 20; k++)
			cache[k] = new NpcDefinition();
		/*for (int index = 0; index < totalAmount; index++) {
			NpcDefinition ed = forID(index);
			if (ed == null)
				continue;
			if (ed.name == null)
				continue;
		}*/
		   //dumpNpcConfig();
		//dumpNpcList();
	}

	public static void dumpNpcConfig() {
for(int i = 0; i < totalAmount; i++) {
NpcDefinition class5 = forID(i);
BufferedWriter bw = null;
try {
bw = new BufferedWriter(new FileWriter(Signlink.getCacheDirectory() + "/dumps/npc_config196.cfg", true));
if(class5.name!= null) {
bw.write("npc = "+i+"\t"+class5.name.replace(" ","_"));
bw.newLine();
bw.flush();
bw.close();
}
} catch (IOException ioe2) {
}
}
}
	private void readValues(Stream stream) {
		int last = -1;
		while (true) {
			int opcode = stream.readUnsignedByte();
			if (opcode == 0)
				return;
			if (opcode == 1) {
				int j = stream.readUnsignedByte();
				models = new int[j];
				for (int j1 = 0; j1 < j; j1++)
					models[j1] = stream.readUnsignedShort();

			} else if (opcode == 2)
				name = stream.readString();
			else if (opcode == 3)
				description = stream.readString();
			else if (opcode == 12)
				boundDim = stream.readSignedByte();
			else if (opcode == 13)
				standAnim = stream.readUnsignedShort();
			else if (opcode == 14)
				walkAnim = stream.readUnsignedShort();
			else if(opcode == 15 || opcode == 16)
				stream.readUnsignedShort();
			else if (opcode == 17) {
				walkAnim = stream.readUnsignedShort();
				anInt58 = stream.readUnsignedShort();
				anInt83 = stream.readUnsignedShort();
				anInt55 = stream.readUnsignedShort();
				if (anInt58 == 65535) {
					anInt58 = -1;
				}
				if (anInt83 == 65535) {
					anInt83 = -1;
				}
				if (anInt55 == 65535) {
					anInt55 = -1;
				}
			} else if(opcode == 18){
						Category = stream.readUnsignedShort();
			} else if (opcode >= 30 && opcode < 40) {
				if (actions == null)
					actions = new String[10];
				actions[opcode - 30] = stream.readString();
				if (actions[opcode - 30].equalsIgnoreCase("hidden"))
					actions[opcode - 30] = null;
			} else if (opcode == 40) {
				int k = stream.readUnsignedByte();
				originalColors = new int[k];
				newColors = new int[k];
				for (int k1 = 0; k1 < k; k1++) {
					originalColors[k1] = stream.readUnsignedShort();
					newColors[k1] = stream.readUnsignedShort();
				}
			} else if (opcode == 41) {
				int k = stream.readUnsignedByte();
				for (int k1 = 0; k1 < k; k1++) {
					stream.readUnsignedShort();
					stream.readUnsignedShort();
				}

			} else if (opcode == 60) {
				int l = stream.readUnsignedByte();
				dialogueModels = new int[l];
				for (int l1 = 0; l1 < l; l1++)
					dialogueModels[l1] = stream.readUnsignedShort();

			} else if (opcode == 93)
				onMinimap = false;
			else if (opcode == 95)
				combatLevel = stream.readUnsignedShort();
			else if (opcode == 97)
				anInt91 = stream.readUnsignedShort();
			else if (opcode == 98)
				anInt86 = stream.readUnsignedShort();
			else if (opcode == 99)
				aBoolean93 = true;
			else if (opcode == 100)
				anInt85 = stream.readSignedByte();
			else if (opcode == 101)
				anInt92 = stream.readSignedByte();
			else if (opcode == 102)
				anInt75 = stream.readUnsignedShort();
			else if (opcode == 103)
				getDegreesToTurn = stream.readUnsignedShort();
			else if (opcode == 106 || opcode == 118) {
				anInt57 = stream.readUnsignedShort();
				if (anInt57 == 65535)
					anInt57 = -1;
				anInt59 = stream.readUnsignedShort();
				if (anInt59 == 65535)
					anInt59 = -1;
				
				int var3 = -1;
				if(opcode == 118) {
					var3 = stream.readUnsignedShort();
				}
				int i1 = stream.readUnsignedByte();
				childrenIDs = new int[i1 + 2];
				for (int i2 = 0; i2 <= i1; i2++) {
					childrenIDs[i2] = stream.readUnsignedShort();
					if (childrenIDs[i2] == 65535)
						childrenIDs[i2] = -1;
				}
				childrenIDs[i1 + 1] = var3;
				
			} else if (opcode == 107)
				aBoolean84 = false;
			else if(opcode == 111 || opcode == 107 || opcode == 109) {
				
			} else {
				System.out.println("Missing NPC opcode " + opcode + "last: " + last);
				continue;
			}
			last = opcode;
		}
	}

	public Model method160() {
		if (childrenIDs != null) {
			NpcDefinition entityDef = method161();
			if (entityDef == null)
				return null;
			else
				return entityDef.method160();
		}
		if (dialogueModels == null) {
			return null;
		}
		boolean flag1 = false;
		for (int i = 0; i < dialogueModels.length; i++)
			if (!Model.isCached(dialogueModels[i]))
				flag1 = true;

		if (flag1)
			return null;
		Model aclass30_sub2_sub4_sub6s[] = new Model[dialogueModels.length];
		for (int j = 0; j < dialogueModels.length; j++)
			aclass30_sub2_sub4_sub6s[j] = Model.getModel(dialogueModels[j]);

		Model model;
		if (aclass30_sub2_sub4_sub6s.length == 1)
			model = aclass30_sub2_sub4_sub6s[0];
		else
			model = new Model(aclass30_sub2_sub4_sub6s.length, aclass30_sub2_sub4_sub6s);

		if (originalColors != null)
			for (int k = 0; k < originalColors.length; k++)
				model.recolor(originalColors[k], newColors[k]);

		return model;
	}

	public NpcDefinition method161() {
		int j = -1;
		if (anInt57 != -1) {
			VarBit varBit = VarBit.cache[anInt57];
			int k = varBit.anInt648;
			int l = varBit.anInt649;
			int i1 = varBit.anInt650;
			int j1 = Client.anIntArray1232[i1 - l];
			j = clientInstance.variousSettings[k] >> l & j1;
		} else if (anInt59 != -1)
			j = clientInstance.variousSettings[anInt59];
		int var2;
		if(j >= 0 && j < childrenIDs.length)
			var2 = childrenIDs[j];
		else
			var2 = childrenIDs[childrenIDs.length - 1];
		
			return var2 == -1 ? null : forID(var2);
	}

	public Model method164(int j, int k, int ai[]) {
		if (childrenIDs != null) {
			NpcDefinition entityDef = method161();
			if (entityDef == null)
				return null;
			else
				return entityDef.method164(j, k, ai);
		}
		Model model = (Model) mruNodes.insertFromCache(interfaceType);
		if (model == null) {
			boolean flag = false;
			for (int i1 = 0; i1 < models.length; i1++)
				if (!Model.isCached(models[i1]))
					flag = true;

			if (flag)
				return null;
			Model aclass30_sub2_sub4_sub6s[] = new Model[models.length];
			for (int j1 = 0; j1 < models.length; j1++)
				aclass30_sub2_sub4_sub6s[j1] = Model.getModel(models[j1]);

			if (aclass30_sub2_sub4_sub6s.length == 1)
				model = aclass30_sub2_sub4_sub6s[0];
			else
				model = new Model(aclass30_sub2_sub4_sub6s.length, aclass30_sub2_sub4_sub6s);
			if (originalColors != null) {
				for (int k1 = 0; k1 < originalColors.length; k1++)
					model.recolor(originalColors[k1], newColors[k1]);

			}
			model.skin();
			model.light(64 + anInt85, 850 + anInt92, -30, -50, -30, true);
			// model.method479(84 + anInt85, 1000 + anInt92, -90, -580, -90, true);
			mruNodes.removeFromCache(model, interfaceType);
		}
		Model model_1 = Model.EMPTY_MODEL;
		model_1.method464(model, Frame.noAnimationInProgress(k) & Frame.noAnimationInProgress(j));
		if (k != -1 && j != -1)
			model_1.applyAnimationFrames(ai, j, k);
		else if (k != -1)
			model_1.applyTransform(k);
		if (anInt91 != 128 || anInt86 != 128)
			model_1.scale(anInt91, anInt91, anInt86);
		model_1.calculateDistances();
		model_1.faceGroups = null;
		model_1.vertexGroups = null;
		if (boundDim == 1)
			model_1.fits_on_single_square = true;
		return model_1;
	}

	private NpcDefinition() {
		anInt55 = -1;
		anInt57 = walkAnim;
		anInt58 = walkAnim;
		anInt59 = walkAnim;
		combatLevel = -1;
		anInt64 = 1834;
		walkAnim = -1;
		boundDim = 1;
		anInt75 = -1;
		standAnim = -1;
		interfaceType = -1L;
		getDegreesToTurn = 32;
		anInt83 = -1;
		aBoolean84 = true;
		anInt86 = 128;
		onMinimap = true;
		anInt91 = 128;
		aBoolean93 = false;
		Category = -1;
	}

	public static void nullLoader() {
		mruNodes = null;
		streamIndices = null;
		cache = null;
		stream = null;
	}

	public static void dumpList() {
		try {
			File file = new File(System.getProperty("user.home") + "/Desktop/npcList 178" + Configuration.dumpID + ".txt");

			if (!file.exists()) {
				file.createNewFile();
			}

			try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
				for (int i = 0; i < totalAmount; i++) {
					NpcDefinition definition = forID(i);
					if (definition != null) {
						writer.write("npc = " + i + "\t" + definition.name + "\t" + definition.combatLevel + "\t"
								+ definition.standAnim + "\t" + definition.walkAnim + "\t");
						writer.newLine();
					}
				}
			}

			System.out.println("Finished dumping npc definitions.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void dumpNpcList() {
		for(int i = 0; i < totalAmount; ++i) {
			NpcDefinition class5 = forID(i);
			BufferedWriter bw = null;

			try {
				bw = new BufferedWriter(new FileWriter(Signlink.getCacheDirectory() + "/dumps/197Npclist.txt", true));
				if(class5.name != null) {
					bw.write("case " + i + "://" + class5.name);
					bw.newLine();
					bw.flush();
					bw.close();
				}
			} catch (IOException var4) {
			}
		}

	}

	public static void dumpSizes() {
		try {
			File file = new File(System.getProperty("user.home") + "/Desktop/npcSizes 143.txt");

			if (!file.exists()) {
				file.createNewFile();
			}

			try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
				for (int i = 0; i < totalAmount; i++) {
					NpcDefinition definition = forID(i);
					if (definition != null) {
						writer.write(i + "	" + definition.boundDim);
						writer.newLine();
					}
				}
			}

			System.out.println("Finished dumping npc definitions.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public int anInt55;
	public static int anInt56;
	public int anInt57;
	public int anInt58;
	public int anInt59;
	public static Stream stream;
	public int combatLevel;
	public final int anInt64;
	public String name;
	public String actions[];
	public int walkAnim;
	public byte boundDim;
	public int[] newColors;
	public static int[] streamIndices;
	public int[] dialogueModels;
	public int anInt75;
	public int[] originalColors;
	public int standAnim;
	public long interfaceType;
	public int getDegreesToTurn;
	public static NpcDefinition[] cache;
	public static Client clientInstance;
	public int anInt83;
	public boolean aBoolean84;
	public int anInt85;
	public int anInt86;
	public boolean onMinimap;
	public int childrenIDs[];
	public String description;
	public int anInt91;
	public int anInt92;
	public boolean aBoolean93;
	public int[] models;
	private int Category;
	public static MRUNodes mruNodes = new MRUNodes(30);

}
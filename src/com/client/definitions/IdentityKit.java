package com.client.definitions;
// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

import com.client.Model;
import com.client.Stream;
import com.client.FileArchive;

public final class IdentityKit {

	public static void unpackConfig(FileArchive streamLoader) {
		Stream stream = new Stream(streamLoader.getDataForName("idk.dat"));
		length = stream.readUnsignedShort();
		if (cache == null)
			cache = new IdentityKit[length];
		for (int j = 0; j < length; j++) {
			if (cache[j] == null)
				cache[j] = new IdentityKit();
			cache[j].readValues(stream);
			cache[j].originalColors[0] = 55232;
			cache[j].replacementColors[0] = 6798;
		}
	}

	private void readValues(Stream stream) {
		do {
			int opcode = stream.readUnsignedByte();
			if (opcode == 0)
				return;
			if (opcode == 1)
				bodyPartId = stream.readUnsignedByte();
			else if (opcode == 2) {
				int j = stream.readUnsignedByte();
				bodyModels = new int[j];
				for (int k = 0; k < j; k++)
					bodyModels[k] = stream.readUnsignedShort();

			} else if (opcode == 3)
				validStyle = true;
			else if (opcode >= 40 && opcode < 50)
				originalColors[opcode - 40] = stream.readUnsignedShort();
			else if (opcode >= 50 && opcode < 60)
				replacementColors[opcode - 50] = stream.readUnsignedShort();
			else if (opcode >= 60 && opcode < 70)
				headModels[opcode - 60] = stream.readUnsignedShort();
			else
				System.out.println("Error unrecognised config code: " + opcode);
		} while (true);
	}

	public boolean method537() {
		if (bodyModels == null)
			return true;
		boolean flag = true;
		for (int j = 0; j < bodyModels.length; j++)
			if (!Model.isCached(bodyModels[j]))
				flag = false;

		return flag;
	}

	public Model method538() {
		if (bodyModels == null)
			return null;
		Model aclass30_sub2_sub4_sub6s[] = new Model[bodyModels.length];
		for (int i = 0; i < bodyModels.length; i++)
			aclass30_sub2_sub4_sub6s[i] = Model.getModel(bodyModels[i]);

		Model model;
		if (aclass30_sub2_sub4_sub6s.length == 1)
			model = aclass30_sub2_sub4_sub6s[0];
		else
			model = new Model(aclass30_sub2_sub4_sub6s.length,
					aclass30_sub2_sub4_sub6s);
		for (int j = 0; j < 6; j++) {
			if (originalColors[j] == 0)
				break;
			model.recolor(originalColors[j], replacementColors[j]);
		}

		return model;
	}

	public boolean method539() {
		boolean flag1 = true;
		for (int i = 0; i < 5; i++)
			if (headModels[i] != -1 && !Model.isCached(headModels[i]))
				flag1 = false;

		return flag1;
	}

	public Model method540() {
		Model aclass30_sub2_sub4_sub6s[] = new Model[5];
		int j = 0;
		for (int k = 0; k < 5; k++)
			if (headModels[k] != -1)
				aclass30_sub2_sub4_sub6s[j++] = Model
						.getModel(headModels[k]);

		Model model = new Model(j, aclass30_sub2_sub4_sub6s);
		for (int l = 0; l < 6; l++) {
			if (originalColors[l] == 0)
				break;
			model.recolor(originalColors[l], replacementColors[l]);
		}

		return model;
	}

	private IdentityKit() {
		bodyPartId = -1;
		originalColors = new int[6];
		replacementColors = new int[6];
		validStyle = false;
	}

	public static int length;
	public static IdentityKit cache[];
	public int bodyPartId;
	private int[] bodyModels;
	private final int[] originalColors;
	private final int[] replacementColors;
	private final int[] headModels = { -1, -1, -1, -1, -1 };
	public boolean validStyle;
}

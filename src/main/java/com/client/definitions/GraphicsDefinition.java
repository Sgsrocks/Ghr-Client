package com.client.definitions;

import com.client.MRUNodes;
import com.client.Model;
import com.client.Buffer;
import com.client.FileArchive;

public final class GraphicsDefinition {

	public static void unpackConfig(FileArchive streamLoader) {
		Buffer stream = new Buffer(streamLoader.readFile("spotanim.dat"));
		int length = stream.readUnsignedShort();
		if (cache == null)
			cache = new GraphicsDefinition[length + 15000];
		for (int j = 0; j < length; j++) {
			if (cache[j] == null) {
				cache[j] = new GraphicsDefinition();
			}
			if (j == 65535) {
				j = -1;
			}
			cache[j].index = j;
			cache[j].setDefault();
			cache[j].readValues(stream);
		}
		cache[1282] = new GraphicsDefinition();
		cache[1282].index = 1282;
		cache[1282].modelId = 44811;
		cache[1282].anInt406 = 7155;
		cache[1282].aAnimation_407 = AnimationDefinition.anims[cache[1282].anInt406];
	}

	private void readValues(Buffer stream) {
		while(true) {
			int i = stream.readUnsignedByte();
			if (i == 0) {
				return;
			}
			if (i == 1) {
				modelId = stream.readUnsignedShort();
			} else if (i == 2) {
				anInt406 = stream.readUnsignedShort();
				if (AnimationDefinition.anims != null) {
					aAnimation_407 = AnimationDefinition.anims[anInt406];
				}
			} else if (i == 4) {
				anInt410 = stream.readUnsignedShort();
			} else if (i == 5) {
				anInt411 = stream.readUnsignedShort();
			} else if (i == 6) {
				anInt412 = stream.readUnsignedShort();
			} else if (i == 7) {
				anInt413 = stream.readUnsignedByte();
			} else if (i == 8) {
				anInt414 = stream.readUnsignedByte();
			} else if (i == 40) {
				int j = stream.readUnsignedByte();
				for (int k = 0; k < j; k++) {
					anIntArray408[k] = stream.readUnsignedShort();
					anIntArray409[k] = stream.readUnsignedShort();
				}
			} else if (i == 41) {
				final int len = stream.readUnsignedByte();
				textureToFind = new short[len];
				textureToReplace = new short[len];
				for (int k = 0; k < len; k++) {
					textureToFind[k] = (short) stream.readUnsignedShort();
					textureToReplace[k] = (short) stream.readUnsignedShort();
				}
			} else {
				System.out.println("Error unrecognised spotanim config code: " + i);
			}
		}
	}

	
	public static GraphicsDefinition fetch(int modelId) {
		for (GraphicsDefinition anim : cache) {
			if (anim == null) {
				continue;
			}
			if (anim.modelId == modelId) {
				return anim;
			}
		}
		return null;
	}

	public Model getModel() {
		Model model = (Model) aMRUNodes_415.insertFromCache(index);
		if (model != null)
			return model;
		model = Model.getModel(modelId);
		if (model == null)
			return null;
		for (int i = 0; i < anIntArray408.length; i++)
			if (anIntArray408[0] != 0) //default frame id
				model.recolor(anIntArray408[i], anIntArray409[i]);

		if (textureToFind != null)
			for (int i = 0; i < textureToReplace.length; i++)
				model.retexture(textureToFind[i], textureToReplace[i]);

		aMRUNodes_415.removeFromCache(model, index);
		return model;
	}
	
	private void setDefault() {
		modelId = -1;
		anInt406 = -1;
		anIntArray408 = new int[6];
		anIntArray409 = new int[6];
		anInt410 = 128;
		anInt411 = 128;
		anInt412 = 0;
		anInt413 = 0;
		anInt414 = 0;
	}

	public GraphicsDefinition() {
		anInt400 = 9;
		anInt406 = -1;
		anIntArray408 = new int[6];
		anIntArray409 = new int[6];
		anInt410 = 128;
		anInt411 = 128;
	}
	
	public int getModelId() {
		return modelId;
	}
	
	public int getIndex() {
		return index;
	}

	public final int anInt400;
	public static GraphicsDefinition cache[];
	private int index;
	private int modelId;
	public int anInt406;
	public AnimationDefinition aAnimation_407;
	public int[] anIntArray408;
	public int[] anIntArray409;
	private short[] textureToFind;
	private short[] textureToReplace;
	public int anInt410;
	public int anInt411;
	public int anInt412;
	public int anInt413;
	public int anInt414;
	public static MRUNodes aMRUNodes_415 = new MRUNodes(30);

}

package com.client.definitions;

import com.client.Buffer;
import com.client.FileArchive;

public final class VarBit {

	public static void unpackConfig(FileArchive streamLoader) {
		Buffer stream = new Buffer(streamLoader.readFile("varbit.dat"));
		int cacheSize = stream.readUnsignedShort();
		if (cache == null)
			cache = new VarBit[cacheSize];
		for (int j = 0; j < cacheSize; j++) {
			if (cache[j] == null)
				cache[j] = new VarBit();
			cache[j].readValues(stream);
		}

		if (stream.currentPosition != stream.payload.length)
			System.out.println("varbit load mismatch");
	}

	private void readValues(Buffer stream) {
		int opcode = stream.readUnsignedByte();

		if (opcode == 0) {
			return;
		} else if (opcode == 1) {
		anInt648 = stream.readUnsignedShort();
		anInt649 = stream.readUnsignedByte();
		anInt650 = stream.readUnsignedByte();
		} else {
			System.out.println(opcode);
		}
	}
	private VarBit() {
		
	}

	public static VarBit cache[];
	public int anInt648;
	public int anInt649;
	public int anInt650;
	
}

package com.client.definitions;

import com.client.Configuration;
import com.client.Frame;
import com.client.Buffer;
import com.client.FileArchive;

public final class AnimationDefinition {

	public static void unpackConfig(FileArchive streamLoader) {
		Buffer stream = new Buffer(streamLoader.readFile("seq.dat"));
		int length = stream.readUnsignedShort();
		if (anims == null)
			anims = new AnimationDefinition[length];
		for (int j = 0; j < length; j++) {
			if (anims[j] == null)
				anims[j] = new AnimationDefinition();
				anims[j].readValues(stream);
			switch (j) {

			}

			if (Configuration.dumpAnimationData) {
				if (anims[j].durations != null && anims[j].durations.length > 0) {
					int sum = 0;
					for (int i = 0; i < anims[j].durations.length; i++) {
						if (anims[j].durations[i] < 100) {
							sum += anims[j].durations[i];
						}
					}

					System.out.println(j + ":" + sum);
				}
			}
		}
	}
	public int getFrameSound(int frameIndex) {
		if (frameSounds != null && frameIndex < frameSounds.length && frameSounds[frameIndex] != 0) {
			return frameSounds[frameIndex];
		} else {
			return -1;
		}
	}
	public int method258(int i) {
		int j = durations[i];
		if (j == 0) {
			Frame class36 = Frame.method531(primaryFrames[i]);
			if (class36 != null)
				j = durations[i] = class36.anInt636;
		}
		if (j == 0)
			j = 1;
		return j;
	}

	private void readValues(Buffer stream) {
		int i;
		while ((i = stream.readUnsignedByte()) != 0) {

			if (i == 1) {
				anInt352 = stream.readUnsignedShort();
				primaryFrames = new int[anInt352];
				secondaryFrames = new int[anInt352];
				durations = new int[anInt352];

				for (int j = 0; j < anInt352; j++)
					durations[j] = stream.readUnsignedShort();


				for (int j = 0; j < anInt352; j++) {
					primaryFrames[j] = stream.readUnsignedShort();
					secondaryFrames[j] = -1;
				}

				for (int j = 0; j < anInt352; j++) {
					primaryFrames[j] += stream.readUnsignedShort() << 16;
				}
			} else if (i == 2)
				anInt356 = stream.readUnsignedShort();
			else if (i == 3) {
				int k = stream.readUnsignedByte();
				anIntArray357 = new int[k + 1];
				for (int l = 0; l < k; l++)
					anIntArray357[l] = stream.readUnsignedByte();
				anIntArray357[k] = 9999999;
			} else if (i == 4) {
				aBoolean358 = true;
		} else if (i == 5) {
				anInt359 = stream.readUnsignedByte();
		} else if (i == 6) {
				anInt360 = stream.readUnsignedShort();
		} else if (i == 7) {
				anInt361 = stream.readUnsignedShort();
		} else if (i == 8) {
				anInt362 = stream.readUnsignedByte();
		} else if (i == 9) {
				anInt363 = stream.readUnsignedByte();
		} else if (i == 10) {
				anInt364 = stream.readUnsignedByte();
		} else if (i == 11) {
				anInt365 = stream.readUnsignedByte();
		} else if (i == 12) {
			int len = stream.readUnsignedByte();

			for (int i1 = 0; i1 < len; i1++) {
				stream.readUnsignedShort();
			}

			for (int i1 = 0; i1 < len; i1++) {
				stream.readUnsignedShort();
			}
			} else if (i == 13) {
				int var3 = stream.readUnsignedByte();
				frameSounds = new int[var3];
				for (int var4 = 0; var4 < var3; ++var4) {
					frameSounds[var4] = stream.read24BitInt();
					if (0 != frameSounds[var4]) {
						int var6 = frameSounds[var4] >> 8;
						int var8 = frameSounds[var4] >> 4 & 7;
						int var9 = frameSounds[var4] & 15;
						frameSounds[var4] = var6;
					}
				}
			}
		}
		if (anInt352 == 0) {
			anInt352 = 1;
			primaryFrames = new int[1];
			primaryFrames[0] = -1;
			secondaryFrames = new int[1];
			secondaryFrames[0] = -1;
			durations = new int[1];
			durations[0] = -1;
		}
		if (anInt363 == -1)
			if (anIntArray357 != null)
				anInt363 = 2;
			else
				anInt363 = 0;
		if (anInt364 == -1) {
			if (anIntArray357 != null) {
				anInt364 = 2;
				return;
			}
			anInt364 = 0;
		}
	}

	public AnimationDefinition() {
		anInt356 = -1;
		aBoolean358 = false;
		anInt359 = 5;
		anInt360 = -1;
		anInt361 = -1;
		anInt362 = 99;
		anInt363 = -1;
		anInt364 = -1;
		anInt365 = 2;
	}

	public static AnimationDefinition anims[];
	public int anInt352;
	public int primaryFrames[];
	public int secondaryFrames[];
	public int[] durations;
	public int anInt356;
	public int anIntArray357[];
	public boolean aBoolean358;
	public int frameSounds[];
	public int anInt359;
	public int anInt360;
	public int anInt361;
	public int anInt362;
	public int anInt363;
	public int anInt364;
	public int anInt365;
	public static int anInt367;
}
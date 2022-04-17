package com.client;
final class Sounds {

	private Sounds() {
		samples = new Synthesizer[10];
	}

	public static void unpack(Buffer stream) {
		output = new byte[0x6baa8];
		stream = new Buffer(output);
		Synthesizer.method166();
		do {
			int j = stream.readUnsignedShort();
			if (j == 65535)
				return;
			TRACKS[j] = new Sounds();
			TRACKS[j].decode(stream);
			anIntArray326[j] = TRACKS[j].method243();
		} while (true);
	}

	public static Buffer method241(int i, int j) {
		if (TRACKS[j] != null) {
			Sounds sounds = TRACKS[j];
			return sounds.pack(i);
		} else {
			return null;
		}
	}

	private void decode(Buffer stream) {
		for (int i = 0; i < 10; i++) {
			int j = stream.readUnsignedByte();
			if (j != 0) {
				stream.currentPosition--;
				samples[i] = new Synthesizer();
				samples[i].method169(stream);
			}
		}
		anInt330 = stream.readUnsignedShort();
		anInt331 = stream.readUnsignedShort();
	}

	private int method243() {
		int j = 0x98967f;
		for (int k = 0; k < 10; k++)
			if (samples[k] != null && samples[k].remaining / 20 < j)
				j = samples[k].remaining / 20;

		if (anInt330 < anInt331 && anInt330 / 20 < j)
			j = anInt330 / 20;
		if (j == 0x98967f || j == 0)
			return 0;
		for (int l = 0; l < 10; l++)
			if (samples[l] != null)
				samples[l].remaining -= j * 20;

		if (anInt330 < anInt331) {
			anInt330 -= j * 20;
			anInt331 -= j * 20;
		}
		return j;
	}

	private Buffer pack(int i) {
		int k = encode(i);
		stream.currentPosition = 0;
		stream.writeDWord(0x52494646);
		stream.method403(36 + k);
		stream.writeDWord(0x57415645);
		stream.writeDWord(0x666d7420);
		stream.method403(16);
		stream.method400(1);
		stream.method400(1);
		stream.method403(22050);
		stream.method403(22050);
		stream.method400(1);
		stream.method400(8);
		stream.writeDWord(0x64617461);
		stream.method403(k);
		stream.currentPosition += k;
		return stream;
	}

	private int encode(int i) {
		int j = 0;
		for (int k = 0; k < 10; k++)
			if (samples[k] != null
					&& samples[k].anInt113
							+ samples[k].remaining > j)
				j = samples[k].anInt113 + samples[k].remaining;

		if (j == 0)
			return 0;
		int l = (22050 * j) / 1000;
		int i1 = (22050 * anInt330) / 1000;
		int j1 = (22050 * anInt331) / 1000;
		if (i1 < 0 || i1 > l || j1 < 0 || j1 > l || i1 >= j1)
			i = 0;
		int k1 = l + (j1 - i1) * (i - 1);
		for (int l1 = 44; l1 < k1 + 44; l1++)
			output[l1] = -128;

		for (int i2 = 0; i2 < 10; i2++)
			if (samples[i2] != null) {
				int j2 = (samples[i2].anInt113 * 22050) / 1000;
				int i3 = (samples[i2].remaining * 22050) / 1000;
				int ai[] = samples[i2].method167(j2,
						samples[i2].anInt113);
				for (int l3 = 0; l3 < j2; l3++)
					output[l3 + i3 + 44] += (byte) (ai[l3] >> 8);

			}

		if (i > 1) {
			i1 += 44;
			j1 += 44;
			l += 44;
			int k2 = (k1 += 44) - l;
			for (int j3 = l - 1; j3 >= j1; j3--)
				output[j3 + k2] = output[j3];

			for (int k3 = 1; k3 < i; k3++) {
				int l2 = (j1 - i1) * k3;
				System.arraycopy(output, i1, output, i1 + l2, j1
						- i1);

			}

			k1 -= 44;
		}
		return k1;
	}

	private static final Sounds[] TRACKS = new Sounds[5000];
	public static final int[] anIntArray326 = new int[5000];
	private static byte[] output;
	private static Buffer stream;
	private final Synthesizer[] samples;
	private int anInt330;
	private int anInt331;

}

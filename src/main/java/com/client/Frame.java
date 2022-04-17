package com.client;



public final class Frame {

	public static void load(int file, byte[] array) {
		if (array.length == 0) {
			return;
		}

		try {
			final Buffer stream = new Buffer(array);
			final FrameBase b2 = new FrameBase(stream);
			final int n = stream.readUShort();
			animationlist[file] = new Frame[n * 3];
			final int[] array2 = new int[500];
			final int[] array3 = new int[500];
			final int[] array4 = new int[500];
			final int[] array5 = new int[500];

			for (int j = 0; j < n; ++j) {
				final int k = stream.readUShort();
				final Frame[] array6 = animationlist[file];
				final Frame q = new Frame();
				array6[k] = q;
				q.base = b2;
				final int f = stream.readUnsignedByte();
				int c2 = 0;
				int n3 = -1;
				for (int l = 0; l < f; ++l) {
					final int f2;
					if ((f2 = stream.readUnsignedByte()) > 0) {
						if (b2.transformationType[l] != 0) {
							for (int n4 = l - 1; n4 > n3; --n4) {
								if (b2.transformationType[n4] == 0) {
									array2[c2] = n4;
									array3[c2] = 0;
									array5[c2] = (array4[c2] = 0);
									++c2;
									break;
								}
							}
						}
						array2[c2] = l;
						int n4 = 0;
						if (b2.transformationType[l] == 3) {
							n4 = 128;
						}
						if ((f2 & 0x1) != 0x0) {
							array3[c2] = stream.readShort2();
						} else {
							array3[c2] = n4;
						}
						if ((f2 & 0x2) != 0x0) {
							array4[c2] = stream.readShort2();
						} else {
							array4[c2] = n4;
						}
						if ((f2 & 0x4) != 0x0) {
							array5[c2] = stream.readShort2();
						} else {
							array5[c2] = n4;
						}
						n3 = l;
						++c2;
					}
				}
				q.transformationCount = c2;
				q.transformationIndices = new int[c2];
				q.transformX = new int[c2];
				q.transformY = new int[c2];
				q.transformZ = new int[c2];

				for (int l = 0; l < c2; ++l) {
					q.transformationIndices[l] = array2[l];
					q.transformX[l] = array3[l];
					q.transformY[l] = array4[l];
					q.transformZ[l] = array5[l];
				}
			}
		} catch (Exception e) {
			System.out.println(file+" - failed");
			e.printStackTrace();
		}
	}

	public static Frame method531(int frame) {
		try {
			int file = frame >> 16;
			int k = frame & 0xffff;

			if (animationlist[file].length == 0) {
				clientInstance.onDemandFetcher.method558(1, file);
				return null;
			}

			return animationlist[file][k];
		} catch (Exception e) {
			System.out.println(frame+" - failed");
			e.printStackTrace();
		}
		return null;
	}

	public static void nullLoader() {
		animationlist = null;
	}

	public static boolean noAnimationInProgress(int i) {
		return i == -1;
	}
	
	public static Frame animationlist[][];
	public int anInt636;
	public FrameBase base;
	public int transformationCount;
	public int transformationIndices[];
	public int transformX[];
	public int transformY[];
	public int transformZ[];
	public static Client clientInstance;

}

package com.client;

final class WorldController {

    public WorldController(int ai[][][]) {
        int i = 104;// was parameter
        int j = 104;// was parameter
        int k = 4;// was parameter
        aBoolean434 = true;
        gameObjectsCache = new GameObject[5000];
        anIntArray486 = new int[10000];
        anIntArray487 = new int[10000];
        numberOfZ = k;
        xRegionSize = j;
        yRegionSize = i;
        tiles = new Tile[k][j][i];
        anIntArrayArrayArray445 = new int[k][j + 1][i + 1];
        heightMap = ai;
        initToNull();
    }

    public static void nullLoader() {
        interactableObjects = null;
        sceneClusterCounts = null;
        sceneClusters = null;
        tileDeque = null;
        aBooleanArrayArrayArrayArray491 = null;
        aBooleanArrayArray492 = null;
    }

    public void initToNull() {
        for (int j = 0; j < numberOfZ; j++) {
            for (int k = 0; k < xRegionSize; k++) {
                for (int i1 = 0; i1 < yRegionSize; i1++)
                    tiles[j][k][i1] = null;

            }

        }
        for (int l = 0; l < cullingClusterPlaneCount; l++) {
            for (int j1 = 0; j1 < sceneClusterCounts[l]; j1++)
                sceneClusters[l][j1] = null;

            sceneClusterCounts[l] = 0;
        }

        for (int k1 = 0; k1 < interactableObjectCacheCurrPos; k1++)
            gameObjectsCache[k1] = null;

        interactableObjectCacheCurrPos = 0;
        for (int l1 = 0; l1 < interactableObjects.length; l1++)
            interactableObjects[l1] = null;

    }

    public void method275(int i) {
        zAnInt442 = i;
        for (int k = 0; k < xRegionSize; k++) {
            for (int l = 0; l < yRegionSize; l++)
                if (tiles[i][k][l] == null)
                    tiles[i][k][l] = new Tile(i, k, l);

        }

    }

    public void method276(int i, int j) {
        Tile class30_sub3 = tiles[0][j][i];
        for (int l = 0; l < 3; l++) {
            Tile class30_sub3_1 = tiles[l][j][i] = tiles[l + 1][j][i];
            if (class30_sub3_1 != null) {
                class30_sub3_1.plane--;
                for (int j1 = 0; j1 < class30_sub3_1.gameObjectIndex; j1++) {
                    GameObject class28 = class30_sub3_1.objects[j1];
                    if ((class28.uid >> 29 & 3) == 2 && class28.xLocLow == j
                            && class28.yLocHigh == i)
                        class28.zLoc--;
                }

            }
        }
        if (tiles[0][j][i] == null)
            tiles[0][j][i] = new Tile(0, j, i);
        tiles[0][j][i].bridge = class30_sub3;
        tiles[3][j][i] = null;
    }

    public static void createNewSceneCluster(int i, int j, int k, int l, int i1, int j1,
                                             int l1, int i2) {
        Class47 class47 = new Class47();
        class47.anInt787 = j / 128;
        class47.anInt788 = l / 128;
        class47.anInt789 = l1 / 128;
        class47.anInt790 = i1 / 128;
        class47.anInt791 = i2;
        class47.anInt792 = j;
        class47.anInt793 = l;
        class47.anInt794 = l1;
        class47.anInt795 = i1;
        class47.anInt796 = j1;
        class47.anInt797 = k;
        sceneClusters[i][sceneClusterCounts[i]++] = class47;
    }

    public void method278(int i, int j, int k, int l) {
        Tile class30_sub3 = tiles[i][j][k];
        if (class30_sub3 != null) {
            tiles[i][j][k].anInt1321 = l;
        }
    }

    public void addTile(int i, int j, int k, int l, int i1, int j1, int k1,
                        int l1, int i2, int j2, int k2, int l2, int i3, int j3, int k3,
                        int l3, int i4, int j4, int k4, int l4) {
        if (l == 0) {
            SceneTilePaint class43 = new SceneTilePaint(k2, l2, i3, j3, -1, k4);
            for (int i5 = i; i5 >= 0; i5--)
                if (tiles[i5][j][k] == null)
                    tiles[i5][j][k] = new Tile(i5, j, k);

            tiles[i][j][k].aClass43_1311 = class43;
            return;
        }
        if (l == 1) {
            SceneTilePaint class43_1 = new SceneTilePaint(k3, l3, i4, j4, j1, l4);
            for (int j5 = i; j5 >= 0; j5--)
                if (tiles[j5][j][k] == null)
                    tiles[j5][j][k] = new Tile(j5, j, k);

            tiles[i][j][k].aClass43_1311 = class43_1;
            return;
        }
        SceneTileModel class40 = new SceneTileModel(k, k3, j3, i2, j1, i4, i1, k2, k4, i3,
                j2, l1, k1, l, j4, l3, l2, j, l4);
        for (int k5 = i; k5 >= 0; k5--)
            if (tiles[k5][j][k] == null)
                tiles[k5][j][k] = new Tile(k5, j, k);

        tiles[i][j][k].aClass40_1312 = class40;
    }

    public void method280(int i, int j, int k, Renderable class30_sub2_sub4, long i1, int j1) {
        if (class30_sub2_sub4 == null)
            return;
        GroundObject class49 = new GroundObject();
        class49.renderable = class30_sub2_sub4;
        class49.anInt812 = j1 * 128 + 64;
        class49.anInt813 = k * 128 + 64;
        class49.anInt811 = j;
        class49.uid = i1;
       // class49.aByte816 = byte0;
        if (tiles[i][j1][k] == null)
            tiles[i][j1][k] = new Tile(i, j1, k);
        tiles[i][j1][k].groundObject = class49;
    }

    public void method281(int i, long j, Renderable class30_sub2_sub4, int k,
                          Renderable class30_sub2_sub4_1, Renderable class30_sub2_sub4_2, int l,
                          int i1) {
        Object4 object4 = new Object4();
        object4.aClass30_Sub2_Sub4_48 = class30_sub2_sub4_2;
        object4.anInt46 = i * 128 + 64;
        object4.anInt47 = i1 * 128 + 64;
        object4.anInt45 = k;
        object4.uid = j;
        object4.aClass30_Sub2_Sub4_49 = class30_sub2_sub4;
        object4.aClass30_Sub2_Sub4_50 = class30_sub2_sub4_1;
        int j1 = 0;
        Tile class30_sub3 = tiles[l][i][i1];
        if (class30_sub3 != null) {
            for (int k1 = 0; k1 < class30_sub3.gameObjectIndex; k1++)
                if ((class30_sub3.objects[k1].uid & 0x400000L) == 4194304L && class30_sub3.objects[k1].renderable instanceof Model) {
                    int l1 = ((Model) class30_sub3.objects[k1].renderable).itemDropHeight;
                    if (l1 > j1)
                        j1 = l1;
                }

        }
        object4.anInt52 = j1;
        if (tiles[l][i][i1] == null)
            tiles[l][i][i1] = new Tile(l, i, i1);
        tiles[l][i][i1].obj4 = object4;
    }

    public void method282(int i, Renderable class30_sub2_sub4, long j, int k, int l, Renderable class30_sub2_sub4_1, int i1, int j1,
                          int k1) {
        if (class30_sub2_sub4 == null && class30_sub2_sub4_1 == null)
            return;
        WallObject object1 = new WallObject();
        object1.uid = j;
        object1.anInt274 = l * 128 + 64;
        object1.anInt275 = k * 128 + 64;
        object1.anInt273 = i1;
        object1.renderable1 = class30_sub2_sub4;
        object1.renderable2 = class30_sub2_sub4_1;
        object1.orientation = i;
        object1.orientation1 = j1;
        for (int l1 = k1; l1 >= 0; l1--)
            if (tiles[l1][l][k] == null)
                tiles[l1][l][k] = new Tile(l1, l, k);

        tiles[k1][l][k].wallObject = object1;
    }

    public void method283(long i, int j, int k, int i1, int j1, int k1,
                          Renderable class30_sub2_sub4, int l1, int i2, int j2) {
        if (class30_sub2_sub4 == null)
            return;
        Object2 class26 = new Object2();
        class26.uid = i;
        class26.anInt500 = l1 * 128 + 64 + j1;
        class26.anInt501 = j * 128 + 64 + i2;
        class26.anInt499 = k1;
        class26.aClass30_Sub2_Sub4_504 = class30_sub2_sub4;
        class26.anInt502 = j2;
        class26.anInt503 = k;
        for (int k2 = i1; k2 >= 0; k2--)
            if (tiles[k2][l1][j] == null)
                tiles[k2][l1][j] = new Tile(k2, l1, j);

        tiles[i1][l1][j].obj2 = class26;
    }

    public boolean method284(long i, int j, int k,
                             Renderable class30_sub2_sub4, int l, int i1, int j1, int k1, int l1) {
        if (class30_sub2_sub4 == null) {
            return true;
        } else {
            int i2 = l1 * 128 + 64 * l;
            int j2 = k1 * 128 + 64 * k;
            return method287(i1, l1, k1, l, k, i2, j2, j, class30_sub2_sub4,
                    j1, false, i);
        }
    }

    public boolean method285(int i, int j, int k, long k3, int i1, int j1,
                             int k1, Renderable class30_sub2_sub4, boolean flag) {
        if (class30_sub2_sub4 == null)
            return true;
        int l1 = k1 - j1;
        int i2 = i1 - j1;
        int j2 = k1 + j1;
        int k2 = i1 + j1;
        if (flag) {
            if (j > 640 && j < 1408)
                k2 += 128;
            if (j > 1152 && j < 1920)
                j2 += 128;
            if (j > 1664 || j < 384)
                i2 -= 128;
            if (j > 128 && j < 896)
                l1 -= 128;
        }
        l1 /= 128;
        i2 /= 128;
        j2 /= 128;
        k2 /= 128;
        return method287(i, l1, i2, (j2 - l1) + 1, (k2 - i2) + 1, k1, i1, k,
                class30_sub2_sub4, j, true, k3);
    }

    public boolean method286(int j, int k, Renderable class30_sub2_sub4, int l,
                             int i1, int j1, int k1, int l1, int i2, long j2, int k2) {
        return class30_sub2_sub4 == null
                || method287(j, l1, k2, (i2 - l1) + 1, (i1 - k2) + 1, j1, k,
                k1, class30_sub2_sub4, l, true, j2);
    }

    private boolean method287(int i, int j, int k, int l, int i1, int j1,
                              int k1, int l1, Renderable class30_sub2_sub4, int i2, boolean flag,
                              long j2) {
        for (int k2 = j; k2 < j + l; k2++) {
            for (int l2 = k; l2 < k + i1; l2++) {
                if (k2 < 0 || l2 < 0 || k2 >= xRegionSize || l2 >= yRegionSize)
                    return false;
                Tile class30_sub3 = tiles[i][k2][l2];
                if (class30_sub3 != null && class30_sub3.gameObjectIndex >= 5)
                    return false;
            }

        }

        GameObject class28 = new GameObject();
        class28.uid = j2;
        class28.zLoc = i;
        class28.xPos = j1;
        class28.yPos = k1;
        class28.tileHeight = l1;
        class28.renderable = class30_sub2_sub4;
        class28.turnValue = i2;
        class28.xLocLow = j;
        class28.yLocHigh = k;
        class28.xLocHigh = (j + l) - 1;
        class28.yLocLow = (k + i1) - 1;
        for (int i3 = j; i3 < j + l; i3++) {
            for (int j3 = k; j3 < k + i1; j3++) {
                int k3 = 0;
                if (i3 > j)
                    k3++;
                if (i3 < (j + l) - 1)
                    k3 += 4;
                if (j3 > k)
                    k3 += 8;
                if (j3 < (k + i1) - 1)
                    k3 += 2;
                for (int l3 = i; l3 >= 0; l3--)
                    if (tiles[l3][i3][j3] == null)
                        tiles[l3][i3][j3] = new Tile(l3, i3, j3);

                Tile class30_sub3_1 = tiles[i][i3][j3];
                class30_sub3_1.objects[class30_sub3_1.gameObjectIndex] = class28;
                class30_sub3_1.gameObjectsChanged[class30_sub3_1.gameObjectIndex] = k3;
                class30_sub3_1.totalTiledObjectMask |= k3;
                class30_sub3_1.gameObjectIndex++;
            }

        }

        if (flag)
            gameObjectsCache[interactableObjectCacheCurrPos++] = class28;
        return true;
    }

    public void clearObj5Cache() {
        for (int i = 0; i < interactableObjectCacheCurrPos; i++) {
            GameObject object5 = gameObjectsCache[i];
            method289(object5);
            gameObjectsCache[i] = null;
        }

        interactableObjectCacheCurrPos = 0;
    }

    private void method289(GameObject class28) {
        for (int j = class28.xLocLow; j <= class28.xLocHigh; j++) {
            for (int k = class28.yLocHigh; k <= class28.yLocLow; k++) {
                Tile class30_sub3 = tiles[class28.zLoc][j][k];
                if (class30_sub3 != null) {
                    for (int l = 0; l < class30_sub3.gameObjectIndex; l++) {
                        if (class30_sub3.objects[l] != class28)
                            continue;
                        class30_sub3.gameObjectIndex--;
                        for (int i1 = l; i1 < class30_sub3.gameObjectIndex; i1++) {
                            class30_sub3.objects[i1] = class30_sub3.objects[i1 + 1];
                            class30_sub3.gameObjectsChanged[i1] = class30_sub3.gameObjectsChanged[i1 + 1];
                        }

                        class30_sub3.objects[class30_sub3.gameObjectIndex] = null;
                        break;
                    }

                    class30_sub3.totalTiledObjectMask = 0;
                    for (int j1 = 0; j1 < class30_sub3.gameObjectIndex; j1++)
                        class30_sub3.totalTiledObjectMask |= class30_sub3.gameObjectsChanged[j1];

                }
            }

        }

    }

    public void method290(int i, int k, int l, int i1) {
        Tile class30_sub3 = tiles[i1][l][i];
        if (class30_sub3 == null)
            return;
        Object2 class26 = class30_sub3.obj2;
        if (class26 != null) {
            int j1 = l * 128 + 64;
            int k1 = i * 128 + 64;
            class26.anInt500 = j1 + ((class26.anInt500 - j1) * k) / 16;
            class26.anInt501 = k1 + ((class26.anInt501 - k1) * k) / 16;
        }
    }

    public void method291(int i, int j, int k, byte byte0) {
        Tile class30_sub3 = tiles[j][i][k];
        if (byte0 != -119)
            aBoolean434 = !aBoolean434;
        if (class30_sub3 != null) {
            class30_sub3.wallObject = null;
        }
    }

    public void method292(int j, int k, int l) {
        Tile class30_sub3 = tiles[k][l][j];
        if (class30_sub3 != null) {
            class30_sub3.obj2 = null;
        }
    }

    public void method293(int i, int k, int l) {
        Tile class30_sub3 = tiles[i][k][l];
        if (class30_sub3 == null)
            return;
        for (int j1 = 0; j1 < class30_sub3.gameObjectIndex; j1++) {
            GameObject class28 = class30_sub3.objects[j1];
            if ((class28.uid >> 29 & 3) == 2 && class28.xLocLow == k
                    && class28.yLocHigh == l) {
                method289(class28);
                return;
            }
        }

    }

    public void method294(int i, int j, int k) {
        Tile class30_sub3 = tiles[i][k][j];
        if (class30_sub3 == null)
            return;
        class30_sub3.groundObject = null;
    }

    public void method295(int i, int j, int k) {
        Tile class30_sub3 = tiles[i][j][k];
        if (class30_sub3 != null) {
            class30_sub3.obj4 = null;
        }
    }

    public WallObject method296(int i, int j, int k) {
        Tile class30_sub3 = tiles[i][j][k];
        if (class30_sub3 == null)
            return null;
        else
            return class30_sub3.wallObject;
    }

    public Object2 method297(int i, int k, int l) {
        Tile class30_sub3 = tiles[l][i][k];
        if (class30_sub3 == null)
            return null;
        else
            return class30_sub3.obj2;
    }

    public GameObject method298(int i, int j, int k) {
        Tile class30_sub3 = tiles[k][i][j];
        if (class30_sub3 == null)
            return null;
        for (int l = 0; l < class30_sub3.gameObjectIndex; l++) {
            GameObject class28 = class30_sub3.objects[l];
            if ((class28.uid >> 29 & 3) == 2 && class28.xLocLow == i
                    && class28.yLocHigh == j)
                return class28;
        }
        return null;
    }

    public GroundObject method299(int i, int j, int k) {
        Tile class30_sub3 = tiles[k][j][i];
        if (class30_sub3 == null || class30_sub3.groundObject == null)
            return null;
        else
            return class30_sub3.groundObject;
    }

	//fetchWallObjectUID
	public long method300(int i, int j, int k) {
		Tile tile = tiles[i][j][k];
		if(tile == null || tile.wallObject == null)
			return 0L;
		else
			return tile.wallObject.uid;
	}

	//fetchWallDecorationUID
	public long method301(int i, int j, int l) {
		Tile tile = tiles[i][j][l];
		if(tile == null || tile.obj2 == null)
			return 0L;
		else
			return tile.obj2.uid;
	}


	//fetchObjectMeshUID
	public long method302(int z, int x, int y) {
		Tile tile = tiles[z][x][y];
		if(tile == null)
			return 0L;
		for(int l = 0; l < tile.gameObjectIndex; l++) {
			GameObject interactableObject = tile.objects[l];
			if((interactableObject.uid >> 29 & 0x3L) == 2L &&
					interactableObject.xLocLow == x && interactableObject.yLocHigh == y) {
				return interactableObject.uid;
			}
		}
		return 0L;
	}

	//fetchGroundDecorationUID
	public long method303(int i, int j, int k) {
		Tile tile = tiles[i][j][k];
		if(tile == null || tile.groundObject == null)
			return 0L;
		else
			return tile.groundObject.uid;
	}


	//fetchObjectIDTAGForXYZ
	public int method304old(int z, int x, int y, long objectMesh) {//fecthObjectIDXYZ
		Tile tile = tiles[z][x][y];
		if(tile == null)
			return -1;
		if(tile.wallObject != null && tile.wallObject.uid == objectMesh)//obj1 = wall
			return tile.wallObject.aByte281 & 255;
		if(tile.obj2 != null && tile.obj2.uid == objectMesh)//obj2 = wallDec
			return tile.obj2.aByte506 & 255;
		if(tile.groundObject != null && tile.groundObject.uid == objectMesh)//obj3 = groundDec
			return tile.groundObject.aByte816 & 255;
			for(int i1 = 0; i1 < tile.gameObjectIndex; i1++)//anInt1317 = count?
				if(tile.objects[i1].uid == objectMesh)
					return tile.objects[i1].mask & 255;

			return -1;
	}
    public int method3042(int i, int j, int k, long l) {
        Tile class30_sub3 = tiles[i][j][k];
        if (class30_sub3 == null)
            return -1;
        if (class30_sub3.wallObject != null && class30_sub3.wallObject.uid == l)
            return class30_sub3.wallObject.aByte281 & 0xff;
        if (class30_sub3.obj2 != null && class30_sub3.obj2.uid == l)
            return class30_sub3.obj2.aByte506 & 0xff;
        if (class30_sub3.groundObject != null && class30_sub3.groundObject.uid == l)
            return class30_sub3.groundObject.aByte816 & 0xff;
        for (int i1 = 0; i1 < class30_sub3.gameObjectIndex; i1++)
            if (class30_sub3.objects[i1].uid == l)
                return class30_sub3.objects[i1].mask & 0xff;

        return -1;
    }
    public boolean method304(int i, int j, int k, long l) {//method1952 // method298
        Tile class30_sub3 = tiles[i][j][k];
        if (class30_sub3 == null) {
            return false;
        }
        if (class30_sub3.wallObject != null && class30_sub3.wallObject.uid == l) {
            return true;
        }
        if (class30_sub3.obj2 != null && class30_sub3.obj2.uid == l) {
            return true;
        }
        if (class30_sub3.groundObject != null && class30_sub3.groundObject.uid == l) {
            return true;
        }
        for (int i1 = 0; i1 < class30_sub3.gameObjectIndex; i1++) {
            if (class30_sub3.objects[i1].uid == l) {
                return true;
            }
        }
        return false;
    }
    public void shadeModels(int lightY, int lightX, int lightZ) {
        int intensity = 64;// was parameter
        int diffusion = 768;// was parameter
        int lightDistance = (int) Math.sqrt(lightX * lightX + lightY * lightY + lightZ * lightZ);
        int someLightQualityVariable = diffusion * lightDistance >> 8;
        for (int zLoc = 0; zLoc < numberOfZ; zLoc++) {
            for (int xLoc = 0; xLoc < xRegionSize; xLoc++) {
                for (int yLoc = 0; yLoc < yRegionSize; yLoc++) {
                    Tile tile = tiles[zLoc][xLoc][yLoc];
                    if (tile != null) {
                        WallObject wallObject = tile.wallObject;
                        if (wallObject != null && wallObject.renderable1 != null
                                && wallObject.renderable1.vertexNormals != null) {
                            get_pos(zLoc, 1, 1, xLoc, yLoc, (Model) wallObject.renderable1);
                            if (wallObject.renderable2 != null && wallObject.renderable2.vertexNormals != null) {
                                get_pos(zLoc, 1, 1, xLoc, yLoc, (Model) wallObject.renderable2);
                                mergeNormals((Model) wallObject.renderable1, (Model) wallObject.renderable2, 0, 0,
                                        0, false);
                                ((Model) wallObject.renderable2).setLighting(intensity, someLightQualityVariable,
                                        lightX, lightY, lightZ);
                            }
                            ((Model) wallObject.renderable1).setLighting(intensity, someLightQualityVariable,
                                    lightX, lightY, lightZ);
                        }
                        for (int k2 = 0; k2 < tile.gameObjectIndex; k2++) {
                            GameObject interactableObject = tile.objects[k2];
                            if (interactableObject != null && interactableObject.renderable != null
                                    && interactableObject.renderable.vertexNormals != null) {
                                get_pos(zLoc, (interactableObject.xLocHigh - interactableObject.xLocLow) + 1,
                                        (interactableObject.yLocLow - interactableObject.yLocHigh) + 1, xLoc, yLoc,
                                        (Model) interactableObject.renderable);
                                ((Model) interactableObject.renderable).setLighting(intensity,
                                        someLightQualityVariable, lightX, lightY, lightZ);
                            }
                        }

                        GroundObject groundDecoration = tile.groundObject;
                        if (groundDecoration != null && groundDecoration.renderable.vertexNormals != null) {
                            get_pos(xLoc, zLoc, (Model) groundDecoration.renderable, yLoc);
                            ((Model) groundDecoration.renderable).setLighting(intensity, someLightQualityVariable,
                                    lightX, lightY, lightZ);
                        }

                    }
                }
            }
        }
    }


    public void get_pos(int modelXLoc, int modelZLoc, Model model,
                        int modelYLoc) { //TODO figure it out
        if (modelXLoc < xRegionSize) {
            Tile tile = tiles[modelZLoc][modelXLoc + 1][modelYLoc];
            if (tile != null && tile.groundObject != null
                    && tile.groundObject.renderable.vertexNormals != null) {
                mergeNormals(model, (Model) tile.groundObject.renderable, 128, 0, 0, true);
            }
        }
        if (modelYLoc < xRegionSize) {
            Tile tile = tiles[modelZLoc][modelXLoc][modelYLoc + 1];
            if (tile != null && tile.groundObject != null
                    && tile.groundObject.renderable.vertexNormals != null) {
                mergeNormals(model, (Model) tile.groundObject.renderable, 0, 0, 128, true);
            }
        }
        if (modelXLoc < xRegionSize && modelYLoc < yRegionSize) {
            Tile tile = tiles[modelZLoc][modelXLoc + 1][modelYLoc + 1];
            if (tile != null && tile.groundObject != null
                    && tile.groundObject.renderable.vertexNormals != null) {
                mergeNormals(model, (Model) tile.groundObject.renderable, 128, 0, 128, true);
            }
        }
        if (modelXLoc < xRegionSize && modelYLoc > 0) {
            Tile tile = tiles[modelZLoc][modelXLoc + 1][modelYLoc - 1];
            if (tile != null && tile.groundObject != null
                    && tile.groundObject.renderable.vertexNormals != null) {
                mergeNormals(model, (Model) tile.groundObject.renderable, 128, 0, -128, true);
            }
        }
    }


    public void get_pos(int modelZLoc, int modelXSize, int modelYSize, int modelXLoc,
                        int modelYLoc, Model model) {
        boolean flag = true;
        int startX = modelXLoc;
        int stopX = modelXLoc + modelXSize;
        int startY = modelYLoc - 1;
        int stopY = modelYLoc + modelYSize;
        for (int zLoc = modelZLoc; zLoc <= modelZLoc + 1; zLoc++) {
            if (zLoc != numberOfZ) {//TODO Always?
                for (int xLoc = startX; xLoc <= stopX; xLoc++) {
                    if (xLoc >= 0 && xLoc < xRegionSize) {
                        for (int yLoc = startY; yLoc <= stopY; yLoc++) {
                            if (yLoc >= 0 && yLoc < yRegionSize && (!flag || xLoc >= stopX || yLoc >= stopY
                                    || yLoc < modelYLoc && xLoc != modelXLoc)) {
                                Tile tile = tiles[zLoc][xLoc][yLoc];
                                if (tile != null) {
                                    int relativeHeightToModelTile = (heightMap[zLoc][xLoc][yLoc]
                                            + heightMap[zLoc][xLoc + 1][yLoc] + heightMap[zLoc][xLoc][yLoc + 1]
                                            + heightMap[zLoc][xLoc + 1][yLoc + 1]) / 4
                                            - (heightMap[modelZLoc][modelXLoc][modelYLoc]
                                            + heightMap[modelZLoc][modelXLoc + 1][modelYLoc]
                                            + heightMap[modelZLoc][modelXLoc][modelYLoc + 1]
                                            + heightMap[modelZLoc][modelXLoc + 1][modelYLoc + 1]) / 4;
                                    WallObject wallObject = tile.wallObject;
                                    if (wallObject != null && wallObject.renderable1 != null
                                            && wallObject.renderable1.vertexNormals != null) {
                                        mergeNormals(model, (Model) wallObject.renderable1,
                                                (xLoc - modelXLoc) * 128 + (1 - modelXSize) * 64, relativeHeightToModelTile,
                                                (yLoc - modelYLoc) * 128 + (1 - modelYSize) * 64, flag);
                                    }
                                    if (wallObject != null && wallObject.renderable2 != null
                                            && wallObject.renderable2.vertexNormals != null) {
                                        mergeNormals(model, (Model) wallObject.renderable2,
                                                (xLoc - modelXLoc) * 128 + (1 - modelXSize) * 64, relativeHeightToModelTile,
                                                (yLoc - modelYLoc) * 128 + (1 - modelYSize) * 64, flag);
                                    }
                                    for (int i = 0; i < tile.gameObjectIndex; i++) {
                                        GameObject gameObject = tile.objects[i];
                                        if (gameObject != null && gameObject.renderable != null
                                                && gameObject.renderable.vertexNormals != null) {
                                            int tiledObjectXSize = (gameObject.xLocHigh - gameObject.xLocLow) + 1;
                                            int tiledObjectYSize = (gameObject.yLocLow - gameObject.yLocHigh) + 1;
                                            mergeNormals(model, (Model) gameObject.renderable,
                                                    (gameObject.xLocLow - modelXLoc) * 128
                                                            + (tiledObjectXSize - modelXSize) * 64,
                                                    relativeHeightToModelTile, (gameObject.yLocHigh - modelYLoc) * 128
                                                            + (tiledObjectYSize - modelYSize) * 64,
                                                    flag);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                startX--;
                flag = false;
            }
        }

    }

    private void mergeNormals(Model first, Model second, int dx, int dy, int dz, boolean flag) {
        anInt488++;
        int count = 0;
        int[] secondX = second.verticesX;
        int secondVertices = second.verticesCount;
        for (int vA = 0; vA < first.verticesCount; vA++) {

            if (first == null || first.vertexNormalsOffsets[vA] == null) {
                return;
            }
            VertexNormal parentNormalA = first.vertexNormals[vA];
            VertexNormal normalA = first.vertexNormalsOffsets[vA];

            if (normalA.magnitude != 0) {
                int y = first.verticesY[vA] - dy;
                if (y <= second.maximumYVertex) {

                    int x = first.verticesX[vA] - dx;
                    if (x >= second.minimumXVertex && x <= second.maximumXVertex) {

                        int z = first.verticesZ[vA] - dz;
                        if (z >= second.minimumZVertex && z <= second.maximumZVertex) {
                            for (int vB = 0; vB < secondVertices; vB++) {
                                VertexNormal parentNormalB = second.vertexNormals[vB];
                                VertexNormal normalB = second.vertexNormalsOffsets[vB];

                                if (x == secondX[vB] && z == second.verticesZ[vB] && y == second.verticesY[vB] && normalB.magnitude != 0) {
                                    parentNormalA.x += normalB.x;
                                    parentNormalA.y += normalB.y;
                                    parentNormalA.z += normalB.z;
                                    parentNormalA.magnitude += normalB.magnitude;
                                    parentNormalB.x += normalA.x;
                                    parentNormalB.y += normalA.y;
                                    parentNormalB.z += normalA.z;
                                    parentNormalB.magnitude += normalA.magnitude;
                                    count++;
                                    anIntArray486[vA] = anInt488;
                                    anIntArray487[vB] = anInt488;
                                }
                            }
                        }
                    }
                }
            }
        }

        if (count < 3 || !flag) {
            return;
        }

        for (int k1 = 0; k1 < first.trianglesCount; k1++) {
            if (anIntArray486[first.trianglesX[k1]] == anInt488 && anIntArray486[first.trianglesY[k1]] == anInt488
                    && anIntArray486[first.trianglesZ[k1]] == anInt488) {
                first.types[k1] = -1;
            }
        }

        for (int l1 = 0; l1 < second.trianglesCount; l1++) {
            if (anIntArray487[second.trianglesX[l1]] == anInt488 && anIntArray487[second.trianglesY[l1]] == anInt488
                    && anIntArray487[second.trianglesZ[l1]] == anInt488) {
                second.types[l1] = -1;
            }
        }
    }

    public void method309(int ai[], int i, int k, int l, int i1) {
        int j = 512;// was parameter
        Tile class30_sub3 = tiles[k][l][i1];
        if (class30_sub3 == null)
            return;
        SceneTilePaint class43 = class30_sub3.aClass43_1311;
        if (class43 != null) {
            int j1 = class43.colorRGB;
            if (j1 == 0)
                return;
            for (int k1 = 0; k1 < 4; k1++) {
                ai[i] = j1;
                ai[i + 1] = j1;
                ai[i + 2] = j1;
                ai[i + 3] = j1;
                i += j;
            }

            return;
        }
        SceneTileModel class40 = class30_sub3.aClass40_1312;
        if (class40 == null)
            return;
        int l1 = class40.shape;
        int i2 = class40.rotation;
        int j2 = class40.colourRGB;
        int k2 = class40.colourRGBA;
        int ai1[] = tileVertices[l1];
        int ai2[] = tileVertexIndices[i2];
        int l2 = 0;
        if (j2 != 0) {
            for (int i3 = 0; i3 < 4; i3++) {
                ai[i] = ai1[ai2[l2++]] != 0 ? k2 : j2;
                ai[i + 1] = ai1[ai2[l2++]] != 0 ? k2 : j2;
                ai[i + 2] = ai1[ai2[l2++]] != 0 ? k2 : j2;
                ai[i + 3] = ai1[ai2[l2++]] != 0 ? k2 : j2;
                i += j;
            }

            return;
        }
        for (int j3 = 0; j3 < 4; j3++) {
            if (ai1[ai2[l2++]] != 0)
                ai[i] = k2;
            if (ai1[ai2[l2++]] != 0)
                ai[i + 1] = k2;
            if (ai1[ai2[l2++]] != 0)
                ai[i + 2] = k2;
            if (ai1[ai2[l2++]] != 0)
                ai[i + 3] = k2;
            i += j;
        }

    }

    public static void method310(int i, int j, int k, int l, int ai[]) {
        anInt495 = 0;
        anInt496 = 0;
        viewportWidth = k;
        viewportHeight = l;
        centerX = k / 2;
        centerY = l / 2;
        boolean aflag[][][][] = new boolean[9][32][(25 * 2) + 3][(25 * 2) + 3];
        for (int i1 = 128; i1 <= 384; i1 += 32) {
            for (int j1 = 0; j1 < 2048; j1 += 64) {
                camUpDownY = Model.SINE[i1];
                camUpDownX = Model.COSINE[i1];
                camLeftRightY = Model.SINE[j1];
                camLeftRightX = Model.COSINE[j1];
                int l1 = (i1 - 128) / 32;
                int j2 = j1 / 64;
                for (int l2 = -26; l2 <= 26; l2++) {
                    for (int j3 = -26; j3 <= 26; j3++) {
                        int k3 = l2 * 128;
                        int i4 = j3 * 128;
                        boolean flag2 = false;
                        for (int k4 = -i; k4 <= j; k4 += 128) {
                            if (!method311(ai[l1] + k4, i4, k3))
                                continue;
                            flag2 = true;
                            break;
                        }

                        aflag[l1][j2][l2 + 25 + 1][j3 + 25 + 1] = flag2;
                    }

                }

            }

        }

        for (int k1 = 0; k1 < 8; k1++) {
            for (int i2 = 0; i2 < 32; i2++) {
                for (int k2 = -25; k2 < 25; k2++) {
                    for (int i3 = -25; i3 < 25; i3++) {
                        boolean flag1 = false;
                        label0:
                        for (int l3 = -1; l3 <= 1; l3++) {
                            for (int j4 = -1; j4 <= 1; j4++) {
                                if (aflag[k1][i2][k2 + l3 + 25 + 1][i3 + j4
                                        + 25 + 1])
                                    flag1 = true;
                                else if (aflag[k1][(i2 + 1) % 31][k2 + l3 + 25
                                        + 1][i3 + j4 + 25 + 1])
                                    flag1 = true;
                                else if (aflag[k1 + 1][i2][k2 + l3 + 25 + 1][i3
                                        + j4 + 25 + 1]) {
                                    flag1 = true;
                                } else {
                                    if (!aflag[k1 + 1][(i2 + 1) % 31][k2 + l3
                                            + 25 + 1][i3 + j4 + 25 + 1])
                                        continue;
                                    flag1 = true;
                                }
                                break label0;
                            }

                        }

                        aBooleanArrayArrayArrayArray491[k1][i2][k2 + 25][i3 + 25] = flag1;
                    }

                }

            }

        }

    }

    private static boolean method311(int i, int j, int k) {
        int l = j * camLeftRightY + k * camLeftRightX >> 16;
        int i1 = j * camLeftRightX - k * camLeftRightY >> 16;
        int j1 = i * camUpDownY + i1 * camUpDownX >> 16;
        int k1 = i * camUpDownX - i1 * camUpDownY >> 16;
        if (j1 < 50 || j1 > 6000)
            return false;
        int l1 = centerX + (l * WorldController.focalLength) / j1;
        int i2 = centerY + (k1 * WorldController.focalLength) / j1;
        return l1 >= anInt495 && l1 <= viewportWidth && i2 >= anInt496
                && i2 <= viewportHeight;
    }

    public void method312(int i, int j) {
        clicked = true;
        clickScreenX = j;
        clickScreenY = i;
        clickedTileX = -1;
        clickedTileY = -1;
    }

    public void method313(int i, int j, int k, int l, int i1, int j1) {
        if (i < 0)
            i = 0;
        else if (i >= xRegionSize * 128)
            i = xRegionSize * 128 - 1;
        if (j < 0)
            j = 0;
        else if (j >= yRegionSize * 128)
            j = yRegionSize * 128 - 1;
        anInt448++;
        camUpDownY = Model.SINE[j1];
        camUpDownX = Model.COSINE[j1];
        camLeftRightY = Model.SINE[k];
        camLeftRightX = Model.COSINE[k];
        aBooleanArrayArray492 = aBooleanArrayArrayArrayArray491[(j1 - 128) / 32][k / 64];
        cameraX2 = i;
        cameraY2 = l;
        cameraZ2 = j;
        screenCenterX = i / 128;
        yCameraTile = j / 128;
        currentRenderPlane = i1;
        cameraLowTileX = screenCenterX - 25;
        if (cameraLowTileX < 0)
            cameraLowTileX = 0;
        cameraLowTileY = yCameraTile - 25;
        if (cameraLowTileY < 0)
            cameraLowTileY = 0;
        cameraHighTileX = screenCenterX + 25;
        if (cameraHighTileX > xRegionSize)
            cameraHighTileX = xRegionSize;
        cameraHighTileY = yCameraTile + 25;
        if (cameraHighTileY > yRegionSize)
            cameraHighTileY = yRegionSize;
        method319();
        anInt446 = 0;
        for (int k1 = zAnInt442; k1 < numberOfZ; k1++) {
            Tile aclass30_sub3[][] = tiles[k1];
            for (int i2 = cameraLowTileX; i2 < cameraHighTileX; i2++) {
                for (int k2 = cameraLowTileY; k2 < cameraHighTileY; k2++) {
                    Tile class30_sub3 = aclass30_sub3[i2][k2];
                    if (class30_sub3 != null)
                        if (class30_sub3.anInt1321 > i1
                                || !aBooleanArrayArray492[(i2 - screenCenterX) + 25][(k2 - yCameraTile) + 25]
                                && heightMap[k1][i2][k2] - l < 2000) {
                            class30_sub3.aBoolean1322 = false;
                            class30_sub3.aBoolean1323 = false;
                            class30_sub3.anInt1325 = 0;
                        } else {
                            class30_sub3.aBoolean1322 = true;
                            class30_sub3.aBoolean1323 = true;
                            class30_sub3.aBoolean1324 = class30_sub3.gameObjectIndex > 0;
                            anInt446++;
                        }
                }

            }

        }

        for (int l1 = zAnInt442; l1 < numberOfZ; l1++) {
            Tile aclass30_sub3_1[][] = tiles[l1];
            for (int l2 = -25; l2 <= 0; l2++) {
                int i3 = screenCenterX + l2;
                int k3 = screenCenterX - l2;
                if (i3 >= cameraLowTileX || k3 < cameraHighTileX) {
                    for (int i4 = -25; i4 <= 0; i4++) {
                        int k4 = yCameraTile + i4;
                        int i5 = yCameraTile - i4;
                        if (i3 >= cameraLowTileX) {
                            if (k4 >= cameraLowTileY) {
                                Tile class30_sub3_1 = aclass30_sub3_1[i3][k4];
                                if (class30_sub3_1 != null
                                        && class30_sub3_1.aBoolean1322)
                                    method314(class30_sub3_1, true);
                            }
                            if (i5 < cameraHighTileY) {
                                Tile class30_sub3_2 = aclass30_sub3_1[i3][i5];
                                if (class30_sub3_2 != null
                                        && class30_sub3_2.aBoolean1322)
                                    method314(class30_sub3_2, true);
                            }
                        }
                        if (k3 < cameraHighTileX) {
                            if (k4 >= cameraLowTileY) {
                                Tile class30_sub3_3 = aclass30_sub3_1[k3][k4];
                                if (class30_sub3_3 != null
                                        && class30_sub3_3.aBoolean1322)
                                    method314(class30_sub3_3, true);
                            }
                            if (i5 < cameraHighTileY) {
                                Tile class30_sub3_4 = aclass30_sub3_1[k3][i5];
                                if (class30_sub3_4 != null
                                        && class30_sub3_4.aBoolean1322)
                                    method314(class30_sub3_4, true);
                            }
                        }
                        if (anInt446 == 0) {
                            clicked = false;
                            return;
                        }
                    }

                }
            }

        }

        for (int j2 = zAnInt442; j2 < numberOfZ; j2++) {
            Tile aclass30_sub3_2[][] = tiles[j2];
            for (int j3 = -25; j3 <= 0; j3++) {
                int l3 = screenCenterX + j3;
                int j4 = screenCenterX - j3;
                if (l3 >= cameraLowTileX || j4 < cameraHighTileX) {
                    for (int l4 = -25; l4 <= 0; l4++) {
                        int j5 = yCameraTile + l4;
                        int k5 = yCameraTile - l4;
                        if (l3 >= cameraLowTileX) {
                            if (j5 >= cameraLowTileY) {
                                Tile class30_sub3_5 = aclass30_sub3_2[l3][j5];
                                if (class30_sub3_5 != null
                                        && class30_sub3_5.aBoolean1322)
                                    method314(class30_sub3_5, false);
                            }
                            if (k5 < cameraHighTileY) {
                                Tile class30_sub3_6 = aclass30_sub3_2[l3][k5];
                                if (class30_sub3_6 != null
                                        && class30_sub3_6.aBoolean1322)
                                    method314(class30_sub3_6, false);
                            }
                        }
                        if (j4 < cameraHighTileX) {
                            if (j5 >= cameraLowTileY) {
                                Tile class30_sub3_7 = aclass30_sub3_2[j4][j5];
                                if (class30_sub3_7 != null
                                        && class30_sub3_7.aBoolean1322)
                                    method314(class30_sub3_7, false);
                            }
                            if (k5 < cameraHighTileY) {
                                Tile class30_sub3_8 = aclass30_sub3_2[j4][k5];
                                if (class30_sub3_8 != null
                                        && class30_sub3_8.aBoolean1322)
                                    method314(class30_sub3_8, false);
                            }
                        }
                        if (anInt446 == 0) {
                            clicked = false;
                            return;
                        }
                    }

                }
            }

        }
        clicked = false;
    }

    private void method314(Tile class30_sub3, boolean flag) {
        tileDeque.insertHead(class30_sub3);
        do {
            Tile class30_sub3_1;
            do {
                class30_sub3_1 = (Tile) tileDeque.popHead();
                if (class30_sub3_1 == null)
                    return;
            } while (!class30_sub3_1.aBoolean1323);
            int i = class30_sub3_1.anInt1308;
            int j = class30_sub3_1.anInt1309;
            int k = class30_sub3_1.plane;
            int l = class30_sub3_1.anInt1310;
            Tile aclass30_sub3[][] = tiles[k];
            if (class30_sub3_1.aBoolean1322) {
                if (flag) {
                    if (k > 0) {
                        Tile class30_sub3_2 = tiles[k - 1][i][j];
                        if (class30_sub3_2 != null
                                && class30_sub3_2.aBoolean1323)
                            continue;
                    }
                    if (i <= screenCenterX && i > cameraLowTileX) {
                        Tile class30_sub3_3 = aclass30_sub3[i - 1][j];
                        if (class30_sub3_3 != null
                                && class30_sub3_3.aBoolean1323
                                && (class30_sub3_3.aBoolean1322 || (class30_sub3_1.totalTiledObjectMask & 1) == 0))
                            continue;
                    }
                    if (i >= screenCenterX && i < cameraHighTileX - 1) {
                        Tile class30_sub3_4 = aclass30_sub3[i + 1][j];
                        if (class30_sub3_4 != null
                                && class30_sub3_4.aBoolean1323
                                && (class30_sub3_4.aBoolean1322 || (class30_sub3_1.totalTiledObjectMask & 4) == 0))
                            continue;
                    }
                    if (j <= yCameraTile && j > cameraLowTileY) {
                        Tile class30_sub3_5 = aclass30_sub3[i][j - 1];
                        if (class30_sub3_5 != null
                                && class30_sub3_5.aBoolean1323
                                && (class30_sub3_5.aBoolean1322 || (class30_sub3_1.totalTiledObjectMask & 8) == 0))
                            continue;
                    }
                    if (j >= yCameraTile && j < cameraHighTileY - 1) {
                        Tile class30_sub3_6 = aclass30_sub3[i][j + 1];
                        if (class30_sub3_6 != null
                                && class30_sub3_6.aBoolean1323
                                && (class30_sub3_6.aBoolean1322 || (class30_sub3_1.totalTiledObjectMask & 2) == 0))
                            continue;
                    }
                } else {
                    flag = true;
                }
                class30_sub3_1.aBoolean1322 = false;
                if (class30_sub3_1.bridge != null) {
                    Tile class30_sub3_7 = class30_sub3_1.bridge;
                    if (class30_sub3_7.aClass43_1311 != null) {
                        if (!method320(0, i, j))
                            method315(class30_sub3_7.aClass43_1311, 0,
                                    camUpDownY, camUpDownX, camLeftRightY, camLeftRightX, i,
                                    j);
                    } else if (class30_sub3_7.aClass40_1312 != null
                            && !method320(0, i, j))
                        method316(i, camUpDownY, camLeftRightY,
                                class30_sub3_7.aClass40_1312, camUpDownX, j,
                                camLeftRightX);
                    WallObject class10 = class30_sub3_7.wallObject;
                    if (class10 != null)
                        class10.renderable1.renderAtPoint(0, camUpDownY,
                                camUpDownX, camLeftRightY, camLeftRightX, class10.anInt274
                                        - cameraX2,
                                class10.anInt273 - cameraY2, class10.anInt275
                                        - cameraZ2, class10.uid);
                    for (int i2 = 0; i2 < class30_sub3_7.gameObjectIndex; i2++) {
                        GameObject class28 = class30_sub3_7.objects[i2];
                        if (class28 != null)
                            class28.renderable.renderAtPoint(
                                    class28.turnValue, camUpDownY, camUpDownX,
                                    camLeftRightY, camLeftRightX, class28.xPos
                                            - cameraX2, class28.tileHeight
                                            - cameraY2, class28.yPos
                                            - cameraZ2, class28.uid);
                    }

                }
                boolean flag1 = false;
                if (class30_sub3_1.aClass43_1311 != null) {
                    if (!method320(l, i, j)) {
                        flag1 = true;
                        method315(class30_sub3_1.aClass43_1311, l, camUpDownY,
                                camUpDownX, camLeftRightY, camLeftRightX, i, j);
                    }
                } else if (class30_sub3_1.aClass40_1312 != null
                        && !method320(l, i, j)) {
                    flag1 = true;
                    method316(i, camUpDownY, camLeftRightY,
                            class30_sub3_1.aClass40_1312, camUpDownX, j, camLeftRightX);
                }
                int j1 = 0;
                int j2 = 0;
                WallObject class10_3 = class30_sub3_1.wallObject;
                Object2 class26_1 = class30_sub3_1.obj2;
                if (class10_3 != null || class26_1 != null) {
                    if (screenCenterX == i)
                        j1++;
                    else if (screenCenterX < i)
                        j1 += 2;
                    if (yCameraTile == j)
                        j1 += 3;
                    else if (yCameraTile > j)
                        j1 += 6;
                    j2 = anIntArray478[j1];
                    class30_sub3_1.anInt1328 = anIntArray480[j1];
                }
                if (class10_3 != null) {
                    if ((class10_3.orientation & anIntArray479[j1]) != 0) {
                        if (class10_3.orientation == 16) {
                            class30_sub3_1.anInt1325 = 3;
                            class30_sub3_1.anInt1326 = anIntArray481[j1];
                            class30_sub3_1.anInt1327 = 3 - class30_sub3_1.anInt1326;
                        } else if (class10_3.orientation == 32) {
                            class30_sub3_1.anInt1325 = 6;
                            class30_sub3_1.anInt1326 = anIntArray482[j1];
                            class30_sub3_1.anInt1327 = 6 - class30_sub3_1.anInt1326;
                        } else if (class10_3.orientation == 64) {
                            class30_sub3_1.anInt1325 = 12;
                            class30_sub3_1.anInt1326 = anIntArray483[j1];
                            class30_sub3_1.anInt1327 = 12 - class30_sub3_1.anInt1326;
                        } else {
                            class30_sub3_1.anInt1325 = 9;
                            class30_sub3_1.anInt1326 = anIntArray484[j1];
                            class30_sub3_1.anInt1327 = 9 - class30_sub3_1.anInt1326;
                        }
                    } else {
                        class30_sub3_1.anInt1325 = 0;
                    }
                    if ((class10_3.orientation & j2) != 0
                            && !method321(l, i, j, class10_3.orientation))
                        class10_3.renderable1.renderAtPoint(0, camUpDownY,
                                camUpDownX, camLeftRightY, camLeftRightX,
                                class10_3.anInt274 - cameraX2,
                                class10_3.anInt273 - cameraY2,
                                class10_3.anInt275 - cameraZ2, class10_3.uid);
                    if ((class10_3.orientation1 & j2) != 0
                            && !method321(l, i, j, class10_3.orientation1))
                        class10_3.renderable2.renderAtPoint(0, camUpDownY,
                                camUpDownX, camLeftRightY, camLeftRightX,
                                class10_3.anInt274 - cameraX2,
                                class10_3.anInt273 - cameraY2,
                                class10_3.anInt275 - cameraZ2, class10_3.uid);
                }
                if (class26_1 != null
                        && !method322(l, i, j,
                        class26_1.aClass30_Sub2_Sub4_504.modelBaseY))
                    if ((class26_1.anInt502 & j2) != 0)
                        class26_1.aClass30_Sub2_Sub4_504.renderAtPoint(
                                class26_1.anInt503, camUpDownY, camUpDownX,
                                camLeftRightY, camLeftRightX, class26_1.anInt500
                                        - cameraX2, class26_1.anInt499
                                        - cameraY2, class26_1.anInt501
                                        - cameraZ2, class26_1.uid);
                    else if ((class26_1.anInt502 & 0x300) != 0) {
                        int j4 = class26_1.anInt500 - cameraX2;
                        int l5 = class26_1.anInt499 - cameraY2;
                        int k6 = class26_1.anInt501 - cameraZ2;
                        int i8 = class26_1.anInt503;
                        int k9;
                        if (i8 == 1 || i8 == 2)
                            k9 = -j4;
                        else
                            k9 = j4;
                        int k10;
                        if (i8 == 2 || i8 == 3)
                            k10 = -k6;
                        else
                            k10 = k6;
                        if ((class26_1.anInt502 & 0x100) != 0 && k10 < k9) {
                            int i11 = j4 + anIntArray463[i8];
                            int k11 = k6 + anIntArray464[i8];
                            class26_1.aClass30_Sub2_Sub4_504.renderAtPoint(
                                    i8 * 512 + 256, camUpDownY, camUpDownX,
                                    camLeftRightY, camLeftRightX, i11, l5, k11,
                                    class26_1.uid);
                        }
                        if ((class26_1.anInt502 & 0x200) != 0 && k10 > k9) {
                            int j11 = j4 + anIntArray465[i8];
                            int l11 = k6 + anIntArray466[i8];
                            class26_1.aClass30_Sub2_Sub4_504.renderAtPoint(
                                    i8 * 512 + 1280 & 0x7ff, camUpDownY,
                                    camUpDownX, camLeftRightY, camLeftRightX, j11, l5, l11,
                                    class26_1.uid);
                        }
                    }
                if (flag1) {
                    GroundObject class49 = class30_sub3_1.groundObject;
                    if (class49 != null)
                        class49.renderable.renderAtPoint(0, camUpDownY,
                                camUpDownX, camLeftRightY, camLeftRightX, class49.anInt812
                                        - cameraX2,
                                class49.anInt811 - cameraY2, class49.anInt813
                                        - cameraZ2, class49.uid);
                    Object4 object4_1 = class30_sub3_1.obj4;
                    if (object4_1 != null && object4_1.anInt52 == 0) {
                        if (object4_1.aClass30_Sub2_Sub4_49 != null)
                            object4_1.aClass30_Sub2_Sub4_49
                                    .renderAtPoint(0, camUpDownY, camUpDownX, camLeftRightY,
                                            camLeftRightX, object4_1.anInt46
                                                    - cameraX2,
                                            object4_1.anInt45 - cameraY2,
                                            object4_1.anInt47 - cameraZ2,
                                            object4_1.uid);
                        if (object4_1.aClass30_Sub2_Sub4_50 != null)
                            object4_1.aClass30_Sub2_Sub4_50
                                    .renderAtPoint(0, camUpDownY, camUpDownX, camLeftRightY,
                                            camLeftRightX, object4_1.anInt46
                                                    - cameraX2,
                                            object4_1.anInt45 - cameraY2,
                                            object4_1.anInt47 - cameraZ2,
                                            object4_1.uid);
                        if (object4_1.aClass30_Sub2_Sub4_48 != null)
                            object4_1.aClass30_Sub2_Sub4_48
                                    .renderAtPoint(0, camUpDownY, camUpDownX, camLeftRightY,
                                            camLeftRightX, object4_1.anInt46
                                                    - cameraX2,
                                            object4_1.anInt45 - cameraY2,
                                            object4_1.anInt47 - cameraZ2,
                                            object4_1.uid);
                    }
                }
                int k4 = class30_sub3_1.totalTiledObjectMask;
                if (k4 != 0) {
                    if (i < screenCenterX && (k4 & 4) != 0) {
                        Tile class30_sub3_17 = aclass30_sub3[i + 1][j];
                        if (class30_sub3_17 != null
                                && class30_sub3_17.aBoolean1323)
                            tileDeque.insertHead(class30_sub3_17);
                    }
                    if (j < yCameraTile && (k4 & 2) != 0) {
                        Tile class30_sub3_18 = aclass30_sub3[i][j + 1];
                        if (class30_sub3_18 != null
                                && class30_sub3_18.aBoolean1323)
                            tileDeque.insertHead(class30_sub3_18);
                    }
                    if (i > screenCenterX && (k4 & 1) != 0) {
                        Tile class30_sub3_19 = aclass30_sub3[i - 1][j];
                        if (class30_sub3_19 != null
                                && class30_sub3_19.aBoolean1323)
                            tileDeque.insertHead(class30_sub3_19);
                    }
                    if (j > yCameraTile && (k4 & 8) != 0) {
                        Tile class30_sub3_20 = aclass30_sub3[i][j - 1];
                        if (class30_sub3_20 != null
                                && class30_sub3_20.aBoolean1323)
                            tileDeque.insertHead(class30_sub3_20);
                    }
                }
            }
            if (class30_sub3_1.anInt1325 != 0) {
                boolean flag2 = true;
                for (int k1 = 0; k1 < class30_sub3_1.gameObjectIndex; k1++) {
                    if (class30_sub3_1.objects[k1].anInt528 == anInt448
                            || (class30_sub3_1.gameObjectsChanged[k1] & class30_sub3_1.anInt1325) != class30_sub3_1.anInt1326)
                        continue;
                    flag2 = false;
                    break;
                }

                if (flag2) {
                    WallObject class10_1 = class30_sub3_1.wallObject;
                    if (!method321(l, i, j, class10_1.orientation))
                        class10_1.renderable1.renderAtPoint(0, camUpDownY,
                                camUpDownX, camLeftRightY, camLeftRightX,
                                class10_1.anInt274 - cameraX2,
                                class10_1.anInt273 - cameraY2,
                                class10_1.anInt275 - cameraZ2, class10_1.uid);
                    class30_sub3_1.anInt1325 = 0;
                }
            }
            if (class30_sub3_1.aBoolean1324)
                try {
                    int i1 = class30_sub3_1.gameObjectIndex;
                    class30_sub3_1.aBoolean1324 = false;
                    int l1 = 0;
                    label0:
                    for (int k2 = 0; k2 < i1; k2++) {
                        GameObject class28_1 = class30_sub3_1.objects[k2];
                        if (class28_1.anInt528 == anInt448)
                            continue;
                        for (int k3 = class28_1.xLocLow; k3 <= class28_1.xLocHigh; k3++) {
                            for (int l4 = class28_1.yLocHigh; l4 <= class28_1.yLocLow; l4++) {
                                Tile class30_sub3_21 = aclass30_sub3[k3][l4];
                                if (class30_sub3_21.aBoolean1322) {
                                    class30_sub3_1.aBoolean1324 = true;
                                } else {
                                    if (class30_sub3_21.anInt1325 == 0)
                                        continue;
                                    int l6 = 0;
                                    if (k3 > class28_1.xLocLow)
                                        l6++;
                                    if (k3 < class28_1.xLocHigh)
                                        l6 += 4;
                                    if (l4 > class28_1.yLocHigh)
                                        l6 += 8;
                                    if (l4 < class28_1.yLocLow)
                                        l6 += 2;
                                    if ((l6 & class30_sub3_21.anInt1325) != class30_sub3_1.anInt1327)
                                        continue;
                                    class30_sub3_1.aBoolean1324 = true;
                                }
                                continue label0;
                            }

                        }

                        interactableObjects[l1++] = class28_1;
                        int i5 = screenCenterX - class28_1.xLocLow;
                        int i6 = class28_1.xLocHigh - screenCenterX;
                        if (i6 > i5)
                            i5 = i6;
                        int i7 = yCameraTile - class28_1.yLocHigh;
                        int j8 = class28_1.yLocLow - yCameraTile;
                        if (j8 > i7)
                            class28_1.anInt527 = i5 + j8;
                        else
                            class28_1.anInt527 = i5 + i7;
                    }

                    while (l1 > 0) {
                        int i3 = -50;
                        int l3 = -1;
                        for (int j5 = 0; j5 < l1; j5++) {
                            GameObject class28_2 = interactableObjects[j5];
                            if (class28_2.anInt528 != anInt448)
                                if (class28_2.anInt527 > i3) {
                                    i3 = class28_2.anInt527;
                                    l3 = j5;
                                } else if (class28_2.anInt527 == i3) {
                                    int j7 = class28_2.xPos - cameraX2;
                                    int k8 = class28_2.yPos - cameraZ2;
                                    int l9 = interactableObjects[l3].xPos
                                            - cameraX2;
                                    int l10 = interactableObjects[l3].yPos
                                            - cameraZ2;
                                    if (j7 * j7 + k8 * k8 > l9 * l9 + l10 * l10)
                                        l3 = j5;
                                }
                        }

                        if (l3 == -1)
                            break;
                        GameObject class28_3 = interactableObjects[l3];
                        class28_3.anInt528 = anInt448;
                        if (!method323(l, class28_3.xLocLow,
                                class28_3.xLocHigh, class28_3.yLocHigh,
                                class28_3.yLocLow,
                                class28_3.renderable.modelBaseY))
                            class28_3.renderable.renderAtPoint(
                                    class28_3.turnValue, camUpDownY, camUpDownX,
                                    camLeftRightY, camLeftRightX, class28_3.xPos
                                            - cameraX2, class28_3.tileHeight
                                            - cameraY2, class28_3.yPos
                                            - cameraZ2, class28_3.uid);
                        for (int k7 = class28_3.xLocLow; k7 <= class28_3.xLocHigh; k7++) {
                            for (int l8 = class28_3.yLocHigh; l8 <= class28_3.yLocLow; l8++) {
                                Tile class30_sub3_22 = aclass30_sub3[k7][l8];
                                if (class30_sub3_22.anInt1325 != 0)
                                    tileDeque.insertHead(class30_sub3_22);
                                else if ((k7 != i || l8 != j)
                                        && class30_sub3_22.aBoolean1323)
                                    tileDeque.insertHead(class30_sub3_22);
                            }

                        }

                    }
                    if (class30_sub3_1.aBoolean1324)
                        continue;
                } catch (Exception _ex) {
                    class30_sub3_1.aBoolean1324 = false;
                }
            if (!class30_sub3_1.aBoolean1323 || class30_sub3_1.anInt1325 != 0)
                continue;
            if (i <= screenCenterX && i > cameraLowTileX) {
                Tile class30_sub3_8 = aclass30_sub3[i - 1][j];
                if (class30_sub3_8 != null && class30_sub3_8.aBoolean1323)
                    continue;
            }
            if (i >= screenCenterX && i < cameraHighTileX - 1) {
                Tile class30_sub3_9 = aclass30_sub3[i + 1][j];
                if (class30_sub3_9 != null && class30_sub3_9.aBoolean1323)
                    continue;
            }
            if (j <= yCameraTile && j > cameraLowTileY) {
                Tile class30_sub3_10 = aclass30_sub3[i][j - 1];
                if (class30_sub3_10 != null && class30_sub3_10.aBoolean1323)
                    continue;
            }
            if (j >= yCameraTile && j < cameraHighTileY - 1) {
                Tile class30_sub3_11 = aclass30_sub3[i][j + 1];
                if (class30_sub3_11 != null && class30_sub3_11.aBoolean1323)
                    continue;
            }
            class30_sub3_1.aBoolean1323 = false;
            anInt446--;
            Object4 object4 = class30_sub3_1.obj4;
            if (object4 != null && object4.anInt52 != 0) {
                if (object4.aClass30_Sub2_Sub4_49 != null)
                    object4.aClass30_Sub2_Sub4_49.renderAtPoint(0, camUpDownY,
                            camUpDownX, camLeftRightY, camLeftRightX, object4.anInt46
                                    - cameraX2, object4.anInt45 - cameraY2
                                    - object4.anInt52, object4.anInt47
                                    - cameraZ2, object4.uid);
                if (object4.aClass30_Sub2_Sub4_50 != null)
                    object4.aClass30_Sub2_Sub4_50.renderAtPoint(0, camUpDownY,
                            camUpDownX, camLeftRightY, camLeftRightX, object4.anInt46
                                    - cameraX2, object4.anInt45 - cameraY2
                                    - object4.anInt52, object4.anInt47
                                    - cameraZ2, object4.uid);
                if (object4.aClass30_Sub2_Sub4_48 != null)
                    object4.aClass30_Sub2_Sub4_48.renderAtPoint(0, camUpDownY,
                            camUpDownX, camLeftRightY, camLeftRightX, object4.anInt46
                                    - cameraX2, object4.anInt45 - cameraY2
                                    - object4.anInt52, object4.anInt47
                                    - cameraZ2, object4.uid);
            }
            if (class30_sub3_1.anInt1328 != 0) {
                Object2 class26 = class30_sub3_1.obj2;
                if (class26 != null
                        && !method322(l, i, j,
                        class26.aClass30_Sub2_Sub4_504.modelBaseY))
                    if ((class26.anInt502 & class30_sub3_1.anInt1328) != 0)
                        class26.aClass30_Sub2_Sub4_504.renderAtPoint(
                                class26.anInt503, camUpDownY, camUpDownX, camLeftRightY,
                                camLeftRightX, class26.anInt500 - cameraX2,
                                class26.anInt499 - cameraY2, class26.anInt501
                                        - cameraZ2, class26.uid);
                    else if ((class26.anInt502 & 0x300) != 0) {
                        int l2 = class26.anInt500 - cameraX2;
                        int j3 = class26.anInt499 - cameraY2;
                        int i4 = class26.anInt501 - cameraZ2;
                        int k5 = class26.anInt503;
                        int j6;
                        if (k5 == 1 || k5 == 2)
                            j6 = -l2;
                        else
                            j6 = l2;
                        int l7;
                        if (k5 == 2 || k5 == 3)
                            l7 = -i4;
                        else
                            l7 = i4;
                        if ((class26.anInt502 & 0x100) != 0 && l7 >= j6) {
                            int i9 = l2 + anIntArray463[k5];
                            int i10 = i4 + anIntArray464[k5];
                            class26.aClass30_Sub2_Sub4_504.renderAtPoint(
                                    k5 * 512 + 256, camUpDownY, camUpDownX,
                                    camLeftRightY, camLeftRightX, i9, j3, i10,
                                    class26.uid);
                        }
                        if ((class26.anInt502 & 0x200) != 0 && l7 <= j6) {
                            int j9 = l2 + anIntArray465[k5];
                            int j10 = i4 + anIntArray466[k5];
                            class26.aClass30_Sub2_Sub4_504.renderAtPoint(
                                    k5 * 512 + 1280 & 0x7ff, camUpDownY,
                                    camUpDownX, camLeftRightY, camLeftRightX, j9, j3, j10,
                                    class26.uid);
                        }
                    }
                WallObject class10_2 = class30_sub3_1.wallObject;
                if (class10_2 != null) {
                    if ((class10_2.orientation1 & class30_sub3_1.anInt1328) != 0
                            && !method321(l, i, j, class10_2.orientation1))
                        class10_2.renderable2.renderAtPoint(0, camUpDownY,
                                camUpDownX, camLeftRightY, camLeftRightX,
                                class10_2.anInt274 - cameraX2,
                                class10_2.anInt273 - cameraY2,
                                class10_2.anInt275 - cameraZ2, class10_2.uid);
                    if ((class10_2.orientation & class30_sub3_1.anInt1328) != 0
                            && !method321(l, i, j, class10_2.orientation))
                        class10_2.renderable1.renderAtPoint(0, camUpDownY,
                                camUpDownX, camLeftRightY, camLeftRightX,
                                class10_2.anInt274 - cameraX2,
                                class10_2.anInt273 - cameraY2,
                                class10_2.anInt275 - cameraZ2, class10_2.uid);
                }
            }
            if (k < numberOfZ - 1) {
                Tile class30_sub3_12 = tiles[k + 1][i][j];
                if (class30_sub3_12 != null && class30_sub3_12.aBoolean1323)
                    tileDeque.insertHead(class30_sub3_12);
            }
            if (i < screenCenterX) {
                Tile class30_sub3_13 = aclass30_sub3[i + 1][j];
                if (class30_sub3_13 != null && class30_sub3_13.aBoolean1323)
                    tileDeque.insertHead(class30_sub3_13);
            }
            if (j < yCameraTile) {
                Tile class30_sub3_14 = aclass30_sub3[i][j + 1];
                if (class30_sub3_14 != null && class30_sub3_14.aBoolean1323)
                    tileDeque.insertHead(class30_sub3_14);
            }
            if (i > screenCenterX) {
                Tile class30_sub3_15 = aclass30_sub3[i - 1][j];
                if (class30_sub3_15 != null && class30_sub3_15.aBoolean1323)
                    tileDeque.insertHead(class30_sub3_15);
            }
            if (j > yCameraTile) {
                Tile class30_sub3_16 = aclass30_sub3[i][j - 1];
                if (class30_sub3_16 != null && class30_sub3_16.aBoolean1323)
                    tileDeque.insertHead(class30_sub3_16);
            }
        } while (true);
    }

   public static boolean boolean666 = false; //hdtextures
    private void method315(SceneTilePaint tile, int i, int j, int k, int l, int i1,
                           int j1, int k1) {
        int l1;
        int i2 = l1 = (j1 << 7) - cameraX2;
        int j2;
        int k2 = j2 = (k1 << 7) - cameraZ2;
        int l2;
        int i3 = l2 = i2 + 128;
        int j3;
        int k3 = j3 = k2 + 128;
        int l3 = heightMap[i][j1][k1] - cameraY2;
        int i4 = heightMap[i][j1 + 1][k1] - cameraY2;
        int j4 = heightMap[i][j1 + 1][k1 + 1] - cameraY2;
        int k4 = heightMap[i][j1][k1 + 1] - cameraY2;
        int l4 = k2 * l + i2 * i1 >> 16;
        k2 = k2 * i1 - i2 * l >> 16;
        i2 = l4;
        l4 = l3 * k - k2 * j >> 16;
        k2 = l3 * j + k2 * k >> 16;
        l3 = l4;
        if (k2 < 50)
            return;
        l4 = j2 * l + i3 * i1 >> 16;
        j2 = j2 * i1 - i3 * l >> 16;
        i3 = l4;
        l4 = i4 * k - j2 * j >> 16;
        j2 = i4 * j + j2 * k >> 16;
        i4 = l4;
        if (j2 < 50)
            return;
        l4 = k3 * l + l2 * i1 >> 16;
        k3 = k3 * i1 - l2 * l >> 16;
        l2 = l4;
        l4 = j4 * k - k3 * j >> 16;
        k3 = j4 * j + k3 * k >> 16;
        j4 = l4;
        if (k3 < 50)
            return;
        l4 = j3 * l + l1 * i1 >> 16;
        j3 = j3 * i1 - l1 * l >> 16;
        l1 = l4;
        l4 = k4 * k - j3 * j >> 16;
        j3 = k4 * j + j3 * k >> 16;
        k4 = l4;
        if (j3 < 50)
            return;
        int i5 = Rasterizer3D.originViewX + (i2 * WorldController.focalLength)
                / k2;
        int j5 = Rasterizer3D.originViewY + (l3 * WorldController.focalLength)
                / k2;
        int k5 = Rasterizer3D.originViewX + (i3 * WorldController.focalLength)
                / j2;
        int l5 = Rasterizer3D.originViewY + (i4 * WorldController.focalLength)
                / j2;
        int i6 = Rasterizer3D.originViewX + (l2 * WorldController.focalLength)
                / k3;
        int j6 = Rasterizer3D.originViewY + (j4 * WorldController.focalLength)
                / k3;
        int k6 = Rasterizer3D.originViewX + (l1 * WorldController.focalLength)
                / j3;
        int l6 = Rasterizer3D.originViewY + (k4 * WorldController.focalLength)
                / j3;

        Rasterizer3D.alpha = 0;
        if ((i6 - k6) * (l5 - l6) - (j6 - l6) * (k5 - k6) > 0) {
            Rasterizer3D.textureOutOfDrawingBounds = i6 < 0 || k6 < 0 || k5 < 0
                    || i6 > Rasterizer2D.lastX || k6 > Rasterizer2D.lastX
                    || k5 > Rasterizer2D.lastX;
            if (clicked
                    && method318(clickScreenX, clickScreenY, j6, l6, l5, i6, k6, k5)) {
                clickedTileX = j1;
                clickedTileY = k1;
            }
            if (tile.texture == -1) {
                if (tile.centerColor != 0xbc614e) {
                    Rasterizer3D.drawShadedTriangle(j6, l6, l5, i6, k6, k5, tile.centerColor,
                            tile.eastColor, tile.northColor, k3, j3, j2);
                }
            } else if (!lowMem) {
                if (tile.flat) {
                    Rasterizer3D.drawTexturedTriangle(j6, l6, l5, i6, k6, k5, tile.centerColor,
                            tile.eastColor, tile.northColor, i2, i3, l1, l3, i4, k4, k2, j2,
                            j3, tile.texture, k3, j3, j2);
                } else {
                    Rasterizer3D.drawTexturedTriangle(j6, l6, l5, i6, k6, k5, tile.centerColor,
                            tile.eastColor, tile.northColor, l2, l1, i3, j4, k4, i4, k3, j3,
                            j2, tile.texture, k3, j3, j2);
                }
            } else {
                int textureColor = TEXTURE_COLORS[tile.texture];
                Rasterizer3D.drawShadedTriangle(j6, l6, l5, i6, k6, k5,
                        light(textureColor, tile.centerColor),
                        light(textureColor, tile.eastColor),
                        light(textureColor, tile.northColor), k3, j3, j2);
            }
        }
        if ((i5 - k5) * (l6 - l5) - (j5 - l5) * (k6 - k5) > 0) {
            Rasterizer3D.textureOutOfDrawingBounds = i5 < 0 || k5 < 0 || k6 < 0
                    || i5 > Rasterizer2D.lastX || k5 > Rasterizer2D.lastX
                    || k6 > Rasterizer2D.lastX;
            if (clicked
                    && method318(clickScreenX, clickScreenY, j5, l5, l6, i5, k5, k6)) {
                clickedTileX = j1;
                clickedTileY = k1;
            }
            if (tile.texture == -1) {
                if (tile.northEastColor != 0xbc614e) {
                    Rasterizer3D.drawShadedTriangle(j5, l5, l6, i5, k5, k6, tile.northEastColor,
                            tile.northColor, tile.eastColor, k2, j2, j3);
                }
            } else {
                if (!lowMem) {
                    Rasterizer3D.drawTexturedTriangle(j5, l5, l6, i5, k5, k6, tile.northEastColor,
                            tile.northColor, tile.eastColor, i2, i3, l1, l3, i4, k4, k2, j2,
                            j3, tile.texture, k2, j2, j3);
                    return;
                }
                int j7 = TEXTURE_COLORS[tile.texture];
                Rasterizer3D.drawShadedTriangle(j5, l5, l6, i5, k5, k6,
                        light(j7, tile.northEastColor), light(j7, tile.northColor),
                        light(j7, tile.eastColor), k2, j2, j3);
            }
        }
    }

    private void method316(int i, int j, int k, SceneTileModel tile, int l,
                           int i1, int j1) {
        int k1 = tile.origVertexX.length;
        for (int l1 = 0; l1 < k1; l1++) {
            int i2 = tile.origVertexX[l1] - cameraX2;
            int k2 = tile.origVertexY[l1] - cameraY2;
            int i3 = tile.origVertexZ[l1] - cameraZ2;
            int k3 = i3 * k + i2 * j1 >> 16;
            i3 = i3 * j1 - i2 * k >> 16;
            i2 = k3;
            k3 = k2 * l - i3 * j >> 16;
            i3 = k2 * j + i3 * l >> 16;
            k2 = k3;
            if (i3 < 50) {
                return;
            }
            if (tile.triangleTexture != null) {
                tile.anIntArray690[l1] = i2;
                tile.anIntArray691[l1] = k2;
                tile.anIntArray692[l1] = i3;
            }
            tile.anIntArray688[l1] = Rasterizer3D.originViewX + (i2 * WorldController.focalLength) / i3;
            tile.anIntArray689[l1] = Rasterizer3D.originViewY + (k2 * WorldController.focalLength) / i3;
            tile.depthPoint[l1] = i3;
        }

        Rasterizer3D.alpha = 0;
        k1 = tile.triangleA.length;
        for (int j2 = 0; j2 < k1; j2++) {
            int l2 = tile.triangleA[j2];
            int j3 = tile.triangleB[j2];
            int l3 = tile.triangleC[j2];
            int i4 = tile.anIntArray688[l2];
            int j4 = tile.anIntArray688[j3];
            int k4 = tile.anIntArray688[l3];
            int l4 = tile.anIntArray689[l2];
            int i5 = tile.anIntArray689[j3];
            int j5 = tile.anIntArray689[l3];
            if ((i4 - j4) * (j5 - i5) - (l4 - i5) * (k4 - j4) > 0) {
                Rasterizer3D.textureOutOfDrawingBounds = i4 < 0 || j4 < 0 || k4 < 0
                        || i4 > Rasterizer2D.lastX || j4 > Rasterizer2D.lastX
                        || k4 > Rasterizer2D.lastX;
                if (clicked && method318(clickScreenX, clickScreenY, l4, i5, j5, i4, j4, k4)) {
                    clickedTileX = i;
                    clickedTileY = i1;
                }
                Rasterizer3D.drawDepthTriangle(i4, j4, k4, l4, i5, j5, tile.depthPoint[l2],
                        tile.depthPoint[j3], tile.depthPoint[l3]);
                if (tile.triangleTexture == null || tile.triangleTexture[j2] == -1) {
                    if (tile.triangleHslA[j2] != 0xbc614e) {
                        Rasterizer3D.drawShadedTriangle(l4, i5, j5, i4, j4, k4, tile.triangleHslA[j2],
                                tile.triangleHslB[j2], tile.triangleHslC[j2], tile.depthPoint[l2],
                                tile.depthPoint[j3], tile.depthPoint[l3]);
                    }
                } else if (!lowMem) {
                    if (tile.flat) {
                        Rasterizer3D.drawTexturedTriangle(l4, i5, j5, i4, j4, k4, tile.triangleHslA[j2],
                                tile.triangleHslB[j2], tile.triangleHslC[j2], tile.anIntArray690[0],
                                tile.anIntArray690[1], tile.anIntArray690[3],
                                tile.anIntArray691[0], tile.anIntArray691[1],
                                tile.anIntArray691[3], tile.anIntArray692[0],
                                tile.anIntArray692[1], tile.anIntArray692[3], tile.triangleTexture[j2],
                                tile.depthPoint[l2], tile.depthPoint[j3], tile.depthPoint[l3]);
                    } else {
                        Rasterizer3D.drawTexturedTriangle(l4, i5, j5, i4, j4, k4, tile.triangleHslA[j2],
                                tile.triangleHslB[j2], tile.triangleHslC[j2], tile.anIntArray690[l2],
                                tile.anIntArray690[j3], tile.anIntArray690[l3],
                                tile.anIntArray691[l2], tile.anIntArray691[j3],
                                tile.anIntArray691[l3], tile.anIntArray692[l2],
                                tile.anIntArray692[j3], tile.anIntArray692[l3],
                                tile.triangleTexture[j2], tile.depthPoint[l2], tile.depthPoint[j3],
                                tile.depthPoint[l3]);
                    }
                } else {
                    int k5 = TEXTURE_COLORS[tile.triangleTexture[j2]];
                    Rasterizer3D.drawShadedTriangle(l4, i5, j5, i4, j4, k4,
                            light(k5, tile.triangleHslA[j2]), light(k5, tile.triangleHslB[j2]),
                            light(k5, tile.triangleHslC[j2]), tile.depthPoint[l2],
                            tile.depthPoint[j3], tile.depthPoint[l3]);
                }
            }
        }
    }

    public int light(int j, int k) {
        k = 127 - k;
        k = (k * (j & 0x7f)) / 160;
        if (k < 2) {
            k = 2;
        } else if (k > 126) {
            k = 126;
        }
        return (j & 0xff80) + k;
    }

    private boolean method318(int i, int j, int k, int l, int i1, int j1,
                              int k1, int l1) {
        if (j < k && j < l && j < i1)
            return false;
        if (j > k && j > l && j > i1)
            return false;
        if (i < j1 && i < k1 && i < l1)
            return false;
        if (i > j1 && i > k1 && i > l1)
            return false;
        int i2 = (j - k) * (k1 - j1) - (i - j1) * (l - k);
        int j2 = (j - i1) * (j1 - l1) - (i - l1) * (k - i1);
        int k2 = (j - l) * (l1 - k1) - (i - k1) * (i1 - l);
        return i2 * k2 > 0 && k2 * j2 > 0;
    }

    private void method319() {
        int j = sceneClusterCounts[currentRenderPlane];
        Class47 aclass47[] = sceneClusters[currentRenderPlane];
        anInt475 = 0;
        for (int k = 0; k < j; k++) {
            Class47 class47 = aclass47[k];
            if (class47.anInt791 == 1) {
                int l = (class47.anInt787 - screenCenterX) + 25;
                if (l < 0 || l > 50)
                    continue;
                int k1 = (class47.anInt789 - yCameraTile) + 25;
                if (k1 < 0)
                    k1 = 0;
                int j2 = (class47.anInt790 - yCameraTile) + 25;
                if (j2 > 50)
                    j2 = 50;
                boolean flag = false;
                while (k1 <= j2)
                    if (aBooleanArrayArray492[l][k1++]) {
                        flag = true;
                        break;
                    }
                if (!flag)
                    continue;
                int j3 = cameraX2 - class47.anInt792;
                if (j3 > 32) {
                    class47.anInt798 = 1;
                } else {
                    if (j3 >= -32)
                        continue;
                    class47.anInt798 = 2;
                    j3 = -j3;
                }
                class47.anInt801 = (class47.anInt794 - cameraZ2 << 8) / j3;
                class47.anInt802 = (class47.anInt795 - cameraZ2 << 8) / j3;
                class47.anInt803 = (class47.anInt796 - cameraY2 << 8) / j3;
                class47.anInt804 = (class47.anInt797 - cameraY2 << 8) / j3;
                aClass47Array476[anInt475++] = class47;
                continue;
            }
            if (class47.anInt791 == 2) {
                int i1 = (class47.anInt789 - yCameraTile) + 25;
                if (i1 < 0 || i1 > 50)
                    continue;
                int l1 = (class47.anInt787 - screenCenterX) + 25;
                if (l1 < 0)
                    l1 = 0;
                int k2 = (class47.anInt788 - screenCenterX) + 25;
                if (k2 > 50)
                    k2 = 50;
                boolean flag1 = false;
                while (l1 <= k2)
                    if (aBooleanArrayArray492[l1++][i1]) {
                        flag1 = true;
                        break;
                    }
                if (!flag1)
                    continue;
                int k3 = cameraZ2 - class47.anInt794;
                if (k3 > 32) {
                    class47.anInt798 = 3;
                } else {
                    if (k3 >= -32)
                        continue;
                    class47.anInt798 = 4;
                    k3 = -k3;
                }
                class47.anInt799 = (class47.anInt792 - cameraX2 << 8) / k3;
                class47.anInt800 = (class47.anInt793 - cameraX2 << 8) / k3;
                class47.anInt803 = (class47.anInt796 - cameraY2 << 8) / k3;
                class47.anInt804 = (class47.anInt797 - cameraY2 << 8) / k3;
                aClass47Array476[anInt475++] = class47;
            } else if (class47.anInt791 == 4) {
                int j1 = class47.anInt796 - cameraY2;
                if (j1 > 128) {
                    int i2 = (class47.anInt789 - yCameraTile) + 25;
                    if (i2 < 0)
                        i2 = 0;
                    int l2 = (class47.anInt790 - yCameraTile) + 25;
                    if (l2 > 50)
                        l2 = 50;
                    if (i2 <= l2) {
                        int i3 = (class47.anInt787 - screenCenterX) + 25;
                        if (i3 < 0)
                            i3 = 0;
                        int l3 = (class47.anInt788 - screenCenterX) + 25;
                        if (l3 > 50)
                            l3 = 50;
                        boolean flag2 = false;
                        label0:
                        for (int i4 = i3; i4 <= l3; i4++) {
                            for (int j4 = i2; j4 <= l2; j4++) {
                                if (!aBooleanArrayArray492[i4][j4])
                                    continue;
                                flag2 = true;
                                break label0;
                            }

                        }

                        if (flag2) {
                            class47.anInt798 = 5;
                            class47.anInt799 = (class47.anInt792 - cameraX2 << 8)
                                    / j1;
                            class47.anInt800 = (class47.anInt793 - cameraX2 << 8)
                                    / j1;
                            class47.anInt801 = (class47.anInt794 - cameraZ2 << 8)
                                    / j1;
                            class47.anInt802 = (class47.anInt795 - cameraZ2 << 8)
                                    / j1;
                            aClass47Array476[anInt475++] = class47;
                        }
                    }
                }
            }
        }

    }

    private boolean method320(int i, int j, int k) {
        int l = anIntArrayArrayArray445[i][j][k];
        if (l == -anInt448)
            return false;
        if (l == anInt448)
            return true;
        int i1 = j << 7;
        int j1 = k << 7;
        if (method324(i1 + 1, heightMap[i][j][k], j1 + 1)
                && method324((i1 + 128) - 1,
                heightMap[i][j + 1][k], j1 + 1)
                && method324((i1 + 128) - 1,
                heightMap[i][j + 1][k + 1],
                (j1 + 128) - 1)
                && method324(i1 + 1, heightMap[i][j][k + 1],
                (j1 + 128) - 1)) {
            anIntArrayArrayArray445[i][j][k] = anInt448;
            return true;
        } else {
            anIntArrayArrayArray445[i][j][k] = -anInt448;
            return false;
        }
    }

    private boolean method321(int i, int j, int k, int l) {
        if (!method320(i, j, k))
            return false;
        int i1 = j << 7;
        int j1 = k << 7;
        int k1 = heightMap[i][j][k] - 1;
        int l1 = k1 - 120;
        int i2 = k1 - 230;
        int j2 = k1 - 238;
        if (l < 16) {
            if (l == 1) {
                if (i1 > cameraX2) {
                    if (!method324(i1, k1, j1))
                        return false;
                    if (!method324(i1, k1, j1 + 128))
                        return false;
                }
                if (i > 0) {
                    if (!method324(i1, l1, j1))
                        return false;
                    if (!method324(i1, l1, j1 + 128))
                        return false;
                }
                return method324(i1, i2, j1) && method324(i1, i2, j1 + 128);
            }
            if (l == 2) {
                if (j1 < cameraZ2) {
                    if (!method324(i1, k1, j1 + 128))
                        return false;
                    if (!method324(i1 + 128, k1, j1 + 128))
                        return false;
                }
                if (i > 0) {
                    if (!method324(i1, l1, j1 + 128))
                        return false;
                    if (!method324(i1 + 128, l1, j1 + 128))
                        return false;
                }
                return method324(i1, i2, j1 + 128)
                        && method324(i1 + 128, i2, j1 + 128);
            }
            if (l == 4) {
                if (i1 < cameraX2) {
                    if (!method324(i1 + 128, k1, j1))
                        return false;
                    if (!method324(i1 + 128, k1, j1 + 128))
                        return false;
                }
                if (i > 0) {
                    if (!method324(i1 + 128, l1, j1))
                        return false;
                    if (!method324(i1 + 128, l1, j1 + 128))
                        return false;
                }
                return method324(i1 + 128, i2, j1)
                        && method324(i1 + 128, i2, j1 + 128);
            }
            if (l == 8) {
                if (j1 > cameraZ2) {
                    if (!method324(i1, k1, j1))
                        return false;
                    if (!method324(i1 + 128, k1, j1))
                        return false;
                }
                if (i > 0) {
                    if (!method324(i1, l1, j1))
                        return false;
                    if (!method324(i1 + 128, l1, j1))
                        return false;
                }
                return method324(i1, i2, j1) && method324(i1 + 128, i2, j1);
            }
        }
        if (!method324(i1 + 64, j2, j1 + 64))
            return false;
        if (l == 16)
            return method324(i1, i2, j1 + 128);
        if (l == 32)
            return method324(i1 + 128, i2, j1 + 128);
        if (l == 64)
            return method324(i1 + 128, i2, j1);
        if (l == 128) {
            return method324(i1, i2, j1);
        } else {
            System.out.println("Warning unsupported wall type");
            return true;
        }
    }

    private boolean method322(int i, int j, int k, int l) {
        if (!method320(i, j, k))
            return false;
        int i1 = j << 7;
        int j1 = k << 7;
        return method324(i1 + 1, heightMap[i][j][k] - l, j1 + 1)
                && method324((i1 + 128) - 1,
                heightMap[i][j + 1][k] - l, j1 + 1)
                && method324((i1 + 128) - 1,
                heightMap[i][j + 1][k + 1] - l,
                (j1 + 128) - 1)
                && method324(i1 + 1, heightMap[i][j][k + 1] - l,
                (j1 + 128) - 1);
    }

    private boolean method323(int i, int j, int k, int l, int i1, int j1) {
        if (j == k && l == i1) {
            if (!method320(i, j, l))
                return false;
            int k1 = j << 7;
            int i2 = l << 7;
            return method324(k1 + 1, heightMap[i][j][l] - j1,
                    i2 + 1)
                    && method324((k1 + 128) - 1,
                    heightMap[i][j + 1][l] - j1, i2 + 1)
                    && method324((k1 + 128) - 1,
                    heightMap[i][j + 1][l + 1] - j1,
                    (i2 + 128) - 1)
                    && method324(k1 + 1, heightMap[i][j][l + 1]
                    - j1, (i2 + 128) - 1);
        }
        for (int l1 = j; l1 <= k; l1++) {
            for (int j2 = l; j2 <= i1; j2++)
                if (anIntArrayArrayArray445[i][l1][j2] == -anInt448)
                    return false;

        }

        int k2 = (j << 7) + 1;
        int l2 = (l << 7) + 2;
        int i3 = heightMap[i][j][l] - j1;
        if (!method324(k2, i3, l2))
            return false;
        int j3 = (k << 7) - 1;
        if (!method324(j3, i3, l2))
            return false;
        int k3 = (i1 << 7) - 1;
        return method324(k2, i3, k3) && method324(j3, i3, k3);
    }

    private boolean method324(int i, int j, int k) {
        for (int l = 0; l < anInt475; l++) {
            Class47 class47 = aClass47Array476[l];
            if (class47.anInt798 == 1) {
                int i1 = class47.anInt792 - i;
                if (i1 > 0) {
                    int j2 = class47.anInt794 + (class47.anInt801 * i1 >> 8);
                    int k3 = class47.anInt795 + (class47.anInt802 * i1 >> 8);
                    int l4 = class47.anInt796 + (class47.anInt803 * i1 >> 8);
                    int i6 = class47.anInt797 + (class47.anInt804 * i1 >> 8);
                    if (k >= j2 && k <= k3 && j >= l4 && j <= i6)
                        return true;
                }
            } else if (class47.anInt798 == 2) {
                int j1 = i - class47.anInt792;
                if (j1 > 0) {
                    int k2 = class47.anInt794 + (class47.anInt801 * j1 >> 8);
                    int l3 = class47.anInt795 + (class47.anInt802 * j1 >> 8);
                    int i5 = class47.anInt796 + (class47.anInt803 * j1 >> 8);
                    int j6 = class47.anInt797 + (class47.anInt804 * j1 >> 8);
                    if (k >= k2 && k <= l3 && j >= i5 && j <= j6)
                        return true;
                }
            } else if (class47.anInt798 == 3) {
                int k1 = class47.anInt794 - k;
                if (k1 > 0) {
                    int l2 = class47.anInt792 + (class47.anInt799 * k1 >> 8);
                    int i4 = class47.anInt793 + (class47.anInt800 * k1 >> 8);
                    int j5 = class47.anInt796 + (class47.anInt803 * k1 >> 8);
                    int k6 = class47.anInt797 + (class47.anInt804 * k1 >> 8);
                    if (i >= l2 && i <= i4 && j >= j5 && j <= k6)
                        return true;
                }
            } else if (class47.anInt798 == 4) {
                int l1 = k - class47.anInt794;
                if (l1 > 0) {
                    int i3 = class47.anInt792 + (class47.anInt799 * l1 >> 8);
                    int j4 = class47.anInt793 + (class47.anInt800 * l1 >> 8);
                    int k5 = class47.anInt796 + (class47.anInt803 * l1 >> 8);
                    int l6 = class47.anInt797 + (class47.anInt804 * l1 >> 8);
                    if (i >= i3 && i <= j4 && j >= k5 && j <= l6)
                        return true;
                }
            } else if (class47.anInt798 == 5) {
                int i2 = j - class47.anInt796;
                if (i2 > 0) {
                    int j3 = class47.anInt792 + (class47.anInt799 * i2 >> 8);
                    int k4 = class47.anInt793 + (class47.anInt800 * i2 >> 8);
                    int l5 = class47.anInt794 + (class47.anInt801 * i2 >> 8);
                    int i7 = class47.anInt795 + (class47.anInt802 * i2 >> 8);
                    if (i >= j3 && i <= k4 && k >= l5 && k <= i7)
                        return true;
                }
            }
        }

        return false;
    }

    private boolean aBoolean434;
    public static boolean lowMem = true;
    private final int numberOfZ;
    private final int xRegionSize;
    private final int yRegionSize;
    private final int[][][] heightMap;
    private final Tile[][][] tiles;
    private int zAnInt442;
    private int interactableObjectCacheCurrPos;
    private final GameObject[] gameObjectsCache;
    private final int[][][] anIntArrayArrayArray445;
    private static int anInt446;
    private static int currentRenderPlane;
    private static int anInt448;
    private static int cameraLowTileX;
    private static int cameraHighTileX;
    private static int cameraLowTileY;
    private static int cameraHighTileY;
    private static int screenCenterX;
    private static int yCameraTile;
    private static int cameraX2;
    private static int cameraY2;
    private static int cameraZ2;
    private static int camUpDownY;
    private static int camUpDownX;
    private static int camLeftRightY;
    private static int camLeftRightX;
    private static GameObject[] interactableObjects = new GameObject[100];
    private static final int[] anIntArray463 = {53, -53, -53, 53};
    private static final int[] anIntArray464 = {-53, -53, 53, 53};
    private static final int[] anIntArray465 = {-45, 45, 45, -45};
    private static final int[] anIntArray466 = {45, 45, -45, -45};
    private static boolean clicked;
    private static int clickScreenX;
    private static int clickScreenY;
    public static int clickedTileX = -1;
    public static int clickedTileY = -1;
    private static final int cullingClusterPlaneCount;
    private static int[] sceneClusterCounts;
    private static Class47[][] sceneClusters;
    private static int anInt475;
    private static final Class47[] aClass47Array476 = new Class47[500];
    private static NodeList tileDeque = new NodeList();
    private static final int[] anIntArray478 = {19, 55, 38, 155, 255, 110,
            137, 205, 76};
    private static final int[] anIntArray479 = {160, 192, 80, 96, 0, 144, 80,
            48, 160};
    private static final int[] anIntArray480 = {76, 8, 137, 4, 0, 1, 38, 2, 19};
    private static final int[] anIntArray481 = {0, 0, 2, 0, 0, 2, 1, 1, 0};
    private static final int[] anIntArray482 = {2, 0, 0, 2, 0, 0, 0, 4, 4};
    private static final int[] anIntArray483 = {0, 4, 4, 8, 0, 0, 8, 0, 0};
    private static final int[] anIntArray484 = {1, 1, 0, 0, 0, 8, 0, 0, 8};
    private static final int[] TEXTURE_COLORS = {41, 39248, 41, 4643, 41, 41,
            41, 41, 41, 41, 41, 41, 41, 41, 41, 43086, 41, 41, 41, 41, 41, 41,
            41, 8602, 41, 28992, 41, 41, 41, 41, 41, 5056, 41, 41, 41, 7079,
            41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 3131, 41, 41, 41};
    private final int[] anIntArray486;
    private final int[] anIntArray487;
    private int anInt488;
    private final int[][] tileVertices = {new int[16],
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
            {1, 0, 0, 0, 1, 1, 0, 0, 1, 1, 1, 0, 1, 1, 1, 1},
            {1, 1, 0, 0, 1, 1, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0},
            {0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 0, 1, 0, 0, 0, 1},
            {0, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
            {1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1},
            {1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 1, 0, 0},
            {1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 0, 0, 1, 1},
            {1, 1, 1, 1, 1, 1, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 1, 1, 1, 1}};
    private final int[][] tileVertexIndices = {
            {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15},
            {12, 8, 4, 0, 13, 9, 5, 1, 14, 10, 6, 2, 15, 11, 7, 3},
            {15, 14, 13, 12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1, 0},
            {3, 7, 11, 15, 2, 6, 10, 14, 1, 5, 9, 13, 0, 4, 8, 12}};
    private static boolean[][][][] aBooleanArrayArrayArrayArray491;
    private static boolean[][] aBooleanArrayArray492;
    private static int centerX;
    private static int centerY;
    private static int anInt495;
    private static int anInt496;
    private static int viewportWidth;
    private static int viewportHeight;
    public static int focalLength = 512;
	public static int viewDistance = 9;
    static {
        aBooleanArrayArrayArrayArray491 = new boolean[8][32][(25 * 2) + 1][(25 * 2) + 1];
        focalLength = 512;
        cullingClusterPlaneCount = 4;
        sceneClusterCounts = new int[cullingClusterPlaneCount];
        sceneClusters = new Class47[cullingClusterPlaneCount][500];
    }


}

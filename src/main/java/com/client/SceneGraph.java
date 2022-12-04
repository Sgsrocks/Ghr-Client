package com.client;

final class SceneGraph {

    public SceneGraph(int ai[][][]) {
        int i = 104;// was parameter
        int j = 104;// was parameter
        int k = 4;// was parameter
        aBoolean434 = true;
        gameObjectsCache = new GameObject[5000];
        anIntArray486 = new int[10000];
        anIntArray487 = new int[10000];
        numberOfZ = k;
        maxX = j;
        maxZ = i;
        tileArray = new Tile[k][j][i];
        anIntArrayArrayArray445 = new int[k][j + 1][i + 1];
        heightMap = ai;
        initToNull();
    }

    public static void destructor() {
        interactableObjects = null;
        sceneClusterCounts = null;
        sceneClusters = null;
        tileDeque = null;
        visibilityMap = null;
        renderArea = null;
    }

    public void initToNull() {
        for (int j = 0; j < numberOfZ; j++)
            for (int k = 0; k < maxX; k++)
                for (int i1 = 0; i1 < maxZ; i1++)
                    tileArray[j][k][i1] = null;
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
        for (int k = 0; k < maxX; k++) {
            for (int l = 0; l < maxZ; l++)
                if (tileArray[i][k][l] == null)
                    tileArray[i][k][l] = new Tile(i, k, l);

        }

    }

    public void applyBridgeMode(int yLoc, int xLoc) {
        Tile tileFirstFloor = tileArray[0][xLoc][yLoc];
        for (int l = 0; l < 3; l++) {
            Tile tile = tileArray[l][xLoc][yLoc] = tileArray[l + 1][xLoc][yLoc];
            if (tile != null) {
                tile.z1AnInt1307--;
                for (int j1 = 0; j1 < tile.gameObjectIndex; j1++) {
                    GameObject gameObject = tile.gameObjects[j1];
                    if ((gameObject.uid >> 29 & 3) == 2 && gameObject.xLocLow == xLoc && gameObject.yLocHigh == yLoc)
                        gameObject.zLoc--;
                }

            }
        }
        if (tileArray[0][xLoc][yLoc] == null)
            tileArray[0][xLoc][yLoc] = new Tile(0, xLoc, yLoc);
        tileArray[0][xLoc][yLoc].firstFloorTile = tileFirstFloor;
        tileArray[3][xLoc][yLoc] = null;
    }


    public static void createNewSceneCluster(int z, int lowestX, int lowestZ, int highestX, int highestY, int highestZ, int lowestY, int searchMask) {
        SceneCluster sceneCluster = new SceneCluster();
        sceneCluster.startXLoc = lowestX / 128;
        sceneCluster.endXLoc = highestX / 128;
        sceneCluster.startYLoc = lowestY / 128;
        sceneCluster.endYLoc = highestY / 128;
        sceneCluster.orientation = searchMask;
        sceneCluster.startXPos = lowestX;
        sceneCluster.endXPos = highestX;
        sceneCluster.startYPos = lowestY;
        sceneCluster.endYPos = highestY;
        sceneCluster.startZPos = highestZ;
        sceneCluster.endZPos = lowestZ;
        sceneClusters[z][sceneClusterCounts[z]++] = sceneCluster;
    }

    public void setTileLogicHeight(int zLoc, int xLoc, int yLoc, int logicHeight) {
        Tile tile = tileArray[zLoc][xLoc][yLoc];
        if (tile != null)
            tileArray[zLoc][xLoc][yLoc].logicHeight = logicHeight;
    }
    public void addTile(int zLoc, int xLoc, int yLoc, int shape, int i1, int j1, int k1, int l1, int i2, int j2, int k2, int l2, int i3, int j3, int k3, int l3, int i4, int j4, int k4, int l4) {
        if (shape == 0) {
            SimpleTile simpleTile = new SimpleTile(k2, l2, i3, j3, -1, k4, false);
            for (int lowerZLoc = zLoc; lowerZLoc >= 0; lowerZLoc--)
                if (tileArray[lowerZLoc][xLoc][yLoc] == null)
                    tileArray[lowerZLoc][xLoc][yLoc] = new Tile(lowerZLoc, xLoc, yLoc);

            tileArray[zLoc][xLoc][yLoc].mySimpleTile = simpleTile;
        } else if (shape == 1) {
            SimpleTile simpleTile = new SimpleTile(k3, l3, i4, j4, j1, l4, k1 == l1 && k1 == i2 && k1 == j2);
            for (int lowerZLoc = zLoc; lowerZLoc >= 0; lowerZLoc--)
                if (tileArray[lowerZLoc][xLoc][yLoc] == null)
                    tileArray[lowerZLoc][xLoc][yLoc] = new Tile(lowerZLoc, xLoc, yLoc);

            tileArray[zLoc][xLoc][yLoc].mySimpleTile = simpleTile;
        } else {
            ShapedTile shapedTile = new ShapedTile(yLoc, k3, j3, i2, j1, i4, i1, k2, k4, i3, j2, l1, k1, shape, j4, l3, l2, xLoc, l4);
            for (int k5 = zLoc; k5 >= 0; k5--)
                if (tileArray[k5][xLoc][yLoc] == null)
                    tileArray[k5][xLoc][yLoc] = new Tile(k5, xLoc, yLoc);

            tileArray[zLoc][xLoc][yLoc].myShapedTile = shapedTile;
        }
    }

    public void addGroundDecoration(int i, int j, int k, Renderable class30_sub2_sub4,
                                    byte byte0, int i1, int j1, int var) {
        if (class30_sub2_sub4 == null)
            return;
        GroundObject class49 = new GroundObject();
        class49.renderable = class30_sub2_sub4;
        class49.anInt812 = j1 * 128 + 64;
        class49.anInt813 = k * 128 + 64;
        class49.anInt811 = j;
        class49.uid = i1;
        class49.aByte816 = byte0;
        class49.setNewUID(var);
        if (tileArray[i][j1][k] == null)
            tileArray[i][j1][k] = new Tile(i, j1, k);
        tileArray[i][j1][k].groundDecoration = class49;
    }

    public void addGroundItemTile(int i, int j, Renderable class30_sub2_sub4, int k,
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
        Tile class30_sub3 = tileArray[l][i][i1];
        if (class30_sub3 != null) {
            for (int k1 = 0; k1 < class30_sub3.gameObjectIndex; k1++)
                if (class30_sub3.gameObjects[k1].renderable instanceof Model) {
                    int l1 = ((Model) class30_sub3.gameObjects[k1].renderable).itemDropHeight;
                    if (l1 > j1)
                        j1 = l1;
                }

        }
        object4.anInt52 = j1;
        if (tileArray[l][i][i1] == null)
            tileArray[l][i][i1] = new Tile(l, i, i1);
        tileArray[l][i][i1].obj4 = object4;
    }

    public void addWallObject(int i, Renderable class30_sub2_sub4, int j, int k,
                              byte byte0, int l, Renderable class30_sub2_sub4_1, int i1, int j1,
                              int k1, int var) {
        if (class30_sub2_sub4 == null && class30_sub2_sub4_1 == null)
            return;
        WallObject object1 = new WallObject();
        object1.uid = j;
        object1.aByte281 = byte0;
        object1.anInt274 = l * 128 + 64;
        object1.anInt275 = k * 128 + 64;
        object1.anInt273 = i1;
        object1.renderable1 = class30_sub2_sub4;
        object1.renderable2 = class30_sub2_sub4_1;
        object1.orientation = i;
        object1.orientation1 = j1;
        object1.setNewUID(var);
        for (int l1 = k1; l1 >= 0; l1--)
            if (tileArray[l1][l][k] == null)
                tileArray[l1][l][k] = new Tile(l1, l, k);

        tileArray[k1][l][k].wallObject = object1;
    }

    public void addWallDecoration(int i, int j, int k, int i1, int j1, int k1,
                                  Renderable class30_sub2_sub4, int l1, byte byte0, int i2, int j2, int var) {
        if (class30_sub2_sub4 == null)
            return;
        WallDecoration class26 = new WallDecoration();
        class26.uid = i;
        class26.aByte506 = byte0;
        class26.anInt500 = l1 * 128 + 64 + j1;
        class26.anInt501 = j * 128 + 64 + i2;
        class26.anInt499 = k1;
        class26.aClass30_Sub2_Sub4_504 = class30_sub2_sub4;
        class26.anInt502 = j2;
        class26.anInt503 = k;
        class26.setNewUID(var);
        for (int k2 = i1; k2 >= 0; k2--)
            if (tileArray[k2][l1][j] == null)
                tileArray[k2][l1][j] = new Tile(k2, l1, j);

        tileArray[i1][l1][j].obj2 = class26;
    }

    public boolean addTiledObject(int i, byte byte0, int j, int k,
                                  Renderable class30_sub2_sub4, int l, int i1, int j1, int k1, int l1, int var11) {
        if (class30_sub2_sub4 == null) {
            return true;
        } else {
            int i2 = l1 * 128 + 64 * l;
            int j2 = k1 * 128 + 64 * k;
            return addAnimableC(i1, l1, k1, l, k, i2, j2, j, class30_sub2_sub4,
                    j1, false, i, byte0, var11);
        }
    }

    public boolean addAnimableA(int i, int j, int k, int l, int i1, int j1,
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
        return addAnimableC(i, l1, i2, (j2 - l1) + 1, (k2 - i2) + 1, k1, i1, k,
                class30_sub2_sub4, j, true, l, (byte) 0, 0);
    }

    public boolean addToScenePlayerAsObject(int j, int k, Renderable class30_sub2_sub4, int l,
                                            int i1, int j1, int k1, int l1, int i2, int j2, int k2) {
        return class30_sub2_sub4 == null
                || addAnimableC(j, l1, k2, (i2 - l1) + 1, (i1 - k2) + 1, j1, k,
                k1, class30_sub2_sub4, l, true, j2, (byte) 0, 0);
    }

    private boolean addAnimableC(int i, int j, int k, int l, int i1, int j1,
                                 int k1, int l1, Renderable class30_sub2_sub4, int i2, boolean flag,
                                 int j2, byte byte0, int var) {
        for (int k2 = j; k2 < j + l; k2++) {
            for (int l2 = k; l2 < k + i1; l2++) {
                if (k2 < 0 || l2 < 0 || k2 >= maxX || l2 >= maxZ)
                    return false;
                Tile class30_sub3 = tileArray[i][k2][l2];
                if (class30_sub3 != null && class30_sub3.gameObjectIndex >= 5)
                    return false;
            }

        }

        GameObject class28 = new GameObject();
        class28.uid = j2;
        class28.mask = byte0;
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
        class28.setNewUID(var);
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
                    if (tileArray[l3][i3][j3] == null)
                        tileArray[l3][i3][j3] = new Tile(l3, i3, j3);

                Tile class30_sub3_1 = tileArray[i][i3][j3];
                class30_sub3_1.gameObjects[class30_sub3_1.gameObjectIndex] = class28;
                class30_sub3_1.gameObjectsChanged[class30_sub3_1.gameObjectIndex] = k3;
                class30_sub3_1.totalTiledObjectMask |= k3;
                class30_sub3_1.gameObjectIndex++;
            }

        }

        if (flag)
            gameObjectsCache[interactableObjectCacheCurrPos++] = class28;
        return true;
    }

    public void clearGameObjectCache() {
        for (int i = 0; i < interactableObjectCacheCurrPos; i++) {
            GameObject object5 = gameObjectsCache[i];
            remove(object5);
            gameObjectsCache[i] = null;
        }

        interactableObjectCacheCurrPos = 0;
    }

    private void remove(GameObject gameObject) {
        for (int j = gameObject.xLocLow; j <= gameObject.xLocHigh; j++) {
            for (int k = gameObject.yLocHigh; k <= gameObject.yLocLow; k++) {
                Tile tile = tileArray[gameObject.zLoc][j][k];
                if (tile != null) {
                    for (int l = 0; l < tile.gameObjectIndex; l++) {
                        if (tile.gameObjects[l] != gameObject)
                            continue;
                        tile.gameObjectIndex--;
                        for (int i1 = l; i1 < tile.gameObjectIndex; i1++) {
                            tile.gameObjects[i1] = tile.gameObjects[i1 + 1];
                            tile.gameObjectsChanged[i1] = tile.gameObjectsChanged[i1 + 1];
                        }

                        tile.gameObjects[tile.gameObjectIndex] = null;
                        break;
                    }

                    tile.totalTiledObjectMask = 0;
                    for (int j1 = 0; j1 < tile.gameObjectIndex; j1++)
                        tile.totalTiledObjectMask |= tile.gameObjectsChanged[j1];

                }
            }

        }

    }

    public void method290(int i, int k, int l, int i1) {
        Tile tile = tileArray[i1][l][i];
        if (tile == null)
            return;
        WallDecoration wallDecoration = tile.obj2;
        if (wallDecoration != null) {
            int j1 = l * 128 + 64;
            int k1 = i * 128 + 64;
            wallDecoration.anInt500 = j1 + ((wallDecoration.anInt500 - j1) * k) / 16;
            wallDecoration.anInt501 = k1 + ((wallDecoration.anInt501 - k1) * k) / 16;
        }
    }

    public void removeWallObject(int i, int j, int k) {
        Tile tile = tileArray[j][i][k];
        if (tile != null) {
            tile.wallObject = null;
        }
    }

    public void removeWallDecoration(int j, int k, int l) {
        Tile tile = tileArray[k][l][j];
        if (tile != null) {
            tile.obj2 = null;
        }
    }

    public void removeTiledObject(int i, int k, int l) {
        Tile tile = tileArray[i][k][l];
        if (tile == null)
            return;
        for (int j1 = 0; j1 < tile.gameObjectIndex; j1++) {
            GameObject gameObject = tile.gameObjects[j1];
            if ((gameObject.uid >> 29 & 3) == 2 && gameObject.xLocLow == k
                    && gameObject.yLocHigh == l) {
                remove(gameObject);
                return;
            }
        }

    }

    public void removeGroundDecoration(int i, int j, int k) {
        Tile tile = tileArray[i][k][j];
        if (tile == null)
            return;
        tile.groundDecoration = null;
    }

    public void removeGroundItemTile(int i, int j, int k) {
        Tile class30_sub3 = tileArray[i][j][k];
        if (class30_sub3 != null) {
            class30_sub3.obj4 = null;
        }
    }

    public WallObject getWallObject(int i, int j, int k) {
        Tile tile = tileArray[i][j][k];
        if (tile == null)
            return null;
        else
            return tile.wallObject;
    }

    public WallDecoration getWallDecoration(int i, int k, int l) {
        Tile tile = tileArray[l][i][k];
        if (tile == null)
            return null;
        else
            return tile.obj2;
    }

    public GameObject getGameObject(int i, int j, int k) {
        Tile tile = tileArray[k][i][j];
        if (tile == null)
            return null;
        for (int l = 0; l < tile.gameObjectIndex; l++) {
            GameObject gameObject = tile.gameObjects[l];
            if ((gameObject.uid >> 29 & 3) == 2 && gameObject.xLocLow == i
                    && gameObject.yLocHigh == j)
                return gameObject;
        }
        return null;
    }

    public GroundObject getGroundDecoration(int i, int j, int k) {
        Tile tile = tileArray[k][j][i];
        if (tile == null || tile.groundDecoration == null)
            return null;
        else
            return tile.groundDecoration;
    }

	//fetchWallObjectUID
	public int getWallObjectUid(int i, int j, int k) {
		Tile tile = tileArray[i][j][k];
		if(tile == null || tile.wallObject == null)
			return 0;
		else
			return tile.wallObject.uid;
	}
	
	//fetchWallObjectNewUID
	public int fetchWallObjectNewUID(int i, int j, int k) {
		Tile tile = tileArray[i][j][k];
		if(tile == null || tile.wallObject == null)
			return 0;
		else
			return tile.wallObject.newuid;
	}
	
	//fetchWallDecorationUID
	public int method301(int i, int j, int l) {
		Tile tile = tileArray[i][j][l];
		if(tile == null || tile.obj2 == null)
			return 0;
		else
			return tile.obj2.uid;
	}
	
	//fetchWallDecorationNewUID
	public int fetchWallDecorationNewUID(int i, int j, int l) {
		Tile tile = tileArray[i][j][l];
		if(tile == null || tile.obj2 == null)
			return 0;
		else
			return tile.obj2.newuid;
	}

	//fetchObjectMeshUID
	public int method302(int z, int x, int y) {
		Tile tile = tileArray[z][x][y];
		if(tile == null)
			return 0;
		for(int l = 0; l < tile.gameObjectIndex; l++) {
			GameObject interactableObject = tile.gameObjects[l];
			if((interactableObject.uid >> 29 & 3) == 2 && 
					interactableObject.xLocLow == x && interactableObject.yLocHigh == y) {
				return interactableObject.uid;
			}
		}
		return 0;
	}
	
	//fetchObjectMeshNewUID
	public int fetchObjectMeshNewUID(int z, int x, int y) {
		Tile tile = tileArray[z][x][y];
		if(tile == null)
			return 0;
		for(int l = 0; l < tile.gameObjectIndex; l++) {
			GameObject interactableObject = tile.gameObjects[l];
			if((interactableObject.uid >> 29 & 3) == 2 && 
					interactableObject.xLocLow == x && interactableObject.yLocHigh == y) {
				return interactableObject.newuid;
			}
		}
		return 0;
	}
	//fetchGroundDecorationUID
	public int method303(int i, int j, int k) {
		Tile tile = tileArray[i][j][k];
		if(tile == null || tile.groundDecoration == null)
			return 0;
		else
			return tile.groundDecoration.uid;
	}
	
	//fetchGroundDecorationNewUID
	public int fetchGroundDecorationNewUID(int i, int j, int k) {
		Tile tile = tileArray[i][j][k];
		if(tile == null || tile.groundDecoration == null)
			return 0;
		else
			return tile.groundDecoration.newuid;
	}

	//fetchObjectIDTAGForXYZ
	public int method304(int z, int x, int y, int objectMesh) {//fecthObjectIDXYZ
		Tile tile = tileArray[z][x][y];
		if(tile == null)
			return -1;
		if(tile.wallObject != null && tile.wallObject.uid == objectMesh)//obj1 = wall
			return tile.wallObject.aByte281 & 255;
		if(tile.obj2 != null && tile.obj2.uid == objectMesh)//obj2 = wallDec
			return tile.obj2.aByte506 & 255;
		if(tile.groundDecoration != null && tile.groundDecoration.uid == objectMesh)//obj3 = groundDec
			return tile.groundDecoration.aByte816 & 255;
			for(int i1 = 0; i1 < tile.gameObjectIndex; i1++)//anInt1317 = count?
				if(tile.gameObjects[i1].uid == objectMesh)
					return tile.gameObjects[i1].mask & 255;

			return -1;
	}

    public void shadeModels(int lightY, int lightX, int lightZ) {
        int intensity = 64;// was parameter
        int diffusion = 768;// was parameter
        int lightDistance = (int) Math.sqrt(lightX * lightX + lightY * lightY + lightZ * lightZ);
        int someLightQualityVariable = diffusion * lightDistance >> 8;
        for (int zLoc = 0; zLoc < numberOfZ; zLoc++) {
            for (int xLoc = 0; xLoc < maxX; xLoc++) {
                for (int yLoc = 0; yLoc < maxZ; yLoc++) {
                    Tile tile = tileArray[zLoc][xLoc][yLoc];
                    if (tile != null) {
                        WallObject wallObject = tile.wallObject;
                        if (wallObject != null && wallObject.renderable1 != null
                                && wallObject.renderable1.aClass33Array1425 != null) {
                            method307(zLoc, 1, 1, xLoc, yLoc, (Model) wallObject.renderable1);
                            if (wallObject.renderable2 != null && wallObject.renderable2.aClass33Array1425 != null) {
                                method307(zLoc, 1, 1, xLoc, yLoc, (Model) wallObject.renderable2);
                                mergeNormals((Model) wallObject.renderable1, (Model) wallObject.renderable2, 0, 0,
                                        0, false);
                                ((Model) wallObject.renderable2).method480(intensity, someLightQualityVariable,
                                        lightX, lightY, lightZ);
                            }
                            ((Model) wallObject.renderable1).method480(intensity, someLightQualityVariable,
                                    lightX, lightY, lightZ);
                        }
                        for (int k2 = 0; k2 < tile.gameObjectIndex; k2++) {
                            GameObject interactableObject = tile.gameObjects[k2];
                            if (interactableObject != null && interactableObject.renderable != null
                                    && interactableObject.renderable.aClass33Array1425 != null) {
                                method307(zLoc, (interactableObject.xLocHigh - interactableObject.xLocLow) + 1,
                                        (interactableObject.yLocLow - interactableObject.yLocHigh) + 1, xLoc, yLoc,
                                        (Model) interactableObject.renderable);
                                ((Model) interactableObject.renderable).method480(intensity,
                                        someLightQualityVariable, lightX, lightY, lightZ);
                            }
                        }

                        GroundObject groundDecoration = tile.groundDecoration;
                        if (groundDecoration != null && groundDecoration.renderable.aClass33Array1425 != null) {
                            method306GroundDecorationOnly(xLoc, zLoc, (Model) groundDecoration.renderable, yLoc);
                            ((Model) groundDecoration.renderable).method480(intensity, someLightQualityVariable,
                                    lightX, lightY, lightZ);
                        }

                    }
                }
            }
        }
    }


/*    public void method306GroundDecorationOnly(int modelXLoc, int modelZLoc, Model model,
                                              int modelYLoc) { //TODO figure it out
        if (modelXLoc < maxX) {
            Tile tile = tileArray[modelZLoc][modelXLoc + 1][modelYLoc];
            if (tile != null && tile.groundDecoration != null
                    && tile.groundDecoration.renderable.vertexNormals != null) {
                mergeNormals(model, (Model) tile.groundDecoration.renderable, 128, 0, 0, true);
            }
        }
        if (modelYLoc < maxX) {
            Tile tile = tileArray[modelZLoc][modelXLoc][modelYLoc + 1];
            if (tile != null && tile.groundDecoration != null
                    && tile.groundDecoration.renderable.vertexNormals != null) {
                mergeNormals(model, (Model) tile.groundDecoration.renderable, 0, 0, 128, true);
            }
        }
        if (modelXLoc < maxX && modelYLoc < maxZ) {
            Tile tile = tileArray[modelZLoc][modelXLoc + 1][modelYLoc + 1];
            if (tile != null && tile.groundDecoration != null
                    && tile.groundDecoration.renderable.vertexNormals != null) {
                mergeNormals(model, (Model) tile.groundDecoration.renderable, 128, 0, 128, true);
            }
        }
        if (modelXLoc < maxX && modelYLoc > 0) {
            Tile tile = tileArray[modelZLoc][modelXLoc + 1][modelYLoc - 1];
            if (tile != null && tile.groundDecoration != null
                    && tile.groundDecoration.renderable.vertexNormals != null) {
                mergeNormals(model, (Model) tile.groundDecoration.renderable, 128, 0, -128, true);
            }
        }
    }


    public void method307(int modelZLoc, int modelXSize, int modelYSize, int modelXLoc,
                          int modelYLoc, Model model) {
        boolean flag = true;
        int startX = modelXLoc;
        int stopX = modelXLoc + modelXSize;
        int startY = modelYLoc - 1;
        int stopY = modelYLoc + modelYSize;
        for (int zLoc = modelZLoc; zLoc <= modelZLoc + 1; zLoc++) {
            if (zLoc != numberOfZ) {//TODO Always?
                for (int xLoc = startX; xLoc <= stopX; xLoc++) {
                    if (xLoc >= 0 && xLoc < maxX) {
                        for (int yLoc = startY; yLoc <= stopY; yLoc++) {
                            if (yLoc >= 0 && yLoc < maxZ && (!flag || xLoc >= stopX || yLoc >= stopY
                                    || yLoc < modelYLoc && xLoc != modelXLoc)) {
                                Tile tile = tileArray[zLoc][xLoc][yLoc];
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
                                    for (int i = 0; i < tile.anInt1317; i++) {
                                        GameObject gameObject = tile.gameObjects[i];
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

    }*/
private void method306GroundDecorationOnly(int modelXLoc, int modelZLoc, Model model, int modelYLoc) { //TODO figure it out
    if (modelXLoc < maxX) {
        Tile tile = tileArray[modelZLoc][modelXLoc + 1][modelYLoc];
        if (tile != null && tile.groundDecoration != null && tile.groundDecoration.renderable.aClass33Array1425 != null)
            mergeNormals(model, (Model) tile.groundDecoration.renderable, 128, 0, 0, true);
    }
    if (modelYLoc < maxX) {
        Tile tile = tileArray[modelZLoc][modelXLoc][modelYLoc + 1];
        if (tile != null && tile.groundDecoration != null && tile.groundDecoration.renderable.aClass33Array1425 != null)
            mergeNormals(model, (Model) tile.groundDecoration.renderable, 0, 0, 128, true);
    }
    if (modelXLoc < maxX && modelYLoc < maxZ) {
        Tile tile = tileArray[modelZLoc][modelXLoc + 1][modelYLoc + 1];
        if (tile != null && tile.groundDecoration != null && tile.groundDecoration.renderable.aClass33Array1425 != null)
            mergeNormals(model, (Model) tile.groundDecoration.renderable, 128, 0, 128, true);
    }
    if (modelXLoc < maxX && modelYLoc > 0) {
        Tile tile = tileArray[modelZLoc][modelXLoc + 1][modelYLoc - 1];
        if (tile != null && tile.groundDecoration != null && tile.groundDecoration.renderable.aClass33Array1425 != null)
            mergeNormals(model, (Model) tile.groundDecoration.renderable, 128, 0, -128, true);
    }
}

    private void method307(int modelZLoc, int modelXSize, int modelYSize, int modelXLoc, int modelYLoc, Model model) {
        boolean flag = true;
        int startX = modelXLoc;
        int stopX = modelXLoc + modelXSize;
        int startY = modelYLoc - 1;
        int stopY = modelYLoc + modelYSize;
        for (int zLoc = modelZLoc; zLoc <= modelZLoc + 1; zLoc++)
            if (zLoc != numberOfZ) {//TODO Always?
                for (int xLoc = startX; xLoc <= stopX; xLoc++)
                    if (xLoc >= 0 && xLoc < maxX) {
                        for (int yLoc = startY; yLoc <= stopY; yLoc++)
                            if (yLoc >= 0 && yLoc < maxZ && (!flag || xLoc >= stopX || yLoc >= stopY || yLoc < modelYLoc && xLoc != modelXLoc)) {
                                Tile tile = tileArray[zLoc][xLoc][yLoc];
                                if (tile != null) {
                                    int relativeHeightToModelTile = (heightMap[zLoc][xLoc][yLoc] + heightMap[zLoc][xLoc + 1][yLoc] + heightMap[zLoc][xLoc][yLoc + 1] + heightMap[zLoc][xLoc + 1][yLoc + 1]) / 4 - (heightMap[modelZLoc][modelXLoc][modelYLoc] + heightMap[modelZLoc][modelXLoc + 1][modelYLoc] + heightMap[modelZLoc][modelXLoc][modelYLoc + 1] + heightMap[modelZLoc][modelXLoc + 1][modelYLoc + 1]) / 4;
                                    WallObject wallObject = tile.wallObject;
                                    if (wallObject != null && wallObject.renderable1 != null && wallObject.renderable1.aClass33Array1425 != null)
                                        mergeNormals(model, (Model) wallObject.renderable1, (xLoc - modelXLoc) * 128 + (1 - modelXSize) * 64, relativeHeightToModelTile, (yLoc - modelYLoc) * 128 + (1 - modelYSize) * 64, flag);
                                    if (wallObject != null && wallObject.renderable2 != null && wallObject.renderable2.aClass33Array1425 != null)
                                        mergeNormals(model, (Model) wallObject.renderable2, (xLoc - modelXLoc) * 128 + (1 - modelXSize) * 64, relativeHeightToModelTile, (yLoc - modelYLoc) * 128 + (1 - modelYSize) * 64, flag);
                                    for (int i = 0; i < tile.gameObjectIndex; i++) {
                                        GameObject gameObject = tile.gameObjects[i];
                                        if (gameObject != null && gameObject.renderable != null && gameObject.renderable.aClass33Array1425 != null) {
                                            int tiledObjectXSize = (gameObject.xLocHigh - gameObject.xLocLow) + 1;
                                            int tiledObjectYSize = (gameObject.yLocLow - gameObject.yLocHigh) + 1;
                                            mergeNormals(model, (Model) gameObject.renderable, (gameObject.xLocLow - modelXLoc) * 128 + (tiledObjectXSize - modelXSize) * 64, relativeHeightToModelTile, (gameObject.yLocHigh - modelYLoc) * 128 + (tiledObjectYSize - modelYSize) * 64, flag);
                                        }
                                    }
                                }
                            }
                    }
                startX--; //TODO why?
                flag = false;
            }

    }


    private void mergeNormals(Model first, Model second, int dx, int dy, int dz, boolean flag) {
        anInt488++;
        int count = 0;
        int[] secondX = second.verticesX;
        int secondVertices = second.verticesCount;
        for (int vA = 0; vA < first.verticesCount; vA++) {

            if (first == null || first.aClass33Array1425[vA] == null) {
                return;
            }
            VertexNormal parentNormalA = first.aClass33Array1425[vA];
            VertexNormal normalA = first.vertexNormals[vA];

            if (normalA.magnitude != 0) {
                int y = first.verticesY[vA] - dy;
                if (y <= second.maximumYVertex) {

                    int x = first.verticesX[vA] - dx;
                    if (x >= second.minimumXVertex && x <= second.maximumXVertex) {

                        int z = first.verticesZ[vA] - dz;
                        if (z >= second.minimumZVertex && z <= second.maximumZVertex) {
                            for (int vB = 0; vB < secondVertices; vB++) {
                                VertexNormal parentNormalB = second.aClass33Array1425[vB];
                                VertexNormal normalB = second.vertexNormals[vB];

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

    public void drawTileMinimapSD(int pixels[], int drawIndex, int zLoc, int xLoc, int yLoc) {
        int leftOverWidth = 512;// was parameter
        Tile tile = tileArray[zLoc][xLoc][yLoc];
        if (tile == null)
            return;
        SimpleTile simpleTile = tile.mySimpleTile;
        if (simpleTile != null) {
            int tileRGB = simpleTile.colorRGB;
            if (tileRGB == 0)
                return;
            for (int i = 0; i < 4; i++) {
                pixels[drawIndex] = tileRGB;
                pixels[drawIndex + 1] = tileRGB;
                pixels[drawIndex + 2] = tileRGB;
                pixels[drawIndex + 3] = tileRGB;
                drawIndex += leftOverWidth;
            }
            return;
        }
        ShapedTile shapedTile = tile.myShapedTile;

        if (shapedTile == null) {
            return;
        }

        int shape = shapedTile.shape;
        int rotation = shapedTile.rotation;
        int underlayRGB = shapedTile.colourRGB;
        int overlayRGB = shapedTile.colourRGBA;
        int shapePoints[] = tileVertices[shape];
        int shapePointIndices[] = tileVertexIndices[rotation];
        int shapePtr = 0;
        if (underlayRGB != 0) {
            for (int i = 0; i < 4; i++) {
                pixels[drawIndex] = shapePoints[shapePointIndices[shapePtr++]] != 0 ? overlayRGB : underlayRGB;
                pixels[drawIndex + 1] = shapePoints[shapePointIndices[shapePtr++]] != 0 ? overlayRGB : underlayRGB;
                pixels[drawIndex + 2] = shapePoints[shapePointIndices[shapePtr++]] != 0 ? overlayRGB : underlayRGB;
                pixels[drawIndex + 3] = shapePoints[shapePointIndices[shapePtr++]] != 0 ? overlayRGB : underlayRGB;
                drawIndex += leftOverWidth;
            }
            return;
        }
        for (int i = 0; i < 4; i++) {
            if (shapePoints[shapePointIndices[shapePtr++]] != 0)
                pixels[drawIndex] = overlayRGB;
            if (shapePoints[shapePointIndices[shapePtr++]] != 0)
                pixels[drawIndex + 1] = overlayRGB;
            if (shapePoints[shapePointIndices[shapePtr++]] != 0)
                pixels[drawIndex + 2] = overlayRGB;
            if (shapePoints[shapePointIndices[shapePtr++]] != 0)
                pixels[drawIndex + 3] = overlayRGB;
            drawIndex += leftOverWidth;
        }
    }
    public static void buildVisibilityMap(int i, int j, int viewportWidth, int viewportHeight, int ai[]) {
        xMin = 0;
        yMin = 0;
        SceneGraph.xMax = viewportWidth;
        SceneGraph.yMax = viewportHeight;
        viewportHalfWidth = viewportWidth / 2;
        viewportHalfHeight = viewportHeight / 2;
        boolean aflag[][][][] = new boolean[9][32][53][53];
        for (int zAngle = 128; zAngle <= 384; zAngle += 32) {
            for (int xyAngle = 0; xyAngle < 2048; xyAngle += 64) {
                camUpDownY = Model.SINE[zAngle];
                camUpDownX = Model.COSINE[zAngle];
                camLeftRightY = Model.SINE[xyAngle];
                camLeftRightX = Model.COSINE[xyAngle];
                int angularZSegment = (zAngle - 128) / 32;
                int angularXYSegment = xyAngle / 64;
                for (int xRelativeToCamera = -26; xRelativeToCamera <= 26; xRelativeToCamera++) {
                    for (int yRelativeToCamera = -26; yRelativeToCamera <= 26; yRelativeToCamera++) {
                        int xRelativeToCameraPos = xRelativeToCamera * 128;
                        int yRelativeToCameraPos = yRelativeToCamera * 128;
                        boolean flag2 = false;
                        for (int k4 = -i; k4 <= j; k4 += 128) {
                            if (!method311(ai[angularZSegment] + k4, yRelativeToCameraPos, xRelativeToCameraPos))
                                continue;
                            flag2 = true;
                            break;
                        }
                        aflag[angularZSegment][angularXYSegment][xRelativeToCamera + 26][yRelativeToCamera + 26] = flag2;
                    }
                }
            }
        }

        for (int angularZSegment = 0; angularZSegment < 8; angularZSegment++) {
            for (int angularXYSegment = 0; angularXYSegment < 32; angularXYSegment++) {
                for (int xRelativeToCamera = -25; xRelativeToCamera < 25; xRelativeToCamera++) {
                    for (int yRelativeToCamera = -25; yRelativeToCamera < 25; yRelativeToCamera++) {
                        boolean flag1 = false;
                        label0:
                        for (int l3 = -1; l3 <= 1; l3++) {
                            for (int j4 = -1; j4 <= 1; j4++) {
                                if (aflag[angularZSegment][angularXYSegment][xRelativeToCamera + l3 + 26][yRelativeToCamera + j4 + 26])
                                    flag1 = true;
                                else if (aflag[angularZSegment][(angularXYSegment + 1) % 31][xRelativeToCamera + l3 + 26][yRelativeToCamera + j4 + 26])
                                    flag1 = true;
                                else if (aflag[angularZSegment + 1][angularXYSegment][xRelativeToCamera + l3 + 26][yRelativeToCamera + j4 + 26]) {
                                    flag1 = true;
                                } else {
                                    if (!aflag[angularZSegment + 1][(angularXYSegment + 1) % 31][xRelativeToCamera + l3 + 26][yRelativeToCamera + j4 + 26])
                                        continue;
                                    flag1 = true;
                                }
                                break label0;
                            }
                        }
                        visibilityMap[angularZSegment][angularXYSegment][xRelativeToCamera + 25][yRelativeToCamera + 25] = flag1;
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
        if (j1 < 50 || j1 > 3500) {
            return false;
        }
        int l1 = viewportHalfWidth + (l * Rasterizer3D.fieldOfView) / j1;
        int i2 = viewportHalfHeight + (k1 * Rasterizer3D.fieldOfView) / j1;
        return l1 >= xMin && l1 <= xMax && i2 >= yMin
                && i2 <= yMax;
    }

    public void clickTile(int i, int j) {
        clicked = true;
        clickScreenX = j;
        clickScreenY = i;
        clickedTileX = -1;
        clickedTileY = -1;
    }

    public void render(int i, int j, int k, int l, int i1, int j1) {
        if (i < 0)
            i = 0;
        else if (i >= maxX * 128)
            i = maxX * 128 - 1;
        if (j < 0)
            j = 0;
        else if (j >= maxZ * 128)
            j = maxZ * 128 - 1;
        anInt448++;
        camUpDownY = Model.SINE[j1];
        camUpDownX = Model.COSINE[j1];
        camLeftRightY = Model.SINE[k];
        camLeftRightX = Model.COSINE[k];
        renderArea = visibilityMap[(j1 - 128) / 32][k / 64];
        cameraX2 = i;
        cameraY2 = l;
        cameraZ2 = j;
        screenCenterX = i / 128;
        yCameraTile = j / 128;
        currentRenderPlane = i1;
        minTileX = screenCenterX - 25;
        if (minTileX < 0)
            minTileX = 0;
        minTileZ = yCameraTile - 25;
        if (minTileZ < 0)
            minTileZ = 0;
        maxTileX = screenCenterX + 25;
        if (maxTileX > maxX)
            maxTileX = maxX;
        maxTileZ = yCameraTile + 25;
        if (maxTileZ > maxZ)
            maxTileZ = maxZ;
        occlude();
        anInt446 = 0;
        for (int k1 = zAnInt442; k1 < numberOfZ; k1++) {
            Tile aclass30_sub3[][] = tileArray[k1];
            for (int i2 = minTileX; i2 < maxTileX; i2++) {
                for (int k2 = minTileZ; k2 < maxTileZ; k2++) {
                    Tile class30_sub3 = aclass30_sub3[i2][k2];
                    if (class30_sub3 != null)
                        if (class30_sub3.logicHeight > i1
                                || !renderArea[(i2 - screenCenterX) + 25][(k2 - yCameraTile) + 25]
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
            Tile aclass30_sub3_1[][] = tileArray[l1];
            for (int l2 = -25; l2 <= 0; l2++) {
                int i3 = screenCenterX + l2;
                int k3 = screenCenterX - l2;
                if (i3 >= minTileX || k3 < maxTileX) {
                    for (int i4 = -25; i4 <= 0; i4++) {
                        int k4 = yCameraTile + i4;
                        int i5 = yCameraTile - i4;
                        if (i3 >= minTileX) {
                            if (k4 >= minTileZ) {
                                Tile class30_sub3_1 = aclass30_sub3_1[i3][k4];
                                if (class30_sub3_1 != null
                                        && class30_sub3_1.aBoolean1322)
                                    drawTile(class30_sub3_1, true);
                            }
                            if (i5 < maxTileZ) {
                                Tile class30_sub3_2 = aclass30_sub3_1[i3][i5];
                                if (class30_sub3_2 != null
                                        && class30_sub3_2.aBoolean1322)
                                    drawTile(class30_sub3_2, true);
                            }
                        }
                        if (k3 < maxTileX) {
                            if (k4 >= minTileZ) {
                                Tile class30_sub3_3 = aclass30_sub3_1[k3][k4];
                                if (class30_sub3_3 != null
                                        && class30_sub3_3.aBoolean1322)
                                    drawTile(class30_sub3_3, true);
                            }
                            if (i5 < maxTileZ) {
                                Tile class30_sub3_4 = aclass30_sub3_1[k3][i5];
                                if (class30_sub3_4 != null
                                        && class30_sub3_4.aBoolean1322)
                                    drawTile(class30_sub3_4, true);
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
            Tile aclass30_sub3_2[][] = tileArray[j2];
            for (int j3 = -25; j3 <= 0; j3++) {
                int l3 = screenCenterX + j3;
                int j4 = screenCenterX - j3;
                if (l3 >= minTileX || j4 < maxTileX) {
                    for (int l4 = -25; l4 <= 0; l4++) {
                        int j5 = yCameraTile + l4;
                        int k5 = yCameraTile - l4;
                        if (l3 >= minTileX) {
                            if (j5 >= minTileZ) {
                                Tile class30_sub3_5 = aclass30_sub3_2[l3][j5];
                                if (class30_sub3_5 != null
                                        && class30_sub3_5.aBoolean1322)
                                    drawTile(class30_sub3_5, false);
                            }
                            if (k5 < maxTileZ) {
                                Tile class30_sub3_6 = aclass30_sub3_2[l3][k5];
                                if (class30_sub3_6 != null
                                        && class30_sub3_6.aBoolean1322)
                                    drawTile(class30_sub3_6, false);
                            }
                        }
                        if (j4 < maxTileX) {
                            if (j5 >= minTileZ) {
                                Tile class30_sub3_7 = aclass30_sub3_2[j4][j5];
                                if (class30_sub3_7 != null
                                        && class30_sub3_7.aBoolean1322)
                                    drawTile(class30_sub3_7, false);
                            }
                            if (k5 < maxTileZ) {
                                Tile class30_sub3_8 = aclass30_sub3_2[j4][k5];
                                if (class30_sub3_8 != null
                                        && class30_sub3_8.aBoolean1322)
                                    drawTile(class30_sub3_8, false);
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

    private void drawTile(Tile tile, boolean flag) {
        tileDeque.insertHead(tile);
        do {
            Tile currentTile;
            do {
                currentTile = (Tile) tileDeque.popHead();
                if (currentTile == null)
                    return;
            } while (!currentTile.aBoolean1323);
            int i = currentTile.anInt1308;
            int j = currentTile.anInt1309;
            int k = currentTile.z1AnInt1307;
            int l = currentTile.renderLevel;
            Tile tiles[][] = tileArray[k];
            if (currentTile.aBoolean1322) {
                if (flag) {
                    if (k > 0) {
                        Tile class30_sub3_2 = tileArray[k - 1][i][j];
                        if (class30_sub3_2 != null
                                && class30_sub3_2.aBoolean1323)
                            continue;
                    }
                    if (i <= screenCenterX && i > minTileX) {
                        Tile tile1 = tiles[i - 1][j];
                        if (tile1 != null
                                && tile1.aBoolean1323
                                && (tile1.aBoolean1322 || (currentTile.totalTiledObjectMask & 1) == 0))
                            continue;
                    }
                    if (i >= screenCenterX && i < maxTileX - 1) {
                        Tile tile1 = tiles[i + 1][j];
                        if (tile1 != null
                                && tile1.aBoolean1323
                                && (tile1.aBoolean1322 || (currentTile.totalTiledObjectMask & 4) == 0))
                            continue;
                    }
                    if (j <= yCameraTile && j > minTileZ) {
                        Tile tile1 = tiles[i][j - 1];
                        if (tile1 != null
                                && tile1.aBoolean1323
                                && (tile1.aBoolean1322 || (currentTile.totalTiledObjectMask & 8) == 0))
                            continue;
                    }
                    if (j >= yCameraTile && j < maxTileZ - 1) {
                        Tile tile1 = tiles[i][j + 1];
                        if (tile1 != null
                                && tile1.aBoolean1323
                                && (tile1.aBoolean1322 || (currentTile.totalTiledObjectMask & 2) == 0))
                            continue;
                    }
                } else {
                    flag = true;
                }
                currentTile.aBoolean1322 = false;
                if (currentTile.firstFloorTile != null) {
                    Tile class30_sub3_7 = currentTile.firstFloorTile;
                    if (class30_sub3_7.mySimpleTile != null) {
                        if (!method320(0, i, j))
                            drawTileUnderlay(class30_sub3_7.mySimpleTile, 0,
                                    camUpDownY, camUpDownX, camLeftRightY, camLeftRightX, i,
                                    j);
                    } else if (class30_sub3_7.myShapedTile != null
                            && !method320(0, i, j))
                        drawTileOverlaySD(i, camUpDownY, camLeftRightY,
                                class30_sub3_7.myShapedTile, camUpDownX, j,
                                camLeftRightX);
                    WallObject class10 = class30_sub3_7.wallObject;
                    if (class10 != null)
                        class10.renderable1.renderAtPoint(0, camUpDownY,
                                camUpDownX, camLeftRightY, camLeftRightX, class10.anInt274
                                        - cameraX2,
                                class10.anInt273 - cameraY2, class10.anInt275
                                        - cameraZ2, class10.uid, class10.getNewUID());
                    for (int i2 = 0; i2 < class30_sub3_7.gameObjectIndex; i2++) {
                        GameObject class28 = class30_sub3_7.gameObjects[i2];
                        if (class28 != null)
                            class28.renderable.renderAtPoint(
                                    class28.turnValue, camUpDownY, camUpDownX,
                                    camLeftRightY, camLeftRightX, class28.xPos
                                            - cameraX2, class28.tileHeight
                                            - cameraY2, class28.yPos
                                            - cameraZ2, class28.uid, class28.getNewUID());
                    }

                }
                boolean flag1 = false;
                if (currentTile.mySimpleTile != null) {
                    if (!method320(l, i, j)) {
                        flag1 = true;
                        drawTileUnderlay(currentTile.mySimpleTile, l, camUpDownY, camUpDownX, camLeftRightY, camLeftRightX, i, j);
                    }
                } else if (currentTile.myShapedTile != null && !method320(l, i, j)) {
                    flag1 = true;
                    drawTileOverlaySD(i, camUpDownY, camLeftRightY, currentTile.myShapedTile, camUpDownX, j, camLeftRightX);
                }
                int j1 = 0;
                int j2 = 0;
                WallObject class10_3 = currentTile.wallObject;
                WallDecoration class26_1 = currentTile.obj2;
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
                    currentTile.anInt1328 = anIntArray480[j1];
                }
                if (class10_3 != null) {
                    if ((class10_3.orientation & anIntArray479[j1]) != 0) {
                        if (class10_3.orientation == 16) {
                            currentTile.anInt1325 = 3;
                            currentTile.anInt1326 = anIntArray481[j1];
                            currentTile.anInt1327 = 3 - currentTile.anInt1326;
                        } else if (class10_3.orientation == 32) {
                            currentTile.anInt1325 = 6;
                            currentTile.anInt1326 = anIntArray482[j1];
                            currentTile.anInt1327 = 6 - currentTile.anInt1326;
                        } else if (class10_3.orientation == 64) {
                            currentTile.anInt1325 = 12;
                            currentTile.anInt1326 = anIntArray483[j1];
                            currentTile.anInt1327 = 12 - currentTile.anInt1326;
                        } else {
                            currentTile.anInt1325 = 9;
                            currentTile.anInt1326 = anIntArray484[j1];
                            currentTile.anInt1327 = 9 - currentTile.anInt1326;
                        }
                    } else {
                        currentTile.anInt1325 = 0;
                    }
                    if ((class10_3.orientation & j2) != 0
                            && !method321(l, i, j, class10_3.orientation))
                        class10_3.renderable1.renderAtPoint(0, camUpDownY,
                                camUpDownX, camLeftRightY, camLeftRightX,
                                class10_3.anInt274 - cameraX2,
                                class10_3.anInt273 - cameraY2,
                                class10_3.anInt275 - cameraZ2, class10_3.uid, class10_3.getNewUID());
                    if ((class10_3.orientation1 & j2) != 0
                            && !method321(l, i, j, class10_3.orientation1))
                        class10_3.renderable2.renderAtPoint(0, camUpDownY,
                                camUpDownX, camLeftRightY, camLeftRightX,
                                class10_3.anInt274 - cameraX2,
                                class10_3.anInt273 - cameraY2,
                                class10_3.anInt275 - cameraZ2, class10_3.uid, class10_3.getNewUID());
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
                                        - cameraZ2, class26_1.uid, class26_1.getNewUID());
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
                                    class26_1.uid, class26_1.getNewUID());
                        }
                        if ((class26_1.anInt502 & 0x200) != 0 && k10 > k9) {
                            int j11 = j4 + anIntArray465[i8];
                            int l11 = k6 + anIntArray466[i8];
                            class26_1.aClass30_Sub2_Sub4_504.renderAtPoint(
                                    i8 * 512 + 1280 & 0x7ff, camUpDownY,
                                    camUpDownX, camLeftRightY, camLeftRightX, j11, l5, l11,
                                    class26_1.uid, class26_1.getNewUID());
                        }
                    }
                if (flag1) {
                    GroundObject class49 = currentTile.groundDecoration;
                    if (class49 != null)
                        class49.renderable.renderAtPoint(0, camUpDownY,
                                camUpDownX, camLeftRightY, camLeftRightX, class49.anInt812
                                        - cameraX2,
                                class49.anInt811 - cameraY2, class49.anInt813
                                        - cameraZ2, class49.uid, class49.getNewUID());
                    Object4 object4_1 = currentTile.obj4;
                    if (object4_1 != null && object4_1.anInt52 == 0) {
                        if (object4_1.aClass30_Sub2_Sub4_49 != null)
                            object4_1.aClass30_Sub2_Sub4_49
                                    .renderAtPoint(0, camUpDownY, camUpDownX, camLeftRightY,
                                            camLeftRightX, object4_1.anInt46
                                                    - cameraX2,
                                            object4_1.anInt45 - cameraY2,
                                            object4_1.anInt47 - cameraZ2,
                                            object4_1.uid, object4_1.getNewUID());
                        if (object4_1.aClass30_Sub2_Sub4_50 != null)
                            object4_1.aClass30_Sub2_Sub4_50
                                    .renderAtPoint(0, camUpDownY, camUpDownX, camLeftRightY,
                                            camLeftRightX, object4_1.anInt46
                                                    - cameraX2,
                                            object4_1.anInt45 - cameraY2,
                                            object4_1.anInt47 - cameraZ2,
                                            object4_1.uid, object4_1.getNewUID());
                        if (object4_1.aClass30_Sub2_Sub4_48 != null)
                            object4_1.aClass30_Sub2_Sub4_48
                                    .renderAtPoint(0, camUpDownY, camUpDownX, camLeftRightY,
                                            camLeftRightX, object4_1.anInt46
                                                    - cameraX2,
                                            object4_1.anInt45 - cameraY2,
                                            object4_1.anInt47 - cameraZ2,
                                            object4_1.uid, object4_1.getNewUID());
                    }
                }
                int k4 = currentTile.totalTiledObjectMask;
                if (k4 != 0) {
                    if (i < screenCenterX && (k4 & 4) != 0) {
                        Tile class30_sub3_17 = tiles[i + 1][j];
                        if (class30_sub3_17 != null
                                && class30_sub3_17.aBoolean1323)
                            tileDeque.insertHead(class30_sub3_17);
                    }
                    if (j < yCameraTile && (k4 & 2) != 0) {
                        Tile class30_sub3_18 = tiles[i][j + 1];
                        if (class30_sub3_18 != null
                                && class30_sub3_18.aBoolean1323)
                            tileDeque.insertHead(class30_sub3_18);
                    }
                    if (i > screenCenterX && (k4 & 1) != 0) {
                        Tile class30_sub3_19 = tiles[i - 1][j];
                        if (class30_sub3_19 != null
                                && class30_sub3_19.aBoolean1323)
                            tileDeque.insertHead(class30_sub3_19);
                    }
                    if (j > yCameraTile && (k4 & 8) != 0) {
                        Tile class30_sub3_20 = tiles[i][j - 1];
                        if (class30_sub3_20 != null
                                && class30_sub3_20.aBoolean1323)
                            tileDeque.insertHead(class30_sub3_20);
                    }
                }
            }
            if (currentTile.anInt1325 != 0) {
                boolean flag2 = true;
                for (int k1 = 0; k1 < currentTile.gameObjectIndex; k1++) {
                    if (currentTile.gameObjects[k1].anInt528 == anInt448
                            || (currentTile.gameObjectsChanged[k1] & currentTile.anInt1325) != currentTile.anInt1326)
                        continue;
                    flag2 = false;
                    break;
                }

                if (flag2) {
                    WallObject class10_1 = currentTile.wallObject;
                    if (!method321(l, i, j, class10_1.orientation))
                        class10_1.renderable1.renderAtPoint(0, camUpDownY,
                                camUpDownX, camLeftRightY, camLeftRightX,
                                class10_1.anInt274 - cameraX2,
                                class10_1.anInt273 - cameraY2,
                                class10_1.anInt275 - cameraZ2, class10_1.uid, class10_1.getNewUID());
                    currentTile.anInt1325 = 0;
                }
            }
            if (currentTile.aBoolean1324)
                try {
                    int i1 = currentTile.gameObjectIndex;
                    currentTile.aBoolean1324 = false;
                    int l1 = 0;
                    label0:
                    for (int k2 = 0; k2 < i1; k2++) {
                        GameObject class28_1 = currentTile.gameObjects[k2];
                        if (class28_1.anInt528 == anInt448)
                            continue;
                        for (int k3 = class28_1.xLocLow; k3 <= class28_1.xLocHigh; k3++) {
                            for (int l4 = class28_1.yLocHigh; l4 <= class28_1.yLocLow; l4++) {
                                Tile class30_sub3_21 = tiles[k3][l4];
                                if (class30_sub3_21.aBoolean1322) {
                                    currentTile.aBoolean1324 = true;
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
                                    if ((l6 & class30_sub3_21.anInt1325) != currentTile.anInt1327)
                                        continue;
                                    currentTile.aBoolean1324 = true;
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
                                            - cameraZ2, class28_3.uid, class28_3.getNewUID());
                        for (int k7 = class28_3.xLocLow; k7 <= class28_3.xLocHigh; k7++) {
                            for (int l8 = class28_3.yLocHigh; l8 <= class28_3.yLocLow; l8++) {
                                Tile class30_sub3_22 = tiles[k7][l8];
                                if (class30_sub3_22.anInt1325 != 0)
                                    tileDeque.insertHead(class30_sub3_22);
                                else if ((k7 != i || l8 != j)
                                        && class30_sub3_22.aBoolean1323)
                                    tileDeque.insertHead(class30_sub3_22);
                            }

                        }

                    }
                    if (currentTile.aBoolean1324)
                        continue;
                } catch (Exception _ex) {
                    currentTile.aBoolean1324 = false;
                }
            if (!currentTile.aBoolean1323 || currentTile.anInt1325 != 0)
                continue;
            if (i <= screenCenterX && i > minTileX) {
                Tile class30_sub3_8 = tiles[i - 1][j];
                if (class30_sub3_8 != null && class30_sub3_8.aBoolean1323)
                    continue;
            }
            if (i >= screenCenterX && i < maxTileX - 1) {
                Tile class30_sub3_9 = tiles[i + 1][j];
                if (class30_sub3_9 != null && class30_sub3_9.aBoolean1323)
                    continue;
            }
            if (j <= yCameraTile && j > minTileZ) {
                Tile class30_sub3_10 = tiles[i][j - 1];
                if (class30_sub3_10 != null && class30_sub3_10.aBoolean1323)
                    continue;
            }
            if (j >= yCameraTile && j < maxTileZ - 1) {
                Tile class30_sub3_11 = tiles[i][j + 1];
                if (class30_sub3_11 != null && class30_sub3_11.aBoolean1323)
                    continue;
            }
            currentTile.aBoolean1323 = false;
            anInt446--;
            Object4 object4 = currentTile.obj4;
            if (object4 != null && object4.anInt52 != 0) {
                if (object4.aClass30_Sub2_Sub4_49 != null)
                    object4.aClass30_Sub2_Sub4_49.renderAtPoint(0, camUpDownY,
                            camUpDownX, camLeftRightY, camLeftRightX, object4.anInt46
                                    - cameraX2, object4.anInt45 - cameraY2
                                    - object4.anInt52, object4.anInt47
                                    - cameraZ2, object4.uid, object4.getNewUID());
                if (object4.aClass30_Sub2_Sub4_50 != null)
                    object4.aClass30_Sub2_Sub4_50.renderAtPoint(0, camUpDownY,
                            camUpDownX, camLeftRightY, camLeftRightX, object4.anInt46
                                    - cameraX2, object4.anInt45 - cameraY2
                                    - object4.anInt52, object4.anInt47
                                    - cameraZ2, object4.uid, object4.getNewUID());
                if (object4.aClass30_Sub2_Sub4_48 != null)
                    object4.aClass30_Sub2_Sub4_48.renderAtPoint(0, camUpDownY,
                            camUpDownX, camLeftRightY, camLeftRightX, object4.anInt46
                                    - cameraX2, object4.anInt45 - cameraY2
                                    - object4.anInt52, object4.anInt47
                                    - cameraZ2, object4.uid, object4.getNewUID());
            }
            if (currentTile.anInt1328 != 0) {
                WallDecoration class26 = currentTile.obj2;
                if (class26 != null
                        && !method322(l, i, j,
                        class26.aClass30_Sub2_Sub4_504.modelBaseY))
                    if ((class26.anInt502 & currentTile.anInt1328) != 0)
                        class26.aClass30_Sub2_Sub4_504.renderAtPoint(
                                class26.anInt503, camUpDownY, camUpDownX, camLeftRightY,
                                camLeftRightX, class26.anInt500 - cameraX2,
                                class26.anInt499 - cameraY2, class26.anInt501
                                        - cameraZ2, class26.uid, class26.getNewUID());
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
                                    class26.uid, class26.getNewUID());
                        }
                        if ((class26.anInt502 & 0x200) != 0 && l7 <= j6) {
                            int j9 = l2 + anIntArray465[k5];
                            int j10 = i4 + anIntArray466[k5];
                            class26.aClass30_Sub2_Sub4_504.renderAtPoint(
                                    k5 * 512 + 1280 & 0x7ff, camUpDownY,
                                    camUpDownX, camLeftRightY, camLeftRightX, j9, j3, j10,
                                    class26.uid, class26.getNewUID());
                        }
                    }
                WallObject class10_2 = currentTile.wallObject;
                if (class10_2 != null) {
                    if ((class10_2.orientation1 & currentTile.anInt1328) != 0
                            && !method321(l, i, j, class10_2.orientation1))
                        class10_2.renderable2.renderAtPoint(0, camUpDownY,
                                camUpDownX, camLeftRightY, camLeftRightX,
                                class10_2.anInt274 - cameraX2,
                                class10_2.anInt273 - cameraY2,
                                class10_2.anInt275 - cameraZ2, class10_2.uid, class10_2.getNewUID());
                    if ((class10_2.orientation & currentTile.anInt1328) != 0
                            && !method321(l, i, j, class10_2.orientation))
                        class10_2.renderable1.renderAtPoint(0, camUpDownY,
                                camUpDownX, camLeftRightY, camLeftRightX,
                                class10_2.anInt274 - cameraX2,
                                class10_2.anInt273 - cameraY2,
                                class10_2.anInt275 - cameraZ2, class10_2.uid, class10_2.getNewUID());
                }
            }
            if (k < numberOfZ - 1) {
                Tile class30_sub3_12 = tileArray[k + 1][i][j];
                if (class30_sub3_12 != null && class30_sub3_12.aBoolean1323)
                    tileDeque.insertHead(class30_sub3_12);
            }
            if (i < screenCenterX) {
                Tile class30_sub3_13 = tiles[i + 1][j];
                if (class30_sub3_13 != null && class30_sub3_13.aBoolean1323)
                    tileDeque.insertHead(class30_sub3_13);
            }
            if (j < yCameraTile) {
                Tile class30_sub3_14 = tiles[i][j + 1];
                if (class30_sub3_14 != null && class30_sub3_14.aBoolean1323)
                    tileDeque.insertHead(class30_sub3_14);
            }
            if (i > screenCenterX) {
                Tile class30_sub3_15 = tiles[i - 1][j];
                if (class30_sub3_15 != null && class30_sub3_15.aBoolean1323)
                    tileDeque.insertHead(class30_sub3_15);
            }
            if (j > yCameraTile) {
                Tile class30_sub3_16 = tiles[i][j - 1];
                if (class30_sub3_16 != null && class30_sub3_16.aBoolean1323)
                    tileDeque.insertHead(class30_sub3_16);
            }
        } while (true);
    }

    private void drawTileUnderlay(SimpleTile simpleTile, int z, int pitchSin, int pitchCos, int yawSin, int yawCos,
                                  int x, int y) {
        int l1;
        int i2 = l1 = (x << 7) - cameraX2;
        int j2;
        int k2 = j2 = (y << 7) - cameraZ2;
        int l2;
        int i3 = l2 = i2 + 128;
        int j3;
        int k3 = j3 = k2 + 128;
        int l3 = heightMap[z][x][y] - cameraY2;
        int i4 = heightMap[z][x + 1][y] - cameraY2;
        int j4 = heightMap[z][x + 1][y + 1] - cameraY2;
        int k4 = heightMap[z][x][y + 1] - cameraY2;
        int l4 = k2 * yawSin + i2 * yawCos >> 16;
        k2 = k2 * yawCos - i2 * yawSin >> 16;
        i2 = l4;
        l4 = l3 * pitchCos - k2 * pitchSin >> 16;
        k2 = l3 * pitchSin + k2 * pitchCos >> 16;
        l3 = l4;
        if (k2 < 50)
            return;
        l4 = j2 * yawSin + i3 * yawCos >> 16;
        j2 = j2 * yawCos - i3 * yawSin >> 16;
        i3 = l4;
        l4 = i4 * pitchCos - j2 * pitchSin >> 16;
        j2 = i4 * pitchSin + j2 * pitchCos >> 16;
        i4 = l4;
        if (j2 < 50)
            return;
        l4 = k3 * yawSin + l2 * yawCos >> 16;
        k3 = k3 * yawCos - l2 * yawSin >> 16;
        l2 = l4;
        l4 = j4 * pitchCos - k3 * pitchSin >> 16;
        k3 = j4 * pitchSin + k3 * pitchCos >> 16;
        j4 = l4;
        if (k3 < 50)
            return;
        l4 = j3 * yawSin + l1 * yawCos >> 16;
        j3 = j3 * yawCos - l1 * yawSin >> 16;
        l1 = l4;
        l4 = k4 * pitchCos - j3 * pitchSin >> 16;
        j3 = k4 * pitchSin + j3 * pitchCos >> 16;
        k4 = l4;
        if (j3 < 50)
            return;
        int i5 = Rasterizer3D.originViewX + i2 * Rasterizer3D.fieldOfView / k2;
        int j5 = Rasterizer3D.originViewY + l3 * Rasterizer3D.fieldOfView / k2;
        int k5 = Rasterizer3D.originViewX + i3 * Rasterizer3D.fieldOfView / j2;
        int l5 = Rasterizer3D.originViewY + i4 * Rasterizer3D.fieldOfView / j2;
        int i6 = Rasterizer3D.originViewX + l2 * Rasterizer3D.fieldOfView / k3;
        int j6 = Rasterizer3D.originViewY + j4 * Rasterizer3D.fieldOfView / k3;
        int k6 = Rasterizer3D.originViewX + l1 * Rasterizer3D.fieldOfView / j3;
        int l6 = Rasterizer3D.originViewY + k4 * Rasterizer3D.fieldOfView / j3;
        Rasterizer3D.alpha = 0;
        if ((i6 - k6) * (l5 - l6) - (j6 - l6) * (k5 - k6) > 0) {
            Rasterizer3D.textureOutOfDrawingBounds = i6 < 0 || k6 < 0 || k5 < 0
                    || i6 > Rasterizer2D.lastX || k6 > Rasterizer2D.lastX
                    || k5 > Rasterizer2D.lastX;
            if (clicked && inBounds(clickScreenX, clickScreenY, j6, l6, l5, i6, k6, k5)) {
                clickedTileX = x;
                clickedTileY = y;
            }
            if (simpleTile.texture == -1) {
                if (simpleTile.centerColor != 0xbc614e)
                    Rasterizer3D.drawShadedTriangle(j6, l6, l5, i6, k6, k5, simpleTile.centerColor, simpleTile.eastColor, simpleTile.northColor, k3, j3, j2);
            } else if (!lowMem) {
                if (simpleTile.flat)
                    Rasterizer3D.drawTexturedTriangle2(j6, l6, l5, i6, k6, k5, simpleTile.centerColor, simpleTile.eastColor, simpleTile.northColor, i2, i3, l1, l3, i4, k4, k2, j2, j3, simpleTile.texture, k3, j3, j2);
                 else
                    Rasterizer3D.drawTexturedTriangle2(j6, l6, l5, i6, k6, k5, simpleTile.centerColor, simpleTile.eastColor, simpleTile.northColor, l2, l1, i3, j4, k4, i4, k3, j3, j2, simpleTile.texture, k3, j3, j2);
            } else {
                int textureColor = TEXTURE_COLORS[simpleTile.texture];
                Rasterizer3D.drawShadedTriangle(j6, l6, l5, i6, k6, k5, light(textureColor, simpleTile.centerColor), light(textureColor, simpleTile.eastColor), light(textureColor, simpleTile.northColor), k3, j3, j2);
            }
        }
        if ((i5 - k5) * (l6 - l5) - (j5 - l5) * (k6 - k5) > 0) {
            Rasterizer3D.textureOutOfDrawingBounds = i5 < 0 || k5 < 0 || k6 < 0
                    || i5 > Rasterizer2D.lastX || k5 > Rasterizer2D.lastX
                    || k6 > Rasterizer2D.lastX;
            if (clicked
                    && inBounds(clickScreenX, clickScreenY, j5, l5, l6, i5, k5, k6)) {
                clickedTileX = x;
                clickedTileY = y;
            }
            if (simpleTile.texture == -1) {
                if (simpleTile.northEastColor != 0xbc614e)
                    Rasterizer3D.drawShadedTriangle(j5, l5, l6, i5, k5, k6, simpleTile.northEastColor, simpleTile.northColor, simpleTile.eastColor, k2, j2, j3);
            } else {
                if (!lowMem) {
                    Rasterizer3D.drawTexturedTriangle2(j5, l5, l6, i5, k5, k6, simpleTile.northEastColor, simpleTile.northColor, simpleTile.eastColor, i2, i3, l1, l3, i4, k4, k2, j2, j3, simpleTile.texture, k2, j2, j3);
                    return;
                }
                int j7 = TEXTURE_COLORS[simpleTile.texture];
                Rasterizer3D.drawShadedTriangle(j5, l5, l6, i5, k5, k6, light(j7, simpleTile.northEastColor), light(j7, simpleTile.northColor), light(j7, simpleTile.eastColor), k2, j2, j3);
            }
        }
    }

    private void drawTileOverlaySD(int tileX, int pitchSin, int yawSin, ShapedTile tile, int pitchCos,
                                   int tileY, int yawCos) {
        int k1 = tile.origVertexX.length;
        for (int l1 = 0; l1 < k1; l1++) {
            int i2 = tile.origVertexX[l1] - cameraX2;
            int k2 = tile.origVertexY[l1] - cameraY2;
            int i3 = tile.origVertexZ[l1] - cameraZ2;
            int k3 = i3 * yawSin + i2 * yawCos >> 16;
            i3 = i3 * yawCos - i2 * yawSin >> 16;
            i2 = k3;
            k3 = k2 * pitchCos - i3 * pitchSin >> 16;
            i3 = k2 * pitchSin + i3 * pitchCos >> 16;
            k2 = k3;
            if (i3 < 50)
                return;
            if (tile.triangleTexture != null) {
                ShapedTile.viewSpaceX[l1] = i2;
                ShapedTile.viewSpaceY[l1] = k2;
                ShapedTile.viewSpaceZ[l1] = i3;
            }
            ShapedTile.anIntArray688[l1] = Rasterizer3D.originViewX + i2 * Rasterizer3D.fieldOfView / i3;
            ShapedTile.anIntArray689[l1] = Rasterizer3D.originViewY + k2 * Rasterizer3D.fieldOfView / i3;
        }

        Rasterizer3D.alpha = 0;
        k1 = tile.triangleA.length;
        for (int j2 = 0; j2 < k1; j2++) {
            int l2 = tile.triangleA[j2];
            int j3 = tile.triangleB[j2];
            int l3 = tile.triangleC[j2];
            int i4 = ShapedTile.anIntArray688[l2];
            int j4 = ShapedTile.anIntArray688[j3];
            int k4 = ShapedTile.anIntArray688[l3];
            int l4 = ShapedTile.anIntArray689[l2];
            int i5 = ShapedTile.anIntArray689[j3];
            int j5 = ShapedTile.anIntArray689[l3];
            if ((i4 - j4) * (j5 - i5) - (l4 - i5) * (k4 - j4) > 0) {
                Rasterizer3D.textureOutOfDrawingBounds = i4 < 0 || j4 < 0 || k4 < 0
                        || i4 > Rasterizer2D.lastX || j4 > Rasterizer2D.lastX
                        || k4 > Rasterizer2D.lastX;
                if (clicked && inBounds(clickScreenX, clickScreenY, l4, i5, j5, i4, j4, k4)) {
                    clickedTileX = tileX;
                    clickedTileY = tileY;
                }
                if (tile.triangleTexture == null || tile.triangleTexture[j2] == -1) {
                    if (tile.triangleHslA[j2] != 0xbc614e)
                        Rasterizer3D.drawShadedTriangle(l4, i5, j5, i4, j4, k4, tile.triangleHslA[j2], tile.triangleHslB[j2], tile.triangleHslC[j2], tile.depthPoint[l2], tile.depthPoint[j3], tile.depthPoint[l3]);
                } else if (!lowMem) {
                    if (tile.flat)
                        Rasterizer3D.drawTexturedTriangle2(l4, i5, j5, i4, j4, k4, tile.triangleHslA[j2],
                                tile.triangleHslB[j2], tile.triangleHslC[j2], ShapedTile.viewSpaceX[0],
                                ShapedTile.viewSpaceX[1], ShapedTile.viewSpaceX[3],
                                ShapedTile.viewSpaceY[0], ShapedTile.viewSpaceY[1],
                                ShapedTile.viewSpaceY[3], ShapedTile.viewSpaceZ[0],
                                ShapedTile.viewSpaceZ[1], ShapedTile.viewSpaceZ[3], tile.triangleTexture[j2],
                                tile.viewSpaceZ[l2],
                                tile.viewSpaceZ[j3],
                                tile.viewSpaceZ[l3]);
                     else
                        Rasterizer3D.drawTexturedTriangle2(l4, i5, j5, i4, j4, k4, tile.triangleHslA[j2],
                                tile.triangleHslB[j2], tile.triangleHslC[j2], ShapedTile.viewSpaceX[l2],
                                ShapedTile.viewSpaceX[j3], ShapedTile.viewSpaceX[l3],
                                ShapedTile.viewSpaceY[l2], ShapedTile.viewSpaceY[j3],
                                ShapedTile.viewSpaceY[l3], ShapedTile.viewSpaceZ[l2],
                                ShapedTile.viewSpaceZ[j3], ShapedTile.viewSpaceZ[l3],
                                tile.triangleTexture[j2],            tile.viewSpaceZ[l2],
                                tile.viewSpaceZ[j3],
                                tile.viewSpaceZ[l3]);
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

    private int light(int j, int k) {
        k = 127 - k;
        k = (k * (j & 0x7f)) / 160;
        if (k < 2)
            k = 2;
        else if (k > 126)
            k = 126;
        return (j & 0xff80) + k;
    }

    private boolean inBounds(int i, int j, int k, int l, int i1, int j1,
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

    private void occlude() {
        int j = sceneClusterCounts[currentRenderPlane];
        SceneCluster sceneClusters[] = SceneGraph.sceneClusters[currentRenderPlane];
        anInt475 = 0;
        for (int k = 0; k < j; k++) {
            SceneCluster sceneCluster = sceneClusters[k];
            if (sceneCluster.orientation == 1) {//YZ-plane
                int l = (sceneCluster.startXLoc - screenCenterX) + 25;
                if (l < 0 || l > 50)
                    continue;
                int k1 = (sceneCluster.startYLoc - yCameraTile) + 25;
                if (k1 < 0)
                    k1 = 0;
                int j2 = (sceneCluster.endYLoc - yCameraTile) + 25;
                if (j2 > 50)
                    j2 = 50;
                boolean flag = false;
                while (k1 <= j2)
                    if (renderArea[l][k1++]) {
                        flag = true;
                        break;
                    }
                if (!flag)
                    continue;
                int j3 = cameraX2 - sceneCluster.startXPos;
                if (j3 > 32) {
                    sceneCluster.cullDirection = 1;
                } else {
                    if (j3 >= -32)
                        continue;
                    sceneCluster.cullDirection = 2;
                    j3 = -j3;
                }
                sceneCluster.anInt801 = (sceneCluster.startYPos - cameraZ2 << 8) / j3;
                sceneCluster.anInt802 = (sceneCluster.endYPos - cameraZ2 << 8) / j3;
                sceneCluster.anInt803 = (sceneCluster.startZPos - cameraY2 << 8) / j3;
                sceneCluster.anInt804 = (sceneCluster.endZPos - cameraY2 << 8) / j3;
                aClass47Array476[anInt475++] = sceneCluster;
                continue;
            }
            if (sceneCluster.orientation == 2) {
                int i1 = (sceneCluster.startYLoc - yCameraTile) + 25;
                if (i1 < 0 || i1 > 50)
                    continue;
                int l1 = (sceneCluster.startXLoc - screenCenterX) + 25;
                if (l1 < 0)
                    l1 = 0;
                int k2 = (sceneCluster.endXLoc - screenCenterX) + 25;
                if (k2 > 50)
                    k2 = 50;
                boolean flag1 = false;
                while (l1 <= k2)
                    if (renderArea[l1++][i1]) {
                        flag1 = true;
                        break;
                    }
                if (!flag1)
                    continue;
                int k3 = cameraZ2 - sceneCluster.startYPos;
                if (k3 > 32) {
                    sceneCluster.cullDirection = 3;
                } else {
                    if (k3 >= -32)
                        continue;
                    sceneCluster.cullDirection = 4;
                    k3 = -k3;
                }
                sceneCluster.anInt799 = (sceneCluster.startXPos - cameraX2 << 8) / k3;
                sceneCluster.anInt800 = (sceneCluster.endXPos - cameraX2 << 8) / k3;
                sceneCluster.anInt803 = (sceneCluster.startZPos - cameraY2 << 8) / k3;
                sceneCluster.anInt804 = (sceneCluster.endZPos - cameraY2 << 8) / k3;
                aClass47Array476[anInt475++] = sceneCluster;
            } else if (sceneCluster.orientation == 4) {
                int j1 = sceneCluster.startZPos - cameraY2;
                if (j1 > 128) {
                    int i2 = (sceneCluster.startYLoc - yCameraTile) + 25;
                    if (i2 < 0)
                        i2 = 0;
                    int l2 = (sceneCluster.endYLoc - yCameraTile) + 25;
                    if (l2 > 50)
                        l2 = 50;
                    if (i2 <= l2) {
                        int i3 = (sceneCluster.startXLoc - screenCenterX) + 25;
                        if (i3 < 0)
                            i3 = 0;
                        int l3 = (sceneCluster.endXLoc - screenCenterX) + 25;
                        if (l3 > 50)
                            l3 = 50;
                        boolean flag2 = false;
                        label0:
                        for (int i4 = i3; i4 <= l3; i4++) {
                            for (int j4 = i2; j4 <= l2; j4++) {
                                if (!renderArea[i4][j4])
                                    continue;
                                flag2 = true;
                                break label0;
                            }

                        }

                        if (flag2) {
                            sceneCluster.cullDirection = 5;
                            sceneCluster.anInt799 = (sceneCluster.startXPos - cameraX2 << 8)
                                    / j1;
                            sceneCluster.anInt800 = (sceneCluster.endXPos - cameraX2 << 8)
                                    / j1;
                            sceneCluster.anInt801 = (sceneCluster.startYPos - cameraZ2 << 8)
                                    / j1;
                            sceneCluster.anInt802 = (sceneCluster.endYPos - cameraZ2 << 8)
                                    / j1;
                            aClass47Array476[anInt475++] = sceneCluster;
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
            SceneCluster class47 = aClass47Array476[l];
            if (class47.cullDirection == 1) {
                int i1 = class47.startXPos - i;
                if (i1 > 0) {
                    int j2 = class47.startYPos + (class47.anInt801 * i1 >> 8);
                    int k3 = class47.endYPos + (class47.anInt802 * i1 >> 8);
                    int l4 = class47.startZPos + (class47.anInt803 * i1 >> 8);
                    int i6 = class47.endZPos + (class47.anInt804 * i1 >> 8);
                    if (k >= j2 && k <= k3 && j >= l4 && j <= i6)
                        return true;
                }
            } else if (class47.cullDirection == 2) {
                int j1 = i - class47.startXPos;
                if (j1 > 0) {
                    int k2 = class47.startYPos + (class47.anInt801 * j1 >> 8);
                    int l3 = class47.endYPos + (class47.anInt802 * j1 >> 8);
                    int i5 = class47.startZPos + (class47.anInt803 * j1 >> 8);
                    int j6 = class47.endZPos + (class47.anInt804 * j1 >> 8);
                    if (k >= k2 && k <= l3 && j >= i5 && j <= j6)
                        return true;
                }
            } else if (class47.cullDirection == 3) {
                int k1 = class47.startYPos - k;
                if (k1 > 0) {
                    int l2 = class47.startXPos + (class47.anInt799 * k1 >> 8);
                    int i4 = class47.endXPos + (class47.anInt800 * k1 >> 8);
                    int j5 = class47.startZPos + (class47.anInt803 * k1 >> 8);
                    int k6 = class47.endZPos + (class47.anInt804 * k1 >> 8);
                    if (i >= l2 && i <= i4 && j >= j5 && j <= k6)
                        return true;
                }
            } else if (class47.cullDirection == 4) {
                int l1 = k - class47.startYPos;
                if (l1 > 0) {
                    int i3 = class47.startXPos + (class47.anInt799 * l1 >> 8);
                    int j4 = class47.endXPos + (class47.anInt800 * l1 >> 8);
                    int k5 = class47.startZPos + (class47.anInt803 * l1 >> 8);
                    int l6 = class47.endZPos + (class47.anInt804 * l1 >> 8);
                    if (i >= i3 && i <= j4 && j >= k5 && j <= l6)
                        return true;
                }
            } else if (class47.cullDirection == 5) {
                int i2 = j - class47.startZPos;
                if (i2 > 0) {
                    int j3 = class47.startXPos + (class47.anInt799 * i2 >> 8);
                    int k4 = class47.endXPos + (class47.anInt800 * i2 >> 8);
                    int l5 = class47.startYPos + (class47.anInt801 * i2 >> 8);
                    int i7 = class47.endYPos + (class47.anInt802 * i2 >> 8);
                    if (i >= j3 && i <= k4 && k >= l5 && k <= i7)
                        return true;
                }
            }
        }

        return false;
    }

    private boolean aBoolean434;
    public static boolean lowMem = false;
    private final int numberOfZ;
    private final int maxX;
    private final int maxZ;
    private final int[][][] heightMap;
    private final Tile[][][] tileArray;
    private int zAnInt442;
    private int interactableObjectCacheCurrPos;
    private final GameObject[] gameObjectsCache;
    private final int[][][] anIntArrayArrayArray445;
    private static int anInt446;
    private static int currentRenderPlane;
    private static int anInt448;
    private static int minTileX;
    private static int maxTileX;
    private static int minTileZ;
    private static int maxTileZ;
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
    private static SceneCluster[][] sceneClusters;
    private static int anInt475;
    private static final SceneCluster[] aClass47Array476 = new SceneCluster[500];
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
    private static boolean[][][][] visibilityMap;
    private static boolean[][] renderArea;
    private static int viewportHalfWidth;
    private static int viewportHalfHeight;
    private static int xMin;
    private static int yMin;
    private static int xMax;
    private static int yMax;
	public static int viewDistance = 9;
    static {
        visibilityMap = new boolean[8][32][(25 * 2) + 1][(25 * 2) + 1];
        cullingClusterPlaneCount = 4;
        sceneClusterCounts = new int[cullingClusterPlaneCount];
        sceneClusters = new SceneCluster[cullingClusterPlaneCount][500];
    }


}

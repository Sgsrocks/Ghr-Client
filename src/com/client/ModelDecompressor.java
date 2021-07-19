package com.client;

import java.io.DataInputStream;
import java.io.FileInputStream;

import com.client.sign.Signlink;

public class ModelDecompressor {
   public static void loadModels() {
      try {
         DataInputStream var71 = new DataInputStream(new FileInputStream(Signlink.getCacheDirectory() + "models.dat"));
         DataInputStream indexFile = new DataInputStream(new FileInputStream(Signlink.getCacheDirectory() + "models.idx"));
         int length = indexFile.readInt();

         for(int i = 0; i < length; ++i) {
            int id = indexFile.readInt();
            int invlength = indexFile.readInt();
            byte[] data = new byte[invlength];
            var71.readFully(data);
            Model.method460(data, id);
         }

         indexFile.close();
         var71.close();
      } catch (Exception var7) {
         var7.printStackTrace();
      }

   }
}
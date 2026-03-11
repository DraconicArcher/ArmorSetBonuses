package uk.co.dotcode.asb.config;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import uk.co.dotcode.asb.ModLogger;
import uk.co.dotcode.asb.ModUtils;

public class ConfigHandler {
    public static File folder;
    public static ArrayList<ArmorSet> localConfigArmorSets = new ArrayList();
    public static ArrayList<ArmorSet> serverArmorSets = new ArrayList();
    private static int totalLoadedSets = 0;
    private static int numberOfErrors = 0;

    public static void load() {
        if (!folder.exists()) {
            ModLogger.info("No configs found, creating defaults");
            folder.mkdirs();
            DefaultArmorSets.generateDefaultConfigs(folder);
        }

        File[] fileArray = folder.listFiles();
        if (fileArray != null && fileArray.length > 0) {
            ModLogger.info("Loading armor sets and bonuses");
            File[] var1 = fileArray;
            int var2 = fileArray.length;

            for(int var3 = 0; var3 < var2; ++var3) {
                File f = var1[var3];
                if (!f.isDirectory() && isJsonFile(f)) {
                    loadFile(f);
                }
            }

            ModLogger.info("Finished loading a total of " + totalLoadedSets + " armor sets and bonuses with " + numberOfErrors + " errors.");
            serverArmorSets = localConfigArmorSets;
        }

    }

    private static void loadFile(File file) {
        try {
            FileReader reader = new FileReader(file);

            try {
                ArmorSet as = (ArmorSet)ModUtils.gson.fromJson(reader, ArmorSet.class);
                ModLogger.info("Loading armor set: " + as.armorSetName);
                if (as.isValid()) {
                    localConfigArmorSets.add(as);
                    ++totalLoadedSets;
                } else {
                    ++numberOfErrors;
                }
            } catch (Throwable var5) {
                try {
                    reader.close();
                } catch (Throwable var4) {
                    var5.addSuppressed(var4);
                }

                throw var5;
            }

            reader.close();
        } catch (FileNotFoundException var6) {
            ModLogger.error("Failed to load armor set (file not found): " + file.getPath());
            var6.printStackTrace();
            ++numberOfErrors;
        } catch (IOException var7) {
            ModLogger.error("Failed to load armor set (IOException): " + file.getPath());
            var7.printStackTrace();
            ++numberOfErrors;
        }

    }

    private static boolean isJsonFile(File f) {
        String extension = f.getPath().substring(f.getPath().lastIndexOf("."));
        return extension.equalsIgnoreCase(".json");
    }
}
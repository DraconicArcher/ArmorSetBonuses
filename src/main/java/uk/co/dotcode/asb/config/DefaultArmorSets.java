package uk.co.dotcode.asb.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import uk.co.dotcode.asb.ModLogger;

public class DefaultArmorSets {

    public static ArmorSet leather;
    public static ArmorSet iron;
    public static ArmorSet gold;
    public static ArmorSet diamond;
    public static ArmorSet netherite;

    public static void generateDefaultConfigs(File folder) {
        // Leather set
        leather = new ArmorSet(
                "leather",
                "minecraft:leather_helmet",
                "minecraft:leather_chestplate",
                "minecraft:leather_leggings",
                "minecraft:leather_boots"
        );
        leather.fullSetBonuses = new Bonus[1];
        leather.fullSetBonuses[0] = new Bonus("attribute", "minecraft:generic.armor", 3)
                .setModifierId("asb:bonus_leather_armor");
        leather.fullSetBonuses[0].description = "+3 armor";

        // Iron set
        SetPiece ironHeadSet = new SetPiece("minecraft:iron_helmet");
        ironHeadSet.mixAndMatch = new String[]{"minecraft:chainmail_helmet"};

        SetPiece ironChestSet = new SetPiece("minecraft:iron_chestplate");
        ironChestSet.mixAndMatch = new String[]{"minecraft:chainmail_chestplate"};

        SetPiece ironLegsSet = new SetPiece("minecraft:iron_leggings");
        ironLegsSet.mixAndMatch = new String[]{"minecraft:chainmail_leggings"};

        SetPiece ironBootsSet = new SetPiece("minecraft:iron_boots");
        ironBootsSet.mixAndMatch = new String[]{"minecraft:chainmail_boots"};

        iron = new ArmorSet("iron", ironHeadSet, ironChestSet, ironLegsSet, ironBootsSet);
        iron.fullSetBonuses = new Bonus[1];
        iron.fullSetBonuses[0] = new Bonus("effect", "minecraft:strength", 0);

        // Gold set
        gold = new ArmorSet(
                "gold",
                "minecraft:golden_helmet",
                "minecraft:golden_chestplate",
                "minecraft:golden_leggings",
                "minecraft:golden_boots"
        );
        gold.fullSetBonuses = new Bonus[1];
        gold.fullSetBonuses[0] = new Bonus("effect", "minecraft:haste", 1);

        // Diamond set
        diamond = new ArmorSet(
                "diamond",
                "minecraft:diamond_helmet",
                "minecraft:diamond_chestplate",
                "minecraft:diamond_leggings",
                "minecraft:diamond_boots"
        );
        diamond.fullSetBonuses = new Bonus[1];
        diamond.fullSetBonuses[0] = new Bonus("attribute", "minecraft:generic.max_health", 4)
                .setModifierId("asb:bonus_diamond_max_health");
        diamond.fullSetBonuses[0].attributeOperation = 0;
        diamond.fullSetBonuses[0].description = "+2 hearts";

        // Netherite set
        netherite = new ArmorSet(
                "netherite",
                "minecraft:netherite_helmet",
                "minecraft:netherite_chestplate",
                "minecraft:netherite_leggings",
                "minecraft:netherite_boots"
        );
        netherite.fullSetBonuses = new Bonus[1];
        netherite.fullSetBonuses[0] = new Bonus("effect", "minecraft:fire_resistance", 0);

        netherite.partialBonusRequiredAmount = 2;
        netherite.partialSetBonuses = new Bonus[1];
        netherite.partialSetBonuses[0] = new Bonus("effect", "minecraft:resistance", 0);

        // Export all armor sets to JSON
        ArrayList<ArmorSet> armorSets = new ArrayList<>();
        armorSets.add(leather);
        armorSets.add(iron);
        armorSets.add(gold);
        armorSets.add(diamond);
        armorSets.add(netherite);

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        for (ArmorSet as : armorSets) {
            File exportPath = new File(folder, as.armorSetName + ".json");
            try (FileWriter writer = new FileWriter(exportPath)) {
                gson.toJson(as, writer);
            } catch (IOException e) {
                ModLogger.warn("Failed to create file for default armor set: " + as.armorSetName + ": " + exportPath);
                e.printStackTrace();
            }
        }
    }
}
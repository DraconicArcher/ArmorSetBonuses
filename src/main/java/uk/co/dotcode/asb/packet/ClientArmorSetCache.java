package uk.co.dotcode.asb.packet;

import uk.co.dotcode.asb.config.ArmorSet;

import java.util.ArrayList;
import java.util.List;

public class ClientArmorSetCache {
    public static final List<ArmorSet> ARMOR_SETS = new ArrayList<>();

    public static void reset() {
        ARMOR_SETS.clear();
    }

    public static void add(ArmorSet set) {
        ARMOR_SETS.add(set);
    }
}
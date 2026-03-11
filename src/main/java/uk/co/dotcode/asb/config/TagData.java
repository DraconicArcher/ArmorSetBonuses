package uk.co.dotcode.asb.config;

import net.minecraft.world.item.ItemStack;

public class TagData {
    public boolean orMode = false;
    public String[] tags;

    public boolean hasTag(ItemStack stack) {
        return !this.orMode;
    }
}
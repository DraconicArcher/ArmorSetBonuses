package uk.co.dotcode.asb.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import uk.co.dotcode.asb.ComponentManager;
import uk.co.dotcode.asb.ModLogger;
import uk.co.dotcode.asb.ModUtils;

public class SetPiece {
    public String itemKey;
    public String[] mixAndMatch;
    public boolean mustBeEmpty;
    public TagData tagData;
    public EnchantmentCondition[] enchantments;
    public transient Integer toolTipTickStage;

    public SetPiece() {
        this.mustBeEmpty = false;
        this.toolTipTickStage = 0;
    }

    public SetPiece(String itemKey) {
        this();
        this.itemKey = itemKey;
    }

    public boolean verify(String armorSetName, String slotType) {
        boolean valid = true;

        if (!this.mustBeEmpty) {

            if (this.itemKey == null || this.itemKey.isEmpty() || !ModUtils.checkItemKey(this.itemKey)) {
                ModLogger.warn("Failed to add custom armorset: " + armorSetName +
                        ". The " + slotType + " slot is invalid: " + this.itemKey);
                valid = false;
            }

            if (this.mixAndMatch != null) {
                for (String s : this.mixAndMatch) {

                    if (s == null || s.isEmpty() || !ModUtils.checkItemKey(s)) {
                        ModLogger.warn("Failed to add custom armorset: " + armorSetName +
                                ". " + slotType + " MixAndMatch is invalid: " + s);
                        valid = false;
                    }
                }
            }

            if (this.enchantments != null) {
                for (EnchantmentCondition e : this.enchantments) {
                    if (!e.verify(armorSetName, slotType)) {
                        valid = false;
                    }
                }
            }
        }

        return valid;
    }

    public MutableComponent toolTipText(Player player, int armorSlot, boolean nextStage) {
        if (this.mustBeEmpty) {
            MutableComponent tooltip = ComponentManager.emptyError;
            boolean matchesRequirement = false;
            if (armorSlot == -1) {
                matchesRequirement = player.getMainHandItem().isEmpty();
                tooltip = ComponentManager.slotEmptyMainHand;
            } else if (armorSlot == -2) {
                matchesRequirement = player.getOffhandItem().isEmpty();
                tooltip = ComponentManager.slotEmptyOffhand;
            } else if (armorSlot == 0) {
                matchesRequirement = player.getInventory().getArmor(armorSlot).isEmpty();
                tooltip = ComponentManager.slotEmptyFeet;
            } else if (armorSlot == 1) {
                matchesRequirement = player.getInventory().getArmor(armorSlot).isEmpty();
                tooltip = ComponentManager.slotEmptyLegs;
            } else if (armorSlot == 2) {
                matchesRequirement = player.getInventory().getArmor(armorSlot).isEmpty();
                tooltip = ComponentManager.slotEmptyChest;
            } else if (armorSlot == 3) {
                matchesRequirement = player.getInventory().getArmor(armorSlot).isEmpty();
                tooltip = ComponentManager.slotEmptyHead;
            }

            return matchesRequirement ? ComponentManager.setGreen(tooltip) : ComponentManager.setGray(tooltip);
        } else {
            ArrayList<String> targets = new ArrayList();
            targets.add(this.itemKey);
            MutableComponent extraText = null;
            if (this.mixAndMatch != null) {
                targets.addAll(Arrays.asList(this.mixAndMatch));
            }

            if (nextStage) {
                Integer var6 = this.toolTipTickStage;
                this.toolTipTickStage = this.toolTipTickStage + 1;
                if (this.toolTipTickStage >= targets.size()) {
                    this.toolTipTickStage = 0;
                }
            }

            String s;
            ItemStack active;
            Iterator var10;
            if (armorSlot == -1) {
                extraText = ComponentManager.mergeComponents(new Component[]{ComponentManager.space, ComponentManager.bracketOpen, ComponentManager.slotMainHand, ComponentManager.bracketClose});
                var10 = targets.iterator();

                while(var10.hasNext()) {
                    s = (String)var10.next();
                    if (ModUtils.getRegistryNameItem(player.getMainHandItem().getItem()).toString().equals(s)) {
                        active = new ItemStack(ModUtils.getItem(s));
                        return ComponentManager.setGreen(ComponentManager.mergeComponents(new Component[]{ComponentManager.dash, ComponentManager.createComponent(active.getDescriptionId(), true), extraText}));
                    }
                }
            } else if (armorSlot == -2) {
                extraText = ComponentManager.mergeComponents(new Component[]{ComponentManager.space, ComponentManager.bracketOpen, ComponentManager.slotOffhand, ComponentManager.bracketClose});
                var10 = targets.iterator();

                while(var10.hasNext()) {
                    s = (String)var10.next();
                    if (ModUtils.getRegistryNameItem(player.getOffhandItem().getItem()).toString().equals(s)) {
                        active = new ItemStack(ModUtils.getItem(s));
                        return ComponentManager.setGreen(ComponentManager.mergeComponents(new Component[]{ComponentManager.dash, ComponentManager.createComponent(active.getDescriptionId(), true), extraText}));
                    }
                }
            } else if (armorSlot == -3) {
                extraText = ComponentManager.mergeComponents(new Component[]{ComponentManager.space, ComponentManager.bracketOpen, ComponentManager.createComponent(AdditionalSetPiece.additionalType, false), ComponentManager.bracketClose});
                var10 = targets.iterator();

                while(var10.hasNext()) {
                    s = (String)var10.next();
                    if (AdditionalSetPiece.additionalMatchesSpecific(player, s)) {
                        active = new ItemStack(ModUtils.getItem(s));
                        return ComponentManager.setGreen(ComponentManager.mergeComponents(new Component[]{ComponentManager.dash, ComponentManager.createComponent(active.getDescriptionId(), true), extraText}));
                    }
                }
            } else {
                var10 = targets.iterator();

                while(var10.hasNext()) {
                    s = (String)var10.next();
                    if (ModUtils.getRegistryNameItem(player.getInventory().getArmor(armorSlot).getItem()).toString().equalsIgnoreCase(s)) {
                        active = new ItemStack(ModUtils.getItem(s));
                        return ComponentManager.setGreen(ComponentManager.mergeComponents(new Component[]{ComponentManager.dash, ComponentManager.createComponent(active.getDescriptionId(), true)}));
                    }
                }
            }

            String currentString = (String)targets.get(this.toolTipTickStage);
            ItemStack target = new ItemStack(ModUtils.getItem(currentString));
            return extraText != null ? ComponentManager.setGray(ComponentManager.mergeComponents(new Component[]{ComponentManager.dash, ComponentManager.createComponent(target.getDescriptionId(), true), extraText})) : ComponentManager.setGray(ComponentManager.mergeComponents(new Component[]{ComponentManager.dash, ComponentManager.createComponent(target.getDescriptionId(), true)}));
        }
    }

    public ArrayList<MutableComponent> toolTipEnchantmentText(Player player, int armorSlot) {
        ArrayList<MutableComponent> tooltips = new ArrayList();
        if (this.enchantments != null) {
            EnchantmentCondition[] var4 = this.enchantments;
            int var5 = var4.length;

            for(int var6 = 0; var6 < var5; ++var6) {
                EnchantmentCondition c = var4[var6];
                tooltips.add(c.getEnchantmentTooltipText(armorSlot, player, this.itemKey));
            }
        }

        return tooltips;
    }

    public boolean matches(ItemStack stack, String itemRegName) {

        if (this.mustBeEmpty) {
            return stack.isEmpty();
        }

        if (!stack.isEmpty() && this.enchantmentsMatch(stack) &&
                (this.tagData == null || this.tagData.hasTag(stack))) {

            if (this.itemKey.equalsIgnoreCase(itemRegName)) {
                return true;
            }

            if (this.mixAndMatch != null) {
                for (String s : this.mixAndMatch) {
                    if (s.equalsIgnoreCase(itemRegName)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public boolean matchesSimple(String itemRegName) {

        if (this.mustBeEmpty) {
            return itemRegName.equalsIgnoreCase("minecraft:air");
        }

        if (this.itemKey.equalsIgnoreCase(itemRegName)) {
            return true;
        }

        if (this.mixAndMatch != null) {
            for (String s : this.mixAndMatch) {
                if (s.equalsIgnoreCase(itemRegName)) {
                    return true;
                }
            }
        }

        return false;
    }

    public boolean enchantmentsMatch(ItemStack stack) {
        boolean flag = true;
        if (this.enchantments != null) {
            EnchantmentCondition[] var3 = this.enchantments;
            int var4 = var3.length;

            for(int var5 = 0; var5 < var4; ++var5) {
                EnchantmentCondition c = var3[var5];
                if (!c.matches(stack)) {
                    flag = false;
                    break;
                }
            }
        }

        return flag;
    }
}
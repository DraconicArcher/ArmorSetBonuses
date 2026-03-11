package uk.co.dotcode.asb.config;

import java.util.Iterator;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import uk.co.dotcode.asb.ComponentManager;
import uk.co.dotcode.asb.ModLogger;
import uk.co.dotcode.asb.ModUtils;

public class EnchantmentCondition {
    public boolean shouldHave = true;
    public String enchantmentKey;
    public Integer minLevel;
    public Integer maxLevel;
    private transient String enchantmentText = "";
    private transient String levelText = "";

    public boolean verify(String armorSetName, String slotType) {
        boolean valid = true;
        if (this.enchantmentKey == null || this.enchantmentKey.isEmpty()) {
            ModLogger.warn("Failed to add custom armorset: " + armorSetName + ". The " + slotType + " enchantmentKey is invalid: " + this.enchantmentKey);
            valid = false;
        }

        return valid;
    }

    public MutableComponent getEnchantmentTooltipText(int armorSlot, Player player, String itemKey) {
        if (this.enchantmentText.isEmpty()) {
            this.enchantmentText = Util.makeDescriptionId("enchantment", ResourceLocation.tryBuild("spacecatasb", this.enchantmentKey));
        }

        if (this.levelText.isEmpty()) {
            if (this.minLevel != null && this.maxLevel == null) {
                this.levelText = this.minLevel.toString();
            } else if (this.minLevel != null) {
                this.levelText = " " + this.minLevel + " - " + this.maxLevel;
            }
        }

        MutableComponent extraText = this.shouldHave ? ComponentManager.empty : ComponentManager.mergeComponents(new Component[]{ComponentManager.no, ComponentManager.space});
        MutableComponent tooltipText = ComponentManager.mergeComponents(new Component[]{ComponentManager.space, ComponentManager.space, ComponentManager.dash, extraText, ComponentManager.createComponent(this.enchantmentText, true), ComponentManager.createComponent(this.levelText, false)});
        if (armorSlot == -1) {
            return this.matches(player.getMainHandItem()) ? ComponentManager.setGreen(tooltipText) : ComponentManager.setGray(tooltipText);
        } else if (armorSlot == -2) {
            return this.matches(player.getOffhandItem()) ? ComponentManager.setGreen(tooltipText) : ComponentManager.setGray(tooltipText);
        } else if (armorSlot == -3) {
            ItemStack active = new ItemStack(ModUtils.getItem(itemKey));
            return AdditionalSetPiece.additionalMatchesSpecific(player, itemKey) ? ComponentManager.setGreen(ComponentManager.mergeComponents(new Component[]{ComponentManager.space, ComponentManager.dash, extraText, ComponentManager.createComponent(active.getDescriptionId(), true)})) : ComponentManager.setGray(ComponentManager.mergeComponents(new Component[]{ComponentManager.space, ComponentManager.dash, extraText, ComponentManager.createComponent(active.getDescriptionId(), true)}));
        } else {
            return this.matches(player.getInventory().getArmor(armorSlot)) ? ComponentManager.setGreen(tooltipText) : ComponentManager.setGray(tooltipText);
        }
    }

    public boolean matches(ItemStack stack) {
        boolean flag = false;
        ItemEnchantments enchantments = stack.getEnchantments();
        ResourceLocation rl = ResourceLocation.tryParse(this.enchantmentKey);
        Iterator var5 = enchantments.keySet().iterator();

        while(var5.hasNext()) {
            Holder<Enchantment> currentEnchantment = (Holder)var5.next();
            int currentLevel = enchantments.getLevel(currentEnchantment);
            if (this.shouldHave) {
                if (currentEnchantment.is(rl)) {
                    if (this.minLevel != null && this.maxLevel == null) {
                        if (currentLevel == this.minLevel) {
                            flag = true;
                            break;
                        }
                    } else {
                        if (this.minLevel != null) {
                            if (currentLevel < this.minLevel || currentLevel > this.maxLevel) {
                                continue;
                            }

                            flag = true;
                            break;
                        }

                        flag = true;
                        break;
                    }
                }
            } else {
                if (currentEnchantment.is(rl)) {
                    flag = false;
                    break;
                }

                flag = true;
            }
        }

        return flag;
    }
}
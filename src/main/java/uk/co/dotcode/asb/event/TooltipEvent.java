package uk.co.dotcode.asb.event;

import java.util.List;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import uk.co.dotcode.asb.ComponentManager;
import uk.co.dotcode.asb.ModUtils;
import uk.co.dotcode.asb.config.AdditionalSetPiece;
import uk.co.dotcode.asb.config.ArmorSet;
import uk.co.dotcode.asb.config.Bonus;
import uk.co.dotcode.asb.config.ConfigHandler;

public class TooltipEvent {
    private static int toolTipTickTimer = 0;

    public static void modifyTooltip(Player player, Item item, List<Component> list) {
        ++toolTipTickTimer;
        boolean nextStage = false;
        if (toolTipTickTimer > 100) {
            toolTipTickTimer = 0;
            nextStage = true;
        }

        boolean shouldAddShiftTooltip = false;

        for(int i = 0; i < ConfigHandler.serverArmorSets.size(); ++i) {
            ArmorSet as = (ArmorSet)ConfigHandler.serverArmorSets.get(i);
            if (as.isPartOfSet(ModUtils.getRegistryNameItem(item).toString())) {
                int var9;
                int var14;
                if (!as.hideGeneratedTooltip) {
                    shouldAddShiftTooltip = true;
                    list.add(ComponentManager.empty);
                    list.add(ComponentManager.setAqua(ComponentManager.mergeComponents(new Component[]{ComponentManager.setTitlePrefix, ComponentManager.space, ComponentManager.createComponent("tooltip.translation.spacecatasb.settitle." + as.armorSetName, true), ComponentManager.space, ComponentManager.setTitleSuffix})));
                    if (Screen.hasShiftDown() && Screen.hasControlDown()) {
                        if (as.head != null) {
                            list.add(as.head.toolTipText(player, 3, nextStage));
                            list.addAll(as.head.toolTipEnchantmentText(player, 3));
                        }

                        if (as.chest != null) {
                            list.add(as.chest.toolTipText(player, 2, nextStage));
                            list.addAll(as.chest.toolTipEnchantmentText(player, 2));
                        }

                        if (as.legs != null) {
                            list.add(as.legs.toolTipText(player, 1, nextStage));
                            list.addAll(as.legs.toolTipEnchantmentText(player, 1));
                        }

                        if (as.boots != null) {
                            list.add(as.boots.toolTipText(player, 0, nextStage));
                            list.addAll(as.boots.toolTipEnchantmentText(player, 0));
                        }

                        if (as.mainHand != null) {
                            list.add(as.mainHand.toolTipText(player, -1, nextStage));
                            list.addAll(as.mainHand.toolTipEnchantmentText(player, -1));
                        }

                        if (as.offHand != null) {
                            list.add(as.offHand.toolTipText(player, -2, nextStage));
                            list.addAll(as.offHand.toolTipEnchantmentText(player, -2));
                        }

                        if (as.additionalSetPieces != null) {
                            for(int j = 0; j < as.additionalSetPieces.length; ++j) {
                                AdditionalSetPiece c = as.additionalSetPieces[j];
                                if (!c.itemKey.isEmpty()) {
                                    list.add(c.toolTipText(player, -3, nextStage));
                                    list.addAll(c.toolTipEnchantmentText(player, -3));
                                }
                            }
                        }

                        Bonus b;
                        MutableComponent description;
                        Bonus[] var12;
                        if (as.partialSetBonuses != null) {
                            list.add(ComponentManager.empty);
                            list.add(ComponentManager.setGray(ComponentManager.mergeComponents(new Component[]{ComponentManager.partialSetBonusesPrefix, ComponentManager.space, ComponentManager.bracketOpen, ComponentManager.createComponent(Integer.toString(as.partialBonusRequiredAmount), false), ComponentManager.bracketClose, ComponentManager.partialSetBonusesSuffix})));
                            var12 = as.partialSetBonuses;
                            var14 = var12.length;

                            for(var9 = 0; var9 < var14; ++var9) {
                                b = var12[var9];
                                if (!b.hideBonusDescription) {
                                    description = ComponentManager.mergeComponents(new Component[]{ComponentManager.bracketOpen, b.getInteractionTypeComponent(), ComponentManager.bracketClose});
                                    if (b.description != null) {
                                        if (!b.description.isEmpty()) {
                                            description = ComponentManager.mergeComponents(new Component[]{description, ComponentManager.space, ComponentManager.createComponent(b.description, true)});
                                        } else {
                                            description = ComponentManager.empty;
                                        }
                                    } else {
                                        if (b.percentageChance > 0.0F && b.percentageChance < 100.0F) {
                                            description = ComponentManager.mergeComponents(new Component[]{description, ComponentManager.space, ComponentManager.createComponent(b.percentageChance + "% ", false), ComponentManager.chanceOfApplying});
                                        }

                                        if (b.type.equalsIgnoreCase("effect")) {
                                            description = ComponentManager.mergeComponents(new Component[]{description, ComponentManager.space, ((MobEffect)b.getBonusEffectInstance().getEffect().value()).getDisplayName(), ComponentManager.space, ComponentManager.createComponent(Integer.toString(b.getBonusEffectInstance().getAmplifier() + 1), false)});
                                        } else if (b.type.equalsIgnoreCase("attribute")) {
                                            description = ComponentManager.mergeComponents(new Component[]{description, ComponentManager.space, ComponentManager.createComponent(b.name, false), ComponentManager.space, ComponentManager.createComponent(Float.toString(b.value), false)});
                                        }
                                    }

                                    description = ComponentManager.setGray(ComponentManager.mergeComponents(new Component[]{description, b.getConditionsComponent()}));
                                    list.add(description);
                                }
                            }
                        }

                        list.add(ComponentManager.empty);
                        list.add(ComponentManager.setGray(ComponentManager.fullSetBonusesTitle));
                        var12 = as.fullSetBonuses;
                        var14 = var12.length;

                        for(var9 = 0; var9 < var14; ++var9) {
                            b = var12[var9];
                            if (!b.hideBonusDescription) {
                                description = ComponentManager.mergeComponents(new Component[]{ComponentManager.bracketOpen, b.getInteractionTypeComponent(), ComponentManager.bracketClose});
                                if (b.description != null) {
                                    if (!b.description.isEmpty()) {
                                        description = ComponentManager.mergeComponents(new Component[]{description, ComponentManager.space, ComponentManager.createComponent(b.description, true)});
                                    } else {
                                        description = ComponentManager.empty;
                                    }
                                } else {
                                    if (b.percentageChance > 0.0F && b.percentageChance < 100.0F) {
                                        description = ComponentManager.mergeComponents(new Component[]{description, ComponentManager.space, ComponentManager.createComponent(b.percentageChance + "% ", false), ComponentManager.chanceOfApplying});
                                    }

                                    if (b.type.equalsIgnoreCase("effect")) {
                                        description = ComponentManager.mergeComponents(new Component[]{description, ComponentManager.space, ((MobEffect)b.getBonusEffectInstance().getEffect().value()).getDisplayName(), ComponentManager.space, ComponentManager.createComponent(Integer.toString(b.getBonusEffectInstance().getAmplifier() + 1), false)});
                                    } else if (b.type.equalsIgnoreCase("attribute")) {
                                        description = ComponentManager.mergeComponents(new Component[]{description, ComponentManager.space, ComponentManager.createComponent(b.name, false), ComponentManager.space, ComponentManager.createComponent(Float.toString(b.value), false)});
                                    }
                                }

                                description = ComponentManager.setGray(ComponentManager.mergeComponents(new Component[]{description, b.getConditionsComponent()}));
                                list.add(description);
                            }
                        }

                        list.add(ComponentManager.empty);
                    }

                    if (shouldAddShiftTooltip && !Screen.hasShiftDown() || !Screen.hasControlDown()) {
                        list.add(ComponentManager.setGray(ComponentManager.holdKeysDetails));
                    }
                }

                if (as.customTooltips != null && as.customTooltips.length > 0) {
                    list.add(ComponentManager.empty);
                    String[] var13 = as.customTooltips;
                    var14 = var13.length;

                    for(var9 = 0; var9 < var14; ++var9) {
                        String tooltip = var13[var9];
                        list.add(ComponentManager.setGray(ComponentManager.createComponent(tooltip, true)));
                    }
                }
            }
        }

    }
}
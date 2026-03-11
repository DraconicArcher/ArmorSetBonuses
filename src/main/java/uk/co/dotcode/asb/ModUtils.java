package uk.co.dotcode.asb;

import com.google.gson.Gson;
import java.util.Random;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

import uk.co.dotcode.asb.config.ArmorSet;
import uk.co.dotcode.asb.config.Bonus;
import uk.co.dotcode.asb.config.ConfigHandler;
import uk.co.dotcode.asb.neoforge.ModUtilsImpl;

public class ModUtils {

    public static Random random = new Random();
    public static Gson gson = new Gson();

    /* -----------------------------
       Registry Helpers
       ----------------------------- */

    public static Item getItem(String itemKey) {
        try {
            ResourceLocation id = ResourceLocation.parse(itemKey);
            return BuiltInRegistries.ITEM.getOptional(id).orElse(Items.AIR);
        } catch (Exception e) {
            return Items.AIR;
        }
    }

    public static ResourceLocation getRegistryNameItem(Item item) {
        return BuiltInRegistries.ITEM.getKey(item);
    }

    public static ResourceLocation getRegistryNameEnchantment(Enchantment enchantment, Level level) {
        return level.registryAccess()
                .registryOrThrow(Registries.ENCHANTMENT)
                .getKey(enchantment);
    }

    public static ResourceLocation getRegistryNameEffect(MobEffect effect) {
        return ModUtilsImpl.getRegistryNameEffect(effect);
    }

    public static Block getBlock(String block) {
        return ModUtilsImpl.getBlock(block);
    }

    public static Holder<Attribute> getAttribute(String attribute) {
        return ModUtilsImpl.getAttribute(attribute);
    }

    public static Holder<MobEffect> getMobEffect(String effect) {
        return ModUtilsImpl.getMobEffect(effect);
    }

    public static ResourceLocation getRegistryNameEntity(Entity entity) {
        return ModUtilsImpl.getRegistryNameEntity(entity);
    }

    public static EntityType<?> getEntityType(String entity) {
        return ModUtilsImpl.getEntityType(entity);
    }

    public static boolean checkItemKey(String itemKey) {

        if (itemKey == null || itemKey.isEmpty()) {
            return false;
        }

        // Only verify the format is correct
        ResourceLocation id = ResourceLocation.tryParse(itemKey);

        return id != null;
    }

    /* -----------------------------
       Bonus Processing
       ----------------------------- */

    public static void checkAndApplyBonusesTo(LivingEntity source, LivingEntity target, String interactionType) {

        if (source instanceof Animal) return;

        for (ArmorSet as : ConfigHandler.serverArmorSets) {

            if (as.performanceMode && !(source instanceof Player)) continue;

            actuallyCheckAndApplyBonuses(as, source, target, interactionType);
        }
    }

    private static void actuallyCheckAndApplyBonuses(ArmorSet as, LivingEntity source, LivingEntity target, String interactionType) {

        if (as.armorSetMatch(source)) {

            for (Bonus b : as.fullSetBonuses) {
                if (b.conditionsMet(source)) {
                    b.applyBonus(target, interactionType);
                } else {
                    b.removeBonus(target, interactionType);
                }
            }

        } else {

            for (Bonus b : as.fullSetBonuses) {
                b.removeBonus(target, interactionType);
            }
        }

        if (as.partialBonusRequiredAmount != null) {

            if (as.armorSetCount(source) >= as.partialBonusRequiredAmount) {

                for (Bonus b : as.partialSetBonuses) {
                    if (b.conditionsMet(source)) {
                        b.applyBonus(target, interactionType);
                    } else {
                        b.removeBonus(target, interactionType);
                    }
                }

            } else {

                for (Bonus b : as.partialSetBonuses) {
                    b.removeBonus(target, interactionType);
                }
            }
        }
    }

    /* -----------------------------
       Immunity Handling
       ----------------------------- */

    public static void checkAndApplyImmunitiesTo(LivingEntity source, LivingEntity target) {

        if (source instanceof Animal) return;

        for (ArmorSet as : ConfigHandler.serverArmorSets) {

            if (as.performanceMode && !(source instanceof Player)) continue;

            actuallyApplyImmunities(as, source, target);
        }
    }

    public static boolean hasDamageImmunity(LivingEntity entity, DamageSource source) {

        for (ArmorSet as : ConfigHandler.serverArmorSets) {

            if (as.performanceMode && !(entity instanceof Player)) continue;

            if (actuallyCheckDamageImmunity(as, entity, source)) {
                return true;
            }
        }

        return false;
    }

    private static void actuallyApplyImmunities(ArmorSet as, LivingEntity source, LivingEntity target) {

        if (as.armorSetMatch(source)) {

            for (Bonus b : as.fullSetBonuses) {
                if (b.conditionsMet(target)) {
                    b.applyImmunity(target);
                }
            }
        }

        if (as.partialBonusRequiredAmount != null &&
                as.armorSetCount(source) >= as.partialBonusRequiredAmount) {

            for (Bonus b : as.partialSetBonuses) {
                if (b.conditionsMet(target)) {
                    b.applyImmunity(target);
                }
            }
        }
    }

    private static boolean actuallyCheckDamageImmunity(ArmorSet as, LivingEntity entity, DamageSource source) {

        if (as.armorSetMatch(entity)) {

            for (Bonus b : as.fullSetBonuses) {

                if (b.type.equalsIgnoreCase("immunity")
                        && b.conditionsMet(entity)
                        && source.typeHolder().is(ResourceLocation.parse(b.name))) {

                    return true;
                }
            }
        }

        if (as.partialBonusRequiredAmount != null &&
                as.armorSetCount(entity) >= as.partialBonusRequiredAmount) {

            for (Bonus b : as.partialSetBonuses) {

                if (b.type.equalsIgnoreCase("immunity")
                        && b.conditionsMet(entity)
                        && source.typeHolder().is(ResourceLocation.parse(b.name))) {

                    return true;
                }
            }
        }

        return false;
    }

    public static boolean isImmuneToEffect(MobEffectInstance effect, LivingEntity entity) {

        if (entity instanceof Animal) return false;

        for (ArmorSet as : ConfigHandler.serverArmorSets) {

            if (as.performanceMode && !(entity instanceof Player)) continue;

            if (actuallyCheckIsImmuneToEffect(as, effect, entity)) {
                return true;
            }
        }

        return false;
    }

    private static boolean actuallyCheckIsImmuneToEffect(ArmorSet as, MobEffectInstance effect, LivingEntity entity) {

        if (as.armorSetMatch(entity)) {

            for (Bonus b : as.fullSetBonuses) {

                if (b.type.equalsIgnoreCase("effect")
                        && b.interactionType.equalsIgnoreCase("immunity")
                        && b.conditionsMet(entity)
                        && b.getBonusEffectInstance().getEffect() == effect.getEffect()) {

                    return true;
                }
            }
        }

        if (as.partialBonusRequiredAmount != null &&
                as.armorSetCount(entity) >= as.partialBonusRequiredAmount) {

            for (Bonus b : as.partialSetBonuses) {

                if (b.type.equalsIgnoreCase("effect")
                        && b.interactionType.equalsIgnoreCase("immunity")
                        && b.conditionsMet(entity)
                        && b.getBonusEffectInstance().getEffect() == effect.getEffect()) {

                    return true;
                }
            }
        }

        return false;
    }

    /* -----------------------------
       Misc Utilities
       ----------------------------- */

    public static String getSlotLabel(int armorSlot) {
        return switch (armorSlot) {
            case -2 -> "offhand";
            case -1 -> "mainhand";
            case 0 -> "feet";
            case 1 -> "legs";
            case 2 -> "chest";
            case 3 -> "head";
            default -> "error";
        };
    }

    public static int convertMoonPhaseToInt(String phase) {
        return switch (phase.toLowerCase()) {
            case "full" -> 0;
            case "waninggibbous" -> 1;
            case "lastquarter" -> 2;
            case "waningcresent" -> 3;
            case "new" -> 4;
            case "waxingcresent" -> 5;
            case "firstquarter" -> 6;
            case "waxinggibbous" -> 7;
            default -> -1;
        };
    }
}
package uk.co.dotcode.asb;

import com.google.gson.Gson;
import java.util.Random;
import net.minecraft.core.Holder;
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


    public static Item getItem(String item) {
        return ModUtilsImpl.getItem(item);
    }

    public static ResourceLocation getRegistryNameItem(Item item) {
        return ModUtilsImpl.getRegistryNameItem(item);
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
        String[] splitLocation = itemKey.split(":");
        if (splitLocation.length != 2) {
            return false;
        } else {
            ResourceLocation resourceLocation = ResourceLocation.tryBuild(splitLocation[0], splitLocation[1]);
            Item item = getItem(resourceLocation.toString());
            return item != null && item != Items.AIR;
        }
    }

    public static void checkAndApplyBonusesTo(LivingEntity source, LivingEntity target, String interactionType) {
        if (!(source instanceof Animal)) {
            for(int i = 0; i < ConfigHandler.serverArmorSets.size(); ++i) {
                ArmorSet as = (ArmorSet)ConfigHandler.serverArmorSets.get(i);
                if (as.performanceMode) {
                    if (source instanceof Player) {
                        actuallyCheckAndApplyBonuses(as, source, target, interactionType);
                    }
                } else {
                    actuallyCheckAndApplyBonuses(as, source, target, interactionType);
                }
            }
        }

    }

    private static void actuallyCheckAndApplyBonuses(ArmorSet as, LivingEntity source, LivingEntity target, String interactionType) {
        Bonus[] var4;
        int var5;
        int var6;
        Bonus b;
        if (as.armorSetMatch(source)) {
            var4 = as.fullSetBonuses;
            var5 = var4.length;

            for(var6 = 0; var6 < var5; ++var6) {
                b = var4[var6];
                if (b.conditionsMet(source)) {
                    b.applyBonus(target, interactionType);
                } else {
                    b.removeBonus(target, interactionType);
                }
            }
        } else {
            var4 = as.fullSetBonuses;
            var5 = var4.length;

            for(var6 = 0; var6 < var5; ++var6) {
                b = var4[var6];
                b.removeBonus(target, interactionType);
            }
        }

        if (as.partialBonusRequiredAmount != null) {
            if (as.armorSetCount(source) >= as.partialBonusRequiredAmount) {
                var4 = as.partialSetBonuses;
                var5 = var4.length;

                for(var6 = 0; var6 < var5; ++var6) {
                    b = var4[var6];
                    if (b.conditionsMet(source)) {
                        b.applyBonus(target, interactionType);
                    } else {
                        b.removeBonus(target, interactionType);
                    }
                }
            } else {
                var4 = as.partialSetBonuses;
                var5 = var4.length;

                for(var6 = 0; var6 < var5; ++var6) {
                    b = var4[var6];
                    b.removeBonus(target, interactionType);
                }
            }
        }

    }

    public static void checkAndApplyImmunitiesTo(LivingEntity source, LivingEntity target) {
        if (!(source instanceof Animal)) {
            for(int i = 0; i < ConfigHandler.serverArmorSets.size(); ++i) {
                ArmorSet as = (ArmorSet)ConfigHandler.serverArmorSets.get(i);
                if (as.performanceMode) {
                    if (source instanceof Player) {
                        actuallyApplyImmunities(as, source, target);
                    }
                } else {
                    actuallyApplyImmunities(as, source, target);
                }
            }
        }

    }

    public static boolean hasDamageImmunity(LivingEntity entity, DamageSource source) {
        for(int i = 0; i < ConfigHandler.serverArmorSets.size(); ++i) {
            ArmorSet as = (ArmorSet)ConfigHandler.serverArmorSets.get(i);
            if (as.performanceMode) {
                if (entity instanceof Player && actuallyCheckDamageImmunity(as, entity, source)) {
                    return true;
                }
            } else if (actuallyCheckDamageImmunity(as, entity, source)) {
                return true;
            }
        }

        return false;
    }

    private static void actuallyApplyImmunities(ArmorSet as, LivingEntity source, LivingEntity target) {
        Bonus[] var3;
        int var4;
        int var5;
        Bonus b;
        if (as.armorSetMatch(source)) {
            var3 = as.fullSetBonuses;
            var4 = var3.length;

            for(var5 = 0; var5 < var4; ++var5) {
                b = var3[var5];
                if (b.conditionsMet(target)) {
                    b.applyImmunity(target);
                }
            }
        }

        if (as.partialBonusRequiredAmount != null && as.armorSetCount(source) >= as.partialBonusRequiredAmount) {
            var3 = as.partialSetBonuses;
            var4 = var3.length;

            for(var5 = 0; var5 < var4; ++var5) {
                b = var3[var5];
                if (b.conditionsMet(target)) {
                    b.applyImmunity(target);
                }
            }
        }

    }

    private static boolean actuallyCheckDamageImmunity(ArmorSet as, LivingEntity entity, DamageSource source) {
        Bonus[] var3;
        int var4;
        int var5;
        Bonus b;
        if (as.armorSetMatch(entity)) {
            var3 = as.fullSetBonuses;
            var4 = var3.length;

            for(var5 = 0; var5 < var4; ++var5) {
                b = var3[var5];
                if (b.type.equalsIgnoreCase("immunity") && b.conditionsMet(entity) && source.typeHolder().is(ResourceLocation.tryParse(b.name))) {
                    System.out.println("SUCCESS 1");
                    return true;
                }
            }
        }

        if (as.partialBonusRequiredAmount != null && as.armorSetCount(entity) >= as.partialBonusRequiredAmount) {
            var3 = as.partialSetBonuses;
            var4 = var3.length;

            for(var5 = 0; var5 < var4; ++var5) {
                b = var3[var5];
                if (b.type.equalsIgnoreCase("immunity") && b.conditionsMet(entity) && source.typeHolder().is(ResourceLocation.tryParse(b.name))) {
                    return true;
                }
            }
        }

        return false;
    }

    public static boolean isImmuneToEffect(MobEffectInstance effect, LivingEntity entity) {
        if (!(entity instanceof Animal)) {
            for(int i = 0; i < ConfigHandler.serverArmorSets.size(); ++i) {
                ArmorSet as = (ArmorSet)ConfigHandler.serverArmorSets.get(i);
                if (as.performanceMode) {
                    if (entity instanceof Player && actuallyCheckIsImmuneToEffect(as, effect, entity)) {
                        return true;
                    }
                } else if (actuallyCheckIsImmuneToEffect(as, effect, entity)) {
                    return true;
                }
            }
        }

        return false;
    }

    private static boolean actuallyCheckIsImmuneToEffect(ArmorSet as, MobEffectInstance effect, LivingEntity entity) {
        Bonus[] var3;
        int var4;
        int var5;
        Bonus b;
        if (as.armorSetMatch(entity)) {
            var3 = as.fullSetBonuses;
            var4 = var3.length;

            for(var5 = 0; var5 < var4; ++var5) {
                b = var3[var5];
                if (b.type.equalsIgnoreCase("effect") && b.interactionType.equalsIgnoreCase("immunity") && b.conditionsMet(entity) && b.getBonusEffectInstance().getEffect() == effect.getEffect()) {
                    return true;
                }
            }
        }

        if (as.partialBonusRequiredAmount != null && as.armorSetCount(entity) >= as.partialBonusRequiredAmount) {
            var3 = as.partialSetBonuses;
            var4 = var3.length;

            for(var5 = 0; var5 < var4; ++var5) {
                b = var3[var5];
                if (b.type.equalsIgnoreCase("effect") && b.interactionType.equalsIgnoreCase("immunity") && b.conditionsMet(entity) && b.getBonusEffectInstance().getEffect() == effect.getEffect()) {
                    return true;
                }
            }
        }

        return false;
    }

    public static String getSlotLabel(int armorSlot) {
        String var10000;
        switch(armorSlot) {
            case -2:
                var10000 = "offhand";
                break;
            case -1:
                var10000 = "mainhand";
                break;
            case 0:
                var10000 = "feet";
                break;
            case 1:
                var10000 = "legs";
                break;
            case 2:
                var10000 = "chest";
                break;
            case 3:
                var10000 = "head";
                break;
            default:
                var10000 = "error";
        }

        return var10000;
    }

    public static int convertMoonPhaseToInt(String phase) {
        String var1 = phase.toLowerCase();
        byte var2 = -1;
        switch(var1.hashCode()) {
            case -2065558086:
                if (var1.equals("waxingcresent")) {
                    var2 = 6;
                }
                break;
            case -1275913680:
                if (var1.equals("waningcresent")) {
                    var2 = 4;
                }
                break;
            case -1092392452:
                if (var1.equals("firstquarter")) {
                    var2 = 7;
                }
                break;
            case 108960:
                if (var1.equals("new")) {
                    var2 = 5;
                }
                break;
            case 3154575:
                if (var1.equals("full")) {
                    var2 = 1;
                }
                break;
            case 870914902:
                if (var1.equals("lastquarter")) {
                    var2 = 3;
                }
                break;
            case 1223527095:
                if (var1.equals("waxinggibbous")) {
                    var2 = 8;
                }
                break;
            case 2013171501:
                if (var1.equals("waninggibbous")) {
                    var2 = 2;
                }
        }

        byte var10000;
        switch(var2) {
            case 1:
                var10000 = 0;
                break;
            case 2:
                var10000 = 1;
                break;
            case 3:
                var10000 = 2;
                break;
            case 4:
                var10000 = 3;
                break;
            case 5:
                var10000 = 4;
                break;
            case 6:
                var10000 = 5;
                break;
            case 7:
                var10000 = 6;
                break;
            case 8:
                var10000 = 7;
                break;
            default:
                var10000 = -1;
        }

        return var10000;
    }
}
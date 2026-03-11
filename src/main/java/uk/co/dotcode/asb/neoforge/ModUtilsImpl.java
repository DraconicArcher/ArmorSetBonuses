package uk.co.dotcode.asb.neoforge;

import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.block.Block;

public class ModUtilsImpl {

    public static Item getItem(String item) {
        return BuiltInRegistries.ITEM.get(ResourceLocation.tryParse(item));
    }

    public static ResourceLocation getRegistryNameItem(Item item) {
        return BuiltInRegistries.ITEM.getKey(item);
    }

    public static ResourceLocation getRegistryNameEnchantment(Enchantment enchantment, RegistryAccess access) {
        return access.registryOrThrow(Registries.ENCHANTMENT).getKey(enchantment);
    }


    public static ResourceLocation getRegistryNameEffect(MobEffect effect) {
        return BuiltInRegistries.MOB_EFFECT.getKey(effect);
    }

    public static Block getBlock(String block) {
        return BuiltInRegistries.BLOCK.get(ResourceLocation.tryParse(block));
    }

    public static Holder<Attribute> getAttribute(String attribute) {
        return BuiltInRegistries.ATTRIBUTE.getHolder(ResourceLocation.tryParse(attribute)).orElse(null);
    }

    public static Holder<MobEffect> getMobEffect(String effect) {
        return BuiltInRegistries.MOB_EFFECT.getHolder(ResourceLocation.tryParse(effect)).orElse(null);
    }

    public static ResourceLocation getRegistryNameEntity(Entity entity) {
        return BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType());
    }

    public static EntityType<?> getEntityType(String entity) {
        return BuiltInRegistries.ENTITY_TYPE.get(ResourceLocation.tryParse(entity));
    }
}

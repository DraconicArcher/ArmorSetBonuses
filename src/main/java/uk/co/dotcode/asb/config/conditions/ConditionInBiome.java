package uk.co.dotcode.asb.config.conditions;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;

import uk.co.dotcode.asb.ComponentManager;
import uk.co.dotcode.asb.ModLogger;

public class ConditionInBiome extends TriggerCondition {

    private transient MutableComponent component;

    public ConditionInBiome(String biomeKey, boolean inverted) {
        super("inbiome", inverted);
        this.extra = biomeKey;
    }

    @Override
    public boolean conditionMet(LivingEntity entity) {
        Level level = entity.level();
        BlockPos pos = entity.blockPosition();

        ResourceLocation biomeId = level
                .getBiome(pos)
                .unwrapKey()
                .map(key -> key.location())
                .orElse(null);

        boolean result =
                biomeId != null && biomeId.toString().equalsIgnoreCase(extra);

        return inverted ? !result : result;
    }

    @Override
    public boolean isValid() {
        if (extra == null || extra.isEmpty() || !extra.contains(":")) {
            ModLogger.warn(
                    "Invalid bonus condition: inbiome," + extra +
                            ". The 'extra' field must be a biome registry key (namespace:path)."
            );
            return false;
        }

        return super.isValid();
    }

    @Override
    public MutableComponent translationText() {
        if (component == null) {
            if (tooltipDescription != null && !tooltipDescription.isEmpty()) {
                component = ComponentManager.createComponent(tooltipDescription, true);
            } else {
                component = inverted
                        ? ComponentManager.mergeComponents(ComponentManager.not, ComponentManager.space)
                        : ComponentManager.empty;

                component = ComponentManager.mergeComponents(
                        component,
                        ComponentManager.conditionOnBlock,
                        ComponentManager.space,
                        ComponentManager.createComponent(extra.split(":")[1], false)
                );
            }
        }
        return component;
    }
}

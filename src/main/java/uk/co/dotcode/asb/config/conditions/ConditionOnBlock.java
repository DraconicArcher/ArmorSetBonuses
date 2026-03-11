package uk.co.dotcode.asb.config.conditions;

import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.Block;
import uk.co.dotcode.asb.ComponentManager;
import uk.co.dotcode.asb.ModLogger;
import uk.co.dotcode.asb.ModUtils;

public class ConditionOnBlock extends TriggerCondition {

    private transient Block blockUnder = null;

    private transient MutableComponent component = null;

    public ConditionOnBlock(String blockKey, boolean inverted) {
        super("onblock", inverted);
        this.extra = blockKey;
        blockUnder = ModUtils.getBlock(extra.toLowerCase());
    }

    @Override
    public boolean conditionMet(LivingEntity e) {
        boolean result = e.level()
                .getBlockState(e.blockPosition().below())
                .getBlock() == blockUnder;

        return inverted ? !result : result;
    }


    @Override
    public boolean isValid() {
        if (extra == null || extra.isEmpty() || !extra.contains(":")) {
            ModLogger.warn("Invalid bonus condition: onblock," + extra
                    + ". The 'extra' field has not been defined correctly (it must be a block key for this type).");
            return false;
        }

        blockUnder = ModUtils.getBlock(extra.toLowerCase());

        if (blockUnder == null) {
            ModLogger.warn("Invalid bonus condition: onblock, " + extra + ". The specified block does not exist.");
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
                component = inverted ? ComponentManager.mergeComponents(ComponentManager.not, ComponentManager.space)
                        : ComponentManager.empty;

                component = ComponentManager.mergeComponents(
                        component,
                        ComponentManager.conditionOnBlock,
                        ComponentManager.space,
                        ComponentManager.createComponent(blockUnder.getDescriptionId(), true)
                );

            }
        }
        return component;
    }
}

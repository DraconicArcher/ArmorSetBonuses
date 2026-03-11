package uk.co.dotcode.asb.config.conditions;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.LivingEntity;
import uk.co.dotcode.asb.ComponentManager;

public class ConditionInRain extends TriggerCondition {

    private transient MutableComponent component = null;

    public ConditionInRain(boolean inverted) {
        super("inrain", inverted);
    }

    @Override
    public boolean conditionMet(LivingEntity e) {
        BlockPos pos = BlockPos.containing(
                e.getX(),
                e.getBoundingBox().maxY,
                e.getZ()
        );

        boolean inRain = e.level().isRainingAt(pos);
        return inverted != inRain;
    }


    @Override
    public boolean isValid() {
        return super.isValid();
    }

    @Override
    public MutableComponent translationText() {
        if (component == null) {
            if (tooltipDescription != null && !tooltipDescription.isEmpty()) {
                component = ComponentManager.createComponent(tooltipDescription, true);
            }
            component = inverted ? ComponentManager.mergeComponents(ComponentManager.not, ComponentManager.space)
                    : ComponentManager.empty;

            component = ComponentManager.mergeComponents(component, ComponentManager.conditionInRain);
        }

        return component;
    }
}

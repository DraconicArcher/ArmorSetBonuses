package uk.co.dotcode.asb.config.conditions;

import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.LivingEntity;
import uk.co.dotcode.asb.ComponentManager;

public class ConditionInLava extends TriggerCondition {

    private transient MutableComponent component = null;

    public ConditionInLava(boolean inverted) {
        super("inlava", inverted);
    }

    @Override
    public boolean conditionMet(LivingEntity e) {
        boolean inLava = e.isInFluidType(net.minecraft.world.level.material.Fluids.LAVA.getFluidType());
        return inverted != inLava;
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
            } else {
                component = inverted ? ComponentManager.mergeComponents(ComponentManager.not, ComponentManager.space)
                        : ComponentManager.empty;

                component = ComponentManager.mergeComponents(component, ComponentManager.conditionInLava);
            }
        }

        return component;
    }
}

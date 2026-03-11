package uk.co.dotcode.asb.config.conditions;

import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.LivingEntity;
import uk.co.dotcode.asb.ComponentManager;

public class ConditionInWater extends TriggerCondition {

    private transient MutableComponent component = null;

    public ConditionInWater(boolean inverted) {
        super("inwater", inverted);
    }

    @Override
    public boolean conditionMet(LivingEntity e) {
        boolean inWater = e.isInWater();
        return inverted != inWater;
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

                component = ComponentManager.mergeComponents(component, ComponentManager.conditionInWater);
            }
        }
        return component;
    }
}

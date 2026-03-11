package uk.co.dotcode.asb.config.conditions;

import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import uk.co.dotcode.asb.ComponentManager;
import uk.co.dotcode.asb.ModLogger;

public class ConditionInDimension extends TriggerCondition {

    private transient MutableComponent component = null;

    public ConditionInDimension(String dimensionKey, boolean inverted) {
        super("indimension", inverted);
        this.extra = dimensionKey;
    }

    @Override
    public boolean conditionMet(LivingEntity e) {
        ResourceLocation current = e.level().dimension().location();
        ResourceLocation target = ResourceLocation.parse(extra);

        boolean matches = current.equals(target);
        return inverted != matches;
    }



    @Override
    public boolean isValid() {
        if (extra == null || extra.isEmpty() || !extra.contains(":")) {
            ModLogger.warn("Invalid bonus condition: indimension," + extra
                    + ". The 'extra' field has not been defined correctly (it must be a dimension key for this type).");
            return false;
        }

        return super.isValid();
    }

    @Override
    public MutableComponent translationText() {
        if (tooltipDescription != null && !tooltipDescription.isEmpty()) {
            return ComponentManager.createComponent(tooltipDescription, true);
        } else {
            if (component == null) {
                component = inverted ? ComponentManager.mergeComponents(ComponentManager.not, ComponentManager.space)
                        : ComponentManager.empty;

                component = ComponentManager.mergeComponents(component, ComponentManager.conditionInDimension,
                        ComponentManager.space, ComponentManager.createComponent(extra.split(":")[1], false));
            }
        }
        return component;
    }
}

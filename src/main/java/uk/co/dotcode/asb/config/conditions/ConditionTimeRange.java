package uk.co.dotcode.asb.config.conditions;

import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.LivingEntity;
import uk.co.dotcode.asb.ComponentManager;
import uk.co.dotcode.asb.ModLogger;

public class ConditionTimeRange extends TriggerCondition {

    private transient int timeStart = 0;
    private transient int timeEnd = 0;

    private transient MutableComponent component = null;

    public ConditionTimeRange(String timeRange, boolean inverted) {
        super("timeRange", inverted);
        this.extra = timeRange;

        this.timeStart = Integer.parseInt(extra.split(",")[0]);
        this.timeEnd = Integer.parseInt(extra.split(",")[1]);
    }

    @Override
    public boolean conditionMet(LivingEntity e) {
        boolean result = false;
        float dayTime = e.level().getDayTime() % 24000L;

        if (timeStart <= dayTime && dayTime <= timeEnd) {
            result = true;
        }

        return inverted ? !result : result;
    }

    @Override
    public boolean isValid() {
        if (extra == null || extra.isEmpty() || !extra.contains(",")) {
            ModLogger.warn("Invalid bonus condition: timeRange," + extra
                    + ". The 'extra' field has not been defined correctly (it must be a tick range - example: '0,10000').");
            return false;
        }

        return super.isValid();
    }

    @Override
    public MutableComponent translationText() {
        if (component == null) {
            if (tooltipDescription != null && !tooltipDescription.isEmpty()) {
                return ComponentManager.createComponent(tooltipDescription, true);
            } else {
                component = inverted ? ComponentManager.mergeComponents(ComponentManager.not, ComponentManager.space)
                        : ComponentManager.empty;

                component = ComponentManager.mergeComponents(component, ComponentManager.timeRangePrefix,
                        ComponentManager.space, ComponentManager.createComponent(Integer.toString(timeStart), false),
                        ComponentManager.space, ComponentManager.dash,
                        ComponentManager.createComponent(Integer.toString(timeEnd), false));
            }
        }

        return component;

    }
}

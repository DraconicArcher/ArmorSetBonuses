package uk.co.dotcode.asb.config.conditions;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.LivingEntity;
import uk.co.dotcode.asb.ComponentManager;
import uk.co.dotcode.asb.ModLogger;

public class ConditionHealthRange extends TriggerCondition {
    private transient int lower = 0;
    private transient int upper = 0;
    private transient MutableComponent component = null;

    public ConditionHealthRange(String range, boolean inverted) {
        super("healthrange", inverted);
        this.extra = range;
        this.lower = Integer.parseInt(range.split(",")[0]);
        this.upper = Integer.parseInt(range.split(",")[1]);
    }

    public boolean conditionMet(LivingEntity e) {
        boolean result = e.getHealth() >= (float)this.lower && e.getHealth() <= (float)this.upper;
        return this.inverted ? !result : result;
    }

    public boolean isValid() {
        if (this.extra != null && !this.extra.isEmpty() && this.extra.contains(",")) {
            return super.isValid();
        } else {
            ModLogger.warn("Invalid bonus condition: healthrange," + this.extra + ". The 'extra' field has not been defined correctly (must be a range, eg. '0,10').");
            return false;
        }
    }

    public MutableComponent translationText() {
        if (this.component == null) {
            if (this.tooltipDescription != null && !this.tooltipDescription.isEmpty()) {
                this.component = ComponentManager.createComponent(this.tooltipDescription, true);
            } else {
                this.component = this.inverted ? ComponentManager.mergeComponents(new Component[]{ComponentManager.not, ComponentManager.space}) : ComponentManager.empty;
                this.component = ComponentManager.mergeComponents(new Component[]{this.component, ComponentManager.conditionHealthRange, ComponentManager.space, ComponentManager.createComponent(String.valueOf(this.lower), false), ComponentManager.space, ComponentManager.and, ComponentManager.space, ComponentManager.createComponent(String.valueOf(this.upper), false)});
            }
        }

        return this.component;
    }
}
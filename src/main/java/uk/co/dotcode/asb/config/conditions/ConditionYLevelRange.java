package uk.co.dotcode.asb.config.conditions;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.LivingEntity;
import uk.co.dotcode.asb.ComponentManager;
import uk.co.dotcode.asb.ModLogger;

public class ConditionYLevelRange extends TriggerCondition {
    private transient int lowerY = 0;
    private transient int upperY = 0;
    private transient MutableComponent component = null;

    public ConditionYLevelRange(String range, boolean inverted) {
        super("ylevelrange", inverted);
        this.extra = range;
        this.lowerY = Integer.parseInt(range.split(",")[0]);
        this.upperY = Integer.parseInt(range.split(",")[1]);
    }

    public boolean conditionMet(LivingEntity e) {
        boolean result = e.getY() >= (double)this.lowerY && e.getY() <= (double)this.upperY;
        return this.inverted ? !result : result;
    }

    public boolean isValid() {
        if (this.extra != null && !this.extra.isEmpty() && this.extra.contains(",")) {
            return super.isValid();
        } else {
            ModLogger.warn("Invalid bonus condition: ylevelrange," + this.extra + ". The 'extra' field has not been defined correctly (must be a range, eg. '-3,10').");
            return false;
        }
    }

    public MutableComponent translationText() {
        if (this.component == null) {
            if (this.tooltipDescription != null && !this.tooltipDescription.isEmpty()) {
                this.component = ComponentManager.createComponent(this.tooltipDescription, true);
            } else {
                this.component = this.inverted ? ComponentManager.mergeComponents(new Component[]{ComponentManager.not, ComponentManager.space}) : ComponentManager.empty;
                this.component = ComponentManager.mergeComponents(new Component[]{this.component, ComponentManager.conditionYRange, ComponentManager.space, ComponentManager.createComponent(String.valueOf(this.lowerY), false), ComponentManager.space, ComponentManager.and, ComponentManager.space, ComponentManager.createComponent(String.valueOf(this.upperY), false)});
            }
        }

        return this.component;
    }
}
    
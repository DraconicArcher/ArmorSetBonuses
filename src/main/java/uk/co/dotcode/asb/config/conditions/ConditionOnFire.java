package uk.co.dotcode.asb.config.conditions;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.LivingEntity;
import uk.co.dotcode.asb.ComponentManager;

public class ConditionOnFire extends TriggerCondition {
    private transient MutableComponent component = null;

    public ConditionOnFire(boolean inverted) {
        super("onfire", inverted);
    }

    public boolean conditionMet(LivingEntity e) {
        return this.inverted ? !e.isOnFire() : e.isOnFire();
    }

    public boolean isValid() {
        return super.isValid();
    }

    public MutableComponent translationText() {
        if (this.component == null) {
            if (this.tooltipDescription != null && !this.tooltipDescription.isEmpty()) {
                this.component = ComponentManager.createComponent(this.tooltipDescription, true);
            }

            this.component = this.inverted ? ComponentManager.mergeComponents(new Component[]{ComponentManager.not, ComponentManager.space}) : ComponentManager.empty;
            this.component = ComponentManager.mergeComponents(new Component[]{this.component, ComponentManager.conditionOnFire});
        }

        return this.component;
    }
}
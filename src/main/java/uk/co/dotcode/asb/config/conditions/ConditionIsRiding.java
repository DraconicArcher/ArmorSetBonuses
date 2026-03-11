package uk.co.dotcode.asb.config.conditions;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import uk.co.dotcode.asb.ComponentManager;
import uk.co.dotcode.asb.ModLogger;
import uk.co.dotcode.asb.ModUtils;

public class ConditionIsRiding extends TriggerCondition {
    private transient EntityType<?> ridableEntity = null;
    private transient MutableComponent component = null;

    public ConditionIsRiding(String entityTypeKey, boolean inverted) {
        super("isriding", inverted);
        this.extra = entityTypeKey.toLowerCase();
        this.ridableEntity = ModUtils.getEntityType(this.extra);
    }

    public boolean conditionMet(LivingEntity e) {
        boolean result = false;
        if (e.getVehicle() != null) {
            result = ModUtils.getRegistryNameEntity(e.getVehicle()).toString().equalsIgnoreCase(this.extra);
        }

        System.out.println("res " + result);
        return this.inverted ? !result : result;
    }

    public boolean isValid() {
        if (this.extra != null && !this.extra.isEmpty() && this.extra.contains(":")) {
            return super.isValid();
        } else {
            ModLogger.warn("Invalid bonus condition: isriding," + this.extra + ". The 'extra' field has not been defined correctly (it must be an entity type key for this type).");
            return false;
        }
    }

    public MutableComponent translationText() {
        if (this.component == null) {
            if (this.tooltipDescription != null && !this.tooltipDescription.isEmpty()) {
                this.component = ComponentManager.createComponent(this.tooltipDescription, true);
            }

            this.component = this.inverted ? ComponentManager.mergeComponents(new Component[]{ComponentManager.not, ComponentManager.space}) : ComponentManager.empty;
            this.component = ComponentManager.mergeComponents(new Component[]{this.component, ComponentManager.conditionIsRiding, ComponentManager.space, ComponentManager.createComponent(this.ridableEntity.getDescriptionId(), true)});
        }

        return this.component;
    }
}
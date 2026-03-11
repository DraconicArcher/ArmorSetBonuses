package uk.co.dotcode.asb.config.conditions;

import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import uk.co.dotcode.asb.ComponentManager;

public class ConditionUsingOffHand extends TriggerCondition {

    private transient MutableComponent component = null;

    public ConditionUsingOffHand(boolean inverted) {
        super("isusingoffhand", inverted);
    }

    @Override
    public boolean conditionMet(LivingEntity e) {
        boolean usingOffHand = false;

        if (e.isUsingItem() && e.getUsedItemHand() == InteractionHand.OFF_HAND) {
            usingOffHand = true;
        }


        if (e instanceof Player player) {
            if (player.swinging) {

                usingOffHand = player.getUsedItemHand() == InteractionHand.OFF_HAND || true;

            }
        }

        return inverted ? !usingOffHand : usingOffHand;
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

                component = ComponentManager.mergeComponents(component, ComponentManager.conditionIsUsingOffHand);
            }
        }

        return component;
    }
}

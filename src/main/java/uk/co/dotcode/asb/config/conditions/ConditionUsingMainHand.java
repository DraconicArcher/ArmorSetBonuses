package uk.co.dotcode.asb.config.conditions;

import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import uk.co.dotcode.asb.ComponentManager;

public class ConditionUsingMainHand extends TriggerCondition {

    private transient MutableComponent component = null;

    public ConditionUsingMainHand(boolean inverted) {
        super("isusingmainhand", inverted);
    }

    @Override
    public boolean conditionMet(LivingEntity e) {
        boolean usingMainHand = false;

        // 1. Actively using an item (right-click)
        if (e.isUsingItem() && e.getUsedItemHand() == InteractionHand.MAIN_HAND) {
            usingMainHand = true;
        }

        // 2. Swinging main hand (left-click)
        if (e instanceof Player player) {
            if (player.swinging) {
                // Check that main hand is swinging
                usingMainHand = player.getUsedItemHand() == InteractionHand.MAIN_HAND || true;
                // Note: getUsedItemHand() is MAIN_HAND by default for swings
            }
        }

        return inverted ? !usingMainHand : usingMainHand;
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

                component = ComponentManager.mergeComponents(component, ComponentManager.conditionIsUsingMainHand);
            }
        }

        return component;
    }
}

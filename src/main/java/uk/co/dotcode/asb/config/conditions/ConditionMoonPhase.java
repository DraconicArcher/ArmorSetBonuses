package uk.co.dotcode.asb.config.conditions;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.LivingEntity;
import uk.co.dotcode.asb.ComponentManager;
import uk.co.dotcode.asb.ModLogger;
import uk.co.dotcode.asb.ModUtils;

public class ConditionMoonPhase extends TriggerCondition {

    private transient MutableComponent component = null;

    public ConditionMoonPhase(String moonPhase, boolean inverted) {
        super("moonPhase", inverted);
        this.extra = moonPhase;
    }

    @Override
    public boolean conditionMet(LivingEntity e) {
        int expectedPhase = convertMoonPhaseToInt(extra);
        return inverted != (e.level().getMoonPhase() == expectedPhase);
    }

    @Override
    public boolean isValid() {
        if (extra == null || extra.isEmpty() || convertMoonPhaseToInt(extra) == -1) {
            ModLogger.warn("Invalid bonus condition: moonPhase," + extra
                    + ". The 'extra' field must be a valid moon phase.");
            return false;
        }

        return super.isValid();
    }

    private static int convertMoonPhaseToInt(String phase) {
        return switch (phase.toLowerCase()) {
            case "full" -> 0;
            case "waninggibbous" -> 1;
            case "lastquarter" -> 2;
            case "waningcresent" -> 3;
            case "new" -> 4;
            case "waxingcresent" -> 5;
            case "firstquarter" -> 6;
            case "waxinggibbous" -> 7;
            default -> -1;
        };
    }



    @Override
    public MutableComponent translationText() {
        if (component == null) {
            if (tooltipDescription != null && !tooltipDescription.isEmpty()) {
                component = ComponentManager.createComponent(tooltipDescription, true);
            } else {
                if (inverted) {
                    component = ComponentManager.mergeComponents(ComponentManager.moonPhaseComponentStartNot,
                            getPhaseTextComponent(), ComponentManager.moonPhaseComponentEnd);
                } else {
                    component = ComponentManager.mergeComponents(ComponentManager.moonPhaseComponentStart,
                            getPhaseTextComponent(), ComponentManager.moonPhaseComponentEnd);
                }
            }
        }
        return component;
    }

    private Component getPhaseTextComponent() {
        switch (extra.toLowerCase()) {
            default:
                return ComponentManager.emptyError;

            case "full":
                return ComponentManager.moonPhaseDescriptionFull;
            case "waninggibbous":
                return ComponentManager.moonPhaseDescriptionWaningGibbous;
            case "lastquarter":
                return ComponentManager.moonPhaseDescriptionLastQuarter;
            case "waningcresent":
                return ComponentManager.moonPhaseDescriptionWaningCresent;
            case "new":
                return ComponentManager.moonPhaseDescriptionNew;
            case "waxingcresent":
                return ComponentManager.moonPhaseDescriptionWaxingCresent;
            case "firstquarter":
                return ComponentManager.moonPhaseDescriptionFirstQuarter;
            case "waxinggibbous":
                return ComponentManager.moonPhaseDescriptionWaxingGibbous;
        }
    }
}

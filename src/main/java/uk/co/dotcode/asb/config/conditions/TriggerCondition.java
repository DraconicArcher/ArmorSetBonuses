package uk.co.dotcode.asb.config.conditions;

import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.LivingEntity;
import uk.co.dotcode.asb.ModLogger;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class TriggerCondition {

    public String type;               // JSON type
    public String extra;              // optional extra info
    public String tooltipDescription;
    public boolean inverted = false;

    protected transient TriggerCondition parsed; // cache parsed condition

    // Registry of condition types
    private static final Map<String, Function<TriggerCondition, TriggerCondition>> REGISTRY = new HashMap<>();

    static {
        REGISTRY.put("inwater", c -> new ConditionInWater(c.inverted));
        REGISTRY.put("inlava", c -> new ConditionInLava(c.inverted));
        REGISTRY.put("inrain", c -> new ConditionInRain(c.inverted));
        REGISTRY.put("onblock", c -> new ConditionOnBlock(c.extra, c.inverted));
        REGISTRY.put("isusingmainhand", c -> new ConditionUsingMainHand(c.inverted));
        REGISTRY.put("isusingoffhand", c -> new ConditionUsingOffHand(c.inverted));
        REGISTRY.put("inbiome", c -> new ConditionInBiome(c.extra, c.inverted));
        REGISTRY.put("timerange", c -> new ConditionTimeRange(c.extra, c.inverted));
        REGISTRY.put("moonphase", c -> new ConditionMoonPhase(c.extra, c.inverted));
        REGISTRY.put("indimension", c -> new ConditionInDimension(c.extra, c.inverted));
        REGISTRY.put("onfire", c -> new ConditionOnFire(c.inverted));
        REGISTRY.put("isriding", c -> new ConditionIsRiding(c.extra, c.inverted));
        REGISTRY.put("ylevelrange", c -> new ConditionYLevelRange(c.extra, c.inverted));
        REGISTRY.put("healthrange", c -> new ConditionHealthRange(c.extra, c.inverted));
        REGISTRY.put("hotkeyactive", c -> new ConditionHotkeyActive(c.extra, c.inverted));
    }

    public TriggerCondition() {}

    public TriggerCondition(String type, boolean inverted) {
        this.type = type;
        this.inverted = inverted;
    }

    /**
     * Parse JSON condition into runtime implementation.
     * Guaranteed to never return null.
     */
    public TriggerCondition parse() {
        if (parsed != null) return parsed;

        if (type == null || type.isBlank()) {
            return parsed = invalid("null or empty type");
        }

        // Normalize type to lowercase for registry lookup
        String key = type.toLowerCase();
        Function<TriggerCondition, TriggerCondition> factory = REGISTRY.get(key);

        if (factory == null) {
            return parsed = invalid("unknown type '" + type + "'");
        }

        parsed = factory.apply(this);
        parsed.tooltipDescription = this.tooltipDescription; // carry over tooltip
        return parsed;
    }

    private TriggerCondition invalid(String reason) {
        ModLogger.warn("Invalid TriggerCondition (" + reason + ")");
        return new TriggerCondition("invalid", false) {
            @Override
            public boolean conditionMet(LivingEntity e) {
                return false;
            }

            @Override
            public MutableComponent translationText() {
                return net.minecraft.network.chat.Component.literal("INVALID CONDITION");
            }
        };
    }

    public boolean conditionMet(LivingEntity e) {
        return parse().conditionMet(e);
    }

    public boolean isValid() {
        return parse() != null && !type.isBlank();
    }

    public MutableComponent translationText() {
        return parse().translationText();
    }
}
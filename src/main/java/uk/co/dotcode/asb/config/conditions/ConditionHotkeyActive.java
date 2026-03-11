package uk.co.dotcode.asb.config.conditions;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.network.chat.MutableComponent;
import uk.co.dotcode.asb.ComponentManager;
import uk.co.dotcode.asb.config.ToggleManager;

public class ConditionHotkeyActive extends TriggerCondition {

    private final String keyChar; // e.g., "N"
    private transient MutableComponent component;

    public ConditionHotkeyActive(String keyChar, boolean inverted) {
        super("hotkeyactive", inverted);
        if (keyChar == null || keyChar.isEmpty()) throw new IllegalArgumentException("Hotkey character cannot be null");
        this.keyChar = keyChar.toUpperCase();
        this.extra = this.keyChar;
    }

    @Override
    public boolean conditionMet(LivingEntity e) {
        if (!(e instanceof Player player)) return false;



        String keyName = "key.asb.toggle" + keyChar;
        boolean active = ToggleManager.isActive(player, keyName);
        return inverted ? !active : active;
    }

    @Override
    public boolean isValid() {
        return keyChar != null && !keyChar.isEmpty();
    }

    @Override
    public MutableComponent translationText() {
        if (component == null) {
            MutableComponent keyComponent = ComponentManager.createComponent(extra, true);
            component = inverted ? ComponentManager.mergeComponents(ComponentManager.not, ComponentManager.space) : ComponentManager.empty;
            component = ComponentManager.mergeComponents(component, ComponentManager.conditionHotkeyActive, ComponentManager.space, keyComponent);
        }
        return component;
    }
}
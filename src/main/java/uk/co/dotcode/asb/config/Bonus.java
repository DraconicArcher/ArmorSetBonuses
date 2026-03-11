package uk.co.dotcode.asb.config;

import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.entity.player.Player;
import uk.co.dotcode.asb.ComponentManager;
import uk.co.dotcode.asb.ModLogger;
import uk.co.dotcode.asb.ModUtils;
import uk.co.dotcode.asb.config.conditions.ConditionHotkeyActive;
import uk.co.dotcode.asb.config.conditions.TriggerCondition;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Bonus {

    /* ---------------- Config fields (JSON) ---------------- */
    /* ---------------- Config fields (JSON) ---------------- */
    public String type;
    public String name; // registry id string
    public float value;
    public String modifierId;
    public String description;
    public Integer attributeOperation;
    public String interactionType;
    public Integer interactionDuration;
    public boolean hideBonusDescription = false;
    public boolean onlyImmuneToNewEffects = false;
    public float percentageChance = 100.0F; // default 100%
    public int duration = 300;   // effect duration default
    public int amplifier = 0;    // effect amplifier default

    public TriggerCondition[] conditions;

    /* ---------------- Runtime cached data ---------------- */
    private transient MutableComponent conditionsComponent;
    private transient ResourceLocation registryId;

    /* ---------------- Constructors ---------------- */
    public Bonus() {
        if (interactionType == null || interactionType.isEmpty()) {
            interactionType = "self";
        }
        if (interactionDuration == null || interactionDuration <= 0) {
            interactionDuration = 300;
        }
    }

    public Bonus(String type, String name, float value) {
        this.type = type;
        this.name = name;
        this.value = value;
        this.interactionType = "self";
        this.interactionDuration = 300;
    }

    public Bonus setModifierId(String id) {
        this.modifierId = id;
        return this;
    }

    /* ---------------- Registry helpers ---------------- */
    private ResourceLocation getRegistryId() {
        if (registryId == null) {
            registryId = ResourceLocation.tryParse(name);
        }
        return registryId;
    }

    private ResourceLocation getModifierId() {
        return ResourceLocation.tryParse(modifierId != null ? modifierId : name);
    }

    public MobEffectInstance createEffectInstance(RegistryAccess access) {
        if (!type.equalsIgnoreCase("effect")) return null;

        Holder<MobEffect> effectHolder =
                access.registryOrThrow(Registries.MOB_EFFECT)
                        .getHolder(getRegistryId())
                        .orElse(null);

        if (effectHolder == null) return null;

        return new MobEffectInstance(effectHolder, interactionDuration, (int) value, false, false, true);
    }

    /* ---------------- Bonus application ---------------- */
    public void applyBonus(LivingEntity entity, String targetInteractionType) {
        if (!interactionType.equalsIgnoreCase(targetInteractionType)) return;

        RegistryAccess access = entity.level().registryAccess();

        if (type.equalsIgnoreCase("effect")) {
            applyEffectBonus(entity, access);
        } else if (type.equalsIgnoreCase("attribute") && entity instanceof Player player) {
            applyAttributeBonus(player);
        }
    }

    private void applyEffectBonus(LivingEntity entity, RegistryAccess access) {
        Holder<MobEffect> holder =
                access.registryOrThrow(Registries.MOB_EFFECT)
                        .getHolder(getRegistryId())
                        .orElse(null);
        if (holder == null) return;

        MobEffectInstance instance =
                new MobEffectInstance(holder, interactionDuration, (int) value, false, false, true);

        if (entity.hasEffect(holder)) {
            if (entity.tickCount % 90 == 0) {
                entity.addEffect(instance);
            }
        } else {
            entity.addEffect(instance);
        }
    }



    private transient Map<UUID, Boolean> appliedPlayers = new HashMap<>();

    private void applyAttributeBonus(Player player) {
        RegistryAccess access = player.level().registryAccess();
        Holder<Attribute> attributeHolder = access.registryOrThrow(Registries.ATTRIBUTE).getHolder(getRegistryId()).orElse(null);
        if (attributeHolder == null) return;

        AttributeInstance instance = player.getAttribute(attributeHolder);
        if (instance == null) return;

        Operation operation = attributeOperation != null ? switch (attributeOperation) {
            case 0 -> Operation.ADD_VALUE;
            case 1 -> Operation.ADD_MULTIPLIED_BASE;
            case 2 -> Operation.ADD_MULTIPLIED_TOTAL;
            default -> Operation.ADD_VALUE;
        } : Operation.ADD_VALUE;

        AttributeModifier modifier = new AttributeModifier(getModifierId(), value, operation);

        // --- Check persistent toggle state ---
        boolean active = true;
        if (conditions != null) {
            for (TriggerCondition condition : conditions) {
                if (condition instanceof ConditionHotkeyActive hotkeyCondition) {
                    String keyName = "key.asb.toggle" + hotkeyCondition.extra;
                    active = ToggleManager.isActive(player, keyName); // persistent state
                } else {
                    active = condition.conditionMet(player);
                }
                if (!active) break;
            }
        }

        // Apply or remove modifier based on persistent state
        if (active && !instance.hasModifier(getModifierId())) {
            instance.addPermanentModifier(modifier);
        } else if (!active && instance.hasModifier(getModifierId())) {
            instance.removeModifier(getModifierId());
        }
    }

    /* ---------------- Immunity handling ---------------- */
    public void applyImmunity(LivingEntity entity) {
        if (!interactionType.equalsIgnoreCase("immunity")) return;

        Holder<MobEffect> holder =
                entity.level().registryAccess()
                        .registryOrThrow(Registries.MOB_EFFECT)
                        .getHolder(getRegistryId())
                        .orElse(null);

        if (holder == null) return;

        if (entity.hasEffect(holder) && !onlyImmuneToNewEffects) {
            entity.removeEffect(holder);
        }
    }

    /* ---------------- Removal ---------------- */
    public void removeBonus(LivingEntity entity, String targetInteractionType) {
        if (!interactionType.equalsIgnoreCase(targetInteractionType)) return;
        if (!(entity instanceof Player player)) return;
        if (!type.equalsIgnoreCase("attribute")) return;

        RegistryAccess access = entity.level().registryAccess();
        Holder<Attribute> attributeHolder =
                access.registryOrThrow(Registries.ATTRIBUTE)
                        .getHolder(getRegistryId())
                        .orElse(null);
        if (attributeHolder == null) return;

        AttributeInstance instance = player.getAttribute(attributeHolder);
        if (instance != null && instance.hasModifier(getModifierId())) {
            instance.removeModifier(getModifierId());
        }

        // Clear tracking
        appliedPlayers.remove(player.getUUID());
    }

    /* ---------------- Conditions ---------------- */
    public boolean conditionsMet(LivingEntity entity) {
        if (conditions == null) return true;

        for (TriggerCondition condition : conditions) {
            if (!condition.conditionMet(entity)) return false;
        }
        return true;
    }

    /* ---------------- Validation ---------------- */
    // Lightweight version for ArmorSet
    public boolean isValid(String armorSetName) {
        return true; // defer registry validation to runtime
    }

    // Full runtime validation
    public boolean isValid(String armorSetName, RegistryAccess access) {
        ResourceLocation id = getRegistryId();
        if (id == null) {
            ModLogger.warn(armorSetName + ": Invalid registry id - " + name);
            return false;
        }

        if (type.equalsIgnoreCase("effect")) {
            if (!access.registryOrThrow(Registries.MOB_EFFECT).containsKey(id)) {
                ModLogger.warn(armorSetName + ": Invalid effect - " + name);
                return false;
            }
        } else if (type.equalsIgnoreCase("attribute")) {
            Holder<Attribute> attributeHolder =
                    access.registryOrThrow(Registries.ATTRIBUTE)
                            .getHolder(id)
                            .orElse(null);
            if (attributeHolder == null) {
                ModLogger.warn(armorSetName + ": Invalid attribute - " + name);
                return false;
            }

            if (modifierId == null || ResourceLocation.tryParse(modifierId) == null) {
                ModLogger.warn(armorSetName + ": Invalid modifierId - " + modifierId);
                return false;
            }
        } else {
            ModLogger.warn(armorSetName + ": Invalid bonus type - " + type);
            return false;
        }

        if (!interactionType.matches("self|attack|interact|immunity|aoe")) {
            ModLogger.warn(armorSetName + ": Invalid interactionType - " + interactionType);
            return false;
        }

        if (conditions != null) {
            for (int i = 0; i < conditions.length; i++) {
                conditions[i] = conditions[i].parse();
                if (!conditions[i].isValid()) return false;
            }
        }

        return true;
    }

    /* ---------------- UI helpers ---------------- */
    public MutableComponent getConditionsComponent() {
        if (conditions == null || conditions.length == 0) return ComponentManager.empty;

        if (conditionsComponent == null) {
            conditionsComponent = ComponentManager.mergeComponents(
                    ComponentManager.space,
                    ComponentManager.when,
                    ComponentManager.space
            );

            for (int i = 0; i < conditions.length; i++) {
                if (i > 0) {
                    conditionsComponent = ComponentManager.mergeComponents(
                            conditionsComponent,
                            ComponentManager.comma
                    );
                }
                conditionsComponent = ComponentManager.mergeComponents(
                        conditionsComponent,
                        conditions[i].translationText()
                );
            }
        }

        return conditionsComponent;
    }

    public MutableComponent getInteractionTypeComponent() {
        return switch (interactionType.toLowerCase()) {
            case "self" -> ComponentManager.interactionSelf;
            case "attack" -> ComponentManager.interactionAttack;
            case "interact" -> ComponentManager.interactionInteract;
            case "immunity" -> ComponentManager.interactionImmunity;
            case "aoe" -> ComponentManager.interactionAoe;
            default -> ComponentManager.emptyError;
        };
    }

    /* ---------------- Effect helpers ---------------- */
    public MobEffectInstance getBonusEffectInstance() {
        Holder<MobEffect> holder = ModUtils.getMobEffect(this.name);
        int amp = (int) value;
        return new MobEffectInstance(holder, interactionDuration, amp, false, false, true);
    }

}
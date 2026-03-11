package uk.co.dotcode.asb;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public class ComponentManager {
    public static final MutableComponent empty = Component.empty();
    public static final MutableComponent space = createComponent(" ", false);
    public static final MutableComponent dash = createComponent("- ", false);
    public static final MutableComponent comma = createComponent(", ", false);
    public static final MutableComponent bracketOpen = createComponent("(", false);
    public static final MutableComponent bracketClose = createComponent(")", false);
    public static final MutableComponent when = createComponent("tooltip.translation.spacecatasb.conditions.when", true);
    public static final MutableComponent and = createComponent("tooltip.translation.spacecatasb.conditions.and", true);
    public static final MutableComponent not = createComponent("tooltip.translation.spacecatasb.conditions.not", true);
    public static final MutableComponent no = createComponent("tooltip.translation.spacecatasb.enchantment.no", true);
    public static final MutableComponent chanceOfApplying = createComponent("tooltip.translation.spacecatasb.chanceofapplying", true);
    public static final MutableComponent setTitlePrefix = createComponent("tooltip.translation.spacecatasb.settitleprefix", true);
    public static final MutableComponent setTitleSuffix = createComponent("tooltip.translation.spacecatasb.settitlesuffix", true);
    public static final MutableComponent partialSetBonusesPrefix = createComponent("tooltip.translation.spacecatasb.partialsetbonusesprefix", true);
    public static final MutableComponent partialSetBonusesSuffix = createComponent("tooltip.translation.spacecatasb.partialsetbonusessuffix", true);
    public static final MutableComponent fullSetBonusesTitle = createComponent("tooltip.translation.spacecatasb.fullsetbonusestitle", true);
    public static final MutableComponent invalidDescription = createComponent("tooltip.translation.spacecatasb.invaliddescription", true);
    public static final MutableComponent holdKeysDetails = createComponent("tooltip.translation.spacecatasb.holdkeysdetails", true);
    public static final MutableComponent interactionSelf = createComponent("tooltip.translation.spacecatasb.interactiontype.self", true);
    public static final MutableComponent interactionAttack = createComponent("tooltip.translation.spacecatasb.interactiontype.attack", true);
    public static final MutableComponent interactionInteract = createComponent("tooltip.translation.spacecatasb.interactiontype.interact", true);
    public static final MutableComponent interactionImmunity = createComponent("tooltip.translation.spacecatasb.interactiontype.immunity", true);
    public static final MutableComponent interactionAoe = createComponent("tooltip.translation.spacecatasb.interactiontype.aoe", true);
    public static final MutableComponent conditionInBiome = createComponent("tooltip.translation.spacecatasb.conditions.description.inbiome", true);
    public static final MutableComponent conditionInDimension = createComponent("tooltip.translation.spacecatasb.conditions.description.indimension", true);
    public static final MutableComponent conditionHotkeyActive = createComponent("tooltip.translation.spacecatasb.conditions.description.hotkeyactive", true);
    public static final MutableComponent conditionInLava = createComponent("tooltip.translation.spacecatasb.conditions.description.inlava", true);
    public static final MutableComponent conditionInRain = createComponent("tooltip.translation.spacecatasb.conditions.description.inrain", true);
    public static final MutableComponent conditionInWater = createComponent("tooltip.translation.spacecatasb.conditions.description.inwater", true);
    public static final MutableComponent conditionOnBlock = createComponent("tooltip.translation.spacecatasb.conditions.description.onblock", true);
    public static final MutableComponent conditionIsUsingMainHand = createComponent("tooltip.translation.spacecatasb.conditions.description.isusingmainhand", true);
    public static final MutableComponent conditionIsUsingOffHand = createComponent("tooltip.translation.spacecatasb.conditions.description.isusingoffhand", true);
    public static final MutableComponent conditionOnFire = createComponent("tooltip.translation.spacecatasb.conditions.description.onfire", true);
    public static final MutableComponent conditionIsRiding = createComponent("tooltip.translation.spacecatasb.conditions.description.isriding", true);
    public static final MutableComponent conditionYRange = createComponent("tooltip.translation.spacecatasb.conditions.description.ylevelrange", true);
    public static final MutableComponent timeRangePrefix = createComponent("tooltip.translation.spacecatasb.conditions.description.timerange.prefix", true);
    public static final MutableComponent conditionHealthRange = createComponent("tooltip.translation.spacecatasb.conditions.description.healthrange", true);
    private static final MutableComponent moonPhasePrefix = createComponent("tooltip.translation.spacecatasb.conditions.description.moonphase.prefix", true);
    private static final MutableComponent moonPhasePrefixNot = createComponent("tooltip.translation.spacecatasb.conditions.description.moonphase.prefixNot", true);
    private static final MutableComponent moonPhaseSuffix = createComponent("tooltip.translation.spacecatasb.conditions.description.moonphase.suffix", true);
    public static final MutableComponent moonPhaseComponentStart;
    public static final MutableComponent moonPhaseComponentStartNot;
    public static final MutableComponent moonPhaseDescriptionFull;
    public static final MutableComponent moonPhaseDescriptionWaningGibbous;
    public static final MutableComponent moonPhaseDescriptionLastQuarter;
    public static final MutableComponent moonPhaseDescriptionWaningCresent;
    public static final MutableComponent moonPhaseDescriptionNew;
    public static final MutableComponent moonPhaseDescriptionWaxingCresent;
    public static final MutableComponent moonPhaseDescriptionFirstQuarter;
    public static final MutableComponent moonPhaseDescriptionWaxingGibbous;
    public static final MutableComponent moonPhaseComponentEnd;
    public static final MutableComponent slotMainHand;
    public static final MutableComponent slotOffhand;
    public static final MutableComponent slotEmptyHead;
    public static final MutableComponent slotEmptyChest;
    public static final MutableComponent slotEmptyLegs;
    public static final MutableComponent slotEmptyFeet;
    public static final MutableComponent slotEmptyMainHand;
    public static final MutableComponent slotEmptyOffhand;
    public static final MutableComponent emptyError;

    public static MutableComponent mergeComponents(Component... components) {
        MutableComponent mc = Component.empty();
        Component[] var2 = components;
        int var3 = components.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            Component c = var2[var4];
            mc.append(c);
        }

        return mc;
    }

    public static MutableComponent createComponent(String text, boolean isTranslatable) {
        return isTranslatable ? Component.translatable(text) : Component.literal(text);
    }

    public static MutableComponent setGreen(MutableComponent c) {
        return c.withStyle(ChatFormatting.GREEN);
    }

    public static MutableComponent setGray(MutableComponent c) {
        return c.withStyle(ChatFormatting.GRAY);
    }

    public static MutableComponent setAqua(MutableComponent c) {
        return c.withStyle(ChatFormatting.AQUA);
    }

    static {
        moonPhaseComponentStart = mergeComponents(moonPhasePrefix, space);
        moonPhaseComponentStartNot = mergeComponents(moonPhasePrefixNot, space);
        moonPhaseDescriptionFull = createComponent("tooltip.translation.spacecatasb.conditions.description.moonphase.full", true);
        moonPhaseDescriptionWaningGibbous = createComponent("tooltip.translation.spacecatasb.conditions.description.moonphase.waninggibbous", true);
        moonPhaseDescriptionLastQuarter = createComponent("tooltip.translation.spacecatasb.conditions.description.moonphase.lastquarter", true);
        moonPhaseDescriptionWaningCresent = createComponent("tooltip.translation.spacecatasb.conditions.description.moonphase.waningcresent", true);
        moonPhaseDescriptionNew = createComponent("tooltip.translation.spacecatasb.conditions.description.moonphase.new", true);
        moonPhaseDescriptionWaxingCresent = createComponent("tooltip.translation.spacecatasb.conditions.description.moonphase.waxingcresent", true);
        moonPhaseDescriptionFirstQuarter = createComponent("tooltip.translation.spacecatasb.conditions.description.moonphase.firstquarter", true);
        moonPhaseDescriptionWaxingGibbous = createComponent("tooltip.translation.spacecatasb.conditions.description.moonphase.waxinggibbous", true);
        moonPhaseComponentEnd = mergeComponents(space, moonPhaseSuffix);
        slotMainHand = createComponent("tooltip.translation.spacecatasb.slot.mainhand", true);
        slotOffhand = createComponent("tooltip.translation.spacecatasb.slot.offhand", true);
        slotEmptyHead = createComponent("tooltip.translation.spacecatasb.slot.emptyhead", true);
        slotEmptyChest = createComponent("tooltip.translation.spacecatasb.slot.emptychest", true);
        slotEmptyLegs = createComponent("tooltip.translation.spacecatasb.slot.emptylegs", true);
        slotEmptyFeet = createComponent("tooltip.translation.spacecatasb.slot.emptyfeet", true);
        slotEmptyMainHand = createComponent("tooltip.translation.spacecatasb.slot.emptymainhand", true);
        slotEmptyOffhand = createComponent("tooltip.translation.spacecatasb.slot.emptyoffhand", true);
        emptyError = createComponent("tooltip.translation.spacecatasb.slot.emptyerror", true);
    }
}
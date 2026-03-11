package uk.co.dotcode.asb.config;

import java.util.Iterator;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import uk.co.dotcode.asb.ModLogger;
import uk.co.dotcode.asb.ModUtils;
import uk.co.dotcode.asb.config.neoforge.ArmorSetImpl;

public class ArmorSet {
    public String armorSetName;
    public boolean performanceMode = true;
    public SetPiece head;
    public SetPiece chest;
    public SetPiece legs;
    public SetPiece boots;
    public SetPiece mainHand;
    public SetPiece offHand;
    public AdditionalSetPiece[] additionalSetPieces;
    public Integer partialBonusRequiredAmount;
    public Bonus[] partialSetBonuses;
    public Bonus[] fullSetBonuses;
    public boolean hideGeneratedTooltip = false;
    public String[] customTooltips;
    public transient Integer armorSetCount;

    public ArmorSet(String setName, String head, String chest, String legs, String boots) {
        this.armorSetName = setName;
        this.head = new SetPiece(head);
        this.chest = new SetPiece(chest);
        this.legs = new SetPiece(legs);
        this.boots = new SetPiece(boots);
    }

    public ArmorSet(String setName, SetPiece head, SetPiece chest, SetPiece legs, SetPiece boots) {
        this.armorSetName = setName;
        this.head = head;
        this.chest = chest;
        this.legs = legs;
        this.boots = boots;
    }

    public boolean armorSetMatch(LivingEntity entity) {
        this.armorSetCount = 0;
        boolean head = this.head == null;
        boolean chest = this.chest == null;
        boolean legs = this.legs == null;
        boolean feet = this.boots == null;
        boolean mainHand = this.mainHand == null;
        boolean offHand = this.offHand == null;
        boolean additionalSetPieces = this.additionalSetPieces == null;
        Iterator var9 = entity.getArmorSlots().iterator();

        while(true) {
            while(var9.hasNext()) {
                ItemStack i = (ItemStack)var9.next();
                String itemRegName = ModUtils.getRegistryNameItem(i.getItem()).toString();
                Integer var12;
                if (this.head != null && this.head.matches(i, itemRegName)) {
                    head = true;
                    var12 = this.armorSetCount;
                    this.armorSetCount = this.armorSetCount + 1;
                } else if (this.chest != null && this.chest.matches(i, itemRegName)) {
                    chest = true;
                    var12 = this.armorSetCount;
                    this.armorSetCount = this.armorSetCount + 1;
                } else if (this.legs != null && this.legs.matches(i, itemRegName)) {
                    legs = true;
                    var12 = this.armorSetCount;
                    this.armorSetCount = this.armorSetCount + 1;
                } else if (this.boots != null && this.boots.matches(i, itemRegName)) {
                    feet = true;
                    var12 = this.armorSetCount;
                    this.armorSetCount = this.armorSetCount + 1;
                }
            }

            Integer var13;
            if (this.mainHand != null && this.mainHand.matches(entity.getMainHandItem(), ModUtils.getRegistryNameItem(entity.getMainHandItem().getItem()).toString())) {
                mainHand = true;
                var13 = this.armorSetCount;
                this.armorSetCount = this.armorSetCount + 1;
            }

            if (this.offHand != null && this.offHand.matches(entity.getOffhandItem(), ModUtils.getRegistryNameItem(entity.getOffhandItem().getItem()).toString())) {
                offHand = true;
                var13 = this.armorSetCount;
                this.armorSetCount = this.armorSetCount + 1;
            }

            additionalSetPieces = armorSetMatchAdditional(entity, this.additionalSetPieces);
            this.armorSetCount = this.armorSetCount + armorSetCountAdditional(entity, this.additionalSetPieces);
            return head && chest && legs && feet && mainHand && offHand && additionalSetPieces;
        }
    }

    public static boolean armorSetMatchAdditional(LivingEntity entity, AdditionalSetPiece[] additionalSetPieces) {
        return ArmorSetImpl.armorSetMatchAdditional(entity, additionalSetPieces);
    }


    public int armorSetCount(LivingEntity entity) {
        return this.armorSetCount != null ? this.armorSetCount : 0;
    }

    public static int armorSetCountAdditional(LivingEntity entity, AdditionalSetPiece[] additionalSetPieces) {
        return ArmorSetImpl.armorSetCountAdditional(entity, additionalSetPieces);
    }


    public boolean isPartOfSet(String item) {
        if (this.head != null && this.head.matchesSimple(item)) {
            return true;
        } else if (this.chest != null && this.chest.matchesSimple(item)) {
            return true;
        } else if (this.legs != null && this.legs.matchesSimple(item)) {
            return true;
        } else if (this.boots != null && this.boots.matchesSimple(item)) {
            return true;
        } else if (this.mainHand != null && this.mainHand.matchesSimple(item)) {
            return true;
        } else {
            return this.offHand != null && this.offHand.matchesSimple(item) ? true : isPartOfSetAdditional(item, this.additionalSetPieces);
        }
    }

    public static boolean isPartOfSetAdditional(String item, AdditionalSetPiece[] additionalSetPieces) {
        return ArmorSetImpl.isPartOfSetAdditional(item, additionalSetPieces);
    }


    public boolean isValid() {
        boolean valid = true;
        if (this.armorSetName == null || this.armorSetName.isEmpty()) {
            ModLogger.warn("Failed to add custom armorset: It is unnamed!");
            valid = false;
        }

        if (this.head != null && !this.head.verify(this.armorSetName, "head")) {
            valid = false;
        }

        if (this.chest != null && !this.chest.verify(this.armorSetName, "chest")) {
            valid = false;
        }

        if (this.legs != null && !this.legs.verify(this.armorSetName, "legs")) {
            valid = false;
        }

        if (this.boots != null && !this.boots.verify(this.armorSetName, "boots")) {
            valid = false;
        }

        if (this.mainHand != null && !this.mainHand.verify(this.armorSetName, "mainHand")) {
            valid = false;
        }

        if (this.offHand != null && !this.offHand.verify(this.armorSetName, "offHand")) {
            valid = false;
        }

        if (!verifyAdditional(this.armorSetName, this.additionalSetPieces)) {
            valid = false;
        }

        Bonus[] var2;
        int var3;
        int var4;
        Bonus b;
        boolean isBonusValid;
        if (this.partialSetBonuses != null && this.partialSetBonuses.length > 0) {
            if (this.partialBonusRequiredAmount != null) {
                if (this.partialBonusRequiredAmount >= 1 && this.partialBonusRequiredAmount <= 5) {
                    if (this.partialBonusRequiredAmount >= this.getPieceCount()) {
                        String var10000 = this.armorSetName;
                        ModLogger.warn("Failed to add custom armorset: " + var10000 + ". 'partialBonusRequiredAmount' is equal to or higher than the total of " + this.getPieceCount() + " armor pieces. Make sure that it is less than this number.");
                        valid = false;
                    }
                } else {
                    ModLogger.warn("Failed to add custom armorset: " + this.armorSetName + ". 'partialBonusRequiredAmount' is limited to the values 2-5 (inclusive).");
                    valid = false;
                }
            } else {
                ModLogger.warn("Failed to add custom armorset: " + this.armorSetName + ". 'partialBonusRequiredAmount' must be defined because you have added partial bonuses.");
                valid = false;
            }

            var2 = this.partialSetBonuses;
            var3 = var2.length;

            for(var4 = 0; var4 < var3; ++var4) {
                b = var2[var4];
                isBonusValid = b.isValid(this.armorSetName);
                if (!isBonusValid) {
                    ModLogger.warn("Failed to add custom armorset: " + this.armorSetName + ". Check reasons logged above.");
                    valid = false;
                }
            }
        }

        if (this.partialBonusRequiredAmount != null && this.partialBonusRequiredAmount > 0 && (this.partialSetBonuses == null || this.partialSetBonuses.length <= 0)) {
            ModLogger.warn("Failed to add custom armorset: " + this.armorSetName + ". You have defined 'partialBonusRequiredAmount', but there are no 'partialSetBonuses' defined");
            valid = false;
        }

        if (this.fullSetBonuses != null) {
            if (this.fullSetBonuses.length <= 0) {
                ModLogger.warn("Failed to add custom armorset: " + this.armorSetName + ". No bonuses defined.");
                valid = false;
            } else {
                var2 = this.fullSetBonuses;
                var3 = var2.length;

                for(var4 = 0; var4 < var3; ++var4) {
                    b = var2[var4];
                    isBonusValid = b.isValid(this.armorSetName);
                    if (!isBonusValid) {
                        ModLogger.warn("Failed to add custom armorset: " + this.armorSetName + ". Check reasons logged above.");
                        valid = false;
                    }
                }
            }
        } else {
            ModLogger.warn("Failed to add custom armorset: " + this.armorSetName + ". No bonuses defined.");
            valid = false;
        }

        return valid;
    }

    public static boolean isAdditionalValid(AdditionalSetPiece[] additionalSetPieces) {
        return ArmorSetImpl.isAdditionalValid(additionalSetPieces);
    }


    public static boolean verifyAdditional(String armorSetName, AdditionalSetPiece[] additionalSetPieces) {
        return ArmorSetImpl.verifyAdditional(armorSetName, additionalSetPieces);
    }


    public int getPieceCount() {
        int count = 0;
        if (this.head != null) {
            ++count;
        }

        if (this.chest != null) {
            ++count;
        }

        if (this.legs != null) {
            ++count;
        }

        if (this.boots != null) {
            ++count;
        }

        if (this.mainHand != null) {
            ++count;
        }

        if (this.offHand != null) {
            ++count;
        }

        count += getAdditionalPieceCount(this.additionalSetPieces);
        return count;
    }

    public static int getAdditionalPieceCount(AdditionalSetPiece[] additionalSetPieces) {
        return ArmorSetImpl.getAdditionalPieceCount(additionalSetPieces);
    }

}
  
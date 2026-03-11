package uk.co.dotcode.asb.config.neoforge;

import java.util.Optional;
import net.minecraft.world.entity.LivingEntity;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotResult;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;
import uk.co.dotcode.asb.ModUtils;
import uk.co.dotcode.asb.config.AdditionalSetPiece;
import uk.co.dotcode.asb.config.SetPiece;

public class ArmorSetImpl {
    public static boolean armorSetMatchAdditional(LivingEntity entity, AdditionalSetPiece[] additionalSetPieces) {
        if (additionalSetPieces != null) {
            boolean allCuriosMatch = true;

            for(int i = 0; i < additionalSetPieces.length; ++i) {
                AdditionalSetPiece c = additionalSetPieces[i];
                if (!curioMatches(entity, c)) {
                    allCuriosMatch = false;
                }
            }

            return allCuriosMatch;
        } else {
            return true;
        }
    }

    public static int armorSetCountAdditional(LivingEntity entity, AdditionalSetPiece[] additionalSetPieces) {
        int count = 0;
        if (additionalSetPieces != null) {
            for(int i = 0; i < additionalSetPieces.length; ++i) {
                AdditionalSetPiece c = additionalSetPieces[i];
                if (curioMatches(entity, c)) {
                    ++count;
                }
            }
        }

        return count;
    }

    public static boolean isPartOfSetAdditional(String item, AdditionalSetPiece[] additionalSetPieces) {
        if (additionalSetPieces != null) {
            for(int i = 0; i < additionalSetPieces.length; ++i) {
                AdditionalSetPiece c = additionalSetPieces[i];
                if (c.matchesSimple(item)) {
                    return true;
                }
            }
        }

        return false;
    }

    public static boolean isAdditionalValid(AdditionalSetPiece[] additionalSetPieces) {
        boolean valid = true;
        if (additionalSetPieces != null) {
            for(int i = 0; i < additionalSetPieces.length; ++i) {
                AdditionalSetPiece c = additionalSetPieces[i];
                if (c.itemKey.isEmpty()) {
                    valid = false;
                }
            }
        }

        return valid;
    }

    public static boolean verifyAdditional(String armorSetName, AdditionalSetPiece[] additionalSetPieces) {
        boolean valid = true;
        if (additionalSetPieces != null) {
            for(int i = 0; i < additionalSetPieces.length; ++i) {
                AdditionalSetPiece c = additionalSetPieces[i];
                if (!c.itemKey.isEmpty() && !c.verify(armorSetName, "curios-" + c.itemKey)) {
                    valid = false;
                }
            }
        }

        return valid;
    }

    public static int getAdditionalPieceCount(AdditionalSetPiece[] additionalSetPieces) {
        int count = 0;
        if (additionalSetPieces != null) {
            count = additionalSetPieces.length;
        }

        return count;
    }

    public static boolean curioMatches(LivingEntity entity, SetPiece setPiece) {
        Optional<SlotResult> optional = ((ICuriosItemHandler)CuriosApi.getCuriosInventory(entity).get()).findFirstCurio(ModUtils.getItem(setPiece.itemKey));
        if (optional.isPresent()) {
            return true;
        } else {
            if (setPiece.mixAndMatch != null && setPiece.mixAndMatch.length > 0) {
                String[] var3 = setPiece.mixAndMatch;
                int var4 = var3.length;

                for(int var5 = 0; var5 < var4; ++var5) {
                    String s = var3[var5];
                    Optional<SlotResult> optionalMix = ((ICuriosItemHandler)CuriosApi.getCuriosInventory(entity).get()).findFirstCurio(ModUtils.getItem(s));
                    if (optionalMix.isPresent()) {
                        return true;
                    }
                }
            }

            return false;
        }
    }
}
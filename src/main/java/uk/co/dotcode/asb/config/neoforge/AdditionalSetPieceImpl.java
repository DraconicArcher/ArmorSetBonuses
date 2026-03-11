package uk.co.dotcode.asb.config.neoforge;

import java.util.Optional;
import net.minecraft.world.entity.player.Player;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotResult;
import uk.co.dotcode.asb.ASB;
import uk.co.dotcode.asb.ModLogger;
import uk.co.dotcode.asb.ModUtils;
import uk.co.dotcode.asb.config.SetPiece;

public class AdditionalSetPieceImpl {
    public static boolean verifySpecific(String armorSetName, String slotType, SetPiece sp) {
        boolean valid = true;
        if (!ASB.isCuriosLoaded) {
            ModLogger.warn("Failed to add custom armorset: " + armorSetName + ". Curios slots have been declared, but the Curios mod is not installed.");
            valid = false;
        }

        if (!ModUtils.checkItemKey(sp.itemKey)) {
            ModLogger.warn("Failed to add custom armorset: " + armorSetName + ". The " + slotType + " slot is invalid!");
            valid = false;
        }

        if (sp.mixAndMatch != null && sp.mixAndMatch.length > 0) {
            String[] var4 = sp.mixAndMatch;
            int var5 = var4.length;

            for(int var6 = 0; var6 < var5; ++var6) {
                String s = var4[var6];
                if (!ModUtils.checkItemKey(s)) {
                    ModLogger.warn("Failed to add custom armorset: " + armorSetName + ". " + slotType + " MixAndMatch is invalid: " + s);
                    valid = false;
                }
            }
        }

        return valid;
    }

    public static boolean additionalMatches(Player player, SetPiece sp) {
        return ArmorSetImpl.curioMatches(player, sp);
    }

    public static boolean additionalMatchesSpecific(Player player, String itemKey) {
        Optional<SlotResult> optional = CuriosApi.getCuriosHelper().findFirstCurio(player, ModUtils.getItem(itemKey));
        return optional.isPresent();
    }
}
package uk.co.dotcode.asb.config;


import net.minecraft.world.entity.player.Player;
import uk.co.dotcode.asb.config.neoforge.AdditionalSetPieceImpl;

public class AdditionalSetPiece extends SetPiece {
    public static String additionalType = "";

    public AdditionalSetPiece(String itemKey) {
        super(itemKey);
    }

    public boolean verify(String armorSetName, String slotType) {
        return verifySpecific(armorSetName, slotType, this);
    }

    public static boolean verifySpecific(String armorSetName, String slotType, SetPiece sp) {
        return AdditionalSetPieceImpl.verifySpecific(armorSetName, slotType, sp);
    }

    public static boolean additionalMatches(Player player, SetPiece sp) {
        return AdditionalSetPieceImpl.additionalMatches(player, sp);
    }

    public static boolean additionalMatchesSpecific(Player player, String itemKey) {
        return AdditionalSetPieceImpl.additionalMatchesSpecific(player, itemKey);
    }

}
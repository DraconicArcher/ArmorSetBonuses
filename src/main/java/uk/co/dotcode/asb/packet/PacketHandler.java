package uk.co.dotcode.asb.packet;

import java.util.ArrayList;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;
import uk.co.dotcode.asb.ModUtils;
import uk.co.dotcode.asb.config.ConfigHandler;
import uk.co.dotcode.asb.config.ToggleManager;
import uk.co.dotcode.asb.network.ToggleKeyPayload;


public class PacketHandler {

    public static void sendArmorSets(ServerPlayer player) {
        ConfigHandler.localConfigArmorSets.forEach(set ->
                PacketDistributor.sendToPlayer(
                        player,
                        new ArmorSetsPayload(
                                ArmorSetsPayload.Action.ADD,
                                ModUtils.gson.toJson(set)
                        )
                )
        );
    }

    public static void sendResetMessage(ServerPlayer player) {
        PacketDistributor.sendToPlayer(
                player,
                new ArmorSetsPayload(
                        ArmorSetsPayload.Action.RESET,
                        ""
                )
        );
    }


    public static void handlePayload(ArmorSetsPayload payload) {
        switch (payload.action()) {
            case RESET -> ClientArmorSetCache.reset();

            case ADD -> ClientArmorSetCache.add(payload.toArmorSet());
        }
    }


}

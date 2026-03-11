

    package uk.co.dotcode.asb.event;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import uk.co.dotcode.asb.config.ConfigHandler;
import uk.co.dotcode.asb.packet.PacketHandler;

public class WorldJoinEvent {
    public static void onWorldLogin(Player player) {
        if (!player.level().isClientSide()) {
            PacketHandler.sendResetMessage((ServerPlayer)player);
            PacketHandler.sendArmorSets((ServerPlayer)player);
        } else {
            ConfigHandler.serverArmorSets = ConfigHandler.localConfigArmorSets;
        }

    }
}
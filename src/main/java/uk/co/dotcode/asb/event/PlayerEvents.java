package uk.co.dotcode.asb.event;

import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import uk.co.dotcode.asb.config.ToggleManager;

@EventBusSubscriber(modid = "asb")
public class PlayerEvents {

    @SubscribeEvent
    public static void onLogout(PlayerEvent.PlayerLoggedOutEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            ToggleManager.reset(player);
        }
    }
}
package uk.co.dotcode.asb.network;

import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import uk.co.dotcode.asb.config.ToggleManager;

@EventBusSubscriber(modid = "asb", bus = EventBusSubscriber.Bus.MOD)
public class NetworkHandler {

    @SubscribeEvent
    public static void register(final RegisterPayloadHandlersEvent event) {

        PayloadRegistrar registrar = event.registrar("1");

        registrar.playToServer(
                ToggleKeyPayload.TYPE,
                ToggleKeyPayload.STREAM_CODEC,
                (payload, context) -> {
                    System.out.println("Packet received on server: " + payload.keyName());

                    ServerPlayer player = (ServerPlayer) context.player();
                    if (player != null) {
                        ToggleManager.toggle(player, payload.keyName());
                        System.out.println("Toggled for: " + player.getName().getString());
                    }
                }
        );
    }
}
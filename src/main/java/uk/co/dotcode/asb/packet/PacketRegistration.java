package uk.co.dotcode.asb.packet;

import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;

public class PacketRegistration {

    public static void register(RegisterPayloadHandlersEvent event) {
        event.registrar("spacecatasb")
                .playToClient(
                        ArmorSetsPayload.TYPE,
                        ArmorSetsPayload.CODEC,
                        (payload, context) -> {
                            context.enqueueWork(() ->
                                    PacketHandler.handlePayload(payload)
                            );
                        }
                );
    }
}
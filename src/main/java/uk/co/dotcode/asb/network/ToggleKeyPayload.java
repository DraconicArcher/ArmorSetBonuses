package uk.co.dotcode.asb.network;


import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import uk.co.dotcode.asb.config.ToggleManager;

public record ToggleKeyPayload(String keyName) implements CustomPacketPayload {

    public static final Type<ToggleKeyPayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath("asb", "toggle_key"));

    public static final StreamCodec<FriendlyByteBuf, ToggleKeyPayload> STREAM_CODEC =
            StreamCodec.of(
                    (buf, payload) -> buf.writeUtf(payload.keyName),
                    buf -> new ToggleKeyPayload(buf.readUtf())
            );



    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
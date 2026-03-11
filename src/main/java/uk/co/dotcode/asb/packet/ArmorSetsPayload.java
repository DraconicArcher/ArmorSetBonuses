package uk.co.dotcode.asb.packet;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import uk.co.dotcode.asb.ModUtils;
import uk.co.dotcode.asb.config.ArmorSet;

public record ArmorSetsPayload(Action action, String json)
        implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<ArmorSetsPayload> TYPE =
            new CustomPacketPayload.Type<>(
                    ResourceLocation.fromNamespaceAndPath("spacecatasb", "armor_sets")
            );

    public static final StreamCodec<RegistryFriendlyByteBuf, ArmorSetsPayload> CODEC =
            StreamCodec.composite(
                    Action.CODEC,
                    ArmorSetsPayload::action,
                    ByteBufCodecs.STRING_UTF8,
                    ArmorSetsPayload::json,
                    ArmorSetsPayload::new
            );

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    /* =========================
       ACTION ENUM + CODEC
       ========================= */
    public enum Action {
        ADD,
        RESET;

        public static final StreamCodec<RegistryFriendlyByteBuf, Action> CODEC =
                StreamCodec.of(
                        (buf, value) -> buf.writeEnum(value),
                        buf -> buf.readEnum(Action.class)
                );
    }

    public ArmorSet toArmorSet() {
        return ModUtils.gson.fromJson(json, ArmorSet.class);
    }
}
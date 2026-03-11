package uk.co.dotcode.asb;

import java.util.Iterator;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import uk.co.dotcode.asb.client.ModKeybinds;
import uk.co.dotcode.asb.config.ConfigHandler;
import uk.co.dotcode.asb.packet.ArmorSetsPayload;
import uk.co.dotcode.asb.packet.PacketHandler;

@Mod(ASB.MOD_ID)
public class ASB {

    public static final String MOD_ID = "spacecatasb";
    public static boolean isCuriosLoaded = false;
    public static boolean isTrinketsLoaded = false;

    public ASB(IEventBus modBus) {
        ConfigHandler.folder =
                FMLPaths.CONFIGDIR.get().resolve("spacecatasb").toFile();

        ConfigHandler.load();



        // Register key mappings
        modBus.addListener(uk.co.dotcode.asb.client.ModKeybinds::registerKeys);

        // Register networking
        modBus.addListener(uk.co.dotcode.asb.network.NetworkHandler::register);

        ModKeybinds.init();
    }

    @SubscribeEvent
    public void registerPackets(RegisterPayloadHandlersEvent event) {
        var registrar = event.registrar(ASB.MOD_ID);

        registrar.playToClient(
                ArmorSetsPayload.TYPE,
                ArmorSetsPayload.CODEC,
                (payload, context) ->
                        context.enqueueWork(() ->
                                PacketHandler.handlePayload(payload)
                        )
        );
    }

    public static void sendConfigIssues(Player player) {
        if (!ModLogger.getConfigIssues().isEmpty()) {
            Iterator<String> it = ModLogger.getConfigIssues().iterator();

            while (it.hasNext()) {
                String s = it.next();
                player.sendSystemMessage(
                        ComponentManager.createComponent("[Armor Set Bonuses] " + s, false)
                );
            }
        }
    }
}

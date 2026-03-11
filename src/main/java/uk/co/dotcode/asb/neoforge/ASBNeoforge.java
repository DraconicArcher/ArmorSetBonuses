package uk.co.dotcode.asb.neoforge;

import java.util.Objects;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforge.common.NeoForge;
import uk.co.dotcode.asb.ASB;
import uk.co.dotcode.asb.config.AdditionalSetPiece;
import uk.co.dotcode.asb.config.ConfigHandler;
import uk.co.dotcode.asb.packet.PacketRegistration;

@Mod("spacecatasb")
public class ASBNeoforge {
    public ASBNeoforge(IEventBus modBus) {
        ASB.isCuriosLoaded = ModList.get().isLoaded("curios");
        AdditionalSetPiece.additionalType = "Curios";
        ConfigHandler.folder = FMLPaths.CONFIGDIR.get().resolve("armorsets").toFile();
        EventHandler eventHandler = new EventHandler();
        Objects.requireNonNull(eventHandler);
        modBus.addListener(PacketRegistration::register);

        modBus.register(new ModLifecycleEvents());
        NeoForge.EVENT_BUS.register(new EventHandler());
    }

}
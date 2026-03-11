package uk.co.dotcode.asb.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.KeyMapping;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.common.NeoForge;
import org.lwjgl.glfw.GLFW;
import uk.co.dotcode.asb.network.ToggleKeyPayload;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.HashMap;
import java.util.Map;

public class ModKeybinds {

    public static final Map<String, KeyMapping> KEYMAP = new HashMap<>();

    public static void init() {
        if (FMLEnvironment.dist == Dist.CLIENT) {
            // Subscribe to client tick
            NeoForge.EVENT_BUS.addListener(ModKeybinds::onClientTick);
        }
    }

    public static void registerKeys(net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent event) {
        register(event, "key.asb.toggleN", GLFW.GLFW_KEY_N);
        register(event, "key.asb.toggleM", GLFW.GLFW_KEY_M);
    }

    private static void register(net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent event,
                                 String name, int key) {
        KeyMapping mapping = new KeyMapping(name, key, "key.categories.asb");
        event.register(mapping);
        KEYMAP.put(name, mapping);
    }

    // Each tick, check for key press and flip toggle on server
    private static void onClientTick(ClientTickEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;

        for (Map.Entry<String, KeyMapping> entry : KEYMAP.entrySet()) {
            String keyName = entry.getKey();
            KeyMapping mapping = entry.getValue();

            // Only trigger when pressed (not held)
            if (mapping.consumeClick()) {
                // Send packet to server to flip the toggle
                PacketDistributor.sendToServer(new ToggleKeyPayload(keyName));
            }
        }
    }
}
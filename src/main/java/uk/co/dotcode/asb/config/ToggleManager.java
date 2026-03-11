package uk.co.dotcode.asb.config;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class ToggleManager {

    // Stores persistent toggles per player
    private static final Map<UUID, Map<String, Boolean>> PLAYER_TOGGLES = new ConcurrentHashMap<>();

    // Flip toggle
    public static void toggle(ServerPlayer player, String keyName) {
        Map<String, Boolean> map = PLAYER_TOGGLES.computeIfAbsent(player.getUUID(), id -> new ConcurrentHashMap<>());
        map.put(keyName, !map.getOrDefault(keyName, false)); // flip state
    }

    // Query toggle
    public static boolean isActive(Player player, String keyName) {
        Map<String, Boolean> map = PLAYER_TOGGLES.get(player.getUUID());
        return map != null && map.getOrDefault(keyName, false);
    }

    public static void reset(Player player) {
        PLAYER_TOGGLES.remove(player.getUUID());
    }
}
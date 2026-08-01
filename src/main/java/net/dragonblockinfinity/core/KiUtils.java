package net.dragonblockinfinity.core;

import net.minecraft.world.entity.player.Player;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class KiUtils {
    private static final long DEFAULT_MAX_KI = 99_999_999_999_999_999L;
    private static final Map<UUID, Long> KI_VALUES = new ConcurrentHashMap<>();

    public static long getKi(Player player) {
        return KI_VALUES.getOrDefault(player.getUUID(), getMaxKi(player));
    }

    public static long getMaxKi(Player player) {
        return DEFAULT_MAX_KI;
    }

    public static void setKi(Player player, long value) {
        long clamped = Math.max(0L, Math.min(value, getMaxKi(player)));
        KI_VALUES.put(player.getUUID(), clamped);
    }

    public static boolean consumeKi(Player player, long amount) {
        long current = getKi(player);
        if (current < amount) return false;
        setKi(player, current - amount);
        return true;
    }

    public static void regenerateKi(Player player, long amountPerTick) {
        setKi(player, getKi(player) + amountPerTick);
    }
}

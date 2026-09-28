package com.craftycorvid.improvedSigns;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

/**
 * Tracks the sign item currently being placed on this thread, so the sign editor hook can tell a
 * placement apart from a player editing an existing sign. Since 26.3 sign items are plain
 * {@code StandingAndWallBlockItem}s, so there is no sign specific placement code to hook anymore.
 */
public final class SignPlacement {
    public record Placement(ItemStack stack, BlockPos pos) {
    }

    private static final ThreadLocal<Placement> CURRENT = new ThreadLocal<>();

    private SignPlacement() {}

    public static void begin(ItemStack stack, BlockPos pos) {
        CURRENT.set(new Placement(stack.copy(), pos));
    }

    public static void end() {
        CURRENT.remove();
    }

    public static @Nullable Placement current() {
        return CURRENT.get();
    }
}

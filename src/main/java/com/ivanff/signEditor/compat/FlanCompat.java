package com.ivanff.signEditor.compat;

import io.github.flemmli97.flan.api.ClaimHandler;
import io.github.flemmli97.flan.api.data.IPermissionContainer;
import io.github.flemmli97.flan.api.data.IPermissionStorage;
import io.github.flemmli97.flan.api.permission.ClaimPermission;
import io.github.flemmli97.flan.api.permission.PermissionRegistry;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class FlanCompat {
    public static final FabricLoader loader = FabricLoader.getInstance();

    public static boolean canInteract(ServerWorld world, ServerPlayerEntity player, BlockPos pos, ClaimPermission type) {
        IPermissionStorage storage = ClaimHandler.getPermissionStorage(world);
        IPermissionContainer container = storage.getForPermissionCheck(pos);

        return container.canInteract(player, type, pos);
    }

    public static ActionResult check(World world, PlayerEntity playerEntity, BlockPos pos) {
        if (loader.isModLoaded("flan") && world instanceof ServerWorld serverWorld && playerEntity instanceof ServerPlayerEntity serverPlayerEntity) {
            if (FlanCompat.canInteract(serverWorld, serverPlayerEntity, pos, PermissionRegistry.BREAK) && FlanCompat.canInteract(serverWorld, serverPlayerEntity, pos, PermissionRegistry.PLACE)) {
                return ActionResult.PASS;
            }
            return ActionResult.FAIL;
        }
        return ActionResult.PASS;
    }
}

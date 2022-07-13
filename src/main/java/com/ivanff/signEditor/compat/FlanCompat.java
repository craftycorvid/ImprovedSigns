package com.ivanff.signEditor.compat;

import com.ivanff.signEditor.SignEditorMod;
import io.github.flemmli97.flan.api.ClaimHandler;
import io.github.flemmli97.flan.api.data.IPermissionContainer;
import io.github.flemmli97.flan.api.data.IPermissionStorage;
import io.github.flemmli97.flan.api.permission.ClaimPermission;
import io.github.flemmli97.flan.api.permission.PermissionRegistry;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

public class FlanCompat {
    public static final FabricLoader loader = FabricLoader.getInstance();

    public static ClaimPermission EDITSIGN;

    public static void register() {
        if (loader.isModLoaded("flan")) {
            Identifier[] blocks = Registry.BLOCK.stream().filter(block -> block instanceof AbstractSignBlock).map(AbstractBlock::getLootTableId).map(block -> {
                SignEditorMod.LOGGER.info("Found Sign {}", block);
                return block;
            }).toArray(Identifier[]::new);

            EDITSIGN = PermissionRegistry.registerBlockInteract(
                    new ClaimPermission("EDITSIGN", () -> new ItemStack(Items.SPRUCE_SIGN), "Permission to edit signs"),
                    blocks
            );
        }
    }
    public static boolean canInteract(ServerWorld world, ServerPlayerEntity player, BlockPos pos, ClaimPermission type) {
        IPermissionStorage storage = ClaimHandler.getPermissionStorage(world);
        IPermissionContainer container = storage.getForPermissionCheck(pos);

        return container.canInteract(player, type, pos);
    }

    public static ActionResult check(World world, PlayerEntity playerEntity, BlockPos pos) {
        if (loader.isModLoaded("flan") && world instanceof ServerWorld serverWorld && playerEntity instanceof ServerPlayerEntity serverPlayerEntity) {
            if (FlanCompat.canInteract(serverWorld, serverPlayerEntity, pos, FlanCompat.EDITSIGN)) {
                return ActionResult.PASS;
            }
            return ActionResult.FAIL;
        }
        return ActionResult.PASS;
    }
}

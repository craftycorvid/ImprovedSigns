package com.ivanff.improvedSigns.compat;

import io.github.flemmli97.flan.api.ClaimHandler;
import io.github.flemmli97.flan.api.data.IPermissionContainer;
import io.github.flemmli97.flan.api.data.IPermissionStorage;
import io.github.flemmli97.flan.api.permission.ClaimPermission;
import io.github.flemmli97.flan.api.permission.ObjectToPermissionMap;
import io.github.flemmli97.flan.api.permission.PermissionRegistry;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class FlanCompat {
    public static final FabricLoader loader = FabricLoader.getInstance();

    public static ClaimPermission EDITSIGN;

    public static void register() {
        if (loader.isModLoaded("flan")) {
            EDITSIGN = PermissionRegistry.registerBlockInteract(new ClaimPermission("EDITSIGN", () -> new ItemStack(Items.SPRUCE_SIGN), "Permission to edit signs"));
            ObjectToPermissionMap.registerBlockPredicateMap(block -> block instanceof AbstractSignBlock, () -> EDITSIGN);
        }
    }
    public static boolean canInteract(ServerWorld world, ServerPlayerEntity player, BlockPos pos, ClaimPermission type) {
        IPermissionStorage storage = ClaimHandler.getPermissionStorage(world);
        IPermissionContainer container = storage.getForPermissionCheck(pos);

        return container.canInteract(player, type, pos);
    }

    public static ActionResult checkEdit(World world, PlayerEntity playerEntity, BlockPos pos) {
        if (loader.isModLoaded("flan") && world instanceof ServerWorld serverWorld && playerEntity instanceof ServerPlayerEntity serverPlayerEntity) {
            if (FlanCompat.canInteract(serverWorld, serverPlayerEntity, pos, FlanCompat.EDITSIGN)) {
                return ActionResult.PASS;
            }
            return ActionResult.FAIL;
        }
        return ActionResult.PASS;
    }

    public static ActionResult checkPassthrough(World world, PlayerEntity playerEntity, BlockPos pos) {
        if (loader.isModLoaded("flan") && world instanceof ServerWorld serverWorld && playerEntity instanceof ServerPlayerEntity serverPlayerEntity) {
            // Flan doesn't add OPENCONTAINER to it's ObjectToPermissionMap :(
            BlockEntity be = world.getBlockEntity(pos);
            if (be instanceof Inventory) {
                return FlanCompat.canInteract(serverWorld, serverPlayerEntity, pos, PermissionRegistry.OPENCONTAINER) ? ActionResult.PASS : ActionResult.FAIL;
            }

            BlockState bs = world.getBlockState(pos);
            ClaimPermission claimPermission = ObjectToPermissionMap.getFromBlock(bs.getBlock());
            if (claimPermission == null) return ActionResult.PASS;
            if (FlanCompat.canInteract(serverWorld, serverPlayerEntity, pos, claimPermission)) {
                return ActionResult.PASS;
            }
            return ActionResult.FAIL;
        }
        return ActionResult.PASS;
    }
}

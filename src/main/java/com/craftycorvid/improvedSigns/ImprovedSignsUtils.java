package com.craftycorvid.improvedSigns;

import java.util.Optional;

// import com.craftycorvid.improvedSigns.compat.FlanCompat;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ImprovedSignsUtils {
    public static ActionResult handlePassthrough(PlayerEntity player, World world, BlockPos pos,
            Direction oppositeDirection) {
        BlockPos hangingPos = pos.add(oppositeDirection.getOffsetX(), oppositeDirection.getOffsetY(),
                oppositeDirection.getOffsetZ());
         /* if (FlanCompat.checkPassthrough(world, player, hangingPos) == ActionResult.FAIL)
             return; */
        BlockState hangingState = world.getBlockState(hangingPos);
        Vec3d hanginPosVec3d = new Vec3d(hangingPos.getX(), hangingPos.getY(), hangingPos.getZ());
        BlockHitResult hangingHitResult = new BlockHitResult(hanginPosVec3d, oppositeDirection, hangingPos, false);
        return hangingState.onUse(world, player, hangingHitResult);
    }

    public static Optional<ItemStack> getItemHand(PlayerEntity player, Item item) {
        ItemStack mainHandItem = player.getEquippedStack(EquipmentSlot.MAINHAND);
        if (mainHandItem.isOf(item))
            return Optional.of(mainHandItem);
        return Optional.empty();
    }

    public static Optional<ItemStack> getSignHand(PlayerEntity player) {
        ItemStack mainHandItem = player.getEquippedStack(EquipmentSlot.MAINHAND);
        if (mainHandItem.getItem() instanceof SignItem)
            return Optional.of(mainHandItem);
        return Optional.empty();
    }
}

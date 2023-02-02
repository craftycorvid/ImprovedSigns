package com.ivanff.improvedSigns;

import java.util.Optional;

// import com.ivanff.improvedSigns.compat.FlanCompat;

import net.minecraft.block.BlockState;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.DecorationItem;
import net.minecraft.item.DyeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.SignItem;
// import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ImprovedSignsUtils {
    public static void handlePassthrough(PlayerEntity player, World world, Hand hand, BlockPos pos,
            Direction oppositeDirection) {
        BlockPos hangingPos = pos.add(oppositeDirection.getOffsetX(), oppositeDirection.getOffsetY(),
                oppositeDirection.getOffsetZ());
        // if (FlanCompat.checkPassthrough(world, player, hangingPos) == ActionResult.FAIL)
        //     return;
        BlockState hangingState = world.getBlockState(hangingPos);
        Vec3d hanginPosVec3d = new Vec3d(hangingPos.getX(), hangingPos.getY(), hangingPos.getZ());
        BlockHitResult hangingHitResult = new BlockHitResult(hanginPosVec3d, oppositeDirection, pos, false);
        hangingState.getBlock().onUse(hangingState, world, hangingPos, player, hand, hangingHitResult);
    }

    public static boolean hasEmptyHand(PlayerEntity player) {
        Item mainHandItem = player.getEquippedStack(EquipmentSlot.MAINHAND).getItem();
        Item offHandItem = player.getEquippedStack(EquipmentSlot.OFFHAND).getItem();
        return !(mainHandItem instanceof BlockItem || mainHandItem instanceof DecorationItem
                || offHandItem instanceof BlockItem || offHandItem instanceof DecorationItem);
    }

    public static Optional<ItemStack> geItemHand(PlayerEntity player, Item item) {
        ItemStack mainHandItem = player.getEquippedStack(EquipmentSlot.MAINHAND);
        ItemStack offHandItem = player.getEquippedStack(EquipmentSlot.OFFHAND);
        if (mainHandItem.isOf(item))
            return Optional.of(mainHandItem);
        if (offHandItem.isOf(item))
            return Optional.of(offHandItem);
        return Optional.empty();
    }

    public static Optional<ItemStack> getSignHand(PlayerEntity player) {
        ItemStack mainHandItem = player.getEquippedStack(EquipmentSlot.MAINHAND);
        ItemStack offHandItem = player.getEquippedStack(EquipmentSlot.OFFHAND);
        if (mainHandItem.getItem() instanceof SignItem)
            return Optional.of(mainHandItem);
        if (offHandItem.getItem() instanceof SignItem)
            return Optional.of(offHandItem);
        return Optional.empty();
    }

    public static boolean isHoldingDye(PlayerEntity player) {
        Item mainHandItem = player.getEquippedStack(EquipmentSlot.MAINHAND).getItem();
        return mainHandItem instanceof DyeItem || mainHandItem == Items.GLOW_INK_SAC;
    }
}

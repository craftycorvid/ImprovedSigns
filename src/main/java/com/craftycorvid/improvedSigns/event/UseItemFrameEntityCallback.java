package com.craftycorvid.improvedSigns.event;

import java.util.Optional;

import com.craftycorvid.improvedSigns.ImprovedSignsUtils;
import static com.craftycorvid.improvedSigns.ImprovedSignsMod.MOD_CONFIG;

import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class UseItemFrameEntityCallback {
    public static ActionResult onUseItemFrameEntityCallback(PlayerEntity player, World world,
            Hand hand, Entity entity, EntityHitResult hitResult) {
        if (!(world instanceof net.minecraft.server.world.ServerWorld))
            return ActionResult.PASS;
        if (!(entity instanceof ItemFrameEntity frameEntity))
            return ActionResult.PASS;

        if (!hand.equals(Hand.MAIN_HAND))
            return ActionResult.FAIL;

        if (MOD_CONFIG.enableInvisibleFrames && player.isSneaking()) {
            Item item;
            switch (MOD_CONFIG.invisibleFrameItem) {
                case GLASS_PANE:
                    item = Items.GLASS_PANE;
                    break;
                default:
                case AMETHYST_SHARD:
                    item = Items.AMETHYST_SHARD;
                    break;
            }

            Optional<ItemStack> itemOption = ImprovedSignsUtils.getItemHand(player, item);
            if (itemOption.isPresent()) {
                if (entity.isInvisible()) {
                    return ActionResult.FAIL;
                }
                if (frameEntity.getHeldItemStack().isOf(Items.AIR)) {
                    return ActionResult.PASS;
                }
                itemOption.get().decrementUnlessCreative(1, player);
                entity.setInvisible(true);
                return ActionResult.SUCCESS;
            }
        }

        if (MOD_CONFIG.enableFramePassthrough && !player.isSneaking()) {
            BlockPos pos = entity.getBlockPos();
            Direction oppositeDirection = entity.getHorizontalFacing().getOpposite();
            return ImprovedSignsUtils.handlePassthrough(player, world, pos, oppositeDirection);
        }

        return ActionResult.PASS;
    }
}

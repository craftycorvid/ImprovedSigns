package com.ivanff.improvedSigns.event;

import java.lang.reflect.Field;
import java.util.Optional;

import com.ivanff.improvedSigns.ImprovedSignsUtils;
import com.ivanff.improvedSigns.compat.FlanCompat;
import com.ivanff.improvedSigns.config.ModConfig;

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
    public static ActionResult onUseItemFrameEntityCallback(PlayerEntity player, World world, Hand hand, Entity entity, EntityHitResult hitResult) {
        if (world.isClient)
            return ActionResult.PASS;
        if (!(entity instanceof ItemFrameEntity frameEntity))
            return ActionResult.PASS;

        if (!hand.equals(Hand.MAIN_HAND))
            return ActionResult.FAIL;

        if (player.isSneaking() && ModConfig.get().enableInvisibleFrames && FlanCompat.checkEdit(world, player, entity.getBlockPos()) != ActionResult.FAIL) {
            Item item;
            try {
                Field itemField = Items.class.getField(ModConfig.get().invisibleFrameItem);
                item = (Item) itemField.get(null);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                item = Items.AMETHYST_SHARD;
            }

            Optional<ItemStack> itemOption = ImprovedSignsUtils.geItemHand(player, item);
            if (itemOption.isPresent()) {
                if (entity.isInvisible()) {
                    return ActionResult.FAIL;
                }
                if (frameEntity.getHeldItemStack().isOf(Items.AIR)) {
                    return ActionResult.PASS;
                }
                if (!player.getAbilities().creativeMode) {
                    ItemStack itemStack = itemOption.get();
                    itemStack.decrement(1);
                }
                entity.setInvisible(true);
                return ActionResult.SUCCESS;
            }
        }

        if (ModConfig.get().enableFramePassthrough && !player.isSneaking()) {
            BlockPos pos = entity.getBlockPos();
            Direction oppositeDirection = entity.getHorizontalFacing().getOpposite();
            ImprovedSignsUtils.handlePassthrough(player, world, hand, pos, oppositeDirection);
            return ActionResult.SUCCESS;
        }

        return ActionResult.PASS;
    }
}

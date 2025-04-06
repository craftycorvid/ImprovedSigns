package com.craftycorvid.improvedSigns.event;

import java.util.Optional;

import com.craftycorvid.improvedSigns.ImprovedSignsUtils;
import com.craftycorvid.improvedSigns.config.ModConfig;

import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.block.entity.SignText;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.DecorationItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SignChangingItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtOps;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class UseSignBlockCallback {
    public static ActionResult onUseSignBlockCallback(PlayerEntity player, World world, Hand hand,
            BlockHitResult hitResult) {
        if (world.isClient)
            return ActionResult.PASS;
        BlockPos pos = hitResult.getBlockPos();
        BlockEntity blockEntity = world.getBlockEntity(pos);
        BlockState blockState = world.getBlockState(pos);
        if (!(blockEntity instanceof SignBlockEntity signBlockEntity))
            return ActionResult.PASS;

        if (hand.equals(Hand.OFF_HAND))
            return ActionResult.PASS;

        if (!player.isSneaking()) {
            Optional<ItemStack> signHand = ImprovedSignsUtils.getSignHand(player);
            if (ModConfig.enableSignCopy && signHand.isPresent()) {
                ItemStack sign = signHand.get();
                NbtCompound nbt =
                        sign.getOrDefault(DataComponentTypes.CUSTOM_DATA, NbtComponent.DEFAULT)
                                .copyNbt();
                NbtCompound blockEntityTag = nbt.getCompoundOrEmpty("BlockEntityTag");
                SignText frontText = signBlockEntity.getFrontText();
                SignText.CODEC.encodeStart(NbtOps.INSTANCE, frontText).result()
                        .ifPresent(textNbt -> {
                            NbtCompound text = (NbtCompound) textNbt;
                            if (!ModConfig.retainDyeOnSignCopy) {
                                text.putBoolean("has_glowing_text", false);
                                text.putInt("color", DyeColor.BLACK.getSignColor());
                            }
                            blockEntityTag.put("front_text", text);
                        });
                SignText backText = signBlockEntity.getBackText();
                SignText.CODEC.encodeStart(NbtOps.INSTANCE, backText).result()
                        .ifPresent(textNbt -> {
                            NbtCompound text = (NbtCompound) textNbt;
                            if (!ModConfig.retainDyeOnSignCopy) {
                                text.putBoolean("has_glowing_text", false);
                                text.putInt("color", DyeColor.BLACK.getSignColor());
                            }
                            blockEntityTag.put("back_text", text);
                        });
                blockEntityTag.putBoolean("is_waxed", signBlockEntity.isWaxed());
                nbt.put("BlockEntityTag", blockEntityTag);
                sign.set(DataComponentTypes.CUSTOM_DATA, NbtComponent.of(nbt));
                player.sendMessage(
                        Text.literal("Sign text copied to " + sign.getCount() + " signs"), true);
                ImprovedSignsUtils.appendSignTooltip(sign);
                return ActionResult.SUCCESS;
            }

            if (ModConfig.enableSignPassthrough) {
                BlockState state = world.getBlockState(pos);
                if (state.contains(HorizontalFacingBlock.FACING)) {
                    Direction oppositeDirection =
                            state.get(HorizontalFacingBlock.FACING).getOpposite();
                    return ImprovedSignsUtils.handlePassthrough(player, world, pos,
                            oppositeDirection);
                }
            }

            return ActionResult.PASS;
        }

        if (ModConfig.enableSignPassthrough) {
            ItemStack handItemStack = player.getStackInHand(Hand.MAIN_HAND);
            Item handItem = handItemStack.getItem();
            if (handItem instanceof SignChangingItem) {
                return blockState.onUseWithItem(handItemStack, world, player, hand, hitResult);
            }

            Item offhandItem = player.getStackInHand(Hand.OFF_HAND).getItem();
            if (!(handItem instanceof BlockItem || handItem instanceof DecorationItem
                    || offhandItem instanceof BlockItem || offhandItem instanceof DecorationItem)) {
                return blockState.onUse(world, player, hitResult);
            }
        }

        return ActionResult.PASS;

    }
}

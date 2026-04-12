package com.craftycorvid.improvedSigns.event;

import java.util.Optional;

import com.craftycorvid.improvedSigns.ImprovedSignsUtils;
import static com.craftycorvid.improvedSigns.ImprovedSignsMod.MOD_CONFIG;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.HangingEntityItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SignApplicator;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.entity.SignText;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class UseSignBlockCallback {
    public static InteractionResult onUseSignBlockCallback(Player player, Level world, InteractionHand hand,
            BlockHitResult hitResult) {
        if (!(world instanceof net.minecraft.server.level.ServerLevel))
            return InteractionResult.PASS;
        BlockPos pos = hitResult.getBlockPos();
        BlockEntity blockEntity = world.getBlockEntity(pos);
        BlockState blockState = world.getBlockState(pos);
        if (!(blockEntity instanceof SignBlockEntity signBlockEntity))
            return InteractionResult.PASS;

        if (hand.equals(InteractionHand.OFF_HAND))
            return InteractionResult.PASS;

        if (!player.isShiftKeyDown()) {
            Optional<ItemStack> signHand = ImprovedSignsUtils.getSignHand(player);
            if (MOD_CONFIG.enableSignCopy && signHand.isPresent()) {
                ItemStack sign = signHand.get();
                CompoundTag nbt =
                        sign.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY)
                                .copyTag();
                CompoundTag blockEntityTag = nbt.getCompoundOrEmpty("BlockEntityTag");
                SignText frontText = signBlockEntity.getFrontText();
                SignText.DIRECT_CODEC.encodeStart(NbtOps.INSTANCE, frontText).result()
                        .ifPresent(textNbt -> {
                            CompoundTag text = (CompoundTag) textNbt;
                            if (!MOD_CONFIG.retainDyeOnSignCopy) {
                                text.putBoolean("has_glowing_text", false);
                                text.putInt("color", DyeColor.BLACK.getTextColor());
                            }
                            blockEntityTag.put("front_text", text);
                        });
                SignText backText = signBlockEntity.getBackText();
                SignText.DIRECT_CODEC.encodeStart(NbtOps.INSTANCE, backText).result()
                        .ifPresent(textNbt -> {
                            CompoundTag text = (CompoundTag) textNbt;
                            if (!MOD_CONFIG.retainDyeOnSignCopy) {
                                text.putBoolean("has_glowing_text", false);
                                text.putInt("color", DyeColor.BLACK.getTextColor());
                            }
                            blockEntityTag.put("back_text", text);
                        });
                blockEntityTag.putBoolean("is_waxed", signBlockEntity.isWaxed());
                nbt.put("BlockEntityTag", blockEntityTag);
                sign.set(DataComponents.CUSTOM_DATA, CustomData.of(nbt));
                player.sendSystemMessage(
                        Component.literal("Sign text copied to " + sign.getCount() + " signs"));
                ImprovedSignsUtils.appendSignTooltip(sign);
                return InteractionResult.SUCCESS;
            }

            if (MOD_CONFIG.enableSignPassthrough) {
                BlockState state = world.getBlockState(pos);
                if (state.hasProperty(HorizontalDirectionalBlock.FACING)) {
                    Direction oppositeDirection =
                            state.getValue(HorizontalDirectionalBlock.FACING).getOpposite();
                    return ImprovedSignsUtils.handlePassthrough(player, world, pos,
                            oppositeDirection);
                }
            }

            return InteractionResult.PASS;
        }

        if (MOD_CONFIG.enableSignPassthrough) {
            ItemStack handItemStack = player.getItemInHand(InteractionHand.MAIN_HAND);
            Item handItem = handItemStack.getItem();
            if (handItem instanceof SignApplicator) {
                return blockState.useItemOn(handItemStack, world, player, hand, hitResult);
            }

            Item offhandItem = player.getItemInHand(InteractionHand.OFF_HAND).getItem();
            if (!(handItem instanceof BlockItem || handItem instanceof HangingEntityItem
                    || offhandItem instanceof BlockItem || offhandItem instanceof HangingEntityItem)) {
                return blockState.useWithoutItem(world, player, hitResult);
            }
        }

        return InteractionResult.PASS;

    }
}

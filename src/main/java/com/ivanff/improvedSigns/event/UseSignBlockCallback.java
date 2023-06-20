package com.ivanff.improvedSigns.event;

import java.util.Optional;
import java.util.UUID;

import com.ivanff.improvedSigns.ImprovedSignsUtils;
import com.ivanff.improvedSigns.compat.FlanCompat;
import com.ivanff.improvedSigns.config.ModConfig;

import net.minecraft.block.AbstractSignBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.block.entity.SignText;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SignChangingItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtOps;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
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
        if (!(blockEntity instanceof SignBlockEntity signBlockEntity))
            return ActionResult.PASS;

        if (hand.equals(Hand.OFF_HAND)) return ActionResult.PASS;

        boolean front = signBlockEntity.isPlayerFacingFront(player);
        SignText signText = signBlockEntity.getText(front);

        if(!player.isSneaking()) {
            Optional<ItemStack> signHand = ImprovedSignsUtils.getSignHand(player);
            if (ModConfig.get().enableSignCopy && signHand.isPresent()) {
                ItemStack sign = signHand.get();
                NbtCompound nbt = sign.getOrCreateNbt();
                NbtCompound blockEntityTag = nbt.getCompound("BlockEntityTag");
                SignText.CODEC.encodeStart(NbtOps.INSTANCE, signText).result().ifPresent(textNbt -> {
                    NbtCompound text = (NbtCompound) textNbt;
                    if (!ModConfig.get().retainDyeOnSignCopy) {
                        text.putBoolean("GlowingText", false);
                        text.putString("Color", "black");
                    }
                    blockEntityTag.put(front ? "front_text" : "back_text", text);
                    nbt.put("BlockEntityTag", blockEntityTag);
                    sign.setNbt(nbt);
                    player.sendMessage(Text.literal("Sign text copied to " + sign.getCount() + " signs"), true);
                });
                return ActionResult.SUCCESS;
            }

            BlockState state = world.getBlockState(pos);
            if (state.contains(HorizontalFacingBlock.FACING)) {
                Direction oppositeDirection = state.get(HorizontalFacingBlock.FACING).getOpposite();
                ImprovedSignsUtils.handlePassthrough(player, world, hand, pos, oppositeDirection);
            }
            return ActionResult.SUCCESS;
        }

        BlockState bs = world.getBlockState(pos);
        if (player.getStackInHand(Hand.MAIN_HAND).getItem() instanceof SignChangingItem signChangingItem && signChangingItem.canUseOnSignText(signText, player) && signChangingItem.useOnSign(world, signBlockEntity, front, player)) {
            return ActionResult.SUCCESS;
        }

        if (ImprovedSignsUtils.hasEmptyHand(player)) {
            if (!ModConfig.get().enableSignEdit || FlanCompat.checkEdit(world, player, pos) == ActionResult.FAIL || signBlockEntity.isWaxed()) {
                world.playSound(player, signBlockEntity.getPos(), SoundEvents.BLOCK_SIGN_WAXED_INTERACT_FAIL, SoundCategory.BLOCKS);
                return ActionResult.FAIL;
            }

            UUID uuid = signBlockEntity.getEditor();
            if (world.getBlockState(pos).getBlock() instanceof AbstractSignBlock signBlock && (uuid == null || uuid.equals(player.getUuid()))) {
                signBlock.openEditScreen(player, signBlockEntity, front);
                return ActionResult.SUCCESS;
            }
        }

        return ActionResult.PASS;
    }
}

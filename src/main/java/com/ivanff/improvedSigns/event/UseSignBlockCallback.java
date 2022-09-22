package com.ivanff.improvedSigns.event;

import java.util.Optional;

import com.ivanff.improvedSigns.ImprovedSignsUtils;
import com.ivanff.improvedSigns.compat.FlanCompat;
import com.ivanff.improvedSigns.config.ModConfig;
import com.ivanff.improvedSigns.mixin.SignEntityMixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
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
        if (!(blockEntity instanceof SignBlockEntity))
            return ActionResult.PASS;
        if (player.isSneaking()) {
            if (ImprovedSignsUtils.hasEmptyHand(player)) {
                if (!(ModConfig.get().enableSignEdit)
                        || FlanCompat.checkEdit(world, player, pos) == ActionResult.FAIL) {
                    player.sendMessage(Text.literal("Sign is not editable"), true);

                    return ActionResult.PASS;
                }
                SignBlockEntity signBlock = (SignBlockEntity) blockEntity;
                ((SignEntityMixin) signBlock).setSignEditable(true);
                if (signBlock.isEditable()) {
                    player.openEditSignScreen(signBlock);
                } else {
                    player.sendMessage(Text.literal("Sign is not editable"), true);
                }
            }
        } else {
            Optional<ItemStack> signOption = ImprovedSignsUtils.getSignHand(player);
            if (ModConfig.get().enableSignCopy && signOption.isPresent()) {
                ItemStack sign = signOption.get();
                NbtCompound nbt = sign.getOrCreateNbt();
                NbtCompound blockEntityTag = nbt.getCompound("BlockEntityTag");
                SignBlockEntity signBlock = (SignBlockEntity) blockEntity;
                for (int i = 0; i < 4; i++) {
                    Text text = signBlock.getTextOnRow(i, false);
                    String string = Text.Serializer.toJson(text);
                    blockEntityTag.putString(String.format("Text%d", i + 1), string);
                }
                nbt.put("BlockEntityTag", blockEntityTag);
                sign.setNbt(nbt);
                player.sendMessage(Text.literal("Sign text copied to " + sign.getCount() + " signs"), true);
            } else if (ModConfig.get().enableSignPassthrough && !ImprovedSignsUtils.isHoldingDye(player)) {
                BlockState state = world.getBlockState(pos);
                if (state.contains(HorizontalFacingBlock.FACING)) {
                    Direction oppositeDirection = state.get(HorizontalFacingBlock.FACING).getOpposite();
                    ImprovedSignsUtils.handlePassthrough(player, world, hand, pos, oppositeDirection);
                }
                return ActionResult.SUCCESS;
            }
        }
        return ActionResult.PASS;
    }
}

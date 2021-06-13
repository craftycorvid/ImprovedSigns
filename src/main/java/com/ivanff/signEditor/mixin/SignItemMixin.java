package com.ivanff.signEditor.mixin;

import java.util.List;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SignItem;
import net.minecraft.item.WallStandingBlockItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@Mixin(SignItem.class)
public class SignItemMixin extends WallStandingBlockItem {
    public SignItemMixin(Block standingBlock, Block wallBlock, Settings settings) {
        super(standingBlock, wallBlock, settings);
    }

    @Inject(method = "postPlacement", at = @At(value = "INVOKE",  target = "Lnet/minecraft/entity/player/PlayerEntity;openEditSignScreen(Lnet/minecraft/block/entity/SignBlockEntity;)V"), cancellable = true)
    protected void postPlacement(BlockPos pos, World world, @Nullable PlayerEntity player, ItemStack stack, BlockState state, CallbackInfoReturnable<Boolean> info){
        NbtCompound compoundTag = stack.getTag();
        if (compoundTag != null && compoundTag.contains("Retained")) {
            info.cancel();
        }
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);
        NbtCompound compoundTag = stack.getTag();
        if (compoundTag != null && compoundTag.contains("Retained")) {
            NbtCompound compoundTag2 = compoundTag.getCompound("Retained");
            for(int i = 0; i < 4; ++i) {
                String string = compoundTag2.getString("Text" + (i + 1));
                Text text = Text.Serializer.fromJson(string.isEmpty() ? "\"\"" : string);
                tooltip.add(text);
            }
        }
    }

    @Inject(
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/player/PlayerEntity;openEditSignScreen(Lnet/minecraft/block/entity/SignBlockEntity;)V"
        ),
        method = "postPlacement",
        cancellable = true)
    private void onPlacement(final BlockPos pos, final World world, final PlayerEntity player, final ItemStack stack, final BlockState state, final CallbackInfoReturnable<Boolean> info) {
        info.cancel();
    }
}
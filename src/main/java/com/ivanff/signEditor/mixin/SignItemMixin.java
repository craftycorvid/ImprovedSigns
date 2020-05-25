package com.ivanff.signEditor.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SignItem;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@Mixin(SignItem.class)
public abstract class SignItemMixin {
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
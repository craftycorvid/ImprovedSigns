package com.ivanff.signEditor.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;

@Mixin(ItemFrameEntity.class)
public abstract class ItemFrameEntityMixin {
    @Inject(at = @At("HEAD"), method = "interact", cancellable = true)
    void onSetRotation(final PlayerEntity player, final Hand hand, final CallbackInfoReturnable<ActionResult> info) {
        if (!player.isSneaking()) {
            info.setReturnValue(ActionResult.FAIL);
        }
    }
}
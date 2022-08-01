package com.ivanff.improvedSigns.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.decoration.AbstractDecorationEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.ivanff.improvedSigns.config.ModConfig;

import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;

@Mixin(ItemFrameEntity.class)
public abstract class ItemFrameEntityMixin extends AbstractDecorationEntity {
    protected ItemFrameEntityMixin(EntityType<? extends AbstractDecorationEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(at = @At("HEAD"), method = "interact", cancellable = true)
    void onSetRotation(final PlayerEntity player, final Hand hand, final CallbackInfoReturnable<ActionResult> info) {
        if (ModConfig.get().enableSignPassthrough && !player.isSneaking()) {
            info.setReturnValue(ActionResult.FAIL);
        }
    }

    @Inject(at = @At("HEAD"), method = "removeFromFrame")
    void onRemoveFrame(ItemStack itemStack, CallbackInfo ci) {
        this.setInvisible(false);
    }
}

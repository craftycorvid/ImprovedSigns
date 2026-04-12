package com.craftycorvid.improvedSigns.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static com.craftycorvid.improvedSigns.ImprovedSignsMod.MOD_CONFIG;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.HangingEntity;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

@Mixin(ItemFrame.class)
public abstract class ItemFrameEntityMixin extends HangingEntity {
    protected ItemFrameEntityMixin(EntityType<? extends HangingEntity> entityType,
            Level world) {
        super(entityType, world);
    }

    @Inject(at = @At("HEAD"), method = "interact", cancellable = true)
    void onSetRotation(final Player player, final InteractionHand hand, final Vec3 location,
            final CallbackInfoReturnable<InteractionResult> info) {
        if (MOD_CONFIG.enableFramePassthrough && !player.isShiftKeyDown()) {
            info.setReturnValue(InteractionResult.FAIL);
        }
    }

    @Inject(at = @At("HEAD"), method = "removeFramedMap")
    void onRemoveFrame(ItemStack itemStack, CallbackInfo ci) {
        this.setInvisible(false);
    }
}

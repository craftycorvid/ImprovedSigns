package com.craftycorvid.improvedSigns.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static com.craftycorvid.improvedSigns.ImprovedSignsMod.MOD_CONFIG;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.HangingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

@Mixin(net.minecraft.world.entity.decoration.ItemFrame.class)
public abstract class ItemFrameEntityMixin extends HangingEntity {
    protected ItemFrameEntityMixin(EntityType<? extends HangingEntity> entityType, Level world) {
        super(entityType, world);
    }

    @Inject(at = @At("HEAD"), method = "interact", cancellable = true)
    void onSetRotation(final Player player, final InteractionHand hand,
            final CallbackInfoReturnable<InteractionResult> info) {
        if (MOD_CONFIG.enableFramePassthrough && !player.isShiftKeyDown()) {
            info.setReturnValue(InteractionResult.FAIL);
        }
    }

    @Inject(at = @At("HEAD"), method = "removeFramedMap")
    void onRemoveFrame(ItemStack itemStack, CallbackInfo ci) {
        this.setInvisible(false);
    }

    // survives() pops the frame when anything collides with it. Let it keep hanging as long as the
    // block sharing its space isn't a full block, so trapdoors, slabs, buttons etc. can cover it.
    @Redirect(at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/level/Level;noCollision(Lnet/minecraft/world/entity/Entity;)Z"),
            method = "survives")
    boolean onCheckLevelCollision(final Level level, final Entity entity) {
        if (level.noCollision(entity))
            return true;

        AABB popBox = entity.getBoundingBox();
        if (!MOD_CONFIG.enableFrameBlockSharing || !level.getWorldBorder().isWithinBounds(popBox))
            return false;

        return BlockPos.betweenClosedStream(popBox.deflate(1.0E-7))
                .noneMatch(pos -> level.getBlockState(pos).isCollisionShapeFullBlock(level, pos));
    }
}

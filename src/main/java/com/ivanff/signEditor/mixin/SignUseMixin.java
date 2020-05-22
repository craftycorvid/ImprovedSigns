package com.ivanff.signEditor.mixin;

import net.minecraft.block.AbstractSignBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import com.ivanff.signEditor.SignEditorCallback;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractSignBlock.class)
public class SignUseMixin {
	@Inject(at = @At("HEAD"), method = "onUse")
	private void onSignUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit, final CallbackInfoReturnable<ActionResult> info) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity.getType() == BlockEntityType.SIGN) {
			SignBlockEntity signBlock = (SignBlockEntity) blockEntity;
			SignEditorCallback.EVENT.invoker().usedSign(player, signBlock);
		}
	}
}

package com.craftycorvid.improvedSigns.mixin;

import com.craftycorvid.improvedSigns.ImprovedSignsUtils;
import com.craftycorvid.improvedSigns.SignPlacement;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SignBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockItem.class)
public abstract class BlockItemMixin {
    @Shadow
    public abstract Block getBlock();

    // Vanilla applies the sign_text_front/sign_text_back/waxed components to the placed sign
    // itself. Move any text stored by older versions of this mod into those components first, and
    // remember the placement so ServerPlayerMixin can decide whether to open the sign editor.
    @Inject(method = "place(Lnet/minecraft/world/item/context/BlockPlaceContext;)Lnet/minecraft/world/InteractionResult;",
            at = @At("HEAD"))
    private void improvedSigns$beforePlace(BlockPlaceContext context,
            CallbackInfoReturnable<InteractionResult> info) {
        if (!(getBlock() instanceof SignBlock))
            return;
        ItemStack stack = context.getItemInHand();
        ImprovedSignsUtils.migrateLegacySignData(stack);
        SignPlacement.begin(stack, context.getClickedPos());
    }

    @Inject(method = "place(Lnet/minecraft/world/item/context/BlockPlaceContext;)Lnet/minecraft/world/InteractionResult;",
            at = @At("RETURN"))
    private void improvedSigns$afterPlace(BlockPlaceContext context,
            CallbackInfoReturnable<InteractionResult> info) {
        if (getBlock() instanceof SignBlock)
            SignPlacement.end();
    }
}

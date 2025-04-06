package com.craftycorvid.improvedSigns.mixin;

import net.minecraft.block.Block;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SignItem;
import net.minecraft.world.World;
import java.util.function.Supplier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import com.craftycorvid.improvedSigns.ImprovedSignsUtils;

@Mixin(Block.class)
public abstract class BlockMixin {
    @Inject(method = "dropStack(Lnet/minecraft/world/World;Ljava/util/function/Supplier;Lnet/minecraft/item/ItemStack;)V",
            at = @At("TAIL"))
    private static void postBreak(final World world, final Supplier<ItemEntity> itemEntitySupplier,
            final ItemStack stack, final CallbackInfo info) {
        if ((stack.getItem() instanceof SignItem)) {
            ImprovedSignsUtils.appendSignTooltip(stack);
        }
    }
}
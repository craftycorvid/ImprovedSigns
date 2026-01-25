package com.craftycorvid.improvedSigns.mixin;

import java.util.function.Supplier;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SignItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import com.craftycorvid.improvedSigns.ImprovedSignsUtils;

@Mixin(Block.class)
public abstract class BlockMixin {
    @Inject(method = "popResource(Lnet/minecraft/world/level/Level;Ljava/util/function/Supplier;Lnet/minecraft/world/item/ItemStack;)V",
            at = @At("TAIL"))
    private static void postBreak(final Level world, final Supplier<ItemEntity> itemEntitySupplier,
            final ItemStack stack, final CallbackInfo info) {
        if ((stack.getItem() instanceof SignItem)) {
            ImprovedSignsUtils.appendSignTooltip(stack);
        }
    }
}
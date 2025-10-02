package com.craftycorvid.improvedSigns.mixin;

import java.util.Optional;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static com.craftycorvid.improvedSigns.ImprovedSignsMod.MOD_CONFIG;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SignItem;
import net.minecraft.item.VerticallyAttachableBlockItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;


@Mixin(SignItem.class)
public class SignItemMixin extends VerticallyAttachableBlockItem {
    public SignItemMixin(Block standingBlock, Block wallBlock,
            Direction verticalAttachmentDirection, net.minecraft.item.Item.Settings settings) {
        super(standingBlock, wallBlock, verticalAttachmentDirection, settings);
    }

    @Inject(method = "postPlacement", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/block/AbstractSignBlock;openEditScreen(Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/block/entity/SignBlockEntity;Z)V"),
            cancellable = true)
    protected void postPlacement(BlockPos pos, World world, @Nullable PlayerEntity player,
            ItemStack stack, BlockState state, CallbackInfoReturnable<Boolean> info) {
        Optional<NbtCompound> optNbtCompound =
                stack.getOrDefault(DataComponentTypes.CUSTOM_DATA, NbtComponent.DEFAULT).copyNbt()
                        .getCompound("BlockEntityTag");
        if (optNbtCompound.isPresent()) {
            info.cancel();
        }
    }

    @Inject(at = @At(value = "INVOKE",
            target = "Lnet/minecraft/block/AbstractSignBlock;openEditScreen(Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/block/entity/SignBlockEntity;Z)V"),
            method = "postPlacement", cancellable = true)
    private void onPlacement(final BlockPos pos, final World world, final PlayerEntity player,
            final ItemStack stack, final BlockState state,
            final CallbackInfoReturnable<Boolean> info) {
        if (MOD_CONFIG.disableSignEditOnPlace)
            info.cancel();
    }
}

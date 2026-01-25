package com.craftycorvid.improvedSigns.mixin;

import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SignItem;
import net.minecraft.world.item.StandingAndWallBlockItem;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static com.craftycorvid.improvedSigns.ImprovedSignsMod.MOD_CONFIG;


@Mixin(SignItem.class)
public class SignItemMixin extends StandingAndWallBlockItem {
    public SignItemMixin(Block standingBlock, Block wallBlock,
            Direction verticalAttachmentDirection, net.minecraft.world.item.Item.Properties settings) {
        super(standingBlock, wallBlock, verticalAttachmentDirection, settings);
    }

    @Inject(method = "updateCustomBlockEntityTag(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/level/block/state/BlockState;)Z", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/level/block/SignBlock;openTextEdit(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/level/block/entity/SignBlockEntity;Z)V"),
            cancellable = true)
    protected void postPlacement(BlockPos pos, Level world, @Nullable Player player,
            ItemStack stack, BlockState state, CallbackInfoReturnable<Boolean> info) {
        Optional<CompoundTag> optNbtCompound =
                stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag()
                        .getCompound("BlockEntityTag");
        if (optNbtCompound.isPresent()) {
            info.cancel();
        }
    }

    @Inject(at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/level/block/SignBlock;openTextEdit(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/level/block/entity/SignBlockEntity;Z)V"),
            method = "updateCustomBlockEntityTag(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/level/block/state/BlockState;)Z", cancellable = true)
    private void onPlacement(final BlockPos pos, final Level world, final Player player,
            final ItemStack stack, final BlockState state,
            final CallbackInfoReturnable<Boolean> info) {
        if (MOD_CONFIG.disableSignEditOnPlace)
            info.cancel();
    }
}

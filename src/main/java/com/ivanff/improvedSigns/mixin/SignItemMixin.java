package com.ivanff.improvedSigns.mixin;

import java.util.List;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.ivanff.improvedSigns.config.ModConfig;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SignItem;
import net.minecraft.item.VerticallyAttachableBlockItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

@Mixin(SignItem.class)
public class SignItemMixin extends VerticallyAttachableBlockItem {
    public SignItemMixin(Block standingBlock, Block wallBlock, Settings settings, Direction verticalAttachmentDirection) {
        super(standingBlock, wallBlock, settings, verticalAttachmentDirection);
    }

    @Inject(method = "postPlacement", at = @At(value = "INVOKE",  target = "Lnet/minecraft/block/AbstractSignBlock;openEditScreen(Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/block/entity/SignBlockEntity;Z)V"), cancellable = true)
    protected void postPlacement(BlockPos pos, World world, @Nullable PlayerEntity player, ItemStack stack, BlockState state, CallbackInfoReturnable<Boolean> info){
        NbtCompound compoundTag = stack.getNbt();
        if (compoundTag != null && compoundTag.contains("BlockEntityTag")) {
            info.cancel();
        }
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);
        NbtCompound compoundTag = stack.getNbt();
        if (compoundTag != null && compoundTag.contains("BlockEntityTag")) {
            NbtCompound compoundTag2 = compoundTag.getCompound("BlockEntityTag");
            for(int i = 0; i < 4; ++i) {
                String string = compoundTag2.getString("Text" + (i + 1));
                Text text = Text.Serializer.fromJson(string.isEmpty() ? "\"\"" : string);
                tooltip.add(text);
            }
        }
    }

    @Inject(
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/block/AbstractSignBlock;openEditScreen(Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/block/entity/SignBlockEntity;Z)V"
        ),
        method = "postPlacement",
        cancellable = true)
    private void onPlacement(final BlockPos pos, final World world, final PlayerEntity player, final ItemStack stack, final BlockState state, final CallbackInfoReturnable<Boolean> info) {
        if (ModConfig.get().disableSignEditOnPlace) info.cancel();
    }
}
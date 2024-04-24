package com.craftycorvid.improvedSigns.mixin;

import java.util.List;
import java.util.Optional;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.craftycorvid.improvedSigns.config.ModConfig;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.SignText;
import net.minecraft.client.item.TooltipType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SignItem;
import net.minecraft.item.VerticallyAttachableBlockItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtOps;
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
        NbtComponent nbtComponent = stack.get(DataComponentTypes.CUSTOM_DATA);
        if (nbtComponent != null && nbtComponent.contains("BlockEntityTag")) {
            info.cancel();
        }
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        super.appendTooltip(stack, context, tooltip, type);
        NbtComponent nbtComponent = stack.get(DataComponentTypes.CUSTOM_DATA);
        if (nbtComponent != null && nbtComponent.contains("BlockEntityTag")) {
            NbtCompound nbtCompound = nbtComponent.copyNbt().getCompound("BlockEntityTag");
            Optional<Text[]> front = Optional.empty();
            Optional<Text[]> back = Optional.empty();

            if (nbtCompound.contains("front_text")) {
                front = SignText.CODEC
                        .parse(NbtOps.INSTANCE, nbtCompound.getCompound("front_text"))
                        .result()
                        .map(text -> text.getMessages(false));
            }

            if (nbtCompound.contains("back_text")) {
                back = SignText.CODEC
                        .parse(NbtOps.INSTANCE, nbtCompound.getCompound("back_text"))
                        .result()
                        .map(text -> text.getMessages(false));
            }

            if (front.isEmpty() && back.isEmpty()) {
                return;
            }
            if (!front.isEmpty()) {
                Text[] tl = front.orElse(null);
                tooltip.add(Text.literal("Front:"));
                for (Text t : tl) {
                    tooltip.add(t);
                   
                }
            }
            if (!back.isEmpty()) {
                Text[] tl = back.orElse(null);
                tooltip.add(Text.literal("Back:"));
                for (Text t : tl) {
                    tooltip.add(t);
                }
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
        if (ModConfig.disableSignEditOnPlace) info.cancel();
    }
}
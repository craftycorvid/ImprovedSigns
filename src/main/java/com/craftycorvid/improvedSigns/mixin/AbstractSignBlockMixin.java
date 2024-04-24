package com.craftycorvid.improvedSigns.mixin;

import net.minecraft.block.AbstractSignBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.block.entity.SignText;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtOps;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(AbstractSignBlock.class)
public abstract class AbstractSignBlockMixin extends BlockWithEntity {
    protected AbstractSignBlockMixin(Settings settings) {
        super(settings);
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        super.onPlaced(world, pos, state, placer, itemStack);
        NbtComponent nbtComponent = itemStack.get(DataComponentTypes.CUSTOM_DATA);
        if (nbtComponent != null && nbtComponent.contains("BlockEntityTag")) {
            NbtCompound nbtCompound = nbtComponent.copyNbt().getCompound("BlockEntityTag");
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof SignBlockEntity signBlockEntity) {
                signBlockEntity.setText(SignText.CODEC.parse(NbtOps.INSTANCE, nbtCompound.getCompound("front_text")).result().orElse(new SignText()), true);
                signBlockEntity.setText(SignText.CODEC.parse(NbtOps.INSTANCE, nbtCompound.getCompound("back_text")).result().orElse(new SignText()), false);
                signBlockEntity.setWaxed(nbtCompound.getBoolean("is_waxed"));
            }
        }
    }
}

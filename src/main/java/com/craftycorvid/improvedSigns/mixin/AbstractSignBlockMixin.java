package com.craftycorvid.improvedSigns.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.NbtOps;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.SignBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.entity.SignText;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(SignBlock.class)
public abstract class AbstractSignBlockMixin extends BaseEntityBlock {
        protected AbstractSignBlockMixin(Properties settings) {
                super(settings);
        }

        @Override
        public void setPlacedBy(Level world, BlockPos pos, BlockState state,
                        @Nullable LivingEntity placer, ItemStack itemStack) {
                super.setPlacedBy(world, pos, state, placer, itemStack);
                itemStack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY)
                                .copyTag().getCompound("BlockEntityTag").ifPresent(nbtCompound -> {
                                        BlockEntity blockEntity = world.getBlockEntity(pos);
                                        if (blockEntity instanceof SignBlockEntity signBlockEntity) {
                                                signBlockEntity.setText(SignText.DIRECT_CODEC.parse(
                                                                NbtOps.INSTANCE,
                                                                nbtCompound.getCompoundOrEmpty(
                                                                                "front_text"))
                                                                .result().orElse(new SignText()),
                                                                true);
                                                signBlockEntity.setText(SignText.DIRECT_CODEC.parse(
                                                                NbtOps.INSTANCE,
                                                                nbtCompound.getCompoundOrEmpty(
                                                                                "back_text"))
                                                                .result().orElse(new SignText()),
                                                                false);
                                                signBlockEntity.setWaxed(
                                                                nbtCompound.getBoolean("is_waxed")
                                                                                .orElse(false));
                                        }
                                });
        }
}

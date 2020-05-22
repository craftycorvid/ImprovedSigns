package com.ivanff.signEditor.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.block.entity.SignBlockEntity;

@Mixin(SignBlockEntity.class)
public interface SignEntityMixin {
    @Accessor
    void setEditable(boolean editable);
}
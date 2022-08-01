package com.ivanff.improvedSigns.mixin;

import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import com.ivanff.improvedSigns.ISignBlockEntity;

@Mixin(SignBlockEntity.class)
public class SignBlockEntityMixin implements ISignBlockEntity {

    @Shadow @Final private Text[] texts;

    public Text getTextOnRow(int row) {
        return this.texts[row];
    }
}
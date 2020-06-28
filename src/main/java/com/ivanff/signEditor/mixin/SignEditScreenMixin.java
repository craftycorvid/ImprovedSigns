package com.ivanff.signEditor.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.client.gui.screen.ingame.SignEditScreen;

@Mixin(SignEditScreen.class)
public abstract class SignEditScreenMixin {
    @Shadow
    private SignBlockEntity sign;

    @Shadow
    private String[] field_24285;

    @Inject(at = @At("HEAD"), method = "init")
    private void init(final CallbackInfo info) {
        for (int i = 0; i < 4; i++) {
            this.field_24285[i] = this.sign.getTextBeingEditedOnRow(i, s -> s).getString();
        }
    }
}
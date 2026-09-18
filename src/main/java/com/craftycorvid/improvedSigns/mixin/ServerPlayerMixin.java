package com.craftycorvid.improvedSigns.mixin;

import static com.craftycorvid.improvedSigns.ImprovedSignsMod.MOD_CONFIG;

import com.craftycorvid.improvedSigns.ImprovedSignsUtils;
import com.craftycorvid.improvedSigns.SignPlacement;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.entity.SignTextSlot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin {
    // Placing a sign opens the sign editor. Skip it when the sign was placed with text already on
    // it (a copied or retained sign), or when the config disables the editor on placement. Editing
    // an existing sign by clicking it is unaffected, as no placement is in progress then.
    @Inject(method = "openTextEdit(Lnet/minecraft/world/level/block/entity/SignBlockEntity;Lnet/minecraft/world/level/block/entity/SignTextSlot;)V",
            at = @At("HEAD"), cancellable = true)
    private void improvedSigns$onOpenTextEdit(SignBlockEntity sign, SignTextSlot slot,
            CallbackInfo info) {
        SignPlacement.Placement placement = SignPlacement.current();
        if (placement == null || !placement.pos().equals(sign.getBlockPos()))
            return;
        if (MOD_CONFIG.disableSignEditOnPlace || ImprovedSignsUtils.hasSignText(placement.stack()))
            info.cancel();
    }
}

package com.ivanff.signEditor;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.entity.player.PlayerEntity;

public interface SignEditorCallback {
    Event<SignEditorCallback> EVENT = EventFactory.createArrayBacked(SignEditorCallback.class,
        (listeners) -> (player, signBlock) -> {
            for (SignEditorCallback listener : listeners) {
                listener.usedSign(player, signBlock);
            }
    });
 
    void usedSign(PlayerEntity player, SignBlockEntity signBlock);
}